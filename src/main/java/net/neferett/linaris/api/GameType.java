package net.neferett.linaris.api;

public enum GameType {

	SHOP(-1,"shop","ShopAPI"),
	LOBBY(0,"hub","Lobby"),
	GOODJUMP(1,"jdd","Jump Des Dieux"),
	FALLENKINGDOMS(2,"fk","FallenKingdoms"),
	PVPSWAP(3,"pvpswap","PvPSwap"),
	UHC(4,"uhc","UHC"),
	UHCRUN(5,"uhcT","UHCRun"),
	PETS(6,"pet","Pets");
	
	private int id;
	private String gameid;
	private String displayName;
	
	private GameType(int id,String gameid, String displayName) {
		this.id = id;
		this.gameid = gameid;
		this.displayName = displayName;
	}
	
	public int getID() {
		return id;
	}
	
	public String getGameName() {
		return gameid;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
    public static GameType getByName(final String name) {
    	for (GameType game : values())
    		if (game.gameid.equals(name))
    			return game;
    	return null;
    }
    
    public static GameType getByID(final int id) {
    	for (GameType game : values())
    		if (game.id == id)
    			return game;
    	return null;
    }
    
    public static GameType getByDisplayName(final String name) {
    	for (GameType game : values())
    		if (game.displayName.equals(name))
    			return game;
    	return null;
    }
	
	
}
