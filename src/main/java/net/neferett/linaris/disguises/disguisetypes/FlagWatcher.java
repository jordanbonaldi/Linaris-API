package net.neferett.linaris.disguises.disguisetypes;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;

import net.neferett.linaris.disguises.DisguiseAPI;
import net.neferett.linaris.disguises.DisguiseConfig;
import net.neferett.linaris.disguises.utilities.DisguiseUtilities;
import net.neferett.linaris.disguises.utilities.ReflectionManager;

public class FlagWatcher {

	public enum SlotType {

		BOOTS(0), CHESTPLATE(2), HELD_ITEM(4), HELMET(3), LEGGINGS(1);
		// The ints is for bukkit. Not nms slots.
		private int slotNo = 0;

		SlotType(int no) {
			this.slotNo = no;
		}

		public int getSlot() {
			return this.slotNo;
		}
	}

	private boolean addEntityAnimations = DisguiseConfig.isEntityAnimationsAdded();
	/**
	 * This is the entity values I need to add else it could crash them..
	 */
	private final HashMap<Integer, Object> backupEntityValues = new HashMap<>();
	private final TargetedDisguise disguise;
	private HashMap<Integer, Object> entityValues = new HashMap<>();
	private boolean hasDied;
	private ItemStack[] items = new ItemStack[5];
	private HashSet<Integer> modifiedEntityAnimations = new HashSet<>();
	private List<WrappedWatchableObject> watchableObjects;

	public FlagWatcher(Disguise disguise) {
		this.disguise = (TargetedDisguise) disguise;
	}

	private byte addEntityAnimations(byte originalValue, byte entityValue) {
		byte valueByte = originalValue;
		for (int i = 0; i < 6; i++)
			if ((entityValue & 1 << i) != 0 && !this.modifiedEntityAnimations.contains(i))
				valueByte = (byte) (valueByte | 1 << i);
		originalValue = valueByte;
		return originalValue;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public FlagWatcher clone(Disguise owningDisguise) {
		FlagWatcher cloned;
		try {
			cloned = this.getClass().getConstructor(Disguise.class).newInstance(this.getDisguise());
		} catch (final Exception e) {
			e.printStackTrace(System.out);
			cloned = new FlagWatcher(this.getDisguise());
		}
		cloned.entityValues = (HashMap<Integer, Object>) this.entityValues.clone();
		cloned.items = this.items.clone();
		cloned.modifiedEntityAnimations = (HashSet) this.modifiedEntityAnimations.clone();
		cloned.addEntityAnimations = this.addEntityAnimations;
		return cloned;
	}

	public List<WrappedWatchableObject> convert(List<WrappedWatchableObject> list) {
		final List<WrappedWatchableObject> newList = new ArrayList<>();
		final HashSet<Integer> sentValues = new HashSet<>();
		boolean sendAllCustom = false;
		for (WrappedWatchableObject watch : list) {
			final int dataType = watch.getIndex();
			sentValues.add(dataType);
			// Its sending the air metadata. This is the least commonly sent
			// metadata which all entitys still share.
			// I send my custom values if I see this!
			if (dataType == 1)
				sendAllCustom = true;
			Object value = null;
			if (this.entityValues.containsKey(dataType)) {
				if (this.entityValues.get(dataType) == null)
					continue;
				value = this.entityValues.get(dataType);
			} else if (this.backupEntityValues.containsKey(dataType)) {
				if (this.backupEntityValues.get(dataType) == null)
					continue;
				value = this.backupEntityValues.get(dataType);
			}
			if (value != null) {
				if (this.isEntityAnimationsAdded() && dataType == 0)
					value = this.addEntityAnimations((Byte) value, (Byte) watch.getValue());
				final boolean isDirty = watch.getDirtyState();
				watch = new WrappedWatchableObject(dataType, value);
				if (!isDirty)
					watch.setDirtyState(false);
			} else {
				final boolean isDirty = watch.getDirtyState();
				watch = new WrappedWatchableObject(dataType, watch.getValue());
				if (!isDirty)
					watch.setDirtyState(false);
			}
			newList.add(watch);
		}
		if (sendAllCustom)
			// Its sending the entire meta data. Better add the custom meta
			for (final int value : this.entityValues.keySet()) {
			if (sentValues.contains(value))
			continue;
			final Object obj = this.entityValues.get(value);
			if (obj == null)
			continue;
			final WrappedWatchableObject watch = new WrappedWatchableObject(value, obj);
			newList.add(watch);
			}
		// Here we check for if there is a health packet that says they died.
		if (this.getDisguise().isSelfDisguiseVisible() && this.getDisguise().getEntity() != null
				&& this.getDisguise().getEntity() instanceof Player)
			for (final WrappedWatchableObject watch : newList)
				// Its a health packet
				if (watch.getIndex() == 6) {
					final Object value = watch.getValue();
					if (value != null && value instanceof Float) {
						final float newHealth = (Float) value;
						if (newHealth > 0 && this.hasDied) {
							this.hasDied = false;
							DisguiseUtilities.sendSelfDisguise((Player) this.getDisguise().getEntity(), this.disguise);
						} else if (newHealth <= 0 && !this.hasDied)
							this.hasDied = true;
					}
				}
		return newList;
	}

	public ItemStack[] getArmor() {
		final ItemStack[] armor = new ItemStack[4];
		System.arraycopy(this.items, 0, armor, 0, 4);
		return armor;
	}

	public String getCustomName() {
		return (String) this.getValue(2, null);
	}

	protected TargetedDisguise getDisguise() {
		return this.disguise;
	}

	private boolean getFlag(int byteValue) {
		return ((Byte) this.getValue(0, (byte) 0) & 1 << byteValue) != 0;
	}

	public ItemStack getItemInHand() {
		return this.getItemStack(SlotType.HELD_ITEM);
	}

	public ItemStack getItemStack(int slot) {
		return this.items[slot];
	}

	public ItemStack getItemStack(SlotType slot) {
		return this.getItemStack(slot.getSlot());
	}

	protected Object getValue(int no, Object backup) {
		if (this.entityValues.containsKey(no))
			return this.entityValues.get(no);
		return backup;
	}

	public List<WrappedWatchableObject> getWatchableObjects() {
		if (this.watchableObjects == null)
			this.rebuildWatchableObjects();
		return this.watchableObjects;
	}

	public boolean hasCustomName() {
		return this.getCustomName() != null;
	}

	protected boolean hasValue(int no) {
		return this.entityValues.containsKey(no);
	}

	public boolean isBurning() {
		return this.getFlag(0);
	}

	public boolean isCustomNameVisible() {
		return (byte) this.getValue(3, (byte) 0) == 1;
	}

	public boolean isEntityAnimationsAdded() {
		return this.addEntityAnimations;
	}

	public boolean isInvisible() {
		return this.getFlag(5);
	}

	public boolean isRightClicking() {
		return this.getFlag(4);
	}

	public boolean isSneaking() {
		return this.getFlag(1);
	}

	public boolean isSprinting() {
		return this.getFlag(3);
	}

	public void rebuildWatchableObjects() {
		this.watchableObjects = new ArrayList<>();
		for (int i = 0; i <= 31; i++) {
			WrappedWatchableObject watchable = null;
			if (this.entityValues.containsKey(i) && this.entityValues.get(i) != null)
				watchable = new WrappedWatchableObject(i, this.entityValues.get(i));
			else if (this.backupEntityValues.containsKey(i) && this.backupEntityValues.get(i) != null)
				watchable = new WrappedWatchableObject(i, this.backupEntityValues.get(i));
			if (watchable != null)
				this.watchableObjects.add(watchable);
		}
	}

	protected void sendData(int... dataValues) {
		if (!DisguiseAPI.isDisguiseInUse(this.getDisguise()) || this.getDisguise().getWatcher() != this)
			return;
		final List<WrappedWatchableObject> list = new ArrayList<>();
		for (final int data : dataValues) {
			if (!this.entityValues.containsKey(data) || this.entityValues.get(data) == null)
				continue;
			Object value = this.entityValues.get(data);
			if (this.isEntityAnimationsAdded() && DisguiseConfig.isMetadataPacketsEnabled() && data == 0)
				value = this.addEntityAnimations((Byte) value,
						WrappedDataWatcher.getEntityWatcher(this.disguise.getEntity()).getByte(0));
			list.add(new WrappedWatchableObject(data, value));
		}
		if (!list.isEmpty()) {
			final PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
			final StructureModifier<Object> mods = packet.getModifier();
			mods.write(0, this.getDisguise().getEntity().getEntityId());
			packet.getWatchableCollectionModifier().write(0, list);
			for (final Player player : DisguiseUtilities.getPerverts(this.getDisguise()))
				try {
					ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
				} catch (final InvocationTargetException e) {
					e.printStackTrace(System.out);
				}
		}
	}

	public void setAddEntityAnimations(boolean isEntityAnimationsAdded) {
		this.addEntityAnimations = isEntityAnimationsAdded;
	}

	public void setArmor(ItemStack[] itemstack) {
		for (int i = 0; i < itemstack.length; i++)
			this.setItemStack(i, itemstack[i]);
	}

	protected void setBackupValue(int no, Object value) {
		this.backupEntityValues.put(no, value);
	}

	public void setBurning(boolean setBurning) {
		this.setFlag(0, setBurning);
		this.sendData(0);
	}

	public void setCustomName(String name) {
		if (name != null && name.length() > 64)
			name = name.substring(0, 64);
		this.setValue(2, name);
		this.sendData(2);
	}

	public void setCustomNameVisible(boolean display) {
		this.setValue(3, (byte) (display ? 1 : 0));
		this.sendData(3);
	}

	private void setFlag(int byteValue, boolean flag) {
		this.modifiedEntityAnimations.add(byteValue);
		final byte b0 = (Byte) this.getValue(0, (byte) 0);
		if (flag)
			this.setValue(0, (byte) (b0 | 1 << byteValue));
		else
			this.setValue(0, (byte) (b0 & ~(1 << byteValue)));
	}

	public void setInvisible(boolean setInvis) {
		this.setFlag(5, setInvis);
		this.sendData(0);
	}

	public void setItemInHand(ItemStack itemstack) {
		this.setItemStack(SlotType.HELD_ITEM, itemstack);
	}

	@SuppressWarnings("deprecation")
	public void setItemStack(int slot, ItemStack itemStack) {
		// Itemstack which is null means that its not replacing the disguises
		// itemstack.
		if (itemStack == null)
			// Find the item to replace it with
			if (this.getDisguise().getEntity() instanceof LivingEntity) {
			final EntityEquipment equipment = ((LivingEntity) this.getDisguise().getEntity()).getEquipment();
			if (slot == 4)
			itemStack = equipment.getItemInHand();
			else
			itemStack = equipment.getArmorContents()[slot];
			if (itemStack != null && itemStack.getTypeId() == 0)
			itemStack = null;
			}

		Object itemToSend = null;
		if (itemStack != null && itemStack.getTypeId() != 0)
			itemToSend = ReflectionManager.getNmsItem(itemStack);
		this.items[slot] = itemStack;
		if (DisguiseAPI.isDisguiseInUse(this.getDisguise()) && this.getDisguise().getWatcher() == this) {
			slot++;
			if (slot > 4)
				slot = 0;
			final PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT);
			final StructureModifier<Object> mods = packet.getModifier();
			mods.write(0, this.getDisguise().getEntity().getEntityId());
			mods.write(1, slot);
			mods.write(2, itemToSend);
			for (final Player player : DisguiseUtilities.getPerverts(this.getDisguise()))
				try {
					ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
				} catch (final InvocationTargetException e) {
					e.printStackTrace(System.out);
				}
		}
	}

	public void setItemStack(SlotType slot, ItemStack itemStack) {
		this.setItemStack(slot.getSlot(), itemStack);
	}

	public void setRightClicking(boolean setRightClicking) {
		this.setFlag(4, setRightClicking);
		this.sendData(0);
	}

	public void setSneaking(boolean setSneaking) {
		this.setFlag(1, setSneaking);
		this.sendData(0);
	}

	public void setSprinting(boolean setSprinting) {
		this.setFlag(3, setSprinting);
		this.sendData(0);
	}

	protected void setValue(int no, Object value) {
		this.entityValues.put(no, value);
		if (!DisguiseConfig.isMetadataPacketsEnabled())
			this.rebuildWatchableObjects();
	}

}
