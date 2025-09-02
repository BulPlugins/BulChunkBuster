package com.alihaine.bulchunkbuster;

import com.alihaine.bulchunkbuster.file.BusterConfig;
import com.alihaine.bulchunkbuster.listener.onBlockPlace;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ChunkBuster extends JavaPlugin {

    public static final String PREFIX = "[BulChunkBuster] ";
    private static ChunkBuster chunkBuster;
    private static BusterConfig busterConfig;

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("--------------------Starting to enable BulChunkBuster--------------------");
        chunkBuster = this;

        busterConfig = new BusterConfig(this);
        Bukkit.getPluginManager().registerEvents(new onBlockPlace(), this);

        Bukkit.getLogger().info("--------------------BulChunkBuster plugin has been enabled--------------------");
    }

    public static ChunkBuster getChunkBuster() { return chunkBuster; }

    public static BusterConfig getBusterConfig() { return busterConfig; }
}
