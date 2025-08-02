package me.roovzi.duelplugin;

import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class DuelCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (!(s instanceof Player)) return false;

        Player sender = (Player) s;

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /duel <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null || !target.isOnline()) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }

        if (sender.equals(target)) {
            sender.sendMessage(ChatColor.RED + "You can't duel yourself!");
            return true;
        }

        if (DuelManager.inDuel.contains(sender.getUniqueId()) || DuelManager.inDuel.contains(target.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + "Either you or the target is already in a duel.");
            return true;
        }

        DuelManager.pendingDuels.put(target.getUniqueId(), sender.getUniqueId());

        sender.sendMessage(ChatColor.YELLOW + "Duel request sent to " + target.getName());

        TextComponent msg = new TextComponent(ChatColor.GREEN + sender.getName() + " has challenged you to a duel! ");
        TextComponent accept = new TextComponent("[ACCEPT]");
        accept.setColor(net.md_5.bungee.api.ChatColor.GREEN);
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/accept"));

        TextComponent deny = new TextComponent(" [DENY]");
        deny.setColor(net.md_5.bungee.api.ChatColor.RED);
        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/deny"));

        msg.addExtra(accept);
        msg.addExtra(deny);

        target.spigot().sendMessage(msg);

        return true;
    }
}
