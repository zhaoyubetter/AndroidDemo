package com.ldfs.demo.preference;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import com.ldfs.demo.preference.config.NetConfig;
import com.ldfs.demo.preference.config.UIConfig;
import com.ldfs.demo.preference.reader.AssetReader;
import com.ldfs.demo.preference.reader.NetInfoReader;
import com.ldfs.demo.preference.reader.UIInfoReader;

/**
 * 配置管理对象
 * 
 * @author momo
 * @Date 2014/11/25
 * 
 */
public class ConfigManager {
	/** 网络信息管理对象 */
	private static SoftReference<HashMap<String, NetConfig>> mNetConfigs;
	private static SoftReference<HashMap<String, UIConfig>> mUIConfigs;
	static {
		mNetConfigs = new SoftReference<HashMap<String, NetConfig>>(null);
		mUIConfigs = new SoftReference<HashMap<String, UIConfig>>(null);
	}

	private static <T> SoftReference<HashMap<String, T>> ensureConfig(SoftReference<HashMap<String, T>> softReference, AssetReader<String,T> reader) {
		if (null == softReference) {
			softReference = new SoftReference<HashMap<String, T>>(null);
		}
		if (null == softReference.get()) {
			softReference = new SoftReference<HashMap<String, T>>(reader.read());
		}
		return softReference;
	}

	/**
	 * 获取网络配置项
	 * 
	 * @param channel
	 * @return
	 */
	public static NetConfig getNetConfig(String action) {
		mNetConfigs = ensureConfig(mNetConfigs, new NetInfoReader());
		return mNetConfigs.get().get(action);
	}

	/**
	 * 获取UI配置项
	 * 
	 * @param channel
	 * @return
	 */
	public static HashMap<String, UIConfig> getUIConfig() {
		mUIConfigs = ensureConfig(mUIConfigs, new UIInfoReader());
		return mUIConfigs.get();
	}

}
