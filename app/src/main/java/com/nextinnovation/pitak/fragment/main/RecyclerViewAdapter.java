package com.nextinnovation.pitak.fragment.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.nextinnovation.pitak.R;
import com.nextinnovation.pitak.model.post.Post;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Post> list = new ArrayList<>();
    private boolean saved;
    private boolean mine;
    private onItemClick itemClick;

    public RecyclerViewAdapter(onItemClick itemClick, boolean saved, boolean mine) {
        this.itemClick = itemClick;
        this.saved = saved;
        this.mine = mine;
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public void addList(List<Post> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public List<Post> getList() {
        return this.list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
        return new ViewHolder(view, itemClick);
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
        private TextView name;
        private TextView fromPlace;
        private TextView toPlace;
        private TextView price;
        private Button call;

        public ViewHolder(@NonNull final View itemView, final onItemClick itemClick) {
            super(itemView);
            profile = itemView.findViewById(R.id.item_main_image);
            name = itemView.findViewById(R.id.item_main_name);
            fromPlace = itemView.findViewById(R.id.item_main_from);
            toPlace = itemView.findViewById(R.id.item_main_to);
            price = itemView.findViewById(R.id.item_main_price);
            call = itemView.findViewById(R.id.item_main_call);
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick.onCall(getAdapterPosition());
                }
            });
            save = itemView.findViewById(R.id.item_main_save);
            save.setTag(0);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ((int) save.getTag() == 0) {
                        save.setImageDrawable(save.getContext().getResources().getDrawable(R.drawable.ic_save_checked));
                        save.setTag(1);
                        itemClick.onSave(getAdapterPosition(), true);
                    } else {
                        save.setImageDrawable(save.getContext().getResources().getDrawable(R.drawable.ic_save));
                        save.setTag(0);
                        itemClick.onSave(getAdapterPosition(), false);
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClick.onClick(getAdapterPosition());
                }
            });
        }

        void bind(Post post) {
            if (saved || post.isFavorite()) {
                save.setImageDrawable(save.getContext().getResources().getDrawable(R.drawable.ic_save_checked));
                save.setTag(1);
            } else {
                save.setImageDrawable(save.getContext().getResources().getDrawable(R.drawable.ic_save));
                save.setTag(0);
            }
            if (mine) {
                save.setVisibility(View.GONE);
                call.setVisibility(View.GONE);
            } else {
                save.setVisibility(View.VISIBLE);
                call.setVisibility(View.VISIBLE);
            }
            if (post.getImgFileList().isEmpty()) {
                Glide.with(profile.getContext()).load(R.drawable.launch_screen).transform(new CenterCrop(), new RoundedCorners(36)).into(profile);
            } else {
                Glide.with(profile.getContext()).load(setImage(post.getImgFileList().get(0).getContent())).transform(new CenterCrop(), new RoundedCorners(36)).into(profile);
            }
            fromPlace.setText(fromPlace.getContext().getResources().getString(R.string.from) + " " + post.getFromPlace());
            toPlace.setText(post.getToPlace());
            price.setText(post.getAmountPayment() + " сом");
        }
    }

    private Bitmap setImage(String encoded) {
        byte[] decodedString = Base64.decode(encoded, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public interface onItemClick {
        void onCall(int pos);

        void onSave(int pos, boolean save);

        void onClick(int pos);
    }
}
