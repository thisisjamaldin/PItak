package com.nextinnovation.pitak.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.asksira.bsimagepicker.BSImagePicker;
import com.bumptech.glide.Glide;
import com.nextinnovation.pitak.R;
import com.nextinnovation.pitak.data.MainRepository;
import com.nextinnovation.pitak.main.MainActivity;
import com.nextinnovation.pitak.model.car.NewCarResponse;
import com.nextinnovation.pitak.model.post.Post;
import com.nextinnovation.pitak.model.user.UserCar;
import com.nextinnovation.pitak.utils.MSharedPreferences;
import com.nextinnovation.pitak.utils.MToast;
import com.nextinnovation.pitak.utils.Statics;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPostActivity extends AppCompatActivity implements BSImagePicker.OnSingleImageSelectedListener, BSImagePicker.ImageLoaderDelegate {

    private ImageView image, image1, image2, image3, image4, back;
    private Button date, time;
    private Button date1, time1;
    private Button save;
    private Spinner carType, serviceType;
    private EditText title, desc, fromPlace, toPlace, payment;
    private CheckBox agreement;
    private ScrollView mainView;
    private File imageFile, image1File, image2File, image3File, image4File;
    private Post post;
    private List<String> cars;
    private NewCarResponse newCarResponse;
    private String advertType;

    public static void start(Context context, Post post) {
        context.startActivity(new Intent(context, AddPostActivity.class).putExtra("post", post));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        initAllView();
        listener();
        post = (Post) getIntent().getSerializableExtra("post");
        if (post != null) {
            setView();
        }
    }

    private void setView() {
        title.setText(post.getTitle());
        desc.setText(post.getText());
        toPlace.setText(post.getToPlace());
        fromPlace.setText(post.getFromPlace());
        payment.setText(post.getAmountPayment());
    }

    private void initAllView() {
        back = findViewById(R.id.add_fragment_back);
        mainView = findViewById(R.id.add_fragment_main_scroll_view_1);
        serviceType = findViewById(R.id.add_fragment_service_type_1);
        title = findViewById(R.id.add_fragment_title_1);
        desc = findViewById(R.id.add_fragment_desc_1);
        fromPlace = findViewById(R.id.add_fragment_from_place_1);
        toPlace = findViewById(R.id.add_fragment_to_place_1);
        payment = findViewById(R.id.add_fragment_payment_1);
        agreement = findViewById(R.id.add_fragment_agreement_1);
        save = findViewById(R.id.add_fragment_post_btn_1);
        carType = findViewById(R.id.add_fragment_car_type_1);
        image = findViewById(R.id.add_fragment_image_1);
        image1 = findViewById(R.id.add_fragment_image_1_1);
        image2 = findViewById(R.id.add_fragment_image_2_1);
        image3 = findViewById(R.id.add_fragment_image_3_1);
        image4 = findViewById(R.id.add_fragment_image_4_1);
        date = findViewById(R.id.add_fragment_date_1);
        date1 = findViewById(R.id.add_fragment_date_1_1);
        time = findViewById(R.id.add_fragment_time_1);
        time1 = findViewById(R.id.add_fragment_time_1_1);
        date.setText(new SimpleDateFormat("EE, dd MMM yyyy").format(Calendar.getInstance().getTime()));
        date1.setText(new SimpleDateFormat("EE, dd MMM yyyy").format(Calendar.getInstance().getTime()));
        time.setText(new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime()));
        time1.setText(new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime()));
    }

    private void listener() {
        if (MSharedPreferences.get(AddPostActivity.this, "who", "").equals(Statics.PASSENGER)) {
            carType.setVisibility(View.GONE);
        }
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddPostActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(y, m, d);
                                date.setText(new SimpleDateFormat("EE, dd MMM yyyy").format(calendar.getTime()));
                            }
                        },
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddPostActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int h, int m) {
                                time.setText(h + ":" + m);
                            }
                        },
                        hour,
                        minute,
                        true);
                timePickerDialog.show();
            }
        });
        date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddPostActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(y, m, d);
                                date1.setText(new SimpleDateFormat("EE, dd MMM yyyy").format(calendar.getTime()));
                            }
                        },
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        time1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        AddPostActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int h, int m) {
                                time1.setText(h + ":" + m);
                            }
                        },
                        hour,
                        minute,
                        true);
                timePickerDialog.show();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Statics.getString(title).length() < 2) {
                    mainView.smoothScrollTo(0, 0);
                    title.setError(getResources().getString(R.string.must_fill));
                    return;
                }
                if (Statics.getString(fromPlace).length() < 1) {
                    mainView.smoothScrollTo(0, 0);
                    fromPlace.setError(getResources().getString(R.string.must_fill));
                    return;
                }
                if (Statics.getString(toPlace).length() < 1) {
                    toPlace.setError(getResources().getString(R.string.must_fill));
                    return;
                }
                if (!agreement.isChecked()) {
                    agreement.setError(getResources().getString(R.string.must_agree));
                    return;
                }
                if (serviceType.getSelectedItemPosition() == 0) {
                    MToast.show(AddPostActivity.this, getResources().getString(R.string.choose_service_type));
                    return;
                }
                save.setVisibility(View.GONE);
                MainActivity.hideKeyboard(AddPostActivity.this, save);
                switch (serviceType.getSelectedItemPosition()){
                    case 1:
                        advertType = "PASSENGER";
                        break;
                    case 2:
                        advertType = "DRIVER";
                        break;
                    case 3:
                        advertType = "PACKAGE";
                        break;
                    case 4:
                        advertType = "SPECIAL_TECH";
                        break;
                    case 5:
                        advertType = "CARGO_TRANSPORTATION";
                        break;
                }
                if (post == null) {
                    MainRepository.getService().savePost(
                            null,
                            getBody(imageFile),
                            getBody(image1File),
                            getBody(image2File),
                            getBody(image3File),
                            getBody(image4File),
                            RequestBody.create(MediaType.parse("text/plain"), Statics.getString(title)),
                            RequestBody.create(MediaType.parse("text/plain"), Statics.getString(desc)),
                            RequestBody.create(MediaType.parse("text/plain"), Statics.getString(payment)),
                            RequestBody.create(MediaType.parse("text/plain"), Statics.getString(fromPlace)),
                            RequestBody.create(MediaType.parse("text/plain"), Statics.getString(toPlace)),
                            RequestBody.create(MediaType.parse("text/plain"), advertType),
                            RequestBody.create(MediaType.parse("text/plain"), "1"),
                            RequestBody.create(MediaType.parse("text/plain"), convertDate(date.getText().toString() + "" + time.getText().toString())),
                            RequestBody.create(MediaType.parse("text/plain"), convertDate(date1.getText().toString() + "" + time1.getText().toString())),
                            RequestBody.create(MediaType.parse("text/plain"), newCarResponse.getResult().get(carType.getSelectedItemPosition()).getId()+""),
                            Statics.getToken(AddPostActivity.this)).enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            if (response.isSuccessful()) {
                                MToast.show(AddPostActivity.this, getResources().getString(R.string.posted));
                                finish();
                            } else {
                                MToast.showResponseError(AddPostActivity.this, response.errorBody());
                            }
                            save.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {
                            MToast.showInternetError(AddPostActivity.this);
                            save.setVisibility(View.VISIBLE);
                        }
                    });
                } else {
                    MainRepository.getService().savePost(
                            RequestBody.create(MediaType.parse("text/plain"), post.getId() + ""),
                            getBody(imageFile),
                            getBody(image1File),
                            getBody(image2File),
                            getBody(image3File),
                            getBody(image4File),
                            RequestBody.create(MediaType.parse("text/plain"), Statics.getString(title)),
                            RequestBody.create(MediaType.parse("text/plain"), Statics.getString(desc)),
                            RequestBody.create(MediaType.parse("text/plain"), Statics.getString(payment)),
                            RequestBody.create(MediaType.parse("text/plain"), Statics.getString(fromPlace)),
                            RequestBody.create(MediaType.parse("text/plain"), Statics.getString(toPlace)),
                            RequestBody.create(MediaType.parse("text/plain"), advertType),
                            RequestBody.create(MediaType.parse("text/plain"), "1"),
                            RequestBody.create(MediaType.parse("text/plain"), convertDate(date.getText().toString() + "" + time.getText().toString())),
                            RequestBody.create(MediaType.parse("text/plain"), convertDate(date1.getText().toString() + "" + time1.getText().toString())),
                            RequestBody.create(MediaType.parse("text/plain"), newCarResponse.getResult().get(carType.getSelectedItemPosition()).getId()+""),
                            Statics.getToken(AddPostActivity.this)).enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            if (response.isSuccessful()) {
                                MToast.show(AddPostActivity.this, getResources().getString(R.string.posted));
                                finish();
                            } else {
                                MToast.showResponseError(AddPostActivity.this, response.errorBody());
                            }
                            save.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {
                            MToast.showInternetError(AddPostActivity.this);
                            save.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });

        MainRepository.getService().getMyCars(Statics.getToken(AddPostActivity.this)).enqueue(new Callback<NewCarResponse>() {
            @Override
            public void onResponse(Call<NewCarResponse> call, Response<NewCarResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    newCarResponse = response.body();
                    List<String> types = new ArrayList<>();
                    for (UserCar car : response.body().getResult()) {
                        types.add(car.getCarBrand().getName());
                    }
                    cars = types;
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(AddPostActivity.this, android.R.layout.simple_spinner_item, types);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    carType.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<NewCarResponse> call, Throwable t) {

            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker("23");
            }
        });
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker("24");
            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker("25");
            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker("26");
            }
        });
        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePicker("27");
            }
        });
        agreement.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivity(new Intent(AddPostActivity.this, AgreementActivity.class));
                return true;
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void loadImage(Uri imageUri, ImageView ivImage) {
        Glide.with(this).load(imageUri).into(ivImage);
    }

    @Override
    public void onSingleImageSelected(Uri uri, String tag) {
        Bitmap bitmap = resizeImage(uri);
        if (bitmap == null) return;
        switch (tag) {
            case "23":
                Glide.with(image.getContext()).load(bitmap).into(image);
                imageFile = createFile(bitmap);
                break;
            case "24":
                Glide.with(image1.getContext()).load(bitmap).into(image1);
                image1File = createFile(bitmap);
                break;
            case "25":
                Glide.with(image2.getContext()).load(bitmap).into(image2);
                image2File = createFile(bitmap);
                break;
            case "26":
                Glide.with(image3.getContext()).load(bitmap).into(image3);
                image3File = createFile(bitmap);
                break;
            case "27":
                Glide.with(image4.getContext()).load(bitmap).into(image4);
                image4File = createFile(bitmap);
                break;
        }
    }

    private void imagePicker(String requestCode) {
        new BSImagePicker.Builder("com.nextinnovation.pitak.fileProvider")
                .hideGalleryTile()
                .setTag(requestCode)
                .build()
                .show(getSupportFragmentManager(), "picker");
    }

    private String convertDate(String dateString) {
        SimpleDateFormat sdfIn = new SimpleDateFormat("EEE, dd MMM yyyyHH:mm", Locale.getDefault());
        Date date = new Date();
        try {
            date = sdfIn.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdfOut = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        return sdfOut.format(date);
    }

    private MultipartBody.Part getBody(File file) {
        if (file == null) return null;
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        return MultipartBody.Part.createFormData("fileList", file.getName(), requestFile);
    }

    private Bitmap resizeImage(Uri uri) {
        try {
            return Bitmap.createScaledBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri), 800, 600, true);
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