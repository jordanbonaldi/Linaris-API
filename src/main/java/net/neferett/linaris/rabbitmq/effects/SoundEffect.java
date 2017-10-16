package net.neferett.linaris.rabbitmq.effects;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundEffect extends PlayerEffect {

	public SoundEffect() {
		super("sounds");
	}

	@Override
	public void use(Player p, Object[] args) {

		if (args.length == 3) {
			String sound = (String) args[0];
			Integer volume = (Integer) args[1];
			Integer pitch =  (Integer) args[2];

			Sound ssound = Sound.valueOf(sound);
			p.playSound(p.getLocation(), ssound, volume, pitch);
		}

	}

}
