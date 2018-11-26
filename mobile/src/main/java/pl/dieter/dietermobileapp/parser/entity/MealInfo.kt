package pl.dieter.dietermobileapp.parser.entity

//JSONObject("{\"kcal\":123,\"weight\":\"123g\",\"protein\":123,\"fat\":123,\"carbs\":112,\"roughage\":123,\"tags\":[\"tag1\",\"tag2\"]}")
data class MealInfo(
	val kcal: String,
	val weight: String,
	val protein: String,
	val fat: String,
	val carbs: String,
	val roughage: String,
	val tags: List<String>
)