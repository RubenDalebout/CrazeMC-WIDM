package io.github.deechtezeeuw.crazemcwidm.events;

import io.github.deechtezeeuw.crazemcwidm.CrazeMCWIDM;
import io.github.deechtezeeuw.crazemcwidm.classes.Contestant;
import io.github.deechtezeeuw.crazemcwidm.classes.Game;
import io.github.deechtezeeuw.crazemcwidm.classes.Peacekeeper;
import io.github.deechtezeeuw.crazemcwidm.gui.books.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class PlayerInteract implements Listener {
    private final CrazeMCWIDM plugin = CrazeMCWIDM.getInstance();
    private final ArrayList<Material> shulkers = new ArrayList<>();

    public PlayerInteract() {
        shulkers.add(Material.BLACK_SHULKER_BOX);
        shulkers.add(Material.BLUE_SHULKER_BOX);
        shulkers.add(Material.CYAN_SHULKER_BOX);
        shulkers.add(Material.GRAY_SHULKER_BOX);
        shulkers.add(Material.GREEN_SHULKER_BOX);
        shulkers.add(Material.LIGHT_BLUE_SHULKER_BOX);
        shulkers.add(Material.SILVER_SHULKER_BOX);
        shulkers.add(Material.LIME_SHULKER_BOX);
        shulkers.add(Material.MAGENTA_SHULKER_BOX);
        shulkers.add(Material.ORANGE_SHULKER_BOX);
        shulkers.add(Material.PINK_SHULKER_BOX);
        shulkers.add(Material.PURPLE_SHULKER_BOX);
        shulkers.add(Material.RED_SHULKER_BOX);
        shulkers.add(Material.WHITE_SHULKER_BOX);
        shulkers.add(Material.YELLOW_SHULKER_BOX);
    }

    @EventHandler
    public void onPlayerInterect(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        // Check if world is a game
        Game game = (plugin.getGameDataManager().worldIsPartOfGame(player.getWorld().getUID())) ? plugin.getGameDataManager().getWorldGame(player.getWorld().getUID()) : null;
        if (game == null) return;

        if (!game.isContestant(player.getUniqueId()) && !game.isHost(player.getUniqueId())) {
            e.setCancelled(true);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfigManager().getMain().serverPrefix + plugin.getConfigManager().getMain().serverDivider + "&cJe mag dit alleen doen als je in het spel zit!"));
            return;
        }

        Contestant contestant = null;
        for (Contestant singleContestant : game.getContestant()) {
            if (singleContestant.getPlayer() == null) continue;
            if (singleContestant.getPlayer().equals(player.getUniqueId())) {
                contestant = singleContestant;
                break;
            }
        }

        if (contestant == null) return;

        // Right click with item in hand
        if (e.getItem() != null) {
            // Check if item is book
            if (e.getItem().getType().equals(Material.BOOK)) {
                openBook(e.getItem(), player);
                return;
            }
        }

        // If clicked on block
        if (e.getClickedBlock() == null) return;
        Block clickedBlock = e.getClickedBlock();
        if (clickedBlock.getType() == null) return;

        // door click
        if (game.isContestant(player.getUniqueId()) && (clickedBlock.getType().equals(Material.ACACIA_DOOR) ||
                clickedBlock.getType().equals(Material.DARK_OAK_DOOR) ||
                clickedBlock.getType().equals(Material.BIRCH_DOOR) ||
                clickedBlock.getType().equals(Material.JUNGLE_DOOR)) ||
                clickedBlock.getType().equals(Material.SPRUCE_DOOR) ||
                clickedBlock.getType().equals(Material.TRAP_DOOR) ||
                clickedBlock.getType().equals(Material.WOOD_DOOR) ||
                clickedBlock.getType().equals(Material.WOODEN_DOOR)) {
            e.setCancelled(true);
            return;
        }

        // Check if player is interacting with shulker
        if (shulkers.contains(clickedBlock.getType())) checkIfUserCanOpenShulker(e, game, player, clickedBlock);

         //Check if player is interacting with chest while not playing
         if ((contestant.getDeath() || game.getGameStatus() != 1) && clickedBlock.getType().equals(Material.CHEST) ||
                 (contestant.getDeath() || game.getGameStatus() != 1) && clickedBlock.getType().equals(Material.TRAPPED_CHEST) ||
                 (contestant.getDeath() || game.getGameStatus() != 1) && clickedBlock.getType().equals(Material.HOPPER) ||
                 (contestant.getDeath() || game.getGameStatus() != 1) && clickedBlock.getType().equals(Material.HOPPER_MINECART) ||
                 (contestant.getDeath() || game.getGameStatus() != 1) && clickedBlock.getType().equals(Material.ANVIL) ||
                 (contestant.getDeath() || game.getGameStatus() != 1) && clickedBlock.getType().equals(Material.WORKBENCH) ||
                 (contestant.getDeath() || game.getGameStatus() != 1) && clickedBlock.getType().equals(Material.ENCHANTMENT_TABLE) ||
                 (contestant.getDeath() || game.getGameStatus() != 1) && clickedBlock.getType().equals(Material.ENDER_CHEST) ||
                 (contestant.getDeath() || game.getGameStatus() != 1) && clickedBlock.getType().equals(Material.FURNACE) ||
                 (contestant.getDeath() || game.getGameStatus() != 1) && clickedBlock.getType().equals(Material.BURNING_FURNACE) ||
                 (contestant.getDeath() || game.getGameStatus() != 1) && clickedBlock.getType().equals(Material.BED) ||
                 (contestant.getDeath() || game.getGameStatus() != 1) && clickedBlock.getType().equals(Material.DISPENSER) ||
                 (contestant.getDeath() || game.getGameStatus() != 1) && clickedBlock.getType().equals(Material.DROPPER) ||
                 (contestant.getDeath() || game.getGameStatus() != 1) && clickedBlock.getType().equals(Material.OBSERVER) ||
                 (contestant.getDeath() || game.getGameStatus() != 1) && clickedBlock.getType().equals(Material.MINECART) ||
                 (contestant.getDeath() || game.getGameStatus() != 1) && clickedBlock.getType().equals(Material.COMMAND_MINECART) ||
                 (contestant.getDeath() || game.getGameStatus() != 1) && clickedBlock.getType().equals(Material.EXPLOSIVE_MINECART) ||
                 (contestant.getDeath() || game.getGameStatus() != 1) && clickedBlock.getType().equals(Material.STORAGE_MINECART) ||
                 (contestant.getDeath() || game.getGameStatus() != 1) && clickedBlock.getType().equals(Material.BOAT) ||
                 (contestant.getDeath() || game.getGameStatus() != 1) && clickedBlock.getType().equals(Material.BOAT_ACACIA) ||
                 (contestant.getDeath() || game.getGameStatus() != 1) && clickedBlock.getType().equals(Material.BOAT_BIRCH) ||
                 (contestant.getDeath() || game.getGameStatus() != 1) && clickedBlock.getType().equals(Material.BOAT_DARK_OAK) ||
                 (contestant.getDeath() || game.getGameStatus() != 1) && clickedBlock.getType().equals(Material.BOAT_JUNGLE) ||
                 (contestant.getDeath() || game.getGameStatus() != 1) && clickedBlock.getType().equals(Material.BOAT_SPRUCE)) {
                e.setCancelled(true);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            plugin.getConfigManager().getMain().serverPrefix + plugin.getConfigManager().getMain().serverDivider + "&cJe kan dit niet gebruiken!"));
         }
    }

    // Open shulker
    protected void checkIfUserCanOpenShulker(PlayerInteractEvent e, Game game, Player player, Block clickedBlock) {
        // Game is not playing so deny acces
        if (game.getGameStatus() != 1) {
            e.setCancelled(true);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfigManager().getMain().serverPrefix + plugin.getConfigManager().getMain().serverDivider + "&cDe game is niet actief! Je kan nog geen shulkers openen!"));
            return;
        }

        // Get player contestant role
        Contestant contestant = null;
        for (Contestant singleContestant : game.getContestant()) {
            if (singleContestant.getPlayer() == null) continue;
            if (singleContestant.getPlayer().equals(player.getUniqueId())) {
                contestant = singleContestant;
            }
        }

        // Check if contestant is null
        if (contestant == null) return;

        // Check if user is allowed to open shulker
        if (!contestant.getShulkerMaterial().equals(clickedBlock.getType())) {
            e.setCancelled(true);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfigManager().getMain().serverPrefix + plugin.getConfigManager().getMain().serverDivider + "&cJe mag alleen je eigen kleur shulker openen!"));
            return;
        }

        ShulkerBox shulker = null;
        try {
            shulker = (ShulkerBox) clickedBlock.getState();
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        if (shulker == null) return;

        for (ItemStack item : shulker.getInventory().getContents()) {
            if (item == null) continue;
            if (item.getItemMeta() == null) continue;
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta.getDisplayName() == null) continue;
            if (item.getType() == null) continue;

            String strippedName = ChatColor.stripColor(itemMeta.getDisplayName()).replaceAll(" >>", "");

            // Check if item paper
            if (item.getType().equals(Material.PAPER)) {
                // Check title of roles
                // speler
                if (strippedName.equalsIgnoreCase("speler")) {
                    if (contestant.getRole() != 1) {
                        try {
                            contestant.setRole(1);
                            game.updateContestant(contestant);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    plugin.getConfigManager().getMain().serverPrefix + plugin.getConfigManager().getMain().serverDivider + "&cEr is iets fout gegaan!"));
                            return;
                        }
                        return;
                    }
                    return;
                }
                // Egoïst
                if (strippedName.equalsIgnoreCase("egoïst")) {
                    if (contestant.getRole() != 2) {
                        try {
                            contestant.setRole(2);
                            game.updateContestant(contestant);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    plugin.getConfigManager().getMain().serverPrefix + plugin.getConfigManager().getMain().serverDivider + "&cEr is iets fout gegaan!"));
                            return;
                        }
                        return;
                    }
                    return;
                }
                // Mol
                if (strippedName.equalsIgnoreCase("mol")) {
                    if (contestant.getRole() != 3) {
                        try {
                            contestant.setRole(3);
                            game.updateContestant(contestant);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    plugin.getConfigManager().getMain().serverPrefix + plugin.getConfigManager().getMain().serverDivider + "&cEr is iets fout gegaan!"));
                            return;
                        }
                        return;
                    }
                    return;
                }
                // Peacekeeper
                if (strippedName.equalsIgnoreCase("peacekeeper")) {
                    if (contestant.getPeacekeeper()) {
                        try {
                            contestant.setPeacekeeper(true);
                            game.updateContestant(contestant);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    plugin.getConfigManager().getMain().serverPrefix + plugin.getConfigManager().getMain().serverDivider + "&cEr is iets fout gegaan!"));
                            return;
                        }

                        // Peacekeeper armor
                        player.getInventory().setHelmet(new Peacekeeper().helmet());
                        player.getInventory().setChestplate(new Peacekeeper().chestplate());
                        player.getInventory().setLeggings(new Peacekeeper().leggings());
                        player.getInventory().setBoots(new Peacekeeper().leggings());
                        player.getInventory().setItemInOffHand(new Peacekeeper().sword());

                        for (Player singlePlayer : Bukkit.getServer().getWorld(game.getMap()).getPlayers()) {
                            singlePlayer.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    plugin.getConfigManager().getMain().serverPrefix + plugin.getConfigManager().getMain().serverDivider + contestant.getChatColor() + contestant.getColorName() + " &fis de &bPeacekeeper &fgeworden! Hij/zij moet 2 kills maken."));
                        }
                        return;
                    }
                    return;
                }
            }
        }
    }

    // Right click while having book in hand then open right menu
    protected void openBook (ItemStack book, Player player) {
        if (book == null) return;
        if (book.getItemMeta() == null) return;
        if (book.getItemMeta().getDisplayName() == null) return;

        String bookTitle = ChatColor.stripColor(book.getItemMeta().getDisplayName()).replaceAll(" >>", "").toLowerCase();

        if (!bookTitle.equalsIgnoreCase("deathnote") &&
                !bookTitle.equalsIgnoreCase("reborn") &&
                !bookTitle.equalsIgnoreCase("teleport") &&
                !bookTitle.equalsIgnoreCase("speler count") &&
                !bookTitle.equalsIgnoreCase("pk check") &&
                !bookTitle.equalsIgnoreCase("switch") &&
                !bookTitle.equalsIgnoreCase("invsee") &&
                !bookTitle.equalsIgnoreCase("itemcheck") &&
                !bookTitle.equalsIgnoreCase("itemclear") &&
                !bookTitle.equalsIgnoreCase("book lock")) return;

        // Check if world is a game
        Game game = (plugin.getGameDataManager().worldIsPartOfGame(player.getWorld().getUID())) ? plugin.getGameDataManager().getWorldGame(player.getWorld().getUID()) : null;
        if (game == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfigManager().getMain().serverPrefix + plugin.getConfigManager().getMain().serverDivider + "&cHet boekje kan alleen gebruikt worden in een game!"));
            return;
        }

        // Check if game is playing
        if (game.getGameStatus() != 1) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfigManager().getMain().serverPrefix + plugin.getConfigManager().getMain().serverDivider + "&cJe kan dit boekje alleen gebruiken als de game bezig is!"));
            return;
        }

        // Check if player is contestant
        if (!game.isContestant(player.getUniqueId())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfigManager().getMain().serverPrefix + plugin.getConfigManager().getMain().serverDivider + "&cJe kan dit alleen gebruiken als speler!"));
            return;
        }

        Contestant contestant = game.getContestant(player.getUniqueId());

        if (contestant.hasBooklock()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfigManager().getMain().serverPrefix + plugin.getConfigManager().getMain().serverDivider + "&cJe kan dit boekje niet gebruiken, want je hebt een book lock!"));
            return;
        }

        switch (bookTitle) {
            case "deathnote":
                new DeathNote().open(player);
                break;
            case "reborn":
                new Reborn().open(player);
                break;
            case "teleport":
                new Teleport().open(player);
                break;
            case "speler count":
                int alive = 0;

                for (Contestant singleContestant : game.getContestant()) {
                    if (singleContestant.getPlayer() == null || singleContestant.getRole() != 1 || singleContestant.getDeath()) continue;
                    alive++;
                }

                for (Player singlePlayer : Bukkit.getServer().getWorld(game.getMap()).getPlayers()) {
                    singlePlayer.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            plugin.getConfigManager().getMain().serverPrefix + plugin.getConfigManager().getMain().serverDivider + "&fEr zijn op het moment &7&l(&f&l" + alive + "&7&l) &fspelers levend!"));
                }

                player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                break;
            case "pk check":
                new PKCheck().open(player);
                break;
            case "switch":
                new Switch().open(player);
                break;
            case "invsee":
                new Invsee().open(player);
                break;
            case "itemcheck":
                new ItemCheck().open(player);
                break;
            case "itemclear":
                new ItemClear().open(player);
                break;
            case "book lock":
                new Booklock().open(player);
                break;
        }
    }
}
