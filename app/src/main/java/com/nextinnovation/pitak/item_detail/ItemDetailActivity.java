package com.nextinnovation.pitak.item_detail;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.nextinnovation.pitak.R;
import com.nextinnovation.pitak.data.MainRepository;
import com.nextinnovation.pitak.fragment.main.MainFragment;
import com.nextinnovation.pitak.fragment.main.RecyclerViewAdapter;
import com.nextinnovation.pitak.fragment.role.RoleFragment;
import com.nextinnovation.pitak.fragment.saved.SavedFragment;
import com.nextinnovation.pitak.model.post.AppAdvertModel;
import com.nextinnovation.pitak.model.post.Post;
import com.nextinnovation.pitak.model.post.PostSingle;
import com.nextinnovation.pitak.register.RegisterActivity;
import com.nextinnovation.pitak.settings.AddPostActivity;
import com.nextinnovation.pitak.utils.MToast;
import com.nextinnovation.pitak.utils.Statics;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDetailActivity extends AppCompatActivity implements RecyclerViewAdapter.onItemClick {

    private ImageView back;
    private TextView title, price, phone;
    private Button save, share, edit, delete;
    private ImageView saveImg, shareImg, reportImage;
    private ImageView mainImg;
    private View call;
    private AppAdvertModel post;
    private long postId;

    private BottomSheetBehavior bsb;
    private View bottomSheet, dim;
    private Button bottomCancel, bottomReport;
    private boolean saved;
    private boolean mine;
    private ProgressDialog dialog;
//    private RecyclerViewAdapter adapter;
//    private RecyclerView recyclerView;

    public static void start(Context context, long id, boolean mine) {
        context.startActivity(new Intent(context, ItemDetailActivity.class).putExtra("id", id).putExtra("mine", mine));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        if (Statics.getToken(this).length() <= 10) {
            startActivity(new Intent(ItemDetailActivity.this, RegisterActivity.class));
            finish();
        }

        dialog = ProgressDialog.show(this, null,
                null, true);

        mine = getIntent().getBooleanExtra("mine", false);
        postId = getIntent().getLongExtra("id", 0);
        getData(postId);
        initView();
        listener();
    }

    private void getData(final long id) {
        if (mine){
            MainRepository.getService().getMyAdvert(id, Statics.getToken(this)).enqueue(new Callback<PostSingle>() {
                @Override
                public void onResponse(Call<PostSingle> call, Response<PostSingle> response) {
                    dialog.dismiss();
                    if (response.isSuccessful() && response.body().getResult() != null) {
                        post = response.body().getResult();
                        setView();
                    } else {
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<PostSingle> call, Throwable t) {
                    dialog.dismiss();
                }
            });
        } else {
            MainRepository.getService().getAdvert(id, Statics.getToken(this)).enqueue(new Callback<PostSingle>() {
                @Override
                public void onResponse(Call<PostSingle> call, Response<PostSingle> response) {
                    dialog.dismiss();
                    if (response.isSuccessful() && response.body().getResult() != null) {
                        Log.e("--------post", response.body().getResult().getAppAdvertModel().getId()+"");
                        Log.e("--------postId", id+"");
                        post = response.body().getResult();
                        setView();
                    } else {
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<PostSingle> call, Throwable t) {
                    dialog.dismiss();
                }
            });
        }
    }

    private void initView() {
        edit = findViewById(R.id.item_detail_edit);
        delete = findViewById(R.id.item_detail_delete);
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
//        recyclerView = findViewById(R.id.item_detail_recycler);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        adapter = new RecyclerViewAdapter(this, false, false);
//        recyclerView.setAdapter(adapter);
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
                intent.putExtra("id", post.getAppAdvertModel().getId());
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
                addToFavourite(saved);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveClicked();
                addToFavourite(saved);
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
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPostActivity.start(ItemDetailActivity.this, post);
                finish();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ItemDetailActivity.this);
                alert.setTitle(getResources().getString(R.string.are_you_sure_to_delete_));
                alert.setNeutralButton(getResources().getString(R.string.cancel), null);
                alert.setPositiveButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainRepository.getService().deleteMyPost(postId, Statics.getToken(ItemDetailActivity.this)).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {

                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
                        finish();
                    }
                });
                alert.show();
            }
        });
        if (mine) {
            share.setVisibility(View.GONE);
            shareImg.setVisibility(View.GONE);
            save.setVisibility(View.GONE);
            saveImg.setVisibility(View.GONE);
            call.setVisibility(View.GONE);
            reportImage.setVisibility(View.GONE);
            edit.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
        }
    }

    private void setView() {
        saved = post.getAppAdvertModel().isFavorite();
        title.setText(Html.fromHtml("<h2>" + post.getAppAdvertModel().getTitle() + "</h2>" + "<br>" + post.getAppAdvertModel().getText()));
        price.setText(post.getAppAdvertModel().getAmountPayment() + " сом");
        phone.setText("+" + post.getAppAdvertModel().getMobileNumber());
        if (post.getAttachmentModels().isEmpty()) {
            Glide.with(this).load(getResources().getDrawable(R.drawable.bg_launch_screen)).into(mainImg);
        } else {
            Statics.loadImage(mainImg, post.getAttachmentModels().get(0).getAppFile().getUrl(), null);
        }
        if (saved) {
            saveImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_save_checked));
            save.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_save_checked, 0, 0, 0);
        } else {
            saveImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_save));
            save.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_save, 0, 0, 0);
        }
    }

    private void addToFavourite(final Boolean mSave) {
        saved = !mSave;
        if (saved) {
            saveImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_save_checked));
            save.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_save_checked, 0, 0, 0);
        } else {
            saveImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_save));
            save.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_save, 0, 0, 0);
        }
        if (saved) {
            MainRepository.getService().addToFavourite(post.getAppAdvertModel().getId(), Statics.getToken(ItemDetailActivity.this)).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    MToast.showInternetError(ItemDetailActivity.this);
                }
            });
        } else {
            MainRepository.getService().deleteFromFavourite(post.getAppAdvertModel().getId(), Statics.getToken(ItemDetailActivity.this)).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    MToast.showInternetError(ItemDetailActivity.this);
                }
            });
        }
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < MainFragment.adapter.getList().size(); i++) {
                    if (MainFragment.adapter.getList().get(i).getAppAdvertModel().getId() == post.getAppAdvertModel().getId()) {
                        MainFragment.adapter.getList().get(i).getAppAdvertModel().setFavorite(saved);
                        MainFragment.adapter.notifyItemChanged(i);
                    }
                }
                for (int i = 0; i < RoleFragment.adapter.getList().size(); i++) {
                    if (RoleFragment.adapter.getList().get(i).getAppAdvertModel().getId() == post.getAppAdvertModel().getId()) {
                        RoleFragment.adapter.getList().get(i).getAppAdvertModel().setFavorite(saved);
                        RoleFragment.adapter.notifyItemChanged(i);
                    }
                }
                for (int i = 0; i < SavedFragment.adapter.getList().size(); i++) {
                    if (SavedFragment.adapter.getList().get(i).getAppAdvertModel().getId() == post.getAppAdvertModel().getId()) {
                        SavedFragment.adapter.getList().get(i).getAppAdvertModel().setFavorite(saved);
                        SavedFragment.adapter.notifyItemChanged(i);
                    }
                }
            }
        });
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
        String text = post.getAppAdvertModel().getFromPlace() + " -> " + post.getAppAdvertModel().getToPlace() + "\n" + post.getAppAdvertModel().getAmountPayment() + " сом\n" + post.getAppAdvertModel().getTitle() + "\n" + post.getAppAdvertModel().getText() + "\n+" + post.getAppAdvertModel().getMobileNumber();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(intent);
    }

    @Override
    public void onCall(int pos) {
//        Statics.call("+" + adapter.getList().get(pos).getMobileNumber(), this);
    }

    @Override
    public void onSave(int pos, boolean save) {
//        if (save) {
//            MainRepository.getService().addToFavourite(adapter.getList().get(pos).getId(), Statics.getToken(this)).enqueue(new Callback<Void>() {
//                @Override
//                public void onResponse(Call<Void> call, Response<Void> response) {
//                }
//
//                @Override
//                public void onFailure(Call<Void> call, Throwable t) {
//                    MToast.showInternetError(ItemDetailActivity.this);
//                }
//            });
//        } else {
//            MainRepository.getService().deleteFromFavourite(adapter.getList().get(pos).getId(), Statics.getToken(this)).enqueue(new Callback<Void>() {
//                @Override
//                public void onResponse(Call<Void> call, Response<Void> response) {
//                }
//
//                @Override
//                public void onFailure(Call<Void> call, Throwable t) {
//                    MToast.showInternetError(ItemDetailActivity.this);
//                }
//            });
//        }
    }

    @Override
    public void onClick(int pos) {
//        ItemDetailActivity.start(this, adapter.getList().get(pos).getId(), false);
    }

    @Override
    public void openWhatsapp(int pos) {
//        Statics.openWhatsapp(adapter.getList().get(pos).getMobileNumber(), this);
    }
}
