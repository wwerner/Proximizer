package net.wolfgangwerner.ibeacon.proximizer;

import com.estimote.sdk.Region;

/**
 * Created by wernerw on 01.01.2015.
 */
public class Beacons {
    private static final String ESTIMOTE_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    public static final String RED = "Red";
    public static final String GREEN = "Green";
    public static final String BLUE = "Blue";

    public static final Region REGION_ALL = new Region("All", ESTIMOTE_PROXIMITY_UUID, null, null);

    public static final Region REGION_R = new Region(RED,
            ESTIMOTE_PROXIMITY_UUID, 1, 1);
    public static final Region REGION_G = new Region(GREEN,
            ESTIMOTE_PROXIMITY_UUID, 2, 1);
    public static final Region REGION_B = new Region(BLUE,
            ESTIMOTE_PROXIMITY_UUID, 3, 1);

}
