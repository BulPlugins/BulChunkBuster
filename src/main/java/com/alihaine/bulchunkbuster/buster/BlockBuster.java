package com.alihaine.bulchunkbuster.buster;

import com.alihaine.bulchunkbuster.ChunkBuster;
import com.alihaine.bulchunkbuster.file.BusterConfig;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class BlockBuster {
    private final BusterConfig busterConfig = ChunkBuster.getBusterConfig();
    private final Chunk chunk;
    private final Location location;

    public BlockBuster(Chunk chunk,  Location location) {
        this.chunk = chunk;
        this.location = location;
        this.runBuster();
    }

    public void runBuster() {
        Bukkit.getScheduler().runTaskAsynchronously(ChunkBuster.getChunkBuster(), () -> {
            final BusterData busterData = asyncRunBusterSetup(this.chunk);
            if (busterData.getBlocks().isEmpty())
                return;
            Bukkit.getLogger().warning("[ChunkBuster] " + busterData.getBlocks().size());
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (busterData.isDone())
                        cancel();
                    syncRunBuster(busterData);
                }
            }.runTaskTimer(ChunkBuster.getChunkBuster(), 0L, 10L);

        });
    }

    public BusterData asyncRunBusterSetup(Chunk chunk) {
        final List<Block> blockToClear = new ArrayList<>();
        BitSet dropFlags = new BitSet();

        for (int y = this.location.getBlockY()-1; y > 0; y--) {
            for (int z = 16; z > 0; z--) {
                for (int x = 16; x > 0; x--) {
                    Block block = chunk.getBlock(x, y, z);
                    if (block.getType() == Material.AIR || busterConfig.isMaterialBlacklisted(block.getType()))
                        continue;
                    blockToClear.add(block);
                    dropFlags.set(blockToClear.size()-1);
                }
            }
        }
        return new BusterData(blockToClear, dropFlags);
    }

    public void syncRunBuster(BusterData busterData) {
        System.out.println("Run task");
        for (int i = 0; i < 255 && busterData.hasNext(); i++) {
            Block block = busterData.nextBlock();
            if (busterData.shouldDrop())
                block.breakNaturally();
            else
                block.setType(Material.AIR);
            busterData.advance();
        }
    }
}
