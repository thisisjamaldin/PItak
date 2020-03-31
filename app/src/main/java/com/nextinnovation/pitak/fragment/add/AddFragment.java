package com.nextinnovation.pitak.fragment.add;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.nextinnovation.pitak.R;
import com.nextinnovation.pitak.data.MainRepository;
import com.nextinnovation.pitak.main.MainActivity;
import com.nextinnovation.pitak.model.car.Car;
import com.nextinnovation.pitak.model.car.CarResponse;
import com.nextinnovation.pitak.model.post.PostCreate;
import com.nextinnovation.pitak.model.user.User;
import com.nextinnovation.pitak.register.RegisterDriverActivity;
import com.nextinnovation.pitak.utils.MSharedPreferences;
import com.nextinnovation.pitak.utils.MToast;
import com.nextinnovation.pitak.utils.Statics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFragment extends Fragment {

    private ImageView image;
    private TextView imageText;
    private Button date, time;
    private Button date1, time1;
    private Button post;
    private Spinner carType;
    private EditText title, desc, fromPlace, toPlace, payment;
    private CheckBox agreement;
    private ScrollView mainView;

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
        title = view.findViewById(R.id.add_fragment_title);
        desc = view.findViewById(R.id.add_fragment_desc);
        fromPlace = view.findViewById(R.id.add_fragment_from_place);
        toPlace = view.findViewById(R.id.add_fragment_to_place);
        payment = view.findViewById(R.id.add_fragment_payment);
        agreement = view.findViewById(R.id.add_fragment_agreement);
        post = view.findViewById(R.id.add_fragment_post_btn);
        carType = view.findViewById(R.id.add_fragment_car_type);
        image = view.findViewById(R.id.add_fragment_image);
        imageText = view.findViewById(R.id.add_fragment_image_text);
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
            image.setVisibility(View.GONE);
            imageText.setVisibility(View.GONE);
            carType.setVisibility(View.GONE);
        }
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
                post.setVisibility(View.GONE);

                PostCreate postCreate = new PostCreate(Statics.getString(title), Statics.getString(desc), Statics.getString(payment), 0, Statics.getString(fromPlace), Statics.getString(toPlace), MSharedPreferences.get(getContext(), "who", ""), date.getText().toString() + "" + time.getText().toString());
                MainActivity.hideKeyboard(getActivity(), post);
                MainRepository.getService().createPost(postCreate, "Bearer " + new Gson().fromJson(MSharedPreferences.get(getContext(), Statics.USER, ""), User.class).getAccessToken()).enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (response.isSuccessful()) {
                            MToast.show(getContext(), getResources().getString(R.string.posted));
                            clearFields();
                        } else {
                            MToast.show(getContext(), Statics.getResponseError(response.errorBody()));
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
                agreement.setChecked(false);
                ((MainActivity) getActivity()).openMain();
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, types);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    carType.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<CarResponse> call, Throwable t) {

            }
        });
    }
}
