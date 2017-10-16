package net.neferett.linaris.disguises.disguisetypes.watchers;

import org.bukkit.inventory.ItemStack;

import net.neferett.linaris.disguises.disguisetypes.Disguise;
import net.neferett.linaris.disguises.disguisetypes.FlagWatcher;

public class ItemFrameWatcher extends FlagWatcher {

	public ItemFrameWatcher(Disguise disguise) {
		super(disguise);
	}

	@SuppressWarnings("deprecation")
	public ItemStack getItem() {
		if (this.getValue(2, null) == null)
			return new ItemStack(0);
		return (ItemStack) this.getValue(8, null);
	}

	public int getRotation() {
		return (Integer) this.getValue(9, 0);
	}

	@SuppressWarnings("deprecation")
	public void setItem(ItemStack newItem) {
		if (newItem == null)
			newItem = new ItemStack(0);
		newItem = newItem.clone();
		newItem.setAmount(1);
		this.setValue(8, newItem);
		this.sendData(8);
	}

	public void setRotation(int rotation) {
		this.setValue(9, (byte) (rotation % 4));
		this.sendData(9);
	}

}
