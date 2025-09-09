package com.alihaine.bulchunkbuster.support;

import com.massivecraft.factions.*;
import org.bukkit.event.block.BlockPlaceEvent;

public class FactionSupport implements Support {
    @Override
    public boolean check(BlockPlaceEvent event) {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(event.getPlayer());
        Faction faction = Board.getInstance().getFactionAt(new FLocation(event.getBlock().getLocation()));
        return fPlayer.hasFaction() && fPlayer.getFaction().equals(faction);
    }
}
