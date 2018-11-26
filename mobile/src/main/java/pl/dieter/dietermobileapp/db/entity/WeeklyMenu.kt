package pl.dieter.dietermobileapp.db.entity


import android.os.Parcelable
import io.requery.*

@Entity
interface WeeklyMenu : Parcelable, Persistable {

	@get:Key
	var id: Int

	var name: String

	@get:OneToMany(mappedBy = "weeklyMenu", cascade = [CascadeAction.DELETE, CascadeAction.SAVE])
	var meals: List<Meal>

}