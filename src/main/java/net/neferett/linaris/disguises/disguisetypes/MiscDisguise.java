package net.neferett.linaris.disguises.disguisetypes;

import java.security.InvalidParameterException;

import org.bukkit.Art;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.neferett.linaris.disguises.disguisetypes.watchers.DroppedItemWatcher;
import net.neferett.linaris.disguises.disguisetypes.watchers.FallingBlockWatcher;
import net.neferett.linaris.disguises.disguisetypes.watchers.PaintingWatcher;
import net.neferett.linaris.disguises.disguisetypes.watchers.SplashPotionWatcher;

public class MiscDisguise extends TargetedDisguise {

	private int id = -1, data = 0;

	public MiscDisguise(DisguiseType disguiseType) {
		this(disguiseType, -1, -1);
	}

	@Deprecated
	public MiscDisguise(DisguiseType disguiseType, boolean replaceSounds) {
		this(disguiseType, replaceSounds, -1, -1);
	}

	@Deprecated
	public MiscDisguise(DisguiseType disguiseType, boolean replaceSounds, int addictionalData) {
		this(disguiseType, replaceSounds,
				(disguiseType == DisguiseType.FALLING_BLOCK || disguiseType == DisguiseType.DROPPED_ITEM
						? addictionalData : -1),
				(disguiseType == DisguiseType.FALLING_BLOCK || disguiseType == DisguiseType.DROPPED_ITEM ? -1
						: addictionalData));
	}

	@Deprecated
	public MiscDisguise(DisguiseType disguiseType, boolean replaceSounds, int id, int data) {
		this(disguiseType, id, data);
		this.setReplaceSounds(replaceSounds);
	}

	public MiscDisguise(DisguiseType disguiseType, int id) {
		this(disguiseType, id, -1);
	}

	@SuppressWarnings("deprecation")
	public MiscDisguise(DisguiseType disguiseType, int firstParam, int secondParam) {
		if (!disguiseType.isMisc())
			throw new InvalidParameterException(
					"Expected a non-living DisguiseType while constructing MiscDisguise. Received " + disguiseType
							+ " instead. Please use " + (disguiseType.isPlayer() ? "PlayerDisguise" : "MobDisguise")
							+ " instead");
		this.createDisguise(disguiseType);
		this.id = this.getType().getEntityId();
		this.data = this.getType().getDefaultId();
		switch (disguiseType) {
		// The only disguises which should use a custom data.
		case PAINTING:
			((PaintingWatcher) this.getWatcher()).setArt(Art.values()[Math.max(0, firstParam) % Art.values().length]);
			break;
		case FALLING_BLOCK:
			((FallingBlockWatcher) this.getWatcher())
					.setBlock(new ItemStack(Math.max(1, firstParam), 1, (short) Math.max(0, secondParam)));
			break;
		case SPLASH_POTION:
			((SplashPotionWatcher) this.getWatcher()).setPotionId(Math.max(0, firstParam));
			break;
		case DROPPED_ITEM:
			if (firstParam > 0)
				((DroppedItemWatcher) this.getWatcher())
						.setItemStack(new ItemStack(firstParam, Math.max(0, secondParam)));
			break;
		case FISHING_HOOK: // Entity ID of whoever is holding fishing rod
		case ARROW: // Entity ID of shooter. Used for "Is he on this scoreboard
					// team and do I render it moving through his body?"
		case SMALL_FIREBALL: // Unknown. Uses entity id of shooter. 0 if no
								// shooter
		case FIREBALL: // Unknown. Uses entity id of shooter. 0 if no shooter
		case WITHER_SKULL: // Unknown. Uses entity id of shooter. 0 if no
							// shooter
			this.data = firstParam;
			break;
		default:
			break;
		}
	}

	@Deprecated
	public MiscDisguise(EntityType entityType) {
		this(entityType, -1, -1);
	}

	@Deprecated
	public MiscDisguise(EntityType entityType, boolean replaceSounds) {
		this(entityType, replaceSounds, -1, -1);
	}

	@Deprecated
	public MiscDisguise(EntityType entityType, boolean replaceSounds, int id, int data) {
		this(DisguiseType.getType(entityType), replaceSounds, id, data);
	}

	@Deprecated
	public MiscDisguise(EntityType entityType, int id) {
		this(entityType, id, -1);
	}

	@Deprecated
	public MiscDisguise(EntityType disguiseType, int id, int data) {
		this(DisguiseType.getType(disguiseType), id, data);
	}

	@Override
	public MiscDisguise addPlayer(Player player) {
		return (MiscDisguise) super.addPlayer(player);
	}

	@Override
	public MiscDisguise addPlayer(String playername) {
		return (MiscDisguise) super.addPlayer(playername);
	}

	@Override
	public MiscDisguise clone() {
		final MiscDisguise disguise = new MiscDisguise(this.getType(), this.getData());
		disguise.setReplaceSounds(this.isSoundsReplaced());
		disguise.setViewSelfDisguise(this.isSelfDisguiseVisible());
		disguise.setHearSelfDisguise(this.isSelfDisguiseSoundsReplaced());
		disguise.setHideArmorFromSelf(this.isHidingArmorFromSelf());
		disguise.setHideHeldItemFromSelf(this.isHidingHeldItemFromSelf());
		disguise.setVelocitySent(this.isVelocitySent());
		disguise.setModifyBoundingBox(this.isModifyBoundingBox());
		disguise.setWatcher(this.getWatcher().clone(disguise));
		return disguise;
	}

	/**
	 * This is the getId of everything but falling block.
	 */
	@SuppressWarnings("deprecation")
	public int getData() {
		switch (this.getType()) {
		case FALLING_BLOCK:
			return ((FallingBlockWatcher) this.getWatcher()).getBlock().getDurability();
		case PAINTING:
			return ((PaintingWatcher) this.getWatcher()).getArt().getId();
		case SPLASH_POTION:
			return ((SplashPotionWatcher) this.getWatcher()).getPotionId();
		default:
			return this.data;
		}
	}

	/**
	 * Only falling block should use this
	 */
	@SuppressWarnings("deprecation")
	public int getId() {
		if (this.getType() == DisguiseType.FALLING_BLOCK)
			return ((FallingBlockWatcher) this.getWatcher()).getBlock().getTypeId();
		return this.id;
	}

	@Override
	public boolean isMiscDisguise() {
		return true;
	}

	@Override
	public MiscDisguise removePlayer(Player player) {
		return (MiscDisguise) super.removePlayer(player);
	}

	@Override
	public MiscDisguise removePlayer(String playername) {
		return (MiscDisguise) super.removePlayer(playername);
	}

	@Override
	public MiscDisguise setDisguiseTarget(TargetType newTargetType) {
		return (MiscDisguise) super.setDisguiseTarget(newTargetType);
	}

	@Override
	public MiscDisguise setEntity(Entity entity) {
		return (MiscDisguise) super.setEntity(entity);
	}

	@Override
	public MiscDisguise setHearSelfDisguise(boolean hearSelfDisguise) {
		return (MiscDisguise) super.setHearSelfDisguise(hearSelfDisguise);
	}

	@Override
	public MiscDisguise setHideArmorFromSelf(boolean hideArmor) {
		return (MiscDisguise) super.setHideArmorFromSelf(hideArmor);
	}

	@Override
	public MiscDisguise setHideHeldItemFromSelf(boolean hideHeldItem) {
		return (MiscDisguise) super.setHideHeldItemFromSelf(hideHeldItem);
	}

	@Override
	public MiscDisguise setKeepDisguiseOnEntityDespawn(boolean keepDisguise) {
		return (MiscDisguise) super.setKeepDisguiseOnEntityDespawn(keepDisguise);
	}

	@Override
	public MiscDisguise setKeepDisguiseOnPlayerDeath(boolean keepDisguise) {
		return (MiscDisguise) super.setKeepDisguiseOnPlayerDeath(keepDisguise);
	}

	@Override
	public MiscDisguise setKeepDisguiseOnPlayerLogout(boolean keepDisguise) {
		return (MiscDisguise) super.setKeepDisguiseOnPlayerLogout(keepDisguise);
	}

	@Override
	public MiscDisguise setModifyBoundingBox(boolean modifyBox) {
		return (MiscDisguise) super.setModifyBoundingBox(modifyBox);
	}

	@Override
	public MiscDisguise setReplaceSounds(boolean areSoundsReplaced) {
		return (MiscDisguise) super.setReplaceSounds(areSoundsReplaced);
	}

	@Override
	public MiscDisguise setVelocitySent(boolean sendVelocity) {
		return (MiscDisguise) super.setVelocitySent(sendVelocity);
	}

	@Override
	public MiscDisguise setViewSelfDisguise(boolean viewSelfDisguise) {
		return (MiscDisguise) super.setViewSelfDisguise(viewSelfDisguise);
	}

	@Override
	public MiscDisguise setWatcher(FlagWatcher newWatcher) {
		return (MiscDisguise) super.setWatcher(newWatcher);
	}

	@Override
	public MiscDisguise silentlyAddPlayer(String playername) {
		return (MiscDisguise) super.silentlyAddPlayer(playername);
	}

	@Override
	public MiscDisguise silentlyRemovePlayer(String playername) {
		return (MiscDisguise) super.silentlyRemovePlayer(playername);
	}

}
