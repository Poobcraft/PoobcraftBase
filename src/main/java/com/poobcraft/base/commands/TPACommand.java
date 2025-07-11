package com.poobcraft.base.commands;

import com.poobcraft.base.util.PBCommand;
import com.poobcraft.base.util.TeleportRequestManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPACommand extends PBCommand {
    public TPACommand() {
        super("tpa", "poobcraft.tpa");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this.");
            return;
        }

        if (args.length != 1) {
            player.sendMessage("§7Usage: /tpa <player>");
            return;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage("§cPlayer not found or not online.");
            return;
        }

        if (target.equals(player)) {
            player.sendMessage("§cYou can’t send a teleport request to yourself.");
            return;
        }

        boolean success = TeleportRequestManager.sendRequest(player, target);
        if (success) {
            player.sendMessage("§aRequest sent to " + target.getName() + ".");
            target.sendMessage("§7" + player.getName() + " has requested to teleport to you.");
            target.sendMessage("§eType /tpaccept or /tpdeny.");
        } else {
            player.sendMessage("§cYou must wait before sending another request.");
        }
    }

    public static void register() {
        new TPACommand().registerCommand();
    }
}
