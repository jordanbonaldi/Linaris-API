package net.neferett.linaris.disguises.disguisetypes.watchers;

import org.bukkit.inventory.ItemStack;

import net.neferett.linaris.disguises.disguisetypes.Disguise;
import net.neferett.linaris.disguises.disguisetypes.FlagWatcher;

public class DroppedItemWatcher extends FlagWatcher {

	public DroppedItemWatcher(Disguise disguise) {
		super(disguise);
	}

	@SuppressWarnings("deprecation")
	public ItemStack getItemStack() {
		return (ItemStack) this.getValue(10, new ItemStack(1));
	}

	public void setItemStack(ItemStack item) {
		this.setValue(10, item);
		this.sendData(10);
	}

}
