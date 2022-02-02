package io.mysmarthome.platform;

import lombok.Builder;
import lombok.Value;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

@Value
@Builder
public class DownloadDetails implements Closeable {

    File file;
    InputStream fileSstream;

    public String getFilename() {
        return file.getName();
    }

    public long getFileSize() {
        return file.length();
    }

    public String getMimeType() {
        return URLConnection.guessContentTypeFromName(file.getName());
    }

    @Override
    public void close() {
        if (fileSstream != null) {
            try {
                fileSstream.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }
}
