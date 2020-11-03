package com.la.myai.ai;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.la.myai.R;
import com.la.myai.ai.emotion.EmotionFragment;
import com.la.myai.ai.mobile_detect.MobileDetectFragment;
import com.la.myai.ai.mobile_emotion.MobileEmotionFragment;
import com.la.myai.ai.nsfw.NSFWFragment;
import com.la.myai.ai.object_detect.ObjectDetectFragment;
import com.la.myai.myai.ViewPagerAdapter;

import java.util.ArrayList;

public class AIFragment extends Fragment {
    private View view;
    private Context context;
    private Activity activity;

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ArrayList<Fragment> fragments;
    private String[] titles ={
            "1",
            "2",
            "3",
            "4",
            "5"
    };


    public AIFragment(Object o){

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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    FragmentManager fragmentManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ai_fragment, container);
        viewPager2 = view.findViewById(R.id.ai_view_pager);
        tabLayout = view.findViewById(R.id.ai_tab_layout);
        fragmentManager = getChildFragmentManager(); ///  非常关键 ！！！ 不然嵌套fragment切换的时候会页面丢失

        fragments = new ArrayList<>();
        fragments.add(new ObjectDetectFragment(null));
        //fragments.add(new EmotionFragment(null));
        fragments.add(new NSFWFragment(null));

        fragments.add(new EmotionFragment(null));

        fragments.add(new MobileDetectFragment(null));

        fragments.add(new MobileEmotionFragment(null));

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter((FragmentActivity) activity);

        viewPagerAdapter.addFragments(fragments);

        viewPager2.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titles[position]);
            }
        }).attach();
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_launcher_background).setText("tflite物体检测");
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_launcher_background).setText("色情图片检测");
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_launcher_background).setText("情感检测");
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_launcher_background).setText("tfmobile物体检测");
        tabLayout.getTabAt(4).setIcon(R.drawable.ic_launcher_background).setText("tfmobile情绪检测");

        // tabLayout.getTabAt(1).setIcon(R.drawable.ic_launcher_background).setText("RxJava功能测试");


        return view;
    }
}

