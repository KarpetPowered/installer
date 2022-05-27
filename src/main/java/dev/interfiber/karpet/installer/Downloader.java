package dev.interfiber.karpet.installer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Downloader extends Thread{
    public void run(ProgressFrame progressFrame, String downloadURL) throws IOException {
        System.out.println("Downloader thread: Started");
        URL fetchWebsite = new URL(downloadURL);
        ReadableByteChannel readableByteChannel = Channels.newChannel(fetchWebsite.openStream());

        try (FileOutputStream fos = new FileOutputStream("/tmp/karpet-server.jar")) {
            fos.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            progressFrame.logError(e.toString());
        }
        progressFrame.logInfo("Downloaded to /tmp/karpet-server.jar");
        System.out.println("Downloader thread: Terminating...");
    }
}
