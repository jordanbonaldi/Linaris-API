package net.neferett.linaris.mistery.mounts.mounts.spider;

import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;

import net.neferett.linaris.mistery.mounts.Mount;

public class SpiderMount extends Mount{

	public Spider Spider;
	
	public SpiderMount(Player player) {
		super("Spider", "Disco Spider", player);
	}

	@Override
	public boolean stayAlive() {
		if (player == null) return false;
		if (!player.isOnline()) return false;
		if (Spider.getPassenger() == null) return false;
		return true;
	}

	@Override
	public void onMount() {
		Spider = SpiderMountEntity.spawn(player.getLocation());
		Spider.setPassenger(player);
	}

	@Override
	public void onDismount() {
		if (Spider != null)
			if (!Spider.isDead())
				Spider.remove();
	}

	@Override
	public void onUpdate() {
		
	}

}
