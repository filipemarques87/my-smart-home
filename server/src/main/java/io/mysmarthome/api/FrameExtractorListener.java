package io.mysmarthome.api;

@FunctionalInterface
public interface FrameExtractorListener {
    void onFrame(byte[] frame);
}
