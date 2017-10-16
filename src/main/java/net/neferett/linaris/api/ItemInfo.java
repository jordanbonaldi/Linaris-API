package net.neferett.linaris.api;

public class ItemInfo {

	String uuid;
	int level;
	
	public ItemInfo(String uuid, int level) {
		this.uuid = uuid;
		this.level = level;
	}
	
	public String getUUID() {
		return uuid;
	}
	
	public int getLevel() {
		return level;
	}
	
}
