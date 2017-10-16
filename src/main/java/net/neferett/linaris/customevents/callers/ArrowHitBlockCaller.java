package net.neferett.linaris.customevents.callers;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import net.minecraft.server.v1_8_R3.EntityArrow;
import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.customevents.events.ArrowHitBlockEvent;

public class ArrowHitBlockCaller implements Listener {

	public static void register() {
		BukkitAPI.get().getServer().getPluginManager().registerEvents(new ArrowHitBlockCaller(), BukkitAPI.get());
	}

	@EventHandler
	private void onProjectileHit(final ProjectileHitEvent e) {
		if (e.getEntityType() == EntityType.ARROW) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(BukkitAPI.get(), new Runnable() {
				public void run() {
					try {

						EntityArrow entityArrow = ((CraftArrow) e.getEntity()).getHandle();

						Field fieldX = EntityArrow.class.getDeclaredField("d");
						Field fieldY = EntityArrow.class.getDeclaredField("e");
						Field fieldZ = EntityArrow.class.getDeclaredField("f");

						fieldX.setAccessible(true);
						fieldY.setAccessible(true);
						fieldZ.setAccessible(true);

						int x = fieldX.getInt(entityArrow);
						int y = fieldY.getInt(entityArrow);
						int z = fieldZ.getInt(entityArrow);

						if (isValidBlock(y)) {
							Block block = e.getEntity().getWorld().getBlockAt(x, y, z);
							Bukkit.getServer().getPluginManager().callEvent(new ArrowHitBlockEvent((Arrow) e.getEntity(), block));
						}

					} catch (NoSuchFieldException e1) {
						e1.printStackTrace();
					} catch (SecurityException e1) {
						e1.printStackTrace();
					} catch (IllegalArgumentException e1) {
						e1.printStackTrace();
					} catch (IllegalAccessException e1) {
						e1.printStackTrace();
					}
				}
			});

		}
	}

	// If the arrow hits a mob or player the y coord will be -1
	private boolean isValidBlock(int y) {
		return y != -1;
	}
}
