package com.app.jetpackmoviecatalogue.ui.main;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.app.jetpackmoviecatalogue.R;
import com.app.jetpackmoviecatalogue.databinding.FragmentFavContainerBinding;
import com.app.jetpackmoviecatalogue.ui.main.favorite.SectionsPagerAdapterFav;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavContainerFragment extends Fragment {
    private FragmentFavContainerBinding binding;
    private final int MOVIE_SECTION = 0;
    private final int TV_SHOW_SECTION = 1;
    private static final String TAG = "FavContainerFragment";

    public static FavContainerFragment newInstance() {
        return new FavContainerFragment();
    }

    public FavContainerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavContainerBinding.inflate(inflater, container, false);
        SectionsPagerAdapterFav sectionsPagerAdapterFav = new SectionsPagerAdapterFav(getChildFragmentManager());
        binding.favPager.setAdapter(sectionsPagerAdapterFav);
        binding.favPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (binding.favPager.getCurrentItem() == MOVIE_SECTION) {
                    binding.subSectionMovies.setTextColor(getResources().getColor(R.color.textColorPrimary));
                    binding.subSectionTvshows.setTextColor(getResources().getColor(R.color.colorDisabled));
                } else {
                    binding.subSectionMovies.setTextColor(getResources().getColor(R.color.colorDisabled));
                    binding.subSectionTvshows.setTextColor(getResources().getColor(R.color.textColorPrimary));
                }
                Log.d(TAG, "onPageSelected: " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return binding.getRoot();
    }

}
