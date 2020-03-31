package com.nextinnovation.pitak.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.google.gson.Gson;
import com.nextinnovation.pitak.R;
import com.nextinnovation.pitak.data.MainRepository;
import com.nextinnovation.pitak.main.MainActivity;
import com.nextinnovation.pitak.model.car.Car;
import com.nextinnovation.pitak.model.car.CarResponse;
import com.nextinnovation.pitak.model.user.User;
import com.nextinnovation.pitak.model.user.UserSignIn;
import com.nextinnovation.pitak.model.user.UserWhenSignedIn;
import com.nextinnovation.pitak.utils.MSharedPreferences;
import com.nextinnovation.pitak.utils.MToast;
import com.nextinnovation.pitak.utils.Statics;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    private String phone;
    private boolean edit;
    private View editPhoneView;
    private EditText phoneEdit;
    private Spinner codeEdit;

    private CarResponse carResponse;

    private UserWhenSignedIn userToEdit;

    public static void start(Context context, boolean edit) {
        context.startActivity(new Intent(context, RegisterDriverActivity.class).putExtra("edit", edit));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_driver);

        edit = getIntent().getBooleanExtra("edit", false);
        phone = MSharedPreferences.get(RegisterDriverActivity.this, "phone", "").replace("+", "");
        initView();
        listener();
    }

    private void initView() {
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
            userToEdit = new Gson().fromJson(MSharedPreferences.get(RegisterDriverActivity.this, Statics.USER, ""), UserWhenSignedIn.class);
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
                    MToast.show(RegisterDriverActivity.this, getResources().getString(R.string.select_your_vehicle));
                    return;
                }
                save.setVisibility(View.GONE);
                if (edit) {
                    final UserWhenSignedIn userWhenSignedIn = new UserWhenSignedIn(userToEdit.getId(), codeEdit.getSelectedItem().toString().replace("+", "") + Statics.getString(phoneEdit), "surname", "patronymic", Statics.getString(name), Statics.getString(email), null, null, null, codeEdit.getSelectedItem().toString().replace("+", "") + Statics.getString(phoneEdit));
                    if (userToEdit.getUsername().equals(userWhenSignedIn.getUsername())) {
                        MainRepository.getService().editDriver(userWhenSignedIn, Statics.getToken(RegisterDriverActivity.this)).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    MSharedPreferences.set(RegisterDriverActivity.this, Statics.USER, new Gson().toJson(userWhenSignedIn));
                                    finish();
                                } else {
                                    MToast.showResponseError(RegisterDriverActivity.this, response.errorBody());
                                    save.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                MToast.showInternetError(RegisterDriverActivity.this);
                                save.setVisibility(View.VISIBLE);
                            }
                        });
                    } else {
                        MSharedPreferences.set(RegisterDriverActivity.this, "phone", userWhenSignedIn.getUsername());
                        MSharedPreferences.set(RegisterDriverActivity.this, "userToEdit", userWhenSignedIn);
                        CodeAuthenticationActivity.start(RegisterDriverActivity.this, Statics.DRIVER);
                    }
                } else {
                    User user = new User(phone, email.getText().toString().trim(), phone, "surname", Statics.getString(name), "patronymic", Statics.DRIVER, "", carMark.getSelectedItem().toString() + " " + carModel.getSelectedItem().toString(), Statics.getString(carNumber));
                    MainRepository.getService().signUp(user).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful()) {
                                MSharedPreferences.set(RegisterDriverActivity.this, Statics.REGISTERED, true);
                                MainRepository.getService().signIn(new UserSignIn(phone, phone)).enqueue(new Callback<UserWhenSignedIn>() {
                                    @Override
                                    public void onResponse(Call<UserWhenSignedIn> call, Response<UserWhenSignedIn> response) {
                                        if (response.isSuccessful()) {
                                            if (response.body().getRoles()[0].equals("ROLE_DRIVER")) {
                                                MSharedPreferences.set(RegisterDriverActivity.this, "who", Statics.DRIVER);
                                            }
                                            if (response.body().getRoles()[0].equals("ROLE_PASSENGER")) {
                                                MSharedPreferences.set(RegisterDriverActivity.this, "who", Statics.PASSENGER);
                                            }
                                            MSharedPreferences.set(RegisterDriverActivity.this, Statics.USER, new Gson().toJson(response.body()));
                                            MainActivity.start(RegisterDriverActivity.this);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<UserWhenSignedIn> call, Throwable t) {
                                        MToast.showInternetError(RegisterDriverActivity.this);
                                        save.setVisibility(View.VISIBLE);
                                    }
                                });
                            } else {
                                MToast.show(RegisterDriverActivity.this, Statics.getResponseError(response.errorBody()));
                            }
                            save.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            MToast.showInternetError(RegisterDriverActivity.this);
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
                    List<String> types = new ArrayList<>();
                    types.add(getResources().getString(R.string.car_type));
                    for (Car car : response.body().getResult()) {
                        types.add(car.getName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(RegisterDriverActivity.this, android.R.layout.simple_spinner_item, types);
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(RegisterDriverActivity.this, android.R.layout.simple_spinner_item, mark);
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
                            List<String> model = new ArrayList<>();
                            model.add(getResources().getString(R.string.car_model));
                            for (Car car : response.body().getResult()) {
                                model.add(car.getName());
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(RegisterDriverActivity.this, android.R.layout.simple_spinner_item, model);
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
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(RegisterDriverActivity.this, android.R.layout.simple_spinner_item, city);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        citySp.setAdapter(adapter);
                        break;
                    case 2:
                        city = getResources().getStringArray(R.array.city_ru);
                        adapter = new ArrayAdapter<>(RegisterDriverActivity.this, android.R.layout.simple_spinner_item, city);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        citySp.setAdapter(adapter);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
}
