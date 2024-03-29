package com.nextinnovation.pitak.fragment.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
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
import com.nextinnovation.pitak.R;
import com.nextinnovation.pitak.model.post.AppAdvertModel;
import com.nextinnovation.pitak.model.post.Post;
import com.nextinnovation.pitak.utils.Statics;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<AppAdvertModel> list = new ArrayList<>();
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

    public void addList(List<AppAdvertModel> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public List<AppAdvertModel> getList() {
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

        private ImageView image;
        private ImageView save;
        private TextView fromPlace;
        private TextView toPlace;
        private TextView price;
        private TextView role;
        private TextView mark;
        private Button call, whatsapp;

        public ViewHolder(@NonNull final View itemView, final onItemClick itemClick) {
            super(itemView);
            mark = itemView.findViewById(R.id.item_main_mark);
            image = itemView.findViewById(R.id.item_main_image);
            fromPlace = itemView.findViewById(R.id.item_main_from);
            toPlace = itemView.findViewById(R.id.item_main_to_text);
            price = itemView.findViewById(R.id.item_main_price);
            role = itemView.findViewById(R.id.item_main_role);
            call = itemView.findViewById(R.id.item_main_call);
            whatsapp = itemView.findViewById(R.id.item_main_whatsapp);
            whatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick.openWhatsapp(getAdapterPosition());
                }
            });
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick.onCall(getAdapterPosition());
                }
            });
            save = itemView.findViewById(R.id.item_main_save);

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

        void bind(final AppAdvertModel appAdvertModel) {
            Post post = appAdvertModel.getAppAdvertModel();
            Context context = save.getContext();
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
                whatsapp.setVisibility(View.GONE);
            } else {
                save.setVisibility(View.VISIBLE);
                call.setVisibility(View.VISIBLE);
                whatsapp.setVisibility(View.VISIBLE);
            }
            if (appAdvertModel.getAttachmentModels().isEmpty()) {
                Glide.with(context).load(R.drawable.launch_screen).centerCrop().into(image);
            } else {
                Statics.loadImage(image, appAdvertModel.getAttachmentModels().get(0).getAppFile().getUrl(), false);
            }
            if (post.getCarCommonModel() != null) {
                mark.setText(post.getCarCommonModel().getCarBrand().getName());
            }
            fromPlace.setText(fromPlace.getContext().getResources().getString(R.string.from) + ": " + post.getFromPlace());
            toPlace.setText(fromPlace.getContext().getResources().getString(R.string.to) + ": " + post.getToPlace());
            price.setText(post.getAmountPayment() + " сом");
            if (post.getTypeService() == null) return;
            role.setText(post.getTypeService().getName());
        }
    }

    public interface onItemClick {
        void onCall(int pos);

        void onSave(int pos, boolean save);

        void onClick(int pos);

        void openWhatsapp(int pos);
    }

}
