package com.nextinnovation.pitak.fragment.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.nextinnovation.pitak.R;
import com.nextinnovation.pitak.model.user.ProfileRequest;
import com.nextinnovation.pitak.model.user.UserWhenSignedIn;
import com.nextinnovation.pitak.register.RegisterClientActivity;
import com.nextinnovation.pitak.register.RegisterDriverActivity;
import com.nextinnovation.pitak.settings.HistoryActivity;
import com.nextinnovation.pitak.settings.MyOrdersActivity;
import com.nextinnovation.pitak.settings.NotificationActivity;
import com.nextinnovation.pitak.settings.PayActivity;
import com.nextinnovation.pitak.settings.PromoCodeActivity;
import com.nextinnovation.pitak.utils.MSharedPreferences;
import com.nextinnovation.pitak.utils.Statics;

public class ProfileFragment extends Fragment {

    private RelativeLayout settings;
    private RelativeLayout notification;
    private RelativeLayout promotionCode;
    private RelativeLayout pay;
    private RelativeLayout history;
    private RelativeLayout myPosts;
    private TextView name;
    private TextView email;
    private ImageView profile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, null);
        initAllView(view);
        listener();
        setView();
        return view;
    }

    private void initAllView(View view) {
        settings = view.findViewById(R.id.profile_fragment_my_settings_rl);
        notification = view.findViewById(R.id.profile_fragment_my_notification_rl);
        promotionCode = view.findViewById(R.id.profile_fragment_promotional_code_rl);
        pay = view.findViewById(R.id.profile_fragment_my_pay_rl);
        history = view.findViewById(R.id.profile_fragment_my_order_rl);
        name = view.findViewById(R.id.profile_fragment_name);
        email = view.findViewById(R.id.profile_fragment_email);
        myPosts = view.findViewById(R.id.profile_fragment_my_address_rl);
        profile = view.findViewById(R.id.profile_fragment_profile);
    }

    private void listener() {
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("-----who", MSharedPreferences.get(getContext(), "who", ""));
                if (MSharedPreferences.get(getContext(), "who", "").equals(Statics.PASSENGER)) {
                    RegisterClientActivity.start(getContext(), true);
                } else if (MSharedPreferences.get(getContext(), "who", "").equals(Statics.DRIVER)) {
                    RegisterDriverActivity.start(getContext(), true);
                }
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationActivity.start(getContext());
            }
        });
        promotionCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PromoCodeActivity.start(getContext());
            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PayActivity.start(getContext());
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HistoryActivity.start(getContext());
            }
        });
        myPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MyOrdersActivity.class));
            }
        });
    }

    private void setView() {
        UserWhenSignedIn user = new Gson().fromJson(MSharedPreferences.get(getContext(), Statics.USER, ""), UserWhenSignedIn.class);
        name.setText(user.getName());
        email.setText(user.getEmail());
        setProfile(user.getProfilePhoto());
    }

    private void setProfile(ProfileRequest profileModel) {
        if (profileModel != null) {
            byte[] decodedString = Base64.decode(profileModel.getContent(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Glide.with(profile.getContext()).load(decodedByte).apply(RequestOptions.circleCropTransform()).into(profile);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setView();
    }
}
