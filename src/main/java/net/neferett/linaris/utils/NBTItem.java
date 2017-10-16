package net.neferett.linaris.utils;

import org.bukkit.inventory.ItemStack;

public class NBTItem {

	private ItemStack bukkititem;
	
	public NBTItem(ItemStack Item){
		bukkititem = Item.clone();
	}
	
	public ItemStack getItem(){
		return bukkititem;
	}
	
	public NBTItem setString(String Key, String Text){
		bukkititem = NBTReflectionutil.setString(bukkititem, Key, Text);
		return this;
	}
	
	public String getString(String Key){
		return NBTReflectionutil.getString(bukkititem, Key);
	}
	
	public NBTItem setInteger(String key, Integer Int){
		bukkititem = NBTReflectionutil.setInt(bukkititem, key, Int);
		return this;
	}
	
	public Integer getInteger(String key){
		return NBTReflectionutil.getInt(bukkititem, key);
	}
	
	public NBTItem setDouble(String key, Double d){
		bukkititem = NBTReflectionutil.setDouble(bukkititem, key, d);
		return this;
	}
	
	public Double getDouble(String key){
		return NBTReflectionutil.getDouble(bukkititem, key);
	}
	
	public NBTItem setBoolean(String key, Boolean b){
		bukkititem = NBTReflectionutil.setBoolean(bukkititem, key, b);
		return this;
	}
	
	public Boolean getBoolean(String key){
		return NBTReflectionutil.getBoolean(bukkititem, key);
	}
	
	public Boolean hasKey(String key){
		return NBTReflectionutil.hasKey(bukkititem, key);
	}
	
}
