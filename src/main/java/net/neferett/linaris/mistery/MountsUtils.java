package net.neferett.linaris.mistery;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import net.neferett.linaris.disguises.disguisetypes.Disguise;
import net.neferett.linaris.mistery.MysteryItem.PriceType;
import net.neferett.linaris.mistery.MysteryItem.RankType;
import net.neferett.linaris.mistery.mounts.Mount;
import net.neferett.linaris.mistery.mounts.mounts.CaveSpider.CaveSpiderMount;
import net.neferett.linaris.mistery.mounts.mounts.Chicken.ChickenMount;
import net.neferett.linaris.mistery.mounts.mounts.Rabbit.RabbitMount;
import net.neferett.linaris.mistery.mounts.mounts.Slime.SlimeMount;
import net.neferett.linaris.mistery.mounts.mounts.Wolf.WolfMount;
import net.neferett.linaris.mistery.mounts.mounts.blaze.BlazeMount;
import net.neferett.linaris.mistery.mounts.mounts.cow.CowMount;
import net.neferett.linaris.mistery.mounts.mounts.horse.HorseMount;
import net.neferett.linaris.mistery.mounts.mounts.irongolem.IronGolemMount;
import net.neferett.linaris.mistery.mounts.mounts.pig.PigMount;
import net.neferett.linaris.mistery.mounts.mounts.sheep.SuperSheep;
import net.neferett.linaris.mistery.mounts.mounts.skeletonhorse.SkeletonHorseMount;
import net.neferett.linaris.mistery.mounts.mounts.spider.SpiderMount;
import net.neferett.linaris.mistery.mounts.mounts.wither.WitherMount;
import net.neferett.linaris.mistery.mounts.mounts.zombiehorse.ZombieHorseMount;

@SuppressWarnings("deprecation")
public enum MountsUtils {

	Blaze("Blaze", 3000, PriceType.LC, RankType.VIPPLUS, new ItemStack(369), BlazeMount.class),
	CaveSpider("CaveSpider", 2000, PriceType.LC, null, new ItemStack(375), CaveSpiderMount.class),
	Chicken("Poulet", 2000, PriceType.LC, null, new ItemStack(344), ChickenMount.class),
	Cow("Vache", 2500, PriceType.LC, null, new ItemStack(334), CowMount.class),
	Dog("Chien", 2500, PriceType.LC, null, new ItemStack(352), WolfMount.class),
	Golem("Golem", 2500, PriceType.LC, null, new ItemStack(265), IronGolemMount.class),
	HORSE("Cheval", 2500, PriceType.LC, null, new ItemStack(383, 1, (short) 100), HorseMount.class),
	Pig("Cochon", 2000, PriceType.LC, null, new ItemStack(398), PigMount.class),
	Rabbit("Lapin", 1500, PriceType.LC, null, new ItemStack(415), RabbitMount.class),
	Sheep("Mouton", 1500, PriceType.LC, RankType.VIP, new ItemStack(35, 1, (short) 10), SuperSheep.class),
	SKELETONHORSE(
			"Cheval Squelette",
			2000,
			PriceType.LC,
			RankType.VIPPLUS,
			new ItemStack(397, 1, (short) 1),
			SkeletonHorseMount.class),
	Slime("Slime", 1500, PriceType.LC, null, new ItemStack(341), SlimeMount.class),
	Spider("Araignée", 2500, PriceType.LC, null, new ItemStack(30), SpiderMount.class),
	Wither("Wither", 5000, PriceType.LC, RankType.EPICVIP, new ItemStack(397, 1, (short) 1), WitherMount.class),
	ZOMBIEHORSE(
			"Cheval Zombie",
			1500,
			PriceType.LC,
			RankType.EPICVIP,
			new ItemStack(397, 1, (short) 2),
			ZombieHorseMount.class);

	public static ItemStack createItem(final Material leatherPiece, final String displayName, final Color color) {
		final ItemStack item = new ItemStack(leatherPiece);
		final LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setDisplayName(displayName);
		if (color != null)
			meta.setColor(Color.fromRGB(color.asRGB()));
		item.setItemMeta(meta);
		return item;
	}

	private Disguise				disguise;
	MysteryItem						item;
	private ItemStack				itemStack;
	private String					name;
	private int						price;
	private PriceType				priceType;
	private Class<? extends Mount>	type;

	private RankType				vipLevel;

	private MountsUtils(final String name, final int price, final PriceType priceType, final RankType vipLevel,
			final ItemStack itemStack, final Class<? extends Mount> type) {
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

	public Class<? extends Mount> getType() {
		return this.type;
	}

	public RankType getVipLevel() {
		return this.vipLevel;
	}

	public void setDisguise(final Disguise disguise) {
		this.disguise = disguise;
	}

	public void setItem(final MysteryItem item) {
		this.item = item;
	}

}
