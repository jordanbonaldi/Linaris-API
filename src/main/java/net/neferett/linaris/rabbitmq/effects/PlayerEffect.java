package net.neferett.linaris.rabbitmq.effects;

import org.bukkit.entity.Player;

public abstract class PlayerEffect {

	String name;
	
	public PlayerEffect(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public abstract void use(Player p, Object[] args);
}
