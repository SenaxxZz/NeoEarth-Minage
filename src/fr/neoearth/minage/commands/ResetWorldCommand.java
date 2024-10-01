package fr.neoearth.minage.commands;

import fr.neoearth.minage.ResetWorld;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ResetWorldCommand implements CommandExecutor {
    private final ResetWorld resetWorld;

    public ResetWorldCommand(JavaPlugin plugin) {
        this.resetWorld = new ResetWorld(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("world")) {
            resetWorld.reset();
            return true;
        }
        return false;
    }
}