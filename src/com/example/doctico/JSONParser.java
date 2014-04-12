// Esta clase no fue programada por mi persona, fueron metodos encontrados en internet que adapte para mi proyecto

package com.example.doctico;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
 
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.util.Log;
 
public class JSONParser {
 
    static InputStream iStream = null;
    static JSONArray jarray = null;
    static String json = "";
 
    public JSONParser() {
    }
 
    public JSONArray getJSONFromUrl(String url) {
 
           StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            try {
              HttpResponse response = client.execute(httpGet);
              StatusLine statusLine = response.getStatusLine();
              int statusCode = statusLine.getStatusCode();
              if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                  builder.append(line);
                }
              } else {
                Log.e("==>", "Failed to download file");
              }
            } catch (ClientProtocolException e) {
              e.printStackTrace();
            } catch (IOException e) {
              e.printStackTrace();
            }
           
        // Parse String to JSON object
        try {
            System.out.println("44444");
            jarray = new JSONArray( builder.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        // return JSON Object
        return jarray;
    }
    
    
	public JSONObject obtenerJSON(String URL, List<NameValuePair> params){
		ServiceHandler _Serviceh = new ServiceHandler();
	    String jsonStr = _Serviceh.makeServiceCall(URL, ServiceHandler.GET,params);
 
        if (jsonStr != null) {
        	try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                 return jsonObj;
        	} 
        	catch (JSONException e) {
        		e.printStackTrace();
        	}
        } 
        else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
            return null;
        }
		return null;		
	}
	
	
	public String crear_usuario(String nombre, String email, String contraseña, String confirmar_contraseña)
	{
		List<NameValuePair> paramentros = new ArrayList<NameValuePair>();
		paramentros.add(new BasicNameValuePair("nombre", nombre));
		paramentros.add(new BasicNameValuePair("email", email));
		paramentros.add(new BasicNameValuePair("password", contraseña));
		paramentros.add(new BasicNameValuePair("password_confirmation", confirmar_contraseña));
		
	    String url_nuevo_usuario = "http://doctico.herokuapp.com/api/api_doc_tico/nuevo_usuario?";
        JSONObject jsonObj = obtenerJSON(url_nuevo_usuario, paramentros);

        try{
        	return jsonObj.get("respuesta").toString(); 

        }
        catch (JSONException e) {
    		e.printStackTrace();
    	}
		return "";
	}
    
	
	public String autenticar_usuario(String email, String password)
	{
		List<NameValuePair> paramentros = new ArrayList<NameValuePair>();
		paramentros.add(new BasicNameValuePair("email", email));
		paramentros.add(new BasicNameValuePair("password", password));
		
	    String url_autenticar_usuario = "http://doctico.herokuapp.com/api/api_doc_tico/autenticar_usuario.json";
        JSONObject jsonObj = obtenerJSON(url_autenticar_usuario, paramentros);

        try{
        	return jsonObj.get("respuesta").toString(); 
        }
        catch (JSONException e) {
    		e.printStackTrace();
    	}
		return "";
	}
	
	
	public String agregar_muestra_presion(String hora, String fecha, String sistolica, String diastolica)
	{
		List<NameValuePair> paramentros = new ArrayList<NameValuePair>();
		paramentros.add(new BasicNameValuePair("hora", hora));
		paramentros.add(new BasicNameValuePair("fecha", fecha));
		paramentros.add(new BasicNameValuePair("sistolica", sistolica));
		paramentros.add(new BasicNameValuePair("diastolica", diastolica));
		
		return "";
	}
	
	
	public String agregar_nueva_cita(String identificador, String hora, String fecha, String centro, String recordatorio)
	{
		List<NameValuePair> paramentros = new ArrayList<NameValuePair>();
		paramentros.add(new BasicNameValuePair("identificador", identificador));
		paramentros.add(new BasicNameValuePair("hora", hora));
		paramentros.add(new BasicNameValuePair("fecha", fecha));
		paramentros.add(new BasicNameValuePair("centro", centro));
		paramentros.add(new BasicNameValuePair("recordatorio", recordatorio));
		
		return "";
	}
    
}