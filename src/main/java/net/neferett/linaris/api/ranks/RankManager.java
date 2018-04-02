package net.neferett.linaris.api.ranks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import lombok.Getter;
import net.neferett.linaris.BukkitAPI;
import net.neferett.linaris.utils.ObjectDataBaseManagement;
import net.neferett.linaris.utils.json.JSONException;
import redis.clients.jedis.Jedis;

public class RankManager {

	public static RankManager getInstance() {
		return BukkitAPI.get().getRankManager();
	}

	@Getter
	private final List<RankAPI> ranks = new ArrayList<>();

	public RankManager() {
		this.getGeneralRanks();
	}

	public void addRank(final RankAPI r) {
		this.ranks.add(r);
	}

	public void addRanks(final RankAPI... r) {
		this.ranks.addAll(Arrays.asList(r));
	}

	private RankAPI deserializeRank(final String serializedstring) {
		try {
			return new RankAPI(
					new ObjectDataBaseManagement(serializedstring.split("ObjectSerializable@1")[0]).deserializeObject()
							.getJsonObject(),
					serializedstring.split("ObjectSerializable@1")[1].equalsIgnoreCase("none") ? null
							: new ObjectDataBaseManagement(serializedstring.split("ObjectSerializable@1")[1])
									.deserializeObject().getJsonObject(),
					serializedstring.split("ObjectSerializable@1")[2].equalsIgnoreCase("none") ? null
							: new ObjectDataBaseManagement(serializedstring.split("ObjectSerializable@1")[2])
									.deserializeObject().getJsonObject());
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void editRank(final RankAPI rank) {
		this.removeRank(rank);
		this.addRank(rank);
		this.updateRanks();
	}

	public void getGeneralRanks() {
		final Jedis j = BukkitAPI.get().getConnector().getRank();
		j.keys("ranks:*").forEach(key -> {
			this.addRank(this.deserializeRank(j.hget(key, "serializedRank")));
		});
		j.close();
	}

	public RankAPI getRank(final int id) {
		return this.ranks.stream().filter(r -> r.getId() == id).findFirst().orElse(null);
	}

	public RankAPI getRank(final String id) {
		return this.ranks.stream().filter(r -> r.getName().equalsIgnoreCase(id)).findFirst().orElse(null);
	}

	public void removeRank(final RankAPI rank) {
		final Iterator<RankAPI> i = this.ranks.iterator();
		RankAPI r;

		while (i.hasNext()) {
			r = i.next();
			if (r.getId() == rank.getId())
				i.remove();
		}
	}

	public void updateRanks() {
		final Jedis j = BukkitAPI.get().getConnector().getRank();
		j.del("ranks:*");
		this.ranks.forEach(e -> {
			System.out.println(e.serialize());
			j.hset("ranks:" + e.getId(), "serializedRank", e.serialize());
		});
		j.close();
	}

}
