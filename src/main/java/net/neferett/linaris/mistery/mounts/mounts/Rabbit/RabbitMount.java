package net.neferett.linaris.mistery.mounts.mounts.Rabbit;

import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;

import net.neferett.linaris.mistery.mounts.Mount;

public class RabbitMount extends Mount{

	public Rabbit Rabbit;
	
	public RabbitMount(Player player) {
		super("Rabbit", "Disco Rabbit", player);
	}

	@Override
	public boolean stayAlive() {
		if (player == null) return false;
		if (!player.isOnline()) return false;
		if (Rabbit.getPassenger() == null) return false;
		return true;
	}

	@Override
	public void onMount() {
		Rabbit = RabbitMountEntity.spawn(player.getLocation());
		Rabbit.setPassenger(player);
	}

	@Override
	public void onDismount() {
		if (Rabbit != null)
			if (!Rabbit.isDead())
				Rabbit.remove();
	}

	@Override
	public void onUpdate() {
		
	}

}
