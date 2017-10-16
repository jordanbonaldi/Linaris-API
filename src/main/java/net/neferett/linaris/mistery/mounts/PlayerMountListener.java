package net.neferett.linaris.mistery.mounts;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerMountListener implements Listener {


	
	@EventHandler
	public void onDismount(PlayerTeleportEvent e){
		if (e.getPlayer().isInsideVehicle())
			e.setTo(e.getFrom());
	}
}
