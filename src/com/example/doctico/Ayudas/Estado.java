package com.example.doctico.Ayudas;

import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Estado {
	
    public boolean GpsDisponible(LocationManager manager){
    	return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    
    
	public boolean ConexionInternetDisponible(ConnectivityManager connectivityManager){
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null;
	}
}
