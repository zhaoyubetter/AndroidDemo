package com.ldfs.demo;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;

import com.ldfs.demo.listener.OperatListener;
import com.ldfs.demo.util.FragmentUtils;


/**
 * 其他Fragment,activity容器体,解决fragment跳转动画,及保存数据问题
 *
 * @author momo
 * @Date 2014/6/19
 */
public class MoreActivity extends ActionBarActivity implements OperatListener {
    public static final String CLASS_TAG = "class";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        App.activitys.add(this);
        toFragment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(resultCode);
    }

    @Override
    public void onOperate(int optionId, Bundle bundle) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (null != fragments && !fragments.isEmpty()) {
            int length = fragments.size();
            for (int i = 0; i < length; i++) {
                Fragment fragment = fragments.get(i);
                if (null != fragment && fragment instanceof OperatListener) {
                    ((OperatListener) fragment).onOperate(optionId, bundle);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (0 < backStackEntryCount) {
            // 返回栈内有对象,通知返回
            FragmentUtils.notifyAction(this, getSupportFragmentManager().getBackStackEntryAt(backStackEntryCount - 1).getName(), OperatListener.BACK_PRESS, null);
        } else {
            // 通知当前列表返回
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            if (null != fragments && !fragments.isEmpty()) {
                Fragment lastFragment = fragments.get(fragments.size() - 1);
                if (null != lastFragment && lastFragment instanceof OperatListener) {
                    FragmentUtils.notifyAction(this, lastFragment.getClass(), OperatListener.BACK_PRESS, null);
                } else {
                    super.onBackPressed();
                }
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onDestroy() {
        App.activitys.remove(this);
        super.onDestroy();
    }

    /**
     * 检测数据类型,跳转到指定fragment
     */
    private void toFragment() {
        Intent intent = getIntent();
        if (null != intent) {
            String className = intent.getStringExtra(CLASS_TAG);
            if (!TextUtils.isEmpty(className)) {
                try {
                    Class<?> clazz = Class.forName(className);
                    Object instance = clazz.newInstance();
                    if (instance instanceof Fragment) {
                        Fragment fragment = (Fragment) instance;
                        Bundle extras = getIntent().getExtras();
                        if (null != extras) {
                            fragment.setArguments(extras);
                        }
                        FragmentUtils.addFragment(this, fragment);
                    } else {
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    finish();
                }
            }
        }
    }

    /**
     * 跳转到一个activity内
     *
     * @param activity
     * @param clazz
     * @param extras
     */
    public static void toActivity(Activity activity, Class<? extends Fragment> clazz, Bundle extras) {
        if (null != activity) {
            Intent intent = new Intent(activity, MoreActivity.class);
            intent.putExtra(CLASS_TAG, clazz.getName());
            if (null != extras) {
                intent.putExtras(extras);
            }
            activity.startActivity(intent);
        }
    }

    /**
     * 跳转到一个activity带结果返回值
     *
     * @param fragment
     * @param activity
     * @param clazz
     * @param extras
     */
    public static void toActivityforResult(Fragment fragment, Activity activity, Class<? extends Fragment> clazz, Bundle extras) {
        if (null != activity && null != fragment) {
            Intent intent = new Intent(activity, MoreActivity.class);
            intent.putExtra(CLASS_TAG, clazz.getName());
            if (null != extras) {
                intent.putExtras(extras);
            }
            fragment.startActivityForResult(intent, 0);
        }
    }

    /**
     * 跳转到一个activity带结果返回值
     *
     * @param activity
     * @param clazz
     * @param extras
     */
    public static void toActivityforResult(Activity activity, Class<? extends Fragment> clazz, Bundle extras) {
        if (null != activity) {
            Intent intent = new Intent(activity, MoreActivity.class);
            intent.putExtra(CLASS_TAG, clazz.getName());
            if (null != extras) {
                intent.putExtras(extras);
            }
            activity.startActivityForResult(intent, 0);
        }
    }
}
