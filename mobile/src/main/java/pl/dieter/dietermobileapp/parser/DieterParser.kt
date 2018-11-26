package pl.dieter.dietermobileapp.parser

import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import pl.dieter.dietermobileapp.auth.data.UserInfo
import pl.dieter.dietermobileapp.db.entity.MealEntity
import pl.dieter.dietermobileapp.db.entity.RecipeEntity
import pl.dieter.dietermobileapp.db.entity.WeeklyMenuEntity
import pl.dieter.dietermobileapp.enums.CategoryEnum
import pl.dieter.dietermobileapp.parser.entity.MealInfo
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.time.DayOfWeek

public class DieterParser(private val sessionID:String){
	val gson = Gson()
	fun getUserInfo(): UserInfo? {
		val url = URL("https://dieter.pl/profil/user-info")
		val urlConnection = url.openConnection() as HttpURLConnection
		urlConnection.setRequestProperty("Cookie", "PHPSESSID=$sessionID")
		try {
			val inp = BufferedInputStream(urlConnection.getInputStream())

			val r = BufferedReader(
					InputStreamReader(inp, StandardCharsets.UTF_8))
			return gson.fromJson(r.readText(),UserInfo::class.java)
			//readStream(inp)
		} finally {
			urlConnection.disconnect()
		}

	}

	fun getMenuList(): List<WeeklyMenuEntity>{
		val doc = Jsoup.connect("https://dieter.pl/panel/moje-tygodniowe-menu")
				.cookies(mapOf("PHPSESSID" to sessionID))
				.get()
		val res = doc.getElementsByClass("list-group-item").map { element -> element.children().first { e -> e.hasAttr("title") } }.map { element ->
				val (id) = """/panel/moje-tygodniowe-menu/menu/(\d*)""".toRegex().find(element.attr("href"))!!.destructured
			WeeklyMenuEntity().apply {
				this.name=element.attr("title")
				this.id=id.toInt()
				this.meals=DayOfWeek.values().map {
					getDailyMeals(this,it)
				}.flatMap { it }
			}
		}
		return res
	}
	fun getDailyMeals(menu: WeeklyMenuEntity, dayOfWeek: DayOfWeek): List<MealEntity>{
				println(menu.id)
			val doc = Jsoup.connect("https://dieter.pl/panel/moje-tygodniowe-menu/menu/${menu.id}/${dayOfWeek.value}")
					.cookies(mapOf("PHPSESSID" to sessionID))
					.get()
			val catsElm = doc.getElementsByAttributeValueContaining("id","category_")
			val meals= catsElm.map {
				val mealEntity = MealEntity()

				mealEntity.name=it.children().first().selectFirst(".dish_set_dish_link").text()
				mealEntity.dayOfWeek=DayOfWeek.MONDAY
				mealEntity.category=CategoryEnum.values().first {catEnum -> catEnum.id == it.attr("id").replace("category_","").toInt() }

				val protein = it.select(".dish-makro").first { makro->

					makro.attr("title") == "Białko" }.text().replace("B: ","")
				val fat = it.select(".dish-makro").first {
					makro-> makro.attr("title") == "Tłuszcz"
				}.text().replace("T: ","")

				val (carbs, roughage) = """W: (.*) \(BŁ: (.*)\)""".toRegex()
					.find(it.select(".dish-makro").first { makro -> makro.attr("title") == "Węglowodany (Błonnik)" }.text())!!.destructured

				val kcal = it.select("strong").first { element -> element.text().contains("kcal") }.text().replace(" kcal","")

				val weight = it.select("strong").first { element -> element.text().contains("Łączna waga porcji") }.text().replace("Łączna waga porcji: ","").removeSuffix(".")

				val tags = it.select("a[href^=/potrawy/]").map { it.text().trim() }

				val mealInfo = MealInfo(kcal,weight,protein,fat,carbs,roughage, tags)

				mealEntity.info = org.json.JSONObject(gson.toJson(mealInfo))

				mealEntity.recipe = getRecipe(it)

				mealEntity
			}
			return meals
	}

	fun getRecipe(element: Element): RecipeEntity{
		val recipe = RecipeEntity()
		recipe.text = element.select(".col-md-4").first { it.children().firstOrNull { e->e.text().trim()=="Przepis:" } != null}.text().replace("Przepis:","").trim()

		val (ingredientsPerServing, portionsToEat) = """Składniki na liczbę porcji:\s?(\d*)\s?Liczba porcji do zjedzenia tego dnia:\s?(\d*)\s?""".toRegex()
			.find(element.select(".col-md-4").first {
				it.text().contains("Składniki na liczbę porcji:")
			}.select("strong").text())!!.destructured
		recipe.ingredientsPerServing = ingredientsPerServing.toInt()
		recipe.portionsToEat = portionsToEat.toInt()

		val ingredients = element.select(".ingredients li").map {
			mapOf("name" to it.select("a[href^=/produkt/]").map { it.text().trim() }, "info" to it.ownText().removePrefix("-").trim())
		}

		recipe.ingredients = JSONArray(ingredients)
		return recipe
	}

}