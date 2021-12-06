package com.openclassrooms.realestatemanager.ui.create

import android.text.Editable

class FormUtils {

    companion object {

        fun validPriceText(text: Editable?): String? {
            if (text!!.isEmpty()) {
                return "Can't be empty"
            }
            if(text.matches(regex = "[A-Z]".toRegex())) {
                return "Must end with an lowercase letter "
            }

            if(text.matches(regex = ".*\\d.*".toRegex())) {
                return "no number allowed"
            }

            //if (!text.matches(regex = "[A-Z][a-z]+((\\s?[A-Z][a-z]+){1,2})?".toRegex())) {
            if (!text.matches(regex = "[A-Z][a-z]+((\\s?[A-Z][a-z]+){0,2})?".toRegex())) {
                return "Must start with an uppercase letter "
            }

            return null
        }


    }


}