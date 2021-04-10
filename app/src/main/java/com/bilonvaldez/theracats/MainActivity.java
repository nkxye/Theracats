package com.bilonvaldez.theracats;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public ImagePopup imagePopup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Picasso.setSingletonInstance(new Picasso.Builder(this).build());
        imagePopup = new ImagePopup(this);

        Toolbar navbar = findViewById(R.id.navbar);
        setSupportActionBar(navbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Theracats Virtual Clinic");

        CatRandomFragment catFragment = new CatRandomFragment();
        SavedFragment savedFragment = new SavedFragment();

        ViewPager viewPager = findViewById(R.id.viewpager);
        TabLayout categoryTabs = findViewById(R.id.categories);

        categoryTabs.setupWithViewPager(viewPager);

        ViewPagerAdapter vpAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        vpAdapter.addFragment(catFragment, "Cute Hambs");
        vpAdapter.addFragment(savedFragment, "Hambs Kept");
        viewPager.setAdapter(vpAdapter);
    }

    private static class ViewPagerAdapter extends FragmentPagerAdapter {

        private final ArrayList<Fragment> fragments = new ArrayList<>();
        private final ArrayList<String> fragmentTitles = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitles.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }
    }

    public void enlargeImage(View v) {
        String image = v.findViewById(R.id.btnOpenPhoto).getTag().toString();
        imagePopup.initiatePopupWithPicasso(image);
        imagePopup.viewPopup();
    }
}