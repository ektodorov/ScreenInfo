package com.screeninfo;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

import java.lang.reflect.Method;

public class MainActivity extends Activity {

    private static final String LOG = MainActivity.class.getSimpleName();

    public static final String STR_EMPTY = "";
    public static final String STR_BLANK = " ";
    public static final String STR_multiplier = "multiplier";
    public static final String STR_Density = "Density";
    public static final String STR_symbol_doubledots = ":";
    public static final String STR_symbol_newLine = "\n";
    public static final String STR_DPI = "DPI";
    public static final String STR_Height = "Height";
    public static final String STR_Width = "Width";
    public static final String STR_Status = "Status";
    public static final String STR_bar = "bar";
    public static final String STR_height = "height";
    public static final String STR_available = "available";
    public static final String STR_for_app = "for app";
    public static final String STR_getRawWidth = "getRawWidth";
    public static final String STR_getRawHeight = "getRawHeight";
    public static final String STR_getRealSize = "getRealSize";
    public static final String STR_status_bar_height = "status_bar_height";
    public static final String STR_dimen = "dimen";
    public static final String STR_android = "android";
    public static final String STR_Android_Version = "Android Version";
    public static final String STR_API_Level = "API Level";
    public static final String STR_Heap = "Heap";
    public static final String STR_size = "size";
    public static final String STR_MB = "MB";

    private static final int SDK_ICE_CREAM_SANDWITCH = 14;
    private static final int SDK_JELLY_BEAN_MR1 = 17;

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView)findViewById(R.id.textView);

        Resources res = getResources();
        DisplayMetrics displayMetrics = res.getDisplayMetrics();

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);

        int widthPixels = -1;
        int heightPixels = -1;
        if (Build.VERSION.SDK_INT >= SDK_ICE_CREAM_SANDWITCH && Build.VERSION.SDK_INT < SDK_JELLY_BEAN_MR1) {
            try {
                Method methodGetRawWidth = display.getClass().getDeclaredMethod(STR_getRawWidth);
                Method methodGetRawHeight = display.getClass().getDeclaredMethod(STR_getRawHeight);
                methodGetRawWidth.setAccessible(true);
                methodGetRawHeight.setAccessible(true);
                widthPixels = (Integer)methodGetRawWidth.invoke(display);
                heightPixels = (Integer)methodGetRawHeight.invoke(display);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (Build.VERSION.SDK_INT >= SDK_JELLY_BEAN_MR1) {
            try {
                Point realSize = new Point();
                Method methodGetRealSize = display.getClass().getDeclaredMethod(STR_getRealSize, new Class[]{Point.class});
                methodGetRealSize.setAccessible(true);
                methodGetRealSize.invoke(display, new Object[]{realSize});
                widthPixels = realSize.x;
                heightPixels = realSize.y;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        float density = displayMetrics.density;
        int densityDpi = displayMetrics.densityDpi;
        int height = screenSize.y;
        int width = screenSize.x;
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier(STR_status_bar_height, STR_dimen, STR_android);
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }


        long megabyte = 1048576l; //1 MB = 1048576 bytes
        Runtime rt = Runtime.getRuntime();
        long maxMemory = rt.maxMemory() / megabyte;

        String strDensity = getResources().getString(R.string.dpi_label);

        StringBuilder sb = new StringBuilder();

        sb.append(STR_Android_Version).append(STR_symbol_doubledots);
        sb.append(Build.VERSION.RELEASE);
        sb.append(STR_symbol_newLine);

        sb.append(STR_API_Level).append(STR_symbol_doubledots);
        sb.append(Build.VERSION.SDK_INT);
        sb.append(STR_symbol_newLine);

        sb.append(STR_Density).append(STR_BLANK).append(STR_multiplier).append(STR_symbol_doubledots);
        sb.append(density);
        sb.append(STR_symbol_newLine);

        sb.append(STR_Density).append(STR_symbol_doubledots);
        sb.append(strDensity);
        sb.append(STR_symbol_newLine);

        sb.append(STR_DPI).append(STR_symbol_doubledots);
        sb.append(densityDpi);
        sb.append(STR_symbol_newLine);

        sb.append(STR_Height).append(STR_symbol_doubledots);
        sb.append(heightPixels);
        sb.append(STR_symbol_newLine);

        sb.append(STR_Width).append(STR_symbol_doubledots);
        sb.append(widthPixels);
        sb.append(STR_symbol_newLine);

        sb.append(STR_Status).append(STR_BLANK).append(STR_bar).append(STR_BLANK).append(STR_height).append(STR_symbol_doubledots);
        sb.append(statusBarHeight);
        sb.append(STR_symbol_newLine);

        sb.append(STR_Height).append(STR_BLANK).append(STR_available).append(STR_BLANK).append(STR_for_app).append(STR_symbol_doubledots);
        sb.append(height);
        sb.append(STR_symbol_newLine);

        sb.append(STR_Width).append(STR_BLANK).append(STR_available).append(STR_BLANK).append(STR_for_app).append(STR_symbol_doubledots);
        sb.append(width);
        sb.append(STR_symbol_newLine);

        sb.append(STR_Heap).append(STR_BLANK).append(STR_size).append(STR_symbol_doubledots);
        sb.append(maxMemory).append(STR_BLANK).append(STR_MB);

        mTextView.setText(sb.toString());
    }
}
