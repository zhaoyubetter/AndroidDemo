package com.ldfs.demo.preference.reader;

import android.content.Context;
import android.text.TextUtils;

import com.ldfs.demo.App;
import com.ldfs.demo.util.IOUtils;
import com.ldfs.demo.util.XmlParser;
import com.ldfs.demo.util.XmlParser.ParserListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public abstract class AssetReader<K, E> {

    /**
     * 初始化资源
     */
    public HashMap<K, E> read() {
        InputStream inputStream = null;
        HashMap<K, E> configs = null;
        try {
            Context context = App.getAppContext();
            configs = new HashMap<K, E>();
            String config = getConfig();
            if (!TextUtils.isEmpty(config)) {
                inputStream=context.getAssets().open(config);
            } else {
                throw new NullPointerException("config 不能都为空!");
            }
            if (null != inputStream) {
                ParserListener listener = getParserListener(configs);
                if (null != listener) {
                    XmlParser.startParser(inputStream, listener);
                } else {
                    throw new NullPointerException("listener 不能为空!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeStream(inputStream);
        }
        return configs;
    }

    /**
     * 读取配置项
     *
     * @return
     */
    public abstract ParserListener getParserListener(HashMap<K, E> configs);

    /**
     * 获得配置项位置
     *
     * @return
     */
    public abstract String getConfig();

}
