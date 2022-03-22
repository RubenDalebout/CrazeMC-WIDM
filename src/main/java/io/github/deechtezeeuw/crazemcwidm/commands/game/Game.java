package io.github.deechtezeeuw.crazemcwidm.commands.game;

import io.github.deechtezeeuw.crazemcwidm.CrazeMCWIDM;
import io.github.deechtezeeuw.crazemcwidm.commands.Commands;
import io.github.deechtezeeuw.crazemcwidm.gui.GameMenu;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Game extends Commands {
    private final CrazeMCWIDM plugin = CrazeMCWIDM.getInstance();

    @Override
    public void onCommand(CommandSender sender, Command command, String[] args) {
        // Check if user is in-game
        if (!(sender instanceof Player)) {
            plugin.getCommandManager().onlyPlayer(sender);
            return;
        }

        Player player = (Player) sender;

        if (!plugin.getSQL().sqlSelect.playerIsHost(player.getUniqueId()) && !plugin.getSQL().sqlSelect.playerIsContestant(player.getUniqueId())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfigManager().getMain().serverPrefix + plugin.getConfigManager().getMain().serverDivider + "&cJe zit niet in een game!"));
            return;
        }

        // Check if there are no arguments
        if (args.length <= 0) {
            new GameMenu().open(player);
            return;
        }
    }

    @Override
    public String name() {
        return "game";
    }

    @Override
    public String info() {
        return "See all information about your game";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
