package net.neferett.linaris.mistery;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public enum ColorUtils {

	NORMAL(null,500,1000,1000,500),
	BLACK(Color.BLACK,750,2000,1500,750), 
	    DARK_BLUE(Color.NAVY,750,2000,1500,750), 
	    DARK_GREEN(Color.GREEN,750,2000,1500,750), 
	    DARK_AQUA(Color.AQUA,750,2000,1500,750), 
	    DARK_RED(Color.RED,750,2000,1500,750), 
	    DARK_PURPLE(Color.PURPLE,750,2000,1500,750), 
	    GOLD(Color.ORANGE,750,2000,1500,750), 
	    GRAY(Color.GRAY,750,2000,1500,750), 
	    DARK_GRAY(Color.SILVER,750,2000,1500,750), 
	    BLUE(Color.BLUE,750,2000,1500,750), 
	    GREEN(Color.LIME,750,2000,1500,750), 
	    RED(Color.MAROON,750,2000,1500,750), 
	    LIGHT_PURPLE(Color.FUCHSIA,750,2000,1500,750), 
	    YELLOW(Color.YELLOW,750,2000,1500,750), 
	    WHITE(Color.WHITE,750,2000,1500,750),
	    OLIVE(Color.OLIVE,750,2000,1500,750),
	    TEAL(Color.TEAL,750,2000,1500,750);

	private Color color;
	private int[] prices;
	MysteryItem helmet;
	MysteryItem chestplate;
	MysteryItem leggings;
	MysteryItem boots;
	

	private ColorUtils(Color color,int... prices) {
		this.color = color;
		this.prices = prices;
	}

	public Color getColor() {
		return color;
	}

	public void setBoots(MysteryItem boots) {
		this.boots = boots;
	}
	
	public void setChestplate(MysteryItem chestplate) {
		this.chestplate = chestplate;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void setHelmet(MysteryItem helmet) {
		this.helmet = helmet;
	}
	
	public void setLeggings(MysteryItem leggings) {
		this.leggings = leggings;
	}
	
	public MysteryItem getBoots() {
		return boots;
	}
	
	public MysteryItem getChestplate() {
		return chestplate;
	}
	
	public MysteryItem getHelmet() {
		return helmet;
	}
	
	public int[] getPrices() {
		return prices;
	}
	
	public MysteryItem getLeggings() {
		return leggings;
	}

	public static ItemStack createItem(Material leatherPiece, String displayName, Color color) {
		ItemStack item = new ItemStack(leatherPiece);
		LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setDisplayName(displayName);
		if (color != null)
		meta.setColor(Color.fromRGB(color.asRGB()));
		item.setItemMeta(meta);
		return item;
	}

}

