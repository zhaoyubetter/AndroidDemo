package com.ldfs.demo.theme;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.SoftReference;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by momo on 2015/5/13.
 */
public class ThemeHelper {
    private static final SparseArray<SparseArray<SoftReference<SparseArray<ArrayList<ThemeValue>>>>> mThemeValues;

    static {
        mThemeValues = new SparseArray<SparseArray<SoftReference<SparseArray<ArrayList<ThemeValue>>>>>();
    }

    /**
     * @param style
     */
    public static void setStyle(int style) {

    }

    /**
     * @param object
     * @param layout
     */
    public static void setLayoutStyle(Object object, int layout) {
        View view = getView(object);
        if (null == view) return;
        int style = 0;
        SoftReference<SparseArray<ArrayList<ThemeValue>>> layoutValuesReference = mThemeValues.get(style).get(layout);
        SparseArray<ArrayList<ThemeValue>> layoutValues = null;
        if (null == layoutValuesReference) {
            layoutValues = layoutValuesReference.get();
            if (null != layoutValues) {
                layoutValues = new ThemeReader(layout).read();
                if (null != layoutValues && 0 != layoutValues.size()) {
                    mThemeValues.get(style).put(layout, new SoftReference(layoutValues));
                }
            }
        }
        if (null != layoutValues && 0 != layoutValues.size()) {
            int size = layoutValues.size();
            ArrayList<ThemeValue> themeValues = null;
            for (int i = 0; i < size; i++) {
                themeValues = layoutValues.get(layoutValues.keyAt(i));
                for (ThemeValue value : themeValues) {
                    setViewStyle(view.findViewById(value.id), value);
                }
            }
        }
    }

    /**
     * @param findView
     * @param value
     */
    private static void setViewStyle(View findView, ThemeValue value) {
        if (null == findView || null == value) return;
        switch (value.valueType) {
            case ValueType.COLOR:
                setThemeColor(findView, value);
                break;
            case ValueType.REFERENCE:
                setThemeReference(findView, value);
                break;
            case ValueType.TEXT_SELECTOR:
                break;
            case ValueType.SHARE_SELECTOR:
                break;
            case ValueType.IMAGE_FILE:
                break;
        }
    }

    private static void setThemeReference(View findView, ThemeValue value) {
        int resId = value.value;
        Resources resources = findView.getResources();
        switch (value.attrValue) {
            case ThemeAttrs._SRC:
                setViewValue(findView, "setImageResource", resId, int.class);
                break;
            case ThemeAttrs._BACKGROUND:
                findView.setBackgroundResource(resId);
                break;
            case ThemeAttrs._TEXT_COLOR:
                setViewValue(findView, "setTextColor", resources.getColorStateList(resId), ColorStateList.class);
                break;
            case ThemeAttrs._DRAWABLELEFT:
                setTextViewCompoundDrawable(findView, 0, resId);
                break;
            case ThemeAttrs._DRAWABLETOP:
                setTextViewCompoundDrawable(findView, 1, resId);
                break;
            case ThemeAttrs._DRAWABLERIGHT:
                setTextViewCompoundDrawable(findView, 2, resId);
                break;
            case ThemeAttrs._DRAWABLEBOTTOM:
                setTextViewCompoundDrawable(findView, 3, resId);
                break;
        }
    }

    private static void setTextViewCompoundDrawable(View findView, int position, int resId) {
        if (findView instanceof TextView) {
            TextView textView = (TextView) findView;
            Resources resources = findView.getResources();
            Drawable drawable = null;
            try {
                drawable = resources.getDrawable(resId);
            } catch (Resources.NotFoundException e) {
            }
            if (null != drawable) {
                Drawable[] drawables = textView.getCompoundDrawables();
                drawables[position] = drawable;
                textView.setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawables[2], drawables[3]);
            }
        }
    }

    private static void setThemeColor(View findView, ThemeValue value) {
        int color = value.value;
        switch (value.attrValue) {
            case ThemeAttrs._SRC:
                setViewValue(findView, "setImageDrawable", new ColorDrawable(color), ColorDrawable.class);
                break;
            case ThemeAttrs._BACKGROUND:
                findView.setBackgroundColor(color);
                break;
            case ThemeAttrs._TEXT_COLOR:
                setViewValue(findView, "setTextColor", color, int.class);
                break;
            case ThemeAttrs.DIVIDE_COLOR:
                setViewValue(findView, "setDivideColor", color, int.class);
                break;
            case ThemeAttrs.SRC_FILTER:
                ThemeUtils.setImageViewDrawableFilter(((ImageView) findView), color);
                break;
            case ThemeAttrs.DRAWABLELEFT_FILTER:
                setTextViewCompoundDrawableFilter(findView, 0, color);
                break;
            case ThemeAttrs.DRAWABLETOP_FILTER:
                setTextViewCompoundDrawableFilter(findView, 1, color);
                break;
            case ThemeAttrs.DRAWABLERIGHT_FILTER:
                setTextViewCompoundDrawableFilter(findView, 2, color);
                break;
            case ThemeAttrs.DRAWABLEBOTTOM_FILTER:
                setTextViewCompoundDrawableFilter(findView, 3, color);
                break;
        }
    }

    /**
     * @param findView
     * @param position
     * @param color
     */
    private static void setTextViewCompoundDrawableFilter(View findView, int position, int color) {
        if (findView instanceof TextView) {
            TextView textView = (TextView) findView;
            Drawable[] drawables = textView.getCompoundDrawables();
            if (null != drawables) {
                ThemeUtils.setDrawableFilter(drawables[position], color);
            }
        }
    }

    private static void setViewValue(View view, String methodName, Object value, Class<?> clazz) {
        if (null == value) return;
        try {
            Method method = view.getClass().getMethod(methodName, clazz);
            if (null != method) {
                method.invoke(view, value);
            }
        } catch (Exception e) {
        }
    }

    private static View getView(Object object) {
        View contentView = null;
        if (object instanceof Fragment) {
            contentView = ((Fragment) object).getView();
        } else if (object instanceof Activity) {
            contentView = getContentView(((Activity) object));
        } else if (object instanceof View) {
            //dialog �����Զ���view����ֱ��ʹ��
            contentView = (View) object;
        }
        return contentView;
    }


    /**
     * @param activity
     * @return
     */
    private static View getContentView(Activity activity) {
        View contentView = null;
        View decorView = activity.getWindow().getDecorView();
        if (decorView instanceof ViewGroup) {
            ViewGroup decorViewGroup = (ViewGroup) decorView;
            int childCount = decorViewGroup.getChildCount();
            if (0 < childCount) {
                View childView = decorViewGroup.getChildAt(0);
                if (childView instanceof ViewGroup) {
                    ViewGroup childViewGroup = ((ViewGroup) childView);
                    childCount = childViewGroup.getChildCount();
                    contentView = childViewGroup.getChildAt(childCount - 1);
                }
            }
        }
        return contentView;
    }
}
