package me.roovzi.duelplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class AcceptCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (!(s instanceof Player)) return false;

        Player target = (Player) s;
        UUID challengerId = DuelManager.pendingDuels.remove(target.getUniqueId());

        if (challengerId == null) {
            target.sendMessage(ChatColor.RED + "You have no pending duel requests.");
            return true;
        }

        Player challenger = Bukkit.getPlayer(challengerId);
        if (challenger == null || !challenger.isOnline()) {
            target.sendMessage(ChatColor.RED + "The challenger is no longer online.");
            return true;
        }

        DuelManager.startDuel(challenger, target);
        return true;
    }
}
