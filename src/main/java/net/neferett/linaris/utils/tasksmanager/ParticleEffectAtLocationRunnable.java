package net.neferett.linaris.utils.tasksmanager;

import org.bukkit.Location;

import net.neferett.linaris.utils.Particles;

public class ParticleEffectAtLocationRunnable implements Runnable {

	protected String			taskName;
	protected Location			location;
	protected Particles	particleEffect;
	protected int				particleNumber;
	protected float				speed, xOffSet, yOffSet, zOffSet;
	protected long				end;
	protected double			range;

	public ParticleEffectAtLocationRunnable(Location location, int duration, Particles particleEffect, int particleNumber, float speed, float xOffSet, float yOffset, float zOffset, double range,
			String taskName) {
		this.taskName = taskName;
		this.location = location;
		this.end = System.currentTimeMillis() + (duration > 0 ? duration * 1000 : Integer.MAX_VALUE);
		this.particleEffect = particleEffect;
		this.particleNumber = particleNumber;
		this.speed = speed;
		this.xOffSet = xOffSet;
		this.yOffSet = yOffset;
		this.zOffSet = zOffset;
		this.range = range;
	}

	@Override
	public void run() {
		if (location != null && System.currentTimeMillis() < end) particleEffect.display(xOffSet, yOffSet, zOffSet, speed, particleNumber, location, range);
		else TaskManager.cancelTaskByName(taskName);
	}
}
