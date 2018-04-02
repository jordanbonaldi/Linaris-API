package net.neferett.linaris.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Team;

import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.api.pets.Pet;
import net.neferett.linaris.ghostplayers.GhostManager;

public class PlayerLocal extends PlayerDataAbstract {

	public static PlayerLocal getPlayer(final String name) {
		return BukkitAPI.get().getPlayerLocalManager().getPlayerLocal(name);
	}

	private double						gainedEC;
	private double						gainedLC;
	private Pet							pet;
	private Team						team		= null;
	private String						teamname;
	private final HashMap<String, Team>	teamplayer	= new HashMap<>();

	public PlayerLocal(final String player) {
		super(player);

		final PlayerData pd = BukkitAPI.get().getPlayerDataManager().getPlayerData(player);

		System.out.println(pd == null);
		System.out.println(pd.getRank() == null);

		this.teamname = pd.getRank().getTablistlvl() + this.playerID;

		if (this.teamname.length() > 15)
			this.teamname = this.teamname.substring(0, this.teamname.length() - (this.teamname.length() - 16));

		if (GhostManager.isGhost(player))
			return;
		if (BukkitAPI.get().isApi() || BukkitAPI.get().isScoreload())
			this.teamplayer.put(this.playerID,
					Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(this.teamname));
		else
			this.team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(this.playerID) == null
					? Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(this.playerID)
					: Bukkit.getScoreboardManager().getMainScoreboard().getTeam(this.playerID);

	}

	@Override
	public boolean contains(final String key) {
		return super.contains(key);
	}

	@Override
	public String get(final String key) {
		return super.get(key);
	}

	public double getGainedEC() {
		return this.gainedEC;
	}

	public double getGainedLC() {
		return this.gainedLC;
	}

	@Override
	public Set<String> getKeys() {
		return super.getKeys();
	}

	public Pet getPet() {
		return this.pet;
	}

	public Team getTeam() {
		if (BukkitAPI.get().isApi() || BukkitAPI.get().isScoreload())
			return this.teamplayer.get(this.playerID);
		return this.team;
	}

	@Override
	public Map<String, String> getValues() {
		return super.getValues();
	}

	public boolean havePet() {
		return this.pet != null;
	}

	@SuppressWarnings("deprecation")
	public void inits() {
		if (BukkitAPI.get().isApi() || BukkitAPI.get().isScoreload()) {
			if (this.teamplayer.containsKey(this.playerID))
				this.teamplayer.get(this.playerID).addPlayer(Bukkit.getOfflinePlayer(this.getPlayerID()));
			return;
		}
		if (this.team != null)
			this.team.addPlayer(Bukkit.getOfflinePlayer(this.getPlayerID()));
	}

	@Override
	public void remove(final String key) {
		this.playerData.remove(key);
	}

	@Override
	public void set(final String key, final String value) {
		this.playerData.put(key, value);
	}

	@Override
	public void setBoolean(final String key, final boolean value) {
		this.set(key, String.valueOf(value));
	}

	@Override
	public void setDouble(final String key, final double value) {
		this.set(key, String.valueOf(value));
	}

	public void setGainedEC(final double gainedEC) {
		this.gainedEC = gainedEC;
	}

	public void setGainedLC(final double gainedLC) {
		this.gainedLC = gainedLC;
	}

	@Override
	public void setInt(final String key, final int value) {
		this.set(key, String.valueOf(value));
	}

	@Override
	public void setLong(final String key, final long value) {
		this.set(key, String.valueOf(value));
	}

	public void setPet(final Pet pet) {
		this.pet = pet;
	}

	public void setPrefix(final String prefix) {
		if (BukkitAPI.get().isApi() || BukkitAPI.get().isScoreload()) {
			if (this.teamplayer.containsKey(this.playerID))
				this.teamplayer.get(this.playerID).setPrefix(prefix);
			return;
		}
		if (this.team != null)
			this.team.setPrefix(prefix);
	}

	public void setSuffix(final String suffix) {
		if (this.team != null)
			this.team.setSuffix(suffix);
	}

	public void unload() {
		if (BukkitAPI.get().isApi() || BukkitAPI.get().isScoreload()) {
			if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam(this.teamname) == null)
				return;
			Bukkit.getScoreboardManager().getMainScoreboard().getTeam(this.teamname).unregister();
			return;
		}
		if (this.team != null)
			Bukkit.getScoreboardManager().getMainScoreboard().getTeam(this.playerID).unregister();
	}

}
