package com.nextinnovation.pitak.fragment.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nextinnovation.pitak.R;
import com.nextinnovation.pitak.register.RegisterClientActivity;
import com.nextinnovation.pitak.register.RegisterDriverActivity;
import com.nextinnovation.pitak.settings.HistoryActivity;
import com.nextinnovation.pitak.settings.NotificationActivity;
import com.nextinnovation.pitak.settings.PayActivity;
import com.nextinnovation.pitak.settings.PromoCodeActivity;
import com.nextinnovation.pitak.utils.MSharedPreferences;

public class ProfileFragment extends Fragment {

    private RelativeLayout settings;
    private RelativeLayout notification;
    private RelativeLayout promotionCode;
    private RelativeLayout pay;
    private RelativeLayout history;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, null);
        initAllView(view);
        listener();
        return view;
    }

    private void initAllView(View view) {
        settings = view.findViewById(R.id.profile_fragment_my_settings_rl);
        notification = view.findViewById(R.id.profile_fragment_my_notification_rl);
        promotionCode = view.findViewById(R.id.profile_fragment_promotional_code_rl);
        pay = view.findViewById(R.id.profile_fragment_my_pay_rl);
        history = view.findViewById(R.id.profile_fragment_my_order_rl);
    }

    private void listener() {
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MSharedPreferences.get(getContext(), "who", "").equals("client")) {
                    RegisterClientActivity.start(getContext());
                } else if (MSharedPreferences.get(getContext(), "who", "").equals("driver")) {
                    RegisterDriverActivity.start(getContext());
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
    }
}
