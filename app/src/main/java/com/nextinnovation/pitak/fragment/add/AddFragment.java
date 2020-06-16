package com.nextinnovation.pitak.fragment.add;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.asksira.bsimagepicker.BSImagePicker;
import com.bumptech.glide.Glide;
import com.nextinnovation.pitak.R;
import com.nextinnovation.pitak.data.MainRepository;
import com.nextinnovation.pitak.model.car.NewCarResponse;
import com.nextinnovation.pitak.model.user.UserCar;
import com.nextinnovation.pitak.settings.AgreementActivity;
import com.nextinnovation.pitak.main.MainActivity;
import com.nextinnovation.pitak.settings.MyCarActivity;
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

public class AddFragment extends Fragment implements BSImagePicker.OnSingleImageSelectedListener, BSImagePicker.ImageLoaderDelegate {

    private ImageView image, image1, image2, image3, image4;
    private Button date, time;
    private Button date1, time1;
    private Button post;
    private Spinner carType, serviceType;
    private EditText title, desc, fromPlace, toPlace, payment;
    private CheckBox agreement;
    private ScrollView mainView;
    private File imageFile, image1File, image2File, image3File, image4File;
    private List<String> cars;
    private NewCarResponse newCarResponse;
    private String advertType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, null);
        initAllView(view);
        listener();
        return view;
    }

    private void initAllView(View view) {
        mainView = view.findViewById(R.id.add_fragment_main_scroll_view);
        serviceType = view.findViewById(R.id.add_fragment_service_type);
        title = view.findViewById(R.id.add_fragment_title);
        desc = view.findViewById(R.id.add_fragment_desc);
        fromPlace = view.findViewById(R.id.add_fragment_from_place);
        toPlace = view.findViewById(R.id.add_fragment_to_place);
        payment = view.findViewById(R.id.add_fragment_payment);
        agreement = view.findViewById(R.id.add_fragment_agreement);
        post = view.findViewById(R.id.add_fragment_post_btn);
        carType = view.findViewById(R.id.add_fragment_car_type);
        image = view.findViewById(R.id.add_fragment_image);
        image1 = view.findViewById(R.id.add_fragment_image_1);
        image2 = view.findViewById(R.id.add_fragment_image_2);
        image3 = view.findViewById(R.id.add_fragment_image_3);
        image4 = view.findViewById(R.id.add_fragment_image_4);
        date = view.findViewById(R.id.add_fragment_date);
        date1 = view.findViewById(R.id.add_fragment_date_1);
        time = view.findViewById(R.id.add_fragment_time);
        time1 = view.findViewById(R.id.add_fragment_time_1);
        date.setText(new SimpleDateFormat("EE, dd MMM yyyy").format(Calendar.getInstance().getTime()));
        date1.setText(new SimpleDateFormat("EE, dd MMM yyyy").format(Calendar.getInstance().getTime()));
        time.setText(new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime()));
        time1.setText(new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime()));
    }

    private void listener() {
        if (MSharedPreferences.get(getContext(), "who", "").equals(Statics.PASSENGER)) {
            carType.setVisibility(View.GONE);
        }
        carType.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (cars.isEmpty()){
                    carType.setEnabled(false);
                    MyCarActivity.start(getContext());
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        carType.setEnabled(true);
                    }
                }, 500);
                return false;
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(),
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
                        getContext(),
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
                        getContext(),
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
                        getContext(),
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
        post.setOnClickListener(new View.OnClickListener() {
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
                    MToast.show(getContext(), getResources().getString(R.string.choose_service_type));
                    return;
                }
                post.setVisibility(View.GONE);
                MainActivity.hideKeyboard(getActivity(), post);
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
                        Statics.getToken(getContext())).enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (response.isSuccessful()) {
                            MToast.show(getContext(), getResources().getString(R.string.posted));
                            clearFields();
                        } else {
                            MToast.showResponseError(getContext(), response.errorBody());
                        }
                        post.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        MToast.showInternetError(getContext());
                        post.setVisibility(View.VISIBLE);
                    }
                });
            }

            private void clearFields() {
                title.setText("");
                desc.setText("");
                fromPlace.setText("");
                toPlace.setText("");
                payment.setText("");
                image.setImageDrawable(null);
                agreement.setChecked(false);
                ((MainActivity) getActivity()).openMain();
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
                startActivity(new Intent(getContext(), AgreementActivity.class));
                return true;
            }
        });
    }

    private MultipartBody.Part getBody(File file) {
        if (file == null) return null;
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        return MultipartBody.Part.createFormData("fileList", file.getName(), requestFile);
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

    private Bitmap resizeImage(Uri uri) {
        try {
            return Bitmap.createScaledBitmap(MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri), 800, 600, true);
        } catch (Exception e) {
            return null;
        }
    }

    private File createFile(Bitmap bitmap) {
        try {
            File f = new File(getContext().getCacheDir(), "filename");
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

    @Override
    public void loadImage(Uri imageUri, ImageView ivImage) {
        Glide.with(getContext()).load(imageUri).into(ivImage);
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
                .show(getChildFragmentManager(), "picker");
    }

    @Override
    public void onResume() {
        super.onResume();
        MainRepository.getService().getMyCars(Statics.getToken(getContext())).enqueue(new Callback<NewCarResponse>() {
            @Override
            public void onResponse(Call<NewCarResponse> call, Response<NewCarResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    newCarResponse = response.body();
                    List<String> types = new ArrayList<>();
                    for (UserCar car : response.body().getResult()) {
                        types.add(car.getCarBrand().getName());
                    }
                    cars = types;
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, types);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    carType.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<NewCarResponse> call, Throwable t) {

            }
        });
    }
}
