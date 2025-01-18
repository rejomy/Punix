package me.rejomy.punix.manager;

import lombok.Getter;
import me.rejomy.punix.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

@Getter
public class MessageConfig extends ConfigHelper {
    List<String> COMMAND_HELP_MESSAGE;

    public MessageConfig() {
        File file = new File(Main.getINSTANCE().getDataFolder(), "message.yml");

        if(!file.exists()) {
            Main.getINSTANCE().saveResource("message.yml", false);
        }

        setConfig(YamlConfiguration.loadConfiguration(file));

        load();
    }

    public void load() {
        COMMAND_HELP_MESSAGE = getStringList("commands.help");
    }
}
