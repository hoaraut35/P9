package com.openclassrooms.realestatemanager

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.openclassrooms.realestatemanager.utils.Utils
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExampleUnitTest {

    private val TODAY_DATE = "13/12/2021"
    val EURO = 82
    val DOLLARS = 100

    @Test
    fun getFormatTodayDate() {
        assertEquals(Utils.getTodayDate(), TODAY_DATE)
    }

    @Test
    fun convertDollarToEuro() {
        assertEquals(
            Utils.convertDollarToEuro(DOLLARS), EURO
        )
    }

    @Test
    fun convertEuroToDollar() {
        assertEquals(Utils.convertEuroToDollar(EURO),
            DOLLARS)
    }

}