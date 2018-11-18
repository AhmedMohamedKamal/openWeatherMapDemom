package com.demo.ahmed.weather;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.demo.ahmed.weather.adapters.WeatherAdapter;
import com.demo.ahmed.weather.constants.ConstantsHolder;
import com.demo.ahmed.weather.contact.ContactUsActivity;
import com.demo.ahmed.weather.helpers.DialogHelper;
import com.demo.ahmed.weather.helpers.GPSTracker;
import com.demo.ahmed.weather.helpers.NetworkHelper;
import com.demo.ahmed.weather.helpers.PermissionHelper;
import com.demo.ahmed.weather.models.LatLng;
import com.demo.ahmed.weather.models.WeatherItem;
import com.demo.ahmed.weather.sqliteDB.DatabaseHandler;
import com.weather.ahmed.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import carbon.view.View;
import carbon.widget.TextView;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity implements android.view.View.OnClickListener {
    private ProgressBar progressBar;
    private RecyclerView recycler;
    private ArrayList<String> permissionsToRequest, permissionsRejected, permissions;
    private final static int ALL_PERMISSIONS_RESULT = 101;
    private ArrayList<WeatherItem> weatherItems;
    private JSONArray jsonObject;
    private GPSTracker locationTrack;
    private List<LatLng> locationList;
    private int locationIndex;
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiateVariables();
        initViews();
        setPermissions();
    }

    @Override
    protected void onStart() {
        super.onStart();
        weatherItems = new ArrayList<>();
        getOnlineData();
    }

    private void initiateVariables() {
        locationTrack = new GPSTracker(this);
        locationList = new ArrayList<>();
        locationIndex = 0;
        addLocations();
        databaseHandler=new DatabaseHandler(this);
    }

    private void addLocations() {
//        //London
//        locationList.add(new LatLng(51.5074, 0.1278));
//        //Paris
//        locationList.add(new LatLng(48.8566, 2.3522));
//        //NewYork
//        locationList.add(new LatLng(40.7128, 74.0060));
//        //Cairo
//        locationList.add(new LatLng(30.0444, 31.2357));
        //CurrentLocation
        locationList.add(new LatLng(locationTrack.getLatitude(), locationTrack.getLongitude()));
    }

    private void setPermissions() {
        permissionsRejected = new ArrayList<String>();
        permissions = new ArrayList<String>();
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = PermissionHelper.findUnAskedPermissions(this, permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
    }

    private void initViews() {
        recycler = findViewById(R.id.home_recycler);
        progressBar = findViewById(R.id.progress);
        findViewById(R.id.send).setOnClickListener(this);
    }

    private void getOnlineData() {
        if (NetworkHelper.isNetworkAvailable(this))
            if (NetworkHelper.isOnline()) {
                loadData();
            } else {
                showMassage(getResources().getString(R.string.offline_problem));
                getOfflineData();
            }
        else {
            showMassage(getResources().getString(R.string.connection_problem));
            getOfflineData();
        }

    }

    private void loadData() {
        if (locationIndex == locationList.size())
            return;
        getWeatherMapList(ConstantsHolder.WeatherMapURL, locationList.get(locationIndex).getLat(), locationList.get(locationIndex).getLng());
    }

    private void showMassage(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    private void getWeatherMapList(String url, double lat, double lng) {
        progressBar.setVisibility(android.view.View.VISIBLE);
        String newURL = url + "&lat=" + lat + "&lon=" + lng;
        Log.e(ConstantsHolder.LogTag, "url: " + newURL);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, newURL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Get the JSON array
                            jsonObject = response.getJSONArray("list");
                            Log.e(ConstantsHolder.LogTag, "response: " + jsonObject.toString());
                            parseWeatherMapResponse(jsonObject);
                            locationIndex++;
                            loadData();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(ConstantsHolder.LogTag, error.getLocalizedMessage());
                    }
                }
        );

        // Add JsonObjectRequest to the RequestQueue
        Volley.newRequestQueue(this).add(jsonObjectRequest);
        return;
    }

    private void parseWeatherMapResponse(JSONArray array) {
        WeatherItem weatherItem = null;
        DecimalFormat decimalFormat = new DecimalFormat("###.##");
        if (array != null)
            for (int i = 0; i < array.length(); i++) {
                try {
                    JSONObject jsonObject = array.getJSONObject(i);
                    JSONObject mainJsonObject1 = jsonObject.getJSONObject("main");
                    weatherItem = new WeatherItem(decimalFormat.format(Double.valueOf(mainJsonObject1.getString("temp_min"))),
                            decimalFormat.format(Double.valueOf(mainJsonObject1.getString("temp_max"))),
                            jsonObject.getString("name"));
                    weatherItems.add(weatherItem);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        setUpWeatherRecycler(this, weatherItems);
        progressBar.setVisibility(View.GONE);
    }

    private void setUpWeatherRecycler(Activity mContext, List<WeatherItem> weatherItems) {
        progressBar.setVisibility(android.view.View.GONE);
        recycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(layoutManager);
        WeatherAdapter adapter = new WeatherAdapter(mContext, weatherItems);
        recycler.setAdapter(adapter);
        saveData(weatherItems);
    }

    private void saveData(List<WeatherItem> weatherItems) {
        databaseHandler.deleteAll();
        for (WeatherItem item: weatherItems){
            databaseHandler.addWeatherRow(item);
        }
    }


    private void getOfflineData() {
        List<WeatherItem> weatherItems = null;
        Log.d(ConstantsHolder.LogTag + "Weather DB Count", databaseHandler.getWeatherCount() + "");
        weatherItems = databaseHandler.getAllWeather();
        databaseHandler.close();
        setUpWeatherRecycler(this,weatherItems);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!PermissionHelper.hasPermission(this, perms)) {
                        permissionsRejected.add(perms);
                    }
                }
                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            DialogHelper.showMessageOKCancel(this, getResources().getString(R.string.ask_permission),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                } else getOnlineData();

                break;
        }

    }
    private void onContactClicked(){
        startActivity(new Intent(this, ContactUsActivity.class));
    }

    @Override
    public void onClick(android.view.View v) {
        switch (v.getId()){
            case R.id.send:
                onContactClicked();
        }

    }
}
