package com.davilapps.youlocator;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OneFragment.OnFragmentInteractionListener {

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    //    final double lat=this.getArguments().getDouble("NAME_KEY");
//    final double longi=this.getArguments().getDouble("NAME_KEY1");
    double lat;
    double lon;
    String detail1;
    static ArrayList<OneFragment> myFriendsSharedPrefArray = new ArrayList<OneFragment>();


    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ParseUser user = ParseUser.getCurrentUser();
        final String currentUserId = user.getCurrentUser().getUsername();
        System.out.println("EL USUARIO ID ES: " + currentUserId);

        String currentUserEmail = user.getCurrentUser().getEmail();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            lat = bundle.getDouble("NAME_KEY");
            lon = bundle.getDouble("NAME_KEY1");
            System.out.println("MI LATITUD Y LONGITUDDDD" +lat+lon+bundle);



        }

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frg);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap mMap) {

                System.out.println("THIS MAP GIVES ME MY CURRENT LOCATION");
                final GPSTracker gps = new GPSTracker (getActivity());
                double latitude = gps.getLatitude();
                double longitude= gps.getLongitude();
                System.out.println("MI LATITUD Y LONGITUD" +latitude+longitude);

                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

                //mMap.clear(); //clear old markers

                //OneFragment myEmailArray = new OneFragment();

                //System.out.println("MY FRIENDS ARE: " + myFriends);

                final ParseQuery<ParseObject> query = ParseQuery.getQuery("MyFirstClass");
                query.whereExists("email"); query.setLimit(1000);
                query.findInBackground(new FindCallback<ParseObject>() {

                    @Override
                    public void done(List<ParseObject> list,
                                     ParseException e) {
                        // TODO Auto-generated method stub
                        if (e == null)
                        {
                            if (list.size() > 0) {
                                for (int i = 0; i < list.size(); i++) {
                                    ParseObject p = list.get(i);
                                    String tagid = p.getString("email");

                                    // LOOK FOR ARRAY HERE AND DISABLE TWO LINES ABOVE

                                    final ArrayList<String> myFriends = getArrayList("email");
//                                    if(myFriends.isEmpty()){
//                                        System.out.println("Array de amigos esta vacio");
//                                        myFriends.add(ParseUser.getCurrentUser().getEmail());
//                                        //myFriends.add(String.valueOf(myFriends));
//                                        System.out.println("Array de amigos esta vacio???? " + myFriends);
//                                    }

                                    for(int ai = 0; ai < myFriends.size(); ai++)
                                    {

                                        String q = myFriends.get(ai);
                                        System.out.println("aboutToFindInSharedPrefLOOPTWICE " + myFriends);

                                        query.whereEqualTo("email", q); // QUERY PARSE USER ID IN LOCAL STORAGE SHARED PREFERENCES
                                        query.getFirstInBackground(new GetCallback<ParseObject>() {
                                            public void done(ParseObject object, ParseException e) {
                                                System.out.println("MY SHARED USERS LIST MY ONLY FRIENDS THESE GUYS REALLY ARE!" + myFriends);
                                                if (e == null) {
                                                    String playerName = object.getString("alerttext");
                                                    String username = object.getString("Username");
                                                    double latitudeParse = object.getDouble("latitude");
                                                    double longitudeParse = object.getDouble("longitude");
                                                    String emailPlayer =  object.getString("email");
                                                    System.out.println("Parse LATITUDE: " + latitudeParse);
                                                    System.out.println("Parse LONGITUDE: " + longitudeParse);
                                                    System.out.println("Parse NAME: " + playerName);
                                                    System.out.println("SALIO OBJECTO EN PRIMERO OnCreate()");

                                                    System.out.println("SUCCESS");

                                                    Marker marker = mMap.addMarker(new MarkerOptions()
                                                            .position(new LatLng(latitudeParse,longitudeParse))
                                                            .title(username)
                                                            .snippet(playerName));
                                                    marker.showInfoWindow();
                                                    //mMap.setMyLocationEnabled(true);

                                                    System.out.println("LOS DATOS QUE BUSCO: " +playerName);

                                                    CameraPosition googlePlex = CameraPosition.builder()
                                                            .target(new LatLng(latitudeParse,longitudeParse))
                                                            .zoom(15)
                                                            .bearing(0)
                                                            .tilt(0)
                                                            .build();



                                                    ParseUser user = ParseUser.getCurrentUser();
                                                    final String currentUserId = user.getCurrentUser().getUsername();
                                                    System.out.println("EL USUARIO ID ES: " + currentUserId);

                                                    System.out.println("mLocationUpdatesResultView.setText" + LocationRequestHelper.getInstance(getContext()).getStringValue("locationTextInApp",""));


                                                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10000, null);

                                                    mMap.addMarker(new MarkerOptions()
                                                            .position(new LatLng( gps.getLatitude(), gps.getLongitude())) //.position(new LatLng( gps.getLatitude(), -gps.getLongitude()))

                                                            .title(currentUserId)
                                                            .snippet(gps.getLatitude() + " " + gps.getLongitude()));
                                                    marker.showInfoWindow();

                                                } else {
                                                    // Something is wrong
                                                    System.out.println("A VER SI SALE MI OBJECT ID CON SU PUTO LATITUD NO?");


                                                }
                                            }
                                        });
                                    }



                                }
                            }
                        }
                    }
                });
            }
        });

        System.out.println("PARSE VALUES3: " + lat + lon);

        return rootView;
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public ArrayList<String> getArrayList(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);

    }
}


/* 1
I am able to get my curremnt location and display it on map with a marker.
I get my loc from my GPS Class and can now use this information
to populate the map with everyones location. DONE************

*/

/*
 * NOW I NEED TO SET UP A WAY TO STORE THE USERS LOCATION
 * IN A DATABASE LIKE SQL AND POPULATE THE MAP WITH MARKERS FROM FAMILY OR FRIENDS DONE!!!!
 *
 * FOR THIS IS HABVE TO:
 * CREATE USERS WITH DIFFERENT NAMES. FIND REFERENCES TO CREATING USERS SIGN INNTO ACCOUNT DONE!!!!!!
 *
 *
 * DEBO ACTUALIZAR EL MAP FRAGMENT CON LA ULTIMA UBICACION DE EL BOTON DE PANICO DEBO PASAR LA UBICACIION DE UNA ACTIVIDAD A OTRA DONEE!!!!
 *
 *
 * must now try with two devices and see if map fragment shows only my current location, then, only if there is a panic, then map fragment shows both markers, user and requester
 *
 * MUST GET USER TEXT NOT NULL FOR SNIPPET FUCKING DONE!
 *
 * make sure that you get coords from other users to display in the map. must get list from parse of users TO BE TESTED
 *
 *
 * CHECK TO SEE IF IN REAL DEVICE THE MARKER UPDATES CORRECTLY WHEN PANIC BUTTON IS PRESSED.
 * TWO USERS MUST BE ON DIFFERRENTE LOCATIONS AND ONE PRESS THE PANIC BUTTON TO SEE HOW THE APP WORLS SO FAR: TO BE TESTED
 *
 * IMPORTANT!!!! ADD FRIENDS LIST OR ABLE TO ADD FRIENDS SO THEY CAN SEE YOUR ACTIVITY WHEN A PANIC IS SENT IMPORTANT!!!!!
 *
 * MAKE SURE DATA IS DELETED AFTER STOP PANIC BUTTON IS PRESSED. Delete just location and messages. not username
 *
 * SAVER USER NAME AND PASSWORD FROM PARSE AND USE THAT AS CREDENTIALS WHEN LO>GGED IN FOR THE FIRST TIME. LOG OUT ONLY FROM DELETING APP.
 *
 * ADD EMAIL VERIFICATION THROUGH email
 *
 * fix opoen screen
 *
 * KEEP USER LOGGED IN
 *
 * DONDE ESTOY YO?!
 *
 *
 *
 *
 *
 * */



