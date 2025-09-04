package com.alihaine.bulchunkbuster;

import com.alihaine.bulchunkbuster.command.GiveBuster;
import com.alihaine.bulchunkbuster.file.BusterConfig;
import com.alihaine.bulchunkbuster.listener.onBlockPlace;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ChunkBuster extends JavaPlugin {

    public static final String PREFIX = "[BulChunkBuster] ";
    private static ChunkBuster chunkBuster;
    private static BusterConfig busterConfig;

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("--------------------Starting to enable BulChunkBuster--------------------");
        new Metrics(this, 27173);
        chunkBuster = this;
        busterConfig = new BusterConfig(this);

        this.getCommand("bulchunkbuster").setExecutor(new GiveBuster());
        Bukkit.getPluginManager().registerEvents(new onBlockPlace(), this);

        Bukkit.getLogger().info("--------------------BulChunkBuster plugin has been enabled--------------------");
    }

    public static ChunkBuster getChunkBuster() { return chunkBuster; }

    public static BusterConfig getBusterConfig() { return busterConfig; }
}
