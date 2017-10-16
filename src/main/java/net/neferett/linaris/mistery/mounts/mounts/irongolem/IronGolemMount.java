package net.neferett.linaris.mistery.mounts.mounts.irongolem;

import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;

import net.neferett.linaris.mistery.mounts.Mount;

public class IronGolemMount extends Mount{

	public IronGolem IronGolem;
	
	public IronGolemMount(Player player) {
		super("IronGolem", "Disco IronGolem", player);
	}

	@Override
	public boolean stayAlive() {
		if (player == null) return false;
		if (!player.isOnline()) return false;
		if (IronGolem.getPassenger() == null) return false;
		return true;
	}

	@Override
	public void onMount() {
		IronGolem = IronGolemMountEntity.spawn(player.getLocation());
		IronGolem.setPassenger(player);
	}

	@Override
	public void onDismount() {
		if (IronGolem != null)
			if (!IronGolem.isDead())
				IronGolem.remove();
	}

	@Override
	public void onUpdate() {
		
	}

}
