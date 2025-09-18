package com.alihaine.bulchunkbuster;

import com.alihaine.bulchunkbuster.buster.BlockBusterManager;
import com.alihaine.bulchunkbuster.command.Confirmation;
import com.alihaine.bulchunkbuster.command.GiveBuster;
import com.alihaine.bulchunkbuster.file.BusterConfig;
import com.alihaine.bulchunkbuster.listener.onBlockPlace;
import com.alihaine.bulchunkbuster.listener.onFlow;
import com.alihaine.bulchunkbuster.support.SupportManager;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class ChunkBuster extends JavaPlugin {
    public static final String PREFIX = "[BulChunkBuster] ";
    private static ChunkBuster chunkBuster;
    private static BusterConfig busterConfig;
    private static BlockBusterManager blockBusterManager;
    private static SupportManager supportManager;

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("--------------------Starting to enable BulChunkBuster--------------------");
        this.updateChecker();
        new Metrics(this, 27173);
        blockBusterManager = new BlockBusterManager();
        chunkBuster = this;
        busterConfig = new BusterConfig(this);

        supportManager = new SupportManager(this.getConfig());

        this.getCommand("bulchunkbuster").setExecutor(new GiveBuster());
        if (busterConfig.getBlockBusterChatConfirmation())
            this.getCommand("busterconfirmation").setExecutor(new Confirmation());

        Bukkit.getPluginManager().registerEvents(new onBlockPlace(), this);
        if (busterConfig.getDisableFlow())
            Bukkit.getPluginManager().registerEvents(new onFlow(), this);

        Bukkit.getLogger().info("--------------------BulChunkBuster plugin has been enabled--------------------");
    }

    public static ChunkBuster getChunkBuster() { return chunkBuster; }

    public static BusterConfig getBusterConfig() { return busterConfig; }

    public static BlockBusterManager getBlockBusterManager() { return blockBusterManager; }

    public static SupportManager getSupportManager() { return supportManager; }

    private void updateChecker() {
        try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=128629").openStream(); Scanner scanner = new Scanner(inputStream)) {
            if (scanner.next().equals(this.getDescription().getVersion()))
                return;
            getLogger().info("------------------------------------------------------------------");
            getLogger().info("There is a new update available for BulChunkBuster !");
            getLogger().info("Download here : https://www.spigotmc.org/resources/128629");
            getLogger().info("------------------------------------------------------------------");
        } catch (IOException exception) {
            getLogger().info("[BulMultiverse] Cannot look for updates please join discord: https://discord.gg/JAe62PEaYv" + exception.getMessage());
        }
    }
}
