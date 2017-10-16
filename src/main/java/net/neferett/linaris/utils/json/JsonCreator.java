package net.neferett.linaris.utils.json;

public class JsonCreator {

	JSONObject js;

	public JsonCreator(final String name, final int violation, final String cheat, final String serv,
			final boolean kick) {
		this.js = new JSONObject();
		this.addString("name", name);
		this.addString("cheat", cheat);
		this.addInt("violation", violation);
		this.addString("serv", serv);
		this.addBoolean("ban", kick);
	}

	private void addBoolean(final String key, final boolean value) {
		try {
			this.js.put(key, value);
		} catch (final JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addInt(final String key, final int value) {
		try {
			this.js.put(key, value);
		} catch (final JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addString(final String key, final String value) {
		try {
			this.js.put(key, value);
		} catch (final JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String build() {
		return this.js.toString();
	}

}
