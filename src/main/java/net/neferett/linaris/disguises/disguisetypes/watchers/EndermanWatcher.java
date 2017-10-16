package net.neferett.linaris.disguises.disguisetypes.watchers;

import org.bukkit.inventory.ItemStack;

import net.neferett.linaris.disguises.disguisetypes.Disguise;

public class EndermanWatcher extends LivingWatcher {

	public EndermanWatcher(Disguise disguise) {
		super(disguise);
	}

	@SuppressWarnings("deprecation")
	@Override
	public ItemStack getItemInHand() {
		return new ItemStack((Byte) this.getValue(16, (byte) 0), 1, ((Byte) this.getValue(17, (byte) 0)));
	}

	public boolean isAggressive() {
		return (Byte) this.getValue(18, (byte) 0) == 1;
	}

	@Deprecated
	public boolean isAgressive() {
		return this.isAggressive();
	}

	public void setAggressive(boolean isAggressive) {
		this.setValue(18, (byte) (isAggressive ? 1 : 0));
		this.sendData(18);
	}

	@Deprecated
	public void setAgressive(boolean isAgressive) {
		this.setAggressive(isAgressive);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setItemInHand(ItemStack itemstack) {
		this.setValue(16, (short) (itemstack.getTypeId() & 255));
		this.setValue(17, (byte) (itemstack.getDurability() & 255));
	}

}
