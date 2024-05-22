package com.example.navigationbottom.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.navigationbottom.R;
import com.example.navigationbottom.adaper.MyViewPagerAdapter;
import com.example.navigationbottom.fragment.SellFragment;
import com.example.navigationbottom.model.User;
import com.example.navigationbottom.viewmodel.WebSocketManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity {

    private ViewPager2 mViewPager2;
    private BottomNavigationView mbottomNavigationView;
    private WebSocketManager webSocketManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webSocketManager = WebSocketManager.getInstance();
        webSocketManager.connect();

        mViewPager2 = findViewById(R.id.view_pager_2);
        mbottomNavigationView = findViewById(R.id.bottom_navigation);



        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(this);
        mViewPager2.setAdapter(myViewPagerAdapter);

        mbottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                if(id == R.id.bottom_home){
                    mViewPager2.setCurrentItem(0);
                }
                else if(id == R.id.bottom_cart){
                    mViewPager2.setCurrentItem(1);
                }
                else if(id == R.id.bottom_Sell){
                    mViewPager2.setCurrentItem(2);
                }
                else if(id == R.id.bottom_notifications){
                    mViewPager2.setCurrentItem(3);
                }
                else if(id == R.id.bottom_profile){
                    mViewPager2.setCurrentItem(4);
                }
                return true;
            }
        });

        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                switch (position){
                    case 0:
                        mbottomNavigationView.getMenu().findItem(R.id.bottom_home).setChecked(true);

                        break;
                    case 1:
                        mbottomNavigationView.getMenu().findItem(R.id.bottom_cart).setChecked(true);

                        break;
                    case 2:
                        mbottomNavigationView.getMenu().findItem(R.id.bottom_Sell).setChecked(true);

                        break;
                    case 3:
                        mbottomNavigationView.getMenu().findItem(R.id.bottom_notifications).setChecked(true);

                        break;
                    case 4:
                        mbottomNavigationView.getMenu().findItem(R.id.bottom_profile).setChecked(true);

                        break;
                }

            }
        });

        // Check for the intent data and navigate to the correct fragment
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("dataFromActivity")) {
            String fragmentName = intent.getStringExtra("dataFromActivity");
            if ("fromAddBook".equals(fragmentName) || "fromEditBook".equals(fragmentName)) {
                mViewPager2.setCurrentItem(2); // Navigate to the SellFragment page
            }else if("fromDetailHome".equals(fragmentName) || "fromDetailCart".equals(fragmentName)){
                mViewPager2.setCurrentItem(1);
            }
        }
    }
}