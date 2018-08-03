package net.neferett.linaris.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;
import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.PlayersHandler.Players;
import net.neferett.linaris.api.PlayerData;
import net.neferett.linaris.api.ranks.Kit;
import net.neferett.linaris.api.ranks.RankAPI;
import net.neferett.linaris.api.ranks.RankManager;
import net.neferett.linaris.api.ranks.Salary;

public class RankCommand extends CommandHandler {

	static abstract class ActionRanks<T, U> {

		interface Action {
			public void doAction();
		}

		@Getter
		@Setter
		Action	a;

		@Getter
		@Setter
		U		attribute;

		@Getter
		@Setter
		Player	player;

		@Getter
		@Setter
		T		t;

		public abstract void editAttribute(Player p, List<T> arg);

		public U getWithModification() {
			if (this.a != null)
				this.a.doAction();
			return this.attribute;
		}

		public void reset() {
			this.attribute = null;
		}
	}

	public static enum RanksNeeds {

		Color("color", new ActionRanks<String, Character>() {

			@Override
			public void editAttribute(final Player p, final List<String> arg) {
				this.attribute = arg.get(0).charAt(0);
			}

		}), COLORLOGO("logocolor", new ActionRanks<String, String>() {

			@Override
			public void editAttribute(final Player p, final List<String> arg) {
				this.attribute = arg.get(0);
			}

		}), ID("id", new ActionRanks<String, Integer>() {

			@Override
			public void editAttribute(final Player p, final List<String> arg) {
				this.attribute = Integer.valueOf(arg.get(0));
			}

		}), KIT("kit", new ActionRanks<String, Kit>() {

			@Override
			public void editAttribute(final Player p, final List<String> arg) {
				if (arg.size() < 3)
					p.sendMessage("§c/rank edit kit <name> <cooldown> <price>");
				this.attribute = new Kit(arg.get(0), Long.valueOf(arg.get(1)), Integer.valueOf(arg.get(2)),
						p.getInventory().getContents());
			}
		}), LOGO("logo", new ActionRanks<String, String>() {

			@Override
			public void editAttribute(final Player p, final List<String> arg) {
				this.attribute = arg.get(0);
			}

		}), MULTIPLIER("multiplier", new ActionRanks<String, Double>() {

			@Override
			public void editAttribute(final Player p, final List<String> arg) {
				this.attribute = Double.valueOf(arg.get(0));
			}

		}), NAME("name", new ActionRanks<String, String>() {

			@Override
			public void editAttribute(final Player p, final List<String> arg) {
				this.attribute = arg.get(0);
			}

		}), PREFIX("prefix", new ActionRanks<String, String>() {

			@Override
			public void editAttribute(final Player p, final List<String> arg) {
				this.attribute = arg.get(0).replaceAll("&", "§");
			}

		}), RANKMOD("modlvl", new ActionRanks<String, Integer>() {

			@Override
			public void editAttribute(final Player p, final List<String> arg) {
				this.attribute = Integer.valueOf(arg.get(0));
			}

		}), SALARY("salary", new ActionRanks<String, Salary>() {

			@Override
			public void editAttribute(final Player p, final List<String> arg) {
				this.attribute = new Salary(Integer.valueOf(arg.get(0)), Short.valueOf(arg.get(1)));
			}

		}), TABLIST("tablist", new ActionRanks<String, String>() {

			@Override
			public void editAttribute(final Player p, final List<String> arg) {
				this.attribute = arg.get(0);
			}

		}), TABLVL("tablvl", new ActionRanks<String, Integer>() {

			@Override
			public void editAttribute(final Player p, final List<String> arg) {
				this.attribute = Integer.valueOf(arg.get(0));
			}

		}), VIPLEVEL("permlevel", new ActionRanks<String, Integer>() {

			@Override
			public void editAttribute(final Player p, final List<String> arg) {
				this.attribute = Integer.valueOf(arg.get(0));
				this.player = p;
				this.a = () -> {
					final PlayerData pd = BukkitAPI.get().getPlayerDataManager().getPlayerData("alphaTest1");
					pd.setRank(build());
					CommandHandler.getCmds().forEach(e -> {
						if (e.getPredicateOnPLayerData().test(pd))
							this.player.sendMessage("§7Commande §f: §e" + e.getCmd() + " §aAutorisée");
						else
							this.player.sendMessage("§7Commande §f: §e" + e.getCmd() + " §cRefusée");
					});
				};
			}

		});

		public static RankAPI build() {
			return new RankAPI((int) ID.getAction().getAttribute(), (String) NAME.getAction().getAttribute(),
					(String) PREFIX.getAction().getAttribute(), (String) TABLIST.getAction().getAttribute(),
					(int) VIPLEVEL.getAction().getAttribute(), (int) RANKMOD.getAction().getAttribute(),
					(Character) Color.getAction().getAttribute(), (String) COLORLOGO.getAction().getAttribute(),
					(String) LOGO.getAction().getAttribute(), (int) TABLVL.getAction().getAttribute(), null,
					(Double) MULTIPLIER.getAction().getAttribute(), (Salary) SALARY.getAction().getAttribute());
		}

		@SuppressWarnings("unchecked")
		public static void fill(final RankAPI rank) {
			ID.getAction().setAttribute(rank.getId());
			NAME.getAction().setAttribute(rank.getName());
			PREFIX.getAction().setAttribute(rank.getPrefix());
			TABLIST.getAction().setAttribute(rank.getTablist());
			VIPLEVEL.getAction().setAttribute(rank.getVipLevel());
			Color.getAction().setAttribute(rank.getColor());
			COLORLOGO.getAction().setAttribute(rank.getLogocolor());
			LOGO.getAction().setAttribute(rank.getLogo());
			TABLVL.getAction().setAttribute(rank.getTablistlvl());
			KIT.getAction().setAttribute(rank.getKit());
			MULTIPLIER.getAction().setAttribute(rank.getMultiplier());
			SALARY.getAction().setAttribute(rank.getSalary());
			RANKMOD.getAction().setAttribute(rank.getModerationLevel());
		}

		public static RanksNeeds getValueByName(final String name) {
			return Arrays.asList(RanksNeeds.values()).stream().filter(e -> e.getAttribute().equalsIgnoreCase(name))
					.findFirst().orElse(null);
		}

		@SuppressWarnings("rawtypes")
		@Getter
		private final ActionRanks	action;

		@Getter
		private final String		attribute;

		@SuppressWarnings("rawtypes")
		RanksNeeds(final String attribute, final ActionRanks action) {
			this.action = action;
			this.attribute = attribute;
		}

	}

	private RankAPI rank;

	public RankCommand() {
		super("ranks", p -> p.getRank().getModerationLevel() >= 4);
		this.setErrorMsg("§cTu ne peux pas faire cette commande !");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void cmd(final Players player, final String cmd, final List<String> args) {

		if (args.get(1).equals("edit")) {
			RanksNeeds.getValueByName(args.get(2)).getAction().editAttribute(Bukkit.getPlayer(player.getName()),
					args.subList(3, args.size()));
			player.sendMessage("Value : " + args.get(2) + " maintenant : "
					+ RanksNeeds.getValueByName(args.get(2)).getAction().getWithModification());
		}
		if (args.get(1).equals("get"))
			player.sendMessage("§e" + args.get(2) + " §7value : §e"
					+ RanksNeeds.getValueByName(args.get(2)).getAction().getWithModification());
		if (args.get(1).equalsIgnoreCase("load"))
			if (!this.load(args.get(2))) {
				player.sendMessage("§cLe rang n'existe pas !");
				RankManager.getInstance().getRanks().forEach(e -> {
					player.sendMessage("§e" + e + ",");
				});
			} else
				player.sendMessage("§aRank loaded !");
		if (args.get(1).equalsIgnoreCase("create")) {
			RankManager.getInstance().editRank(RanksNeeds.build());
			Arrays.asList(RanksNeeds.values()).forEach(e -> e.getAction().reset());
			player.sendMessage("Ranks updated");
		}
		if (args.get(1).equals("delete")) {
			if (!this.load(args.get(2))) {
				player.sendMessage("§cLe rang n'existe pas !");
				RankManager.getInstance().getRanks().forEach(e -> {
					player.sendMessage("§e" + e + ",");
				});
			}
			RankManager.getInstance().removeRank(this.rank);
			RankManager.getInstance().updateRanks();
		}
		if (args.get(1).equalsIgnoreCase("reset"))
			Arrays.asList(RanksNeeds.values()).forEach(e -> e.getAction().reset());
	}

	public boolean load(final String name) {
		final RankAPI rank = RankManager.getInstance().getRank(name);
		if (rank == null)
			return false;
		this.rank = rank;
		RanksNeeds.fill(rank);
		return true;
	}

	@Override
	public void onError(final Players p) {
		p.DisplayErrorMessage();
	}

}
