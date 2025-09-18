package com.alihaine.bulchunkbuster.file;

import com.alihaine.bulchunkbuster.ChunkBuster;
import net.md_5.bungee.api.chat.*;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class BusterConfig {
    private final Material materialBuster;
    private final String blockBusterName;
    private final List<String> blockBusterLore;
    private final boolean blockBusterCheckAll;

    private final boolean blockBusterSneakConfirmation;
    private final boolean blockBusterChatConfirmation;

    private final long busterSpeed;
    private final int busterSize;
    private Sound busterSound;
    private Effect busterEffect;

    boolean destroyBlockBuster;
    double chanceDropDestroyedBlock;

    final List<Material> blackListMaterials = new ArrayList<>();
    final List<String> blockListWorlds;

    private final long cooldown;
    private final int serverYMin;
    private final boolean disableFlow;
    private final List<Material> flowMaterials =  new ArrayList<>();

    private final BaseComponent[] chatConfirmation;

    private final HashMap<String, String> messages = new HashMap<>();

    public BusterConfig(ChunkBuster chunkBuster) {
        chunkBuster.saveDefaultConfig();
        final FileConfiguration config = chunkBuster.getConfig();

        //Initialize config elements
        final String materialAsString = config.getString("block_buster");
        try {
            this.materialBuster = Material.getMaterial(materialAsString.toUpperCase());
        } catch (Exception e) {
            Bukkit.getLogger().severe(ChunkBuster.PREFIX + " The material " + materialAsString + " was not found or don't exist");
            throw new RuntimeException();
        }
        this.blockBusterName = config.getString("block_buster_name").replaceAll("&", "§");
        this.blockBusterLore = config.getStringList("block_buster_lore").stream()
                .map(line -> line.replace("&", "§"))
                .collect(Collectors.toList());
        this.blockBusterCheckAll = config.getBoolean("block_buster_check_all");

        this.blockBusterSneakConfirmation = config.getBoolean("block_buster_sneak_confirmation");
        this.blockBusterChatConfirmation = config.getBoolean("block_buster_chat_confirmation");

        this.busterSpeed = config.getInt("buster_speed") * 20L;
        this.busterSize = config.getInt("buster_size");

        final String soundAsString = config.getString("buster_sound");
        try {
            this.busterSound = Sound.valueOf(soundAsString);
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().warning("The sound " + soundAsString + " don't exist in your minecraft server version.");
        }

        final String effectAsString = config.getString("buster_effect");
        try {
            this.busterEffect = Effect.valueOf(effectAsString);
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().warning("The effect " + effectAsString + " don't exist in your minecraft server version.");
        }

        this.destroyBlockBuster = config.getBoolean("destroy_block_after_use");
        this.chanceDropDestroyedBlock = config.getDouble("chance_to_drop_destroyed_block");

        final List<String> blackListMaterialsString = config.getStringList("black_list_materials");
        for (String materialStr : blackListMaterialsString) {
            try {
                this.blackListMaterials.add(Material.getMaterial(materialStr.toUpperCase()));
            } catch (Exception e) {
                Bukkit.getLogger().warning(ChunkBuster.PREFIX + " The material " + materialStr + " was not found or don't exist (black list material)");
            }
        }
        this.blockListWorlds = config.getStringList("block_list_worlds");

        this.cooldown = config.getInt("cooldown_for_each_use") * 1000L;
        this.serverYMin = config.getInt("server_y_min");

        ConfigurationSection configSection = config.getConfigurationSection("messages");
        for (String key : configSection.getKeys(false))
            this.messages.put(key, configSection.getString(key).replaceAll("&", "§"));

        this.disableFlow = config.getBoolean("disable_flowing_in_busted_chunk");
        if (this.disableFlow) {
            final List<String> flowMaterialsString = config.getStringList("server_flow_materials");
            for (String materialStr : flowMaterialsString) {
                try {
                    this.flowMaterials.add(Material.getMaterial(materialStr.toUpperCase()));
                } catch (Exception e) {
                    Bukkit.getLogger().warning(ChunkBuster.PREFIX + " The flow material " + materialStr + " was not found or don't exist in your version");
                }
            }
        }

        this.chatConfirmation = this.buildChatConfirmationComponent();
    }

    private BaseComponent[] buildChatConfirmationComponent() {
        return new ComponentBuilder(this.getMessage("chat_confirmation"))
                .append(this.getMessage("chat_confirmation_yes"))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/busterconfirmation yes"))
                .append(" - ")
                .event((ClickEvent) null)
                .append(this.getMessage("chat_confirmation_no"))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/busterconfirmation no"))
                .create();
    }

    public void sendChatConfirmationComponent(Player player) {
        player.spigot().sendMessage(this.chatConfirmation);
    }

    public boolean isMaterialBlacklisted(Material material) {
        return this.blackListMaterials.contains(material);
    }

    public boolean isBlockBuster(ItemStack itemStack) {
        if (this.blockBusterCheckAll)
            return itemStack.getType().equals(this.materialBuster) && itemStack.getItemMeta().getDisplayName() != null && itemStack.getItemMeta().getDisplayName().equals(this.blockBusterName);
        return itemStack.getType().equals(this.materialBuster);
    }

    public boolean isBlacklistedWorld(World world) {
        return this.blockListWorlds.contains(world.getName());
    }

    public boolean isFlowMaterial(Material material) { return this.flowMaterials.contains(material); }

    public Material getMaterialBuster() { return this.materialBuster; }

    public boolean getBlockBusterSneakConfirmation() {
        return this.blockBusterSneakConfirmation;
    }

    public boolean getBlockBusterChatConfirmation() {
        return this.blockBusterChatConfirmation;
    }

    public long getBusterSpeed() { return this.busterSpeed; }

    public int getBusterSize() { return this.busterSize; }

    public Sound getBusterSound() { return this.busterSound; }

    public Effect getBusterEffect() { return this.busterEffect; }

    public boolean getDestroyBlockBuster() {
        return this.destroyBlockBuster;
    }

    public double getChanceDropDestroyedBlock() {
        return this.chanceDropDestroyedBlock;
    }

    public long getCooldown() {
        return this.cooldown;
    }

    public int getServerYMin() { return this.serverYMin; }

    public boolean getDisableFlow() { return this.disableFlow; }

    public String getMessage(String key) {
        return this.messages.get(key);
    }

    public ItemStack getBlockBuster(int amount) {
        final ItemStack itemBuster = new ItemStack(this.materialBuster, amount);
        final ItemMeta meta = itemBuster.getItemMeta();
        meta.setDisplayName(this.blockBusterName);
        meta.setLore(this.blockBusterLore);
        itemBuster.setItemMeta(meta);
        return itemBuster;
    }
}
