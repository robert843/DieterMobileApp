package pl.dieter.dietermobileapp.db.entity


import android.os.Parcelable
import io.requery.*
import org.json.JSONArray
import org.json.JSONObject

@Entity()
interface Recipe : Parcelable, Persistable {

	@get:Key
	@get:Generated
	var id: Int

	var ingredients: JSONArray?

	@get:Key
	var text: String?

	var ingredientsPerServing: Int

	var portionsToEat: Int

	//val meal: Meal
}