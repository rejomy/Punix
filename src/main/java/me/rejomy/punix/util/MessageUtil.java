package me.rejomy.punix.util;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class MessageUtil {
    public static void sendMessage(String message, CommandSender sender) {
        if(message.isEmpty()) {
            return;
        }

        sender.sendMessage(message);
    }

    public static void sendMessage(List<String> messages, CommandSender sender) {
        if(messages.isEmpty()) {
            return;
        }

        for(String message : messages) {
            sender.sendMessage(message);
        }
    }
}
