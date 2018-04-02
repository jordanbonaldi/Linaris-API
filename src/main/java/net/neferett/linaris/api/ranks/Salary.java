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
	short	time;

	public Salary(final JSONObject o) throws JSONException {
		this((short) o.getInt("salary"), (short) o.getInt("time"));
	}

	public Salary(final short time, final short salary) {
		super("ObjectSerializable@3");
		this.time = time;
		this.salary = salary;
	}

	@Override
	public String serialize() throws JsonProcessingException, JSONException {
		this.SerializedString = null;
		this.addString(new JSONObject().put("salary", this.salary).put("time", this.time).toString());
		return this.SerializedString;
	}

}
