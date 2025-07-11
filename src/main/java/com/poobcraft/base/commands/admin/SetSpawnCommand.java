package com.poobcraft.base.commands.admin;

import com.poobcraft.base.util.PBCommand;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand extends PBCommand {

    public SetSpawnCommand() {
        super("setspawn", "poobcraft.setspawn");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use /setspawn.");
            return;
        }

        Location loc = player.getLocation();
        World world = loc.getWorld();

        if (world == null) {
            sender.sendMessage("§cCould not determine your world.");
            return;
        }

        world.setSpawnLocation(loc); // ✅ Correct and non-deprecated
        sender.sendMessage("§aSpawn location set to your current position.");
    }

    public static void register() {
        new SetSpawnCommand().registerCommand();
    }
}
