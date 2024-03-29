package io.github.deechtezeeuw.crazemcwidm.commands.panel;

import io.github.deechtezeeuw.crazemcwidm.CrazeMCWIDM;
import io.github.deechtezeeuw.crazemcwidm.classes.Game;
import io.github.deechtezeeuw.crazemcwidm.commands.Commands;
import io.github.deechtezeeuw.crazemcwidm.gui.GameMenu;
import io.github.deechtezeeuw.crazemcwidm.gui.PanelMenu;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Panel extends Commands {
    private final CrazeMCWIDM plugin = CrazeMCWIDM.getInstance();

    @Override
    public void onCommand(CommandSender sender, Command command, String[] args) {
        // Check if user has host perms
        if (!sender.hasPermission("crazemc.tempvuller") && !sender.hasPermission("crazemc.jrgamehost") && !sender.hasPermission("crazemc.gamehost") && !sender.hasPermission("crazemc.srgamehost")) {
            plugin.getCommandManager().noPermission(null, sender);
            return;
        }
        // Check if user is in-game
        if (!(sender instanceof Player)) {
            plugin.getCommandManager().onlyPlayer(sender);
            return;
        }

        Player player = (Player) sender;

        if (!plugin.getGameDataManager().alreadyHosting(player.getUniqueId())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfigManager().getMain().serverPrefix + plugin.getConfigManager().getMain().serverDivider + "&cJe host geen game!"));
            return;
        }

        Game game = plugin.getGameDataManager().getHostingGame(player.getUniqueId());

        if (!player.getWorld().getUID().equals(game.getMap())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfigManager().getMain().serverPrefix + plugin.getConfigManager().getMain().serverDivider + "&cJe bent niet in de juiste game om dit te doen."));
            return;
        }

        // Check if there are no arguments
        if (args.length <= 0) {
            new PanelMenu().open(player);
            return;
        }
    }

    @Override
    public String name() {
        return "panel";
    }

    @Override
    public String info() {
        return "control your game";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
