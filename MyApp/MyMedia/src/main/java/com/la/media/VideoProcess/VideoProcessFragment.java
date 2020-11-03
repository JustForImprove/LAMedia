package com.la.media.VideoProcess;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.la.media.Player.PlayerFragment;
import com.la.mymedia.R;
public class VideoProcessFragment extends Fragment implements VideoProcess.View {
    private Context context;
    private Activity activity;
    private VideoProcess.Callback callback;
    private View view;
    private EditText src;
    private EditText src2;
    private EditText output;
    private Button src_open;
    private Button src2_open;
    private Button output_open;
    private VideoProcess.Presenter presenter;

    private FrameLayout frameLayout;
    private RecyclerView functions;
    private Button playSrc;
    private Button playSrc2;
    private Button playOutput;
    private ProgressBar progressBar;

    // 标签
    String[] labels = new String[]{
            "视频转码",
            "视频剪切",
            "视频拼接",
            "视频截图",
            "视频水印",
            "视频转GIF",
            "视频倒播",
            "视频降噪",
            "视频加速",
            "提取视频",
            "提取音频",
            "音视频合成",
            "音频转码",
            "音频编码",
            "格式解析",
    };
    String src_path = "/storage/4925-382B/1234.mp4";
    String src2_path = "/storage/4925-382B/1234.mp4";
    String output_path = "/storage/4925-382B/1234.mp4";

    public VideoProcessFragment(VideoProcess.Callback callback){

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
        view = inflater.inflate(R.layout.video_process_fragment,null);
        presenter = new VideoProcessPresenter(context, activity, this);

        src = view.findViewById(R.id.video_process_src);
        src2 = view.findViewById(R.id.video_process_src2);
        output = view.findViewById(R.id.video_process_output);

        playSrc = view.findViewById(R.id.video_process_src_play);
        playSrc2 = view.findViewById(R.id.video_process_src2_play);
        playOutput = view.findViewById(R.id.video_process_output_play);

        src_open = view.findViewById(R.id.video_process_src_open);
        src2_open = view.findViewById(R.id.video_process_src2_open);
        output_open = view.findViewById(R.id.video_process_output_open);

        frameLayout = view.findViewById(R.id.video_process_player_fragment);
        progressBar = view.findViewById(R.id.video_process_progress);

        PlayerFragment playerFragment = new PlayerFragment(null);
        setFragment(playerFragment);

        functions = view.findViewById(R.id.video_process_function_list);
        setFunctionsLayout();



        src_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // src_path = presenter.selectFile();
                 src.setText(src_path);
            }
        });


        src2_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               src2.setText(src2_path);
            }
        });

        output_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                output.setText(output_path);
            }
        });

        playSrc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playerFragment.setPlayUrl(String.valueOf(src.getText()));
            }
        });

        playSrc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerFragment.setPlayUrl(String.valueOf(src2.getText()));
            }
        });

        playOutput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerFragment.setPlayUrl(String.valueOf(output.getText()));
            }
        });

        return view;
    }
    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((AppCompatActivity)activity).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =  fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.video_process_player_fragment,fragment);
        fragmentTransaction.commit();
    }

    private void setFunctionsLayout(){
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL);
        FunctionsAdapter functionsAdapter = new FunctionsAdapter();
        functions.setLayoutManager(layoutManager);
        functions.setAdapter(functionsAdapter);
    }

    @Override
    public void processBegin() {
        progressBar.setVisibility(View.VISIBLE);
        functions.setVisibility(View.GONE);
    }

    @Override
    public void processContinued(int progress) {
        progressBar.setProgress(progress);
    }

    @Override
    public void processFinished() {
        progressBar.setVisibility(View.GONE);
        functions.setVisibility(View.VISIBLE);
        Toast.makeText(context, "操作完成", Toast.LENGTH_SHORT).show();
    }

    class FunctionsAdapter extends RecyclerView.Adapter<FunctionViewHolder>{

        @NonNull
        @Override
        public FunctionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new FunctionViewHolder(LayoutInflater.from(context).inflate(R.layout.video_process_function_item,null));
        }

        @Override
        public void onBindViewHolder(@NonNull FunctionViewHolder holder, int position) {
            holder.function_btn.setText(labels[position]);
            holder.function_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button button = (Button)v;
                   // presenter.onFunctionItemClick(labels, (String) button.getText(), position);
                    //
                    presenter.onFunctionItemClick(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return labels.length;
        }
    }

    class FunctionViewHolder extends RecyclerView.ViewHolder{
        private Button function_btn;

        public FunctionViewHolder(@NonNull View itemView) {
            super(itemView);
            function_btn = itemView.findViewById(R.id.video_process_function_item_btn);
        }
    }
}
