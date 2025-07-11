package com.poobcraft.base;

import com.poobcraft.base.commands.GraveCommand;
import com.poobcraft.base.commands.HelpCommand;
import com.poobcraft.base.commands.HomeCommand;
import com.poobcraft.base.commands.SpawnCommand;
import com.poobcraft.base.commands.admin.*;
import com.poobcraft.base.graves.GraveListener;
import com.poobcraft.base.graves.GraveManager;
import com.poobcraft.base.permissions.PoobPerms;
import org.bukkit.plugin.java.JavaPlugin;

public class PoobcraftBase extends JavaPlugin {

    private static PoobcraftBase instance;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        saveResource("permissions.yml", false);
        saveResource("graves.yml", false);

        // Load custom permissions
        PoobPerms.reload();

        // Grave system
        GraveManager.init();
        getServer().getPluginManager().registerEvents(new GraveListener(), this);
        GraveCommand.register();

        // Commands
        HelpCommand.register();
        PBReloadCommand.register();
        PromoteCommand.register();
        DemoteCommand.register();
        HomeCommand.register();
        SpawnCommand.register();
        SetSpawnCommand.register();
        BackupCommand.register();

        getLogger().info("✅ PoobcraftBase enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("❌ PoobcraftBase disabled.");
    }

    public static PoobcraftBase getInstance() {
        return instance;
    }
}
