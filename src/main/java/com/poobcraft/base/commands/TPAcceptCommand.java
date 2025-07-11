package com.poobcraft.base.commands;

import com.poobcraft.base.util.PBCommand;
import com.poobcraft.base.util.TeleportRequestManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPAcceptCommand extends PBCommand {
    public TPAcceptCommand() {
        super("tpaccept", "poobcraft.tpa");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this.");
            return;
        }

        if (!TeleportRequestManager.hasRequest(player)) {
            player.sendMessage("§cYou have no pending teleport requests.");
            return;
        }

        TeleportRequestManager.accept(player);
    }

    public static void register() {
        new TPAcceptCommand().registerCommand();
    }
}
