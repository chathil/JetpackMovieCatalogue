package com.app.jetpackmoviecatalogue.ui.main.favorite;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class SectionsPagerAdapterFav extends FragmentStatePagerAdapter {
    public SectionsPagerAdapterFav(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return FavFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
