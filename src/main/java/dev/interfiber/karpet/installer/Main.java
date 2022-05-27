package dev.interfiber.karpet.installer;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.jthemedetecor.OsThemeDetector;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Objects;

public class Main {

    public static Image getImage() throws IOException {
        return ImageIO.read(Objects.requireNonNull(Main.class.getResource("/wool.png")));
    }

    public static String folderPath = "";

    public static void main(String[] args) throws UnsupportedLookAndFeelException, IOException {
        System.out.println("Checking for system color scheme...");
        System.out.println("Use --native-gui-enable to enable native gui");
        final OsThemeDetector detector = OsThemeDetector.getDetector();
        if (args.length == 1){
            if (Objects.equals(args[0], "--native-gui-enable")){
                System.out.println("Using native GUI instead of FlatLaf");
                System.out.println("Enabling better font rendering...");
                System.setProperty("awt.useSystemAAFontSettings", "on");
                System.setProperty("swing.aatext", "true");
            }
        } else if (detector.isDark()){
            System.out.println("Using dark mode");
            UIManager.setLookAndFeel( new FlatDarkLaf() );
        } else {
            System.out.println("Using light mode");
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        }

        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("Karpet Installer");
        InstallerGUI gui = new InstallerGUI();
        System.out.println("Downloading release json...");
        DownloadURLS urls = new DownloadURLS();
        for (int i = 0; i < urls.installOptions.length; i++){
            System.out.println("Added version: " + urls.installOptions[i]);
            gui.serverVersion.addItem(urls.installOptions[i]);
        }

        folderPath = System.getProperty("user.home") + "/server";
        gui.path.setText(folderPath);
        gui.installKarpetButton.addActionListener(e -> {
            try {
                InstallerHandler.StartInstall(Objects.requireNonNull(urls.versionToUrl((String) gui.serverVersion.getSelectedItem()).toString()), folderPath);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        gui.chooseFolder.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select install location:");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int status = fileChooser.showOpenDialog(gui.panel);
            if (status == JFileChooser.APPROVE_OPTION) {
                folderPath = fileChooser.getSelectedFile().getAbsolutePath();
                gui.path.setText(folderPath);
            } else {
                System.out.println("Canceled");
                JOptionPane.showMessageDialog(gui.panel, "Please select a install folder!", "Server alert", JOptionPane.WARNING_MESSAGE);

            }
        });

        gui.resetButton.addActionListener(e -> gui.serverVersion.setSelectedItem(urls.defaultVersion));

        // Logo
        ImageIcon icon = new ImageIcon(getImage().getScaledInstance(120, 120, Image.SCALE_DEFAULT));
        gui.title.setIcon(icon);
        frame.setType(Window.Type.NORMAL);
        frame.setContentPane(gui.panel);
        frame.setIconImage(getImage());
        frame.setSize(300, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}