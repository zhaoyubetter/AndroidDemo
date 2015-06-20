package com.ldfs.demo.theme;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by momo on 2015/5/13.
 */
public class ThemeUtils {
    /**
     *
     * @param image
     * @param color
     */
    public static void setImageViewDrawableFilter(ImageView image, int color) { if (null != image) {
            setDrawableFilter(image.getDrawable(), color);
        }
    }

    /**
     * @param view
     * @param color
     */
    public static void setViewBackGroundFilter(View view, int color) {
        if (null != view) {
            setDrawableFilter(view.getBackground(), color);
        }
    }

    public static void setDrawableFilter(Drawable drawable, int color) {
        if (null != drawable) {
            drawable.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        }
    }
}
