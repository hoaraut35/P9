package com.openclassrooms.realestatemanager


import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.navigateUp
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    //binding
    private lateinit var binding: ActivityMainBinding
    //viewmodel
    private val mainViewModel by viewModels<MainViewModel>()
    //navigation
    private lateinit var appBarConfiguration : AppBarConfiguration

    //private var textViewMain: TextView? = null
    //private var textViewQuantity: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //first bug
        //this.textViewMain = findViewById(R.id.activity_second_activity_text_view_main);
       // textViewMain = findViewById(R.id.activity_main_activity_text_view_main)
       // textViewQuantity = findViewById(R.id.activity_main_activity_text_view_quantity)
       // configureTextViewMain()
       // configureTextViewQuantity()


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_item) as NavHostFragment
        val navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(navController.graph)

        setupActionBarWithNavController(this,navController, appBarConfiguration)



        //for test
        mainViewModel.allRealEstate.observe(this){ listRealEstate ->
            Log.i("[THOMAS]","recup : ${listRealEstate.size}" )
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        val navControler = findNavController(R.id.nav_host_fragment_item)
        //return super.onSupportNavigateUp()
        return navControler.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    private fun configureTextViewMain() {
       /* binding.activityMainActivityTextViewMain!!.textSize=15f
        binding.activityMainActivityTextViewMain!!.text="Le premier bien immobilier enregistré vaut "
        //textViewMain!!.textSize = 15f
        //textViewMain!!.text = "Le premier bien immobilier enregistré vaut "

        */
    }

    private fun configureTextViewQuantity() {
       /* val quantity = Utils.convertDollarToEuro(100)
        textViewQuantity!!.textSize = 20f
        //second bug
        //this.textViewQuantity.setText(quantity);
        textViewQuantity!!.text = quantity.toString()

        */
    }
}