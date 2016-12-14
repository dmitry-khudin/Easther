package com.tyvip.rewarding;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import com.tyvip.rewarding.Fragments.HomeFragment;

public class HomeActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener{

    private BottomNavigationBar mBottomNavigationBar;
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initview();
//        ReplaceFragment(0);
    }

    private void initview() {
        AddFragment(0);
        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_bar);
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_action_tab_home, "").setActiveColor("#47891E")
        .setInActiveColor(R.color.colorwhite))
                .addItem(new BottomNavigationItem(R.drawable.ic_action_tab_boxa, "").setActiveColor("#47891E")
                        .setInActiveColor(R.color.colorwhite))
                .addItem(new BottomNavigationItem(R.drawable.ic_action_tab_card, "").setActiveColor("#47891E")
                        .setInActiveColor(R.color.colorwhite))
        .setFirstSelectedPosition(0)
        .initialise();
        mBottomNavigationBar.setTabSelectedListener(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.id_drawer_layout);


    }

    public void AddFragment(int index)
    {
        if (index == 0) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(R.id.frame, HomeFragment.newInstance("", ""));
            ft.commit();
        }
        else if (index == 1)
        {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(R.id.frame, HomeFragment.newInstance("", ""));
            ft.commit();
        }
    }
    public void ReplaceFragment(int index)
    {
        switch (index)
        {
            case 0:
            {
                replaceFrag(HomeFragment.newInstance("", ""));
                setTitle("Home");
            }
            case 1:
            {

            }
            case 2:
            {

            }
            default:
                break;
        }

    }

    public void setTitle(String str)
    {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(str);
    }
    public void BackToFragment()
    {
        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStack();
    }
    private void replaceFrag (Fragment fragment){
        String backStateName = fragment.getClass().getName();

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped){ //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.frame, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    @Override
    public void onTabSelected(int position) {
        ReplaceFragment(position);

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}
