package dev.interfiber.karpet.installer;

import javax.swing.*;

public class ProgressFrame {

    public JTextArea console;

    public JFrame windowFrame;

    public void start(){
        windowFrame = new JFrame();
        windowFrame.setTitle("Karpet log");
        windowFrame.setSize(400, 400);
        windowFrame.setResizable(false);
        this.console = new JTextArea();
        this.console.append("Opened logging window\n");
        this.console.setEditable(false);
        windowFrame.add(this.console);
        windowFrame.setVisible(true);
    }

    public void logInfo(String message){
        this.console.append("[INFO] " + message + "\n");
    }

    public void logWarn(String message){
        this.console.append("[WARN] " + message + "\n");
    }

    public void logError(String message){
        this.console.append("[ERROR] " + message + "\n");
    }
}
