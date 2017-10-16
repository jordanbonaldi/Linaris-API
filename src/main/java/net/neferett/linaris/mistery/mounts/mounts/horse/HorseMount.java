package net.neferett.linaris.mistery.mounts.mounts.horse;

import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import net.neferett.linaris.mistery.mounts.Mount;

public class HorseMount extends Mount{

	public Horse Horse;
	
	public HorseMount(Player player) {
		super("Horse", "Disco Horse", player);
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
		Horse = HorseMountEntity.spawn(player.getLocation());
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
