package com.la.customview.SmartRecyclerViewLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.la.customview.LayoutManagerTestActivity;
import com.la.customview.R;

public class LayoutManagerFragment extends Fragment {

    private RecyclerView recyclerView;
    private View view;
    private Context context;
    private Activity activity;
    private Drawable[] drawable = new Drawable[100000];

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
        view = inflater.inflate(R.layout.layout_manager_fragment, null);
        recyclerView = view.findViewById(R.id.layout_manager_recycler_view);
        Path path = new Path();
        path.moveTo(250,250);
        path.rLineTo(600,300);// 以当前位置为起点加上dx， dy
        path.rLineTo(-600,300);
        path.rLineTo(600,300);
        path.rLineTo(-600,300);
        recyclerView.setLayoutManager(new MyLayoutManager(path, 150, RecyclerView.VERTICAL, new float[]{0f, 0f, 1f, 0.5f, 0f, 1f}));

        recyclerView.setAdapter(new Adapter());

        return view;
    }

    class Adapter extends RecyclerView.Adapter<LayoutManagerFragment.ViewHolder>{

        @NonNull
        @Override
        public LayoutManagerFragment.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item, null));
        }

        @Override
        public void onBindViewHolder(@NonNull LayoutManagerFragment.ViewHolder holder, int position) {
            if (holder != null) {
                holder.textView.setText(String.valueOf(position));
            }
        }

        @Override
        public int getItemCount() {
            return drawable.length;
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}
