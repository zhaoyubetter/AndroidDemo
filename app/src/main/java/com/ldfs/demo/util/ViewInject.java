package com.ldfs.demo.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ldfs.demo.App;
import com.ldfs.demo.R;
import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.annotation.MethodClick;
import com.ldfs.demo.annotation.Navigation;
import com.ldfs.demo.annotation.ViewClick;
import com.ldfs.demo.annotation.item.RateInfo;
import com.ldfs.demo.listener.ViewImageClickListener;
import com.ldfs.demo.preference.ConfigName;
import com.ldfs.demo.preference.PrefernceUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;


/**
 * 控件寻找id与设置点击事件帮助类
 *
 * @author 扑倒末末
 */
public class ViewInject {

    /**
     * 初始化属性
     *
     * @param
     */
    public static void init(Object object) {
        init(object, null);
    }

    /**
     * 初始化fragment
     */
    public static void init(Object object, View view) {
        init(object, view, false);
    }

    /**
     * 初始化Fragment/Dialog/View
     *
     * @param object
     * @param view
     * @param initParent 是否初始化父类-----使用于ViewHolder 对象继承
     */
    public static void init(Object object, View view, boolean initParent) {
        initActionBar(object, view);
        initView(object, view, initParent);
        initClick(object, view);
        initMethodClick(object, view);
        initRate(object, view);
    }

    /**
     * 初始化进度信息
     *
     * @param object
     * @param view
     */
    private static void initRate(Object object, View view) {
        //因主界面程序己固定,且布局等都己非常多了.所以默认并不改动每个布局去添加这个文件,
        // 而以动态生成布局方式添加.非RelativeLayout,则在外围添加一层RelativeLayout
        boolean bateInfo = PrefernceUtils.getRvsBoolean(ConfigName.BATE_INFO);
        RateInfo info = object.getClass().getAnnotation(RateInfo.class);
        if (bateInfo && null != info && null != view) {
            RelativeLayout rateContainer = null;
            Context context = view.getContext();
            View rateLayout = View.inflate(context, R.layout.rate_layout, null);
            TextView rateStatus = (TextView) rateLayout.findViewById(R.id.tv_rate_state);
            TextView rateInfo = (TextView) rateLayout.findViewById(R.id.tv_rate_info);
            rateStatus.setText(info.rate().toString());//设置demo完成进度状态
            rateInfo.setText(info.beteInfo());//设置demo进度备注信息
            if (view instanceof RelativeLayout) {
                //直接添加
                rateContainer = (RelativeLayout) view;
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                LinkedList<View> childViews = new LinkedList<>();
                for (int i = 0; i < viewGroup.getChildCount(); ) {
                    View childView = viewGroup.getChildAt(i);
                    childViews.add(childView);
                    viewGroup.removeView(childView);
                }
                rateContainer = new RelativeLayout(context);
                viewGroup.addView(rateContainer, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                LinearLayout container = new LinearLayout(context);
                container.setOrientation(LinearLayout.VERTICAL);
                int size = childViews.size();
                for (int i = 0; i < size; i++) {
                    container.addView(childViews.removeFirst());
                }
                rateContainer.addView(container, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }
            if (null != rateContainer) {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                rateContainer.addView(rateLayout, layoutParams);
            }
        }
    }

    /**
     * 初始化actionBar标题
     *
     * @param object
     * @param view
     */
    private static void initActionBar(Object object, View view) {
        Navigation navigation = object.getClass().getAnnotation(Navigation.class);
        if (null != navigation) {
            ActionBar actionBar = null;
            if (object instanceof ActionBarActivity) {
                actionBar = ((ActionBarActivity) object).getSupportActionBar();
            } else if (object instanceof Fragment) {
                actionBar = ((ActionBarActivity) ((Fragment) object).getActivity()).getSupportActionBar();
            }
            // 设置标题
            if (-1 != navigation.title()) {
                actionBar.setTitle(navigation.title());
            } else {
                actionBar.setIcon(R.drawable.ic_launcher);
            }
            // 设置是否启用返回键
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(navigation.displayHome());
            Resources resources = App.getAppContext().getResources();
            // 设置标题栏色值
            actionBar.setBackgroundDrawable(new ColorDrawable(resources.getColor(R.color.blue)));
            // int titleId = resources.getIdentifier("action_bar_title", "id",
            // "android");
            // View titleView = findView(object, view, titleId);
            // if (null != titleView) {
            // ((TextView)
            // titleView).setTextColor(resources.getColor(R.color.black));
            // }
            // 设置是否隐藏系统图标
            actionBar.setDisplayUseLogoEnabled(!navigation.hiddenIcon());
            if (!navigation.hiddenIcon()) {
                actionBar.setIcon(R.drawable.ic_launcher);
            } else {
                actionBar.setIcon(new ColorDrawable(Color.TRANSPARENT));
            }
            // actionBar是否隐藏
            if (navigation.hidden()) {
                actionBar.hide();
            } else {
                actionBar.show();
            }
            // 横屏
            // ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            // 竖屏
            // ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            if (object instanceof Fragment) {
                //noinspection ResourceType
                ((Fragment) object).getActivity().setRequestedOrientation(navigation.orientation());
                // 是否启用当前fragment actionBar
                ((Fragment) object).setHasOptionsMenu(navigation.hasOptionMenu());
            }
        }
    }

    /**
     * 初始化方法点击事件
     *
     * @param object
     * @param view
     */
    private static void initMethodClick(final Object object, View view) {
        if (null != object) {
            final Method[] methods = object.getClass().getDeclaredMethods();
            int length = methods.length;
            for (int i = 0; i < length; i++) {
                methods[i].setAccessible(true);
                MethodClick methodClick = methods[i].getAnnotation(MethodClick.class);
                if (null != methodClick) {
                    int[] ids = methodClick.ids();
                    if (null != ids) {
                        View findView = null;
                        for (int j = 0; j < ids.length; j++) {
                            findView = getFindView(object, view, ids[j]);
                            if (null != findView) {
                                //设置方法点击
                                final int finalI = i;
                                findView.setOnClickListener(new ViewImageClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        try {
                                            methods[finalI].invoke(object, view);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }));
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 初始化控件点击事件
     *
     * @param object
     */
    private static void initClick(Object object, View view) {
        if (null != object && object instanceof OnClickListener) {
            ViewClick viewClick = object.getClass().getAnnotation(ViewClick.class);
            if (null != viewClick) {
                if (null != viewClick.ids() && 0 < viewClick.ids().length && -1 != viewClick.ids()[0]) {
                    int[] ids = viewClick.ids();
                    for (int i = 0; i < ids.length; i++) {
                        setOnclickListenerById(object, view, ids[i]);
                    }
                }
                //子控件点击
                if (null != viewClick.childClick() && 0 < viewClick.childClick().length && -1 != viewClick.childClick()[0]) {
                    int[] ids = viewClick.childClick();
                    for (int i = 0; i < ids.length; i++) {
                        View findView = getFindView(object, view, ids[i]);
                        if (null != findView && findView instanceof ViewGroup) {
                            ViewGroup findGroupView = (ViewGroup) findView;
                            int childCount = findGroupView.getChildCount();
                            for (int j = 0; j < childCount; j++) {
                                setOnClickListener(object, findGroupView.getChildAt(j));
                            }
                        }
                    }
                }
            }
        }
    }

    private static void setOnclickListenerById(Object object, View view, int id) {
        View findView = getFindView(object, view, id);
        if (null != findView) {
            setOnClickListener(object, findView);
        }
    }

    private static View getFindView(Object object, View view, int id) {
        View findView = null;
        if (object instanceof Activity) {
            findView = ((Activity) object).findViewById(id);
        } else if (object instanceof Dialog) {
            findView = ((Dialog) object).findViewById(id);
        } else if (null != view) {
            findView = view.findViewById(id);
        }
        return findView;
    }

    /**
     * 查找控件id
     *
     * @param object
     * @param view
     * @param initParent
     */
    private static void initView(Object object, View view, boolean initParent) {
        initObject(object, object.getClass(), view);
        if (initParent) {
            initObject(object, object.getClass().getSuperclass(), view);
        }
    }

    private static void initObject(Object object, Class<?> clazz, View view) {
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            ID id = field.getAnnotation(ID.class);
            if (null != id) {
                // 设置控件id
                if (0 != id.id()) {
                    try {
                        View findView = null;
                        if (object instanceof Activity) {
                            // activity
                            findView = ((Activity) object).findViewById(id.id());
                        } else if (object instanceof Dialog) {
                            // 对话框---
                            findView = ((Dialog) object).findViewById(id.id());
                        } else if (object instanceof View) {
                            findView = ((View) object).findViewById(id.id());
                        } else if (null != view) {
                            // view---用于fragment
                            findView = view.findViewById(id.id());
                        }
                        if (null != findView) {
                            field.set(object, findView);
                            // 设置当前控件点击
                            if (id.click()) {
                                setOnClickListener(object, findView);
                            }
                            // 设置子控件点击事件
                            if (id.childClick() && findView instanceof ViewGroup) {
                                for (int i = 0; i < ((ViewGroup) findView).getChildCount(); i++) {
                                    setOnClickListener(object, ((ViewGroup) findView).getChildAt(i));
                                }
                            }

                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 设置控件onClick事件到当前acivity----activity必须实现onclick接口！
     *
     * @param object
     * @param view
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws java.lang.reflect.InvocationTargetException
     */
    private static void setOnClickListener(Object object, View view) {
        try {
            Method method = view.getClass().getMethod("setOnClickListener", OnClickListener.class);
            if (null != method && object instanceof OnClickListener) {
                method.invoke(view, new ViewImageClickListener((OnClickListener) object));
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
