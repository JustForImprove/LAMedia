package com.la.media.Camera;

import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Magnifier;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.la.mymedia.R;
import com.la.mymedia.customview.SmartCameraView.SmartCameraView;
import com.la.mymedia.customview.SmartCameraView.filter.utils.MagicFilterType;

public class CameraProcessFragment extends Fragment implements SmartCameraView.PreviewCallback{
    private Context context;
    private View view;
    private SmartCameraView smartCameraView;
    private ImageButton photograph;
    private CheckBox record;
    private RecyclerView filters;
    private CameraProcess.Callback callback;
    private String[] filterLabels = new String[]{
            "原图",
            "健康",
            "美颜",
            "冷酷",
            "日出",
            "素描",
            "浪漫",
            //"童话", // 有bug
            "樱花" // 樱花

    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public CameraProcessFragment(CameraProcess.Callback callback){
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.recoeder_fragment, null);
        smartCameraView = view.findViewById(R.id.recorder_smart_camera);
        photograph = view.findViewById(R.id.recorder_photograph);
        record = view.findViewById(R.id.recorder_start_or_stop_record);
        filters = view.findViewById(R.id.recorder_filter);
        setFiltersLayoutParams();

        smartCameraView.setPreviewCallback(this);



        photograph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        record.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        smartCameraView.setPreviewResolution(1000,1000);
        smartCameraView.startCamera();
    }



    @Override
    public void onPause() {
        super.onPause();
        smartCameraView.stopCamera();
    }

    private void setFiltersLayoutParams(){
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        FiltersAdapter filtersAdapter = new FiltersAdapter();
        filters.setLayoutManager(layoutManager);
        filters.setAdapter(filtersAdapter);
    }

    @Override
    public void onGetRgbaFrame(byte[] data, int width, int height) {

    }

    class FiltersAdapter extends RecyclerView.Adapter<FilterViewHolder>{

        @NonNull
        @Override
        public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new FilterViewHolder(LayoutInflater.from(context).inflate(R.layout.recorder_filter_item,null));
        }

        @Override
        public void onBindViewHolder(@NonNull FilterViewHolder holder, int position) {
            holder.button.setText(filterLabels[position]);
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position){
                        case 0:
                            smartCameraView.setFilter(MagicFilterType.NONE);
                            break;
                        case 1:
                            smartCameraView.setFilter(MagicFilterType.HEALTHY);
                            break;
                        case 2:
                            smartCameraView.setFilter(MagicFilterType.BEAUTY);
                            break;
                        case 3:
                            smartCameraView.setFilter(MagicFilterType.COOL);
                            break;
                        case 4:
                            smartCameraView.setFilter(MagicFilterType.SUNRISE);
                            break;
                        case 5:
                            smartCameraView.setFilter(MagicFilterType.SKETCH);
                            break;
                        case 6:
                            smartCameraView.setFilter(MagicFilterType.ROMANCE);
                            break;
                        case 7:
                            smartCameraView.setFilter(MagicFilterType.SAKURA);
                            break;
                        default:
                            smartCameraView.setFilter(MagicFilterType.NONE);
                            break;

                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return filterLabels.length;
        }
    }

    class FilterViewHolder extends RecyclerView.ViewHolder{
        private Button button;
        public FilterViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.recorder_filter_item_filter);
        }
    }


}
