package net.neferett.linaris.mistery.mounts.mounts.wither;

import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;

import net.neferett.linaris.mistery.mounts.Mount;

public class WitherMount extends Mount{

	public Wither Wither;
	
	public WitherMount(Player player) {
		super("Wither", "Disco Wither", player);
	}

	@Override
	public boolean stayAlive() {
		if (player == null) return false;
		if (!player.isOnline()) return false;
		if (Wither.getPassenger() == null) return false;
		return true;
	}

	@Override
	public void onMount() {
		Wither = WitherMountEntity.spawn(player.getLocation());
		Wither.setPassenger(player);
	}

	@Override
	public void onDismount() {
		if (Wither != null)
			if (!Wither.isDead())
				Wither.remove();
	}

	@Override
	public void onUpdate() {
		
	}

}
