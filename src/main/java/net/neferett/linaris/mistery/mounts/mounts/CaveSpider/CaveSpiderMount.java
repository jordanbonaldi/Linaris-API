package net.neferett.linaris.mistery.mounts.mounts.CaveSpider;

import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Player;

import net.neferett.linaris.mistery.mounts.Mount;

public class CaveSpiderMount extends Mount{

	public CaveSpider CaveSpider;
	
	public CaveSpiderMount(Player player) {
		super("CaveSpider", "Disco CaveSpider", player);
	}

	@Override
	public boolean stayAlive() {
		if (player == null) return false;
		if (!player.isOnline()) return false;
		if (CaveSpider.getPassenger() == null) return false;
		return true;
	}

	@Override
	public void onMount() {
		CaveSpider = CaveSpiderMountEntity.spawn(player.getLocation());
		CaveSpider.setPassenger(player);
	}

	@Override
	public void onDismount() {
		if (CaveSpider != null)
			if (!CaveSpider.isDead())
				CaveSpider.remove();
	}

	@Override
	public void onUpdate() {
		
	}

}
