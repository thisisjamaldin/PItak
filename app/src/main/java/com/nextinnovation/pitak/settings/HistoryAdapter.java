package com.nextinnovation.pitak.settings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.nextinnovation.pitak.R;

import java.util.ArrayList;
import java.util.List;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    List<String> list = new ArrayList<>();
    private boolean saved;

    public void setList(List<String> list, boolean saved) {
        this.list.clear();
        this.list.addAll(list);
        this.saved = saved;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
        return new HistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView profile;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.item_main_image);
            itemView.findViewById(R.id.item_main_save).setVisibility(View.GONE);
        }

        void bind(String s) {
            Glide.with(profile.getContext()).load(R.drawable.launch_screen).transform(new CenterCrop(), new RoundedCorners(36)).into(profile);
        }
    }
}
