package com.nextinnovation.pitak.start;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.nextinnovation.pitak.R;

public class OnBoardAdapter extends PagerAdapter {

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.item_on_board, container, false);
        ImageView img = view.findViewById(R.id.item_on_board_img);
        TextView title = view.findViewById(R.id.item_on_board_title_tv);
        TextView desc = view.findViewById(R.id.item_on_board_desc_tv);
        if (position==0){
            Glide.with(img.getContext()).load(img.getContext().getResources().getDrawable(R.mipmap.on_board_one)).transform(new FitCenter(), new RoundedCorners(24)).into(img);
            title.setText("Размещайте Ваши посылки и грузы");
            desc.setText("Нужно срочно доставить посылку, документы, груз? Или купить и доставить нужную вещь? Теперь это сделать стало еще проще!");
        } else if (position==1){
            Glide.with(img.getContext()).load(img.getContext().getResources().getDrawable(R.mipmap.on_board_two)).transform(new FitCenter(), new RoundedCorners(24)).into(img);
            title.setText("Надежная доставка!");
            desc.setText("Pitak - это удобный и надежный сервис, для доставки любых грузов и посыл по территории Кыргызстана и Казахстана.");
        }else if (position==2){
            Glide.with(img.getContext()).load(img.getContext().getResources().getDrawable(R.mipmap.on_board_three)).transform(new FitCenter(), new RoundedCorners(24)).into(img);
            title.setText("Предоставление услуг спец. техники");
            desc.setText("Мы готовы сделать все возможное, чтобы обеспечить для Вас максимально комфортные условия доставки.");
        }
        container.addView(view);
        return view;
    }
}
