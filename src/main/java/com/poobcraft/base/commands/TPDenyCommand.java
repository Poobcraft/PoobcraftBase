package com.poobcraft.base.commands;

import com.poobcraft.base.util.PBCommand;
import com.poobcraft.base.util.TeleportRequestManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPDenyCommand extends PBCommand {
    public TPDenyCommand() {
        super("tpdeny", "poobcraft.tpa");
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

        TeleportRequestManager.deny(player);
    }

    public static void register() {
        new TPDenyCommand().registerCommand();
    }
}
