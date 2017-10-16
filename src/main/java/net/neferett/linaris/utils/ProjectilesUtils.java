package net.neferett.linaris.utils;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftFireball;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.WitherSkull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_8_R3.EntityArrow;
import net.minecraft.server.v1_8_R3.EntityEgg;
import net.minecraft.server.v1_8_R3.EntityEnderPearl;
import net.minecraft.server.v1_8_R3.EntityLargeFireball;
import net.minecraft.server.v1_8_R3.EntityPotion;
import net.minecraft.server.v1_8_R3.EntitySmallFireball;
import net.minecraft.server.v1_8_R3.EntitySnowball;
import net.minecraft.server.v1_8_R3.EntityWitherSkull;
import net.neferett.linaris.metadatas.Flags;
import net.neferett.linaris.specialitems.SpecialItemProjectileLauncheur;

public class ProjectilesUtils {
	/**
	 * Lance un projectile à partir d'une source dans la direction choisit Le laucher id represente le special item
	 * implementant SpecialItemProjectileLauncheur utilisé par la source pour lancer le projectile
	 * @param launcherId
	 * @param source
	 * @param spawn
	 * @param projectile
	 * @return
	 */
	public static <T extends Projectile> T launchProjectile(int launcherId, LivingEntity source, Location spawn, Class<? extends T> projectile) {
		T launched = launchProjectile(source, spawn, projectile);
		Flags.setIntegerFlag(launched, SpecialItemProjectileLauncheur.flag, launcherId);
		return launched;
	}
	/**
	 * TODO : adapter les launch sans entité sources (fait uniquement pour les boules de neiges) Lance un projectile à
	 * partir d'une source dans direction choisit
	 * @param source
	 * @param spawn
	 * @param projectile
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Projectile> T launchProjectile(LivingEntity source, Location spawn, Class<? extends T> projectile) {
		net.minecraft.server.v1_8_R3.EntityLiving launcher = source == null ? null : ((CraftLivingEntity) source).getHandle();
		net.minecraft.server.v1_8_R3.World world = ((CraftWorld) spawn.getWorld()).getHandle();
		net.minecraft.server.v1_8_R3.Entity launch = null;

		if (Snowball.class.isAssignableFrom(projectile)) {
			launch = launcher == null ? new EntitySnowball(world) : new EntitySnowball(world, launcher);
		}
		else if (Egg.class.isAssignableFrom(projectile)) {
			launch = launcher == null ? new EntityEgg(world) : new EntityEgg(world, launcher);
		}
		else if (EnderPearl.class.isAssignableFrom(projectile)) {
			launch = launcher == null ? new EntityEnderPearl(world, launcher) : new EntityEnderPearl(world, launcher);
		}
		else if (Arrow.class.isAssignableFrom(projectile)) {
			launch = launcher == null ? new EntityArrow(world) : new EntityArrow(world, launcher, 1);
		}
		else if (ThrownPotion.class.isAssignableFrom(projectile)) {
			launch = launcher == null ? new EntityPotion(world) : new EntityPotion(world, launcher, CraftItemStack.asNMSCopy(new ItemStack(Material.POTION, 1)));
		}
		else if (Fireball.class.isAssignableFrom(projectile)) {
			Vector direction = spawn.getDirection().multiply(10);

			if (SmallFireball.class.isAssignableFrom(projectile)) {
				launch = launcher == null ? new EntitySmallFireball(world) : new EntitySmallFireball(world, launcher, direction.getX(), direction.getY(), direction.getZ());
			}
			else if (WitherSkull.class.isAssignableFrom(projectile)) {
				launch = launcher == null ? new EntityWitherSkull(world) : new EntityWitherSkull(world, launcher, direction.getX(), direction.getY(), direction.getZ());
			}
			else {
				launch = launcher == null ? new EntityLargeFireball(world) : new EntityLargeFireball(world, launcher, direction.getX(), direction.getY(), direction.getZ());
			}
			launch.setPositionRotation(spawn.getX(), spawn.getY(), spawn.getZ(), spawn.getYaw(), spawn.getPitch());
		}
		if (launcher == null) {
			// launch.setPosition(spawn.getX(), spawn.getY(), spawn.getZ());
			launch.setPositionRotation(spawn.getX(), spawn.getY(), spawn.getZ(), spawn.getYaw(), spawn.getPitch());
			launch.getBukkitEntity().setVelocity(spawn.getDirection());
		}
		Validate.notNull(launch, "Projectile not supported");

		world.addEntity(launch);
		return (T) launch.getBukkitEntity();
	}

	/**
	 * Spawn une entité de type fireball sans lanceur et non bugguée
	 * @param spawn
	 * @param fireball
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Fireball> T spawnFireBall(Location spawn, Class<? extends T> fireball) {
		Vector direction = spawn.getDirection().multiply(10);
		Fireball projectile = spawn.getWorld().spawn(spawn, fireball);
		net.minecraft.server.v1_8_R3.EntityFireball launch = ((CraftFireball) projectile).getHandle();
		launch.setPositionRotation(spawn.getX(), spawn.getY(), spawn.getZ(), spawn.getYaw(), spawn.getPitch());
		launch.setPosition(launch.locX, launch.locY, launch.locZ);
		// launch.height = 0.0F;
		launch.motX = (launch.motY = launch.motZ = 0.0D);
		launch.setDirection(direction.getX(), direction.getY(), direction.getZ());

		return (T) launch.getBukkitEntity();
	}

}
