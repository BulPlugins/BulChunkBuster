package com.alihaine.bulchunkbuster.buster;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BlockBusterManager {
    private final HashMap<Player, Location> blockBusterWaitForChatConfirmation = new HashMap<>();
    private final List<Chunk> chunkUsedByBuster = new ArrayList<>();

    public void addUsedChunk(Chunk chunk) {
        this.chunkUsedByBuster.add(chunk);
    }

    public boolean isUsedChunk(Chunk chunk) {
        return this.chunkUsedByBuster.contains(chunk);
    }

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
}
