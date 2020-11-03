package com.la.mymedia.customview;

import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.la.mymedia.R;
import com.la.mymedia.customview.SmartRecyclerViewLayoutManager.SmartLayoutManager;

public class LayoutManagerTestActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Drawable[] drawable = new Drawable[100000];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_activity);
        recyclerView = findViewById(R.id.recycler_view);
        Path path = new Path();
        path.moveTo(250,250);
        path.rLineTo(600,300);// 以当前位置为起点加上dx， dy
        path.rLineTo(-600,300);
        path.rLineTo(600,300);
        path.rLineTo(-600,300);

        recyclerView.setLayoutManager(new SmartLayoutManager(path, 150, RecyclerView.VERTICAL, new float[]{0f, 0f, 1f, 0.5f, 0f, 1f}));

        recyclerView.setAdapter(new Adapter());

    }



    class Adapter extends RecyclerView.Adapter<ViewHolder>{

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(LayoutManagerTestActivity.this).inflate(R.layout.item, null));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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