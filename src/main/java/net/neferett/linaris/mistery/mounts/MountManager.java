 package net.neferett.linaris.mistery.mounts;

import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import net.minecraft.server.v1_8_R3.EntityAgeable;
import net.minecraft.server.v1_8_R3.EntityBlaze;
import net.minecraft.server.v1_8_R3.EntityCaveSpider;
import net.minecraft.server.v1_8_R3.EntityChicken;
import net.minecraft.server.v1_8_R3.EntityCow;
import net.minecraft.server.v1_8_R3.EntityHorse;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityIronGolem;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.EntityOcelot;
import net.minecraft.server.v1_8_R3.EntityPig;
import net.minecraft.server.v1_8_R3.EntityRabbit;
import net.minecraft.server.v1_8_R3.EntitySheep;
import net.minecraft.server.v1_8_R3.EntitySlime;
import net.minecraft.server.v1_8_R3.EntitySpider;
import net.minecraft.server.v1_8_R3.EntityWither;
import net.minecraft.server.v1_8_R3.EntityWolf;
import net.minecraft.server.v1_8_R3.World;
import net.neferett.linaris.metadatas.Flags;
import net.neferett.linaris.mistery.NMSUtils;
import net.neferett.linaris.mistery.mounts.mounts.CaveSpider.CaveSpiderMountEntity;
import net.neferett.linaris.mistery.mounts.mounts.Chicken.ChickenMountEntity;
import net.neferett.linaris.mistery.mounts.mounts.Rabbit.RabbitMountEntity;
import net.neferett.linaris.mistery.mounts.mounts.Slime.SlimeMountEntity;
import net.neferett.linaris.mistery.mounts.mounts.Wolf.WolfMountEntity;
import net.neferett.linaris.mistery.mounts.mounts.blaze.BlazeMountEntity;
import net.neferett.linaris.mistery.mounts.mounts.cow.CowMountEntity;
import net.neferett.linaris.mistery.mounts.mounts.horse.HorseMountEntity;
import net.neferett.linaris.mistery.mounts.mounts.irongolem.IronGolemMountEntity;
import net.neferett.linaris.mistery.mounts.mounts.ocelot.OcelotMountEntity;
import net.neferett.linaris.mistery.mounts.mounts.pig.PigMountEntity;
import net.neferett.linaris.mistery.mounts.mounts.sheep.SuperSheepEntity;
import net.neferett.linaris.mistery.mounts.mounts.skeletonhorse.SkeletonHorseMountEntity;
import net.neferett.linaris.mistery.mounts.mounts.spider.SpiderMountEntity;
import net.neferett.linaris.mistery.mounts.mounts.wither.WitherMountEntity;
import net.neferett.linaris.mistery.mounts.mounts.zombiehorse.ZombieHorseMountEntity;

public class MountManager {
	
	private static double		maxHealth	= 2.0D;

	public static void init() {
		NMSUtils.registerEntity("Sheep", 91,  EntitySheep.class, SuperSheepEntity.class); 
		NMSUtils.registerEntity("Pig", 90,  EntityPig.class, PigMountEntity.class); 
		NMSUtils.registerEntity("Ocelot", 98,  EntityOcelot.class, OcelotMountEntity.class); 
		NMSUtils.registerEntity("Blaze", 61,  EntityBlaze.class, BlazeMountEntity.class); 
		NMSUtils.registerEntity("Chicken", 93,  EntityChicken.class, ChickenMountEntity.class); 
		NMSUtils.registerEntity("Wolf", 95,  EntityWolf.class, WolfMountEntity.class); 
		NMSUtils.registerEntity("Slime", 55,  EntitySlime.class, SlimeMountEntity.class); 
		NMSUtils.registerEntity("Wither", 64,  EntityWither.class, WitherMountEntity.class); 
		NMSUtils.registerEntity("Cow", 92,  EntityCow.class, CowMountEntity.class); 
		NMSUtils.registerEntity("Iron Golem", 99,  EntityIronGolem.class, IronGolemMountEntity.class); 
		NMSUtils.registerEntity("Rabbit", 101,  EntityRabbit.class, RabbitMountEntity.class); 
        NMSUtils.registerEntity("Horse", 100, EntityHorse.class, HorseMountEntity.class);
        NMSUtils.registerEntity("Horse", 100, EntityHorse.class, ZombieHorseMountEntity.class);
        NMSUtils.registerEntity("Horse", 100, EntityHorse.class, SkeletonHorseMountEntity.class);
		NMSUtils.registerEntity("CaveSpider", 59,  EntityCaveSpider.class, CaveSpiderMountEntity.class); 
		NMSUtils.registerEntity("Spider", 52,  EntitySpider.class, SpiderMountEntity.class); 
		


	}
	
	public static void make(EntityLiving nmsEntity, Player player) {

		LivingEntity mount = (LivingEntity) nmsEntity.getBukkitEntity();

		Flags.setIntegerFlag(player, MountFlags.mount, nmsEntity.getId());
		Flags.setStringFlag(mount, MountFlags.mount, player.getName());

		if (mount instanceof EntityAgeable) ((EntityAgeable) mount).setAge(0);
		org.bukkit.Location loc = player.getLocation();
		World nmsWorld = ((CraftWorld) loc.getWorld()).getHandle();
		nmsEntity.setPosition(loc.getX(), loc.getY() + 0.3, loc.getZ());
		nmsWorld.addEntity(nmsEntity, SpawnReason.CUSTOM);

		mount.setMaxHealth(maxHealth);

		mount.setPassenger(player);
		player.closeInventory();
	}

	public static boolean shouldDie(EntityLiving mount, Player rider) {
		if (mount.passenger == null || !(mount.passenger instanceof EntityHuman)) {
			mount.die();
			return true;
		}
		return false;
	}
	
	
}
