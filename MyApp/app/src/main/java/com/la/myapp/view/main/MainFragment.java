package com.la.myapp.view.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.la.customview.CustomViewFragment;
import com.la.media.Media;
import com.la.media.MediaFragment;
import com.la.myai.ai.AIFragment;
import com.la.myai.ai.object_detect.ObjectDetectFragment;
import com.la.myapp.R;
import com.la.myapp.view.ViewPagerAdapter;
import java.util.ArrayList;



public class MainFragment extends Fragment implements Main.View, Media.Callback {

private View view;
private Context context;
private AppCompatActivity activity;
private ArrayList<Fragment> fragments;
private ViewPager2 viewPager2;
private BottomNavigationView bottomNavigationView;
private Main.MainCallback mainCallBack;
private int[] itemIds = {
        R.id.main_menu_item1,
        R.id.main_menu_item2,
        R.id.main_menu_item3,
        R.id.main_menu_item4,
        R.id.main_menu_item5
};

    public MainFragment(Main.MainCallback mainCallBack){
        this.mainCallBack = mainCallBack;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = (AppCompatActivity) activity;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate( R.layout.main_fragment, null);
        viewPager2 = view.findViewById(R.id.main_view_pager);
        bottomNavigationView = view.findViewById(R.id.main_nav_view);

        FragmentManager fragmentManager = activity.getSupportFragmentManager();

        fragments = new ArrayList<>();

        // 添加fragment
        MediaFragment mediaFragment = new MediaFragment(this);

        fragments.add(mediaFragment);
        fragments.add(new AIFragment(null));
        fragments.add(new CustomViewFragment(null));

        ViewPagerAdapter adapter = new ViewPagerAdapter(activity);

        //viewPagerAdapter.(fragments);
        adapter.addFragments(fragments);
        viewPager2.setAdapter(adapter);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                bottomNavigationView.setSelectedItemId(position);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.main_menu_item1:
                        viewPager2.setCurrentItem(0, true);
                        break;
                    case R.id.main_menu_item2:
                        viewPager2.setCurrentItem(1, true);
                        break;
                    case R.id.main_menu_item3:
                        viewPager2.setCurrentItem(2, true);
                        break;
                    case R.id.main_menu_item4:
                        viewPager2.setCurrentItem(3, true);
                        break;
                    case R.id.main_menu_item5:
                        viewPager2.setCurrentItem(4, true);
                        break;
                }
                return true;
            }
        });
        return view;
    }

}
