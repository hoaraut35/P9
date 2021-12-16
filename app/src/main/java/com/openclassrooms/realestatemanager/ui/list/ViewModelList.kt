package com.openclassrooms.realestatemanager.ui.list

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.repositories.LocalDatabaseRepository
import com.openclassrooms.realestatemanager.repositories.Shared
import javax.inject.Inject

class ViewModelList @Inject constructor(private val localDatabaseRepository: LocalDatabaseRepository, private val shared: Shared) :
    ViewModel() {



}