package net.neferett.linaris.mistery.mounts.mounts.skeletonhorse;

import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import net.neferett.linaris.mistery.mounts.Mount;

public class SkeletonHorseMount extends Mount{

	public Horse Horse;
	
	public SkeletonHorseMount(Player player) {
		super("SkeletonHorse", "Disco Horse", player);
	}

	@Override
	public boolean stayAlive() {
		if (player == null) return false;
		if (!player.isOnline()) return false;
		if (Horse.getPassenger() == null) return false;
		return true;
	}

	@Override
	public void onMount() {
		Horse = SkeletonHorseMountEntity.spawn(player.getLocation());
		Horse.setPassenger(player);
	}

	@Override
	public void onDismount() {
		if (Horse != null)
			if (!Horse.isDead())
				Horse.remove();
	}

	@Override
	public void onUpdate() {
		
	}

}
