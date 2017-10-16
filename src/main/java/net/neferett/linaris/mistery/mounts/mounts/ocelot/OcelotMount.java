package net.neferett.linaris.mistery.mounts.mounts.ocelot;

import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;

import net.neferett.linaris.mistery.mounts.Mount;

public class OcelotMount extends Mount{

	public Ocelot Ocelot;
	
	public OcelotMount(Player player) {
		super("Ocelot", "Disco Ocelot", player);
	}

	@Override
	public boolean stayAlive() {
		if (player == null) return false;
		if (!player.isOnline()) return false;
		if (Ocelot.getPassenger() == null) return false;
		return true;
	}

	@Override
	public void onMount() {
		Ocelot = OcelotMountEntity.spawn(player.getLocation());
		Ocelot.setPassenger(player);
	}

	@Override
	public void onDismount() {
		if (Ocelot != null)
			if (!Ocelot.isDead())
				Ocelot.remove();
	}

	@Override
	public void onUpdate() {
		
	}

}
