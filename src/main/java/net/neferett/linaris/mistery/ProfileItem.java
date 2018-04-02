package net.neferett.linaris.mistery;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.api.PlayerData;
import net.neferett.linaris.api.ranks.RankAPI;
import net.neferett.linaris.specialitems.MenuItem;
import net.neferett.linaris.utils.ItemStackUtils;
import net.neferett.linaris.utils.TimeUtils;

public class ProfileItem extends MenuItem {

	public static String[] getLore(final Player p) {
		final List<String> lore = new ArrayList<>();

		final PlayerData pd = BukkitAPI.get().getPlayerDataManager().getPlayerData(p.getName());
		final RankAPI rank = pd.getRank();

		if (pd.contains("rankFinish"))
			lore.add("§7Grade: §" + rank.getColor() + rank.getName() + " " + TimeUtils.minutesToDayHoursMinutes(
					(int) (Math.abs(pd.getRankFinish() - System.currentTimeMillis()) / 1000 / 60)));
		else
			lore.add("§7Grade: §" + rank.getColor() + rank.getName());
		lore.add("§7Coins: §e" + String.format("%.2f", pd.getEC()));
		lore.add("§7Credtis: §b" + String.format("%.2f", pd.getLC()));
		if (pd.contains("booster"))
			lore.add("§7Booster: " + TimeUtils.minutesToDayHoursMinutes(
					(int) (Math.abs(pd.getBoosterFinish() - System.currentTimeMillis()) / 1000 / 60)));
		else
			lore.add("§7Booster: §cD§sactiv§");
		lore.add("§7Gains de Coins: §e" + (100 + pd.getRank().getMultiplier() * 100) + "%");

		return lore.toArray(new String[lore.size()]);
	}

	public ProfileItem(final Player p) {
		super("§6§n" + p.getName(), ItemStackUtils.getPlayerHead(p.getName()), getLore(p));

	}

	@Override
	public void inventoryClickEvent(final Player player) {
		// TODO Auto-generated method stub

	}

}
