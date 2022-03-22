package io.mysmarthome;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.atomic.AtomicBoolean;


@Slf4j
@EnableScheduling
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
//static int i = 0;
//    public static void main(String[] args) throws IOException {
//
//        FrameExtractor frameExtractor = new FrameExtractor("/home/filipe/test/20210624_205804.mp4");
//        frameExtractor.setFrameExtractorListener(frame -> {
//            FileChannel fc = null;
//            try {
//System.out.println(frame.length);
//
//                    fc = new FileOutputStream("data"+i+".jpg").getChannel();
//                    fc.write(ByteBuffer.wrap(frame, 0, frame.length));
//                    fc.close();
//                if (++i == 10) {
//                    frameExtractor.stop();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//        frameExtractor.run();
//    }
}

//@FunctionalInterface
//interface FrameExtractorListener {
//    void onFrame(byte[] frame);
//}
//
//@RequiredArgsConstructor
//class FrameExtractor implements Runnable {
//
//    private static final int BUFFER_SIZE = 4_000;
//    private static final int FRAME_SIZE = 512_000;
//
//    private final String url;
//    private final AtomicBoolean isAlive = new AtomicBoolean(false);
//
//    private Process process;
//    private FrameExtractorListener listener;
//
//    public void setFrameExtractorListener(FrameExtractorListener listener) {
//        this.listener = listener;
//    }
//
//    private String[] buildCommand() {
//        return new String[]{
//                "ffmpeg",
//                "-i", url, // input
//                "-nostdin", // no ask to user input
//                "-f", "singlejpeg", // convert to jpg
//                "-"}; // redirect output to stdout
//    }
//
//    private synchronized boolean isAlive(Process p) {
//        try {
//            p.exitValue();
//            return false;
//        } catch (IllegalThreadStateException e) {
//            return isAlive.get();
//        }
//    }
//
//    @SneakyThrows
//    @Override
//    public void run() {
//        ProcessBuilder builder = new ProcessBuilder(buildCommand());
//        builder.redirectErrorStream(true); // so we can ignore the error stream
//        process = builder.start();
//        isAlive.set(true);
//        InputStream out = process.getInputStream();
//
//        int bufferLen = 0;
//        int idx = 0;
//        byte[] bufferImg = new byte[FRAME_SIZE];
//        byte[] buffer = new byte[BUFFER_SIZE];
//        boolean processingFrame = false;
//
//        while (isAlive(process)) {
//            int no = out.available();
//            if (no > 0) {
//                int n = out.read(buffer, 0, Math.min(no, buffer.length));
//
//                if (!processingFrame) {
//                    for (int i = 0; i < n - 2; i++) {
//                        if (buffer[i] == (byte) 0xff && buffer[i + 1] == (byte) 0xD8 && buffer[i + 2] == (byte) 0xFF) {
//                            int len = n - i;
//                            bufferLen = len;
//                            System.arraycopy(buffer, i, bufferImg, idx, len);
//                            idx = bufferLen;
//                            processingFrame = true;
////                            break;
//
////                            if (listener != null) {
//////                                listener.onFrame(ByteBuffer.wrap(bufferImg, 0, bufferLen).array());
////                                byte[] frame = new byte[bufferLen];
////                                System.arraycopy(bufferImg, 0, frame, 0, bufferLen);
////                                listener.onFrame(frame);
////                            }
////                            processingFrame = false;
//                        }
//                    }
////                    if (n == BUFFER_SIZE) {
////                        continue;
////                    }
//                    continue;
//                }
//
//                if (processingFrame) {
//                    for (int i = 0; i < n - 1; i++) {
//                        if (buffer[i] == (byte) 0xff && buffer[i + 1] == (byte) 0xD9) {
//                            int len = n - i;
//                            bufferLen += i+2/*len*/;
//                            System.arraycopy(buffer, 0, bufferImg, idx, i+2);
//                            idx=0;
//
//                            if (listener != null) {
////                                listener.onFrame(ByteBuffer.wrap(bufferImg, 0, bufferLen).array());
//                                byte[] frame = new byte[bufferLen];
//                                System.arraycopy(bufferImg, 0, frame, 0, bufferLen);
//                                listener.onFrame(frame);
//                            }
//
//                            processingFrame = false;
//                            break;
//                        }
//                    }
//                }
//
//                if (processingFrame) {
//                    bufferLen += n;
//                    System.arraycopy(buffer, 0, bufferImg, idx, n);
//                    idx = bufferLen;
//                }
//            }
//        }
//    }
//
//    public synchronized void stop() {
//        process.destroy();
//        isAlive.set(false);
//    }
//}