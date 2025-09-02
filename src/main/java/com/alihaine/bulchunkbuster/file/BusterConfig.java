package com.alihaine.bulchunkbuster.file;

import com.alihaine.bulchunkbuster.ChunkBuster;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BusterConfig {
    private final Material materialBuster;
    private final String blockBusterName;
    private final List<String> blockBusterLore;

    int busterSpeed;
    int busterSize;

    boolean busterDown;
    boolean busterUp;

    boolean destroyBlockBuster;
    boolean dropDestroyedBlock;
    double chanceDropDestroyedBlock;

    final List<Material> blackListMaterials = new ArrayList<>();
    final List<String> blockListWorlds;

    int cooldown;

    private final HashMap<String, String> messages = new HashMap();

    private final FileConfiguration config;

    public BusterConfig(ChunkBuster chunkBuster) {
        chunkBuster.saveDefaultConfig();
        this.config = chunkBuster.getConfig();

        //Initialize config elements
        final String materialAsString = this.config.getString("block_buster");
        try {
            this.materialBuster = Material.getMaterial(materialAsString.toUpperCase());
        } catch (Exception e) {
            Bukkit.getLogger().severe(ChunkBuster.PREFIX + " The material " + materialAsString + " was not found or don't exist");
            throw new RuntimeException();
        }
        this.blockBusterName = this.config.getString("block_buster_name");
        this.blockBusterLore = this.config.getStringList("block_buster_lore");

        this.busterSpeed = this.config.getInt("buster_speed");
        this.busterSize = this.config.getInt("buster_size");
        this.busterDown = this.config.getBoolean("buster_down");
        this.busterUp = this.config.getBoolean("buster_up");

        this.destroyBlockBuster = this.config.getBoolean("destroy_block_after_use");
        this.dropDestroyedBlock = this.config.getBoolean("drop_destroyed_block");
        this.chanceDropDestroyedBlock = this.config.getDouble("chance_to_drop_destroyed_block");

        final List<String> blackListMaterialsString = this.config.getStringList("black_list_materials");
        for (String materialStr : blackListMaterialsString) {
            try {
                this.blackListMaterials.add(Material.getMaterial(materialStr.toUpperCase()));
            } catch (Exception e) {
                Bukkit.getLogger().warning(ChunkBuster.PREFIX + " The material " + materialStr + " was not found or don't exist (black list material)");
            }
        }
        this.blockListWorlds = this.config.getStringList("block_list_worlds");

        this.cooldown = this.config.getInt("cooldown_for_each_use");

        ConfigurationSection configSection = this.config.getConfigurationSection("messages");
        for (String key : configSection.getKeys(false))
            this.messages.put(key, configSection.getString(key));
    }

    public boolean isMaterialBlacklisted(Material material) {
        return this.blackListMaterials.contains(material);
    }

    public boolean isBlockBuster(Material material) {
        return this.materialBuster == material;
    }

}
