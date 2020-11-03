package com.la.mymaterial.material.record_material;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.la.mymaterial.R;

public class ProcessMaterialFragment extends Fragment implements ProcessMaterial.View{
    private ProcessMaterial.Callback callback;
    private View view;
    private Context context;
    private Activity activity;
    private RecyclerView videoTabs;
    private RecyclerView audioTabs;
    private RecyclerView imageTabs;
    private RecyclerView textTabs;

    private String[] videoLabels = new String[]{
        "视频转码","视频剪切","视频拼接","视频截图","视频加水印","视频转GIF", "图片合成视频","画面拼接","视频倒播","视频降噪"
    };

    private String[] audioLabels = new String[]{
        "音频转码","音频剪切","音频合并","音频编码","音频混合","PCM合并","PCM播放"
    };

    private String[] imageLabels = new String[]{
        "模糊图片修复",""
    };

    private String[] textLabels = new String[]{
        "文字生成语音",""
    };

    public ProcessMaterialFragment(ProcessMaterial.Callback callback){
        this.callback = callback;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context =context;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.material_process_fragment, null);
        videoTabs = view.findViewById(R.id.material_process_video_tabs);
        audioTabs = view.findViewById(R.id.material_process_audio_tabs);
        imageTabs = view.findViewById(R.id.material_process_image_tabs);
        textTabs = view.findViewById(R.id.material_process_text_tabs);

        setTabsLayout(videoTabs, videoLabels);
        setTabsLayout(audioTabs, audioLabels);
        setTabsLayout(imageTabs, imageLabels);
        setTabsLayout(textTabs, textLabels);
        return view;
    }

    private void setTabsLayout(RecyclerView tabs, String[] labels){
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL);
        //SmartItemDecoration itemDecoration = new SmartItemDecoration(context);
        TabsAdapter adapter = new TabsAdapter(labels);
        tabs.setLayoutManager(layoutManager);
       // tabs.addItemDecoration(itemDecoration);
        tabs.setAdapter(adapter);
    }

    class TabsAdapter extends RecyclerView.Adapter<TabsHolder>{
        String[] labels;
        public TabsAdapter(String[] lables){
            this.labels = lables;
        }

        @NonNull
        @Override
        public TabsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new TabsHolder(LayoutInflater.from(context).inflate(R.layout.material_process_tab_item,null));
        }

        @Override
        public void onBindViewHolder(@NonNull TabsHolder holder, int position) {
            holder.labelBtn.setText(labels[position]);
        }

        @Override
        public int getItemCount() {
            return labels.length;
        }
    }

    class TabsHolder extends RecyclerView.ViewHolder{
        private Button labelBtn;

        public TabsHolder(@NonNull View itemView) {
            super(itemView);
            labelBtn = itemView.findViewById(R.id.tab_item_btn);
        }
    }

}
