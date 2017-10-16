package net.neferett.linaris.mistery.mounts.mounts.sheep;

import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;

import net.neferett.linaris.mistery.mounts.Mount;

public class SuperSheep extends Mount{

	public Sheep sheep;
	
	public SuperSheep(Player player) {
		super("supersheep", "Disco Sheep", player);
	}

	@Override
	public boolean stayAlive() {
		if (player == null) return false;
		if (!player.isOnline()) return false;
		if (sheep.getPassenger() == null) return false;
		return true;
	}

	@Override
	public void onMount() {
		sheep = SuperSheepEntity.spawn(player.getLocation());
		sheep.setPassenger(player);
	}

	@Override
	public void onDismount() {
		if (sheep != null)
			if (!sheep.isDead())
				sheep.remove();
	}

	@Override
	public void onUpdate() {
		
	}

}
