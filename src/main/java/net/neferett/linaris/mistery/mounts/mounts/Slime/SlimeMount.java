package net.neferett.linaris.mistery.mounts.mounts.Slime;

import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;

import net.neferett.linaris.mistery.mounts.Mount;

public class SlimeMount extends Mount{

	public Slime Slime;
	
	public SlimeMount(Player player) {
		super("Slime", "Disco Slime", player);
	}

	@Override
	public boolean stayAlive() {
		if (player == null) return false;
		if (!player.isOnline()) return false;
		if (Slime.getPassenger() == null) return false;
		return true;
	}

	@Override
	public void onMount() {
		Slime = SlimeMountEntity.spawn(player.getLocation());
		Slime.setPassenger(player);
	}

	@Override
	public void onDismount() {
		if (Slime != null)
			if (!Slime.isDead())
				Slime.remove();
	}

	@Override
	public void onUpdate() {
		
	}

}
