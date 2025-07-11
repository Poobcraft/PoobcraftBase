package com.poobcraft.base.commands.admin;

import com.poobcraft.base.PoobcraftBase;
import com.poobcraft.base.permissions.PoobPerms;
import com.poobcraft.base.util.PBCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class DemoteCommand extends PBCommand {

    public DemoteCommand() {
        super("demote", "poobcraft.demote");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("§7Usage: /demote <player>");
            return;
        }

        String name = args[0];
        if (sender.getName().equalsIgnoreCase(name)) {
            sender.sendMessage("§cYou cannot demote yourself.");
            return;
        }

        File file = new File(PoobcraftBase.getInstance().getDataFolder(), "permissions.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        String path = "users." + name + ".rank";
        String current = config.getString(path, "default");
        String next;

        switch (current.toLowerCase()) {
            case "admin" -> next = "friend";
            case "friend" -> next = "default";
            case "default" -> {
                sender.sendMessage("§e" + name + " is already at the lowest rank.");
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
        sender.sendMessage("§aDemoted " + name + " to " + next + ".");
    }

    public static void register() {
        new DemoteCommand().registerCommand();
    }
}
