package net.neferett.linaris.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import net.neferett.linaris.api.ranks.ObjectDataView;
import net.neferett.linaris.utils.json.JSONException;
import net.neferett.linaris.utils.json.JSONObject;

public class ObjectDataBaseManagement {

	@Getter
	String			json;
	@Getter
	JSONObject		jsonObject;
	ObjectMapper	mapper;
	Object			object;

	public ObjectDataBaseManagement() {
		this.mapper = new ObjectMapper();
	}

	public ObjectDataBaseManagement(final Object o) {
		this();
		this.object = o;
	}

	public ObjectDataBaseManagement(final String json) {
		this();
		this.json = json;
	}

	public ObjectDataBaseManagement deserializeObject()
			throws JsonParseException, JsonMappingException, IOException, JSONException {
		System.out.println(this.json);
		this.jsonObject = new JSONObject(this.json);
		return this;
	}

	public ObjectDataBaseManagement serializeObject() throws JsonProcessingException {
		this.json = this.mapper.writerWithView(ObjectDataView.Serializable.class).writeValueAsString(this.object);
		return this;
	}

}
