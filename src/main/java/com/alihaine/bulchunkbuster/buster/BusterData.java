package com.alihaine.bulchunkbuster.buster;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class BusterData {
    private final List<Block> blocks = new ArrayList<>();
    private final BitSet dropFlags = new BitSet();
    private int cursor = 0;
    private final Location locationMiddle;

    public BusterData(Chunk chunk, int y) {
        int x = (chunk.getX() << 4) + 8; // chunk.getX() * 16 + 8
        int z = (chunk.getZ() << 4) + 8; // chunk.getZ() * 16 + 8
        this.locationMiddle = new Location(chunk.getWorld(), x, y, z);
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public BitSet getDropFlags() {
        return this.dropFlags;
    }

    public boolean hasNext() {
        return cursor < blocks.size();
    }

    public Block nextBlock() {
        return blocks.get(cursor);
    }

    public boolean shouldDrop() {
        return dropFlags.get(cursor);
    }

    public void advance() {
        cursor++;
    }

    public boolean isDone() {
        return this.cursor >= this.blocks.size();
    }

    public Location getMiddleLocation() {
        return locationMiddle;
    }
}
