package net.neferett.linaris.utils.floatingtext;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

public class Hologram {

	static List<Hologram> holos = new ArrayList<Hologram>();
	
	public static List<Hologram> getHolos() {
		return holos;
	}
	
	public static void removes() {
		for (Hologram holo : holos)
			holo.remove();
	}
	
	List<ArmorStand> stands;
	boolean spawned;
	Location location;
	
	public Hologram(Location location,List<String> messages) {
		stands = new ArrayList<ArmorStand>();
		location.setY(location.getY()-1.23);
		this.location = location;
		Location loc = location.clone();
		World world = location.getWorld();
		for (String message : messages) {
			ArmorStand armor = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);
			armor.setVisible(false);
			armor.setCustomNameVisible(true);
			armor.setCustomName(message);
			armor.setGravity(false);
			loc.setY(loc.getY() - 0.30D);
			stands.add(armor);
		}
		holos.add(this);
		setSpawned(true);
	}
	
	public void setSpawned(boolean spawned) {
		this.spawned = spawned;
	}
	
	public void setMessages(List<String> messages) {
		for (ArmorStand armor : this.stands) 
			armor.remove();

		Location loc = location.clone();
		World world = loc.getWorld();
		for (String message : messages) {
			ArmorStand armor = (ArmorStand) world.spawnEntity(loc, EntityType.ARMOR_STAND);
			armor.setVisible(false);
			armor.setCustomNameVisible(true);
			armor.setCustomName(message);
			armor.setGravity(false);
			loc.setY(loc.getY() - 0.30D);
		}
	}
	
	public void remove() {
		setSpawned(false);
		for (ArmorStand armor : stands) 
			armor.remove();
	}

}
