package com.example.doctico.AccesoDatos;

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
    
    private String host_api = "http://doctico.herokuapp.com/api/api_doc_tico/";
    
    private String url_nuevo_usuario           = host_api + "nuevo_usuario";
    private String url_autenticar_usuario      = host_api + "autenticar_usuario.json";
    private String url_cerrar_sesion           = host_api + "cerrar_sesion";
    private String url_agregar_muestra_presion = host_api + "nueva_presion_arterial.json";
    private String url_agregar_cita            = host_api + "nueva_cita.json";
 
    private List<NameValuePair> paramentros;
    
    
    public JSONParser() {
    	paramentros = new ArrayList<NameValuePair>();
    }
 
    // Este metodo no fue creado por mi persona, fue extraido de internet
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
           
        try {
            jarray = new JSONArray(builder.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
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
	
	
    private String ejecutarObtenerJson(String url, List<NameValuePair> paramentros){
        JSONObject jsonObj = obtenerJSON(url, paramentros);
        
        try{
        	return jsonObj.get("respuesta").toString(); 
        }
        catch (JSONException e) {
    		e.printStackTrace();
    	}
		return "";
    }
	
	
	public String crear_usuario(String nombre, String email, String contraseña, String confirmar_contraseña)
	{
		paramentros.add(new BasicNameValuePair("nombre", nombre));
		paramentros.add(new BasicNameValuePair("email", email));
		paramentros.add(new BasicNameValuePair("password", contraseña));
		paramentros.add(new BasicNameValuePair("password_confirmation", confirmar_contraseña));
		
		return ejecutarObtenerJson(url_nuevo_usuario, paramentros);
	}
    
	
	public String autenticar_usuario(String email, String password)
	{
		paramentros.add(new BasicNameValuePair("email", email));
		paramentros.add(new BasicNameValuePair("password", password));
		
		return ejecutarObtenerJson(url_autenticar_usuario, paramentros);
	}
	
	
	public String cerrar_sesion(String token)
	{
		paramentros.clear();
		paramentros.add(new BasicNameValuePair("token", token));
		
		return ejecutarObtenerJson(url_cerrar_sesion, paramentros);
	}
	
	
	public String agregar_muestra_presion(String token, String hora, String fecha, String sistolica, String diastolica)
	{
		paramentros.add(new BasicNameValuePair("token", token));
		paramentros.add(new BasicNameValuePair("hora", hora));
		paramentros.add(new BasicNameValuePair("fecha", fecha));
		paramentros.add(new BasicNameValuePair("sistolica", sistolica));
		paramentros.add(new BasicNameValuePair("diastolica", diastolica));
		
		return ejecutarObtenerJson(url_agregar_muestra_presion, paramentros);
	}
	
	
	public String agregar_nueva_cita(String token, String identificador, String hora, String fecha, String centro)
	{
		paramentros.add(new BasicNameValuePair("token", token));
		paramentros.add(new BasicNameValuePair("identificador", identificador));
		paramentros.add(new BasicNameValuePair("hora", hora));
		paramentros.add(new BasicNameValuePair("fecha", fecha));
		paramentros.add(new BasicNameValuePair("centro", centro));
		
		return ejecutarObtenerJson(url_agregar_cita, paramentros);
	}
}