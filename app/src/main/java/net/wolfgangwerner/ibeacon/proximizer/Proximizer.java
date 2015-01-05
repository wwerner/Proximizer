package net.wolfgangwerner.ibeacon.proximizer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;

import java.util.List;


public class Proximizer extends Activity {


    public static final String RED = "r";
    public static final String GREEN = "g";
    public static final String BLUE = "b";
    private BeaconManager beaconManager;
    private Region redRegion;
    private Region greenRegion;
    private Region blueRegion;
    private int scanInterval;
    private int scanPause;

    int r;
    int g;
    int b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximizer);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        initFromPreferences();

        View contentView = findViewById(R.id.baseview);
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackground(v);
                updateValueFields(v);
            }
        });

        beaconManager = new BeaconManager(this);
        beaconManager.setBackgroundScanPeriod(scanInterval, scanPause);

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {

            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {

                for (Beacon beacon : beacons) {
                    double acc = Utils.computeAccuracy(beacon);
                    int colorValue = scaleToColorValue(acc);

                    if (region.getIdentifier().equals(RED)) {
                        r = colorValue;
                    } else if (region.getIdentifier().equals(GREEN)) {
                        g = colorValue;
                    } else if (region.getIdentifier().equals(BLUE)) {
                        b = colorValue;
                    }

                    updateBackground();
                }
            }
        });

    }

    private void initFromPreferences() {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        scanInterval = Integer.valueOf(prefs.getString(getString(R.string.pref_key_scan_interval), Defaults.SCAN_INTERVAL));
        scanPause = Integer.valueOf(prefs.getString(getString(R.string.pref_key_scan_pause), Defaults.SCAN_PAUSE));

        redRegion = new Region(RED, prefs.getString(getString(R.string.pref_key_red_uuid), Defaults.RED_UUID),
                Integer.valueOf(prefs.getString(getString(R.string.pref_key_red_major), Defaults.RED_MAJOR)),
                Integer.valueOf(prefs.getString(getString(R.string.pref_key_red_minor), Defaults.RED_MINOR)));

        greenRegion = new Region(GREEN, prefs.getString(getString(R.string.pref_key_green_uuid), Defaults.GREEN_UUID),
                Integer.valueOf(prefs.getString(getString(R.string.pref_key_green_major), Defaults.GREEN_MAJOR)),
                Integer.valueOf(prefs.getString(getString(R.string.pref_key_green_minor), Defaults.GREEN_MINOR)));

        blueRegion = new Region(BLUE, prefs.getString(getString(R.string.pref_key_blue_uuid), Defaults.BLUE_UUID),
                Integer.valueOf(prefs.getString(getString(R.string.pref_key_blue_major), Defaults.BLUE_MAJOR)),
                Integer.valueOf(prefs.getString(getString(R.string.pref_key_blue_minor), Defaults.BLUE_MINOR)));

    }

    private void updateBackground() {
        View contentView = findViewById(R.id.baseview);
        setBackground(contentView);
        updateValueFields(contentView);
    }

    private int scaleToColorValue(double acc) {
        long result = Math.round(acc * 255);
        if (result > 255)
            return 0;

        return (int) (255 - result);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initFromPreferences();
        startRanging();
    }

    @Override
    protected void onPause() {
        stopRanging();

        super.onPause();
    }

    private void stopRanging() {
        try {
            beaconManager.stopRanging(redRegion);
            beaconManager.stopRanging(greenRegion);
            beaconManager.stopRanging(blueRegion);
        } catch (RemoteException e) {
            Log.e("E", "Cannot stop ranging but it does not matter now", e);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        startRanging();
    }

    private void startRanging() {
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager.startRanging(redRegion);
                    beaconManager.startRanging(greenRegion);
                    beaconManager.startRanging(blueRegion);
                } catch (RemoteException e) {
                    Log.e("E", "Cannot start ranging", e);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        stopRanging();
        super.onStop();
    }

    public void setBackground(View v) {
        View root = v.getRootView();
        root.setBackgroundColor(Color.rgb(r, g, b));
    }

    public void updateValueFields(View v) {
        View root = v.getRootView();
        int color = ((ColorDrawable) root.getBackground()).getColor();
        TextView r = (TextView) findViewById(R.id.rvalue);
        r.setText(" " + Color.red(color));

        TextView g = (TextView) findViewById(R.id.gvalue);
        g.setText(" " + Color.green(color));

        TextView b = (TextView) findViewById(R.id.bvalue);
        b.setText(" " + Color.blue(color));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_proximizer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
        return true;
    }
}
