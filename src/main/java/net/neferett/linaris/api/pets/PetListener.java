package net.neferett.linaris.api.pets;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.api.PlayerLocal;

public class PetListener implements Listener {
	
	@EventHandler
	public void onPlayerSneak(PlayerToggleSneakEvent e) {
		PlayerLocal pl = BukkitAPI.get().getPlayerLocalManager().getPlayerLocal(e.getPlayer().getName());
		if (!e.isCancelled())
			if (pl.havePet())
				pl.getPet().crounch(e.isSneaking());
	}
	
	@EventHandler
	public void onPlayerSneak(PlayerGameModeChangeEvent e) {
		PlayerLocal pl = BukkitAPI.get().getPlayerLocalManager().getPlayerLocal(e.getPlayer().getName());
		if (!e.isCancelled())
			if (pl.havePet())
				pl.getPet().crounch(e.getNewGameMode() == GameMode.SPECTATOR ? true : false);
	}

}
