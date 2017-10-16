package net.neferett.linaris.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.neferett.linaris.PlayersHandler.Players;
import net.neferett.linaris.utils.ItemBuilder;
import net.neferett.linaris.utils.gui.GuiScreen;

public class PlayerInventory extends GuiScreen {

	private final Players p;

	public PlayerInventory(final Players p, final Players toopen) {
		super("Inventaire de " + p.getName(), 6, toopen.getPlayer(), true);
		this.p = p;
		this.build();
	}

	public String convertAmplifier(final int amp) {
		return amp == 1 ? "II" : amp == 2 ? "III" : amp == 3 ? "IV" : amp == 4 ? "V" : amp + 1 + "";
	}

	public String convertPotionDuration(final int ms) {
		final int secondes = ms % 60;
		final int minutes = ms / 60;

		return "§a" + (minutes != 0 ? minutes + "§f:" : "") + "§a" + (secondes < 10 ? "0" + secondes : secondes) + "";
	}

	@Override
	public void drawScreen() {
		final AtomicInteger slotnb = new AtomicInteger(18);
		this.placeGlasses();
		this.placeArmor();
		this.placeInfo();
		Arrays.asList(this.p.getInventory().getContents()).forEach(item -> {
			if (item != null && item.getType() != null && !item.getType().equals(Material.AIR))
				this.setItem(item, slotnb.getAndIncrement());
			else if (item == null || item.getType() == null || item.getType().equals(Material.AIR))
				slotnb.incrementAndGet();
		});
	}

	public List<String> getPotionsEffect() {
		return this.p.getActivePotionEffects().stream().map(potions -> "§f- §b"
				+ potions.getType().getName().toLowerCase().replace(potions.getType().getName().toLowerCase().charAt(0),
						potions.getType().getName().charAt(0))
				+ " §7"
				+ (potions.getAmplifier() == 0 ? "" : "§c" + this.convertAmplifier(potions.getAmplifier()) + " ")
				+ this.convertPotionDuration(potions.getDuration() / 20)).collect(Collectors.toList());
	}

	public ItemStack getRandomGlasses() {
		final ItemBuilder item = new ItemBuilder(Material.STAINED_GLASS_PANE);

		item.setDamage((short) RandomUtils.nextInt(15));
		item.setTitle("");
		return item.build();
	}

	@Override
	public void onClick(final ItemStack item, final InventoryClickEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onClose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onOpen() {}

	public void placeArmor() {
		this.setItemLine(this.p.getInventory().getArmorContents()[0], 1, 1);
		this.setItemLine(this.p.getInventory().getArmorContents()[1], 1, 2);
		this.setItemLine(this.p.getInventory().getArmorContents()[2], 1, 3);
		this.setItemLine(this.p.getInventory().getArmorContents()[3], 1, 4);
	}

	public void placeGlasses() {
		final ItemStack glass = this.getRandomGlasses();

		this.setItemLine(glass, 1, 5);
		for (int i = 0; i < 10; i++)
			this.setItemLine(glass, 2, i);
	}

	public void placeInfo() {
		this.setItemLine(new ItemBuilder(Material.EXP_BOTTLE, this.p.getLevel()).setTitle("§6Expérience")
				.addLores("§7Level" + (this.p.getLevel() > 1 ? "s" : "") + "§f: §a" + this.p.getLevel()).build(), 1, 6);

		this.setItemLine(
				new ItemBuilder(Material.POTION, this.getPotionsEffect().size()).setTitle("§6Effets de potions")
						.addLores("§7Potion" + (this.getPotionsEffect().size() > 1 ? "s actives" : " active") + "§f:")
						.addLores(this.getPotionsEffect().size() == 0 ? new ArrayList<>(Arrays.asList("§cAucune"))
								: this.getPotionsEffect())
						.build(),
				1, 7);

		this.setItemLine(new ItemBuilder(Material.COOKED_BEEF, this.p.getFoodLevel()).setTitle("§6Nourriture")
				.addLores("§7Nourriture§f:§a " + this.p.getFoodLevel()).build(), 1, 8);

		this.setItemLine(new ItemBuilder(Material.INK_SACK, (int) this.p.getHealth(), (short) 1).setTitle("§6Vie")
				.addLores("§7Vie§f:§c " + this.p.getHealth() + " ❤").build(), 1, 9);
	}

}
