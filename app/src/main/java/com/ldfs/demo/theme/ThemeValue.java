package com.ldfs.demo.theme;

/**
 * Created by momo on 2015/5/13.
 * 主题取值
 */
public class ThemeValue {
    public int id;
    public String attrName;
    public int attrValue;
    public int valueType;
    public int value;

    @Override
    public boolean equals(Object o) {
        boolean result = false;
        if (o instanceof ThemeValue) {
            ThemeValue value = (ThemeValue) o;
            result = id == value.id && attrValue == value.attrValue;
        }
        return result;
    }
}
