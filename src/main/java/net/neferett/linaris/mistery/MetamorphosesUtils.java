package net.neferett.linaris.mistery;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import net.neferett.linaris.disguises.disguisetypes.Disguise;
import net.neferett.linaris.disguises.disguisetypes.DisguiseType;
import net.neferett.linaris.mistery.MysteryItem.PriceType;
import net.neferett.linaris.mistery.MysteryItem.RankType;

@SuppressWarnings("deprecation")
public enum MetamorphosesUtils {

	SHEEP("Mouton",10000,PriceType.LC,null,new ItemStack(383,1,(short) 11),DisguiseType.SHEEP),
	WOLF("Loup",0,PriceType.LC,RankType.VIP,new ItemStack(383,1,(short) 95),DisguiseType.WOLF),
	MUSHCOW("Vache Champignon",0,PriceType.LC,RankType.VIPPLUS,new ItemStack(383,1,(short) 96),DisguiseType.MUSHROOM_COW),
	POWERCREEPER("Creeper Chargé",0,PriceType.LC,RankType.EPICVIP,GlowUtils.addGlow(new ItemStack(383,1,(short) 50)),DisguiseType.CREEPER),
	GUARDIAN("Guardian",0,PriceType.LC,RankType.EPICVIP,new ItemStack(383,1,(short) 68),DisguiseType.GUARDIAN),
	
	SILVERFISH("SilverFish",1000,PriceType.LC,null,new ItemStack(383,1,(short) 60),DisguiseType.SILVERFISH),
	PIG("Cochon",1000,PriceType.LC,null,new ItemStack(383,1,(short) 90),DisguiseType.PIG),
	COW("Vache",1000,PriceType.LC,null,new ItemStack(383,1,(short) 92),DisguiseType.COW),
	SQUID("Poulpe",1200,PriceType.LC,null,new ItemStack(383,1,(short) 94),DisguiseType.SQUID),
	ENDERMAN("Enderman",1200,PriceType.LC,null,new ItemStack(383,1,(short) 58),DisguiseType.ENDERMAN),
	CHICKEN("Poulet",1200,PriceType.LC,null,new ItemStack(383,1,(short) 93),DisguiseType.CHICKEN),
	SPIDER("Arraign§e noire",1200,PriceType.LC,null,new ItemStack(383,1,(short) 52),DisguiseType.SPIDER),
	BLACKSPIDER("Arraignée des carvernes",1200,PriceType.LC,null,new ItemStack(383,1,(short) 59),DisguiseType.CAVE_SPIDER),
	CREEPER("Creeper",1600,PriceType.LC,RankType.VIPPLUS,new ItemStack(383,1,(short) 50),DisguiseType.CREEPER),
	
	BLAZE("Blaze",1800,PriceType.LC,RankType.VIP,new ItemStack(383,1,(short) 61),DisguiseType.BLAZE),
	CAT("Ocelot",1400,PriceType.LC,null,new ItemStack(383,1,(short) 98),DisguiseType.OCELOT),
	HORSE("Cheval",1600,PriceType.LC,null,new ItemStack(383,1,(short) 100),DisguiseType.HORSE),
	ZOMBIE("Zombie",1200,PriceType.LC,null,new ItemStack(383,1,(short) 51),DisguiseType.ZOMBIE),
	SKELETON("Squelette",1200,PriceType.LC,null,new ItemStack(383,1,(short) 54),DisguiseType.SKELETON),
	PIGZOMBIE("Cochon-Zombie",1400,PriceType.LC,null,new ItemStack(383,1,(short) 57),DisguiseType.PIG_ZOMBIE),
	WITCH("Sorcière",1800,PriceType.LC,null,new ItemStack(383,1,(short) 66),DisguiseType.WITCH),
	GIANT("Zombie Géant",8000,PriceType.LC,null,new ItemStack(383,1,(short) 53),DisguiseType.GIANT),
	BAT("Chauve-Souris",1200,PriceType.LC,null,new ItemStack(383,1,(short) 65),DisguiseType.BAT),
	
	WITHER("Squelette Wither",1600,PriceType.LC,RankType.EPICVIP,new ItemStack(397,1,(short) 1),DisguiseType.WITHER_SKELETON),
	SLIME("Slime",1600,PriceType.LC,null,new ItemStack(383,1,(short) 55),DisguiseType.SLIME),
	LAVASLIME("Slime de lave",1800,PriceType.LC,RankType.VIPPLUS,new ItemStack(383,1,(short) 62),DisguiseType.MAGMA_CUBE),
	SNOWGOLEM("Golem de neige",2000,PriceType.LC,null,new ItemStack(332,1,(short) 0),DisguiseType.SNOWMAN),
	GOLEM("Golem de fer",3000,PriceType.LC,RankType.VIPPLUS,new ItemStack(265,1,(short) 0),DisguiseType.IRON_GOLEM),
	RABBIT("Lapin",1400,PriceType.LC,null,new ItemStack(415,1,(short) 0),DisguiseType.RABBIT);

	private Disguise disguise;
	private String name;
	private int price;
	private PriceType priceType;
	private RankType vipLevel;
	private ItemStack itemStack;
	private DisguiseType type;
	MysteryItem item;

	private MetamorphosesUtils(String name,int price,PriceType priceType,RankType vipLevel,ItemStack itemStack,DisguiseType type) {
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
	
	public DisguiseType getType() {
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

