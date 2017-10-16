package net.neferett.linaris.utils.tasksmanager;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import net.neferett.linaris.utils.Particles;

public class ParticleEffectOnEntityRunnable implements Runnable {

	protected String			taskName;
	protected Entity			entity;
	protected Particles	particleEffect;
	protected int				particleNumber;
	protected float				speed, xOffSet, yOffSet, zOffSet;
	protected Vector			vector;
	protected long				end;
	protected double			range;

	public ParticleEffectOnEntityRunnable(Entity entity, int duration, Particles particleEffect, int particleNumber, float speed, float xOffSet, float yOffset, float zOffset, double range,
			Vector vector, String taskName) {
		this.taskName = taskName;
		this.entity = entity;
		this.end = System.currentTimeMillis() + (duration > 0 ? duration * 1000 : Integer.MAX_VALUE);
		this.particleEffect = particleEffect;
		this.particleNumber = particleNumber;
		this.speed = speed;
		this.xOffSet = xOffSet;
		this.yOffSet = yOffset;
		this.zOffSet = zOffset;
		this.vector = vector;
		this.range = range;
	}

	@Override
	public void run() {
		if (entity != null && entity.isValid() && System.currentTimeMillis() < end) particleEffect.display(xOffSet, yOffSet, zOffSet, speed, particleNumber,
				vector != null ? entity.getLocation().add(vector) : entity.getLocation(), range);
		else TaskManager.cancelTaskByName(taskName);
	}
}
