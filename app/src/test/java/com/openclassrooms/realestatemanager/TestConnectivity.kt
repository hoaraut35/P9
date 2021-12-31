package com.openclassrooms.realestatemanager

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.openclassrooms.realestatemanager.utils.Utils
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows
import org.robolectric.shadows.ShadowNetworkInfo

@RunWith(AndroidJUnit4::class)
class TestConnectivity {

    private var connectivityManager: ConnectivityManager? = null
    private var shadowOfActiveNetworkInfo = ShadowNetworkInfo()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        connectivityManager =
            getApplicationContext<Context>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        shadowOfActiveNetworkInfo = Shadows.shadowOf(connectivityManager!!.activeNetworkInfo)
    }

    @Test
    fun testisConnected() {
        shadowOfActiveNetworkInfo.setConnectionStatus(NetworkInfo.State.CONNECTED)
        Assert.assertTrue(Utils.isInternetAvailable(getApplicationContext()))
    }

    @Test
    fun testIfDisconnected(){
        shadowOfActiveNetworkInfo.setConnectionStatus(NetworkInfo.State.DISCONNECTED)
        Assert.assertTrue(Utils.isInternetAvailable(getApplicationContext()))
    }

}