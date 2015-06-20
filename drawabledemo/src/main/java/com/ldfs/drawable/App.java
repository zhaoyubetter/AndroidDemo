package com.ldfs.drawable;

import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class App extends Application implements Thread.UncaughtExceptionHandler {
    public static final String STACKTRACE = "strace";
    private static Context context = null;
    public static final List<FragmentActivity> activitys;
    public static float sWidth, sHeight;
    private static boolean isDebug;// 是否为debug模式

    static {
        activitys = new ArrayList<FragmentActivity>();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        sWidth = getResources().getDisplayMetrics().widthPixels;
        sHeight = getResources().getDisplayMetrics().heightPixels;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public static String getStr(int resId, Object... args) {
        String result = null;
        if (null != args) {
            result = context.getString(resId, args);
        } else {
            result = context.getString(resId);
        }
        return result;
    }

    public static Resources getAppResources() {
        return context.getResources();
    }

    public static ContentResolver getResolver() {
        return context.getContentResolver();
    }

    public static Context getAppContext() {
        return context;
    }

    public static boolean isRun() {
        return null != activitys && !activitys.isEmpty();
    }

    public static int getResourcesColor(int id) {
        return context.getResources().getColor(id);
    }

    public static String[] getStringArray(int array) {
        return context.getResources().getStringArray(array);
    }

    public static int[] getIntegerArray(int id) {
        return context.getResources().getIntArray(id);
    }

    public static void toast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void toast(int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    public static void toast(int resId, Object... args) {
        Toast.makeText(context, getStr(resId, args), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        final StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        for (Activity activity : activitys) {
            activity.finish();
        }
        // 重启机制
        // Intent intent = new Intent(this, SplashActivity.class);
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // intent.putExtra(STACKTRACE, stackTrace.toString());
        // startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
