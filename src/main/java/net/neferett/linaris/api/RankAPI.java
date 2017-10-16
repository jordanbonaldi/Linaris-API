package net.neferett.linaris.api;

public class RankAPI {

	private final char		color;
	private final int		id;
	private final String	logo;
	private final String	logocolor;
	private final int		moderationLevel;
	private final String	name;
	private final String	prefix;
	private Rank			r;

	private final String	tablist;

	private final int		tablistlvl;

	private final int		vipLevel;

	public RankAPI(final int id, final String name, final String prefix, final String tablist, final int vipLevel,
			final int moderationLevel, final char color, final String logocolor, final String logo,
			final int tablistlvl) {
		this.id = id + 100;
		this.logo = logo;
		this.tablistlvl = tablistlvl;
		this.logocolor = logocolor;
		this.name = name;
		this.tablist = tablist;
		this.prefix = prefix;
		this.vipLevel = vipLevel;
		this.moderationLevel = moderationLevel;
		this.color = color;
		this.r = null;
	}

	public RankAPI(final Rank r) {
		this(r.getID(), r.getName(), r.getRealPrefix(), r.getRealPrefix(), r.getVipLevel(), r.getModerationLevel(),
				r.getColor(), r.getLogocolor(), r.getLogo(), r.getTablist());
		this.r = r;
	}

	public char getColor() {
		return this.color;
	}

	public int getId() {
		return this.id;
	}

	public int getModerationLevel() {
		return this.moderationLevel;
	}

	public String getName() {
		return this.name;
	}

	public String getPrefix(final PlayerData pd) {
		if (this.prefix.contains("%c") && this.prefix.contains("%s"))
			return this.prefix.replace("%c", pd.contains("logocolor") ? pd.get("logocolor") : this.logocolor)
					.replace("%s", pd.contains("logo") ? pd.get("logo") : this.logo);
		else if (this.prefix.contains("%c"))
			return this.prefix.replace("%c", pd.contains("logocolor") ? pd.get("logocolor") : this.logocolor);
		else
			return this.prefix;
	}

	public Rank getRank() {
		return this.r;
	}

	public String getRealPrefix() {
		return this.prefix;
	}

	public String getTablist(final PlayerData pd) {
		if (this.tablist.contains("%c") && this.tablist.contains("%s"))
			return this.tablist.replace("%c", pd.contains("logocolor") ? pd.get("logocolor") : this.logocolor)
					.replace("%s", pd.contains("logo") ? pd.get("logo") : this.logo);
		else if (this.tablist.contains("%c"))
			return this.tablist.replace("%c", pd.contains("logocolor") ? pd.get("logocolor") : this.logocolor);
		else
			return this.tablist;
	}

	public int getTablistlvl() {
		return this.tablistlvl;
	}

	public int getVipLevel() {
		return this.vipLevel;
	}

}
