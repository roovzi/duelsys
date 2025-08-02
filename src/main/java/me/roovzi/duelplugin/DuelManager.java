package me.roovzi.duelplugin;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class DuelManager implements Listener {

    public static Map<UUID, UUID> pendingDuels = new HashMap<>(); // challenger -> target
    public static Set<UUID> inDuel = new HashSet<>();

    private static final Location arena1 = new Location(Bukkit.getWorld("world"), 100, 80, 100);
    private static final Location arena2 = new Location(Bukkit.getWorld("world"), 110, 80, 100);

    public static void sendKit(Player p) {
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);

        p.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD));
        p.getInventory().addItem(new ItemStack(Material.WOOL, 64, (short) 0));

        ItemStack leatherHelmet = new ItemStack(Material.LEATHER_HELMET);
        ItemStack leatherChest = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemStack leatherLegs = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemStack leatherBoots = new ItemStack(Material.LEATHER_BOOTS);

        p.getInventory().setHelmet(leatherHelmet);
        p.getInventory().setChestplate(leatherChest);
        p.getInventory().setLeggings(leatherLegs);
        p.getInventory().setBoots(leatherBoots);
    }

    public static void startDuel(Player p1, Player p2) {
        inDuel.add(p1.getUniqueId());
        inDuel.add(p2.getUniqueId());

        p1.teleport(arena1);
        p2.teleport(arena2);

        sendKit(p1);
        sendKit(p2);

        p1.sendMessage(ChatColor.GREEN + "Duel started with " + p2.getName() + "!");
        p2.sendMessage(ChatColor.GREEN + "Duel started with " + p1.getName() + "!");
    }

    public static void endDuel(Player winner, Player loser) {
        inDuel.remove(winner.getUniqueId());
        inDuel.remove(loser.getUniqueId());

        winner.sendMessage(ChatColor.GOLD + "You have won the duel!");
        loser.sendMessage(ChatColor.RED + "You have lost the duel!");

        Bukkit.broadcastMessage(ChatColor.AQUA + winner.getName() + " has won a duel against " + loser.getName() + "!");
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player dead = e.getEntity();
        if (inDuel.contains(dead.getUniqueId())) {
            Player killer = dead.getKiller();
            if (killer != null && inDuel.contains(killer.getUniqueId())) {
                endDuel(killer, dead);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        pendingDuels.values().remove(uuid);
        pendingDuels.remove(uuid);
        inDuel.remove(uuid);
    }
}
