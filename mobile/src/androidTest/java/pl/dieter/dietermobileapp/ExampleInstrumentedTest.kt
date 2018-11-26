package pl.dieter.dietermobileapp

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.util.Log
import io.requery.EntityStore
import io.requery.Persistable
import io.requery.android.sqlite.DatabaseSource
import io.requery.meta.EntityModelBuilder
import io.requery.reactivex.KotlinReactiveEntityStore
import org.json.JSONObject

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import pl.dieter.dietermobileapp.db.entity.*
import pl.dieter.dietermobileapp.enums.CategoryEnum
import java.time.DayOfWeek
import android.R.attr.data
import com.google.gson.GsonBuilder
import io.requery.cache.EntityCacheBuilder
import io.requery.kotlin.eq
import io.requery.sql.*
import org.json.JSONArray
import org.jsoup.select.Selector.select
import pl.dieter.dietermobileapp.parser.DieterParser
import pl.dieter.dietermobileapp.parser.entity.MealInfo


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

	/*fun <E: Persistable, K> EntityDataStore<Persistable>.upsert(clazz: Class<E>, key: K, entity: E): E {
		try {
			findByKey(clazz, key) == null
		} catch (e: NoSuchElementException) {
			return insert(entity)
		}
		return update(entity)
	}
*/
	@Test
	fun dbTest() {
		val appContext = InstrumentationRegistry.getTargetContext()
		Log.d("context", appContext.packageName)

		val source = DatabaseSource(appContext, Models.DEFAULT, 1)
		source.setTableCreationMode(TableCreationMode.CREATE_NOT_EXISTS)

		val store = EntityDataStore<Persistable>(source.configuration)

		val meal = MealEntity()
		meal.category = CategoryEnum.BREAKFAST
		meal.dayOfWeek = DayOfWeek.MONDAY
		meal.name = "Makaron z brokułem (wersja bezglutenowa)"
		meal.info = JSONObject("{\"kcal\":123,\"weight\":\"123g\",\"protein\":123,\"fat\":123,\"carbs\":112,\"roughage\":123,\"tags\":[\"tag1\",\"tag2\"]}")

		val recipe = RecipeEntity()
		recipe.ingredients = JSONArray("[{\"name\":\"Chleb żytni\",\"info\":\"4 kromki (140 g)\"},{\"name\":\"Pomidory\",\"info\":\"140 g\"}]")
		recipe.portionsToEat = 1
		recipe.ingredientsPerServing = 1
		recipe.text = "testetsetsetsetse"
		meal.recipe = recipe

		val weeklyMenu = WeeklyMenuEntity()
		weeklyMenu.name = "testoweMenu"
		weeklyMenu.meals = weeklyMenu.meals + meal

		store.insert(weeklyMenu)

		println(store.findByKey(WeeklyMenuEntity::class.java, 1).name)

		assertEquals("pl.dieter.dietermobileapp", appContext.packageName)
	}

	@Test
	fun parserTest() {
		val appContext = InstrumentationRegistry.getTargetContext()
		val source = DatabaseSource(appContext, Models.DEFAULT, 1)
		source.setTableCreationMode(TableCreationMode.CREATE_NOT_EXISTS)

		val store = EntityDataStore<Persistable>(source.configuration)
		var parser = DieterParser("9en1tlhe592hccm97p9bof3b99")
		println("parserTest")
		val menus = parser.getMenuList()

		try {
			store.delete(menus)
		} catch (e: Exception) {
			// handler
		}
		store.upsert(parser.getMenuList()).forEach { menu ->
			println(menu.name)
			menu.meals.forEach { println(it.name) }
		}
	}

	@Test
	fun testJson() {
		val mealInfo = MealInfo("1","2","3","4","5","6", emptyList<String>())
		val gson = GsonBuilder().setPrettyPrinting().create()
		val jsonPerson = gson.toJsonTree(mealInfo).asJsonObject
		println(jsonPerson.toString())
	}
}
