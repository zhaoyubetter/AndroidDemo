package com.ldfs.demo.theme;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.SparseArray;

import com.ldfs.demo.App;
import com.ldfs.demo.util.XmlParser;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by momo on 2015/5/13.
 *
 * 主题样式读取类
 */
public class ThemeReader {
    private static final String ANDROID_NAME_SPACE = "http://schemas.android.com/apk/res/android";
    private static final ArrayList<String> FILTER_NAME_SPACE;
    public static final ArrayList<String> mAttrsNames;

    static {
        FILTER_NAME_SPACE = new ArrayList<>();
        FILTER_NAME_SPACE.add(ANDROID_NAME_SPACE);
        mAttrsNames = new ArrayList<>();
        mAttrsNames.addAll(Arrays.asList(new String[]{"_src", "src_filter",
                "_background", "divide_color",
                "_text_color", "_drawableTop", "_drawableLeft", "_drawableRight", "_drawableBottom", "drawableTop_filter", "drawableLeft_filter", "drawableRight_filter", "drawableBottom_filter"}));
    }

    private Context mContext;
    private final int mLayout;
    private int id;

    public ThemeReader(int mLayout) {
        this.mContext = App.getAppContext();
        this.mLayout = mLayout;
    }

    public SparseArray<ArrayList<ThemeValue>> read() {
        SparseArray<ArrayList<ThemeValue>> configs = null;
        try {
            configs = new SparseArray<>();
            XmlResourceParser resourceParser = mContext.getResources().getXml(mLayout);
            XmlParser.ParserListener listener = getParserListener(configs);
            if (null != listener) {
                XmlParser.startParser(resourceParser, listener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return configs;
    }

    public XmlParser.ParserListener getParserListener(final SparseArray<ArrayList<ThemeValue>> configs) {
        return new XmlParser.ParserListener<XmlResourceParser>() {
            private ArrayList<ThemeValue> mThemeValues;
            private ThemeValue mValue;

            @Override
            public void startParser(final XmlResourceParser parser) {
                mThemeValues = new ArrayList<>();
                parserAttrs(parser, new XmlLayoutParserListener() {
                    @Override
                    public void parser(String nameSpace, String attrName, String attrValue) {
                        if (null == mValue) {
                            mValue = new ThemeValue();
                            mThemeValues.add(mValue);
                            configs.append(id, mThemeValues);
                            mValue.id = id;
                            mValue.attrName = attrName;
                            mValue.attrValue = mAttrsNames.indexOf(attrName);
                            initResourceValue(mValue, attrValue);
                        }
//                        if ("_src".equals(attrName)) {
//                        } else if ("src_filter".equals(attrName)) {
//                        } else if ("_background".equals(attrName)) {
//                        } else if ("divide_color".equals(attrName)) {
//                        } else if ("_text_color".equals(attrName)) {
//                        } else if ("_drawableTop".equals(attrName)) {
//                        } else if ("_drawableLeft".equals(attrName)) {
//                        } else if ("_drawableRight".equals(attrName)) {
//                        } else if ("_drawableBottom".equals(attrName)) {
//                        } else if ("drawableTop_filter".equals(attrName)) {
//                        } else if ("drawableLeft_filter".equals(attrName)) {
//                        } else if ("drawableRight_filter".equals(attrName)) {
//                        } else if ("drawableBottom_filter".equals(attrName)) {
//                        }
                        mValue = null;
                    }
                });
            }

            @Override
            public void endParser(XmlResourceParser parser) {
            }
        };
    }

    private ThemeValue initResourceValue(ThemeValue value, String attrValue) {
        if (!TextUtils.isEmpty(attrValue)) {
            attrValue = attrValue.substring(1);
            if (attrValue.startsWith("#")) {
                //颜色资源
                value.valueType = ValueType.COLOR;
                if (6 == attrValue.length()) {
                    attrValue = "FF" + attrValue;
                }
                value.value = Color.parseColor(attrValue);
            } else if (attrValue.startsWith("@")) {
                //引用资源
                value.valueType = ValueType.REFERENCE;
                value.value = Integer.valueOf(attrValue, 16);
            }
        }
        return value;
    }

    private int getHexValue(String attrValue) {
        int value = 0;
        String hex = "0x";
        int index = attrValue.indexOf(hex);
        if (-1 != index) {
            value = Integer.valueOf(attrValue.substring(index + hex.length()), 16);
        }
        return value;
    }

    public void parserAttrs(XmlResourceParser parser, XmlLayoutParserListener listener) {
        for (int i = 0; i < parser.getAttributeCount(); i++) {
            String namespace = parser.getAttributeNamespace(i);
            String attributeName = parser.getAttributeName(i);
            if ("id".equals(attributeName)) {
                id = parser.getAttributeResourceValue(ANDROID_NAME_SPACE, attributeName, i);
            }
            if (mAttrsNames.contains(attributeName) && null != listener) {
                listener.parser(namespace, attributeName, parser.getAttributeValue(i));
            }
        }
    }


    public interface XmlLayoutParserListener {
        void parser(String nameSpace, String attrName, String attrValue);
    }
}
