package fr.neoearth.minage.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Location spawnLocation = new Location(Bukkit.getWorld("world"), -15, 157, -15); // Changer les coordonnées si besoin
        event.getPlayer().teleport(spawnLocation);
    }
}