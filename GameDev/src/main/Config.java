package main;

import java.io.*;

public class Config {
    GamePanel gp;
    File configFile;

    public Config(GamePanel gp) {
        this.gp = gp;
        this.configFile = resolveConfigFile();
    }

    private File resolveConfigFile() {
        File f1 = new File("config.txt");
        if (f1.exists()) {
            return f1;
        }
        File f2 = new File("GameDev", "config.txt");
        if (f2.exists()) {
            return f2;
        }
        return f1; // default to config.txt in current working directory
    }

    public void saveConfig() {
        try {
            if (configFile == null) {
                configFile = resolveConfigFile();
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(configFile));

            // Full Screen
            if (gp.fullScreenOn) {
                bw.write("On");
            } else {
                bw.write("Off");
            }
            bw.newLine();

            // Music Volume
            bw.write(String.valueOf(gp.music.volumeScale));
            bw.newLine();

            // SE Volume
            bw.write(String.valueOf(gp.se.volumeScale));
            bw.newLine();

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfig() {
        configFile = resolveConfigFile();
        if (!configFile.exists()) {
            // Default settings if file does not exist yet (prevents crashing when run from different directories)
            gp.fullScreenOn = false;
            saveConfig();
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(configFile))) {
            String s = br.readLine();
            if (s != null) {
                if (s.trim().equals("On")) {
                    gp.fullScreenOn = true;
                } else if (s.trim().equals("Off")) {
                    gp.fullScreenOn = false;
                }
            }

            // Music Volume
            s = br.readLine();
            if (s != null && !s.trim().isEmpty()) {
                gp.music.volumeScale = Integer.parseInt(s.trim());
            }

            // SE Volume
            s = br.readLine();
            if (s != null && !s.trim().isEmpty()) {
                gp.se.volumeScale = Integer.parseInt(s.trim());
            }
        } catch (Exception e) {
            System.err.println("Could not load config: " + e.getMessage());
            gp.fullScreenOn = false;
        }
    }
}