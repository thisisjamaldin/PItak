package com.nextinnovation.pitak.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.nextinnovation.pitak.R;
import com.nextinnovation.pitak.data.MainRepository;
import com.nextinnovation.pitak.fragment.main.RecyclerViewAdapter;
import com.nextinnovation.pitak.item_detail.ItemDetailActivity;
import com.nextinnovation.pitak.model.post.PostResponse;
import com.nextinnovation.pitak.utils.MToast;
import com.nextinnovation.pitak.utils.Statics;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrdersActivity extends AppCompatActivity implements RecyclerViewAdapter.onItemClick {

    private RecyclerView recyclerView;
    private ImageView back;
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        back = findViewById(R.id.my_order_back_img);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView = findViewById(R.id.my_order_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(this, false, true);
        recyclerView.setAdapter(adapter);
        getData();
    }

    @Override
    public void onCall(int pos) {

    }

    @Override
    public void onSave(int pos, boolean save) {

    }

    @Override
    public void onClick(int pos) {
        ItemDetailActivity.start(MyOrdersActivity.this, adapter.getList().get(pos).getId(), null);
    }

    @Override
    public void openWhatsapp(int pos) {
        Statics.openWhatsapp(adapter.getList().get(pos).getMobileNumber(), MyOrdersActivity.this);
    }

    private void getData() {
        MainRepository.getService().getMyPosts(Statics.getToken(MyOrdersActivity.this)).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getResult() != null && !response.body().getResult().getContent().isEmpty()) {
                    adapter.addList(response.body().getResult().getContent());
                } else if (response.isSuccessful() && response.body() != null && response.body().getResult() != null && response.body().getResult().getContent().isEmpty()) {
                    MToast.show(MyOrdersActivity.this, getResources().getString(R.string.nothing_found));
                } else {
                    MToast.showResponseError(MyOrdersActivity.this, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                MToast.showInternetError(MyOrdersActivity.this);
            }
        });
    }
}
