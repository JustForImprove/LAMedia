package com.la.customview.SmartViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.la.customview.R;

public class SmartViewPagerFragment extends Fragment {

private SmartViewPager smartViewPager;


    private View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.smart_view_pager_fragment, null);
        smartViewPager = view.findViewById(R.id.custom_smart_view_pager);

        return view;
    }


}
