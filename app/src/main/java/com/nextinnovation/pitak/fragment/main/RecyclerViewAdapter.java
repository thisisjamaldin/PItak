package com.nextinnovation.pitak.fragment.main;

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
import com.nextinnovation.pitak.item_detail.ItemDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView profile;
        private ImageView save;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.item_main_image);
            save = itemView.findViewById(R.id.item_main_save);
            save.setTag(0);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ((int) save.getTag() == 0) {
                        save.setImageDrawable(save.getContext().getResources().getDrawable(R.drawable.ic_save_checked));
                        save.setTag(1);
                    } else {
                        save.setImageDrawable(save.getContext().getResources().getDrawable(R.drawable.ic_save));
                        save.setTag(0);
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ItemDetailActivity.start(itemView.getContext());
                }
            });
        }

        void bind(String s) {
            if (saved) {
                save.setImageDrawable(save.getContext().getResources().getDrawable(R.drawable.ic_save_checked));
                save.setTag(1);
            }
            Glide.with(profile.getContext()).load(R.drawable.launch_screen).transform(new CenterCrop(), new RoundedCorners(36)).into(profile);
        }
    }
}
