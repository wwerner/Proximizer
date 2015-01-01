package net.wolfgangwerner.ibeacon.proximizer;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.RemoteException;
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
import java.util.Random;


public class Proximizer extends Activity {


    private BeaconManager beaconManager;

    int r;
    int g;
    int b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximizer);

        View contentView = findViewById(R.id.baseview);
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackground(v);
                updateValueFields(v);
            }
        });

        beaconManager = new BeaconManager(this);
        beaconManager.setBackgroundScanPeriod(5,5);

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {

            @Override public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {

                for (Beacon beacon : beacons) {
                    double acc = Utils.computeAccuracy(beacon);
                    int colorValue = scaleToColorValue(acc);

                    if (region.getIdentifier().equals(Beacons.RED)) {
                        r = colorValue;
                    } else if (region.getIdentifier().equals(Beacons.GREEN)) {
                        g =colorValue;
                    } else if (region.getIdentifier().equals(Beacons.BLUE)) {
                        b = colorValue;
                    }

                    updateBackground();
                }
            }
        });

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
        startRanging();
    }

    @Override
    protected void onPause() {
        stopRanging();

        super.onPause();
    }

    private void stopRanging() {
        try {
            beaconManager.stopRanging(Beacons.REGION_ALL);
        } catch (RemoteException e) {
            Log.e("E", "Cannot stop but it does not matter now", e);
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
                    beaconManager.startRanging(Beacons.REGION_R);
                    beaconManager.startRanging(Beacons.REGION_G);
                    beaconManager.startRanging(Beacons.REGION_B);
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
        root.setBackgroundColor(Color.rgb(r,g,b));
    }

    public void updateValueFields(View v) {
        View root = v.getRootView();
       int color =  ((ColorDrawable)root.getBackground()).getColor();
      TextView r = (TextView)findViewById(R.id.rvalue);
        r.setText(" " + Color.red(color));

        TextView g = (TextView)findViewById(R.id.gvalue);
        g.setText(" " + Color.green(color));

        TextView b = (TextView)findViewById(R.id.bvalue);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private int getRandomColor() {
        int r;
        int g;
        int b;

        int min = 0;
        int max = 255;

        Random rand = new Random();

        r = rand.nextInt(max - min + 1) + min;
        g = rand.nextInt(max - min + 1) + min;
        b = rand.nextInt(max - min + 1) + min;

        return  Color.rgb(r, g, b);
    }
}
