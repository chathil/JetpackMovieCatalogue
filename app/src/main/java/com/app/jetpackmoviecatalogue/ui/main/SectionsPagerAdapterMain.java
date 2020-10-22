package com.app.jetpackmoviecatalogue.ui.main;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.jetbrains.annotations.NotNull;

public class SectionsPagerAdapterMain extends FragmentStatePagerAdapter {

    public SectionsPagerAdapterMain(FragmentManager fm){
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        if (position < 2)
            return PlaceholderFragmentMain.newInstance(position + 1);
        else
            return FavContainerFragment.newInstance();
    }

    @Override
    public int getCount() {
        return 3;
    }


}