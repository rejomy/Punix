package me.rejomy.punix;

import lombok.Getter;
import me.rejomy.punix.command.PunixCommand;
import me.rejomy.punix.manager.FileManager;
import me.rejomy.punix.util.FileUtil;
import me.rejomy.punix.util.TimeUtil;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {
    @Getter
    private static Main INSTANCE;
    @Getter
    private FileManager fileManager;

    @Override
    public void onEnable() {
        INSTANCE = this;

        fileManager = new FileManager();
        fileManager.load();

        new FileUtil().clearOldFiles(new File(getDataFolder() + "/users"),
                TimeUtil.getTimeToMillis(getConfig().getString("clear-player-data-after")));

        getCommand("punix").setExecutor(new PunixCommand());
    }

    @Override
    public void onDisable() {

    }

}