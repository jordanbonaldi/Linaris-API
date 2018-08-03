package net.neferett.linaris.api.ranks;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.Getter;
import net.neferett.linaris.utils.ObjectSerializable;
import net.neferett.linaris.utils.json.JSONException;
import net.neferett.linaris.utils.json.JSONObject;

public class Salary extends ObjectSerializable {

	@Getter
	short	salary;
	@Getter
	int		time;

	public Salary(final int time, final short salary) {
		super("ObjectSerializable@3");
		this.time = time;
		this.salary = salary;
	}

	public Salary(final JSONObject o) throws JSONException {
		this(o.getInt("time"), (short) o.getInt("salary"));
	}

	@Override
	public String serialize() throws JsonProcessingException, JSONException {
		this.SerializedString = null;
		this.addString(new JSONObject().put("salary", this.salary).put("time", this.time).toString());
		return this.SerializedString;
	}

}
