package net.neferett.linaris.ghostplayers.events;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerQuitEvent;

import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.ghostplayers.GhostManager;

public class ActionOnPlayerQuit extends ActionOnEvent {
	@Override
	public boolean shouldFire(final Event event) {
		final PlayerQuitEvent e = (PlayerQuitEvent) event;
		BukkitAPI.get().getTasksManager().addTask(() -> {

			BukkitAPI.get().getPlayerLocalManager().unload(e.getPlayer().getName());
			BukkitAPI.get().getPlayerDataManager().unload(e.getPlayer().getName());
			BukkitAPI.get().heartbeat();
			
		});
		if (GhostManager.isGhost(e.getPlayer())) {
			GhostManager.removeGhost(e.getPlayer());
			e.setQuitMessage((String) null);
			return false;
		}
		return true;
	}
}
