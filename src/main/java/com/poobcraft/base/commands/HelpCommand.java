package com.poobcraft.base.commands;

import com.poobcraft.base.util.PBCommand;
import com.poobcraft.base.permissions.PoobPerms;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand extends PBCommand {

    public HelpCommand() {
        super("help", null); // No permission needed to use help
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        List<String> help = new ArrayList<>();
        help.add("§8======[ §bPoobcraftBase Help §8]======");

        if (PoobPerms.has(sender, "poobcraft.home")) {
            help.add("§7/home §8- §fTeleport to your home");
        }
        if (PoobPerms.has(sender, "poobcraft.spawn")) {
            help.add("§7/spawn §8- §fTeleport to spawn");
        }
        if (PoobPerms.has(sender, "poobcraft.grave")) {
            help.add("§7/grave §8- §fView location of your most recent grave");
        }

        if (PoobPerms.has(sender, "poobcraft.promote")) {
            help.add("§7/promote <player> §8- §fPromote a player");
        }
        if (PoobPerms.has(sender, "poobcraft.demote")) {
            help.add("§7/demote <player> §8- §fDemote a player");
        }

        if (PoobPerms.has(sender, "poobcraft.setspawn")) {
            help.add("§7/setspawn §8- §fSet the spawn location");
        }
        if (PoobPerms.has(sender, "poobcraft.backup")) {
            help.add("§7/backup §8- §fManually create a server backup");
        }
        if (PoobPerms.has(sender, "poobcraft.reload")) {
            help.add("§7/pbreload §8- §fReload plugin config and permissions");
        }

        if (help.size() == 1) {
            help.add("§7You do not have access to any commands.");
        }

        for (String line : help) {
            sender.sendMessage(line);
        }
    }

    public static void register() {
        new HelpCommand().registerCommand();
    }
}
