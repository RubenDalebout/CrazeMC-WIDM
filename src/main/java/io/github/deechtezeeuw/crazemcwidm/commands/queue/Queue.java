package io.github.deechtezeeuw.crazemcwidm.commands.queue;

import io.github.deechtezeeuw.crazemcwidm.CrazeMCWIDM;
import io.github.deechtezeeuw.crazemcwidm.commands.Commands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

public class Queue extends Commands {
    private final CrazeMCWIDM plugin = CrazeMCWIDM.getInstance();

    @Override
    public void onCommand(CommandSender sender, Command command, String[] args) {
        if (!(sender instanceof Player)) {
            plugin.getCommandManager().onlyPlayer(sender);
            return;
        }

        // Check permissions
        if (!sender.hasPermission("crazemc.queue.join") && !sender.hasPermission("crazemc.leave") && !sender.hasPermission("crazemc.queue.list")) {
            plugin.getCommandManager().noPermission(null, sender);
            return;
        }

        // Check if no arguments
        if (args.length == 0 || args.length > 0 && !args[0].equalsIgnoreCase("join") && !args[0].equalsIgnoreCase("leave") && !args[0].equalsIgnoreCase("list")) {
            ArrayList<String> subs = new ArrayList<>();
            if (sender.hasPermission("crazemc.queue.join")) subs.add("join");
            if (sender.hasPermission("crazemc.queue.leave")) subs.add("leave");
            if (sender.hasPermission("crazemc.queue.list")) subs.add("list");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfigManager().getMain().serverPrefix + plugin.getConfigManager().getMain().serverDivider + "&cBedoel je &4&l/queue <"+subs+">"));
            return;
        }

        Player player = (Player) sender;

        // join
        if (args[0].equalsIgnoreCase("join")) {
            if (!sender.hasPermission("crazemc.queue.join")) {
                plugin.getCommandManager().noPermission(player, null);
                return;
            }

            if (plugin.getGameDataManager().alreadyHosting(player.getUniqueId()) || plugin.getGameDataManager().alreadyContestant(player.getUniqueId())) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        plugin.getConfigManager().getMain().serverPrefix + plugin.getConfigManager().getMain().serverDivider + "&cJe kan de queue niet joinen, terwijl je in een game zit!"));
                return;
            }

            if (plugin.getGameManager().getQueue().contains(player.getUniqueId())) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        plugin.getConfigManager().getMain().serverPrefix + plugin.getConfigManager().getMain().serverDivider + "&cJe zit al in de queue!"));
                return;
            }

            plugin.getGameManager().setQueue(player.getUniqueId());
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfigManager().getMain().serverPrefix + plugin.getConfigManager().getMain().serverDivider + "&aJe bent toegevoegd aan de queue!"));
            return;
        }

        if (args[0].equalsIgnoreCase("leave")) {
            if (!sender.hasPermission("crazemc.queue.leave")) {
                plugin.getCommandManager().noPermission(player, null);
                return;
            }

            if (!plugin.getGameManager().getQueue().contains(player.getUniqueId())) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        plugin.getConfigManager().getMain().serverPrefix + plugin.getConfigManager().getMain().serverDivider + "&cJe zit niet in de queue!"));
                return;
            }

            plugin.getGameManager().setQueue(player.getUniqueId());
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfigManager().getMain().serverPrefix + plugin.getConfigManager().getMain().serverDivider + "&aJe bent uit de queue gehaald!"));
        }

        if (args[0].equalsIgnoreCase("list")) {
            if (!sender.hasPermission("crazemc.queue.list")) {
                plugin.getCommandManager().noPermission(player, null);
                return;
            }

            if (!plugin.getGameDataManager().alreadyHosting(player.getUniqueId())) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        plugin.getConfigManager().getMain().serverPrefix + plugin.getConfigManager().getMain().serverDivider + "&cJe moet een game hosten om de queue te bekijken!"));
                return;
            }

            int number = 1;
            ArrayList<String> queueMessage = new ArrayList<>();
            queueMessage.add(ChatColor.translateAlternateColorCodes('&', "&7&m-------[&f " + plugin.getConfigManager().getMain().serverPrefix + " &cQueue &7&m]-------"));
            for(UUID uuid : plugin.getGameManager().getQueue()) {
                if (Bukkit.getServer().getPlayer(uuid) != null) {
                    queueMessage.add(ChatColor.translateAlternateColorCodes('&', "&7" + number + ". &f&l" + Bukkit.getServer().getPlayer(uuid).getName()));
                    number++;
                }
            }

            if (queueMessage.size() == 1) queueMessage.add(ChatColor.translateAlternateColorCodes('&', "&f          &c&lGeen spelers in queue"));
            queueMessage.add(ChatColor.translateAlternateColorCodes('&', "&7&m-------[&f " + plugin.getConfigManager().getMain().serverPrefix + " &cQueue &7&m]-------"));

            player.sendMessage(queueMessage.toArray(new String[0]));
        }
    }

    @Override
    public String name() {
        return "queue";
    }

    @Override
    public String info() {
        return "general queue command";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
