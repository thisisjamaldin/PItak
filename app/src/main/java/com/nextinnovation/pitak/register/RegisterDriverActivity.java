package com.nextinnovation.pitak.register;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.nextinnovation.pitak.R;
import com.nextinnovation.pitak.data.MainRepository;
import com.nextinnovation.pitak.main.MainActivity;
import com.nextinnovation.pitak.model.car.Car;
import com.nextinnovation.pitak.model.car.CarResponse;
import com.nextinnovation.pitak.model.user.ProfileRequest;
import com.nextinnovation.pitak.model.user.ProfileResponse;
import com.nextinnovation.pitak.model.user.User;
import com.nextinnovation.pitak.model.user.UserCar;
import com.nextinnovation.pitak.model.user.UserSignIn;
import com.nextinnovation.pitak.model.user.UserWhenSignedIn;
import com.nextinnovation.pitak.utils.MSharedPreferences;
import com.nextinnovation.pitak.utils.MToast;
import com.nextinnovation.pitak.utils.Statics;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterDriverActivity extends AppCompatActivity {

    private ImageView back;
    private EditText name;
    private EditText carNumber;
    private EditText email;
    private Button save;
    private Spinner carMark, carModel, carType;
    private Spinner countySp, citySp;
    private View editPhoneView;
    private EditText phoneEdit;
    private ImageView profile;
    private Spinner codeEdit;

    private String phone;
    private boolean edit;
    private File profileFile;

    private CarResponse carResponse;
    private CarResponse carTypeResponse = new CarResponse();
    private CarResponse modelResponse = new CarResponse();
    private UserWhenSignedIn userToEdit;
    private Context context;

    public static void start(Context context, boolean edit) {
        context.startActivity(new Intent(context, RegisterDriverActivity.class).putExtra("edit", edit));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_driver);

        context = this;
        edit = getIntent().getBooleanExtra("edit", false);
        phone = MSharedPreferences.get(this, "phone", "").replace("+", "");
        initView();
        listener();
    }

    private void initView() {
        profile = findViewById(R.id.register_driver_profile_img);
        phoneEdit = findViewById(R.id.register_driver_phone_et);
        editPhoneView = findViewById(R.id.register_driver_phone_layout);
        codeEdit = findViewById(R.id.register_driver_phone_code_sp);
        back = findViewById(R.id.register_driver_back_img);
        name = findViewById(R.id.register_driver_name_et);
        save = findViewById(R.id.register_driver_save_btn);
        carMark = findViewById(R.id.register_driver_car_mark_sp);
        carModel = findViewById(R.id.register_driver_car_model_sp);
        carNumber = findViewById(R.id.register_driver_car_number);
        countySp = findViewById(R.id.register_driver_county_sp);
        citySp = findViewById(R.id.register_driver_city_sp);
        carType = findViewById(R.id.register_driver_car_type_sp);
        email = findViewById(R.id.register_driver_email_et);
    }

    private void listener() {
        if (edit) {
            userToEdit = new Gson().fromJson(MSharedPreferences.get(this, Statics.USER, ""), UserWhenSignedIn.class);
            setValues(userToEdit);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Statics.getString(name).length() < 1) {
                    name.setError(getResources().getString(R.string.must_fill));
                    return;
                } else if (Statics.getString(email).length() < 3) {
                    email.setError(getResources().getString(R.string.must_fill));
                    return;
                } else if (Statics.getString(carNumber).length() < 2) {
                    carNumber.setError(getResources().getString(R.string.must_fill));
                    return;
                } else if (carMark.getSelectedItemPosition() == 0 || carModel.getSelectedItemPosition() == 0 || carType.getSelectedItemPosition() == 0) {
                    MToast.show(context, getResources().getString(R.string.select_your_vehicle));
                    return;
                }
                save.setVisibility(View.GONE);
                Car car = new Car(carResponse.getResult().get(carMark.getSelectedItemPosition() - 1).getId(), carResponse.getResult().get(carMark.getSelectedItemPosition() - 1).getName());
                Car model = new Car(modelResponse.getResult().get(carModel.getSelectedItemPosition() - 1).getId(), modelResponse.getResult().get(carModel.getSelectedItemPosition() - 1).getName());
                Car type = new Car(carTypeResponse.getResult().get(carType.getSelectedItemPosition() - 1).getId(), carTypeResponse.getResult().get(carType.getSelectedItemPosition() - 1).getName());
                UserCar userCar = new UserCar(car.getId(), car.getName(), model.getId(), model.getName(), Statics.getString(carNumber), type.getId(), type.getName());
                if (edit) {
                    final UserWhenSignedIn userWhenSignedIn = new UserWhenSignedIn(userToEdit.getId(), codeEdit.getSelectedItem().toString().replace("+", "") + Statics.getString(phoneEdit), "surname", "patronymic", Statics.getString(name), Statics.getString(email), null, null, codeEdit.getSelectedItem().toString().replace("+", "") + Statics.getString(phoneEdit), userCar);
                    if (userToEdit.getUsername().equals(userWhenSignedIn.getUsername())) {
                        MainRepository.getService().editDriver(userWhenSignedIn, Statics.getToken(context)).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    MainRepository.getService().setUserProfile(getBody(profileFile), Statics.getToken(context)).enqueue(new Callback<ProfileResponse>() {
                                        @Override
                                        public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                                            if (response.isSuccessful()) {
                                                userWhenSignedIn.setProfilePhoto(new ProfileRequest(response.body().getResult().getContent()));
                                                MSharedPreferences.set(context, Statics.USER, new Gson().toJson(userWhenSignedIn));
                                                finish();
                                            } else {
                                                MToast.show(context, Statics.getResponseError(response.errorBody()));
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ProfileResponse> call, Throwable t) {
                                            MToast.showInternetError(context);
                                        }
                                    });
                                } else {
                                    MToast.showResponseError(context, response.errorBody());
                                    save.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                MToast.showInternetError(context);
                                save.setVisibility(View.VISIBLE);
                            }
                        });
                    } else {
                        MSharedPreferences.set(context, "phone", userWhenSignedIn.getUsername());
                        MSharedPreferences.set(context, "userToEdit", userWhenSignedIn);
                        CodeAuthenticationActivity.start(context, Statics.DRIVER);
                    }
                } else {
                    MainRepository.getService().signUp(
                            getBody(profileFile),
                            RequestBody.create(MediaType.parse("text/plain"), phone),
                            RequestBody.create(MediaType.parse("text/plain"), Statics.getString(email)),
                            RequestBody.create(MediaType.parse("text/plain"), phone),
                            RequestBody.create(MediaType.parse("text/plain"), "username"),
                            RequestBody.create(MediaType.parse("text/plain"), Statics.getString(name)),
                            RequestBody.create(MediaType.parse("text/plain"), "patronymic"),
                            RequestBody.create(MediaType.parse("text/plain"), Statics.PASSENGER),
                            RequestBody.create(MediaType.parse("text/plain"), car.getId() + ""),
                            RequestBody.create(MediaType.parse("text/plain"), car.getName()),
                            RequestBody.create(MediaType.parse("text/plain"), model.getId() + ""),
                            RequestBody.create(MediaType.parse("text/plain"), model.getName()),
                            RequestBody.create(MediaType.parse("text/plain"), userCar.getCarNumber()),
                            RequestBody.create(MediaType.parse("text/plain"), type.getId() + ""),
                            RequestBody.create(MediaType.parse("text/plain"), type.getName())).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful()) {
                                MSharedPreferences.set(context, Statics.REGISTERED, true);
                                MainRepository.getService().signIn(new UserSignIn(phone, phone)).enqueue(new Callback<UserWhenSignedIn>() {
                                    @Override
                                    public void onResponse(Call<UserWhenSignedIn> call, Response<UserWhenSignedIn> response) {
                                        if (response.isSuccessful()) {
                                            if (response.body().getRoles()[0].equals("ROLE_DRIVER")) {
                                                MSharedPreferences.set(context, "who", Statics.DRIVER);
                                            }
                                            if (response.body().getRoles()[0].equals("ROLE_PASSENGER")) {
                                                MSharedPreferences.set(context, "who", Statics.PASSENGER);
                                            }
                                            MSharedPreferences.set(context, Statics.USER, new Gson().toJson(response.body()));
                                            MainActivity.start(context);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<UserWhenSignedIn> call, Throwable t) {
                                        MToast.showInternetError(context);
                                        save.setVisibility(View.VISIBLE);
                                    }
                                });
                            } else {
                                MToast.show(context, Statics.getResponseError(response.errorBody()));
                            }
                            save.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            MToast.showInternetError(context);
                            save.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });

        MainRepository.getService().getCarTypes().enqueue(new Callback<CarResponse>() {
            @Override
            public void onResponse(Call<CarResponse> call, Response<CarResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    carTypeResponse = response.body();
                    List<String> types = new ArrayList<>();
                    types.add(getResources().getString(R.string.car_type));
                    for (Car car : response.body().getResult()) {
                        types.add(car.getName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, types);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    carType.setAdapter(adapter);
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
                    carResponse = new CarResponse(response.body().getResult());
                    List<String> mark = new ArrayList<>();
                    mark.add(getResources().getString(R.string.car_mark));
                    for (Car car : response.body().getResult()) {
                        mark.add(car.getName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, mark);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    carMark.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<CarResponse> call, Throwable t) {

            }
        });

        carMark.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return;
                }
                MainRepository.getService().getCarsModel(carResponse.getResult().get(--position).getId()).enqueue(new Callback<CarResponse>() {
                    @Override
                    public void onResponse(Call<CarResponse> call, Response<CarResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            modelResponse = response.body();
                            List<String> model = new ArrayList<>();
                            model.add(getResources().getString(R.string.car_model));
                            for (Car car : response.body().getResult()) {
                                model.add(car.getName());
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, model);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            carModel.setAdapter(adapter);
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

        countySp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        citySp.setSelection(0);
                        break;
                    case 1:
                        String[] city = getResources().getStringArray(R.array.city_kg);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, city);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        citySp.setAdapter(adapter);
                        break;
                    case 2:
                        city = getResources().getStringArray(R.array.city_ru);
                        adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, city);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        citySp.setAdapter(adapter);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 28);
            }
        });

    }

    private void setValues(UserWhenSignedIn user) {
        String userPhone = "+" + user.getUsername();
        editPhoneView.setVisibility(View.VISIBLE);
        name.setText(user.getName());
        email.setText(user.getEmail());
        phoneEdit.setText(userPhone.replace("+7", "+996").replace("+996", ""));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 28 && resultCode == RESULT_OK && data != null && data.getData()!=null) {
            Glide.with(profile.getContext()).load(resizeImage(data.getData())).apply(RequestOptions.circleCropTransform()).into(profile);
            profileFile = createFile(resizeImage(data.getData()));
        }
    }

    private MultipartBody.Part getBody(File file) {
        if (file == null) return null;
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        return MultipartBody.Part.createFormData("file", file.getName(), requestFile);
    }

    private Bitmap resizeImage(Uri uri) {
        try {
            return Bitmap.createScaledBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri), 300, 300, true);
        } catch (Exception e) {
            return null;
        }
    }

    private File createFile(Bitmap bitmap) {
        try {
            File f = new File(this.getCacheDir(), "filename");
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
            return null;
        }
    }
}
