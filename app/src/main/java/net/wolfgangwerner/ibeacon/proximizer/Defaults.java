package net.wolfgangwerner.ibeacon.proximizer;


public class Defaults {

    // Why are these all Strings? Because they come as Strings from EditTextPreferences and are parsed afterwards, so this is consistent
    // Typed preferences can only be put pragmatically. The input fields are validated accordingly.
    public static final String SCAN_INTERVAL = "5";
    public static final String SCAN_PAUSE = "5";
    public static final String RED_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    public static final String RED_MAJOR = "1";
    public static final String RED_MINOR = "1";
    public static final String GREEN_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    public static final String GREEN_MAJOR = "2";
    public static final String GREEN_MINOR = "1";
    public static final String BLUE_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    public static final String BLUE_MAJOR = "3";
    public static final String BLUE_MINOR = "1";
}
