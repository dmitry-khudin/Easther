package com.tyvip.rewarding;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import com.squareup.picasso.Picasso;
import com.tyvip.rewarding.Fragments.BoxaFragment;
import com.tyvip.rewarding.Fragments.CardFragment;
import com.tyvip.rewarding.Fragments.FindResultFragment;
import com.tyvip.rewarding.Fragments.HomeFragment;
import com.tyvip.rewarding.Utils.Constants;
import com.tyvip.rewarding.Utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationBar.OnTabSelectedListener {

    private static BottomNavigationBar mBottomNavigationBar;
    private static HomeFragment homeFragment = null;
    private static CardFragment cardFragment = null;
    private static BoxaFragment boxaFragment = null;
    private static FindResultFragment findResultFragment = null;
    ImageButton imageButton1, imageButton2, imageButton3;
    View headerView;
    int tabIndex;
    boolean flag = false;
    ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tabIndex == 3)
                {
                    ReplaceFragment(0);
                }
                else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        int count = navigationView.getHeaderCount();

        headerView = navigationView.getHeaderView(0);
        final CircleImageView imageView = (CircleImageView) headerView.findViewById(R.id.imageView);
        TextView textView1 = (TextView) headerView.findViewById(R.id.textView1);
        TextView textView2 = (TextView) headerView.findViewById(R.id.textView2);

        imageView.setImageResource(R.drawable.ic_action_tab_boxa);
        String str = Util.GetStringFromReference(this, Constants.USER_DATA);
        try {
            JSONObject jsonObject = new JSONObject(str);
            textView1.setText(jsonObject.getString("fullname"));
            String imageURL = Constants.GETIMAGE_URL + jsonObject.getInt("userbid") + "/uid/" + jsonObject.getInt("userid") + ".png";
            final String placeHolder = Constants.GETIMAGE_URL + jsonObject.getInt("userbid") + "/uid/default.png";
            Picasso.Builder builder = new Picasso.Builder(this);
            builder.listener(new Picasso.Listener()
            {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    Picasso.with(MainActivity.this).load(placeHolder).error(R.drawable.app_logo).into(imageView);
                }
            });
            builder.build().load(imageURL).into(imageView);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("header", "" + count);
        navigationView.setNavigationItemSelectedListener(this);
        initview();
    }

    void init_toolbar(boolean ff)
    {
        if (ff == true)
        {
            toggle.setDrawerIndicatorEnabled(true);
        }
        else
        {
            toggle.setDrawerIndicatorEnabled(false);

            toggle.setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (tabIndex == 0)
            {
                finish();
            }
            super.onBackPressed();

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home)
        {
            Toast.makeText(this, "home", Toast.LENGTH_SHORT).show();
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            imageButton1.setColorFilter(Color.argb(255, 0x47, 0x89, 0x1E));
            imageButton2.setColorFilter(Color.argb(255, 255, 255, 255));
            imageButton3.setColorFilter(Color.argb(255, 255, 255, 255));
            ReplaceFragment(0);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {
            SignOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void SignOut() {
        Util.SaveStringToReference(this, "", Constants.USER_DATA);
        Util.SaveStringToReference(this, "", Constants.LOGINED);
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    private void initview() {
        /*
        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_bar);
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar
                .setBarBackgroundColor((R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.drawable.ic_action_tab_home, "").setActiveColor("#47891E")
                        .setInActiveColor(R.color.colorwhite))
                .addItem(new BottomNavigationItem(R.drawable.ic_action_tab_boxa, "").setActiveColor("#47891E")
                        .setInActiveColor(R.color.colorwhite))
                .addItem(new BottomNavigationItem(R.drawable.ic_action_tab_card, "").setActiveColor("#47891E")
                        .setInActiveColor(R.color.colorwhite))
                .initialise();

        mBottomNavigationBar.setTabSelectedListener(this); */

        imageButton1 = (ImageButton) findViewById(R.id.imageButton3);
        imageButton2 = (ImageButton) findViewById(R.id.imageButton4);
        imageButton3 = (ImageButton) findViewById(R.id.imageButton6);
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageButton1.setColorFilter(Color.argb(255, 0x47, 0x89, 0x1E));
                imageButton2.setColorFilter(Color.argb(255, 255, 255, 255));
                imageButton3.setColorFilter(Color.argb(255, 255, 255, 255));
                if (flag != false)
                {
                    Log.d("find", "find");
                    ReplaceFragment(3);
                }
                else
                    ReplaceFragment(0);
            }
        });
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageButton2.setColorFilter(Color.argb(255, 0x47, 0x89, 0x1E));
                imageButton1.setColorFilter(Color.argb(255, 255, 255, 255));
                imageButton3.setColorFilter(Color.argb(255, 255, 255, 255));
                ReplaceFragment(1);
            }
        });
        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageButton3.setColorFilter(Color.argb(255, 0x47, 0x89, 0x1E));
                imageButton2.setColorFilter(Color.argb(255, 255, 255, 255));
                imageButton1.setColorFilter(Color.argb(255, 255, 255, 255));
                ReplaceFragment(2);
            }
        });
//        47891E

        setDefaultFragment();
    }

    public void setDefaultFragment()
    {
        imageButton1.setColorFilter(Color.argb(255, 0x47, 0x89, 0x1E));
        imageButton2.setColorFilter(Color.argb(255, 255, 255, 255));
        imageButton3.setColorFilter(Color.argb(255, 255, 255, 255));
        if (homeFragment == null) homeFragment =  HomeFragment.newInstance("", "");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame, homeFragment);
        ft.addToBackStack(homeFragment.getClass().getName());
        ft.commit();
        setTitle("Home");
    }
    public void ReplaceFragment(int index)
    {

        tabIndex = index;
        switch (index)
        {
            case 0:
            {
                if (homeFragment == null)    homeFragment =  HomeFragment.newInstance("", "");
                findResultFragment = null;
                flag = false;
                init_toolbar(true);
                replaceFrag(homeFragment);
                setTitle("Home");
                break;
            }
            case 1:
            {
                init_toolbar(true);
                if (boxaFragment == null)      boxaFragment = BoxaFragment.newInstance("", "")    ;
                findResultFragment = null;
                replaceFrag(boxaFragment);
                setTitle("BOXA");
                break;
            }
            case 2:
            {
                init_toolbar(true);
                if (cardFragment == null)      cardFragment = CardFragment.newInstance("", "")      ;
                replaceFrag(cardFragment);
                setTitle("MY CARD");
                break;
            }
            case 3:
            {
                init_toolbar(false);
                if (findResultFragment == null)
                {
                    findResultFragment = FindResultFragment.newInstance("", "");
                    Log.d("find", "find_create");
                }

                flag = true;
                replaceFrag(findResultFragment);
                setTitle("Result");
                break;
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
    public void BackPress()
    {
        finish();
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

//        if (!fragmentPopped)
        {
            //fragment not in back stack, create it.

            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.frame, fragment);
//            ft.addToBackStack(backStateName);
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
