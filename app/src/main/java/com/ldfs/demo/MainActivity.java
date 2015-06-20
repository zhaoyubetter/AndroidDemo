package com.ldfs.demo;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ldfs.demo.annotation.ID;
import com.ldfs.demo.db.DbTable;
import com.ldfs.demo.preference.ConfigManager;
import com.ldfs.demo.preference.ConfigName;
import com.ldfs.demo.preference.PrefernceUtils;
import com.ldfs.demo.preference.config.UIConfig;
import com.ldfs.demo.preference.config.UIItem;
import com.ldfs.demo.theme.ThemeReader;
import com.ldfs.demo.theme.ThemeValue;
import com.ldfs.demo.ui.FragmentFunctionList;
import com.ldfs.demo.ui.FragmentFunctionList.TitleChangeCallbacks;
import com.ldfs.demo.ui.NavigationDrawerFragment;
import com.ldfs.demo.ui.NavigationDrawerFragment.NavigationDrawerCallbacks;
import com.ldfs.demo.util.FragmentUtils;
import com.ldfs.demo.util.HandleTask;
import com.ldfs.demo.util.HandleTask.TaskAction;
import com.ldfs.demo.util.Loger;
import com.ldfs.demo.util.ViewInject;
import com.ldfs.demo.widget.DrawerArrowDrawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static android.view.Gravity.LEFT;

public class MainActivity extends FragmentActivity implements NavigationDrawerCallbacks, TitleChangeCallbacks {
    private DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;
    @ID(id = R.id.drawer_layout)
    private DrawerLayout mDrawer;
    @ID(id = R.id.tv_app_name)
    private TextView mTitleView;
    @ID(id = R.id.progressBar)
    private ProgressBar mProgressBar;
    private LinkedList<CharSequence> mTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewInject.init(this);
        mTitles = new LinkedList<>();
        final Resources resources = getResources();
        drawerArrowDrawable = new DrawerArrowDrawable(resources);
        drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.light_gray));
        final ImageView imageView = (ImageView) findViewById(R.id.drawer_indicator);
        imageView.setImageDrawable(drawerArrowDrawable);

        SparseArray<ArrayList<ThemeValue>> read = new ThemeReader(R.layout.activity_main).read();

        Loger.i("style:" + read);

        mDrawer.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                offset = slideOffset;

                if (slideOffset >= .995) {
                    flipped = true;
                    drawerArrowDrawable.setFlip(flipped);
                } else if (slideOffset <= .005) {
                    flipped = false;
                    drawerArrowDrawable.setFlip(flipped);
                }

                drawerArrowDrawable.setParameter(offset);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawer.isDrawerVisible(LEFT)) {
                    mDrawer.closeDrawer(LEFT);
                } else {
                    mDrawer.openDrawer(LEFT);
                }
            }
        });

        final TextView styleButton = (TextView) findViewById(R.id.indicator_style);
        styleButton.setOnClickListener(new View.OnClickListener() {
            boolean rounded = false;

            @Override
            public void onClick(View v) {
                rounded = !rounded;

                drawerArrowDrawable = new DrawerArrowDrawable(resources, rounded);
                drawerArrowDrawable.setParameter(offset);
                drawerArrowDrawable.setFlip(flipped);
                drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.light_gray));

                imageView.setImageDrawable(drawerArrowDrawable);
            }
        });
        initItems();
    }

    private void initItems() {
        final HashMap<String, UIConfig> uiConfig = ConfigManager.getUIConfig();
        final int version = PrefernceUtils.getInt(ConfigName.ITEM_VERSION);
        final int newVersion = PrefernceUtils.getInt(ConfigName.NEW_VERSION);
        mProgressBar.setVisibility(version < newVersion ? View.VISIBLE : View.GONE);
        HandleTask.run(new TaskAction<Void>() {
            @Override
            public Void run() {
                if (null != uiConfig) {
                    ContentValues values = null;
                    ContentResolver resolver = getContentResolver();
                    if (version < newVersion) {
                        for (Map.Entry<String, UIConfig> entry : uiConfig.entrySet()) {
                            UIConfig config = entry.getValue();
                            values = config.getContentValues();
                            if (newVersion == config.version) {
                                resolver.insert(DbTable._URI, values);
                                if (null != config.items && !config.items.isEmpty()) {
                                    for (UIItem item : config.items) {
                                        values = item.getContentValues();
                                        resolver.insert(DbTable._URI, values);
                                    }
                                }
                            }
                        }
                        PrefernceUtils.setInt(ConfigName.ITEM_VERSION, newVersion);
                    }
                }
                return null;
            }

            @Override
            public void postRun(Void t) {
                PrefernceUtils.setBoolean(ConfigName.IS_INIT, true);
                mProgressBar.setVisibility(View.GONE);
                initContentView();
            }
        });
    }

    private void initContentView() {
        FragmentUtils.toFragment(MainActivity.this, new NavigationDrawerFragment(), R.id.drawer_content);
        FragmentUtils.toFragment(MainActivity.this, FragmentFunctionList.newInstance(0), R.id.view_content);
    }

    @Override
    public void onItemClick(int position, String title) {
        mDrawer.closeDrawer(Gravity.LEFT);
        mTitleView.setText(title);
        mTitles.clear();
        mTitles.add(title);
        // 弹出所有fragment
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onTitleChange(String title) {
        mTitles.add(mTitleView.getText());
        mTitleView.setText(title);
    }

    @Override
    public void onDetach() {
        if (null != mTitles && !mTitles.isEmpty()) {
            mTitleView.setText(mTitles.pollLast());
        }
    }
}
