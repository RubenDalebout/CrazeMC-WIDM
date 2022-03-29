package io.github.deechtezeeuw.crazemcwidm.classes;

import io.github.deechtezeeuw.crazemcwidm.CrazeMCWIDM;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class Game {
    private final CrazeMCWIDM plugin = CrazeMCWIDM.getInstance();

    protected UUID uuid; // Special id of the game
    protected UUID map; // Special id of the world its playing in
    protected ArrayList<UUID> hosts = new ArrayList<>(); // Arraylist of the host
    protected String theme = ""; // String for the theme map
    protected Integer gameStatus; // Status of the game
    protected ArrayList<Contestant> contestants = new ArrayList<>(); // Contestants of the game
    protected int time;

    // Default
    public Game(UUID GameKey) {
        this.uuid = GameKey;
    }

    // Set match uuid
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    // Get uuid of game
    public UUID getUuid() {
        return uuid;
    }

    // Set map uuid
    public void setMap(UUID map) {
        this.map = map;
    }

    // Get map uuid
    public UUID getMap() {
        return map;
    }

    // Set hosts
    public void setHost(UUID player) {
        if (this.hosts.contains(player)) {
            this.hosts.remove(player);
        } else {
            this.hosts.add(player);
        }
    }

    // Get hosts
    public ArrayList<UUID> getHosts() {
        return hosts;
    }

    // Get theme
    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    // Get gameStatus
    public Integer getGameStatus() {
        if (gameStatus == null) gameStatus = 0;
        return gameStatus;
    }

    public void setGameStatus(Integer gameStatus) {
        this.gameStatus = gameStatus;
    }

    protected ItemStack Item(String title, String material, Integer amount, Integer itemShort, ArrayList<String> lore) {
        ItemStack item = new ItemStack(Material.valueOf(material), amount, itemShort.shortValue());
        ItemMeta MetaItem = item.getItemMeta();
        MetaItem.setLore(lore);
        MetaItem.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        item.setItemMeta(MetaItem);
        MetaItem.setDisplayName(ChatColor.translateAlternateColorCodes('&', title));
        item.setItemMeta(MetaItem);
        return item;
    }

    // Get contestant
    public ArrayList<Contestant> getContestant() {
        return this.contestants;
    }

    public void setContestantsList(ArrayList<Contestant> contestants) {
        this.contestants = contestants;
    }

    // Time
    public int getTime() {
        return this.time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    // Other commands
    public void updateTimer() {
        if (!plugin.getGameManager().getGamesThatStarted().containsKey(this.uuid)) return;
        final UUID uuid = this.getUuid();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!plugin.getSQL().sqlSelect.gameExists(uuid)) {
                    cancel();
                    return;
                }
                Game game = plugin.getSQL().sqlSelect.uuidGame(uuid);
                if (game == null) {
                    cancel();
                    return;
                }

                if (game.getGameStatus() == 1) {
                    plugin.getGameManager().getGamesThatStarted().put(uuid, plugin.getGameManager().getGamesThatStarted().get(uuid) + 1);
                }
            }
        }.runTaskTimer(plugin, 1, 1 * 20L);
    }

    // All players
    public ArrayList<UUID> AllPlayersInsideGame() {
        ArrayList<UUID> uuidArrayList = new ArrayList<>();
        for (UUID singleHost : hosts) {
            uuidArrayList.add(singleHost);
        }

        return uuidArrayList;
    }
}
