package com.nextinnovation.pitak.settings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nextinnovation.pitak.R;
import com.nextinnovation.pitak.data.MainRepository;
import com.nextinnovation.pitak.model.user.UserCar;
import com.nextinnovation.pitak.utils.Statics;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCarDetailActivity extends AppCompatActivity {

    private Button edit, delete;
    private ImageView image, back;
    private TextView number, mark, model, type;
    private UserCar car;

    public static void start(Context context, UserCar car){
        context.startActivity(new Intent(context, MyCarDetailActivity.class).putExtra("car", car));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_car_detail);

        car = (UserCar) getIntent().getSerializableExtra("car");
        findView();
        listener();
        initView();
    }

    private void findView(){
        edit = findViewById(R.id.my_car_edit);
        delete = findViewById(R.id.my_car_delete);
        image = findViewById(R.id.my_car_image);
        back = findViewById(R.id.my_car_back_img);
        number = findViewById(R.id.my_car_number);
        mark = findViewById(R.id.my_car_mark);
        model = findViewById(R.id.my_car_model);
        type = findViewById(R.id.my_car_type);
    }

    private void listener(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCarActivity.start(MyCarDetailActivity.this, car);
                finish();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MyCarDetailActivity.this);
                alert.setTitle(getResources().getString(R.string.are_you_sure_to_delete_));
                alert.setNeutralButton(getResources().getString(R.string.cancel), null);
                alert.setPositiveButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainRepository.getService().deleteMyCar(car.getId(), Statics.getToken(MyCarDetailActivity.this)).enqueue(new Callback<Void>() {
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
    }

    private void initView(){
        if (!car.getCarFiles().isEmpty() && car.getCarFiles().get(0).getContent()!=null){
            Glide.with(this).load(setImage(car.getCarFiles().get(0).getContent())).centerCrop().into(image);
        }
        number.setText(car.getCarNumber());
        mark.setText(car.getCarBrand().getName());
        model.setText(car.getCarModel().getName());
        type.setText(car.getCarType().getName());
    }

    private Bitmap setImage(String encoded) {
        byte[] decodedString = Base64.decode(encoded, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}