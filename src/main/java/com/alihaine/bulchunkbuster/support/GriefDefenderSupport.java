package com.alihaine.bulchunkbuster.support;

import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.claim.Claim;
import org.bukkit.event.block.BlockPlaceEvent;

public class GriefDefenderSupport implements Support {
    @Override
    public boolean check(BlockPlaceEvent event) {
        final Claim claim = GriefDefender.getCore().getClaimAt(event.getBlock().getLocation());

        if (claim != null && !claim.isWilderness())
            return claim.getOwnerUniqueId().equals(event.getPlayer().getUniqueId());
        return false;
    }
}
