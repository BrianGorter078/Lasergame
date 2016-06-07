package com.briang.lasergame;

import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import com.briang.lasergame.Connections.AsyncResponse;
import com.briang.lasergame.Connections.OkHttpGet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements AsyncResponse {

    OkHttpGet okHttpGet = new OkHttpGet();

    @BindView(R.id.toolbar)
    Toolbar mActionBarToolbar;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.toolbar_title)
    TextView title;

    private int[] tabIcons = {
            R.drawable.ic_home,
            R.drawable.ic_settings
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        String deviceId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);


        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);

        setupViewPager(viewPager);


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(onTabSelectedListener());
        setupTabIcons();

        okHttpGet.delegate = this;
        getRequest("http://laser-web.herokuapp.com/game");


        System.out.println("UID is : " + deviceId);
    }


    /**
     * This overrides the implemented method from asyncTask
     * This method gets the response from the asyncTask
     *
     * @param output
     */
    public void processFinish(String output){
        //Here you will receive the result fired from async class
        //of onPostExecute(result) method.
        Log.d("Response" , output);

    }

    /**
     * Executes a http getRequest from the requested URl
     * @param url
     */
    public void getRequest(String url)
    {
        okHttpGet.execute(url);
    }
    /**
     * Listener for Tabs underneath the status bar
     *
     * @return onTabSelectedListener
     */
    private TabLayout.OnTabSelectedListener onTabSelectedListener() {

        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0){
                    title.setText("Home");
                } else {
                    title.setText("Stats");

                }
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Home(), "Home");
        adapter.addFragment(new Stats(), "Stats");
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
    }




    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }


        @Override
        public Fragment getItem(int position) {
            System.out.println(position);
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String t) {
            mFragmentList.add(fragment);
        }

    }


}
