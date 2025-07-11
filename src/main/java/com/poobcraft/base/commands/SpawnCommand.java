package com.poobcraft.base.commands;

import com.poobcraft.base.util.PBCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand extends PBCommand {

    public SpawnCommand() {
        super("spawn", "poobcraft.spawn");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use /spawn.");
            return;
        }

        Location spawn = Bukkit.getWorlds().get(0).getSpawnLocation();
        player.teleport(spawn);
        player.sendMessage("§aTeleported to spawn.");
    }

    public static void register() {
        new SpawnCommand().registerCommand();
    }
}
