package me.rejomy.punix.command;

import me.rejomy.punix.Main;
import me.rejomy.punix.util.ColorUtil;
import me.rejomy.punix.util.FileUtil;
import me.rejomy.punix.util.MessageUtil;
import me.rejomy.punix.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PunixCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length <= 2) {
            MessageUtil.sendMessage(
                    Main.getINSTANCE().getFileManager().getMessageConfig().getCOMMAND_HELP_MESSAGE(), sender);
            return false;
        }

        String playerName = args[0],
                group = args[1],
                reason = Arrays.stream(args).skip(3).collect(Collectors.joining(" "));

        File file = FileUtil.getFileFromDirectory(Main.getINSTANCE().getFileManager().getUsersDirectory(),
                playerName + ".yml");

        int currentViolation, maxVl = 0, violationForAdd = getIntFromString(args[2]);

        if (violationForAdd == -1) {
            return false;
        }

        if (!file.exists()) {
            FileUtil.create(file);
        }

        YamlConfiguration user = YamlConfiguration.loadConfiguration(file);

        {
            Object value = user.get("violation." + group + ".level");

            if(group != null) {
                spoofVl(user, group);
            }

            currentViolation = value == null ? 0 : (int) value;
        }

        if (Main.getINSTANCE().getConfig().getBoolean("log-in-console")) {
            Main.getINSTANCE().getLogger().info(playerName + " get flag for " + group + " " + reason
                    + " vl=" + currentViolation + " + " + violationForAdd);
        }

        for (String configVlString : Main.getINSTANCE().getConfig().getConfigurationSection("action." + args[1])
                .getKeys(false)) {

            int configVl;

            try {
                configVl = Integer.parseInt(configVlString);
            } catch (NumberFormatException exception) {
                continue;
            }

            if (configVl > maxVl) {
                maxVl = configVl;
            }

            if (configVl > currentViolation && configVl <= currentViolation + violationForAdd) {
                for (String command : Main.getINSTANCE().getConfig()
                        .getStringList("action." + group + "." + configVlString)) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                            ColorUtil.toColor(command
                                    .replace("$player", args[0])
                                    .replace("$reason", reason)));
                }
            }

        }

        Object maybeLogs = user.get("logs." + group);
        List<String> logs = maybeLogs != null? (List<String>) maybeLogs : new ArrayList<>();
        String logToAdd = TimeUtil.getCurrentDate() + " | " + reason + " vl=" + currentViolation + violationForAdd;
        logs.add(logToAdd);

        user.set("logs." + group, logs);

        user.set("violation." + group + ".level",
                currentViolation + violationForAdd >= maxVl ? 0 : currentViolation + violationForAdd);
        user.set("violation." + group + ".time",
                System.currentTimeMillis());

        FileUtil.save(user, file);

        return false;
    }

    int getIntFromString(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException exception) {
            Main.getINSTANCE().getLogger().severe("Error when parsing a string " + string + " into a number: "
                    + exception.getMessage());
        }

        return -1;
    }

    private void spoofVl(YamlConfiguration config, String group) {
        long needTimeToSpoof = TimeUtil.getTimeToMillis(Main.getINSTANCE().getConfig()
                .getString("action." + group + ".reduce-after"));

        long time = config.getLong("violation." + group + ".time");

        int vlToSpoof = (int) ((System.currentTimeMillis() - time) / needTimeToSpoof);

        if(vlToSpoof > 0) {
            config.set("violation." + group + ".level", Math.max(0,
                    config.getLong("violation." + group+  ".level") - vlToSpoof));
        }
    }

}
