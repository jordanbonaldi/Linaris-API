package net.neferett.linaris.commands;

import java.util.List;

import org.bukkit.Bukkit;

import net.neferett.linaris.PlayersHandler.PlayerManager;
import net.neferett.linaris.PlayersHandler.Players;

public class FreezeCommand extends CommandHandler {

	public FreezeCommand() {
		super("freeze", p -> p.getRank().getModerationLevel() > 1);
		this.setErrorMsg("§cCette commande est reservé au §eStaff§c !");
	}

	@Override
	public void cmd(final Players player, final String cmd, final List<String> args) {
		if (args.size() == 1 || Bukkit.getPlayer(args.get(1)) == null) {
			player.sendMessage("§cMerci de fournir un joueur");
			return;
		}
		final Players p = PlayerManager.get().getPlayer(Bukkit.getPlayer(args.get(1)));
		if (p.isFreeze()) {
			p.setFreeze(false);
			p.setWalkSpeed(0.2F);
		} else {
			p.sendMessage("§cVous avez été freeze un modo va venir vous examiner !");
			p.sendMessage("§cSi vous deconnectez vous serez automatiquement BANIP");
			p.setFreeze(true);
			p.setWalkSpeed(0);
		}
	}

	@Override
	public void onError(final Players p) {
		p.DisplayErrorMessage();
	}

}
