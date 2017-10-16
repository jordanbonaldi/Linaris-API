package net.neferett.linaris.mistery;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public enum OresUtils {

	FER("Fer",Material.IRON_HELMET,Material.IRON_CHESTPLATE,Material.IRON_LEGGINGS,Material.IRON_BOOTS,750,1500,1500,1200),
	OR("Or",Material.GOLD_HELMET,Material.GOLD_CHESTPLATE,Material.GOLD_LEGGINGS,Material.GOLD_BOOTS,950,2000,1800,1400),
	MAILLE("Maille",Material.CHAINMAIL_HELMET,Material.CHAINMAIL_CHESTPLATE,Material.CHAINMAIL_LEGGINGS,Material.CHAINMAIL_BOOTS,1150,2500,2000,1600),
	DIAMANT("Diamant",Material.DIAMOND_HELMET,Material.DIAMOND_CHESTPLATE,Material.DIAMOND_LEGGINGS,Material.DIAMOND_BOOTS,1300,3000,2500,1800);

	private String name;
	private Material mata;
	private Material matb;
	private Material matc;
	private Material matd;
	private int[] prices;
	
	MysteryItem helmet;
	MysteryItem chestplate;
	MysteryItem leggings;
	MysteryItem boots;

	private OresUtils(String name,Material mata,Material matb,Material matc,Material matd,int... prices) {
		this.name = name;
		this.mata = mata;
		this.matb = matb;
		this.matc = matc;
		this.matd = matd;
		this.prices = prices;
	}

	public void setBoots(MysteryItem boots) {
		this.boots = boots;
	}
	
	public void setChestplate(MysteryItem chestplate) {
		this.chestplate = chestplate;
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
	
	public MysteryItem getLeggings() {
		return leggings;
	}
	
	public Material getMata() {
		return mata;
	}
	
	public Material getMatb() {
		return matb;
	}
	
	public Material getMatc() {
		return matc;
	}
	
	public Material getMatd() {
		return matd;
	}
	
	public String getName() {
		return name;
	}
	
	public int[] getPrices() {
		return prices;
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

