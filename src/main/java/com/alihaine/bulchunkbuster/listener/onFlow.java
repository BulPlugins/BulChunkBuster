package com.alihaine.bulchunkbuster.listener;

import com.alihaine.bulchunkbuster.ChunkBuster;
import com.alihaine.bulchunkbuster.buster.BlockBusterManager;
import com.alihaine.bulchunkbuster.file.BusterConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

public class onFlow implements Listener {
    private final BlockBusterManager blockBusterManager = ChunkBuster.getBlockBusterManager();
    private final BusterConfig busterConfig = ChunkBuster.getBusterConfig();

    @EventHandler
    public void onWaterFlow(BlockFromToEvent event) {
        if (!this.busterConfig.isFlowMaterial(event.getBlock().getType())) return;
        if (!this.blockBusterManager.isUsedChunk(event.getToBlock().getChunk())) return;
        event.setCancelled(true);
    }
}
