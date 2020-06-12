package com.nextinnovation.pitak.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nextinnovation.pitak.R;
import com.nextinnovation.pitak.model.user.UserCar;
import com.nextinnovation.pitak.model.user.UserWhenSignedIn;
import com.nextinnovation.pitak.utils.MSharedPreferences;
import com.nextinnovation.pitak.utils.Statics;

public class MyCarActivity extends AppCompatActivity {

    public static void start(Context context) {
        context.startActivity(new Intent(context, MyCarActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_car);

        UserCar car = new Gson().fromJson(MSharedPreferences.get(this, Statics.USER, ""), UserWhenSignedIn.class).getCarCommonModel();
        ImageView back = findViewById(R.id.my_car_back_img);
        TextView mark = findViewById(R.id.my_car_mark);
        TextView model = findViewById(R.id.my_car_model);
        TextView type = findViewById(R.id.my_car_type);
        if (car != null && car.getCarBrand() != null && car.getCarModel() != null && car.getCarType() != null) {
            mark.setText(car.getCarBrand().getName());
            model.setText(car.getCarModel().getName());
            type.setText(car.getCarType().getName());
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}