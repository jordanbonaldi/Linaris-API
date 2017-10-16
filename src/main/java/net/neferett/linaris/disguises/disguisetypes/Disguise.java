package net.neferett.linaris.disguises.disguisetypes;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;

import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.disguises.DisguiseAPI;
import net.neferett.linaris.disguises.DisguiseConfig;
import net.neferett.linaris.disguises.disguisetypes.TargetedDisguise.TargetType;
import net.neferett.linaris.disguises.disguisetypes.watchers.AgeableWatcher;
import net.neferett.linaris.disguises.disguisetypes.watchers.BatWatcher;
import net.neferett.linaris.disguises.disguisetypes.watchers.ZombieWatcher;
import net.neferett.linaris.disguises.events.DisguiseEvent;
import net.neferett.linaris.disguises.events.UndisguiseEvent;
import net.neferett.linaris.disguises.utilities.DisguiseUtilities;
import net.neferett.linaris.disguises.utilities.DisguiseValues;
import net.neferett.linaris.disguises.utilities.PacketsManager;
import net.neferett.linaris.disguises.utilities.ReflectionManager;

public abstract class Disguise {

	private static List<UUID> viewSelf = new ArrayList<>();

	/**
	 * Returns the list of people who have /disguiseViewSelf toggled on
	 *
	 * @return
	 */
	public static List<UUID> getViewSelf() {
		return viewSelf;
	}

	private boolean disguiseInUse;
	private DisguiseType disguiseType;
	private Entity entity;
	private boolean hearSelfDisguise = DisguiseConfig.isSelfDisguisesSoundsReplaced();
	private boolean hideArmorFromSelf = DisguiseConfig.isHidingArmorFromSelf();
	private boolean hideHeldItemFromSelf = DisguiseConfig.isHidingHeldItemFromSelf();
	private boolean keepDisguiseEntityDespawn = DisguiseConfig.isKeepDisguiseOnEntityDespawn();
	private boolean keepDisguisePlayerDeath = DisguiseConfig.isKeepDisguiseOnPlayerDeath();
	private boolean keepDisguisePlayerLogout = DisguiseConfig.isKeepDisguiseOnPlayerLogout();
	private boolean modifyBoundingBox = DisguiseConfig.isModifyBoundingBox();
	private boolean replaceSounds = DisguiseConfig.isSoundEnabled();
	private boolean showName = false;
	private BukkitTask task = null;
	private Runnable velocityRunnable;
	private boolean velocitySent = DisguiseConfig.isVelocitySent();

	private boolean viewSelfDisguise = DisguiseConfig.isViewDisguises();

	private FlagWatcher watcher;

	@Override
	public abstract Disguise clone();

	/**
	 * Seems I do this method so I can make cleaner constructors on disguises..
	 *
	 * @param newType
	 */
	@SuppressWarnings("unchecked")
	protected void createDisguise(DisguiseType newType) {
		if (this.getWatcher() != null)
			return;
		if (newType.getEntityType() == null)
			throw new RuntimeException("DisguiseType " + newType
					+ " was used in a futile attempt to construct a disguise, but this version of craftbukkit does not have that entity");
		// Set the disguise type
		this.disguiseType = newType;
		// Get if they are a adult now..
		boolean isAdult = true;
		if (this.isMobDisguise())
			isAdult = ((MobDisguise) this).isAdult();
		try {
			// Construct the FlagWatcher from the stored class
			this.setWatcher(
					(FlagWatcher) this.getType().getWatcherClass().getConstructor(Disguise.class).newInstance(this));
		} catch (final Exception e) {
			e.printStackTrace(System.out);
		}
		// Set the disguise if its a baby or not
		if (!isAdult)
			if (this.getWatcher() instanceof AgeableWatcher)
				((AgeableWatcher) this.getWatcher()).setBaby(true);
			else if (this.getWatcher() instanceof ZombieWatcher)
				((ZombieWatcher) this.getWatcher()).setBaby(true);
		// If the disguise type is a wither, set the flagwatcher value for the
		// skeleton to a wither skeleton
		if (this.getType() == DisguiseType.WITHER_SKELETON)
			this.getWatcher().setValue(13, (byte) 1);
		else if (this.getType() == DisguiseType.ZOMBIE_VILLAGER)
			this.getWatcher().setValue(13, (byte) 1);
		else if (this.getType() == DisguiseType.ELDER_GUARDIAN)
			this.getWatcher().setValue(16, 0 | 4);
		final boolean alwaysSendVelocity;
		switch (this.getType()) {
		case EGG:
		case ENDER_PEARL:
		case BAT:
		case EXPERIENCE_ORB:
		case FIREBALL:
		case SMALL_FIREBALL:
		case SNOWBALL:
		case SPLASH_POTION:
		case THROWN_EXP_BOTTLE:
		case WITHER_SKULL:
		case FIREWORK:
			alwaysSendVelocity = true;
			break;
		default:
			alwaysSendVelocity = false;
			break;
		}
		double velocitySpeed = 0.0005;
		switch (this.getType()) {
		case FIREWORK:
			velocitySpeed = -0.040;
			break;
		case WITHER_SKULL:
			velocitySpeed = 0.000001D;
			break;
		case ARROW:
		case BOAT:
		case ENDER_CRYSTAL:
		case ENDER_DRAGON:
		case GHAST:
		case ITEM_FRAME:
		case MINECART:
		case MINECART_CHEST:
		case MINECART_COMMAND:
		case MINECART_FURNACE:
		case MINECART_HOPPER:
		case MINECART_MOB_SPAWNER:
		case MINECART_TNT:
		case PAINTING:
		case PLAYER:
		case SQUID:
			velocitySpeed = 0;
			break;
		case DROPPED_ITEM:
		case PRIMED_TNT:
		case WITHER:
		case FALLING_BLOCK:
			velocitySpeed = 0.04;
			break;
		case EXPERIENCE_ORB:
			velocitySpeed = 0.0221;
			break;
		case SPIDER:
		case BAT:
		case CAVE_SPIDER:
			velocitySpeed = 0.004;
			break;
		default:
			break;
		}
		final double vectorY = velocitySpeed;
		final TargetedDisguise disguise = (TargetedDisguise) this;
		// A scheduler to clean up any unused disguises.
		this.velocityRunnable = new Runnable() {
			private int blockX, blockY, blockZ, facing;
			private int deadTicks = 0;
			private int refreshDisguise = 0;

			@Override
			public void run() {
				// If entity is no longer valid. Remove it.
				if (!Disguise.this.getEntity().isValid()) {
					// If it has been dead for 30+ ticks
					// This is to ensure that this disguise isn't removed while
					// clients think its the real entity
					// The delay is because if it sends the destroy entity
					// packets straight away, then it means no death animation
					// This is probably still a problem for wither and
					// enderdragon deaths.
					if (this.deadTicks++ > (Disguise.this.getType() == DisguiseType.ENDER_DRAGON ? 200 : 20)) {
						this.deadTicks = 0;
						if (Disguise.this.isRemoveDisguiseOnDeath())
							Disguise.this.removeDisguise();
						else {
							Disguise.this.entity = null;
							Disguise.this.watcher = Disguise.this.getWatcher().clone(disguise);
							Disguise.this.task.cancel();
							Disguise.this.task = null;
						}
					}
				} else {
					this.deadTicks = 0;
					// If the disguise type is tnt, we need to resend the entity
					// packet else it will turn invisible
					if (Disguise.this.getType() == DisguiseType.PRIMED_TNT
							|| Disguise.this.getType() == DisguiseType.FIREWORK) {
						this.refreshDisguise++;
						if (this.refreshDisguise % 40 == 0) {
							this.refreshDisguise = 0;
							DisguiseUtilities.refreshTrackers(disguise);
						}
					}
					if (Disguise.this.getType() == DisguiseType.ITEM_FRAME) {
						final Location loc = Disguise.this.getEntity().getLocation();
						final int newFacing = (((int) loc.getYaw() + 720 + 45) / 90) % 4;
						if (loc.getBlockX() != this.blockX || loc.getBlockY() != this.blockY
								|| loc.getBlockZ() != this.blockZ || newFacing != this.facing) {
							this.blockX = loc.getBlockX();
							this.blockY = loc.getBlockY();
							this.blockZ = loc.getBlockZ();
							this.facing = newFacing;
							DisguiseUtilities.refreshTrackers(disguise);
						}
					}
					if (Disguise.this.isModifyBoundingBox())
						DisguiseUtilities.doBoundingBox(disguise);
					if (Disguise.this.getType() == DisguiseType.BAT
							&& !((BatWatcher) Disguise.this.getWatcher()).isFlying())
						return;
					// If the vectorY isn't 0. Cos if it is. Then it doesn't
					// want to send any vectors.
					// If this disguise has velocity sending enabled and the
					// entity is flying.
					if (Disguise.this.isVelocitySent() && vectorY != 0
							&& (alwaysSendVelocity || !Disguise.this.getEntity().isOnGround())) {
						final Vector vector = Disguise.this.getEntity().getVelocity();
						// If the entity doesn't have velocity changes already -
						// You know. I really can't wrap my head about the
						// if statement.
						// But it doesn't seem to do anything wrong..
						if (vector.getY() != 0
								&& !(vector.getY() < 0 && alwaysSendVelocity && Disguise.this.getEntity().isOnGround()))
							return;
						// If disguise isn't a experience orb, or the entity
						// isn't standing on the ground
						if (Disguise.this.getType() != DisguiseType.EXPERIENCE_ORB
								|| !Disguise.this.getEntity().isOnGround()) {
							PacketContainer lookPacket = null;
							if (Disguise.this.getType() == DisguiseType.WITHER_SKULL
									&& DisguiseConfig.isWitherSkullPacketsEnabled()) {
								lookPacket = new PacketContainer(PacketType.Play.Server.ENTITY_LOOK);
								final StructureModifier<Object> mods = lookPacket.getModifier();
								lookPacket.getIntegers().write(0, Disguise.this.getEntity().getEntityId());
								final Location loc = Disguise.this.getEntity().getLocation();
								mods.write(4,
										PacketsManager.getYaw(Disguise.this.getType(),
												Disguise.this.getEntity().getType(),
												(byte) Math.floor(loc.getYaw() * 256.0F / 360.0F)));
								mods.write(5,
										PacketsManager.getPitch(Disguise.this.getType(),
												DisguiseType.getType(Disguise.this.getEntity().getType()),
												(byte) Math.floor(loc.getPitch() * 256.0F / 360.0F)));
								if (Disguise.this.isSelfDisguiseVisible()
										&& Disguise.this.getEntity() instanceof Player) {
									final PacketContainer selfLookPacket = lookPacket.shallowClone();
									selfLookPacket.getIntegers().write(0, DisguiseAPI.getSelfDisguiseId());
									try {
										ProtocolLibrary.getProtocolManager().sendServerPacket(
												(Player) Disguise.this.getEntity(), selfLookPacket, false);
									} catch (final InvocationTargetException e) {
										e.printStackTrace(System.out);
									}
								}
							}
							try {
								final PacketContainer velocityPacket = new PacketContainer(
										PacketType.Play.Server.ENTITY_VELOCITY);
								final StructureModifier<Integer> mods = velocityPacket.getIntegers();
								mods.write(1, (int) (vector.getX() * 8000));
								mods.write(3, (int) (vector.getZ() * 8000));
								for (final Player player : DisguiseUtilities.getPerverts(disguise)) {
									if (Disguise.this.getEntity() == player) {
										if (!Disguise.this.isSelfDisguiseVisible())
											continue;
										mods.write(0, DisguiseAPI.getSelfDisguiseId());
									} else
										mods.write(0, Disguise.this.getEntity().getEntityId());
									mods.write(2,
											(int) (8000D * (vectorY * ReflectionManager.getPing(player)) * 0.069D));
									if (lookPacket != null && player != Disguise.this.getEntity())
										ProtocolLibrary.getProtocolManager().sendServerPacket(player, lookPacket,
												false);
									ProtocolLibrary.getProtocolManager().sendServerPacket(player,
											velocityPacket.shallowClone(), false);
								}
							} catch (final Exception e) {
								e.printStackTrace(System.out);
							}
						}
						// If we need to send a packet to update the exp
						// position as it likes to gravitate client sided to
						// players.
					}
					if (Disguise.this.getType() == DisguiseType.EXPERIENCE_ORB) {
						final PacketContainer packet = new PacketContainer(PacketType.Play.Server.REL_ENTITY_MOVE);
						packet.getIntegers().write(0, Disguise.this.getEntity().getEntityId());
						try {
							for (final Player player : DisguiseUtilities.getPerverts(disguise))
								if (Disguise.this.getEntity() != player)
									ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet, false);
								else if (Disguise.this.isSelfDisguiseVisible()) {
									final PacketContainer selfPacket = packet.shallowClone();
									selfPacket.getModifier().write(0, DisguiseAPI.getSelfDisguiseId());
									try {
										ProtocolLibrary.getProtocolManager().sendServerPacket(
												(Player) Disguise.this.getEntity(), selfPacket, false);
									} catch (final InvocationTargetException e) {
										e.printStackTrace(System.out);
									}
								}
						} catch (final InvocationTargetException e) {
							e.printStackTrace(System.out);
						}
					}
				}
			}
		};
	}

	/**
	 * Get the disguised entity
	 *
	 * @return
	 */
	public Entity getEntity() {
		return this.entity;
	}

	/**
	 * Get the disguise type
	 *
	 * @return
	 */
	public DisguiseType getType() {
		return this.disguiseType;
	}

	/**
	 * Get the flag watcher
	 *
	 * @return
	 */
	public FlagWatcher getWatcher() {
		return this.watcher;
	}

	/**
	 * In use doesn't mean that this disguise is active. It means that Lib's
	 * Disguises still stores a reference to the disguise. getEntity() can still
	 * return null if this disguise is active after despawn, logout, etc.
	 *
	 * @return
	 */
	public boolean isDisguiseInUse() {
		return this.disguiseInUse;
	}

	public boolean isHidingArmorFromSelf() {
		return this.hideArmorFromSelf;
	}

	public boolean isHidingHeldItemFromSelf() {
		return this.hideHeldItemFromSelf;
	}

	public boolean isKeepDisguiseOnEntityDespawn() {
		return this.keepDisguiseEntityDespawn;
	}

	public boolean isKeepDisguiseOnPlayerDeath() {
		return this.keepDisguisePlayerDeath;
	}

	public boolean isKeepDisguiseOnPlayerLogout() {
		return this.keepDisguisePlayerLogout;
	}

	public boolean isMiscDisguise() {
		return false;
	}

	public boolean isMobDisguise() {
		return false;
	}

	public boolean isModifyBoundingBox() {
		return this.modifyBoundingBox;
	}

	public boolean isPlayerDisguise() {
		return false;
	}

	/**
	 * Internal use
	 *
	 * @return
	 */
	public boolean isRemoveDisguiseOnDeath() {
		if (this.getEntity() == null)
			return true;
		return this.getEntity() instanceof Player
				? (!((Player) this.getEntity()).isOnline() ? !this.isKeepDisguiseOnPlayerLogout()
						: !this.isKeepDisguiseOnPlayerDeath())
				: (!this.isKeepDisguiseOnEntityDespawn() || this.getEntity().isDead());
	}

	public boolean isSelfDisguiseSoundsReplaced() {
		return this.hearSelfDisguise;
	}

	/**
	 * Can the disguised view himself as the disguise
	 *
	 * @return
	 */
	public boolean isSelfDisguiseVisible() {
		return this.viewSelfDisguise;
	}

	/**
	 * Returns true if the entity's name is showing through the disguise
	 *
	 * @return
	 */
	public boolean isShowName() {
		return this.showName;
	}

	public boolean isSoundsReplaced() {
		return this.replaceSounds;
	}

	public boolean isVelocitySent() {
		return this.velocitySent;
	}

	/**
	 * Removes the disguise and undisguises the entity if its using this
	 * disguise.
	 *
	 * @return
	 */
	public boolean removeDisguise() {
		if (this.disguiseInUse) {
			final UndisguiseEvent event = new UndisguiseEvent(this.entity, this);
			Bukkit.getPluginManager().callEvent(event);
			if (!event.isCancelled()) {
				this.disguiseInUse = false;
				if (this.task != null) {
					this.task.cancel();
					this.task = null;
				}
				final HashMap<UUID, HashSet<TargetedDisguise>> disguises = DisguiseUtilities.getDisguises();
				// If this disguise has a entity set
				if (this.getEntity() != null) {
					// If this disguise is active
					// Remove the disguise from the current disguises.
					if (DisguiseUtilities.removeDisguise((TargetedDisguise) this)) {
						if (this.getEntity() instanceof Player)
							DisguiseUtilities.removeSelfDisguise((Player) this.getEntity());

						// Better refresh the entity to undisguise it
						if (this.getEntity().isValid())
							DisguiseUtilities.refreshTrackers((TargetedDisguise) this);
						else
							DisguiseUtilities.destroyEntity((TargetedDisguise) this);
					}
				} else {
					// Loop through the disguises because it could be used with
					// a unknown entity id.
					final HashMap<Integer, HashSet<TargetedDisguise>> future = DisguiseUtilities.getFutureDisguises();
					final Iterator<Integer> itel = DisguiseUtilities.getFutureDisguises().keySet().iterator();
					while (itel.hasNext()) {
						final int id = itel.next();
						if (future.get(id).remove(this) && future.get(id).isEmpty())
							itel.remove();
					}
				}

				if (this.isPlayerDisguise()) {
					final String name = ((PlayerDisguise) this).getName();
					if (!DisguiseUtilities.getAddedByPlugins().contains(name.toLowerCase())) {
						for (final HashSet<TargetedDisguise> disguise : disguises.values())
							for (final Disguise d : disguise)
								if (d.isPlayerDisguise() && ((PlayerDisguise) d).getName().equals(name))
									return true;
						DisguiseUtilities.getGameProfiles().remove(name.toLowerCase());
					}
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Set the entity of the disguise. Only used for internal things.
	 *
	 * @param entity
	 * @return
	 */
	public Disguise setEntity(Entity entity) {
		if (this.getEntity() != null) {
			if (this.getEntity() == entity)
				return this;
			throw new RuntimeException("This disguise is already in use! Try .clone()");
		}
		if (this.isMiscDisguise() && !DisguiseConfig.isMiscDisguisesForLivingEnabled()
				&& entity instanceof LivingEntity)
			throw new RuntimeException(
					"Cannot disguise a living entity with a misc disguise. Renable MiscDisguisesForLiving in the config to do this");
		this.entity = entity;
		this.setupWatcher();
		return this;
	}

	public Disguise setHearSelfDisguise(boolean hearSelfDisguise) {
		this.hearSelfDisguise = hearSelfDisguise;
		return this;
	}

	public Disguise setHideArmorFromSelf(boolean hideArmor) {
		this.hideArmorFromSelf = hideArmor;
		if (this.getEntity() instanceof Player)
			((Player) this.getEntity()).updateInventory();
		return this;
	}

	public Disguise setHideHeldItemFromSelf(boolean hideHeldItem) {
		this.hideHeldItemFromSelf = hideHeldItem;
		if (this.getEntity() instanceof Player)
			((Player) this.getEntity()).updateInventory();
		return this;
	}

	public Disguise setKeepDisguiseOnEntityDespawn(boolean keepDisguise) {
		this.keepDisguiseEntityDespawn = keepDisguise;
		return this;
	}

	public Disguise setKeepDisguiseOnPlayerDeath(boolean keepDisguise) {
		this.keepDisguisePlayerDeath = keepDisguise;
		return this;
	}

	public Disguise setKeepDisguiseOnPlayerLogout(boolean keepDisguise) {
		this.keepDisguisePlayerLogout = keepDisguise;
		return this;
	}

	public Disguise setModifyBoundingBox(boolean modifyBox) {
		if (((TargetedDisguise) this).getDisguiseTarget() != TargetType.SHOW_TO_EVERYONE_BUT_THESE_PLAYERS)
			throw new RuntimeException(
					"Cannot modify the bounding box of a disguise which is not TargetType.SHOW_TO_EVERYONE_BUT_THESE_PLAYERS");
		if (this.isModifyBoundingBox() != modifyBox) {
			this.modifyBoundingBox = modifyBox;
			if (DisguiseUtilities.isDisguiseInUse(this))
				DisguiseUtilities.doBoundingBox((TargetedDisguise) this);
		}
		return this;
	}

	public Disguise setReplaceSounds(boolean areSoundsReplaced) {
		this.replaceSounds = areSoundsReplaced;
		return this;
	}

	public Disguise setShowName(boolean showName) {
		this.showName = showName;
		return this;
	}

	/**
	 * Sets up the FlagWatcher with the entityclass, it creates all the data it
	 * needs to prevent conflicts when sending the datawatcher.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setupWatcher() {
		final HashMap<Integer, Object> disguiseValues = DisguiseValues.getMetaValues(this.getType());
		final HashMap<Integer, Object> entityValues = DisguiseValues
				.getMetaValues(DisguiseType.getType(this.getEntity().getType()));
		// Start from 2 as they ALL share 0 and 1
		for (int dataNo = 0; dataNo <= 31; dataNo++) {
			// STEP 1. Find out if the watcher has set data on it.
			// If the watcher already set a metadata on this
			if (this.getWatcher().hasValue(dataNo))
				// Better check that the value is stable.
				// To check this, I'm going to check if there exists a default
				// value
				// Then I'm going to check if the watcher value is the same as
				// the default value.
				if (disguiseValues.containsKey(dataNo))
					// Now check if they are the same class, or both null.
					if (disguiseValues.get(dataNo) == null || this.getWatcher().getValue(dataNo, null) == null) {
					if (disguiseValues.get(dataNo) == null && this.getWatcher().getValue(dataNo, null) == null)
						// They are both null. Idk what this means really.
						continue;
					} else if (this.getWatcher().getValue(dataNo, null).getClass() == disguiseValues.get(dataNo).getClass())
						// The classes are the same. The client "shouldn't"
						// crash.
						continue;
			// STEP 2. As the watcher has not set data on it, check if I need to
			// set the default data.
			// If neither of them touch it
			if (!entityValues.containsKey(dataNo) && !disguiseValues.containsKey(dataNo))
				continue;
			// If the disguise has this, but not the entity. Then better set it!
			if (!entityValues.containsKey(dataNo) && disguiseValues.containsKey(dataNo)) {
				this.getWatcher().setBackupValue(dataNo, disguiseValues.get(dataNo));
				continue;
			}
			// Else if the disguise doesn't have it. But the entity does. Better
			// remove it!
			if (entityValues.containsKey(dataNo) && !disguiseValues.containsKey(dataNo)) {
				this.getWatcher().setBackupValue(dataNo, null);
				continue;
			}
			final Object eObj = entityValues.get(dataNo);
			final Object dObj = disguiseValues.get(dataNo);
			if (eObj == null || dObj == null)
				if (eObj == null && dObj == null)
					continue;
				else {
					this.getWatcher().setBackupValue(dataNo, dObj);
					continue;
				}
			if (eObj.getClass() != dObj.getClass()) {
				this.getWatcher().setBackupValue(dataNo, dObj);
				continue;
			}

			// Since they both share it. With the same classes. Time to check if
			// its from something they extend.
			// Better make this clear before I compare the values because some
			// default values are different!
			// Entity is 0 & 1 - But we aint gonna be checking that
			// EntityAgeable is 16
			// EntityInsentient is 10 & 11
			// EntityZombie is 12 & 13 & 14 - But it overrides other values and
			// another check already does this.
			// EntityLiving is 6 & 7 & 8 & 9
			// Lets use switch
			Class baseClass = null;
			switch (dataNo) {
			case 6:
			case 7:
			case 8:
			case 9:
				baseClass = ReflectionManager.getNmsClass("EntityLiving");
				break;
			case 10:
			case 11:
				baseClass = ReflectionManager.getNmsClass("EntityInsentient");
				break;
			case 16:
				baseClass = ReflectionManager.getNmsClass("EntityAgeable");
				break;
			default:
				break;
			}
			final Class nmsEntityClass = ReflectionManager.getNmsEntity(this.getEntity()).getClass();
			final Class nmsDisguiseClass = DisguiseValues.getNmsEntityClass(this.getType());
			if (nmsDisguiseClass != null) {
				// If they both extend the same base class. They OBVIOUSLY share
				// the same datavalue. Right..?
				if (baseClass != null && baseClass.isAssignableFrom(nmsDisguiseClass)
						&& baseClass.isAssignableFrom(nmsEntityClass))
					continue;

				// So they don't extend a basic class.
				// Maybe if I check that they extend each other..
				// Seeing as I only store the finished forms of entitys. This
				// should raise no problems and allow for more shared
				// datawatchers.
				if (nmsEntityClass.isAssignableFrom(nmsDisguiseClass)
						|| nmsDisguiseClass.isAssignableFrom(nmsEntityClass))
					continue;
			}
			// Well I can't find a reason I should leave it alone. They will
			// probably conflict.
			// Time to set the value to the disguises value so no conflicts!
			this.getWatcher().setBackupValue(dataNo, disguiseValues.get(dataNo));
		}
	}

	public Disguise setVelocitySent(boolean sendVelocity) {
		this.velocitySent = sendVelocity;
		return this;
	}

	/**
	 * Can the disguised view himself as the disguise
	 *
	 * @param viewSelfDisguise
	 * @return
	 */
	public Disguise setViewSelfDisguise(boolean viewSelfDisguise) {
		if (this.isSelfDisguiseVisible() != viewSelfDisguise) {
			this.viewSelfDisguise = viewSelfDisguise;
			if (this.getEntity() != null && this.getEntity() instanceof Player)
				if (DisguiseAPI.getDisguise((Player) this.getEntity(), this.getEntity()) == this)
					if (this.isSelfDisguiseVisible())
						DisguiseUtilities.setupFakeDisguise(this);
					else
						DisguiseUtilities.removeSelfDisguise((Player) this.getEntity());
		}
		return this;
	}

	public Disguise setWatcher(FlagWatcher newWatcher) {
		if (!this.getType().getWatcherClass().isInstance(newWatcher))
			throw new IllegalArgumentException(newWatcher.getClass().getSimpleName() + " is not a instance of "
					+ this.getType().getWatcherClass().getSimpleName() + " for DisguiseType " + this.getType().name());
		this.watcher = newWatcher;
		if (this.getEntity() != null)
			this.setupWatcher();
		return this;
	}

	public boolean startDisguise() {
		if (!this.isDisguiseInUse()) {
			if (this.getEntity() == null)
				throw new RuntimeException("No entity is assigned to this disguise!");
			// Fire a disguise event
			final DisguiseEvent event = new DisguiseEvent(this.entity, this);
			Bukkit.getPluginManager().callEvent(event);
			// If they cancelled this disguise event. No idea why.
			// Just return.
			if (!event.isCancelled()) {
				this.disguiseInUse = true;
				this.task = Bukkit.getScheduler().runTaskTimer(BukkitAPI.get(), this.velocityRunnable, 1, 1);
				// Stick the disguise in the disguises bin
				DisguiseUtilities.addDisguise(this.entity.getUniqueId(), (TargetedDisguise) this);
				if (this.isSelfDisguiseVisible() && this.getEntity() instanceof Player)
					DisguiseUtilities.removeSelfDisguise((Player) this.getEntity());
				// Resend the disguised entity's packet
				DisguiseUtilities.refreshTrackers((TargetedDisguise) this);
				// If he is a player, then self disguise himself
				Bukkit.getScheduler().scheduleSyncDelayedTask(BukkitAPI.get(),
						() -> DisguiseUtilities.setupFakeDisguise(Disguise.this), 2);
				return true;
			}
		}
		return false;
	}

	public boolean stopDisguise() {
		return this.removeDisguise();
	}
}
