package net.neferett.linaris.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.neferett.linaris.api.Games;
import net.neferett.linaris.utils.QueueUtils;

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            QueueUtils.addInQueue((Player) sender, Games.UHCRUN);
        }
        return false;
    }

}
