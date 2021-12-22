package com.openclassrooms.realestatemanager

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.openclassrooms.realestatemanager.utils.Utils
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExampleUnitTest {

    private val myDate = "20_12_2021"
    private val myEURO = 89
    private val myDOLLARS = 100

    @Test
    fun getFormatDateForToday() {
        assertEquals(Utils.getTodayDate(), myDate)
    }

    @Test
    fun convertDollarToEuro() {
        assertEquals(Utils.convertDollarToEuro(myDOLLARS), myEURO)
    }

    @Test
    fun convertEuroToDollar() {
        assertEquals(Utils.convertEuroToDollar(myEURO),
            myDOLLARS)
    }

    //TODO: check network here...


}