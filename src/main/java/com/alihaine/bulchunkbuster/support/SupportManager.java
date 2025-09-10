package com.alihaine.bulchunkbuster.support;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;
import java.util.List;

public class SupportManager {
    private final List<Support> supports = new ArrayList<>();

    public SupportManager(FileConfiguration config) {
        final ConfigurationSection section = config.getConfigurationSection("supports");

        for (String key : section.getKeys(false)) {
            if (!section.getBoolean(key))
                continue;
            if (!this.isPluginLoaded(key)) {
                Bukkit.getLogger().warning("You enabled support for plugin " + key + " but the plugin is not found or not loaded.." );
                continue;
            }
            if (key.equals("factions"))
                this.supports.add(new FactionSupport());
        }

    }

    private boolean isPluginLoaded(String pluginName) {
        return Bukkit.getPluginManager().getPlugin(pluginName) != null
                || Bukkit.getPluginManager().isPluginEnabled(pluginName);
    }

    public boolean runSupports(BlockPlaceEvent event) {
        for (Support support : this.supports)
            if (!support.check(event))
                return false;
        return true;
    }
}
