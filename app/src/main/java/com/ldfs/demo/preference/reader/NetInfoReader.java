package com.ldfs.demo.preference.reader;

import android.text.TextUtils;

import com.ldfs.demo.listener.ITask;
import com.ldfs.demo.preference.config.NetConfig;
import com.ldfs.demo.util.XmlParser;
import com.ldfs.demo.util.XmlParser.ParserListener;

import org.xmlpull.v1.XmlPullParser;

import java.util.HashMap;

/**
 * 网络信息读取类
 * 
 * @author momo
 * 
 * @Date 2014/11/25
 */
public class NetInfoReader extends AssetReader<String,NetConfig> {

	@Override
	public ParserListener getParserListener(final HashMap<String, NetConfig> configs) {
		return new ParserListener() {
			private NetConfig config;

			@Override
			public void startParser(XmlPullParser parser) {
				if ("item".equals(parser.getName())) {
					config = new NetConfig();
					XmlParser.runParser(parser, new ITask<String>() {
						@Override
						public void run(String... ts) {
							if ("action".equals(ts[0])) {
								config.action = ts[1];
							} else if ("method".equals(ts[0])) {
								config.method = ts[1];
							} else if ("params".equals(ts[0])) {
								config.params = !TextUtils.isEmpty(ts[1]) ? ts[1].split("&") : null;
							} else if ("url".equals(ts[0])) {
								config.url = ts[1];
							}
						}
					});
				}
			}

			@Override
			public void endParser(XmlPullParser parser) {
				if ("item".equals(parser.getName()) && null != config) {
					configs.put(config.action, config);
				}
			}
		};
	}

	@Override
	public String getConfig() {
		return "config/net_config.xml";
	}

}
