package com.la.mymaterial.material.network_material;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.la.mymaterial.R;

public class NetworkMaterialFragment extends Fragment {
    private View view;
    private Context context;
    private Activity activity;
    private RecyclerView materials;

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
        view = inflater.inflate(R.layout.material_network_fragment, null);
        materials = view.findViewById(R.id.material_network_recycler_view);
        setMaterialsLayout();
        return view;
    }

    private void setMaterialsLayout(){
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        MaterialsAdapter materialsAdapter = new MaterialsAdapter();
        materials.setLayoutManager(layoutManager);
        materials.setAdapter(materialsAdapter);
    }


    class MaterialsAdapter extends RecyclerView.Adapter<MaterialHolder>{

        @NonNull
        @Override
        public MaterialHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MaterialHolder(LayoutInflater.from(context).inflate(R.layout.material_item, null));
        }

        @Override
        public void onBindViewHolder(@NonNull MaterialHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }

    class MaterialHolder extends RecyclerView.ViewHolder{

        public MaterialHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
