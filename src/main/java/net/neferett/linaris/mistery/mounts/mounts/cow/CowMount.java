package net.neferett.linaris.mistery.mounts.mounts.cow;

import org.bukkit.entity.Cow;
import org.bukkit.entity.Player;

import net.neferett.linaris.mistery.mounts.Mount;

public class CowMount extends Mount{

	public Cow Cow;
	
	public CowMount(Player player) {
		super("Cow", "Disco Cow", player);
	}

	@Override
	public boolean stayAlive() {
		if (player == null) return false;
		if (!player.isOnline()) return false;
		if (Cow.getPassenger() == null) return false;
		return true;
	}

	@Override
	public void onMount() {
		Cow = CowMountEntity.spawn(player.getLocation());
		Cow.setPassenger(player);
	}

	@Override
	public void onDismount() {
		if (Cow != null)
			if (!Cow.isDead())
				Cow.remove();
	}

	@Override
	public void onUpdate() {
		
	}

}
