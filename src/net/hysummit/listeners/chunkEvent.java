package net.hysummit.listeners;

import net.hysummit.main;
import net.hysummit.utils.utils;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

public class chunkEvent implements Listener {

    private final main plugin;

    public chunkEvent(main plugin) {
        this.plugin = plugin;
    }

    private void remove(final Location location, final ItemStack droppedItem, final int limit) {
        new BukkitRunnable() {
            int timer = 0;

            @Override
            public void run() {
                timer++;
                if (timer >= limit) {
                    cancel();
                    final World world = location.getWorld();
                    for (Entity entity : location.getChunk().getEntities()) {
                        if (entity instanceof Item item) {
                            ItemStack stack = item.getItemStack();
                            if (stack.isSimilar(droppedItem)){
                                item.remove();
                                utils.deletedLog(world, location.getBlockX(), location.getBlockY(), location.getBlockZ(), droppedItem);
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }


    @EventHandler
    public void onChunkEvent(ChunkLoadEvent e){

        if(e.isNewChunk()){ //If the loaded chunk is a new chunk i.e. New Chunk just been generated:

            //Get the Chance from the config file
            FileConfiguration config = plugin.getConfig();
            int spawnChance = config.getInt("loot-drop-chance");
            int despawnTime = config.getInt("loot-despawn-time");

            //Roll for success
            Random rand = new Random();
            int upper = 101; //Rolls a number between 0-100
            int roll = rand.nextInt(upper);

            //If successful spawn a diamond at a random location on the surface of the chunk
            if (roll<=spawnChance){

                //Scan through the whole chunk finding coordinates for surface levels
                Chunk currentChunk = e.getChunk();
                int chunkX = currentChunk.getX();
                int chunkZ = currentChunk.getZ();

                int max = 15;
                int offSetX = rand.nextInt(max);
                int dropXlocation = (chunkX*16)+offSetX;

                int offSetZ = rand.nextInt(max);
                int dropZlocation = (chunkZ*16)+offSetZ;

                int dropYlocation = currentChunk.getWorld().getHighestBlockYAt(dropXlocation,dropZlocation);

                //Create the ItemStack with Meta
                ItemStack droppedItem = utils.createItem(Material.DIAMOND,1,"&b&lMysterious Diamond","&7&oA Mysterious Diamond found","&7&orandomly scattered across the land");

                //Parse the location from randomly selected into a Location type
                Location droppedLocation = new Location(currentChunk.getWorld(),dropXlocation,dropYlocation,dropZlocation);

                //Drop ItemStack at Location
                currentChunk.getWorld().dropItemNaturally(droppedLocation,droppedItem);

                //Using `Log Level Info`, log the world name, coordinates and item type spawned.
                utils.droppedLog(currentChunk.getWorld(),dropXlocation,dropYlocation,dropZlocation,droppedItem);

                //Pass spawned ITEMSTACK into bukkit runnable for a 60second timer
                remove(droppedLocation, droppedItem,despawnTime);

                }
            }
        }
    }
