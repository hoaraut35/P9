package com.openclassrooms.realestatemanager

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.openclassrooms.realestatemanager.utils.Utils
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestConnectivityManager {

    private lateinit var context: Context

    @Before
    fun setup(){
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun checkIfInternetIsEnabled(){
        Utils.isInternetAvailable(context)
        Assert.assertTrue(Utils.isInternetAvailable(context))
    }

}