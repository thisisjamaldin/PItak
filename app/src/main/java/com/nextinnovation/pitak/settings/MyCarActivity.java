package com.nextinnovation.pitak.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.nextinnovation.pitak.R;
import com.nextinnovation.pitak.data.MainRepository;
import com.nextinnovation.pitak.model.car.NewCarResponse;
import com.nextinnovation.pitak.utils.Statics;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCarActivity extends AppCompatActivity implements CarAdapter.onItemClick {

    private RecyclerView recyclerView;
    private CarAdapter adapter;
    private ImageView back;
    private Button add;

    public static void start(Context context) {
        context.startActivity(new Intent(context, MyCarActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_car);


        add = findViewById(R.id.my_car_add);
        recyclerView = findViewById(R.id.my_car_recycler);
        back = findViewById(R.id.my_car_back_img);
        adapter = new CarAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyCarActivity.this, AddCarActivity.class));
            }
        });

    }

    @Override
    public void onClick(int pos) {
        MyCarDetailActivity.start(MyCarActivity.this, adapter.getList().get(pos));
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainRepository.getService().getMyCars(Statics.getToken(this)).enqueue(new Callback<NewCarResponse>() {
            @Override
            public void onResponse(Call<NewCarResponse> call, Response<NewCarResponse> response) {
                if (response.body().getResult().isEmpty()){
                    adapter.clear();
                    return;
                }
                adapter.addList(response.body().getResult());
            }

            @Override
            public void onFailure(Call<NewCarResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}