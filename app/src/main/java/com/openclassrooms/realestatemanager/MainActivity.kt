package com.openclassrooms.realestatemanager


import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.navigateUp
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext

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

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_item_detail) as NavHostFragment
        val navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(this,navController, appBarConfiguration)

        //for test
       /* mainViewModel.allRealEstate.observe(this){ listRealEstate ->
          //  Log.i("[THOMAS]","recup : ${listRealEstate.size}" )
        }

        */

    }

    //to setup return button on fragment
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_item_detail)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

//    private fun configureTextViewMain() {
//       /* binding.activityMainActivityTextViewMain!!.textSize=15f
//        binding.activityMainActivityTextViewMain!!.text="Le premier bien immobilier enregistré vaut "
//        //textViewMain!!.textSize = 15f
//        //textViewMain!!.text = "Le premier bien immobilier enregistré vaut "
//
//        */
//    }

//    private fun configureTextViewQuantity() {
//       /* val quantity = Utils.convertDollarToEuro(100)
//        textViewQuantity!!.textSize = 20f
//        //second bug
//        //this.textViewQuantity.setText(quantity);
//        textViewQuantity!!.text = quantity.toString()
//
//        */
//    }
}