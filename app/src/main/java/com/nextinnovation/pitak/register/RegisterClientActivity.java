package com.nextinnovation.pitak.register;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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
import com.nextinnovation.pitak.model.car.CarResponse;
import com.nextinnovation.pitak.model.user.ProfileRequest;
import com.nextinnovation.pitak.model.user.ProfileResponse;
import com.nextinnovation.pitak.model.user.User;
import com.nextinnovation.pitak.model.user.UserSignIn;
import com.nextinnovation.pitak.model.user.UserWhenSignedIn;
import com.nextinnovation.pitak.utils.MSharedPreferences;
import com.nextinnovation.pitak.utils.MToast;
import com.nextinnovation.pitak.utils.Statics;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterClientActivity extends AppCompatActivity {

    private ImageView back;
    private ImageView profile;
    private Button save;
    private EditText name;
    private EditText email;
    private View editPhoneView;
    private EditText phoneEdit;
    private Spinner codeEdit;

    private UserWhenSignedIn userToEdit;
    private boolean edit;
    private String phone;
    private File profileFile;
    private Spinner country, city;
    private CarResponse countryResponse, cityResponse;
    private Context context;

    public static void start(Context context, boolean edit) {
        context.startActivity(new Intent(context, RegisterClientActivity.class).putExtra("edit", edit));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_client);

        context = this;
        edit = getIntent().getBooleanExtra("edit", false);
        phone = MSharedPreferences.get(RegisterClientActivity.this, "phone", "").replace("+", "");
        initView();
        listener();
    }

    private void initView() {
        phoneEdit = findViewById(R.id.register_client_phone_et);
        editPhoneView = findViewById(R.id.register_client_phone_layout);
        codeEdit = findViewById(R.id.register_client_phone_code_sp);
        back = findViewById(R.id.register_client_back_img);
        profile = findViewById(R.id.register_client_profile_img);
        save = findViewById(R.id.register_client_save_btn);
        name = findViewById(R.id.register_client_name_et);
        email = findViewById(R.id.register_client_email_et);
        country = findViewById(R.id.register_client_county_sp);
        city = findViewById(R.id.register_client_city_sp);
    }

    private void listener() {
        if (edit) {
            userToEdit = new Gson().fromJson(MSharedPreferences.get(RegisterClientActivity.this, Statics.USER, ""), UserWhenSignedIn.class);
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
                save.setVisibility(View.GONE);
                Car countryModel;
                Car cityModel;
                if (countryResponse != null && !countryResponse.getResult().isEmpty()) {
                    countryModel = new Car(countryResponse.getResult().get(country.getSelectedItemPosition()).getId(), countryResponse.getResult().get(country.getSelectedItemPosition()).getName());
                } else {
                    countryModel = new Car(null, null);
                }
                if (cityResponse != null && !cityResponse.getResult().isEmpty()) {
                    cityModel = new Car(cityResponse.getResult().get(city.getSelectedItemPosition()).getId(), cityResponse.getResult().get(city.getSelectedItemPosition()).getName());
                } else {
                    cityModel = new Car(null, null);
                }
                if (edit) {
                    final UserWhenSignedIn userWhenSignedIn = new UserWhenSignedIn(userToEdit.getId(), codeEdit.getSelectedItem().toString().replace("+", "") + Statics.getString(phoneEdit), "surname", "patronymic", Statics.getString(name), Statics.getString(email), null, Statics.getToken(context).substring(7), codeEdit.getSelectedItem().toString().replace("+", "") + Statics.getString(phoneEdit), countryModel, cityModel);
                    if (userToEdit.getUsername().equals(userWhenSignedIn.getUsername())) {
                        MainRepository.getService().editClient(userWhenSignedIn, Statics.getToken(RegisterClientActivity.this)).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    if (profileFile != null) {
                                        MainRepository.getService().setUserProfile(getBody(profileFile), Statics.getToken(RegisterClientActivity.this)).enqueue(new Callback<ProfileResponse>() {
                                            @Override
                                            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                                                if (response.isSuccessful()) {
                                                    userWhenSignedIn.setProfilePhoto(new ProfileRequest(response.body().getResult().getContent()));
                                                    MSharedPreferences.set(RegisterClientActivity.this, Statics.USER, new Gson().toJson(userWhenSignedIn));
                                                    finish();
                                                } else {
                                                    MToast.showResponseError(RegisterClientActivity.this, response.errorBody());
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                                                MToast.showInternetError(RegisterClientActivity.this);
                                                save.setVisibility(View.VISIBLE);
                                            }
                                        });
                                    } else {
                                        MSharedPreferences.set(RegisterClientActivity.this, Statics.USER, new Gson().toJson(userWhenSignedIn));
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
                                    MToast.showResponseError(RegisterClientActivity.this, response.errorBody());
                                    save.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                MToast.showInternetError(RegisterClientActivity.this);
                                save.setVisibility(View.VISIBLE);
                            }
                        });
                    } else {
                        MSharedPreferences.set(RegisterClientActivity.this, "phone", userWhenSignedIn.getUsername());
                        MSharedPreferences.set(RegisterClientActivity.this, "userToEdit", userWhenSignedIn);
                        CodeAuthenticationActivity.start(RegisterClientActivity.this, Statics.PASSENGER);
                        save.setVisibility(View.VISIBLE);
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
                            RequestBody.create(MediaType.parse("text/plain"), "1"),
                            RequestBody.create(MediaType.parse("text/plain"), "1"),
                            RequestBody.create(MediaType.parse("text/plain"), "a"),
                            RequestBody.create(MediaType.parse("text/plain"), "1"),
                            RequestBody.create(MediaType.parse("text/plain"), countryModel.getId() + ""),
                            RequestBody.create(MediaType.parse("text/plain"), cityModel.getId() + "")
                    ).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful()) {
                                MainRepository.getService().signIn(new UserSignIn(phone, phone)).enqueue(new Callback<UserWhenSignedIn>() {
                                    @Override
                                    public void onResponse(Call<UserWhenSignedIn> call, Response<UserWhenSignedIn> response) {
                                        if (response.isSuccessful()) {
                                            MSharedPreferences.set(RegisterClientActivity.this, Statics.REGISTERED, true);
                                            if (response.body().getRoles()[0].equals("ROLE_DRIVER")) {
                                                MSharedPreferences.set(RegisterClientActivity.this, "who", Statics.DRIVER);
                                            }
                                            if (response.body().getRoles()[0].equals("ROLE_PASSENGER")) {
                                                MSharedPreferences.set(RegisterClientActivity.this, "who", Statics.PASSENGER);
                                            }
                                            MSharedPreferences.set(RegisterClientActivity.this, Statics.USER, new Gson().toJson(response.body()));
                                            MainActivity.start(RegisterClientActivity.this);
                                        } else {
                                            MToast.showResponseError(RegisterClientActivity.this, response.errorBody());
                                            save.setVisibility(View.VISIBLE);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<UserWhenSignedIn> call, Throwable t) {
                                        MToast.showInternetError(RegisterClientActivity.this);
                                        save.setVisibility(View.VISIBLE);
                                    }
                                });
                            } else {
                                Log.e("---------if", "else");
                                MToast.showResponseError(RegisterClientActivity.this, response.errorBody());
                                save.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            MToast.showInternetError(RegisterClientActivity.this);
                            save.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });

        MainRepository.getService().getCountry().enqueue(new Callback<CarResponse>() {
            @Override
            public void onResponse(Call<CarResponse> call, Response<CarResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    countryResponse = new CarResponse(response.body().getResult());
                    Collections.sort(countryResponse.getResult());
                    List<String> list = new ArrayList<>();
                    for (Car car : countryResponse.getResult()) {
                        list.add(car.getName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    country.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<CarResponse> call, Throwable t) {

            }
        });

        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MainRepository.getService().getCity(countryResponse.getResult().get(position).getId()).enqueue(new Callback<CarResponse>() {
                    @Override
                    public void onResponse(Call<CarResponse> call, Response<CarResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            cityResponse = response.body();
                            List<String> cityList = new ArrayList<>();
                            for (Car car : response.body().getResult()) {
                                cityList.add(car.getName());
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, cityList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            city.setAdapter(adapter);
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

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 29);
            }
        });
    }

    private void setValues(UserWhenSignedIn user) {
        String userPhone = "+" + user.getUsername();
        editPhoneView.setVisibility(View.VISIBLE);
        name.setText(user.getName());
        email.setText(user.getEmail());
        phoneEdit.setText(userPhone.replace("+7", "+996").replace("+996", ""));
        setProfile(user.getProfilePhoto());
        if (userPhone.startsWith("+7")) {
            codeEdit.setSelection(1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 29 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Glide.with(profile.getContext()).load(resizeImage(data.getData())).apply(RequestOptions.circleCropTransform()).into(profile);
            profileFile = createFile(resizeImage(data.getData()));
        }
    }

    private void setProfile(ProfileRequest profileModel) {
        if (profileModel != null) {
            byte[] decodedString = Base64.decode(profileModel.getContent(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Glide.with(profile.getContext()).load(decodedByte).apply(RequestOptions.circleCropTransform()).into(profile);
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
