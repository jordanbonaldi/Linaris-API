package net.neferett.linaris.commands;

import java.util.List;

import org.bukkit.Bukkit;

import net.neferett.linaris.PlayersHandler.PlayerManager;
import net.neferett.linaris.PlayersHandler.Players;
import net.neferett.linaris.utils.gui.GuiManager;

public class InvseeCommand extends CommandHandler {

	public InvseeCommand() {
		super("invsee", p -> p.getRank().getModerationLevel() > 1);
		this.setErrorMsg("§cCette commande est reservé au §eStaff§c !");
	}

	@Override
	public void cmd(final Players player, final String cmd, final List<String> args) {
		if (args.size() == 1 || Bukkit.getPlayer(args.get(1)) == null) {
			player.sendMessage("§cMerci de fournir un joueur");
			return;
		}
		final Players p = PlayerManager.get().getPlayer(Bukkit.getPlayer(args.get(1)));
		GuiManager.openGui(p.seeInventory(player));
	}

	@Override
	public void onError(final Players p) {
		p.DisplayErrorMessage();
	}

}
