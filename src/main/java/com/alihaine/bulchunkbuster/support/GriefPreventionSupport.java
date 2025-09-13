package com.alihaine.bulchunkbuster.support;

import org.bukkit.Bukkit;
import org.bukkit.event.block.BlockPlaceEvent;

import java.lang.reflect.Method;
import java.util.UUID;

public class GriefPreventionSupport implements Support {
    private Object dataStore;
    private Method getClaimAt;

    public GriefPreventionSupport() {
        try {
            Class<?> gpClass = Class.forName("me.ryanhamshire.GriefPrevention.GriefPrevention");
            Object gpInstance = gpClass.getField("instance").get(null);
            this.dataStore = gpInstance.getClass().getField("dataStore").get(gpInstance);

            for (Method m: this.dataStore.getClass().getMethods()) {
                if (m.getName().equals("getClaimAt") && m.getParameterCount() == 3)
                    this.getClaimAt = m;
            }
        } catch (Exception e) {
            Bukkit.getLogger().warning("[BulChunkBuster] Failed to hook into GriefPrevention! " + e.getMessage());
        }
    }


    @Override
    public boolean check(BlockPlaceEvent event) {
        try {
            Object claim = this.getClaimAt.invoke(this.dataStore, event.getBlock().getLocation(), false, null);
            if (claim == null) return false;
            UUID ownerId = (UUID) claim.getClass().getMethod("getOwnerID").invoke(claim);
            return event.getPlayer().getUniqueId().equals(ownerId);
        } catch (Exception e) {
            Bukkit.getLogger().warning("Failed to check GriefPrevention claim contact the admin " + e);
            return false;
        }
    }
}
