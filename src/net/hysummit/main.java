package net.hysummit;

import net.hysummit.listeners.chunkEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin{

    @Override
    public void onEnable() {
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new chunkEvent(this), this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

}

