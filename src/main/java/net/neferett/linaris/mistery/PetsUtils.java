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
	Bat("Chauve-Souris", 3000, PriceType.TOKEN, null, new ItemStack(383, 1, (short) 65), PetBat.class),
	Chat("Chat", 5000, PriceType.TOKEN, null, new ItemStack(383, 1, (short) 90), PetOcelot.class),
	Chicken("Poulet", 3000, PriceType.TOKEN, null, new ItemStack(383, 1, (short) 93), PetChicken.class),
	Cow("Vache", 3000, PriceType.TOKEN, null, new ItemStack(383, 1, (short) 92), PetCow.class),
	Creeper("Creeper", 3000, PriceType.TOKEN, RankType.EPICVIP, new ItemStack(383, 1, (short) 50), PetCreeper.class),
	EnderMite("Endermite", 3000, PriceType.TOKEN, null, new ItemStack(383, 1, (short) 67), PetEnderMite.class),
	Lapin("Lapin", 7000, PriceType.TOKEN, RankType.VIP, new ItemStack(383, 1, (short) 11), PetLapin.class),
	Magma("MagmaCube", 3000, PriceType.TOKEN, null, new ItemStack(383, 1, (short) 62), PetMagMaCube.class),
	MooshroomCow("Vache champignon", 3000, PriceType.TOKEN, null, new ItemStack(383, 1, (short) 96), PetMushroomCow.class),
	Pig("Cochon", 3000, PriceType.TOKEN, null, new ItemStack(383, 1, (short) 90), PetPig.class),
	Sheep("Mouton", 3000, PriceType.TOKEN, null, new ItemStack(383, 1, (short) 91), PetSheep.class),
	SilverFish("SilverFish", 3000, PriceType.TOKEN, null, new ItemStack(383, 1, (short) 60), PetSilverFish.class),
	Slime("Slime", 3000, PriceType.TOKEN, null, new ItemStack(383, 1, (short) 55), PetSlime.class),
	Villager("Villageois", 7000, PriceType.TOKEN, RankType.VIP, new ItemStack(383, 1, (short) 120), PetVillager.class),
	Wither("Wither", 30000, PriceType.TOKEN, RankType.EPICVIP, new ItemStack(383, 1, (short) 58), PetWither.class),
	Wolf("Chien", 10000, PriceType.TOKEN, null, new ItemStack(383, 1, (short) 95), PetWolf.class),
	Zombie("Zombie", 3000, PriceType.TOKEN, RankType.VIPPLUS, new ItemStack(383, 1, (short) 54), PetZombie.class);

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
	private Class<? extends Pet>	type;

	private RankType				vipLevel;

	private PetsUtils(final String name, final int price, final PriceType priceType, final RankType vipLevel,
			final ItemStack itemStack, final Class<? extends Pet> type) {
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

	public Class<? extends Pet> getType() {
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
