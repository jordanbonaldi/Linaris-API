package net.neferett.linaris.logo.gui.color;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.neferett.linaris.api.PlayerData;
import net.neferett.linaris.inventory.InstantShopItem;
import net.neferett.linaris.utils.GetHead;
import net.neferett.linaris.utils.ItemBuilder;
import net.neferett.linaris.utils.NBTItem;
import net.neferett.linaris.utils.gui.GuiScreen;

public class ColorShop extends GuiScreen {

	private final HashMap<ItemStack, InstantShopItem>	itemstacks	= new HashMap<>();
	private final String								name;
	private final PlayerData							pd;

	public ColorShop(final PlayerData pd, final String name) {
		super("ColorShop", 5, Bukkit.getPlayer(pd.getPlayername()), false);
		this.build();
		this.pd = pd;
		this.name = name;
	}

	@Override
	public void drawScreen() {

		this.setItem(new ColorItems("Rouge", "§c", 3500,
				new GetHead("0738e42f-b6aa-4f22-a444-5717a63de4a4",
						"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTdjMWYxZWFkNGQ1MzFjYWE0YTViMGQ2OWVkYmNlMjlhZjc4OWEyNTUwZTVkZGJkMjM3NzViZTA1ZTJkZjJjNCJ9fX0="),
				this.pd, this.name), 1, 1);
		this.setItem(new ColorItems("Rouge foncé", "§4", 3500,
				new GetHead("27b2c930-daca-4574-a51b-df4f3332396e",
						"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2Y0NmMzMWQ2ZWU2ZWE2MTlmNzJlNzg1MjMyY2IwNDhhYjI3MDQ2MmRiMGNiMTQ1NDUxNDQzNjI1MWMxYSJ9fX0="),
				this.pd, this.name), 1, 2);
		this.setItem(new ColorItems("Or", "§6", 3500,
				new GetHead("95dfc63f-0278-4f2e-8094-db1ecf6abdc8",
						"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDNjZGIxNmFiYjE3NTFkMWE0ODFlZDg3YjU3ZGIzYjg4M2U5OTYxZGEyZjlkNDg1YTI5ODY0ODdlMiJ9fX0="),
				this.pd, this.name), 1, 3);
		this.setItem(new ColorItems("Jaune", "§e", this.pd.getRank().getVipLevel() == 2 ? 0 : 3500,
				new GetHead("969fce41-6900-4877-8529-169ea6065a8d",
						"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTRjNDE0MWMxZWRmM2Y3ZTQxMjM2YmQ2NThjNWJjN2I1YWE3YWJmN2UyYTg1MmI2NDcyNTg4MThhY2Q3MGQ4In19fQ=="),
				this.pd, this.name), 1, 4);
		this.setItem(new ColorItems("Vert", "§2", this.pd.getRank().getVipLevel() == 1 ? 0 : 1500,
				new GetHead("c8fee7ee-b067-48cb-80c8-337a63edd0b1",
						"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzZmNjlmN2I3NTM4YjQxZGMzNDM5ZjM2NThhYmJkNTlmYWNjYTM2NmYxOTBiY2YxZDZkMGEwMjZjOGY5NiJ9fX0="),
				this.pd, this.name), 1, 5);
		this.setItem(new ColorItems("Vert Clair", "§a", 1500,
				new GetHead("aa5f432b-59ee-4db5-b58e-0138280f2860",
						"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzYxZTViMzMzYzJhMzg2OGJiNmE1OGI2Njc0YTI2MzkzMjM4MTU3MzhlNzdlMDUzOTc3NDE5YWYzZjc3In19fQ=="),
				this.pd, this.name), 1, 6);
		this.setItem(new ColorItems("Cyan", "§b", this.pd.getRank().getVipLevel() == 3 ? 0 : 1500,
				new GetHead("41227d46-3350-4aea-b3e1-4c7480d891f7",
						"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDg5Y2U4OTUyNmZjMTI2MjQ2NzhmMzA1NDkzYWE2NWRhOGExYjM2MDU0NmE1MDVkMTE4ZWIxZmFkNzc1In19fQ=="),
				this.pd, this.name), 1, 7);
		this.setItem(new ColorItems("Turquoise", "§3", 1500,
				new GetHead("28408049-4255-46b8-936b-85bc87f68492",
						"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmMzNWY3MzA5OGQ1ZjViNDkyYWY4N2Q5YzU3ZmQ4ZGFhMWM4MmNmN2Y5YTdlYjljMzg0OTgxYmQ3NmRkOSJ9fX0=="),
				this.pd, this.name), 1, 8);
		this.setItem(new ColorItems("Bleu", "§1", 1500,
				new GetHead("0d2d7296-5e2e-409e-8d15-c01c8229b83d",
						"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzk2NTQwY2U3NjIxMjVlMzk4Y2E1M2Q0Y2Q5YjY2ODM5NmQwNDY3ZTEyOGIzMGRhNWFhNjJiZTljZTA2MCJ9fX0="),
				this.pd, this.name), 1, 9);
		this.setItem(new ColorItems("Bleu clair", "§9", 1500,
				new GetHead("2a83c29f-748a-4298-aa11-e5022fc62be0",
						"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2IxOWRjNGQ0Njc4ODJkYmNhMWI1YzM3NDY1ZjBjZmM3MGZmMWY4MjllY2Y0YTg2NTc5NmI4ZTVjMjgwOWEifX19"),
				this.pd, this.name), 2, 1);
		this.setItem(new ColorItems("Rose", "§d", 1500,
				new GetHead("309c9299-c87a-4b3f-9472-b5df4536b1a0",
						"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTEzM2ZhNTJkZDc0ZDcxMWU1Mzc0N2RhOTYzYjhhZGVjZjkyZGI5NDZiZTExM2I1NmMzOGIzZGMyNzBlZWIzIn19fQ=="),
				this.pd, this.name), 2, 2);
		this.setItem(new ColorItems("Magenta", "§5", 1500,
				new GetHead("6780703e-48af-4651-b0ff-e611b1fdaa2a",
						"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODU1NjU0YjNmMWJmYjJjZGYwZjRiNTJkNjM2MGEwM2QzMWRkYWZjNzEwZjhhZmFlYTk5ZmJhNjY3ZTQ4MmRmIn19fQ=="),
				this.pd, this.name), 2, 3);
		this.setItem(new ColorItems("Blanc", "§f", 1500,
				new GetHead("47160e62-9eef-4c69-ab91-1ba1fa5dc883",
						"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTVhNzcwZTdlNDRiM2ExZTZjM2I4M2E5N2ZmNjk5N2IxZjViMjY1NTBlOWQ3YWE1ZDUwMjFhMGMyYjZlZSJ9fX0="),
				this.pd, this.name), 2, 4);
		this.setItem(new ColorItems("Gris Clair", "§7", 1500,
				new GetHead("bfef85fc-9c34-48f8-8b29-51c74e8f44d1",
						"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzFjNDVhNTk1NTAxNDNhNDRlZDRlODdjZTI5NTVlNGExM2U5NGNkZmQ0YzY0ZGVlODgxZGZiNDhkZDkyZSJ9fX0="),
				this.pd, this.name), 2, 5);
		this.setItem(new ColorItems("Noir", "§0", 1500,
				new GetHead("78bc0b40-5eba-474d-81c1-b0f6b87a26c3",
						"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWRkZWJiYjA2MmY2YTM4NWE5MWNhMDVmMThmNWMwYWNiZTMzZTJkMDZlZTllNzQxNmNlZjZlZTQzZGZlMmZiIn19fQ=="),
				this.pd, this.name), 2, 6);

		this.setItem(new ColorEffectsItems("Barré", "§m", 5000, new ItemStack(Material.WOOD_AXE), this.pd, this.name), 4,
				3);

		this.setItem(new ColorEffectsItems("Itallic", "§o", 1500, new ItemStack(Material.STONE_AXE), this.pd, this.name),
				4, 4);

		this.setItem(
				new ColorEffectsItems("Sous-Ligné", "§n", 3500, new ItemStack(Material.IRON_AXE), this.pd, this.name), 4,
				5);

		this.setItem(new ColorEffectsItems("Gras", "§l", 5000, new ItemStack(Material.GOLD_AXE), this.pd, this.name), 4,
				6);

		this.setItem(new ColorEffectsItems("Magic", "§k", 6500, new ItemStack(Material.DIAMOND_AXE), this.pd, this.name),
				4, 7);
		this.setItem(new ColorEffectsItems("Défaut", "§" + this.pd.getRank().getColor(), 0, new ItemStack(Material.BARRIER), this.pd, this.name), 5, 5);

		this.setItemLine(new ItemBuilder(Material.ARROW).setTitle("§fRevenir en arrière").build(), 5, 9);
	}

	@Override
	public void onClick(final ItemStack item, final InventoryClickEvent event) {
		event.setCancelled(true);

		if (item.getType() == Material.ARROW) {
			this.getPlayer().closeInventory();
			return;
		}

		if ((event.isRightClick() || event.isLeftClick()) && this.itemstacks.containsKey(item)) {
			final InstantShopItem myitem = this.itemstacks.get(item);
			if (myitem == null) return;
			this.close();
			myitem.useOrBuy(this.pd, this);
		}
	}

	@Override
	public void onClose() {}

	@Override
	public void onOpen() {}

	public void setItem(final InstantShopItem item, final int line, final int slot) {
		final ItemStack i = item.getItemUI(this.getPlayer());
		this.itemstacks.put(i, item);
		this.setItemLine(i, line, slot);
	}

	public void setItemLine(final int id, final ItemStack item, final int line, final int slot) {
		super.setItemLine(new NBTItem(item).setInteger("itemID", id).getItem(), line, slot);
	}

}
