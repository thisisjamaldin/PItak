package com.nextinnovation.pitak.register;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
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
import com.nextinnovation.pitak.model.car.CarCommonModel;
import com.nextinnovation.pitak.model.car.CarResponse;
import com.nextinnovation.pitak.model.car.NewCarResponse;
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
    private EditText carry;
    private ImageView profile;
    private Spinner codeEdit;

    private String phone;
    private boolean edit;
    private File profileFile;

    private CarResponse carResponse;
    private CarResponse countryResponse;
    private CarResponse carTypeResponse = new CarResponse();
    private CarResponse modelResponse = new CarResponse();
    private CarResponse cityResponse = new CarResponse();
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
        carry = findViewById(R.id.register_driver_car_capacity);
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
                Car country;
                Car city;
                if (countryResponse != null && !countryResponse.getResult().isEmpty()) {
                    country = new Car(countryResponse.getResult().get(countySp.getSelectedItemPosition()).getId(), countryResponse.getResult().get(countySp.getSelectedItemPosition()).getName());
                } else {
                    country = new Car(1, null);
                }
                if (cityResponse != null && !cityResponse.getResult().isEmpty()) {
                    city = new Car(cityResponse.getResult().get(citySp.getSelectedItemPosition()).getId(), cityResponse.getResult().get(citySp.getSelectedItemPosition()).getName());
                } else {
                    city = new Car(1, null);
                }
                UserCar userCar = new UserCar(car, model, Statics.getString(carNumber), type, Integer.parseInt(Statics.getString(carry)));
                if (edit) {
                    final UserWhenSignedIn userWhenSignedIn = new UserWhenSignedIn(userToEdit.getId(), codeEdit.getSelectedItem().toString().replace("+", "") + Statics.getString(phoneEdit), "surname", "patronymic", Statics.getString(name), Statics.getString(email), Statics.DRIVER, Statics.getToken(context).substring(7), codeEdit.getSelectedItem().toString().replace("+", "") + Statics.getString(phoneEdit), userCar, country, city);
                    if (userToEdit.getUsername().equals(userWhenSignedIn.getUsername())) {
                        MainRepository.getService().editDriver(userWhenSignedIn, Statics.getToken(context)).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    if (profileFile != null) {
                                        MainRepository.getService().setUserProfile(getBody(profileFile), Statics.getToken(context)).enqueue(new Callback<ProfileResponse>() {
                                            @Override
                                            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                                                if (response.isSuccessful()) {
                                                    userWhenSignedIn.setProfilePhoto(new ProfileRequest(response.body().getResult().getUrl()));
                                                    MSharedPreferences.set(context, Statics.USER, new Gson().toJson(userWhenSignedIn));
                                                    finish();
                                                } else {
                                                    MToast.showResponseError(context, response.errorBody());
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                                                MToast.showInternetError(context);
                                                save.setVisibility(View.VISIBLE);
                                            }
                                        });
                                    } else {
                                        MSharedPreferences.set(context, Statics.USER, new Gson().toJson(userWhenSignedIn));
                                        MainRepository.getService().removeProfile(Statics.getToken(context)).enqueue(new Callback<Void>() {
                                            @Override
                                            public void onResponse(Call<Void> call, Response<Void> response) {
                                            }

                                            @Override
                                            public void onFailure(Call<Void> call, Throwable t) {
                                            }
                                        });
                                        finish();
                                    }
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
                            RequestBody.create(MediaType.parse("text/plain"), Statics.DRIVER),
                            RequestBody.create(MediaType.parse("text/plain"), car.getId() + ""),
                            RequestBody.create(MediaType.parse("text/plain"), model.getId() + ""),
                            RequestBody.create(MediaType.parse("text/plain"), userCar.getCarNumber()),
                            RequestBody.create(MediaType.parse("text/plain"), type.getId() + ""),
                            RequestBody.create(MediaType.parse("text/plain"), country.getId() + ""),
                            RequestBody.create(MediaType.parse("text/plain"), city.getId() + "")
                    ).enqueue(new Callback<UserWhenSignedIn>() {
                        @Override
                        public void onResponse(Call<UserWhenSignedIn> call, Response<UserWhenSignedIn> response) {
                            if (response.isSuccessful()) {
                                MSharedPreferences.set(context, Statics.REGISTERED, true);
                                MainRepository.getService().signIn(new UserSignIn(phone, phone, Statics.DRIVER)).enqueue(new Callback<UserWhenSignedIn>() {
                                    @Override
                                    public void onResponse(Call<UserWhenSignedIn> call, Response<UserWhenSignedIn> response) {
                                        if (response.isSuccessful()) {
                                            Statics.setToken(RegisterDriverActivity.this, response.body().getToken());
                                            MainRepository.getService().getMe(Statics.getToken(RegisterDriverActivity.this)).enqueue(new Callback<User>() {
                                                @Override
                                                public void onResponse(Call<User> call, Response<User> response) {
                                                    MSharedPreferences.set(context, Statics.USER, new Gson().toJson(response.body().getResult()));
                                                    if (response.body().getResult().getUserType().equals("PASSENGER")) {
                                                        MSharedPreferences.set(context, "who", Statics.PASSENGER);
                                                    }
                                                    if (response.body().getResult().getUserType().equals("DRIVER")) {
                                                        MSharedPreferences.set(context, "who", Statics.DRIVER);
                                                        MainRepository.getService().getMyCars(Statics.getToken(RegisterDriverActivity.this)).enqueue(new Callback<NewCarResponse>() {
                                                            @Override
                                                            public void onResponse(Call<NewCarResponse> call, Response<NewCarResponse> response) {
                                                                UserWhenSignedIn uws = new Gson().fromJson(MSharedPreferences.get(RegisterDriverActivity.this, Statics.USER, ""), UserWhenSignedIn.class);
                                                                if (response.body().getResult().isEmpty())
                                                                    return;
                                                                uws.setCarCommonModel(response.body().getResult().get(0).getCarCommonModel());
                                                                MSharedPreferences.set(RegisterDriverActivity.this, Statics.USER, new Gson().toJson(uws));
                                                            }

                                                            @Override
                                                            public void onFailure(Call<NewCarResponse> call, Throwable t) {

                                                            }
                                                        });
                                                    }
                                                    MainActivity.start(context);
                                                }

                                                @Override
                                                public void onFailure(Call<User> call, Throwable t) {

                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<UserWhenSignedIn> call, Throwable t) {
                                        MToast.showInternetError(context);
                                        save.setVisibility(View.VISIBLE);
                                    }
                                });
                            } else {
                                MToast.showResponseError(context, response.errorBody());
                            }
                            save.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFailure(Call<UserWhenSignedIn> call, Throwable t) {
                            MToast.showInternetError(context);
                            save.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });
        if (!edit) {
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

            MainRepository.getService().getCountry().enqueue(new Callback<CarResponse>() {
                @Override
                public void onResponse(Call<CarResponse> call, Response<CarResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        countryResponse = new CarResponse(response.body().getResult());
                        List<String> country = new ArrayList<>();
                        for (Car car : countryResponse.getResult()) {
                            country.add(car.getName());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, country);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        countySp.setAdapter(adapter);
                    }
                }

                @Override
                public void onFailure(Call<CarResponse> call, Throwable t) {

                }
            });


            carMark.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) return;
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
                    MainRepository.getService().getCity(countryResponse.getResult().get(position).getId()).enqueue(new Callback<CarResponse>() {
                        @Override
                        public void onResponse(Call<CarResponse> call, Response<CarResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                cityResponse = response.body();
                                List<String> city = new ArrayList<>();
                                for (Car car : response.body().getResult()) {
                                    city.add(car.getName());
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, city);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                citySp.setAdapter(adapter);
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
        }
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 28);
            }
        });

    }

    private void setValues(final UserWhenSignedIn user) {
        String userPhone = "+" + user.getUsername();
        editPhoneView.setVisibility(View.VISIBLE);
        name.setText(user.getName());
        email.setText(user.getEmail());
        carry.setText(user.getCarCommonModel().getCarryCapacity() + "");
        phoneEdit.setText(userPhone.replace("+7", "+996").replace("+996", ""));
        if (user.getCarCommonModel() != null) {
            carNumber.setText(user.getCarCommonModel().getCarNumber());
        }
        if (user.getProfilePhoto() != null) {
            Statics.loadImage(profile, user.getProfilePhoto().getUrl(), true);
            try {
                profile.invalidate();
                createFile(((BitmapDrawable) profile.getDrawable()).getBitmap());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (userPhone.startsWith("+7")) {
            codeEdit.setSelection(1);
        }
        MainRepository.getService().getCountry().enqueue(new Callback<CarResponse>() {
            @Override
            public void onResponse(Call<CarResponse> call, Response<CarResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    countryResponse = new CarResponse(response.body().getResult());
                    List<String> country = new ArrayList<>();
                    int current = 0;
                    if (user.getCountryModel() != null) {
                        for (int i = 0; i < countryResponse.getResult().size(); i++) {
                            country.add(countryResponse.getResult().get(i).getName());
                            if (countryResponse.getResult().get(i).getId() == user.getCountryModel().getId()) {
                                current = i;
                            }
                        }
                    } else {
                        for (Car c : response.body().getResult()) {
                            country.add(c.getName());
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, country);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    countySp.setAdapter(adapter);
                    countySp.setSelection(current);
                    MainRepository.getService().getCity(countryResponse.getResult().get(current).getId()).enqueue(new Callback<CarResponse>() {
                        @Override
                        public void onResponse(Call<CarResponse> call, Response<CarResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                cityResponse = response.body();
                                List<String> city = new ArrayList<>();
                                int current = 0;
                                if (user.getCityModel() != null) {
                                    for (int i = 0; i < response.body().getResult().size(); i++) {
                                        city.add(response.body().getResult().get(i).getName());
                                        if (response.body().getResult().get(i).getId() == user.getCityModel().getId()) {
                                            current = i;
                                        }
                                    }
                                } else {
                                    for (Car c : response.body().getResult()) {
                                        city.add(c.getName());
                                    }
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, city);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                citySp.setAdapter(adapter);
                                citySp.setSelection(current);
                                countySp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        MainRepository.getService().getCity(countryResponse.getResult().get(position).getId()).enqueue(new Callback<CarResponse>() {
                                            @Override
                                            public void onResponse(Call<CarResponse> call, Response<CarResponse> response) {
                                                if (response.isSuccessful() && response.body() != null) {
                                                    cityResponse = response.body();
                                                    List<String> city = new ArrayList<>();
                                                    for (Car car : response.body().getResult()) {
                                                        city.add(car.getName());
                                                    }
                                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, city);
                                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                    citySp.setAdapter(adapter);
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
                            }
                        }

                        @Override
                        public void onFailure(Call<CarResponse> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<CarResponse> call, Throwable t) {

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
                    if (user.getCarCommonModel() != null) {
                        for (int i = 0; i < response.body().getResult().size(); i++) {
                            types.add(response.body().getResult().get(i).getName());
                            if (response.body().getResult().get(i).getId() == user.getCarCommonModel().getCarType().getId()) {
                                current = i + 1;
                            }
                        }
                    } else {
                        for (Car c : response.body().getResult()) {
                            types.add(c.getName());
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, types);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    carType.setAdapter(adapter);
                    carType.setSelection(current);
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
                    int current = 0;
                    if (user.getCarCommonModel() != null) {
                        for (int i = 0; i < response.body().getResult().size(); i++) {
                            mark.add(response.body().getResult().get(i).getName());
                            if (response.body().getResult().get(i).getId() == user.getCarCommonModel().getCarBrand().getId()) {
                                current = i + 1;
                            }
                        }
                    } else {
                        for (Car c : response.body().getResult()) {
                            mark.add(c.getName());
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, mark);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    carMark.setAdapter(adapter);
                    carMark.setSelection(current);
                    carMark.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0) return;
                            MainRepository.getService().getCarsModel(carResponse.getResult().get(--position).getId()).enqueue(new Callback<CarResponse>() {
                                @Override
                                public void onResponse(Call<CarResponse> call, Response<CarResponse> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        modelResponse = response.body();
                                        List<String> model = new ArrayList<>();
                                        model.add(getResources().getString(R.string.car_model));
                                        int current = 0;
                                        if (user.getCarCommonModel() != null) {
                                            for (int i = 0; i < response.body().getResult().size(); i++) {
                                                model.add(response.body().getResult().get(i).getName());
                                                if (response.body().getResult().get(i).getId() == user.getCarCommonModel().getCarModel().getId()) {
                                                    current = i + 1;
                                                }
                                            }
                                        } else {
                                            for (Car c : response.body().getResult()) {
                                                model.add(c.getName());
                                            }
                                        }
                                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, model);
                                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        carModel.setAdapter(adapter);
                                        carModel.setSelection(current);
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

                }
            }

            @Override
            public void onFailure(Call<CarResponse> call, Throwable t) {

            }
        });


    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        new Handler().post(new Thread() {
            @Override
            public void run() {
                if (requestCode == 28 && resultCode == RESULT_OK && data != null && data.getData() != null) {
                    Glide.with(profile.getContext()).load(resizeImage(data.getData())).apply(RequestOptions.circleCropTransform()).into(profile);
                }
            }
        });
    }

    private MultipartBody.Part getBody(File file) {
        if (file == null) return null;
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        return MultipartBody.Part.createFormData("file", file.getName(), requestFile);
    }

    private Bitmap resizeImage(Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);


            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            float bitmapRatio = (float) width / (float) height;
            if (bitmapRatio > 1) {
                width = 1000;
                height = (int) (width / bitmapRatio);
            } else {
                height = 1000;
                width = (int) (height * bitmapRatio);
            }
            Bitmap result = Bitmap.createScaledBitmap(bitmap, width, height, true);
            createFile(result);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    private void createFile(Bitmap bitmap) {
        try {
            File f = new File(this.getCacheDir(), "filename");
            f.createNewFile();
//Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 30, bos);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, bos);
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            profileFile = f;
        } catch (Exception e) {
            profileFile = null;
        }
    }
}
