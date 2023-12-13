package net.hysummit.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class utils {

    public static String chat(String s){
        return ChatColor.translateAlternateColorCodes('&',s);
    }

    public static ItemStack createItem(Material material, int amount, String displayName, String... loreString){
        ItemStack item;
        List<String> lore = new ArrayList<>();

        item = new ItemStack(material,amount);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(utils.chat(displayName));
        for (String s : loreString){
            lore.add(utils.chat(s));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    public static void droppedLog(World w, int x, int y, int z, ItemStack item){
        Logger log = Bukkit.getLogger();
        log.info(item.getType()+" has been DROPPED in "+w.getName()+" at: "+x+","+y+","+z);
    }

    public static void deletedLog(World w, int x, int y, int z, ItemStack item){
        Logger log = Bukkit.getLogger();
        log.info(item.getType()+" has been DELETED in "+w.getName()+" at: "+x+","+y+","+z);
    }
}
