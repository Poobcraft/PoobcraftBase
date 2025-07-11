package com.poobcraft.base.commands.admin;

import com.poobcraft.base.PoobcraftBase;
import com.poobcraft.base.permissions.PoobPerms;
import com.poobcraft.base.util.PBCommand;
import org.bukkit.command.CommandSender;

public class PBReloadCommand extends PBCommand {

    public PBReloadCommand() {
        super("pbreload", "poobcraft.reload");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        PoobcraftBase.getInstance().reloadConfig();
        PoobPerms.reload();
        sender.sendMessage("§aPoobcraftBase config and permissions reloaded.");
    }

    public static void register() {
        new PBReloadCommand().registerCommand();
    }
}
