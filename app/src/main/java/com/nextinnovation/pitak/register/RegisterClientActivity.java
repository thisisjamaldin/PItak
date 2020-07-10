package com.nextinnovation.pitak.register;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.nextinnovation.pitak.R;
import com.nextinnovation.pitak.data.MainRepository;
import com.nextinnovation.pitak.main.MainActivity;
import com.nextinnovation.pitak.model.car.Car;
import com.nextinnovation.pitak.model.car.CarResponse;
import com.nextinnovation.pitak.model.car.NewCarResponse;
import com.nextinnovation.pitak.model.user.EditUser;
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
import java.net.URL;
import java.util.ArrayList;
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
    private int maxSize = 1000;

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
                    countryModel = new Car(1, null);
                }
                if (cityResponse != null && !cityResponse.getResult().isEmpty()) {
                    cityModel = new Car(cityResponse.getResult().get(city.getSelectedItemPosition()).getId(), cityResponse.getResult().get(city.getSelectedItemPosition()).getName());
                } else {
                    cityModel = new Car(1, null);
                }
                if (edit) {
                    final EditUser editUser = new EditUser(null, cityModel, countryModel, Statics.getString(email), userToEdit.getId(), Statics.getString(name), codeEdit.getSelectedItem().toString().replace("+", "") + Statics.getString(phoneEdit), "patronymic", "surname", Statics.PASSENGER, codeEdit.getSelectedItem().toString().replace("+", "") + Statics.getString(phoneEdit));
                    final UserWhenSignedIn userWhenSignedIn = new UserWhenSignedIn(userToEdit.getId(), codeEdit.getSelectedItem().toString().replace("+", "") + Statics.getString(phoneEdit), "surname", "patronymic", Statics.getString(name), Statics.getString(email), null, Statics.getToken(context).substring(7), codeEdit.getSelectedItem().toString().replace("+", "") + Statics.getString(phoneEdit), countryModel, cityModel);
                    if (userToEdit.getUsername().equals(editUser.getUsername())) {
                        MainRepository.getService().editClient(editUser, Statics.getToken(RegisterClientActivity.this)).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    if (profileFile != null) {
                                        MainRepository.getService().setUserProfile(getBody(profileFile), Statics.getToken(RegisterClientActivity.this)).enqueue(new Callback<ProfileResponse>() {
                                            @Override
                                            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                                                if (response.isSuccessful()) {
                                                    userWhenSignedIn.setProfilePhoto(new ProfileRequest(response.body().getResult().getUrl()));
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
                        MSharedPreferences.set(RegisterClientActivity.this, "phone", editUser.getUsername());
                        MSharedPreferences.set(RegisterClientActivity.this, "userToEdit", editUser);
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
                    ).enqueue(new Callback<UserWhenSignedIn>() {
                        @Override
                        public void onResponse(Call<UserWhenSignedIn> call, Response<UserWhenSignedIn> response) {
                            if (response.isSuccessful()) {
                                MSharedPreferences.set(RegisterClientActivity.this, Statics.REGISTERED, true);
                                MainRepository.getService().signIn(new UserSignIn(phone, phone, Statics.PASSENGER)).enqueue(new Callback<UserWhenSignedIn>() {
                                    @Override
                                    public void onResponse(Call<UserWhenSignedIn> call, Response<UserWhenSignedIn> response) {
                                        if (response.isSuccessful()) {
                                            Statics.setToken(RegisterClientActivity.this, response.body().getToken());
                                            MainRepository.getService().getMe(Statics.getToken(RegisterClientActivity.this)).enqueue(new Callback<User>() {
                                                @Override
                                                public void onResponse(Call<User> call, Response<User> response) {
                                                    MSharedPreferences.set(RegisterClientActivity.this, Statics.USER, new Gson().toJson(response.body().getResult()));
                                                    if (response.body().getResult().getUserType().equals("PASSENGER")) {
                                                        MSharedPreferences.set(RegisterClientActivity.this, "who", Statics.PASSENGER);
                                                    }
                                                    if (response.body().getResult().getUserType().equals("DRIVER")) {
                                                        MSharedPreferences.set(RegisterClientActivity.this, "who", Statics.DRIVER);
                                                        MainRepository.getService().getMyCars(Statics.getToken(RegisterClientActivity.this)).enqueue(new Callback<NewCarResponse>() {
                                                            @Override
                                                            public void onResponse(Call<NewCarResponse> call, Response<NewCarResponse> response) {
                                                                UserWhenSignedIn uws = new Gson().fromJson(MSharedPreferences.get(RegisterClientActivity.this, Statics.USER, ""), UserWhenSignedIn.class);
                                                                if (response.body().getResult().isEmpty())
                                                                    return;
                                                                uws.setCarCommonModel(response.body().getResult().get(0).getCarCommonModel());
                                                                MSharedPreferences.set(RegisterClientActivity.this, Statics.USER, new Gson().toJson(uws));
                                                            }

                                                            @Override
                                                            public void onFailure(Call<NewCarResponse> call, Throwable t) {

                                                            }
                                                        });
                                                    }
                                                    MainActivity.start(RegisterClientActivity.this);
                                                }

                                                @Override
                                                public void onFailure(Call<User> call, Throwable t) {

                                                }
                                            });
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
                }
            }
        });

        if (!edit) {
            MainRepository.getService().getCountry().enqueue(new Callback<CarResponse>() {
                @Override
                public void onResponse(Call<CarResponse> call, Response<CarResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        countryResponse = new CarResponse(response.body().getResult());
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
        }

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 29);
            }
        });
    }

    private void setValues(final UserWhenSignedIn user) {
        String userPhone = "+" + user.getUsername();
        editPhoneView.setVisibility(View.VISIBLE);
        name.setText(user.getName());
        email.setText(user.getEmail());
        phoneEdit.setText(userPhone.replace("+7", "+996").replace("+996", ""));
        if (user.getProfilePhoto() != null) {
            Statics.loadImage(profile, user.getProfilePhoto().getUrl(), true);
            try {
                profile.invalidate();
                createFile(((BitmapDrawable)profile.getDrawable()).getBitmap());
            } catch (Exception e){e.printStackTrace();}
        }
        if (userPhone.startsWith("+7")) {
            codeEdit.setSelection(1);
        }
        if (user.getCountryModel() != null && user.getCityModel() != null) {
            MainRepository.getService().getCountry().enqueue(new Callback<CarResponse>() {
                @Override
                public void onResponse(Call<CarResponse> call, Response<CarResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        countryResponse = new CarResponse(response.body().getResult());
                        List<String> list = new ArrayList<>();
                        int current = 0;
                        for (int i = 0; i < countryResponse.getResult().size(); i++) {
                            list.add(countryResponse.getResult().get(i).getName());
                            if (countryResponse.getResult().get(i).getId() == user.getCountryModel().getId()) {
                                current = i;
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, list);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        country.setAdapter(adapter);
                        country.setSelection(current);
                        MainRepository.getService().getCity(countryResponse.getResult().get(current).getId()).enqueue(new Callback<CarResponse>() {
                            @Override
                            public void onResponse(Call<CarResponse> call, Response<CarResponse> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    cityResponse = response.body();
                                    List<String> list1 = new ArrayList<>();
                                    int current = 0;
                                    for (int i = 0; i < response.body().getResult().size(); i++) {
                                        list1.add(response.body().getResult().get(i).getName());
                                        if (response.body().getResult().get(i).getId() == user.getCityModel().getId()) {
                                            current = i;
                                        }
                                    }
                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, list1);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    city.setAdapter(adapter);
                                    city.setSelection(current);
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
        } else {
            MainRepository.getService().getCountry().enqueue(new Callback<CarResponse>() {
                @Override
                public void onResponse(Call<CarResponse> call, Response<CarResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        countryResponse = new CarResponse(response.body().getResult());
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
        }
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
            bitmap.compress(Bitmap.CompressFormat.PNG, 10, bos);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, bos);
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
