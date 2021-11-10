package com.openclassrooms.realestatemanager


import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    //work but not used at this time
    //val database by lazy { RealEstateDatabase.getDatabase(this) }
    //val repository by lazy { LocalDatabaseRepository(database.realEstateDao()) }

    private var textViewMain: TextView? = null
    private var textViewQuantity: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //first bug
        //this.textViewMain = findViewById(R.id.activity_second_activity_text_view_main);
        textViewMain = findViewById(R.id.activity_main_activity_text_view_main)
        textViewQuantity = findViewById(R.id.activity_main_activity_text_view_quantity)
        configureTextViewMain()
        configureTextViewQuantity()




        mainViewModel.allRealEstate.observe(this){ listRealEstate ->
            Log.i("[THOMAS]","recup : ${listRealEstate.size}" )
        }








    }

    private fun configureTextViewMain() {
        binding.activityMainActivityTextViewMain!!.textSize=15f
        binding.activityMainActivityTextViewMain!!.text="Le premier bien immobilier enregistré vaut "
        //textViewMain!!.textSize = 15f
        //textViewMain!!.text = "Le premier bien immobilier enregistré vaut "
    }

    private fun configureTextViewQuantity() {
        val quantity = Utils.convertDollarToEuro(100)
        textViewQuantity!!.textSize = 20f
        //second bug
        //this.textViewQuantity.setText(quantity);
        textViewQuantity!!.text = quantity.toString()
    }
}