package com.ldfs.demo.preference.reader;

import com.ldfs.demo.listener.ITask;
import com.ldfs.demo.preference.ConfigName;
import com.ldfs.demo.preference.PrefernceUtils;
import com.ldfs.demo.preference.config.UIConfig;
import com.ldfs.demo.preference.config.UIItem;
import com.ldfs.demo.util.XmlParser;
import com.ldfs.demo.util.XmlParser.ParserListener;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.HashMap;

public class UIInfoReader extends AssetReader<String,UIConfig> {

	@Override
	public ParserListener getParserListener(final HashMap<String, UIConfig> configs) {
		return new ParserListener() {
			private UIConfig mConfig;
			private UIItem mItem;
			private int mVersion;

			@Override
			public void startParser(XmlPullParser parser) {
				if ("ui".equals(parser.getName())) {
					XmlParser.runParser(parser, new ITask<String>() {
						@Override
						public void run(String... ts) {
							if ("version".equals(ts[0])) {
								// 当前更新版本号
								mVersion = Integer.valueOf(ts[1]);
							}
						}
					});
				} else if ("type".equals(parser.getName())) {
					mConfig = new UIConfig();
					mConfig.items = new ArrayList<UIItem>();
					XmlParser.runParser(parser, new ITask<String>() {
						@Override
						public void run(String... ts) {
							if ("name".equals(ts[0])) {
								mConfig.name = ts[1];
							} else if ("id".equals(ts[0])) {
								mConfig.id = Integer.valueOf(ts[1]);
							} else if ("parent".equals(ts[0])) {
								mConfig.parent = Integer.valueOf(ts[1]);
							} else if ("version".equals(ts[0])) {
								mConfig.version = Integer.valueOf(ts[1]);
							}
						}
					});
				} else if ("item".equals(parser.getName())) {
					mItem = new UIItem();
					XmlParser.runParser(parser, new ITask<String>() {
						@Override
						public void run(String... ts) {
							if ("name".equals(ts[0])) {
								mItem.name = ts[1];
							} else if ("info".equals(ts[0])) {
								mItem.info = ts[1];
							} else if ("clazz".equals(ts[0])) {
								mItem.clazz = ts[1];
							} else if ("version".equals(ts[0])) {
								mItem.version = Integer.valueOf(ts[1]);
							} else if ("id".equals(ts[0])) {
								mItem.container = ts[1];
							}
						}
					});
				}
			}

			@Override
			public void endParser(XmlPullParser parser) {
				if ("ui".equals(parser.getName())) {
					PrefernceUtils.setInt(ConfigName.NEW_VERSION, mVersion);
				} else if ("type".equals(parser.getName())) {
					// 记录parentid
					mConfig.parent = (0 == mConfig.parent) ? -1 : mConfig.parent;
					configs.put(String.valueOf(mConfig.id), mConfig);
				} else if ("item".equals(parser.getName())) {
					mItem.id = -1;// 代表其为子分类
					mItem.parent = mConfig.id;
					mConfig.items.add(mItem);
				}
			}
		};
	}

	@Override
	public String getConfig() {
		return "config/ui_config.xml";
	}

}
