package com.app.jetpackmoviecatalogue.ui.main;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.databinding.DataBindingUtil;

import com.app.jetpackmoviecatalogue.R;
import com.app.jetpackmoviecatalogue.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setSupportActionBar(binding.bar);
        SectionsPagerAdapterMain sectionsPagerAdapter = new SectionsPagerAdapterMain(getSupportFragmentManager());
        binding.viewPager.setAdapter(sectionsPagerAdapter);
        binding.viewPager.setOffscreenPageLimit(2);
        binding.tabs.setupWithViewPager(binding.viewPager);

        Objects.requireNonNull(binding.tabs.getTabAt(0)).setIcon(getResources().getDrawable(R.drawable.ic_movie_selected_24dp));
        Objects.requireNonNull(binding.tabs.getTabAt(1)).setIcon(getResources().getDrawable(R.drawable.ic_tv_black_24dp));
        Objects.requireNonNull(binding.tabs.getTabAt(2)).setIcon(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));

    }
}
