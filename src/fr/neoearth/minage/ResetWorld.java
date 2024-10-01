package fr.neoearth.minage;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.sk89q.worldedit.schematic.SchematicFormat;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;

public class ResetWorld {
    private final JavaPlugin plugin;
    private boolean shouldPasteSchematic = false;

    public ResetWorld(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void reset() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.kickPlayer("Server is resetting.");
        }


        Bukkit.setWhitelist(true);
        plugin.getLogger().info("Whitelist enabled.");

        World world = Bukkit.getWorld("world");
        if (world != null) {
            Bukkit.unloadWorld(world, false);
            deleteWorldFolder(new File(Bukkit.getWorldContainer(), "world"));
        }

        shouldPasteSchematic = true;
        plugin.getConfig().set("shouldPasteSchematic", true);
        plugin.saveConfig();


        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getServer().shutdown();
            }
        }.runTaskLater(plugin, 20L);
    }

    private void deleteWorldFolder(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteWorldFolder(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }
        path.delete();
    }

    public void onServerStart() {
        shouldPasteSchematic = plugin.getConfig().getBoolean("shouldPasteSchematic", false);
        if (shouldPasteSchematic) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    String worldName = "world";
                    String schematicPath = "/home/container/schematic/spawn.schematic";
                    pasteSchematic(worldName, schematicPath);
                    Bukkit.setWhitelist(false);
                    plugin.getLogger().info("Whitelist disabled.");

                    // Teleport players to spawn
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.teleport(Bukkit.getWorld(worldName).getSpawnLocation());
                    }

                    // Reset flag
                    shouldPasteSchematic = false;
                    plugin.getConfig().set("shouldPasteSchematic", false);
                    plugin.saveConfig();
                }
            }.runTaskLater(plugin, 200L);
        }
    }

    private void pasteSchematic(String worldName, String schematicPath) {
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            File schematicFile = new File(schematicPath);
            if (!schematicFile.exists()) {
                plugin.getLogger().severe("Le fichier schematic n'existe pas!");
                return;
            }

            try {
                SchematicFormat format = MCEditSchematicFormat.getFormat(schematicFile);
                if (format == null) {
                    plugin.getLogger().severe("Format de fichier schematic non supporté!");
                    return;
                }

                CuboidClipboard clipboard = format.load(schematicFile);
                EditSession editSession = new EditSession(BukkitUtil.getLocalWorld(world), -1);

                plugin.getLogger().info("Pasting schematic...");
                clipboard.paste(editSession, new Vector(0, 180, 0), false);
                world.setSpawnLocation(0, 180, 0);
                plugin.getLogger().info("Le spawn a été collé avec succès et le point de spawn a été défini.");
            } catch (IOException | com.sk89q.worldedit.data.DataException | com.sk89q.worldedit.MaxChangedBlocksException e) {
                plugin.getLogger().severe("Une erreur s'est produite lors du collage du spawn!");
                e.printStackTrace();
            }
        } else {
            plugin.getLogger().severe("Le monde " + worldName + " n'existe pas.");
        }
    }
}