package net.neferett.linaris.mistery.mounts.mounts.blaze;

import org.bukkit.entity.Blaze;
import org.bukkit.entity.Player;

import net.neferett.linaris.mistery.mounts.Mount;

public class BlazeMount extends Mount{

	public Blaze Blaze;
	
	public BlazeMount(Player player) {
		super("Blaze", "Disco Blaze", player);
	}

	@Override
	public boolean stayAlive() {
		if (player == null) return false;
		if (!player.isOnline()) return false;
		if (Blaze.getPassenger() == null) return false;
		return true;
	}

	@Override
	public void onMount() {
		Blaze = BlazeMountEntity.spawn(player.getLocation());
		Blaze.setPassenger(player);
	}

	@Override
	public void onDismount() {
		if (Blaze != null)
			if (!Blaze.isDead())
				Blaze.remove();
	}

	@Override
	public void onUpdate() {
		
	}

}
