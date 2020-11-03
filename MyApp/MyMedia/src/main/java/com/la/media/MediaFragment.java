package com.la.media;

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
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.la.media.Live.Live;
import com.la.media.Live.LiveFragment;
import com.la.media.Camera.CameraProcess;
import com.la.media.Camera.CameraProcessFragment;
import com.la.media.VideoProcess.VideoProcess;
import com.la.media.VideoProcess.VideoProcessFragment;
import com.la.mymedia.R;

import java.util.ArrayList;

public class MediaFragment extends Fragment implements VideoProcess.Callback , Live.Callback, CameraProcess.Callback {

    private Context context;
    private Activity activity;
    private Media.Callback callback;
    private View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;

    public MediaFragment(Media.Callback callback){

        this.callback = callback;
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
        view = inflater.inflate(R.layout.my_media_fragment,null);
        tabLayout = view.findViewById(R.id.media_tab_layout);
        viewPager = view.findViewById(R.id.media_view_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter( getChildFragmentManager(),
                ViewPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        fragments = new ArrayList<>();
       //添加分页
        fragments.add(new VideoProcessFragment(this));
        fragments.add(new LiveFragment(this));
        fragments.add(new CameraProcessFragment(this));

        viewPagerAdapter.addFragments(fragments);

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager); // 该方法会移除所有tab， 需要先addFragment
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_launcher_background).setText("音视频处理");
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_launcher_background).setText("直播处理");
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_launcher_background).setText("滤镜相机");

        return view;
    }
}
