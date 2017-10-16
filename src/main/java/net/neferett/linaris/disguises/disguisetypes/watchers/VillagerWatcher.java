package net.neferett.linaris.disguises.disguisetypes.watchers;

import java.util.Random;

import org.bukkit.entity.Villager.Profession;

import net.neferett.linaris.disguises.disguisetypes.Disguise;

public class VillagerWatcher extends AgeableWatcher {

	public VillagerWatcher(Disguise disguise) {
		super(disguise);
		this.setProfession(Profession.values()[new Random().nextInt(Profession.values().length)]);
	}

	public Profession getProfession() {
		return Profession.values()[(Integer) this.getValue(16, 0)];
	}

	public void setProfession(int professionId) {
		this.setValue(16, professionId % 6);
		this.sendData(16);
	}

	@SuppressWarnings("deprecation")
	public void setProfession(Profession newProfession) {
		this.setProfession(newProfession.getId());
	}
}
