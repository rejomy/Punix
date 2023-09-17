package me.rejomy.punix.util;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileUtil {

    public File file;
    public YamlConfiguration configuration;

    public FileUtil(String fileName, File pathToFile) {
        file = new File(pathToFile, fileName + ".yml");

        if(!file.exists()) {
            createNewFile();
        }

        configuration = YamlConfiguration.loadConfiguration(file);

    }

    public void createNewFile() {

        try {
            file.createNewFile();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

    public void saveYamlIntoFile() {

        try {
            configuration.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

}
