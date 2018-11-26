package pl.dieter.dietermobileapp

import org.junit.Test

import org.junit.Assert.*
import pl.dieter.dietermobileapp.parser.DieterParser

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        var parser = DieterParser("4ok09lifteahlvt23vlmui5kcl")
        println(parser.getUserInfo()?.isLoggedIn)
        //Meal.fi
       // Meal.find()


        assertEquals(4, 2 + 2)
    }
}
