package com.alihaine.bulchunkbuster.buster;

import com.alihaine.bulchunkbuster.ChunkBuster;
import com.alihaine.bulchunkbuster.file.BusterConfig;
import com.alihaine.bulchunkbuster.utils.BlockUtils;
import com.alihaine.bulchunkbuster.utils.MathUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class BlockBuster {
    private final BusterConfig busterConfig = ChunkBuster.getBusterConfig();
    private final Chunk chunk;
    private final Location location;
    private final Player player;

    public BlockBuster(Chunk chunk,  Location location, Player player) {
        this.chunk = chunk;
        this.location = location;
        this.player = player;
        this.runBuster();
    }

    public void runBuster() {
        Bukkit.getScheduler().runTaskAsynchronously(ChunkBuster.getChunkBuster(), () -> {
            final BusterData busterData = asyncRunBusterSetup();
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (busterData.isDone()) {
                        player.sendMessage(busterConfig.getMessage("buster_end"));
                        cancel();
                    }
                    syncRunBuster(busterData);
                }
            }.runTaskTimer(ChunkBuster.getChunkBuster(), 0L, busterConfig.getBusterSpeed());
        });
    }

    /*
     * All the calculations and checks are done asynchronously, instead of
     * Iterating EVERY block in the main thread, I do that in another one.
     * So, the sync task will iterate only the necessary block (not air, or blacklisted blocks)
     */
    public BusterData asyncRunBusterSetup() {
        final BusterData bustersData = new BusterData(this.chunk, location.getBlockY()-1);
        for (int y = this.location.getBlockY() - 1; y >= busterConfig.getServerYMin(); y--)
            this.asyncFillCurrentY(y, bustersData);
        return bustersData;
    }

    private void asyncFillCurrentY(int y, BusterData busterData) {
        for (int z = 15; z >= 0; z--) {
            for (int x = 15; x >= 0; x--) {
                Block block = chunk.getBlock(x, y, z);
                Material mat = BlockUtils.getMaterial(block);
                if (mat == Material.AIR || busterConfig.isMaterialBlacklisted(mat)) continue;
                busterData.getBlocks().add(block);
                if (this.busterConfig.getChanceDropDestroyedBlock() > 0.0 && MathUtils.getRandomDouble() < this.busterConfig.getChanceDropDestroyedBlock())
                    busterData.getDropFlags().set(busterData.getBlocks().size()-1);
            }
        }
    }

    public void syncRunBuster(BusterData busterData) {
        syncPlayAnim(busterData);
        for (int i = 0; busterData.hasNext() && i < busterConfig.getBusterSize()*256; i++)
            syncRunBusterAction(busterData);
        syncRunBusterAction(busterData);
    }

    private void syncRunBusterAction(BusterData busterData) {
        if (!busterData.hasNext())
            return;
        Block block = busterData.nextBlock();
        if (busterData.shouldDrop())
            block.breakNaturally();
        else
            block.setType(Material.AIR);
        busterData.advance();
    }

    private void syncPlayAnim(BusterData busterData) {
        final Location location = busterData.getMiddleLocation();
        if (!busterData.hasNext())
            return;
        if (this.busterConfig.getBusterSound() != null)
            location.getWorld().playSound(location, this.busterConfig.getBusterSound(), 0.5F, 1.0F);
        if (this.busterConfig.getBusterEffect() != null)
            location.getWorld().playEffect(location, this.busterConfig.getBusterEffect(), 0);
        location.add(0, -busterConfig.getBusterSize(), 0);
    }
}
