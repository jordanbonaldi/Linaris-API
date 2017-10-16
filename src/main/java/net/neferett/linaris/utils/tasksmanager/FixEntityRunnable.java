package net.neferett.linaris.utils.tasksmanager;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class FixEntityRunnable implements Runnable {

	protected String	taskName;
	protected Entity	entity;
	protected long		end;
	protected boolean	fixView;
	protected Location	location;

	public FixEntityRunnable(String taskName, Entity entity, Location location, int duration, boolean fixView) {
		this.taskName = taskName;
		this.entity = entity;
		this.fixView = fixView;
		this.location = location;
		this.end = System.currentTimeMillis() + (duration > 0 ? duration * 1000 : Integer.MAX_VALUE);
	}

	@Override
	public void run() {
		if (entity.isDead()) {
			TaskManager.cancelTaskByName(taskName);
			return;
		}

		if (System.currentTimeMillis() < end) {
			TaskManager.cancelTaskByName(taskName);
			return;
		}
		if (entity.isValid()) {
			if (!fixView) {
				location.setYaw(entity.getLocation().getYaw());
				location.setPitch(entity.getLocation().getPitch());
			}
			entity.teleport(location);
		}
	}
}
