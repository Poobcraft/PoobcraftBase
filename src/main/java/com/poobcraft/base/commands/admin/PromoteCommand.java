package com.poobcraft.base.commands.admin;

import com.poobcraft.base.PoobcraftBase;
import com.poobcraft.base.permissions.PoobPerms;
import com.poobcraft.base.util.PBCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PromoteCommand extends PBCommand {

    public PromoteCommand() {
        super("promote", "poobcraft.promote");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("§7Usage: /promote <player>");
            return;
        }

        String name = args[0];
        if (sender.getName().equalsIgnoreCase(name)) {
            sender.sendMessage("§cYou cannot promote yourself.");
            return;
        }

        File file = new File(PoobcraftBase.getInstance().getDataFolder(), "permissions.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        String path = "users." + name + ".rank";
        String current = config.getString(path, "default");
        String next;

        switch (current.toLowerCase()) {
            case "default" -> next = "friend";
            case "friend" -> next = "admin";
            case "admin" -> {
                sender.sendMessage("§e" + name + " is already an admin.");
                return;
            }
            default -> {
                sender.sendMessage("§cUnknown current rank for " + name + ".");
                return;
            }
        }

        config.set(path, next);
        try {
            config.save(file);
        } catch (IOException e) {
            sender.sendMessage("§cFailed to save permissions.yml.");
            return;
        }

        PoobPerms.reload();
        sender.sendMessage("§aPromoted " + name + " to " + next + ".");
    }

    public static void register() {
        new PromoteCommand().registerCommand();
    }
}
