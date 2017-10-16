package net.neferett.linaris.api.pets;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import net.neferett.linaris.api.Games;
import net.neferett.linaris.api.PlayerLocalManager;
import net.neferett.linaris.api.SettingsManager;
import net.neferett.linaris.utils.tasksmanager.TaskManager;

public abstract class Pet {

	private String id;
	private String name;

	private String _taskName;

	protected Player player;

	private boolean crounched;

	private List<String> players;

	public Pet(String id, String name, Player player) {
		this.id = id;
		this.name = name;
		this.player = player;
		this.players = new ArrayList<String>();
		this._taskName = "Pet_" + id + "_" + player.getName();
		onSpawn();
		TaskManager.scheduleSyncRepeatingTask(this._taskName, this::update, 0, 1);
		PlayerLocalManager.get().getPlayerLocal(player.getName()).setPet(this);
		if (player.isSneaking())
			crounch(true);
	}

	public String getID() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Player getPlayer() {
		return player;
	}

	public void update() {
		if (!stayAlive()) {
			dispawn();
			return;
		}
		
		List<String> destroys = new ArrayList<String>();
		for (String names : players) {
			Player target = Bukkit.getPlayer(names);
			if (target==null) {
				destroys.add(names);
				continue;
			}
			if (names.equals(player.getName().toLowerCase())) {
				boolean see = SettingsManager.isEnabled(names, Games.PETS, "pets-see-own",true);
				if (!see) {
					onDispawn(target);
					destroys.add(names);
				}
			} else {
				boolean see = SettingsManager.isEnabled(names, Games.PETS, "pets-see-others",true);
				if (!see) {
					onDispawn(target);
					destroys.add(names);
				}
			}
		}
		
		for (String names : destroys) {
			players.remove(names);
		}
		
		Bukkit.getOnlinePlayers().forEach((p) -> {
			
			if (!alreadySee(p)) {
				String name = p.getName().toLowerCase();
				if (name.equals(player.getName().toLowerCase())) {
					boolean see = SettingsManager.isEnabled(name, Games.PETS, "pets-see-own",true);
					if (see) {
						onSpawn(p);
						addSee(p);
					}
				} else {
					boolean see = SettingsManager.isEnabled(name, Games.PETS, "pets-see-others",true);
					if (see) {
						onSpawn(p);
						addSee(p);
					}
				}
			}
			
		});
		onUpdate();
	}

	public void dispawn() {
		TaskManager.cancelTaskByName(_taskName);
		onDispawn();
		for (String names : players) {
			Player target = Bukkit.getPlayer(names);
			if (target!=null)
				onDispawn(target);
		}
	}

	public void crounch(boolean crounch) {
		if (crounch) {
			this.crounched = true;
			for (String names : players) {
				Player target = Bukkit.getPlayer(names);
				if (target!=null)
					onDispawn(target);
			}
			onCrounch();
		} else {
			this.crounched = false;
			onUnCrounch();
			for (String names : players) {
				Player target = Bukkit.getPlayer(names);
				if (target!=null)
					onSpawn(target);
			}
		}
	}

	public boolean isCrounched() {
		return this.crounched;
	}

	public abstract boolean stayAlive();

	public abstract void onSpawn();

	public abstract void onDispawn();
	
	public abstract void onSpawn(Player target);

	public abstract void onDispawn(Player target);
	
	public abstract void onCrounch();

	public abstract void onUnCrounch();

	public abstract void onUpdate();

	public Location getShoulder() {
		if (player == null)
			return null;
		if (!player.isOnline())
			return null;
		Location location = player.getLocation();
		double space = 0.2;
		double defX = location.getX() - (space * 18 / 2) + space;
		double x = defX;
		double y = location.clone().getY() + 2;
		double z = location.clone().getZ();
		double angle = -((location.getYaw() + 180) / 60);
		angle += (location.getYaw() < -180 ? 3.25 : 2.985);

		x += space * 4.85;
		y -= space * 2;
		z -= 0.3;

		Location target = location.clone();
		target.setX(x);
		target.setZ(z);
		target.setY(y);

		Vector v = target.toVector().subtract(location.toVector());
		Vector v2 = getBackVector(location);
		v = rotateAroundAxisY(v, angle);
		v2.setY(0).multiply(-0.2);

		location.add(v);
		location.add(v2);

		return location;

	}
	
	public boolean alreadySee(Player player) {
		return players.contains(player.getName().toLowerCase());
	}
	
	public void addSee(Player player) {
		players.add(player.getName().toLowerCase());
	}
	
	public void removeSee(Player player) {
		players.remove(player.getName().toLowerCase());
	}
	
	public List<Player> getSeePlayers() {
		List<Player> players = new ArrayList<>();
		for (String name : this.players) {
			Player target = Bukkit.getPlayer(name);
			if (target!=null)
				players.add(target);
		}
		return players;
	}

	public static Vector rotateAroundAxisY(Vector v, double angle) {
		double x, z, cos, sin;
		cos = Math.cos(angle);
		sin = Math.sin(angle);
		x = v.getX() * cos + v.getZ() * sin;
		z = v.getX() * -sin + v.getZ() * cos;
		return v.setX(x).setZ(z);
	}

	public static Vector getBackVector(Location loc) {
		final float newZ = (float) (loc.getZ() + (1 * Math.sin(Math.toRadians(loc.getYaw() + 90 * 1))));
		final float newX = (float) (loc.getX() + (1 * Math.cos(Math.toRadians(loc.getYaw() + 90 * 1))));
		return new Vector(newX - loc.getX(), 0, newZ - loc.getZ());
	}
}
