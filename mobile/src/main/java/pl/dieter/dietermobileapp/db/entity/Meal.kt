package pl.dieter.dietermobileapp.db.entity

import android.os.Parcelable
import io.requery.*
import org.json.JSONObject
import pl.dieter.dietermobileapp.db.converter.JSONConverter
import pl.dieter.dietermobileapp.enums.CategoryEnum
import java.time.DayOfWeek

@Entity
@Table(name="Meal")
interface Meal : Parcelable, Persistable {

	@get:Key
	@get:Generated
	var id: Int

	@get:ManyToOne(cascade = [CascadeAction.DELETE,CascadeAction.SAVE])
	@get:ForeignKey(delete = ReferentialAction.SET_NULL)
	var weeklyMenu: WeeklyMenu

	@get:ForeignKey(update = ReferentialAction.CASCADE, delete = ReferentialAction.SET_NULL, referencedColumn = "id")
	@get:OneToOne
	var recipe: Recipe

	var category: CategoryEnum

	var dayOfWeek: DayOfWeek

	var name: String

	@get:Convert(JSONConverter::class)
	var info: JSONObject?

}