package net.neferett.linaris.commands;

import java.util.List;
import java.util.Objects;

import net.neferett.linaris.PlayersHandler.Players;
import net.neferett.linaris.logo.gui.color.ColorShop;
import net.neferett.linaris.utils.gui.GuiManager;

public class ColorCommand extends CommandHandler {

	public ColorCommand() {
		super("color", Objects::nonNull);
	}

	@Override
	public void cmd(final Players player, final String cmd, final List<String> args) {
		GuiManager.openGui(new ColorShop(player.getPlayerData(), player.getName()));
	}

	@Override
	public void onError(final Players p) {
		p.DisplayErrorMessage();
	}

}
