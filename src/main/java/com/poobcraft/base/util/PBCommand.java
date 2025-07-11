package com.poobcraft.base.util;

import com.poobcraft.base.PoobcraftBase;
import com.poobcraft.base.permissions.PoobPerms;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.jetbrains.annotations.NotNull;

public abstract class PBCommand {

    private final String commandName;
    private final String permission;

    public PBCommand(@NotNull String commandName, @NotNull String permission) {
        this.commandName = commandName;
        this.permission = permission;
    }

    public abstract void execute(@NotNull CommandSender sender, @NotNull String[] args);

    public void registerCommand() {
        PluginCommand command = PoobcraftBase.getInstance().getCommand(commandName);

        if (command != null) {
            command.setExecutor(this::onCommand);
        } else {
            PoobcraftBase.getInstance().getLogger().warning("⚠ Could not register command: /" + commandName + " (not defined in plugin.yml?)");
        }
    }

    private boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String label,
            @NotNull String[] args
    ) {
        if (!PoobPerms.has(sender, permission)) {
            sender.sendMessage("§cYou don’t have permission to use this command.");
            return true;
        }

        execute(sender, args);
        return true;
    }
}
