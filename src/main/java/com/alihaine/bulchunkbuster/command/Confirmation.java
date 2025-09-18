package com.alihaine.bulchunkbuster.command;

import com.alihaine.bulchunkbuster.ChunkBuster;
import com.alihaine.bulchunkbuster.buster.BlockBuster;
import com.alihaine.bulchunkbuster.buster.BlockBusterManager;
import com.alihaine.bulchunkbuster.file.BusterConfig;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Confirmation implements CommandExecutor {
    private final BlockBusterManager blockBusterManager = ChunkBuster.getBlockBusterManager();
    private final BusterConfig busterConfig = ChunkBuster.getBusterConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) return true;
        final Player player = (Player) sender;
        final Location location = blockBusterManager.getWaitingLocation(player);
        if (location == null) return true;
        blockBusterManager.removePlayerWaitForChatConfirmation(player);
        if (args[0].equalsIgnoreCase("yes")) {
            final Block block = location.getBlock();
            if (block.getType() != busterConfig.getMaterialBuster()) {
                player.sendMessage(busterConfig.getMessage("chat_confirmation_block_error"));
                return true;
            }
            block.setType(Material.AIR);
            new BlockBuster(block.getChunk(), block.getLocation(), player);
        } else {
            player.sendMessage(busterConfig.getMessage("chat_confirmation_cancel"));
        }
        return true;
    }
}
