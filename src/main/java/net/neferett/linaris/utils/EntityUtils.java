package net.neferett.linaris.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import net.neferett.linaris.utils.tasksmanager.FixEntityRunnable;
import net.neferett.linaris.utils.tasksmanager.TaskManager;

/**
 * @author Likaos
 * @author Unreal852
 */
public class EntityUtils {

	/**
	 * Correspond à un damage, test pour ne pas provoquer le bug de knockback
	 * 1.8 des joueurs qui volent.
	 *
	 * @param livingEntity
	 */
	public static void damage(final LivingEntity livingEntity, final double value) {
		if (livingEntity.isDead())
			return;
		if (value <= 0)
			return;
		// int newLife = (int) (livingEntity.getHealth() - value);
		// Tué
		livingEntity.damage(value);
		livingEntity.setVelocity(new Vector(0, 0, 0));
	}

	/**
	 * Fix la position d'une entity (téléport en boucle)
	 *
	 * @param entity
	 * @param duration
	 *            en secondes
	 * @param location
	 * @param delay
	 *            avant début de la tâche
	 * @param refresh
	 *            vitesse de refresh
	 * @param fixView
	 *            bloque ou non la tête ?
	 */
	public static void fixEntity(final Entity entity, final int duration, final Location location, final int delay,
			final int refresh, final boolean fixView) {
		final String taskName = "EntityFix" + entity.getUniqueId();
		if (TaskManager.taskExist(taskName))
			return;
		TaskManager.scheduleSyncRepeatingTask(taskName,
				new FixEntityRunnable(taskName, entity, location, duration, fixView), delay, refresh);
	}

	public static Entity getEntityNear(final Player p, final int range) {
		final List<Entity> en = p.getNearbyEntities(range, range, range);
		final BlockIterator bItr = new BlockIterator(p, range);
		Block block;
		while (bItr.hasNext()) {
			block = bItr.next();
			for (final Entity e : en)
				if (block.getX() - .75 <= e.getLocation().getX() && e.getLocation().getX() <= block.getX() + 1.75
						&& block.getZ() - .75 <= e.getLocation().getZ() && e.getLocation().getZ() <= block.getZ() + 1.75
						&& block.getY() - 1 <= e.getLocation().getY() && e.getLocation().getY() <= block.getY() + 2.5)
					return e;
		}
		return null;
	}

	/**
	 * Get nearby entities
	 *
	 * @param loc
	 * @param range
	 * @return
	 */
	public static List<Entity> getNearbyEntities(final Location loc, final int range) {
		final List<Entity> found = new ArrayList<>();

		for (final Entity entity : loc.getWorld().getEntities())
			if (isInBorder(loc, entity.getLocation(), range))
				found.add(entity);
		return found;
	}

	public static Player getPlayerNear(final Player p, final int range) {
		final List<Entity> en = p.getNearbyEntities(range, range, range).stream().filter(ent -> ent instanceof Player)
				.collect(Collectors.toList());
		final BlockIterator i = new BlockIterator(p, range);
		Block block;
		while (i.hasNext()) {
			block = i.next();
			for (final Entity e : en)
				if (block.getX() - .75 <= e.getLocation().getX() && e.getLocation().getX() <= block.getX() + 1.75
						&& block.getZ() - .75 <= e.getLocation().getZ() && e.getLocation().getZ() <= block.getZ() + 1.75
						&& block.getY() - 1 <= e.getLocation().getY() && e.getLocation().getY() <= block.getY() + 2.5)
					return (Player) e;
		}
		return null;
	}

	/**
	 * Check if entity is in border
	 *
	 * @param center
	 * @param notCenter
	 * @param range
	 * @return
	 */
	private static boolean isInBorder(final Location center, final Location notCenter, final int range) {
		final int x = center.getBlockX(), z = center.getBlockZ();
		final int x1 = notCenter.getBlockX(), z1 = notCenter.getBlockZ();

		if (x1 >= x + range || z1 >= z + range || x1 <= x - range || z1 <= z - range)
			return false;
		return true;
	}

}
