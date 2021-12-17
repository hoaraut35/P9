package com.openclassrooms.realestatemanager.ui.list

import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.repositories.LocalDatabaseRepository
import com.openclassrooms.realestatemanager.repositories.SharedRepository
import javax.inject.Inject

class ListViewModel @Inject constructor(private val localDatabaseRepository: LocalDatabaseRepository, private val sharedRepository: SharedRepository) :
    ViewModel() {



}