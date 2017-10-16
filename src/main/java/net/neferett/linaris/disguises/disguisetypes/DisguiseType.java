package net.neferett.linaris.disguises.disguisetypes;

import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;

public enum DisguiseType {

	ARMOR_STAND(78), ARROW(60), BAT, BLAZE, BOAT(1), CAVE_SPIDER, CHICKEN, COW, CREEPER, DONKEY, DROPPED_ITEM(2,
			1), EGG(62), ELDER_GUARDIAN, ENDER_CRYSTAL(51), ENDER_DRAGON, ENDER_PEARL(65), ENDER_SIGNAL(
					72), ENDERMAN, ENDERMITE, EXPERIENCE_ORB, FALLING_BLOCK(70, 1), FIREBALL(63,
							0), FIREWORK(76), FISHING_HOOK(90), GHAST, GIANT, GUARDIAN, HORSE, IRON_GOLEM, ITEM_FRAME(
									71), LEASH_HITCH(77), MAGMA_CUBE, MINECART(10, 0), MINECART_CHEST(10,
											1), MINECART_COMMAND(10, 6), MINECART_FURNACE(10, 2), MINECART_HOPPER(10,
													5), MINECART_MOB_SPAWNER(10, 4), MINECART_TNT(10,
															3), MULE, MUSHROOM_COW, OCELOT, PAINTING, PIG, PIG_ZOMBIE, PLAYER, PRIMED_TNT(
																	50), RABBIT, SHEEP, SILVERFISH, SKELETON, SKELETON_HORSE, SLIME, SMALL_FIREBALL(
																			64, 0), SNOWBALL(
																					61), SNOWMAN, SPIDER, SPLASH_POTION(
																							73), SQUID, THROWN_EXP_BOTTLE(
																									75), UNDEAD_HORSE, UNKNOWN, VILLAGER, WITCH, WITHER, WITHER_SKELETON, WITHER_SKULL(
																											66), WOLF, ZOMBIE, ZOMBIE_VILLAGER;

	private static Method isVillager, getVariant, getSkeletonType, isElder;

	static {
		// We set the entity type in this so that we can safely ignore
		// disguisetypes which don't exist in older versions of MC.
		// Without erroring up everything.
		for (final DisguiseType type : values())
			try {
				DisguiseType toUse = type;
				switch (type) {
				// Disguise item frame isn't supported. So we don't give it a
				// entity type which should prevent it from being..
				// Usable.
				case ITEM_FRAME:
					break;
				case DONKEY:
				case MULE:
				case UNDEAD_HORSE:
				case SKELETON_HORSE:
					toUse = DisguiseType.HORSE;
					break;
				case ZOMBIE_VILLAGER:
					toUse = DisguiseType.ZOMBIE;
					break;
				case WITHER_SKELETON:
					toUse = DisguiseType.SKELETON;
					break;
				case ELDER_GUARDIAN:
					toUse = DisguiseType.GUARDIAN;
					break;
				default:
					break;
				}
				type.setEntityType(EntityType.valueOf(toUse.name()));
			} catch (final Throwable ex) {
				// This version of craftbukkit doesn't have the disguise.
			}
		try {
			isVillager = Zombie.class.getMethod("isVillager");
		} catch (final Throwable ignored) {
		}
		try {
			getVariant = Horse.class.getMethod("getVariant");
		} catch (final Throwable ignored) {
			// Pre-1.6, but that isn't even supported
		}
		try {
			getSkeletonType = Skeleton.class.getMethod("getSkeletonType");
		} catch (final Throwable ignored) {
		}
		try {
			isElder = Guardian.class.getMethod("isElder");
		} catch (final Throwable ignored) {
		}
	}

	@SuppressWarnings("rawtypes")
	public static DisguiseType getType(Entity entity) {
		DisguiseType disguiseType = getType(entity.getType());
		switch (disguiseType) {
		case ZOMBIE:
			try {
				if ((Boolean) isVillager.invoke(entity))
					disguiseType = DisguiseType.ZOMBIE_VILLAGER;
			} catch (final Exception ex) {
				ex.printStackTrace(System.out);
			}
			break;
		case HORSE:
			try {
				final Object variant = getVariant.invoke(entity);
				disguiseType = DisguiseType.valueOf(((Enum) variant).name());
			} catch (final Exception ex) {
				ex.printStackTrace(System.out);
			}
			break;
		case SKELETON:
			try {
				final Object type = getSkeletonType.invoke(entity);
				if (type == Skeleton.SkeletonType.WITHER)
					disguiseType = DisguiseType.WITHER_SKELETON;
			} catch (final Exception ex) {
				ex.printStackTrace(System.out);
			}
			break;
		case GUARDIAN:
			try {
				if ((Boolean) isElder.invoke(entity))
					disguiseType = DisguiseType.ELDER_GUARDIAN;
			} catch (final Exception ex) {
				ex.printStackTrace(System.out);
			}
			break;
		default:
			break;
		}
		return disguiseType;
	}

	public static DisguiseType getType(EntityType entityType) {
		try {
			return valueOf(entityType.name().toUpperCase());
		} catch (final Throwable ex) {
			return DisguiseType.UNKNOWN;
		}
	}

	private int defaultId, entityId;
	private EntityType entityType;
	private Class<? extends FlagWatcher> watcherClass;

	DisguiseType(int... ints) {
		for (int i = 0; i < ints.length; i++) {
			final int value = ints[i];
			switch (i) {
			case 0:
				this.entityId = value;
				break;
			case 1:
				this.defaultId = value;
				break;
			default:
				break;
			}
		}
	}

	public int getDefaultId() {
		return this.defaultId;
	}

	public Class<? extends Entity> getEntityClass() {
		if (this.entityType != null)
			return this.getEntityType().getEntityClass();
		return Entity.class;
	}

	public int getEntityId() {
		return this.entityId;
	}

	public EntityType getEntityType() {
		return this.entityType;
	}

	@SuppressWarnings("deprecation")
	public int getTypeId() {
		return this.getEntityType().getTypeId();
	}

	@SuppressWarnings("rawtypes")
	public Class getWatcherClass() {
		return this.watcherClass;
	}

	public boolean isMisc() {
		return this.getEntityType() != null && !this.getEntityType().isAlive();
	}

	public boolean isMob() {
		return this.getEntityType() != null && this.getEntityType().isAlive() && !this.isPlayer();
	}

	public boolean isPlayer() {
		return this == DisguiseType.PLAYER;
	}

	public boolean isUnknown() {
		return this == DisguiseType.UNKNOWN;
	}

	private void setEntityType(EntityType entityType) {
		this.entityType = entityType;
	}

	public void setWatcherClass(Class<? extends FlagWatcher> c) {
		this.watcherClass = c;
	}

	public String toReadable() {
		final String[] split = this.name().split("_");
		for (int i = 0; i < split.length; i++)
			split[i] = split[i].substring(0, 1) + split[i].substring(1).toLowerCase();
		return StringUtils.join(split, " ");
	}
}
