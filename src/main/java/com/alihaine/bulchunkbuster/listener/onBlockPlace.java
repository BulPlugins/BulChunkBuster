package com.alihaine.bulchunkbuster.listener;

import com.alihaine.bulchunkbuster.ChunkBuster;
import com.alihaine.bulchunkbuster.buster.BlockBuster;
import com.alihaine.bulchunkbuster.file.BusterConfig;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.HashMap;
import java.util.UUID;

public class onBlockPlace implements Listener {
    private final BusterConfig busterConfig = ChunkBuster.getBusterConfig();
    private final HashMap<String, Long> cooldowns = new HashMap<>();

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        final Block block = event.getBlock();
        final Player player = event.getPlayer();
        if (!busterConfig.isBlockBuster(event.getItemInHand()))
            return;
        event.setCancelled(true);
        if (!player.hasPermission("bulchunkbuster.use")) {
            player.sendMessage(busterConfig.getMessage("no_permission"));
            return;
        }
        if (busterConfig.isBlacklistedWorld(block.getLocation().getWorld())) {
            player.sendMessage(busterConfig.getMessage("blacklist_world"));
            return;
        }
        long cooldown = this.isOnCooldown(player.getName());
        if (cooldown > 0) {
            player.sendMessage(busterConfig.getMessage("cooldown_error").replace("%time%", String.valueOf(cooldown)));
            return;
        }
        if (busterConfig.getDestroyBlockBuster())
            event.getItemInHand().setAmount(event.getItemInHand().getAmount() - 1);
        player.sendMessage(busterConfig.getMessage("buster_placed"));
        this.cooldowns.put(player.getName(), System.currentTimeMillis());
        new BlockBuster(block.getChunk(), block.getLocation(), player);
    }

    private long isOnCooldown(String name) {
        if (!this.cooldowns.containsKey(name))
            return 0;

        long diff = System.currentTimeMillis() - this.cooldowns.get(name);
        long remaining = this.busterConfig.getCooldown() - diff; // ms left
        return Math.max(0, remaining) / 1000;
    }
}
