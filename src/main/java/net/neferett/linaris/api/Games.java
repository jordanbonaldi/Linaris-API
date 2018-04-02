package net.neferett.linaris.api;

public enum Games {

	ARROW(16, "aw", "Arrow"),
	BuildBattle(24, "bb", "BuildBattle"),
	Cheat(18, "cheat", "PvPCheat"),
	COC(29, "coc", "ClashOfClans"),
	COCV(30, "cocv", "COCVILLAGE"),
	COD(15, "cod", "Call Of Duty"),
	Fac(17, "magie", "Magie"),
	FacMagie(50, "faction", "Faction"),
	FALLENKINGDOMS(2, "fk", "FallenKingdoms"),
	FB(20, "fb", "FreeBuild"),
	GLADIATOR(10, "glad", "Gladiator"),
	GOODJUMP(1, "jdd", "Jump Des Dieux"),
	GTA(22, "gta", "GTA"),
	HG(14, "hg", "HungerGames"),
	HikaBrain(33, "hb", "HikaBrain"),
	LOBBY(0, "hub", "Lobby"),
	LOGIN(35, "l", "Login"),
	PETS(6, "pet", "Pets"),
	PRACTICE(31, "practice", "Practice"),
	Prison(19, "prison", "Prison"),
	PvPBox(25, "pb", "PvPBox"),
	PvPBoxCheat(26, "pbc", "PvPBoxCheat"),
	PVPSWAP(3, "pvpswap", "PvPSwap"),
	PvPTraining(27, "pt", "PvPTraining"),
	RUSH(8, "rush", "Rush"),
	SHEEPWARS(12, "sheep", "SheepWars"),
	SHOP(-1, "shop", "ShopAPI"),
	SKYBLOCK(21, "sk", "SkyBlock"),
	SkyPvP(34, "sky", "SkyPvP"),
	SKYWARS(7, "sw", "SkyWars"),
	SpeedUHC(28, "speed", "SpeedUHC"),
	SURVIVOR(11, "sv", "Survivor"),
	TOTEM(13, "tt", "Totem"),
	TOWERS(9, "tw", "Towers"),
	TWD(23, "twd", "TheWalkingDead"),
	UHC(4, "uhc", "UHC"),
	UHCRUN(5, "uhcrun", "UHCRun"),
	UHCRUNSolo(32, "uhcrunsolo", "UHCRunSolo");

	public static Games getByDisplayName(final String name) {
		for (final Games game : values())
			if (game.displayName.equals(name))
				return game;
		return null;
	}

	public static Games getByID(final int id) {
		for (final Games game : values())
			if (game.id == id)
				return game;
		return null;
	}

	public static Games getByName(final String name) {
		for (final Games game : values())
			if (game.gameid.equals(name))
				return game;
		return null;
	}

	public static String getIDByDisplayName(final String name) {
		for (final Games game : values())
			if (game.displayName.equals(name))
				return game.getGameName();
		return null;
	}

	private String	displayName;

	private String	gameid;

	private int		id;

	private Games(final int id, final String gameid, final String displayName) {
		this.id = id;
		this.gameid = gameid;
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public String getGameName() {
		return this.gameid;
	}

	public int getID() {
		return this.id;
	}

}
