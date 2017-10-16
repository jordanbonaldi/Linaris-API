package net.neferett.linaris.utils;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import net.minecraft.server.v1_8_R3.NBTTagString;

public class GetHead {

	protected static int 	i = 0;
	protected String 		uuid;
	protected String		value;
	protected ItemStack		item;
	
	public GetHead(String uuid, String value){
		this.uuid = uuid;
		this.value = value;
		build();
	}
	
	public void build()
	  {
	    org.bukkit.inventory.ItemStack itemStack = new org.bukkit.inventory.ItemStack(Material.SKULL_ITEM, 1, (short)3);

	    ItemMeta PlusMeta = itemStack.getItemMeta();
	    PlusMeta.setDisplayName(i++ + "");
	    itemStack.setItemMeta(PlusMeta);

	    net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);

	    NBTTagCompound compound = nmsStack.getTag();
	    if (compound == null) {
	      compound = new NBTTagCompound();
	      nmsStack.setTag(compound);
	      compound = nmsStack.getTag();
	    }

	    NBTTagCompound skullOwner = new NBTTagCompound();
	    skullOwner.set("Id", new NBTTagString(this.uuid));
	    NBTTagCompound properties = new NBTTagCompound();
	    NBTTagList textures = new NBTTagList();
	    NBTTagCompound value = new NBTTagCompound();
	    value.set("Value", new NBTTagString(this.value));
	    textures.add(value);
	    properties.set("textures", textures);
	    skullOwner.set("Properties", properties);

	    compound.set("SkullOwner", skullOwner);
	    nmsStack.setTag(compound);

	    this.item =  CraftItemStack.asBukkitCopy(nmsStack);
	  }
	
	public String getValue() {
		return value;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public ItemStack getItem(){
		return this.item;
	}
}
