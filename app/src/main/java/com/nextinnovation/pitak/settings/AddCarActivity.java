package com.nextinnovation.pitak.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.asksira.bsimagepicker.BSImagePicker;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.nextinnovation.pitak.R;
import com.nextinnovation.pitak.data.MainRepository;
import com.nextinnovation.pitak.model.car.Car;
import com.nextinnovation.pitak.model.car.CarResponse;
import com.nextinnovation.pitak.model.user.UserCar;
import com.nextinnovation.pitak.model.user.UserWhenSignedIn;
import com.nextinnovation.pitak.utils.MSharedPreferences;
import com.nextinnovation.pitak.utils.MToast;
import com.nextinnovation.pitak.utils.Statics;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCarActivity extends AppCompatActivity implements BSImagePicker.OnSingleImageSelectedListener, BSImagePicker.ImageLoaderDelegate {

    private ImageView back, image;
    private Spinner markS, modelS, typeS;
    private EditText number, capacity;
    private File imageFile;
    private CarResponse carTypeResponse = new CarResponse();
    private CarResponse modelResponse = new CarResponse();
    private CarResponse markResponse;
    private Button save;
    private UserCar car;

    public static void start(Context context, UserCar car) {
        context.startActivity(new Intent(context, AddCarActivity.class).putExtra("car", car));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        car = (UserCar) getIntent().getSerializableExtra("car");
        findView();
        listener();
    }

    private void findView() {
        save = findViewById(R.id.add_car_save);
        back = findViewById(R.id.add_car_back);
        image = findViewById(R.id.add_car_image);
        markS = findViewById(R.id.add_car_mark);
        modelS = findViewById(R.id.add_car_model);
        typeS = findViewById(R.id.add_car_type);
        number = findViewById(R.id.add_car_number);
        capacity = findViewById(R.id.add_car_capacity);
    }

    private void listener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        MainRepository.getService().getCarTypes().enqueue(new Callback<CarResponse>() {
            @Override
            public void onResponse(Call<CarResponse> call, Response<CarResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    carTypeResponse = response.body();
                    List<String> types = new ArrayList<>();
                    types.add(getResources().getString(R.string.car_type));
                    int current = 0;
                    if (car == null) {
                        for (Car car : response.body().getResult()) {
                            types.add(car.getName());
                        }
                    } else {
                        for (int i = 0; i < response.body().getResult().size(); i++) {
                            types.add(response.body().getResult().get(i).getName());
                            if (response.body().getResult().get(i).getId() == car.getCarType().getId()) {
                                current = i;
                            }
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(AddCarActivity.this, android.R.layout.simple_spinner_item, types);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    typeS.setAdapter(adapter);
                    typeS.setSelection(current);
                }
            }

            @Override
            public void onFailure(Call<CarResponse> call, Throwable t) {

            }
        });

        MainRepository.getService().getCars().enqueue(new Callback<CarResponse>() {
            @Override
            public void onResponse(Call<CarResponse> call, Response<CarResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    markResponse = new CarResponse(response.body().getResult());
                    List<String> mark = new ArrayList<>();
                    mark.add(getResources().getString(R.string.car_mark));
                    int current = 0;
                    if (car != null) {
                        for (int i = 0; i < response.body().getResult().size(); i++) {
                            mark.add(response.body().getResult().get(i).getName());
                            if (response.body().getResult().get(i).getId() == car.getCarBrand().getId()) {
                                current = i;
                            }
                        }
                    } else {
                        for (Car car : response.body().getResult()) {
                            mark.add(car.getName());
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(AddCarActivity.this, android.R.layout.simple_spinner_item, mark);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    markS.setAdapter(adapter);
                    markS.setSelection(current);
                    if (car != null) {
                        MainRepository.getService().getCarsModel(car.getCarBrand().getId()).enqueue(new Callback<CarResponse>() {
                            @Override
                            public void onResponse(Call<CarResponse> call, Response<CarResponse> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    modelResponse = response.body();
                                    List<String> model = new ArrayList<>();
                                    model.add(getResources().getString(R.string.car_model));
                                    int current = 0;
                                    for (int i = 0; i < response.body().getResult().size(); i++) {
                                        model.add(response.body().getResult().get(i).getName());
                                        if (response.body().getResult().get(i).getId() == car.getCarModel().getId()) {
                                            current = i;
                                        }
                                    }
                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(AddCarActivity.this, android.R.layout.simple_spinner_item, model);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    modelS.setAdapter(adapter);
                                    modelS.setSelection(current);
                                }
                            }

                            @Override
                            public void onFailure(Call<CarResponse> call, Throwable t) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<CarResponse> call, Throwable t) {

            }
        });

        if (car == null) {
            markS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) return;
                    MainRepository.getService().getCarsModel(markResponse.getResult().get(--position).getId()).enqueue(new Callback<CarResponse>() {
                        @Override
                        public void onResponse(Call<CarResponse> call, Response<CarResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                modelResponse = response.body();
                                List<String> model = new ArrayList<>();
                                model.add(getResources().getString(R.string.car_model));
                                for (Car car : response.body().getResult()) {
                                    model.add(car.getName());
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddCarActivity.this, android.R.layout.simple_spinner_item, model);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                modelS.setAdapter(adapter);
                            }
                        }

                        @Override
                        public void onFailure(Call<CarResponse> call, Throwable t) {

                        }
                    });
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else {
            number.setText(car.getCarNumber());
            capacity.setText(car.getCarryCapacity() + "");
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (markS.getSelectedItemPosition() == 0 || modelS.getSelectedItemPosition() == 0 || typeS.getSelectedItemPosition() == 0) {
                    MToast.show(AddCarActivity.this, getResources().getString(R.string.select_your_vehicle));
                    return;
                }
                if (Statics.getString(number).length() == 0) {
                    number.setError(getResources().getString(R.string.must_fill));
                    return;
                }
                if (Statics.getString(capacity).length() == 0) {
                    capacity.setError(getResources().getString(R.string.must_fill));
                    return;
                }
                String id = new Gson().fromJson(MSharedPreferences.get(AddCarActivity.this, Statics.USER, ""), UserWhenSignedIn.class).getId() + "";
                if (car == null) {
                    MainRepository.getService().createCar(
                            Collections.singletonList(getBody(imageFile)),
                            null,
                            RequestBody.create(MediaType.parse("text/plain"), id),
                            RequestBody.create(MediaType.parse("text/plain"), markResponse.getResult().get(markS.getSelectedItemPosition() - 1).getId() + ""),
                            RequestBody.create(MediaType.parse("text/plain"), markResponse.getResult().get(markS.getSelectedItemPosition() - 1).getName()),
                            RequestBody.create(MediaType.parse("text/plain"), modelResponse.getResult().get(modelS.getSelectedItemPosition() - 1).getId() + ""),
                            RequestBody.create(MediaType.parse("text/plain"), modelResponse.getResult().get(modelS.getSelectedItemPosition() - 1).getName()),
                            RequestBody.create(MediaType.parse("text/plain"), Statics.getString(number)),
                            RequestBody.create(MediaType.parse("text/plain"), carTypeResponse.getResult().get(typeS.getSelectedItemPosition() - 1).getId() + ""),
                            RequestBody.create(MediaType.parse("text/plain"), carTypeResponse.getResult().get(typeS.getSelectedItemPosition() - 1).getName()),
                            RequestBody.create(MediaType.parse("text/plain"), Statics.getString(capacity)),
                            Statics.getToken(AddCarActivity.this))
                            .enqueue(new Callback<Object>() {
                                @Override
                                public void onResponse(Call<Object> call, Response<Object> response) {
                                    if (response.isSuccessful()) {
                                        MToast.show(AddCarActivity.this, getResources().getString(R.string.posted));
                                        finish();
                                    } else {
                                        MToast.showResponseError(AddCarActivity.this, response.errorBody());
                                    }
                                }

                                @Override
                                public void onFailure(Call<Object> call, Throwable t) {
                                    MToast.showInternetError(AddCarActivity.this);
                                }
                            });
                } else {
                    MainRepository.getService().createCar(
                            Collections.singletonList(getBody(imageFile)),
                            RequestBody.create(MediaType.parse("text/plain"), car.getId() + ""),
                            RequestBody.create(MediaType.parse("text/plain"), id),
                            RequestBody.create(MediaType.parse("text/plain"), markResponse.getResult().get(markS.getSelectedItemPosition() - 1).getId() + ""),
                            RequestBody.create(MediaType.parse("text/plain"), markResponse.getResult().get(markS.getSelectedItemPosition() - 1).getName()),
                            RequestBody.create(MediaType.parse("text/plain"), modelResponse.getResult().get(modelS.getSelectedItemPosition() - 1).getId() + ""),
                            RequestBody.create(MediaType.parse("text/plain"), modelResponse.getResult().get(modelS.getSelectedItemPosition() - 1).getName()),
                            RequestBody.create(MediaType.parse("text/plain"), Statics.getString(number)),
                            RequestBody.create(MediaType.parse("text/plain"), carTypeResponse.getResult().get(typeS.getSelectedItemPosition() - 1).getId() + ""),
                            RequestBody.create(MediaType.parse("text/plain"), carTypeResponse.getResult().get(typeS.getSelectedItemPosition() - 1).getName()),
                            RequestBody.create(MediaType.parse("text/plain"), Statics.getString(capacity)),
                            Statics.getToken(AddCarActivity.this))
                            .enqueue(new Callback<Object>() {
                                @Override
                                public void onResponse(Call<Object> call, Response<Object> response) {
                                    if (response.isSuccessful()) {
                                        MToast.show(AddCarActivity.this, getResources().getString(R.string.posted));
                                        finish();
                                    } else {
                                        MToast.showResponseError(AddCarActivity.this, response.errorBody());
                                    }
                                }

                                @Override
                                public void onFailure(Call<Object> call, Throwable t) {
                                    MToast.showInternetError(AddCarActivity.this);
                                }
                            });

                }

            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BSImagePicker.Builder("com.nextinnovation.pitak.fileProvider")
                        .hideGalleryTile()
                        .build()
                        .show(getSupportFragmentManager(), "picker");
            }
        });
    }

    private MultipartBody.Part getBody(File file) {
        if (file == null) return null;
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        return MultipartBody.Part.createFormData("fileList", file.getName(), requestFile);
    }

    @Override
    public void loadImage(Uri imageUri, ImageView ivImage) {
        Glide.with(this).load(imageUri).into(ivImage);
    }

    @Override
    public void onSingleImageSelected(Uri uri, String tag) {
        Bitmap bitmap = resizeImage(uri);
        Glide.with(image.getContext()).load(bitmap).into(image);
        imageFile = createFile(bitmap);
    }

    private Bitmap resizeImage(Uri uri) {
        try {
            return Bitmap.createScaledBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri), 800, 500, true);
        } catch (Exception e) {
            return null;
        }
    }

    private File createFile(Bitmap bitmap) {
        try {
            File f = new File(this.getCacheDir(), "filename2");
            f.createNewFile();

//Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 10, bos);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, bos);
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            return f;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}