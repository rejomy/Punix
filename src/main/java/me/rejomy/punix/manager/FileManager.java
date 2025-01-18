package me.rejomy.punix.manager;

import lombok.Getter;
import me.rejomy.punix.Main;

import java.io.File;

@Getter
public class FileManager {
    File usersDirectory = new File(Main.getINSTANCE().getDataFolder(), "users");
    MessageConfig messageConfig = new MessageConfig();
    public void load() {
        Main.getINSTANCE().saveDefaultConfig();

        usersDirectory.mkdirs();
    }
}
