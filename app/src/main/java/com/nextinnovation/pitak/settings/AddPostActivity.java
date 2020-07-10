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
import android.util.Log;
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
import com.nextinnovation.pitak.model.car.Car;
import com.nextinnovation.pitak.model.car.CarCommonModel;
import com.nextinnovation.pitak.model.car.CarResponse;
import com.nextinnovation.pitak.model.car.NewCarResponse;
import com.nextinnovation.pitak.model.post.AppAdvertModel;
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
import java.util.Arrays;
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
    private Button date;
    private Button date1;
    private Button save;
    private Spinner carType, serviceType;
    private EditText title, desc, fromPlace, toPlace, payment;
    private CheckBox agreement;
    private ScrollView mainView;
    private File imageFile, image1File, image2File, image3File, image4File;
    private AppAdvertModel post;
    private NewCarResponse newCarResponse;
    private String advertType;
    private long advertTypeId;
    private List<Car> services = new ArrayList<>();

    public static void start(Context context, AppAdvertModel post) {
        context.startActivity(new Intent(context, AddPostActivity.class).putExtra("post", post));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        initAllView();
        listener();
        post = (AppAdvertModel) getIntent().getSerializableExtra("post");
        if (post != null) {
            setView();
        }
    }

    private void setView() {
        title.setText(post.getAppAdvertModel().getTitle());
        desc.setText(post.getAppAdvertModel().getText());
        toPlace.setText(post.getAppAdvertModel().getToPlace());
        fromPlace.setText(post.getAppAdvertModel().getFromPlace());
        payment.setText(post.getAppAdvertModel().getAmountPayment());
        switch (post.getAttachmentModels().size()) {
            case 1:
                Statics.loadImage(image, post.getAttachmentModels().get(0).getAppFile().getUrl(), null);
                break;
            case 2:
                Statics.loadImage(image, post.getAttachmentModels().get(0).getAppFile().getUrl(), null);
                Statics.loadImage(image1, post.getAttachmentModels().get(1).getAppFile().getUrl(), null);
                break;
            case 3:
                Statics.loadImage(image, post.getAttachmentModels().get(0).getAppFile().getUrl(), null);
                Statics.loadImage(image1, post.getAttachmentModels().get(1).getAppFile().getUrl(), null);
                Statics.loadImage(image2, post.getAttachmentModels().get(2).getAppFile().getUrl(), null);
                break;
            case 4:
                Statics.loadImage(image, post.getAttachmentModels().get(0).getAppFile().getUrl(), null);
                Statics.loadImage(image1, post.getAttachmentModels().get(1).getAppFile().getUrl(), null);
                Statics.loadImage(image2, post.getAttachmentModels().get(2).getAppFile().getUrl(), null);
                Statics.loadImage(image3, post.getAttachmentModels().get(3).getAppFile().getUrl(), null);
                break;
            case 5:
                Statics.loadImage(image, post.getAttachmentModels().get(0).getAppFile().getUrl(), null);
                Statics.loadImage(image1, post.getAttachmentModels().get(1).getAppFile().getUrl(), null);
                Statics.loadImage(image2, post.getAttachmentModels().get(2).getAppFile().getUrl(), null);
                Statics.loadImage(image3, post.getAttachmentModels().get(3).getAppFile().getUrl(), null);
                Statics.loadImage(image4, post.getAttachmentModels().get(4).getAppFile().getUrl(), null);
                break;
        }
    }

    private void initAllView() {
        back = findViewById(R.id.add_activity_back);
        mainView = findViewById(R.id.add_activity_main_scroll_view);
        serviceType = findViewById(R.id.add_activity_service_type);
        title = findViewById(R.id.add_activity_title);
        desc = findViewById(R.id.add_activity_desc);
        fromPlace = findViewById(R.id.add_activity_from_place);
        toPlace = findViewById(R.id.add_activity_to_place);
        payment = findViewById(R.id.add_activity_payment);
        agreement = findViewById(R.id.add_activity_agreement);
        save = findViewById(R.id.add_activity_post_btn);
        carType = findViewById(R.id.add_activity_car_type);
        image = findViewById(R.id.add_activity_image);
        image1 = findViewById(R.id.add_activity_image_1);
        image2 = findViewById(R.id.add_activity_image_2);
        image3 = findViewById(R.id.add_activity_image_3);
        image4 = findViewById(R.id.add_activity_image_4);
        date = findViewById(R.id.add_activity_date);
        date1 = findViewById(R.id.add_activity_date_1);
        date.setText(new SimpleDateFormat("EE, dd MMM yyyy").format(Calendar.getInstance().getTime()));
        date1.setText(new SimpleDateFormat("EE, dd MMM yyyy").format(Calendar.getInstance().getTime()));
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
                advertType = services.get(serviceType.getSelectedItemPosition()).getName();
                advertTypeId = services.get(serviceType.getSelectedItemPosition()).getId();
                if (post == null) {
                    if (newCarResponse!=null) {
                        MainRepository.getService().savePost(
                                null,
                                Arrays.asList(getBody(imageFile), getBody(image1File), getBody(image2File), getBody(image3File), getBody(image4File)),
                                RequestBody.create(MediaType.parse("text/plain"), Statics.getString(title)),
                                RequestBody.create(MediaType.parse("text/plain"), Statics.getString(desc)),
                                RequestBody.create(MediaType.parse("text/plain"), Statics.getString(payment)),
                                RequestBody.create(MediaType.parse("text/plain"), Statics.getString(fromPlace)),
                                RequestBody.create(MediaType.parse("text/plain"), Statics.getString(toPlace)),
                                RequestBody.create(MediaType.parse("text/plain"), advertTypeId+""),
                                RequestBody.create(MediaType.parse("text/plain"), advertType),
                                RequestBody.create(MediaType.parse("text/plain"), "1"),
                                RequestBody.create(MediaType.parse("text/plain"), convertDate(date.getText().toString() + "00:00")),
                                RequestBody.create(MediaType.parse("text/plain"), convertDate(date1.getText().toString() + "00:00")),
                                RequestBody.create(MediaType.parse("text/plain"), newCarResponse.getResult().get(carType.getSelectedItemPosition()).getCarCommonModel().getId() + ""),
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
                                null,
                                Arrays.asList(getBody(imageFile), getBody(image1File), getBody(image2File), getBody(image3File), getBody(image4File)),
                                RequestBody.create(MediaType.parse("text/plain"), Statics.getString(title)),
                                RequestBody.create(MediaType.parse("text/plain"), Statics.getString(desc)),
                                RequestBody.create(MediaType.parse("text/plain"), Statics.getString(payment)),
                                RequestBody.create(MediaType.parse("text/plain"), Statics.getString(fromPlace)),
                                RequestBody.create(MediaType.parse("text/plain"), Statics.getString(toPlace)),
                                RequestBody.create(MediaType.parse("text/plain"), advertTypeId+""),
                                RequestBody.create(MediaType.parse("text/plain"), advertType),
                                RequestBody.create(MediaType.parse("text/plain"), "1"),
                                RequestBody.create(MediaType.parse("text/plain"), convertDate(date.getText().toString() + "00:00")),
                                RequestBody.create(MediaType.parse("text/plain"), convertDate(date1.getText().toString() + "00:00")),
                                RequestBody.create(MediaType.parse("text/plain"), ""),
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
                } else {
                    if (newCarResponse!=null) {
                        MainRepository.getService().savePost(
                                RequestBody.create(MediaType.parse("text/plain"), post.getAppAdvertModel().getId() + ""),
                                Arrays.asList(getBody(imageFile), getBody(image1File), getBody(image2File), getBody(image3File), getBody(image4File)),
                                RequestBody.create(MediaType.parse("text/plain"), Statics.getString(title)),
                                RequestBody.create(MediaType.parse("text/plain"), Statics.getString(desc)),
                                RequestBody.create(MediaType.parse("text/plain"), Statics.getString(payment)),
                                RequestBody.create(MediaType.parse("text/plain"), Statics.getString(fromPlace)),
                                RequestBody.create(MediaType.parse("text/plain"), Statics.getString(toPlace)),
                                RequestBody.create(MediaType.parse("text/plain"), advertTypeId+""),
                                RequestBody.create(MediaType.parse("text/plain"), advertType),
                                RequestBody.create(MediaType.parse("text/plain"), "1"),
                                RequestBody.create(MediaType.parse("text/plain"), convertDate(date.getText().toString() + "00:00")),
                                RequestBody.create(MediaType.parse("text/plain"), convertDate(date1.getText().toString() + "00:00")),
                                RequestBody.create(MediaType.parse("text/plain"), newCarResponse.getResult().get(carType.getSelectedItemPosition()).getCarCommonModel().getId() + ""),
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
                                RequestBody.create(MediaType.parse("text/plain"), post.getAppAdvertModel().getId() + ""),
                                Arrays.asList(getBody(imageFile), getBody(image1File), getBody(image2File), getBody(image3File), getBody(image4File)),
                                RequestBody.create(MediaType.parse("text/plain"), Statics.getString(title)),
                                RequestBody.create(MediaType.parse("text/plain"), Statics.getString(desc)),
                                RequestBody.create(MediaType.parse("text/plain"), Statics.getString(payment)),
                                RequestBody.create(MediaType.parse("text/plain"), Statics.getString(fromPlace)),
                                RequestBody.create(MediaType.parse("text/plain"), Statics.getString(toPlace)),
                                RequestBody.create(MediaType.parse("text/plain"), advertTypeId+""),
                                RequestBody.create(MediaType.parse("text/plain"), advertType),
                                RequestBody.create(MediaType.parse("text/plain"), "1"),
                                RequestBody.create(MediaType.parse("text/plain"), convertDate(date.getText().toString() + "00:00")),
                                RequestBody.create(MediaType.parse("text/plain"), convertDate(date1.getText().toString() + "00:00")),
                                RequestBody.create(MediaType.parse("text/plain"), ""),
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
            }
        });

        MainRepository.getService().getMyCars(Statics.getToken(AddPostActivity.this)).enqueue(new Callback<NewCarResponse>() {
            @Override
            public void onResponse(Call<NewCarResponse> call, Response<NewCarResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    newCarResponse = response.body();
                    List<String> types = new ArrayList<>();
                    for (CarCommonModel carModel : response.body().getResult()) {
                        UserCar car = carModel.getCarCommonModel();
                        types.add(car.getCarBrand().getName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(AddPostActivity.this, android.R.layout.simple_spinner_item, types);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    carType.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<NewCarResponse> call, Throwable t) {

            }
        });
        MainRepository.getService().getServices().enqueue(new Callback<CarResponse>() {
            @Override
            public void onResponse(Call<CarResponse> call, Response<CarResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    services.clear();
                    services.addAll(response.body().getResult());
                    List<String> list = new ArrayList<>();
                    for (Car car : services) {
                        list.add(car.getName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(AddPostActivity.this, android.R.layout.simple_spinner_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    serviceType.setAdapter(adapter);
                    if (post!=null){
                        for (int i=0; i< list.size();i++){
                            if (list.get(i).equals(post.getAppAdvertModel().getTypeService().getName())){
                                serviceType.setSelection(i);
                            }

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CarResponse> call, Throwable t) {

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
        return MultipartBody.Part.createFormData("multiUploadRequest.fileList", file.getName(), requestFile);
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
            return Bitmap.createScaledBitmap(bitmap, width, height, true);
        } catch (Exception e) {
            return null;
        }
    }

    private File createFile(Bitmap bitmap) {
        try {
            File f = new File(this.getCacheDir(), "filename3");
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