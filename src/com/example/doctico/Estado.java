package com.example.doctico;

import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Estado {
	
    public boolean GpsDisponible(LocationManager manejador){
	    LocationManager manager = manejador;
	    boolean estadoGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    	return estadoGPS;
    }
    
    
	public boolean ConexionInternetDisponible(ConnectivityManager manejador){
	    ConnectivityManager connectivityManager = manejador;
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null;
	}

}
