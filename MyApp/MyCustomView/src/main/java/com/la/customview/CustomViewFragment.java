package com.la.customview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.la.customview.SmartRecyclerViewLayoutManager.LayoutManagerFragment;
import com.la.customview.SmartViewPager.SmartViewPagerFragment;

import java.util.ArrayList;

public class CustomViewFragment extends Fragment {
    private Context context;
    private Activity activity;
    //private Media.Callback callback;
    private View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;

    public CustomViewFragment(Object o){

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.custom_view_fragment, null);
        viewPager = view.findViewById(R.id.custom_view_view_pager);
        tabLayout = view.findViewById(R.id.custom_view_tab_layout);
        FragmentManager fragmentManager = ((AppCompatActivity)activity).getSupportFragmentManager();
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(fragmentManager, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        fragments = new ArrayList<>();

        fragments.add(new LayoutManagerFragment());
        fragments.add(new SmartViewPagerFragment());
        viewPagerAdapter.addFragments(fragments);

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager); // 该方法会移除所有tab， 需要先addFragment
        tabLayout.setTabMode(TabLayout.MODE_FIXED);


        tabLayout.getTabAt(0).setIcon(R.drawable.ic_launcher_background).setText("自定义布局管理");
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_launcher_background).setText("自定义viewpager");
       // tabLayout.getTabAt(2).setIcon(R.drawable.ic_launcher_background).setText("自定义ItemDecoration");
        return view;
    }
}
