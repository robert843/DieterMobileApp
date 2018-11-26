package pl.dieter.dietermobileapp.db.dao


import pl.dieter.dietermobileapp.db.entity.Meal

interface MealDao {

  //  @Query("SELECT * from meal")
    fun getAll(): List<Meal>

 //   @Insert(onConflict = REPLACE)
    fun insert(meals: Meal)

//    @Query("DELETE from meal")
    fun deleteAll()
}