package me.rejomy.punix.manager;

import lombok.Getter;
import lombok.Setter;
import me.rejomy.punix.util.ColorUtil;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class ConfigHelper {
    @Getter
    @Setter
    private YamlConfiguration config;

    public List<String> getStringList(String path) {
        List<String> list = config.getStringList(path);
        list.replaceAll(ColorUtil::toColor);
        return list;
    }

    public String getString(String path) {
        return ColorUtil.toColor(path);
    }
}
