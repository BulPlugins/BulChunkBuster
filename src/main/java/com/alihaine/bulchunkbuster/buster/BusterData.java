package com.alihaine.bulchunkbuster.buster;

import org.bukkit.block.Block;

import java.util.BitSet;
import java.util.List;

public class BusterData {
    private final List<Block> blocks;
    private final BitSet dropFlags;
    private int cursor = 0;

    public BusterData(List<Block> blocks, BitSet dropFlags) {
        this.blocks = blocks;
        this.dropFlags = dropFlags;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public BitSet getDropFlags() {
        return dropFlags;
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
}
