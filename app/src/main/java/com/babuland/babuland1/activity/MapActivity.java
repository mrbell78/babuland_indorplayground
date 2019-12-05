package com.babuland.babuland1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.babuland.babuland1.utils.Gpsroutin;
import com.babuland.babuland1.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener{

    private static final String TAG = "MapActivity";
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location mlocation;
    private Gpsroutin gpsroutin;
    //SupportMapFragment mapFragment;
    private Marker oldMarker,newMarker,mirpurMarker,uttaraMarker,wariMarker;
    private CameraUpdate cameraUpdate;
    private MarkerOptions markerOptions;
    private LatLng ll;

    private GoogleMap gMap,gMapmirpur,gMapwari,gMaputtara;

    SupportMapFragment mapFragment;
    String[]permision = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION

    };

    final int RC_PERMISSION= 100;
    private ImageButton fab;
    private Button btn_mirpur,btn_uttara,btn_wari;
    private EditText editText;

    ArrayList<Marker> arrymarker = new ArrayList<>();
    ArrayList<HashMap<String,String>> arraylocation = new ArrayList<>();
    ArrayList<HashMap<String,Double>> arraySteps=new ArrayList<>();

    String url ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?" + "key=AIzaSyBgZTnamFOQng2HZSQKqxIRhmeCmyD5qAI" +"&query=";
    String url2="http://maps.googleapis.com/maps/api/directions/json?"+"key=AIzaSyBgZTnamFOQng2HZSQKqxIRhmeCmyD5qAI";

    String MODE_DRIVIES="driving";
    String urlQuery="";

    int firsttimeshow=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);



        btn_mirpur=findViewById(R.id.btn_mirpur);
        btn_uttara=findViewById(R.id.btn_uttara);
        btn_wari=findViewById(R.id.btn_wari);
        fab=findViewById(R.id.btn_location);


        btn_mirpur.setOnClickListener(this);
        btn_uttara.setOnClickListener(this);
        btn_wari.setOnClickListener(this);
        fab.setOnClickListener(this);
        mapFragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onClick(View view) {


        switch (view.getId()){
            case R.id.btn_location:
                firsttimeshow=0;
                getThisDevicelocation();
                break;
            case R.id.btn_mirpur:
                getThisDevicelocation();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?&daddr=23.8047339,90.3614061"));
                startActivity(intent);

                break;
            case R.id.btn_uttara:
                LatLng uttara = new LatLng(23.874216,90.3979105);
                getThisDevicelocation();
                Intent intentt = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?&daddr=23.8742693,90.397924"));
                startActivity(intentt);
                break;
            case R.id.btn_wari:
                LatLng wari = new LatLng(23.7195281,90.397924);
                getThisDevicelocation();
                Intent intenttt = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?&daddr=23.7195281,90.397924"));
                startActivity(intenttt);

                break;
            default:
                firsttimeshow=0;
                getThisDevicelocation();
        }
    }

    private void desirelocatoin(String s) {

        gMap.clear();
        getThisDevicelocation();
        urlQuery=url + s;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urlQuery,null,jsonObjectlistener,jsonObjectErrorlisener);
        Volley.newRequestQueue(MapActivity.this).add(jsonObjectRequest);

    }

    Response.Listener<JSONObject>jsonObjectlistener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            try {
                String lat="",lng="",name="",address="",placeid="";
                JSONArray places= response.getJSONArray("results");
                arraylocation.clear();

                for(int i=0; i<places.length();i++){
                    JSONObject info = places.getJSONObject(i);
                    name=info.getString("name");
                    address=info.getString("formatted_address");
                    placeid=info.getString("place_id");
                    JSONObject  geometri = info.getJSONObject("geometry");
                    JSONObject  location = geometri.getJSONObject("location");

                    lat=location.getString("lat");
                    lng=location.getString("lng");
                    HashMap<String,String> hm =new HashMap<>();

                    hm.put("lat",lat);
                    hm.put("lng",lng);
                    hm.put("name",name);
                    hm.put("address",address);
                    hm.put("place_id",placeid);
                    arraylocation.add(hm);

                }
                addMarkertoMap(places.length());

            }catch (JSONException e){
                e.printStackTrace();
                Log.d(TAG, "onResponse: -------------------print stack tree--------------------------------------------"+e.getMessage());

            }
        }



    };

    private void addMarkertoMap(int length) {
        markerOptions =new MarkerOptions();
        for(int i=0;i<length;i++){
            ll=new LatLng(
                    Double.valueOf(arraylocation.get(i).get("lat")),
                    Double.valueOf(arraylocation.get(i).get("lng"))
            );

            markerOptions.position(ll);
            markerOptions.title(arraylocation.get(i).get("name"));
            markerOptions.snippet(arraylocation.get(i).get("address"));
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            newMarker= gMap.addMarker(markerOptions);
            newMarker.setTag(i);

            arrymarker.add(newMarker);

            urlQuery=url2+"orgin=" + String.valueOf(mlocation.getLatitude()+",")+String.valueOf(mlocation.getLongitude())+"mode="+MODE_DRIVIES+
                    "&"+"destination=place_id:"+arraylocation.get(i).get("place_id");


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urlQuery,null,jsonObjectDirectionListener,jsonObjectErrorlisener);

            Volley.newRequestQueue(MapActivity.this).add(jsonObjectRequest);

            ll= new LatLng(mlocation.getLatitude(),mlocation.getLongitude());
            cameraUpdate=CameraUpdateFactory.newLatLngZoom(ll,12);
            gMap.animateCamera(cameraUpdate);

        }
    }


    Response.Listener<JSONObject> jsonObjectDirectionListener=new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            try {
                double startlat = 0,startlng=0,stoplat=0,stoplng=0,gDistance=0;
                arraySteps.clear();

                JSONObject routes =response.getJSONArray("routes").getJSONObject(0);
                JSONObject legs = routes.getJSONArray("legs").getJSONObject(0);
                JSONArray steps = legs.getJSONArray("steps");


                JSONObject distance  = legs.getJSONObject("distance");
                String dis = distance.getString("text");
                JSONObject duration = legs.getJSONObject("duration");
                String dur = duration.getString("text");
                editText.setText("Distance="+dis+" || Duration="+dur);


                for(int i = 0; i<steps.length();i++){
                    JSONObject step = steps.getJSONObject(i);
                    JSONObject startlocation=step.getJSONObject("start_location");
                    startlat =startlocation.getDouble("lat");
                    startlng=startlocation.getDouble("lng");
                    JSONObject endLocation = step.getJSONObject("end_location");
                    stoplat = endLocation.getDouble("lat");
                    stoplng=endLocation.getDouble("lng");

                    HashMap<String,Double> hm  = new HashMap<>();
                    hm.put("start_lat",startlat);
                    hm.put("stop_lat",stoplat);
                    hm.put("start_lng",startlng);
                    hm.put("stop_lng",stoplng);
                    arraySteps.add(hm);

                }

                gambarPolyline(arraySteps, Color.BLACK,5f);



            }catch (JSONException e){

                e.printStackTrace();
                Toast.makeText(getBaseContext(), "error is occured: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void gambarPolyline(ArrayList<HashMap<String, Double>> arraySteps, int black, float v) {

        ArrayList<LatLng>arraylatlong= new ArrayList<>();
        for(int i=0;i<arraySteps.size();i++){
            arraylatlong.add(new LatLng(arraySteps.get(i).get("start_lat"),arraySteps.get(i).get("start_lng")));
            arraylatlong.add(new LatLng(arraySteps.get(i).get("stop_lat"),arraySteps.get(i).get("stop_lng")));
        }
        PolylineOptions polylineOptions =  new PolylineOptions().clickable(true).addAll(arraylatlong).color(black).width(v);
        gMap.addPolyline(polylineOptions);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        gMap=googleMap;
        if(gMap!=null){
            getThisDevicelocation();
        }
    }


    public void getThisDevicelocation(){
        if(Build.VERSION.SDK_INT>=23){
            if(ActivityCompat.checkSelfPermission(MapActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&

                    ActivityCompat.checkSelfPermission(MapActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED
            ){
                ActivityCompat.requestPermissions(MapActivity.this,permision,RC_PERMISSION);
            }
        }

        gpsroutin=new Gpsroutin(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapActivity.this);

        fusedLocationProviderClient.requestLocationUpdates(gpsroutin.getmLocationreq(),locationCallback, Looper.myLooper());



        LatLng uttara = new LatLng(23.8742693,90.397924);
        uttaraMarker= gMaputtara.addMarker(new MarkerOptions().position(uttara));//.title("babuland mirpur")).showInfoWindow();
        uttaraMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        uttaraMarker.setTitle("Babuland Uttara");

        LatLng wari = new LatLng(23.7195281,90.397924);
        wariMarker= gMapwari.addMarker(new MarkerOptions().position(wari));//.title("babuland mirpur")).showInfoWindow();
        wariMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        wariMarker.setTitle("Babuland Wari");

        LatLng mirpur = new LatLng(23.8047339,90.3614061);
        mirpurMarker= gMapmirpur.addMarker(new MarkerOptions().position(mirpur));//.title("babuland mirpur")).showInfoWindow();
        mirpurMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mirpurMarker.setTitle("Babuland Mirpur");
        mirpurMarker.showInfoWindow();
    }

    LocationCallback locationCallback= new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            //super.onLocationResult(locationResult);

            mlocation= locationResult.getLastLocation();
            ll=new LatLng(mlocation.getLatitude(),mlocation.getLongitude());

            if(oldMarker!=null) oldMarker.remove();
            newMarker= gMap.addMarker(new MarkerOptions().position(ll));
            newMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            newMarker.setTitle("My position");
            oldMarker=newMarker;
            if(firsttimeshow==0){
                cameraUpdate= CameraUpdateFactory.newLatLngZoom(ll,16);
                gMap.animateCamera(cameraUpdate);
                firsttimeshow=1;
            }
        }
    };

    Response.ErrorListener jsonObjectErrorlisener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

            Toast.makeText(getBaseContext(), "Error: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onErrorResponse: ---------------------------------------------reason of error--------------------"+error.getMessage());
        }
    };
}