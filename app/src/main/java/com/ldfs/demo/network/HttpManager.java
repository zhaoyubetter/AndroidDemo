package com.ldfs.demo.network;

import java.io.File;
import java.util.Map;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.ldfs.demo.App;
import com.ldfs.demo.preference.ConfigManager;
import com.ldfs.demo.preference.config.NetConfig;
import com.ldfs.demo.util.JsonUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 网络帮助类
 * 
 * @author Administrator
 */
public class HttpManager {

	private static final int MAX_CONNTION = 10 * 1000;
	public static final int PAGE_SIZE = 10;
	public static HttpUtils httpUtils;

	static {
		httpUtils = new HttpUtils(MAX_CONNTION);
		httpUtils.configHttpCacheSize(5);
		httpUtils.configRequestThreadPoolSize(5);
		httpUtils.configResponseTextCharset("utf-8");
	}

	/**
	 * 下载文件
	 * 
	 * @return
	 */
	public static HttpHandler<File> down(String url, String target, RequestCallBack<File> callBack) {
		return httpUtils.download(url, target, true, true, callBack);
	}

	/**
	 * 判断当前apn列表中哪个连接选中了
	 */
	public static boolean checkNetWork() {
		final Context context = App.getAppContext();
		boolean wifi = isWIFI(context);
		boolean moble = isMoble(context);
		// 如果两个连接都未选中
		if (!wifi && !moble) {
			return false;
		}
		return true;
	}

	/**
	 * 判断wifi是否处于连接状态
	 * 
	 * @return boolean :返回wifi是否连接
	 */
	public static boolean isWIFI(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		boolean result = false;
		if (networkInfo != null) {
			result = networkInfo.isConnected();
		}
		return result;
	}

	/**
	 * 判断是否APN列表中某个渠道处于连接状态
	 * 
	 * @return 返回是否连接
	 */
	public static boolean isMoble(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		boolean result = false;
		if (networkInfo != null) {
			result = networkInfo.isConnected();
		}
		return result;
	}

	static {
		httpUtils = new HttpUtils(MAX_CONNTION);
		httpUtils.configHttpCacheSize(5);
		httpUtils.configRequestThreadPoolSize(5);
		httpUtils.configResponseTextCharset("utf-8");
	}

	/**
	 * 请求事件
	 * 
	 * @param action
	 * @param listener
	 * @param showMessage
	 * @param params
	 */
	public static void request(String action, final boolean showMessage, final ResponseListener listener, Object... params) {
		if (checkNetWork()) {
			if (!TextUtils.isEmpty(action)) {
				NetConfig config = ConfigManager.getNetConfig(action);
				if (null != config) {
					HttpMethod httpMethod = getRequestMethod(config.method);
					RequestParams requestParams = getRequestParams(httpMethod, action, params);
					httpUtils.send(httpMethod, config.url, requestParams, new RequestCallBack<String>() {
						@Override
						public void onFailure(HttpException exception, String info) {
							if (null != listener) {
								listener.onFail(true, exception);
							}
						}

						@Override
						public void onSuccess(ResponseInfo<String> info) {
							if (null != info && null != info.result) {
								Map<String, String> params = JsonUtils.getResonseDataMap(info.result);
								if (null != params) {
									// 用户提示信息
									String message = params.get("message");
									if (showMessage && !TextUtils.isEmpty(message)) {
										App.toast(message);
									}
									if (null != listener) {
										listener.onSuccess(Boolean.parseBoolean(params.get("success")), JsonUtils.getRequestNumber(params.get("error_code")), params.get("items"));
									}
								}
							}
						}
					});
				}
			}
		} else if (null != listener) {
			listener.onFail(true, null);
		}
	}

	public static void request(String action, final ResponseParamsListener listener, Object... params) {
		request(action, false, listener, params);
	}

	/**
	 * 请求事件
	 * 
	 * @param action
	 * @param listener
	 * @param showMessage
	 * @param params
	 */
	public static void request(String action, final boolean showMessage, final ResponseParamsListener listener, Object... params) {
		if (checkNetWork()) {
			if (!TextUtils.isEmpty(action)) {
				NetConfig config = ConfigManager.getNetConfig(action);
				if (null != config) {
					HttpMethod httpMethod = getRequestMethod(config.method);
					httpUtils.send(httpMethod, config.url, getRequestParams(httpMethod, action, params), new RequestCallBack<String>() {
						@Override
						public void onFailure(HttpException exception, String info) {
							if (null != listener) {
								listener.onFail(true, exception);
							}
						}

						@Override
						public void onSuccess(ResponseInfo<String> info) {
							if (null != info && null != info.result) {
								Map<String, String> params = JsonUtils.getResonseDataMap(info.result);
								if (null != params) {
									// 用户提示信息
									String message = params.get("message");
									if (showMessage && !TextUtils.isEmpty(message)) {
										App.toast(message);
									}
									if (null != listener) {
										listener.onSuccess(Boolean.parseBoolean(params.get("success")), JsonUtils.getRequestNumber(params.get("error_code")), params, info.result);
									}
								}
							}
						}
					});
				}
			}
		} else if (null != listener) {
			listener.onFail(true, null);
		}
	}

	public static void request(String action, final ResponseListener listener, Object... params) {
		request(action, false, listener, params);
	}

	/**
	 * 获得请求参数 基于xutils
	 * 
	 * @param httpMethod
	 * @return
	 */
	private static RequestParams getRequestParams(HttpMethod httpMethod, String action, Object... paransValues) {
		RequestParams params = new RequestParams();
		NetConfig netConfig = ConfigManager.getNetConfig(action);
		if (null != netConfig) {
			String[] paramsNames = netConfig.params;
			if (null != paramsNames && null != paransValues) {
				int length = paransValues.length;
				final String DEFAULT_NUMBER_VALUE = "-1";
				for (int i = 0; i < length; i++) {
					if (null != paransValues[i] && null != paransValues[i] && !DEFAULT_NUMBER_VALUE.equals(paransValues[i].toString())) {
						addParams(httpMethod, params, paramsNames[i], paransValues[i].toString());
					}
				}
			}
		}
		return params;
	}

	private static void addParams(HttpMethod httpMethod, RequestParams params, String key, String value) {
		switch (httpMethod) {
		case GET:
			params.addQueryStringParameter(key, value);
			break;
		case POST:
			params.addBodyParameter(key, value);
			break;
		default:
			break;
		}
	}

	/**
	 * 获得请求类型
	 * 
	 * @param method
	 * @return
	 */
	private static HttpMethod getRequestMethod(String method) {
		HttpMethod httpMethod = HttpMethod.GET;
		if (!TextUtils.isEmpty(method)) {
			httpMethod = "get".equals(method.toLowerCase()) ? HttpMethod.GET : HttpMethod.POST;
		}
		return httpMethod;
	}

	/**
	 * 获得网络状态显示字符串
	 * 
	 * @param context
	 * @return
	 */
	public static String getNetWorkState(Context context) {
		String result = null;
		ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectMgr.getActiveNetworkInfo();
		if (null != info) {
			int type = info.getType();
			switch (type) {
			case ConnectivityManager.TYPE_WIFI:
				result = "WIFI";
				break;
			case ConnectivityManager.TYPE_MOBILE:
				int subtype = info.getSubtype();
				switch (subtype) {
				// 2G
				case TelephonyManager.NETWORK_TYPE_GPRS: // 网络类型为GPRS
				case TelephonyManager.NETWORK_TYPE_CDMA: // 网络类型为CDMA
				case TelephonyManager.NETWORK_TYPE_EDGE: // 网络类型为EDGE
					result = "2G";
					break;
				// 4G??
				case TelephonyManager.NETWORK_TYPE_LTE:
					result = "4G";
					break;
				// 3G
				case TelephonyManager.NETWORK_TYPE_EVDO_0: // 网络类型为EVDO0
				case TelephonyManager.NETWORK_TYPE_EVDO_A:// 网络类型为EVDOA
				case TelephonyManager.NETWORK_TYPE_HSDPA: // 网络类型为HSDPA
				case TelephonyManager.NETWORK_TYPE_HSPA: // 网络类型为HSPA
				case TelephonyManager.NETWORK_TYPE_HSUPA: // 网络类型为HSUPA
				case TelephonyManager.NETWORK_TYPE_UMTS: // 网络类型为UMTS 联通3G
				default:
					result = "3G";
					break;
				}
				break;
			default:
				break;
			}
		} else {
			result = "NO";
		}
		return result;
	}

	/**
	 * request请求回调
	 * 
	 * @author Administrator
	 */
	public interface ResponseListener extends FailListener {
		/* 请求成功 */
		void onSuccess(boolean isScuuess, int code, String data);
	}

	/**
	 * request请求回调
	 * 
	 * @author Administrator
	 */
	public interface ResponseParamsListener extends FailListener {
		/* 请求成功 */
		void onSuccess(boolean isScuuess, int code, Map<String, String> params, String result);
	}

	public interface FailListener {
		/* 请求失败 */
		void onFail(boolean noNetwok, Exception error);
	}

	/**
	 * 无网络事件接口
	 */
	public interface NoNetworkListener {
		/**
		 * 无网络回调
		 */
		void noNetwork();
	}

}
