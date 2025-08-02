package me.roovzi.duelplugin;

import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DenyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (!(s instanceof Player)) return false;

        Player target = (Player) s;
        UUID challengerId = DuelManager.pendingDuels.remove(target.getUniqueId());

        if (challengerId == null) {
            target.sendMessage(ChatColor.RED + "You have no pending duel requests.");
            return true;
        }

        target.sendMessage(ChatColor.YELLOW + "You denied the duel request.");
        Player challenger = target.getServer().getPlayer(challengerId);
        if (challenger != null) {
            challenger.sendMessage(ChatColor.RED + target.getName() + " denied your duel request.");
        }

        return true;
    }
}
