package net.neferett.linaris.api;

public enum Rank {
	ADMIN(9, "Admin", "§c[Admin] ", 4, 4, 'c', null, null, -2),
	AMI(5, "Ami", "§fAmi %c%s§f ", 4, 0, 'f', "§b", "✪", -7),
	FONDATEUR(12, "Fondateur", "§c[Fondateur] ", 4, 5, 'c', null, null, -1),
	HELPEUR(6, "Helpeur", "§3[Helpeur] ", 3, 1, '1', null, null, -5),
	Joueur(0, "Joueur", "§7", 0, 0, '7', null, null, 0),
	MINIVIP(1, "MiniVIP", "§f[MiniVIP] ", 1, 0, 'f', null, null, -11),
	MOD(7, "Modo", "§6[Modérateur] ", 4, 2, '6', null, null, -4),
	RESP(8, "Responsable", "§6[Resp.Modo] ", 4, 3, '6', null, null, -3),
	SUPERVIP(4, "Héros", "§aHéros %c%s§a ", 4, 0, 'a', "§e", "✪", -8),
	VIP(2, "VIP", "§f[VIP] ", 2, 0, 'f', null, null, -10),
	VIPPLUS(3, "VIP+", "§b[VIP%c+§b] ", 3, 0, 'b', "§b", null, -9),
	YT(10, "YT", "§fY§cT %c%s§f ", 4, 0, 'f', "§c", "❤", -6);

	public static Rank get(final int i) {
		for (final Rank rank : values())
			if (rank.id == i)
				return rank;
		return null;
	}

	public static Rank get(final String name) {
		for (final Rank rank : values())
			if (rank.getName().equals(name))
				return rank;
		return null;
	}

	private char	color;
	private int		id;
	private String	logo;
	private String	logocolor;
	private int		moderationLevel;
	private String	name;

	private String	prefix;

	private int		tablist;

	private int		vipLevel;

	private Rank(final int id, final String name, final String prefix, final int vipLevel, final int moderationLevel,
			final char color, final String logocolor, final String logo, final int tablistlevel) {
		this.id = id;
		this.logo = logo;
		this.logocolor = logocolor;
		this.name = name;
		this.prefix = prefix;
		this.vipLevel = vipLevel;
		this.moderationLevel = moderationLevel;
		this.color = color;
		this.tablist = tablistlevel;
	}

	public char getColor() {
		return this.color;
	}

	public double getECMultiplier() {
		if (this.vipLevel == 0)
			return 0;
		else if (this.vipLevel == 1)
			return 2;
		else if (this.vipLevel == 2)
			return 3;
		else if (this.vipLevel == 3)
			return 4;
		else if (this.vipLevel == 4)
			return 4;
		else
			return 0;
	}

	public int getID() {
		return this.id;
	}

	public double getLCMultiplier() {
		if (this.vipLevel == 0)
			return 0;
		else if (this.vipLevel == 1)
			return 0;
		else if (this.vipLevel == 2)
			return 0;
		else if (this.vipLevel == 3)
			return 0;
		else if (this.vipLevel == 4)
			return 2;
		else
			return 0;
	}

	public String getLogo() {
		return this.logo;
	}

	public String getLogocolor() {
		return this.logocolor;
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

	public String getRealPrefix() {
		return this.prefix;
	}

	public int getTablist() {
		return this.tablist;
	}

	public int getVipLevel() {
		return this.vipLevel;
	}
}
