package net.neferett.linaris.disguises.disguisetypes.watchers;

import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Ocelot.Type;

import net.neferett.linaris.disguises.disguisetypes.Disguise;

public class OcelotWatcher extends TameableWatcher {

	public OcelotWatcher(Disguise disguise) {
		super(disguise);
	}

	@SuppressWarnings("deprecation")
	public Type getType() {
		return Ocelot.Type.getType((Byte) this.getValue(18, (byte) 0));
	}

	@SuppressWarnings("deprecation")
	public void setType(Type newType) {
		this.setValue(18, (byte) newType.getId());
		this.sendData(18);
	}
}
