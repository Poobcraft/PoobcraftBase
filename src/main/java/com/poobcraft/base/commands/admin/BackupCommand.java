package com.poobcraft.base.commands.admin;

import com.poobcraft.base.util.PBCommand;
import com.poobcraft.base.util.BackupManager;
import org.bukkit.command.CommandSender;

public class BackupCommand extends PBCommand {

    public BackupCommand() {
        super("backup", "poobcraft.backup");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        BackupManager.createBackup(sender, true);
    }

    public static void register() {
        new BackupCommand().registerCommand();
    }
}
