package net.neferett.linaris.api.ranks;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.Getter;
import net.neferett.linaris.api.PlayerData;
import net.neferett.linaris.utils.ObjectDataBaseManagement;
import net.neferett.linaris.utils.ObjectSerializable;
import net.neferett.linaris.utils.json.JSONException;
import net.neferett.linaris.utils.json.JSONObject;

public class RankAPI extends ObjectSerializable {

	@Getter
	private final char		color;
	@Getter
	private final int		id;

	@JsonView(ObjectDataView.nonSerializable.class)
	@Getter
	private final Kit		kit;

	@Getter
	private final String	logo;
	@Getter
	private final String	logocolor;
	@Getter
	private final int		moderationLevel;
	@Getter
	private final double	multiplier;
	@Getter
	private final String	name;
	@Getter
	private final String	prefix;

	@JsonView(ObjectDataView.nonSerializable.class)
	@Getter
	private final Salary	salary;
	@Getter
	private final String	tablist;
	@Getter
	private final int		tablistlvl;
	@Getter
	private final int		vipLevel;

	public RankAPI(final int id, final String name, final String prefix, final String tablist, final int vipLevel,
			final int moderationLevel, final char color, final String logocolor, final String logo,
			final int tablistlvl, final Kit k, final double multiplier, final Salary salary) {
		super("ObjectSerializable@1");
		this.id = id;
		this.logo = logo;
		this.tablistlvl = tablistlvl;
		this.logocolor = logocolor;
		this.name = name;
		this.tablist = tablist;
		this.prefix = prefix;
		this.vipLevel = vipLevel;
		this.moderationLevel = moderationLevel;
		this.color = color;
		this.kit = k;
		this.multiplier = multiplier;
		this.salary = salary;
	}

	public RankAPI(final JSONObject o, final JSONObject kit, final JSONObject salary) throws JSONException {
		this(o.getInt("id"), o.getString("name"), o.getString("prefix"), o.getString("tablist"), o.getInt("vipLevel"),
				o.getInt("moderationLevel"), o.getString("color").charAt(0),
				o.isNull("logocolor") ? null : o.getString("logocolor"), o.isNull("logo") ? null : o.getString("logo"),
				o.getInt("tablistlvl"), kit == null ? null : new Kit(kit), o.getDouble("multiplier"),
				salary == null ? null : new Salary(salary));
	}

	private String prefixer(String pref, PlayerData pd) {
		String prefix =  pref.replace("%c", pd.contains("logocolor") ? pd.get("logocolor") : this.logocolor)
				.replace("%s", pd.contains("logo") ? pd.get("logo") : this.logo);

		if (prefix.charAt(prefix.length() - 2) == 'k')
			prefix = prefix.substring(0, prefix.length() - 3) + " ";

		return prefix;
	}

	public String getPrefix(final PlayerData pd) {
		return this.prefixer(this.prefix, pd);
	}

	public String getTablist(final PlayerData pd) {
		return this.prefixer(this.tablist, pd);
	}

	@Override
	public String serialize() {
		this.SerializedString = null;
		try {
			this.addString(new ObjectDataBaseManagement(this).serializeObject().getJson())
					.addString(this.kit != null ? this.kit.serialize() : "none")
					.addString(this.salary != null ? this.serializeSalary() : "none");
		} catch (JsonProcessingException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.SerializedString;
	}

	public String serializeSalary() throws JsonProcessingException {
		return new ObjectDataBaseManagement(this.salary).serializeObject().getJson();
	}

}
