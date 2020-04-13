package com.nextinnovation.pitak.item_detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.nextinnovation.pitak.R;
import com.nextinnovation.pitak.data.MainRepository;
import com.nextinnovation.pitak.model.post.Post;
import com.nextinnovation.pitak.utils.MToast;
import com.nextinnovation.pitak.utils.Statics;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDetailActivity extends AppCompatActivity {

    private ImageView back;
    private Post post;
    private TextView title, price, phone;
    private Button save, share;
    private ImageView saveImg, shareImg;
    private ImageView mainImg;
    private View call;
    private MutableLiveData<Boolean> saved = new MutableLiveData<>();

    public static void start(Context context, Post post, Boolean saved) {
        context.startActivity(new Intent(context, ItemDetailActivity.class).putExtra("post", post).putExtra("saved", saved));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        saved.setValue(getIntent().getBooleanExtra("saved", false));
        post = (Post) getIntent().getSerializableExtra("post");
        initView();
        listener();
        setView();
    }

    private void initView() {
        back = findViewById(R.id.item_detail_back_img);
        title = findViewById(R.id.item_detail_title);
        price = findViewById(R.id.item_detail_price);
        phone = findViewById(R.id.item_detail_phone);
        call = findViewById(R.id.item_detail_call);
        save = findViewById(R.id.item_detail_save_2);
        share = findViewById(R.id.item_detail_share_2);
        shareImg = findViewById(R.id.item_detail_share);
        saveImg = findViewById(R.id.item_detail_save);
        mainImg = findViewById(R.id.item_detail_main_img);
    }

    private void listener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Statics.call(phone.getText().toString(), ItemDetailActivity.this);
            }
        });
        saveImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveClicked();
                saved.setValue(!saved.getValue());
                addToFavourite(saved.getValue());
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveClicked();
                saved.setValue(!saved.getValue());
                addToFavourite(saved.getValue());
            }
        });
        saved.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean s) {
                if (s == null) {
                    saveImg.setVisibility(View.GONE);
                    save.setVisibility(View.GONE);
                } else if (s) {
                    saveImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_save_checked));
                    save.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_save_checked, 0, 0, 0);
                } else {
                    saveImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_save));
                    save.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_save, 0, 0, 0);
                }
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });
        shareImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });
    }

    private void setView() {
        title.setText(Html.fromHtml("<h2>" + post.getTitle() + "</h2>" + "<br>" + post.getText()));
        price.setText(post.getAmountPayment() + " сом");
        phone.setText("+" + post.getMobileNumber());
        if (!post.getImgFileList().isEmpty()) {
            setImage(post.getImgFileList().get(0).getContent());
        }
    }

    private void addToFavourite(Boolean mSave) {
        if (mSave) {
            MainRepository.getService().addToFavourite(post.getId(), Statics.getToken(ItemDetailActivity.this)).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    MToast.showInternetError(ItemDetailActivity.this);
                }
            });
        } else {
            MainRepository.getService().deleteFromFavourite(post.getId(), Statics.getToken(ItemDetailActivity.this)).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    MToast.showInternetError(ItemDetailActivity.this);
                }
            });
        }
    }

    private void saveClicked() {
        save.setEnabled(false);
        saveImg.setEnabled(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                save.setEnabled(true);
                saveImg.setEnabled(true);
            }
        }, 1000);
    }

    private void share() {
        String text = post.getFromPlace() + " -> " + post.getToPlace() + "\n" + post.getAmountPayment() + " сом\n" + post.getTitle() + "\n" + post.getText() + "\n+" + post.getMobileNumber();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(intent);
    }

    private void setImage(String encoded) {
        if (encoded != null) {
            byte[] decodedString = Base64.decode(encoded, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Glide.with(this).load(decodedByte).into(mainImg);
        }
    }
}
