package net.neferett.linaris.commands;

import java.util.List;

import org.bukkit.Bukkit;

import net.neferett.linaris.PlayersHandler.Players;

public class TPCommands extends CommandHandler {

	public TPCommands() {
		super("tp", p -> p.getRank().getModerationLevel() > 1, "go");
		this.setErrorMsg("§cCette commande est reservé au §eStaff§c !");
	}

	@Override
	public void cmd(final Players player, final String cmd, final List<String> args) {
		if (args.size() == 1 || Bukkit.getPlayer(args.get(1)) == null) {
			player.sendMessage("§cMerci de fournir un joueur");
			return;
		} else if (args.size() == 3) {
			if (Bukkit.getPlayer(args.get(1)) == null || Bukkit.getPlayer(args.get(2)) == null) {
				player.sendMessage("§cLe joueur §e" + Bukkit.getPlayer(args.get(1)) == null ? args.get(1)
						: args.get(2) + "§c n'existe pas !");
				return;
			}
			Bukkit.getPlayer(args.get(2)).teleport(Bukkit.getPlayer(args.get(1)));
		}
		player.teleport(Bukkit.getPlayer(args.get(1)));
	}

	@Override
	public void onError(final Players p) {
		p.DisplayErrorMessage();
	}

}
