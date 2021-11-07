package com.openclassrooms.realestatemanager

import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.os.Bundle
import com.openclassrooms.realestatemanager.R
import dagger.hilt.EntryPoint


class MainActivity : AppCompatActivity() {
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