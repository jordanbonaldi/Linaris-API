package net.neferett.linaris.mistery;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import net.neferett.linaris.disguises.disguisetypes.Disguise;
import net.neferett.linaris.disguises.disguisetypes.DisguiseType;
import net.neferett.linaris.mistery.MysteryItem.PriceType;

@SuppressWarnings("deprecation")
public enum MetamorphosesUtils {

	BAT("Chauve-Souris", 12000, PriceType.TOKEN, 0, new ItemStack(383, 1, (short) 65), DisguiseType.BAT),
	BLACKSPIDER(
			"Arraignée des carvernes",
			12000,
			PriceType.TOKEN,
			5,
			new ItemStack(383, 1, (short) 59),
			DisguiseType.CAVE_SPIDER),
	BLAZE("Blaze", 18000, PriceType.TOKEN, 1, new ItemStack(383, 1, (short) 61), DisguiseType.BLAZE),
	CAT("Ocelot", 14000, PriceType.TOKEN, 0, new ItemStack(383, 1, (short) 98), DisguiseType.OCELOT),
	CHICKEN("Poulet", 12000, PriceType.TOKEN, 0, new ItemStack(383, 1, (short) 93), DisguiseType.CHICKEN),

	COW("Vache", 10000, PriceType.TOKEN, 0, new ItemStack(383, 1, (short) 92), DisguiseType.COW),
	CREEPER("Creeper", 16000, PriceType.TOKEN, 2, new ItemStack(383, 1, (short) 50), DisguiseType.CREEPER),
	ENDERMAN("Enderman", 12000, PriceType.TOKEN, 5, new ItemStack(383, 1, (short) 58), DisguiseType.ENDERMAN),
	GIANT("Zombie Géant", 80000, PriceType.TOKEN, 3, new ItemStack(383, 1, (short) 53), DisguiseType.GIANT),
	GOLEM("Golem de fer", 30000, PriceType.TOKEN, 3, new ItemStack(265, 1, (short) 0), DisguiseType.IRON_GOLEM),
	GUARDIAN("Guardian", 0, PriceType.TOKEN, 4, new ItemStack(383, 1, (short) 68), DisguiseType.GUARDIAN),
	HORSE("Cheval", 16000, PriceType.TOKEN, 0, new ItemStack(383, 1, (short) 100), DisguiseType.HORSE),
	LAVASLIME("Slime de lave", 18000, PriceType.TOKEN, 2, new ItemStack(383, 1, (short) 62), DisguiseType.MAGMA_CUBE),
	MUSHCOW("Vache Champignon", 0, PriceType.TOKEN, 2, new ItemStack(383, 1, (short) 96), DisguiseType.MUSHROOM_COW),

	PIG("Cochon", 10000, PriceType.TOKEN, 0, new ItemStack(383, 1, (short) 90), DisguiseType.PIG),
	PIGZOMBIE("Cochon-Zombie", 1400, PriceType.TOKEN, 0, new ItemStack(383, 1, (short) 57), DisguiseType.PIG_ZOMBIE),
	POWERCREEPER(
			"Creeper Chargé",
			0,
			PriceType.TOKEN,
			4,
			GlowUtils.addGlow(new ItemStack(383, 1, (short) 50)),
			DisguiseType.CREEPER),
	RABBIT("Lapin", 14000, PriceType.TOKEN, 0, new ItemStack(415, 1, (short) 0), DisguiseType.RABBIT),
	SHEEP("Mouton", 10000, PriceType.TOKEN, 0, new ItemStack(383, 1, (short) 11), DisguiseType.SHEEP),
	SILVERFISH("SilverFish", 10000, PriceType.TOKEN, 0, new ItemStack(383, 1, (short) 60), DisguiseType.SILVERFISH),
	SKELETON("Squelette", 12000, PriceType.TOKEN, 0, new ItemStack(383, 1, (short) 54), DisguiseType.SKELETON),
	SLIME("Slime", 16000, PriceType.TOKEN, 0, new ItemStack(383, 1, (short) 55), DisguiseType.SLIME),
	SNOWGOLEM("Golem de neige", 20000, PriceType.TOKEN, 0, new ItemStack(332, 1, (short) 0), DisguiseType.SNOWMAN),

	SPIDER("Arraign§e noire", 1200, PriceType.TOKEN, 0, new ItemStack(383, 1, (short) 52), DisguiseType.SPIDER),
	SQUID("Poulpe", 12000, PriceType.TOKEN, 0, new ItemStack(383, 1, (short) 94), DisguiseType.SQUID),
	WITCH("Sorcière", 18000, PriceType.TOKEN, 0, new ItemStack(383, 1, (short) 66), DisguiseType.WITCH),
	WITHER(
			"Squelette Wither",
			16000,
			PriceType.TOKEN,
			5,
			new ItemStack(397, 1, (short) 1),
			DisguiseType.WITHER_SKELETON),
	WOLF("Loup", 0, PriceType.TOKEN, 1, new ItemStack(383, 1, (short) 95), DisguiseType.WOLF),
	ZOMBIE("Zombie", 12000, PriceType.TOKEN, 0, new ItemStack(383, 1, (short) 51), DisguiseType.ZOMBIE);
	public static ItemStack createItem(final Material leatherPiece, final String displayName, final Color color) {
		final ItemStack item = new ItemStack(leatherPiece);
		final LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setDisplayName(displayName);
		if (color != null)
			meta.setColor(Color.fromRGB(color.asRGB()));
		item.setItemMeta(meta);
		return item;
	}

	private Disguise		disguise;
	MysteryItem				item;
	private ItemStack		itemStack;
	private String			name;
	private int				price;
	private PriceType		priceType;
	private DisguiseType	type;

	private int				vipLevel;

	private MetamorphosesUtils(final String name, final int price, final PriceType priceType, final int vipLevel,
			final ItemStack itemStack, final DisguiseType type) {
		this.name = name;
		this.price = price;
		this.priceType = priceType;
		this.vipLevel = vipLevel;
		this.itemStack = itemStack;
		this.type = type;
	}

	public Disguise getDisguise() {
		return this.disguise.clone();
	}

	public MysteryItem getItem() {
		return this.item;
	}

	public ItemStack getItemStack() {
		return this.itemStack;
	}

	public String getName() {
		return this.name;
	}

	public int getPrice() {
		return this.price;
	}

	public PriceType getPriceType() {
		return this.priceType;
	}

	public DisguiseType getType() {
		return this.type;
	}

	public int getVipLevel() {
		return this.vipLevel;
	}

	public void setDisguise(final Disguise disguise) {
		this.disguise = disguise;
	}

	public void setItem(final MysteryItem item) {
		this.item = item;
	}

}
