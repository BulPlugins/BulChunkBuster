package com.alihaine.bulchunkbuster.command;

import com.alihaine.bulchunkbuster.ChunkBuster;
import com.alihaine.bulchunkbuster.file.BusterConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveBuster implements CommandExecutor {
    private final BusterConfig busterConfig = ChunkBuster.getBusterConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /buster <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found: " + args[0]);
            return true;
        }

        int amount = 1;
        if (args.length == 2) {
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Invalid amount: " + args[1]);
            }
        }

        final ItemStack itemBuster = busterConfig.getBlockBuster(amount);
        target.getInventory().addItem(itemBuster);
        target.sendMessage(busterConfig.getMessage("buster_receive").replace("%amount%", String.valueOf(amount)));

        sender.sendMessage(busterConfig.getMessage("buster_give"));

        return true;
    }
}
