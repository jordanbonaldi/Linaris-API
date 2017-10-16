package net.neferett.linaris.spectator;

import java.util.HashMap;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import net.neferett.linaris.specialitems.MenuItem;
import net.neferett.linaris.specialitems.SpecialItem;

public class EpicubeMenuItemNext extends MenuItem {
	private static ItemStack								arrow;
	private static HashMap<Integer, EpicubeMenuItemNext>	instances;
	static {
		EpicubeMenuItemNext.instances = new HashMap<>();
	}

	private static ItemStack buildArrow() {
		if (EpicubeMenuItemNext.arrow != null)
			return EpicubeMenuItemNext.arrow;
		@SuppressWarnings("deprecation")
		final ItemStack arrow = new ItemStack(Material.BANNER, 1, DyeColor.WHITE.getDyeData());
		final BannerMeta meta = (BannerMeta) arrow.getItemMeta();
		meta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE));
		meta.addPattern(new Pattern(DyeColor.BLACK, PatternType.RHOMBUS_MIDDLE));
		meta.addPattern(new Pattern(DyeColor.WHITE, PatternType.HALF_VERTICAL));
		meta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE));
		meta.addPattern(new Pattern(DyeColor.WHITE, PatternType.BORDER));
		meta.addPattern(new Pattern(DyeColor.WHITE, PatternType.BORDER));
		arrow.setItemMeta(meta);
		return arrow;
	}

	public static ItemStack getItem(final Integer page) {
		if (!EpicubeMenuItemNext.instances.containsKey(page))
			EpicubeMenuItemNext.instances.put(page, new EpicubeMenuItemNext(page));
		return EpicubeMenuItemNext.instances.get(page).getClonedItem();
	}

	int page;

	private EpicubeMenuItemNext(final int page) {
		super("=>", buildArrow(), new String[0]);
		this.page = page;
		SpecialItem.registerItem(this);
	}

	@Override
	public void inventoryClickEvent(final Player player) {
		final EpicubeVirtualMenuPlayerTracker menu = EpicubeVirtualMenuPlayerTracker.get();
		if (menu != null)
			menu.open(player, this.page);
	}
}
