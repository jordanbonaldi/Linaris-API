package net.neferett.linaris.mistery;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import net.neferett.linaris.api.pets.Pet;
import net.neferett.linaris.api.pets.PetBat;
import net.neferett.linaris.api.pets.PetChicken;
import net.neferett.linaris.api.pets.PetCow;
import net.neferett.linaris.api.pets.PetCreeper;
import net.neferett.linaris.api.pets.PetEnderMite;
import net.neferett.linaris.api.pets.PetLapin;
import net.neferett.linaris.api.pets.PetMagMaCube;
import net.neferett.linaris.api.pets.PetMushroomCow;
import net.neferett.linaris.api.pets.PetOcelot;
import net.neferett.linaris.api.pets.PetPig;
import net.neferett.linaris.api.pets.PetSheep;
import net.neferett.linaris.api.pets.PetSilverFish;
import net.neferett.linaris.api.pets.PetSlime;
import net.neferett.linaris.api.pets.PetVillager;
import net.neferett.linaris.api.pets.PetWither;
import net.neferett.linaris.api.pets.PetWolf;
import net.neferett.linaris.api.pets.PetZombie;
import net.neferett.linaris.disguises.disguisetypes.Disguise;
import net.neferett.linaris.mistery.MysteryItem.PriceType;
import net.neferett.linaris.mistery.MysteryItem.RankType;

@SuppressWarnings("deprecation")
public enum PetsUtils {
	Wither("Wither",4500,PriceType.LC,RankType.EPICVIP,new ItemStack(383,1,(short) 58),PetWither.class),
	Creeper("Creeper",2500,PriceType.LC,RankType.EPICVIP,new ItemStack(383,1,(short) 50),PetCreeper.class),
	Zombie("Zombie",2500,PriceType.LC,RankType.VIPPLUS,new ItemStack(383,1,(short) 54),PetZombie.class),
	Villager("Villageois",2500,PriceType.LC,RankType.VIP,new ItemStack(383,1,(short) 120),PetVillager.class),
	Cow("Vache",1300,PriceType.LC,null,new ItemStack(383,1,(short) 92),PetCow.class),
	MooshroomCow("Vache champignon",1300,PriceType.LC,null,new ItemStack(383,1,(short) 96),PetMushroomCow.class),
	Magma("MagmaCube",1400,PriceType.LC,null,new ItemStack(383,1,(short) 62),PetMagMaCube.class),
	Lapin("Lapin",1500,PriceType.LC,RankType.VIP,new ItemStack(383,1,(short) 11),PetLapin.class),
	Pig("Cochon",1250,PriceType.LC,null,new ItemStack(383,1,(short)90),PetPig.class),
	Sheep("Mouton",1350,PriceType.LC,null,new ItemStack(383,1,(short)91),PetSheep.class),
	Chat("Chat",1400,PriceType.LC,null,new ItemStack(383,1,(short)90),PetOcelot.class),
	Slime("Slime",1200,PriceType.LC,null,new ItemStack(383,1,(short)55),PetSlime.class),
	Wolf("Chien",1500,PriceType.LC,null,new ItemStack(383,1,(short)95),PetWolf.class),
	SilverFish("SilverFish",1200,PriceType.LC,null,new ItemStack(383,1,(short)60),PetSilverFish.class),
	EnderMite("Endermite",1200,PriceType.LC,null,new ItemStack(383,1,(short)67),PetEnderMite.class),
	Chicken("Poulet",1350,PriceType.LC,null,new ItemStack(383,1,(short)93),PetChicken.class),
	Bat("Chauve-Souris",1300,PriceType.LC,null,new ItemStack(383,1,(short)65),PetBat.class);


	
	private Disguise disguise;
	private String name;
	private int price;
	private PriceType priceType;
	private RankType vipLevel;
	private ItemStack itemStack;
	private Class<? extends Pet> type;
	MysteryItem item;

	private PetsUtils(String name,int price,PriceType priceType,RankType vipLevel,ItemStack itemStack,Class<? extends Pet> type) {
		this.name = name;
		this.price = price;
		this.priceType = priceType;
		this.vipLevel = vipLevel;
		this.itemStack = itemStack;
		this.type = type;
	}

	public String getName() {
		return name;
	}
	
	public int getPrice() {
		return price;
	}
	
	public PriceType getPriceType() {
		return priceType;
	}
	
	public RankType getVipLevel() {
		return vipLevel;
	}
	
	public ItemStack getItemStack() {
		return itemStack;
	}
	
	public void setItem(MysteryItem item) {
		this.item = item;
	}
	
	public MysteryItem getItem() {
		return item;
	}
	
	public Class<? extends Pet> getType() {
		return type;
	}
	
	public void setDisguise(Disguise disguise) {
		this.disguise = disguise;
	}
	
	public Disguise getDisguise() {
		return disguise.clone();
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

