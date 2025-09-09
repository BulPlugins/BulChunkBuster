package com.alihaine.bulchunkbuster;

import com.alihaine.bulchunkbuster.command.Confirmation;
import com.alihaine.bulchunkbuster.command.GiveBuster;
import com.alihaine.bulchunkbuster.file.BusterConfig;
import com.alihaine.bulchunkbuster.listener.onBlockPlace;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

public class ChunkBuster extends JavaPlugin {
    public static final String PREFIX = "[BulChunkBuster] ";
    private static ChunkBuster chunkBuster;
    private static BusterConfig busterConfig;
    private final HashMap<Player, Location> blockBusterWaitForChatConfirmation = new HashMap<>();

    @Override
    public void onEnable() {
        this.updateChecker();
        Bukkit.getLogger().info("--------------------Starting to enable BulChunkBuster--------------------");
        new Metrics(this, 27173);
        chunkBuster = this;
        busterConfig = new BusterConfig(this);

        this.getCommand("bulchunkbuster").setExecutor(new GiveBuster());
        if (busterConfig.getBlockBusterChatConfirmation())
            this.getCommand("busterconfirmation").setExecutor(new Confirmation());
        Bukkit.getPluginManager().registerEvents(new onBlockPlace(), this);

        Bukkit.getLogger().info("--------------------BulChunkBuster plugin has been enabled--------------------");
    }

    public static ChunkBuster getChunkBuster() { return chunkBuster; }

    public static BusterConfig getBusterConfig() { return busterConfig; }

    public void addPlayerWaitForChatConfirmation(Player player, Location location) {
        this.blockBusterWaitForChatConfirmation.put(player, location);
    }

    public void removePlayerWaitForChatConfirmation(Player player) {
        this.blockBusterWaitForChatConfirmation.remove(player);
    }

    public boolean isThePlayerWaiting(Player player) {
        return blockBusterWaitForChatConfirmation.containsKey(player);
    }

    public Location getWaitingLocation(Player player) {
        return blockBusterWaitForChatConfirmation.get(player);
    }

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
