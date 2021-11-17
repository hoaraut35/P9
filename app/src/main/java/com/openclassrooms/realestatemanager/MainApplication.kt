package com.openclassrooms.realestatemanager

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp //Hilt annotation for base class application
class MainApplication : Application()