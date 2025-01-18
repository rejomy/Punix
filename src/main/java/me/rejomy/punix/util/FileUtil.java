package me.rejomy.punix.util;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class FileUtil {
    public void clearOldFiles(File directory, long timeInMillis) {
        if(!directory.exists()) return;

        for(File file : directory.listFiles()) {
            if(System.currentTimeMillis() - file.lastModified() < timeInMillis) continue;

            file.delete();
        }
    }

    public static void create(File file) {
        try {
            file.createNewFile();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void save(YamlConfiguration config, File file) {
        try {
            config.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static File getFileFromDirectory(File directory, String fileName) {
        return Arrays.stream(directory.listFiles())
                .filter(file -> file.getName().equalsIgnoreCase(fileName))
                .findAny().orElse(new File(directory, fileName));
    }
}
