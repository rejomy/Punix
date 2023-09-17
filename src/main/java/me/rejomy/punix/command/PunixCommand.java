package me.rejomy.punix.command;

import me.rejomy.punix.Main;
import me.rejomy.punix.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.stream.Collectors;

public class PunixCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length <= 2) {

            sender.sendMessage(BAD_ARGS_MESSAGE);
            return false;

        }

        String reason = Arrays.stream(args).skip(3).collect(Collectors.joining(" "));

        int currentViolation = 0;

        try {
            // get violation in config
            currentViolation = Main.getInstance().violationFile.configuration.getInt(args[0] + ".violation");

            // get amount after last vl adding time
            float spoofVlCount = (System.currentTimeMillis() -
                    Main.getInstance().violationFile.configuration.getLong(args[0] + ".time"))
                    / 1000f // to second
                    / 60 // to minutes
                    / 60 // to hourse
                    / Main.getInstance().getConfig().getInt("remove violation after the hours");

            if(spoofVlCount >= 1) {
                currentViolation = currentViolation - spoofVlCount < 0? 0 : (int) (currentViolation - spoofVlCount);
            }

        } catch (NullPointerException ignored) {}

        int violationForAdd = Integer.parseInt(args[2]);

        int maxVl = 0;

        for (String group : Main.getInstance().getConfig().getConfigurationSection("action").getKeys(false)) {

            if (!group.equalsIgnoreCase(args[1])) continue;

            for (String configVlString : Main.getInstance().getConfig()
                    .getConfigurationSection("action." + group).getKeys(false)) {

                int configVl = Integer.parseInt(configVlString);

                if(configVl > maxVl) {
                    maxVl = configVl;
                }

                if (configVl > currentViolation && configVl <= currentViolation + violationForAdd) {

                    for (String command : Main.getInstance().getConfig()
                            .getStringList("action." + group + "." + configVlString)) {

                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                                command
                                        .replace("$player", args[0])
                                        .replace("$reason", reason));
                    }

                }

            }

        }

        if(currentViolation + violationForAdd >= maxVl) {

            Main.getInstance().violationFile.configuration.set(args[0], null);

            return true;
        }

        Main.getInstance().logFile.configuration.set(currentViolation + violationForAdd + "." + args[0], reason);

        Main.getInstance().violationFile.configuration.set(args[0] + ".violation", currentViolation + violationForAdd);

        Main.getInstance().violationFile.configuration.set(args[0] + ".time", System.currentTimeMillis());

        return false;
    }

    private final String BAD_ARGS_MESSAGE = ColorUtil.toColor("&7PUNIX &8- &fUse &7/punix &bplayer &agroup &dvl &creason");

}
