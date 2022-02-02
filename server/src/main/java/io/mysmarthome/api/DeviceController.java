package io.mysmarthome.api;

import io.mysmarthome.configuration.ApplicationProperties;
import io.mysmarthome.platform.DownloadDetails;
import io.mysmarthome.service.DeviceInteraction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/device")
public class DeviceController {

    private final DeviceInteraction deviceInteraction;
    private final ApplicationProperties applicationProperties;

    @RequestMapping(value = "/{deviceId}/download/**", method = RequestMethod.GET)
    public void downloadFromDevice(@PathVariable("deviceId") String deviceId, HttpServletRequest request, HttpServletResponse response) throws IOException {
//        log.info("Receive download request for device {}: {}", deviceId, request.getRequestURI());
        String filePath = request.getRequestURI().substring(("/device/" + deviceId + "/download").length());

        DownloadDetails downloadDetails = deviceInteraction.download(deviceId, filePath);
            response.setContentType(downloadDetails.getMimeType());
            response.setHeader("Content-Disposition", String.format("inline; filename=\"%s\"", downloadDetails.getFilename()));
            response.setContentLength((int) downloadDetails.getFileSize());
            InputStream inputStream = new BufferedInputStream(downloadDetails.getFileSstream());
            FileCopyUtils.copy(inputStream, response.getOutputStream());
    }

    @PostMapping(value = "/{deviceId}")
    public Object sentToDevice(@PathVariable("deviceId") String deviceId, @RequestBody Map<String, Object> msg) throws InterruptedException, ExecutionException, TimeoutException {
        log.info("Receive action request for device {}: {}", deviceId, msg);
        return deviceInteraction
                .send(deviceId, msg)
                .get(applicationProperties.getInt("api.timeout"), TimeUnit.SECONDS)
                .map( rm -> Map.of("data", rm.getMessage(), "lastUpdate", rm.getReceivedAt()))
                .orElseThrow(() -> new IllegalArgumentException("Not able to get response from " + deviceId));
    }
}

