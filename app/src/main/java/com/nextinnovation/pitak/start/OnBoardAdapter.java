package com.nextinnovation.pitak.start;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

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
            img.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.im_on_board_1));
            title.setText("Lorem ipsum");
            desc.setText("Небольшой текст для туториала изображени подберу под текста");
        } else if (position==1){
            img.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.im_on_board_2));
            title.setText("Lorem ipsum");
            desc.setText("Небольшой текст для туториала изображени подберу под текста");
        }else if (position==2){
            img.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.im_on_board_3));
            title.setText("Lorem ipsum");
            desc.setText("Небольшой текст для туториала изображени подберу под текста");
        }
        container.addView(view);
        return view;
    }
}
