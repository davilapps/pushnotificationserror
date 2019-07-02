package com.davilapps.youlocator;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.davilapps.youlocator.dummy.DummyContent;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;






import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.parse.SaveCallback;


public class MainActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener,OneFragment.OnFragmentInteractionListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private Button btn1, btn2, btn3, btn4, btn5, buttonSearch, buttonAddContact;
    public EditText alertText;
    public static boolean exampleBool = false;
    final  static ParseObject myFirstClass = new ParseObject("MyFirstClass"); // When I press panic, it creates a new record


    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private ActivityRecognitionClient mActivityRecognitionClient;
    private LocationCallback mLocationCallback;
    private Button mRequestUpdatesButton;
    private Button mRemoveUpdatesButton;
    private static TextView mLocationUpdatesResultView;



    @Override
    protected void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

        if (!checkPermissions()) {
            requestPermissions();
        } else {
        }
        updateTextField(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocationUpdatesResultView = (TextView) findViewById(R.id.alertText);

        mSettingsClient = LocationServices.getSettingsClient(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        createLocationRequest();
        buildLocationSettingsRequest();

        mActivityRecognitionClient = new ActivityRecognitionClient(this);



        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.sendAlert);
        btn5 = (Button) findViewById(R.id.button2);
        alertText = (EditText) findViewById(R.id.alertText);
        buttonSearch = (Button) findViewById(R.id.buttonSearch);

        final GPSTracker gps = new GPSTracker (getApplicationContext());
        double latitude = gps.getLatitude();
        double longitude= gps.getLongitude();
        System.out.println("MI LATITUD Y LONGITUD" +latitude+longitude);

        final ParseUser pUser= ParseUser.getCurrentUser();

        final String objId= pUser.getObjectId();

        String objectId = Preferences.getPrefs("objectId",MainActivity.this);
        System.out.println("SHARED PREFERENCES VALUEte: " + objectId);

        if(Preferences.getPrefs("objectId",MainActivity.this) == "notfound"){
            System.out.println("SHARED PREFERENCES VALUEteecholabulete: " + objectId);
            myFirstClass.put("latitude", gps.getLatitude());
            myFirstClass.put("longitude", gps.getLongitude());
            myFirstClass.put("Username", pUser.getUsername());
            myFirstClass.put("email", pUser.getEmail());
            //myFirstClass.put("alerttext", alertText.getText().toString());
            myFirstClass.saveInBackground(new SaveCallback() { // ESTO CREA UN OBJECT ID
                @Override
                public void done(ParseException e) {
                    // Now you can do whatever you want with the object ID, like save it in a variable
                    String objectId = myFirstClass.getObjectId();
                    //objectId.put("Username", objectId);
                    System.out.println("cambio de Object ID" + objectId); // OBJECT ID IS GOTTEN FROM HERE USE THIS OBJ ID FOR EACH USER
                    System.out.println("cambio de USER ID" + pUser);
                    System.out.println("cambio de ALERT ID" + objectId);
                    //ParseObject myFirstClass = new ParseObject("MyFirstClass");
                    //SAVE objectID in settings preferences shared?
                    String value;
                    Preferences.setPrefs("objectId",objectId,MainActivity.this);
                    objectId = Preferences.getPrefs("objectId",MainActivity.this);
                    System.out.println("SHARED PREFERENCES VALUE: " + objectId);

                }
            });
        }

        //sendText();



/*        // OBJECT ID IS GOTTEN FROM HERE PROBLEM IS DATA IS BEING CREATED EVERY TIME THIS CLASS TRIGGERS
        final ParseObject myFirstClass = new ParseObject("MyFirstClass"); // When I press panic, it creates a new record
        //myFirstClass.put("alerttext", alertText.getText().toString());
//        myFirstClass.put("latitude", gps.getLatitude());
//        myFirstClass.put("longitude", gps.getLongitude());
        //myFirstClass.put("Username", objId);
        myFirstClass.saveInBackground(new SaveCallback() { // VER SI ESTO CREA UN OBJECT ID
            @Override
            public void done(ParseException e) {
                // Now you can do whatever you want with the object ID, like save it in a variable
                String objectId = myFirstClass.getObjectId();
                //myFirstClass.put("Username", objectId);
                System.out.println("cambio de Object ID" + objectId); // OBJECT ID IS GOTTEN FROM HERE USE THIS OBJ ID FOR EACH USER

                //ParseObject myFirstClass = new ParseObject("MyFirstClass");

                // BELOW: JUST ADDS THE BASIC PARSE TABLE INFO, LIKE
                //myFirstClass.put("alerttext", alertText.getText().toString());
                myFirstClass.put("latitude", gps.getLatitude());
                myFirstClass.put("longitude", gps.getLongitude());
                //myFirstClass.put("ObjectId", objectId);


// WANT TO SEE IF MY EVERY USER OBJECT ID IS WORKING WHEN RETRIEVING OR UPDATING RIGHT NOW JUST RETRIEVING

                *//*alertText = (EditText) findViewById(R.id.alertText);
                ParseQuery<ParseObject> query = ParseQuery.getQuery("MyFirstClass"); // ***THIS GETS THE PARSE DATA
                query.whereEqualTo("objectId", objectId); // TEST HERE FOR EVERY OBJECT ID SAVED TO USER
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            String playerName = object.getString("alerttext");
                            //int yearOfBirth = object.getInt("yearOfBirth");
                            double latitudeParse = object.getDouble("latitude");
                            double longitudeParse = object.getDouble("longitude");
                            String objectId =  object.getString("objectId");
                            System.out.println("Parse LATITUDE: " + latitudeParse);
                            System.out.println("Parse LONGITUDE: " + longitudeParse);
                            System.out.println("Parse NAME: " + playerName);
                            System.out.println("Parse OBJID: " + objectId); // I GET OBJECT ID SUCCESFULLY

                        } else {
                            // Something is wrong
                            System.out.println("CONFIRMANDO SI ENTRO A ESTE ELSE");


                        }
                    }
                });*//*





            }
        });*/





//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //addFragment(new ItemFragment(), false, "one");
//            }
//        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // CONTACTS ARE FED HERE
                addFragment(new OneFragment(), false, "one");
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(new MapFragment(), false, "one");

            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //addFragment(new MapFragment(), false, "one");
                System.out.println("GET MY CURRENT LOCATION FIRST");

                sendData();

            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //addFragment(new MapFragment(), false, "one");
                /*alertText.getText().toString();
                ParseObject myFirstClass = new ParseObject("MyFirstClass");
                myFirstClass.put("name", alertText.getText().toString());
                System.out.println("Probando MI TEXTO" + alertText.getText().toString());*/
                sendText();

            }
        });









    }

    public void sendData() {

        final GPSTracker gps = new GPSTracker (getApplicationContext());
        double latitude = gps.getLatitude();
        double longitude= gps.getLongitude();
        System.out.println("MI LATITUD Y LONGITUD" +latitude+longitude);

        //PACK DATA IN A BUNDLE
        Bundle bundle = new Bundle();
        bundle.putDouble("NAME_KEY", latitude);
        bundle.putDouble("NAME_KEY1", longitude);

        //PASS OVER THE BUNDLE TO OUR FRAGMENT
        MapFragment myFragment = new MapFragment();
        myFragment.setArguments(bundle);

        String objectId = Preferences.getPrefs("objectId",MainActivity.this);
        System.out.println("SHARED PREFERENCES VALUE: " + objectId);


//UODATE HERE
        final ParseUser pUser= ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("MyFirstClass");
// Retrieve the object by id
        query.getInBackground(objectId, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.
                    System.out.println("UPDATE USER IS HERE. GETTING LATITUD AND LON PROBLEM");

                    object.put("latitude", gps.getLatitude());
                    object.put("longitude", gps.getLongitude());
                    object.put("Username", pUser.getUsername());
                    object.put("alerttext", alertText.getText().toString());
                    System.out.println("LAT LONGSSSS " +gps.getLatitude()+gps.getLongitude());

                    object.saveInBackground();
                } else {
                    // Failed
                    System.out.println("FAILRD");
                }
            }
        });

        sendText();

        //THEN NOW SHOW OUR FRAGMENT
        getSupportFragmentManager().beginTransaction().replace(R.id.container_frame_back,myFragment).commit();



    }

    public void sendText() {

        final GPSTracker gps = new GPSTracker (getApplicationContext());
        double latitude = gps.getLatitude();
        double longitude= gps.getLongitude();
        System.out.println("MI LATITUD Y LONGITUD" +latitude+longitude);

        final ParseUser pUser= ParseUser.getCurrentUser();

        final String objId= pUser.getObjectId();
        System.out.println("TEST objId" + objId);


        String value;
        value = Preferences.getPrefs("objectId",MainActivity.this);
        System.out.println("SHARED PREFERENCES VALUE SEND TEXT: " + value);


        ParseQuery<ParseObject> query = ParseQuery.getQuery("MyFirstClass");
// Retrieve the object by id
        query.getInBackground(value, new GetCallback<ParseObject>() { // VALUYE IS THE AUTOMATIC objectId GETTER, USE IT FROM SHARED PREFERENCES
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data. In this case, only cheatMode and score
                    // will get sent to the Parse Cloud. playerName hasn't changed.

                    object.put("alerttext", alertText.getText().toString());
                    object.put("latitude", gps.getLatitude());
                    object.put("longitude", gps.getLongitude());
                    object.put("Username", pUser.getUsername());
                    object.put("email", pUser.getEmail());
                    System.out.println("TEST TEXT SENT" + objId);
                    System.out.println("TEST TEXT SENTS" + gps.getLongitude() + gps.getLatitude() + pUser.getUsername());

                    object.saveInBackground();
                } else {
                    // Failed
                    System.out.println("TEST TEXT SENT ERROR" + objId);
                    System.out.println("TEST TEXT SENTS ERRORS" + gps.getLongitude() + gps.getLatitude() + pUser.getUsername());

                }
            }
        });



        /*// OBJECT ID IS GOTTEN FROM HERE
        final ParseObject mainObjectId = new ParseObject("MyFirstClass");
        mainObjectId.put("alerttext", alertText.getText().toString());
        mainObjectId.put("latitude", gps.getLatitude());
        mainObjectId.put("longitude", gps.getLongitude());
        //myFirstClass.put("Username", objId);
        mainObjectId.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                // Now you can do whatever you want with the object ID, like save it in a variable
                String objectId = mainObjectId.getObjectId();
                System.out.println("myFirstClass Object ID" + objectId); // OBJECT ID IS GOTTEN FROM HERE USE THIS OBJ ID FOR EACH USER
                mainObjectId.put("Username", objectId);
*/


/*                ParseQuery<ParseObject> query = ParseQuery.getQuery("MyFirstClass");
// Retrieve the object by id
                query.getInBackground(objectId, new GetCallback<ParseObject>() {
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            // Now let's update it with some new data. In this case, only cheatMode and score
                            // will get sent to the Parse Cloud. playerName hasn't changed.
                            object.put("alerttext", alertText.getText().toString());

                            object.saveInBackground();
                        } else {
                            // Failed
                        }
                    }
                });*/







//        ParseObject myFirstClass = new ParseObject("MyFirstClass");
//        myFirstClass.put("alerttext", alertText.getText().toString());
//        myFirstClass.saveInBackground();

    }

        public void addFragment(Fragment fragment, boolean addToBackStack, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.replace(R.id.container_frame_back, fragment, tag);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTextField(this);
    }

    @Override
    protected void onStop() {
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        //toast("GPS turned on");
                        System.out.println("TOAST GPS ENCENDIDO");
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            //toast("Please Provide Location Permission.");
                            return;
                        }
                        changeStatusAfterGetLastLocation("1","Manual");

                        break;
                    case Activity.RESULT_CANCELED:
                        if (!checkPermissions()) {
                            requestPermissions();
                        }
                        //toast("GPS is required to Start Tracking");
                        System.out.println("TOAST ACTIVE SU GPS");
                        break;
                }
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                Log.i(TAG, "User interaction was cancelled.");
                requestPermissions();
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                // Permission denied.
                Toast.makeText(MainActivity.this, "Session Expire..!", Toast.LENGTH_SHORT).show();
                Snackbar.make(
                        findViewById(R.id.editSearchText),
                        R.string.NOGPS,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        updateTextField(this);
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(Utils.UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(Utils.FASTEST_UPDATE_INTERVAL);

        //mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(Utils.SMALLEST_DISPLACEMENT);
        //mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        mLocationRequest.setMaxWaitTime(Utils.MAX_WAIT_TIME);
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, LocationUpdatesBroadcastReceiver.class);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.setAction(LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION);

        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startLocationPermissionRequest();
                        }
                    });

        } else {
            Log.i(TAG, "Requesting permission");
            startLocationPermissionRequest();
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }



    public void requestLocationUpdates(View view) {
        if (!checkPermissions()) {
            //toast("Please Allow Location Permission!");
            System.out.println("PLEASE ALLOW PERMISSION!");
            requestPermissions();
            return;
        }
        try {
            mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                    .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                        @Override
                        public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                            //toast("All location settings are satisfied.");

                            changeStatusAfterGetLastLocation("1","Manual");
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            int statusCode = ((ApiException) e).getStatusCode();
                            switch (statusCode) {
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                            "location settings ");
                                    try {
                                        ResolvableApiException rae = (ResolvableApiException) e;
                                        rae.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                                    } catch (IntentSender.SendIntentException sie) {
                                        Log.i(TAG, "PendingIntent unable to execute request.");
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    String errorMessage = "Location settings are inadequate, and cannot be " +
                                            "fixed here. Fix in Settings.";
                                    Log.e(TAG, errorMessage);
                                    Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                                case LocationSettingsStatusCodes.DEVELOPER_ERROR:
                                    Log.e(TAG, "DEVELOPER_ERROR");
                            }
                        }
                    });

        } catch (SecurityException e) {
            LocationRequestHelper.getInstance(getApplicationContext()).setValue("RequestingLocationUpdates",false);
            e.printStackTrace();
        }
    }

    public void removeLocationUpdates(View view) {
        changeStatusAfterGetLastLocation("0","Manual");
    }

    public static void updateTextField(Context context) {
        //mLocationUpdatesResultView.setText(LocationRequestHelper.getInstance(context).getStringValue("locationTextInApp",""));
        System.out.println("mLocationUpdatesResultView.setText" + LocationRequestHelper.getInstance(context).getStringValue("locationTextInApp",""));
    }

    @SuppressLint("MissingPermission")
    private void changeStatusAfterGetLastLocation(final String value, final String changeby) {
        if(value == "1"){
            //toast("Location Updates Started!");
            System.out.println("TRACKEANDO AHORA");

            mFusedLocationClient.requestLocationUpdates(mLocationRequest, getPendingIntent());
            LocationRequestHelper.getInstance(getApplicationContext()).setValue("RequestingLocationUpdates",true);

            Task<Void> task = mActivityRecognitionClient.requestActivityUpdates(
                    Utils.UPDATE_INTERVAL,
                    getPendingIntent());

            task.addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void result) {

                }
            });
            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i(TAG, "addOnFailureListener mActivityRecognitionClient "+e);
                }
            });

        }else if(value == "0"){

            LocationRequestHelper.getInstance(getApplicationContext()).setValue("RequestingLocationUpdates",false);
            mFusedLocationClient.removeLocationUpdates(getPendingIntent());
            Utils.removeNotification(getApplicationContext());

            //toast("Location Updates Stopped!");
            System.out.println("TRACKEO APAGADO");

            Task<Void> task = mActivityRecognitionClient.removeActivityUpdates(
                    getPendingIntent());
            task.addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void result) {
                }
            });
            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i(TAG, "removeActivityUpdates addOnFailureListener "+e);

                }
            });
        }
    }



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}




/*
* I WAS ABLE TO CONNECT TO MY DATABASE I MUST CONTINUE TO ADD LOGIN AND CREDENTIALS FOR EACH GROUP MEMBER ----- LINE 73 fucking done!
* NOW ADD THE OBJ ID TO THE QUERY WHERE OBJ ID EQUALS = USER USE THIS OBJ ID FOR ALL QUERIES AS BASE
*
*
* */