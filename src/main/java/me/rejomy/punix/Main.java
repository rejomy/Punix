package me.rejomy.punix;

import me.rejomy.punix.command.PunixCommand;
import me.rejomy.punix.util.FileUtil;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main INSTANCE;

    public static Main getInstance() {
        return INSTANCE;
    }

    public FileUtil violationFile;
    public FileUtil logFile;

    @Override
    public void onLoad() {
        INSTANCE = this;

        violationFile = new FileUtil("violations", getDataFolder());

        logFile = new FileUtil("logs", getDataFolder());

        saveDefaultConfig();
    }

    @Override
    public void onDisable() {

        violationFile.saveYamlIntoFile();
        logFile.saveYamlIntoFile();

    }

    @Override
    public void onEnable() {
        getCommand("punix").setExecutor(new PunixCommand());

    }

}