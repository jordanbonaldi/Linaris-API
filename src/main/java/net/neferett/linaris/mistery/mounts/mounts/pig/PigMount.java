package net.neferett.linaris.mistery.mounts.mounts.pig;

import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;

import net.neferett.linaris.mistery.mounts.Mount;

public class PigMount extends Mount{

	public Pig pig;
	
	public PigMount(Player player) {
		super("Pig", "Disco pig", player);
	}

	@Override
	public boolean stayAlive() {
		if (player == null) return false;
		if (!player.isOnline()) return false;
		if (pig.getPassenger() == null) return false;
		return true;
	}

	@Override
	public void onMount() {
		pig = PigMountEntity.spawn(player.getLocation());
		pig.setPassenger(player);
	}

	@Override
	public void onDismount() {
		if (pig != null)
			if (!pig.isDead())
				pig.remove();
	}

	@Override
	public void onUpdate() {
		
	}

}
