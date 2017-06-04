package com.android.logismove;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import android.Manifest;

import android.content.pm.PackageManager;

import android.net.Uri;

import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.logismove.adapter.CampaignAdapter;
import com.android.logismove.interfaces.AsyncTaskCompleteListener;
import com.android.logismove.models.Campaign;
import com.android.logismove.models.LocationObject;
import com.android.logismove.models.LocationSend;
import com.android.logismove.utils.CommonUtils;
import com.android.logismove.utils.ShareDataHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static com.android.logismove.R.id.map;

/**
 * The only activity in this sample.
 *
 * Note: for apps running in the background on "O" devices (regardless of the targetSdkVersion),
 * location may be computed less frequently than requested when the app is not in the foreground.
 * Apps that use a foreground service -  which involves displaying a non-dismissable
 * notification -  can bypass the background location limits and request location updates as before.
 *
 * This sample uses a long-running bound and started service for location updates. The service is
 * aware of foreground status of this activity, which is the only bound client in
 * this sample. After requesting location updates, when the activity ceases to be in the foreground,
 * the service promotes itself to a foreground service and continues receiving location updates.
 * When the activity comes back to the foreground, the foreground service stops, and the
 * notification associated with that foreground service is removed.
 *
 * While the foreground service notification is displayed, the user has the option to launch the
 * activity from the notification. The user can also remove location updates directly from the
 * notification. This dismisses the notification and stops the service.
 */
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    // Used in checking for runtime permissions.
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    // The BroadcastReceiver used to listen from broadcasts from the service.
    private MyReceiver myReceiver;

    // A reference to the service used to get location updates.
    private LocationUpdatesService mService = null;

    // Tracks the bound state of the service.
    private boolean mBound = false;
    private GoogleMap mMap;
    private String mSelectedCampaignId = null;

    // UI elements.
    private Button mRequestLocationUpdatesButton;
    private Button mRemoveLocationUpdatesButton;
    private Spinner mCampaignSpinner;
    // Monitors the state of the connection to the service.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myReceiver = new MyReceiver();
        setContentView(R.layout.activity_main);
        mCampaignSpinner = (Spinner)findViewById(R.id.spinnerCampaign);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(map);
        mapFragment.getMapAsync(this);
        getCampaign();
    }

    public void getCampaign(){
        final ProgressDialog progressDialog = CommonUtils.showProgressBar(MainActivity.this, R.string.msg_loading);

        MyApplicaiton.getUserProxy().getUserCampaign(ShareDataHelper.getInstance().getUser().getId(), new AsyncTaskCompleteListener<ArrayList<Campaign>>() {
            @Override
            public void onTaskComplete(ArrayList<Campaign> result) {
                progressDialog.dismiss();
                // Check that the user hasn't revoked permissions by going to Settings.
                Campaign[] arrCampaign = result.toArray(new Campaign[result.size()]);
                final CampaignAdapter adapter = new CampaignAdapter(MainActivity.this, android.R.layout.simple_spinner_item, arrCampaign);
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       mCampaignSpinner.setAdapter(adapter);
                       mCampaignSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                           @Override
                           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                               String selected = adapter.getItemID(position);
                               if(!TextUtils.equals(mSelectedCampaignId, selected)){
                                   mSelectedCampaignId = selected;
                                   remoteDrawRoute();
                               }
                           }

                           @Override
                           public void onNothingSelected(AdapterView<?> parent) {

                           }
                       });
                       if (PreferenceUtil.requestingLocationUpdates(MainActivity.this)) {
                           if (!checkPermissions()) {
                               requestPermissions();
                           }
                       }
                   }
               });
            }

            @Override
            public void onFailure(int errorCode) {
                progressDialog.dismiss();
                mSelectedCampaignId = null;
                CommonUtils.showMessage(errorCode, MainActivity.this);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        android.preference.PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

        mRequestLocationUpdatesButton = (Button) findViewById(R.id.request_location_updates_button);
        mRemoveLocationUpdatesButton = (Button) findViewById(R.id.remove_location_updates_button);

        mRequestLocationUpdatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPermissions()) {
                    requestPermissions();
                } else {
                    refreshMap(mMap);
                    mService.requestLocationUpdates(mSelectedCampaignId);
                    mCampaignSpinner.setEnabled(false);
                }
            }
        });

        mRemoveLocationUpdatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCampaignSpinner.setEnabled(true);
                mService.removeLocationUpdates();
                CommonUtils.showMessage(R.string.msg_stop_tracking, MainActivity.this);
            }
        });
        // Restore the state of the buttons when the activity (re)launches.
        setButtonsState(PreferenceUtil.requestingLocationUpdates(this));

        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);
    }

    public void remoteDrawRoute() {
        refreshMap(mMap);
        RealmConfiguration config2 = new RealmConfiguration.Builder(MainActivity.this)
                .name("default2")
                .schemaVersion(3)
                .deleteRealmIfMigrationNeeded()
                .build();
           Realm realm = Realm.getInstance(config2);
           realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.clear(LocationObject.class);
                }});
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                new IntentFilter(LocationUpdatesService.ACTION_BROADCAST));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            unbindService(mServiceConnection);
            mBound = false;
        }
        android.preference.PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    /**
     * Returns the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        return  PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            Snackbar.make(
                    findViewById(R.id.activity_main),
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    })
                    .show();
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted.
                mService.requestLocationUpdates(mSelectedCampaignId);
            } else {
                // Permission denied.
                setButtonsState(false);
                Snackbar.make(
                        findViewById(R.id.activity_main),
                        R.string.permission_denied_explanation,
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

    /**
     * Receiver for broadcasts sent by {@link LocationUpdatesService}.
     */
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            final Location location = intent.getParcelableExtra(LocationUpdatesService.EXTRA_LOCATION);
            if (location != null) {

                Toast.makeText(MainActivity.this, PreferenceUtil.getLocationText(location),
                        Toast.LENGTH_SHORT).show();

                RealmConfiguration config2 = new RealmConfiguration.Builder(MainActivity.this)
                        .name("default2")
                        .schemaVersion(3)
                        .deleteRealmIfMigrationNeeded()
                        .build();

                Realm myRealm = Realm.getInstance(config2);
                List<LocationObject> startToPresentLocations =  myRealm.where(LocationObject.class).findAll();

             //prepare map drawing.
                List<LatLng> locationPoints = getPoints(startToPresentLocations);
                refreshMap(mMap);
                markStartingLocationOnMap(mMap, locationPoints.get(0));
                mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Current location"));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))
                        .zoom(17)
                        .bearing(90)
                        .tilt(40)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                drawRouteOnMap(mMap, locationPoints);
            }
        }
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        // Update the buttons state depending on whether location updates are being requested.
        if (s.equals(PreferenceUtil.KEY_REQUESTING_LOCATION_UPDATES)) {
            setButtonsState(sharedPreferences.getBoolean(PreferenceUtil.KEY_REQUESTING_LOCATION_UPDATES,
                    false));
        }
    }

    private void setButtonsState(boolean requestingLocationUpdates) {
        if (requestingLocationUpdates) {
            mRequestLocationUpdatesButton.setEnabled(false);
            mRemoveLocationUpdatesButton.setEnabled(true);
        } else {
            mRequestLocationUpdatesButton.setEnabled(true);
            mRemoveLocationUpdatesButton.setEnabled(false);
        }
    }

    private void refreshMap(GoogleMap mapInstance) {
        mapInstance.clear();
    }
    private void markStartingLocationOnMap(GoogleMap mapObject, LatLng location) {
        mapObject.addMarker(new MarkerOptions().position(location).title("Start location"));
    }
    private void drawRouteOnMap(GoogleMap map, List<LatLng> positions) {
        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        options.addAll(positions);
        map.addPolyline(options);
    }
    private List<LatLng> getPoints(List<LocationObject> mLocations) {
        List<LatLng> points = new ArrayList<>();
        for (LocationObject mLocation : mLocations) {
            points.add(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()));
        }
        return points;
    }
}
