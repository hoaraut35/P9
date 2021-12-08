package com.openclassrooms.realestatemanager.ui



import android.Manifest
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.navigateUp
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import android.widget.Toast
import androidx.annotation.RequiresApi




@AndroidEntryPoint  //Hilt annotation for activity
class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding

    private lateinit var appBarConfiguration: AppBarConfiguration

    lateinit var navController: NavController

    //TODO: do not remove for exam
    //private var textViewMain: TextView? = null
    //private var textViewQuantity: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_item_detail) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(this, navController, appBarConfiguration)



    }





    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, navController)
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