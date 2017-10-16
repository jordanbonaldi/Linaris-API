package net.neferett.linaris.commands;

import java.util.Date;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.utils.RestartTask;

public class RebootCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.isOp()) return true;
            player.sendMessage("§e-----------------------------------------------------");
            player.sendMessage("§bServeur: §a" + BukkitAPI.get().getServerInfos().getServerName() + " §7 §e" + new Date().toString());
            player.sendMessage("§cReboot du serveurs dans 30 seconds...");
            player.sendMessage("§e-----------------------------------------------------");
            RestartTask.reboot();
        } else {
        	RestartTask.reboot();
        	return true;
        }
        return false;
    }

}
