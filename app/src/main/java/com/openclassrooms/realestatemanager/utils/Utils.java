package com.openclassrooms.realestatemanager.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @param dollars
     * @return
     */
    public static int convertDollarToEuro(int dollars) {
        return (int) Math.round(dollars * 0.89);
    }

    /**
     * Conversion d'un prix d'un bien immobilier (Euros vers Dollars)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @param dollars
     * @return
     */
    public static int convertEuroToDollar(int euro) {
        return (int) Math.round(euro * 1.1288);
    }

    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @return
     */
    public static String getTodayDate() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(new Date());
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     *
     * @param context
     * @return
     */
    public static boolean isInternetAvailable(Context context) {

        //WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //return wifi.isWifiEnabled();

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        @SuppressLint("MissingPermission") NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

//    public static void checkInternet(Context context) {
//
//        RequestQueue queue = Volley.newRequestQueue(context);
//        String url = "http://www.google.com";
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Toast.makeText(context, "Intertnet is enabled", Toast.LENGTH_SHORT).show();
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(context, "Intertnet is not enabled", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        queue.add(stringRequest);
//
//    }

    //**********************************************************************************************


    public static NumberFormat getCurrencyFormat() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        currencyFormat.setMaximumFractionDigits(0);
        currencyFormat.setCurrency(Currency.getInstance(Locale.getDefault()));
        return currencyFormat;
    }

    public static Long getTodayDateToLong(){
        return System.currentTimeMillis();
    }

    public static LocalDate epochMilliToLocalDate(Long millisecondsFromEpoch) {
        Instant instant = Instant.ofEpochMilli(millisecondsFromEpoch);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return dateTime.toLocalDate();
    }


}
