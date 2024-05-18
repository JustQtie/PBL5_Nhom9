package com.example.navigationbottom.adaper;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.navigationbottom.fragment.CartFragment;
import com.example.navigationbottom.fragment.HomeFragment;
import com.example.navigationbottom.fragment.NotificationsFragment;
import com.example.navigationbottom.fragment.ProfileFragment;
import com.example.navigationbottom.fragment.SellFragment;

public class MyViewPagerAdapter extends FragmentStateAdapter {

    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new CartFragment();
            case 2:
                return new SellFragment();
            case 3:
                return new NotificationsFragment();
            case 4:
                return new ProfileFragment();
            default:
                return new HomeFragment();
        }

    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
