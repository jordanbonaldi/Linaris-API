package net.neferett.linaris.utils;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.Getter;
import net.neferett.linaris.api.ranks.ObjectDataView;
import net.neferett.linaris.utils.json.JSONException;

public abstract class ObjectSerializable {

	@Getter
	@JsonView(ObjectDataView.nonSerializable.class)
	protected String	sepString;

	@JsonView(ObjectDataView.nonSerializable.class)
	protected String	SerializedString;

	public ObjectSerializable(final String sz) {
		this.sepString = sz;
	}

	public ObjectSerializable addString(final String s) {
		this.SerializedString = this.SerializedString == null ? s : this.SerializedString + this.sepString + s;
		return this;
	}

	public abstract String serialize() throws JsonProcessingException, JSONException;

}
