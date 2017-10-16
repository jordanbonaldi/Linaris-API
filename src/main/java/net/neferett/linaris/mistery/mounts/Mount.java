package net.neferett.linaris.mistery.mounts;

import org.bukkit.entity.Player;

import net.neferett.linaris.utils.tasksmanager.TaskManager;

public abstract class Mount {

	private String id;
	private String name;
	
	private String _taskName;
	
	protected Player player;

	public Mount(String id,String name,Player player) {
		this.id = id;
		this.name = name;
		this.player = player;
		this._taskName = "Mount_" + id + "_" + player.getName();
		onMount();
		TaskManager.scheduleSyncRepeatingTask(this._taskName, this::update, 0, 1);
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
		if(!stayAlive()) {
			dismount();
			return;
		}
		onUpdate();
	}
	
	public void dismount() {
		TaskManager.cancelTaskByName(_taskName);
		onDismount();
	}
	
	public abstract boolean stayAlive();
	public abstract void onMount();
	public abstract void onDismount();
	
	public abstract void onUpdate();
	
}
