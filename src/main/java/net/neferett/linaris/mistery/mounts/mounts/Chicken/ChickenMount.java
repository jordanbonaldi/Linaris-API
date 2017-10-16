package net.neferett.linaris.mistery.mounts.mounts.Chicken;

import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;

import net.neferett.linaris.mistery.mounts.Mount;

public class ChickenMount extends Mount{

	public Chicken Chicken;
	
	public ChickenMount(Player player) {
		super("Chicken", "Disco Chicken", player);
	}

	@Override
	public boolean stayAlive() {
		if (player == null) return false;
		if (!player.isOnline()) return false;
		if (Chicken.getPassenger() == null) return false;
		return true;
	}

	@Override
	public void onMount() {
		Chicken = ChickenMountEntity.spawn(player.getLocation());
		Chicken.setPassenger(player);
	}

	@Override
	public void onDismount() {
		if (Chicken != null)
			if (!Chicken.isDead())
				Chicken.remove();
	}

	@Override
	public void onUpdate() {
		
	}

}
