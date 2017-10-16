package net.neferett.linaris.disguises.disguisetypes.watchers;

import org.bukkit.inventory.ItemStack;

import net.neferett.linaris.disguises.disguisetypes.Disguise;
import net.neferett.linaris.disguises.disguisetypes.FlagWatcher;

public class MinecartWatcher extends FlagWatcher {

	public MinecartWatcher(Disguise disguise) {
		super(disguise);
	}

	@SuppressWarnings("deprecation")
	public ItemStack getBlockInCart() {
		final int id = (Integer) this.getValue(20, 0) & 0xffff;
		final int data = (Integer) this.getValue(20, 0) >> 16;
		return new ItemStack(id, 1, (short) data);
	}

	public int getBlockOffset() {
		return (Integer) this.getValue(21, 0);
	}

	@Deprecated
	public int getBlockOffSet() {
		return this.getBlockOffset();
	}

	public float getDamage() {
		return (Float) this.getValue(19, 0F);
	}

	public boolean getViewBlockInCart() {
		return ((Byte) this.getValue(22, (byte) 0)) == (byte) 1;
	}

	public void setBlockInCart(ItemStack item) {
		@SuppressWarnings("deprecation")
		final int id = item.getTypeId();
		final int data = item.getDurability();
		this.setValue(20, id & 0xffff | data << 16);
		this.setValue(22, (byte) 1);
		this.sendData(20, 22);
	}

	public void setBlockOffset(int i) {
		this.setValue(21, i);
		this.sendData(21);
	}

	@Deprecated
	public void setBlockOffSet(int i) {
		this.setBlockOffset(i);
	}

	public void setDamage(float damage) {
		this.setValue(19, damage);
		this.sendData(19);
	}

	public void setViewBlockInCart(boolean viewBlock) {
		this.setValue(22, (byte) (viewBlock ? 1 : 0));
		this.sendData(22);
	}
}
