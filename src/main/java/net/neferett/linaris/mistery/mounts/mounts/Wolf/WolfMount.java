package net.neferett.linaris.mistery.mounts.mounts.Wolf;

import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import net.neferett.linaris.mistery.mounts.Mount;

public class WolfMount extends Mount{

	public Wolf Wolf;
	
	public WolfMount(Player player) {
		super("Wolf", "Disco Wolf", player);
	}

	@Override
	public boolean stayAlive() {
		if (player == null) return false;
		if (!player.isOnline()) return false;
		if (Wolf.getPassenger() == null) return false;
		return true;
	}

	@Override
	public void onMount() {
		Wolf = WolfMountEntity.spawn(player.getLocation());
		Wolf.setPassenger(player);
	}

	@Override
	public void onDismount() {
		if (Wolf != null)
			if (!Wolf.isDead())
				Wolf.remove();
	}

	@Override
	public void onUpdate() {
		
	}

}
