package fr.neoearth.minage;

import fr.neoearth.minage.commands.ResetWorldCommand;
import fr.neoearth.minage.listener.PlayerJoinListener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private ResetWorld resetWorld;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        resetWorld = new ResetWorld(this);
        getCommand("resetworld").setExecutor(new ResetWorldCommand(this));
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);

        resetWorld.onServerStart();
    }

    @Override
    public void onDisable() {
    }
}