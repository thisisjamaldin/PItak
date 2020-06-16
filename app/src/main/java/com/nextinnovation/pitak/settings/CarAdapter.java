package com.nextinnovation.pitak.settings;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nextinnovation.pitak.R;
import com.nextinnovation.pitak.model.user.UserCar;

import java.util.ArrayList;
import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ViewHolder> {

    private List<UserCar> list = new ArrayList<>();
    private onItemClick itemClick;

    public CarAdapter(onItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public void addList(List<UserCar> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void addItem(UserCar item) {
        this.list.add(item);
        notifyDataSetChanged();
    }

    public List<UserCar> getList() {
        return this.list;
    }

    @NonNull
    @Override
    public CarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car, parent, false);
        return new CarAdapter.ViewHolder(view, itemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull CarAdapter.ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView mark, number, model, type;

        public ViewHolder(@NonNull final View itemView, final onItemClick itemClick) {
            super(itemView);

            image = itemView.findViewById(R.id.item_car_image);
            mark = itemView.findViewById(R.id.item_car_mark);
            number = itemView.findViewById(R.id.item_car_number);
            model = itemView.findViewById(R.id.item_car_model);
            type = itemView.findViewById(R.id.item_car_type);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick.onClick(getAdapterPosition());
                }
            });
        }

        void bind(UserCar car) {
            Context context = image.getContext();
            if (car.getCarFiles().isEmpty()) {
                Glide.with(context).load(R.drawable.bg_upload_image).centerCrop().into(image);
            } else if (car.getCarFiles().get(0).getContent() == null) {
                Glide.with(context).load(R.drawable.bg_upload_image).centerCrop().into(image);
            } else {
                Glide.with(context).load(setImage(car.getCarFiles().get(0).getContent())).centerCrop().into(image);
            }
            mark.setText(car.getCarBrand().

                    getName());
            model.setText(car.getCarModel().

                    getName());
            number.setText(car.getCarNumber());
            type.setText(car.getCarType().

                    getName());
        }
    }

    private Bitmap setImage(String encoded) {
        byte[] decodedString = Base64.decode(encoded, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public interface onItemClick {
        void onClick(int pos);
    }
}