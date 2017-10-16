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

public class EpicubeMenuItemPrevious extends MenuItem {
	private static HashMap<Integer, EpicubeMenuItemPrevious> instances;
	private static ItemStack arrow;
	int page;

	private EpicubeMenuItemPrevious(final int page) {
		super("<=", buildArrow(), new String[0]);
		this.page = page;
		SpecialItem.registerItem(this);
	}

	public static ItemStack getItem(final Integer page) {
		if (!EpicubeMenuItemPrevious.instances.containsKey(page)) {
			EpicubeMenuItemPrevious.instances.put(page, new EpicubeMenuItemPrevious(page));
		}
		return EpicubeMenuItemPrevious.instances.get(page).getClonedItem();
	}

	private static ItemStack buildArrow() {
		if (EpicubeMenuItemPrevious.arrow != null) {
			return EpicubeMenuItemPrevious.arrow;
		}
		@SuppressWarnings("deprecation")
		final ItemStack arrow = new ItemStack(Material.BANNER, 1, DyeColor.WHITE.getDyeData());
		final BannerMeta meta = (BannerMeta) arrow.getItemMeta();
		meta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE));
		meta.addPattern(new Pattern(DyeColor.BLACK, PatternType.RHOMBUS_MIDDLE));
		meta.addPattern(new Pattern(DyeColor.WHITE, PatternType.HALF_VERTICAL_MIRROR));
		meta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE));
		meta.addPattern(new Pattern(DyeColor.WHITE, PatternType.BORDER));
		meta.addPattern(new Pattern(DyeColor.WHITE, PatternType.BORDER));
		arrow.setItemMeta(meta);
		return arrow;
	}

	@Override
	public void inventoryClickEvent(final Player player) {
		final EpicubeVirtualMenuPlayerTracker menu = EpicubeVirtualMenuPlayerTracker.get();
		if (menu != null) {
			menu.open(player, this.page);
		}
	}

	static {
		EpicubeMenuItemPrevious.instances = new HashMap<>();
	}
}
