package dev.interfiber.karpet.installer;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class InstallerHandler {

    public static void StartInstall(String url, String installPath) throws IOException {
        if (!new File(installPath).exists()){
            System.out.println("Creating install folder");
            new File(installPath).mkdir();
        }
        System.out.println("Creating output window...");
        ProgressFrame progressFrame = new ProgressFrame();
        progressFrame.start();
        progressFrame.logInfo("Getting download URL");
            progressFrame.logInfo(url);
            progressFrame.logInfo("Fetching server jar...");
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    Downloader downloader = new Downloader();
                    try {
                        downloader.setDaemon(true);
                        downloader.run(progressFrame, url);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    progressFrame.logInfo("Installing server...");
                    String path = installPath;
                    progressFrame.logInfo("Copying /tmp/karpet-server.jar...");
                    new File("/tmp/karpet-server.jar").renameTo(new File(path + "/karpet-server.jar"));
                    progressFrame.logInfo("Creating start script...");
                    try {
                        FileWriter fileWriter = new FileWriter(path + "/start.sh");
                        fileWriter.write("#!/usr/bin/env bash\njava -Xmx2G -jar karpet-server.jar");
                        fileWriter.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    progressFrame.logInfo("Server install complete");
                    JOptionPane.showMessageDialog(progressFrame.windowFrame, "Server installed successfully!");
                }
            });
    }
}
