package com.o2games.playwin.android.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.o2games.playwin.android.fragment.HomeFragment;
import com.o2games.playwin.android.fragment.LeaderboardFragment;
import com.o2games.playwin.android.fragment.GameOfferFragment;
import com.o2games.playwin.android.fragment.ProfileFragment;
import com.o2games.playwin.android.fragment.WalletFragment;

public class MainFragmentAdapter extends FragmentStateAdapter {
    public MainFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new LeaderboardFragment();
            case 2:
                return new GameOfferFragment();
            case 3:
                return new WalletFragment();
            case 4:
                return new ProfileFragment();
//            case 4:
//                return new ContactUsFragment();
        }
        return new HomeFragment();
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
