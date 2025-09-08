package com.alihaine.bulchunkbuster.listener;

import com.alihaine.bulchunkbuster.ChunkBuster;
import com.alihaine.bulchunkbuster.buster.BlockBuster;
import com.alihaine.bulchunkbuster.file.BusterConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class onChat implements Listener {
    private final ChunkBuster chunkBuster = ChunkBuster.getChunkBuster();
    private final BusterConfig busterConfig = ChunkBuster.getBusterConfig();

    /*
     * Chat is async, to be safe and prevent duplication we
     * Have to run the tasks in the main thread using runTask
     */
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        if (!chunkBuster.isThePlayerWaiting(player)) return;
        event.setCancelled(true);
        if (!event.getMessage().equalsIgnoreCase("buster")) {
            Bukkit.getScheduler().runTask(ChunkBuster.getChunkBuster(), () -> {
                player.sendMessage(busterConfig.getMessage("chat_confirmation_error"));
            });
            chunkBuster.removePlayerWaitForChatConfirmation(player);
            return;
        }
        Bukkit.getScheduler().runTask(ChunkBuster.getChunkBuster(), () -> {
            final Location location = chunkBuster.getWaitingLocation(player);
            final Block block = location.getBlock();

            chunkBuster.removePlayerWaitForChatConfirmation(player);
            if (block.getType() != busterConfig.getMaterialBuster()) {
                player.sendMessage(busterConfig.getMessage("chat_confirmation_block_error"));
                return;
            }
            block.setType(Material.AIR);
            new BlockBuster(block.getChunk(), block.getLocation(), player);
        });
    }
}
