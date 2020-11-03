package com.la.mymaterial.material;

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
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.la.mymaterial.R;
import com.la.mymaterial.material.native_material.NativeMaterialFragment;
import com.la.mymaterial.material.network_material.NetworkMaterialFragment;
import com.la.mymaterial.material.record_material.ProcessMaterial;
import com.la.mymaterial.material.record_material.ProcessMaterialFragment;

import java.util.ArrayList;

public class MaterialFragment extends Fragment implements Material.View, ProcessMaterial.Callback{
private Context context;
private Activity activity;
private Material.Callback callback;
private View view;
private TabLayout tabLayout;
private ViewPager viewPager;
private ArrayList<Fragment> fragments;

    public MaterialFragment(Material.Callback callback){

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
        view = inflater.inflate(R.layout.material_fragment,null);
        tabLayout = view.findViewById(R.id.material_my_tab_layout);
        viewPager = view.findViewById(R.id.material_my_view_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(((AppCompatActivity)activity).getSupportFragmentManager(),
                ViewPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        fragments = new ArrayList<>();
        fragments.add(new NetworkMaterialFragment());
        fragments.add(new NativeMaterialFragment());
        fragments.add(new ProcessMaterialFragment(this));

        viewPagerAdapter.addFragments(fragments);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager); // 该方法会移除所有tab， 需要先addFragment
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_launcher_background);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_launcher_background);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_launcher_background);

        return view;
    }
}
