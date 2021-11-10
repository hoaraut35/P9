package com.openclassrooms.realestatemanager.ui.master


import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.repositories.LocalDatabaseRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel>()


    val database by lazy { RealEstateDatabase.getDatabase(this) }
    val repository by lazy { LocalDatabaseRepository(database.realEstateDao()) }

    private var textViewMain: TextView? = null
    private var textViewQuantity: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //first bug
        //this.textViewMain = findViewById(R.id.activity_second_activity_text_view_main);
        textViewMain = findViewById(R.id.activity_main_activity_text_view_main)
        textViewQuantity = findViewById(R.id.activity_main_activity_text_view_quantity)
        configureTextViewMain()
        configureTextViewQuantity()


       // mainViewModel.allRealEstate.value

        //ok
        /*
        mainViewModel.allRealEstate.observe(this, Observer {
        } )

         */

        mainViewModel.allRealEstate.observe(this){ listRealEstate ->
            Log.i("[THOMAS]","recup : ${listRealEstate.size}" )
        }








    }

    private fun configureTextViewMain() {
        textViewMain!!.textSize = 15f
        textViewMain!!.text = "Le premier bien immobilier enregistré vaut "
    }

    private fun configureTextViewQuantity() {
        val quantity = Utils.convertDollarToEuro(100)
        textViewQuantity!!.textSize = 20f
        //second bug
        //this.textViewQuantity.setText(quantity);
        textViewQuantity!!.text = quantity.toString()
    }
}