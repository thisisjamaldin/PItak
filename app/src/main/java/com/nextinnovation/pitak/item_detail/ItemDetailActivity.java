package com.nextinnovation.pitak.item_detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.nextinnovation.pitak.R;
import com.nextinnovation.pitak.data.MainRepository;
import com.nextinnovation.pitak.fragment.main.RecyclerViewAdapter;
import com.nextinnovation.pitak.model.post.Post;
import com.nextinnovation.pitak.model.post.PostSingle;
import com.nextinnovation.pitak.register.RegisterActivity;
import com.nextinnovation.pitak.utils.MToast;
import com.nextinnovation.pitak.utils.Statics;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDetailActivity extends AppCompatActivity implements RecyclerViewAdapter.onItemClick {

    private ImageView back;
    private TextView title, price, phone;
    private Button save, share;
    private ImageView saveImg, shareImg, reportImage;
    private ImageView mainImg;
    private View call;
    private Post post;
    private MutableLiveData<Boolean> saved = new MutableLiveData<>();

    private BottomSheetBehavior bsb;
    private View bottomSheet, dim;
    private Button bottomCancel, bottomReport;
    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;

    public static void start(Context context, long id, Boolean saved) {
        context.startActivity(new Intent(context, ItemDetailActivity.class).putExtra("id", id).putExtra("saved", saved));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        saved.setValue(getIntent().getBooleanExtra("saved", false));

        if (Statics.getToken(this).length() <= 10){
            startActivity(new Intent(ItemDetailActivity.this, RegisterActivity.class));
            finish();
        }

        getData(getIntent().getLongExtra("id", 0));
        initView();
        listener();
    }

    private void getData(long id) {
        MainRepository.getService().getAdvert(id, Statics.getToken(this)).enqueue(new Callback<PostSingle>() {
            @Override
            public void onResponse(Call<PostSingle> call, Response<PostSingle> response) {
                if (response.isSuccessful() && response.body().getResult() != null) {
                    post = response.body().getResult();
                    setView();
                }
            }

            @Override
            public void onFailure(Call<PostSingle> call, Throwable t) {
            }
        });
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
        reportImage = findViewById(R.id.item_detail_report);
        dim = findViewById(R.id.main_fragment_dim);
        bottomSheet = findViewById(R.id.bottom_sheet);
        bottomCancel = findViewById(R.id.bottom_sheet_cancel);
        bottomReport = findViewById(R.id.bottom_sheet_report);
        bsb = BottomSheetBehavior.from(bottomSheet);
        recyclerView = findViewById(R.id.item_detail_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(this, false, false);
        recyclerView.setAdapter(adapter);
    }

    private void listener() {
        reportImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bsb.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        dim.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);
                return true;
            }
        });
        bottomReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemDetailActivity.this, ReportActivity.class);
                intent.putExtra("id", post.getId());
                startActivity(intent);
                bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        bottomCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        bsb.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    dim.setVisibility(View.GONE);
                } else {
                    dim.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
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
        if (!post.getImgFileList().isEmpty() && post.getImgFileList().get(0) != null && post.getImgFileList().get(0).getContent() != null) {
            setImage(post.getImgFileList().get(0).getContent());
        } else {
            Glide.with(this).load(getResources().getDrawable(R.drawable.bg_launch_screen)).into(mainImg);
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
        byte[] decodedString = Base64.decode(encoded, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        Glide.with(this).load(decodedByte).into(mainImg);
    }

    @Override
    public void onCall(int pos) {
        Statics.call("+" + adapter.getList().get(pos).getMobileNumber(), this);
    }

    @Override
    public void onSave(int pos, boolean save) {
        if (save) {
            MainRepository.getService().addToFavourite(adapter.getList().get(pos).getId(), Statics.getToken(this)).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    MToast.showInternetError(ItemDetailActivity.this);
                }
            });
        } else {
            MainRepository.getService().deleteFromFavourite(adapter.getList().get(pos).getId(), Statics.getToken(this)).enqueue(new Callback<Void>() {
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

    @Override
    public void onClick(int pos) {
        ItemDetailActivity.start(this, adapter.getList().get(pos).getId(), false);
    }

    @Override
    public void openWhatsapp(int pos) {
        Statics.openWhatsapp(adapter.getList().get(pos).getMobileNumber(), this);
    }
}
