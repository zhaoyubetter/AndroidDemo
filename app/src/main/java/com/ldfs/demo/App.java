package com.ldfs.demo;

import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.ldfs.demo.preference.PrefernceUtils;
import com.ldfs.demo.util.PackageUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

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
	private boolean test;

	static {
		activitys = new ArrayList<FragmentActivity>();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		initImageLoader(context);
		Stetho.initialize(
				Stetho.newInitializerBuilder(this)
						.enableDumpapp(
								Stetho.defaultDumperPluginsProvider(this))
						.enableWebKitInspector(
								Stetho.defaultInspectorModulesProvider(this))
						.build());
		PrefernceUtils.init(context);
		sWidth = getResources().getDisplayMetrics().widthPixels;
		sHeight = getResources().getDisplayMetrics().heightPixels;
		if (isDebug()) {
			Thread.setDefaultUncaughtExceptionHandler(this);
		}
	}

	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator()).diskCacheSize(50 * 1024 * 1024) // 50
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
		ImageLoader.getInstance().init(config);
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

	/**
	 * 是否为debug模式 如果是则开启log日志控制,开启测试信息,try catch控制
	 * 
	 * @return
	 */
	public static boolean isDebug() {
		if (!isDebug) {
			isDebug = PackageUtil.getBooleanMataData("IS_DUBUG");
		}
		return isDebug;
	}

	public static Resources getAppResource() {
		return context.getResources();
	}
}
