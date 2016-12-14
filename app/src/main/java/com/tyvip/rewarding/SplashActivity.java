package com.tyvip.rewarding;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.tyvip.rewarding.Utils.Constants;
import com.tyvip.rewarding.Utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private LocationManager locationManager;
    GoogleApiClient mGoogleApiClient;
    Location mylocation;
    GPSTracker tracker;
    LocationRequest mLocationRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        user_data = Util.GetStringFromReference(this, Constants.USER_DATA);
        try {
            int v = getPackageManager().getPackageInfo("com.google.android.gms", 0 ).versionCode;
            if (v < 9600000)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setMessage("Plz upgrade google play service version to 10.0.1")
                        .setTitle("Rewarding").show();
                return;
            }
            Log.d("google", "" + v);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (!Util.isOnline())
        {
            new CountDownTimer(3000, 1000) {

                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    String str = Util.GetStringFromReference(SplashActivity.this, Constants.LOGINED);
                    if (str.equals("1") && !user_data.equals(""))
                    {

                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }.start();
            return;
        }

        if (mGoogleApiClient == null) {
            // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
            // See https://g.co/AppIndexing/AndroidStudio for more information.
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(AppIndex.API).build();
        }


    }

    String user_data;
    private void PostLocation()
    {
//        String str = Util.GetStringFromReference(this, Constants.USER_DATA);

        Log.d("postlocation", "post");
        String url = Constants.GETLOCATION_URL;
        JSONObject jsonObject = new JSONObject();
        try {
            JSONObject jsonUser = new JSONObject(user_data);
            jsonObject.put("userid", jsonUser.getInt("userid"));
            jsonObject.put("userbid", jsonUser.getInt("userbid"));
            jsonObject.put("lat", Constants.user_latitude);
            jsonObject.put("lng", Constants.user_longitude);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("tag", response.toString());
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("tag", "ErrorLocation: " + error.getMessage());
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }) ;
        VipApplication.getInstance().addToRequestQueue(jsonObjReq, "location");
    }

    @Override
    protected void onStart() {
        super.onStart();
//        tracker = new GPSTracker(this);
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
    }
    void buildlocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Toast.makeText(this, "location disable", Toast.LENGTH_LONG).show();
                return;
            } else {
                mylocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//                Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//            LatLng loc = new LatLng(21.356993, 72.826647);
                if (mylocation == null) {
                    Log.d("GPS","location is again null");
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                    Log.d("GPS", "Requested for updates");
                }
                else {
//                loc = new LatLng(location.getLatitude(),location.getLongitude());
//                    Toast.makeText(this, " " + mylocation.getLatitude() + " ," + mylocation.getLongitude(), Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            tracker = new GPSTracker(this);
            mylocation = tracker.getLocation();
        }
        if (mylocation == null) {
            Constants.user_latitude = (String.format("%.4f", 0.0));
            Constants.user_longitude = (String.format("%.4f", 0.0));
        }
        else {
            final Double latitude = mylocation.getLatitude();
            final Double longitude = mylocation.getLongitude();
//        Toast.makeText(this, " " + latitude + " " + longitude, Toast.LENGTH_LONG).show();
            Constants.user_latitude = (String.format("%.4f", latitude));
            Constants.user_longitude = (String.format("%.4f", longitude));
        }
        new CountDownTimer(2000, 1000) {

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                String str = Util.GetStringFromReference(SplashActivity.this, Constants.LOGINED);
                Log.d("start", str);
                if (str.equals("1") && !user_data.equals(""))
                {
                    PostLocation();
                }
                else {
                    Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }.start();
        Log.d("location", Constants.user_latitude  + "," +  Constants.user_latitude  );

    }
    @Override
    protected void onStop() {

        super.onStop();
        if (mGoogleApiClient != null) mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mGoogleApiClient.connect();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                }
            } else {
                buildlocation();
            }
        } else {
            buildlocation();
        }
        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
//            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        } catch (Exception e) {
            Log.d("GPS", "Error in onConnected.  "+e.getMessage());

        }
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED)
        {
            buildlocation();
        }
    }
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }
}
