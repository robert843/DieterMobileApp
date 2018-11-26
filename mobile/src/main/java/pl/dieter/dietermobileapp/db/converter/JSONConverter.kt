package pl.dieter.dietermobileapp.db.converter
import io.requery.Converter
import org.json.JSONException
import org.json.JSONObject


class JSONConverter : Converter<JSONObject, String> {
	override fun convertToMapped(type: Class<out JSONObject>?, value: String?): JSONObject {
		return JSONObject(value)
	}

	override fun getPersistedType(): Class<String> {
		return String::class.java
	}

	override fun convertToPersisted(value: JSONObject?): String {
		return value.toString()
	}

	override fun getMappedType(): Class<JSONObject> {
		return JSONObject::class.java
	}

	override fun getPersistedSize(): Int? {
		return null
	}


}