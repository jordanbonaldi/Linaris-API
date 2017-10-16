package net.neferett.linaris.db.redis;

import com.google.gson.Gson;

public abstract class RedisEntity {

	private String id;

	public RedisEntity fromJson(final String json) {
		final Gson gson = new Gson();
		final RedisEntity entity = gson.fromJson(json, this.getClass());
		return entity;
	}

	public String getId() {
		return this.id;
	}

	public boolean haveId() {
		return this.id != null && !this.id.isEmpty();
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String toJson() {
		final Gson gson = new Gson();
		return gson.toJson(this);
	}

}
