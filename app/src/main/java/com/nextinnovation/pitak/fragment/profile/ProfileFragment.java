package com.nextinnovation.pitak.fragment.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.nextinnovation.pitak.R;
import com.nextinnovation.pitak.model.user.UserWhenSignedIn;
import com.nextinnovation.pitak.register.RegisterActivity;
import com.nextinnovation.pitak.register.RegisterClientActivity;
import com.nextinnovation.pitak.register.RegisterDriverActivity;
import com.nextinnovation.pitak.settings.AboutActivity;
import com.nextinnovation.pitak.settings.AgreementActivity;
import com.nextinnovation.pitak.settings.HistoryActivity;
import com.nextinnovation.pitak.settings.MyCarActivity;
import com.nextinnovation.pitak.settings.MyOrdersActivity;
import com.nextinnovation.pitak.settings.NotificationActivity;
import com.nextinnovation.pitak.settings.PayActivity;
import com.nextinnovation.pitak.settings.PromoCodeActivity;
import com.nextinnovation.pitak.utils.MSharedPreferences;
import com.nextinnovation.pitak.utils.Statics;

public class ProfileFragment extends Fragment {

//    private RelativeLayout settings;
    private RelativeLayout notification;
    private RelativeLayout promotionCode;
    private RelativeLayout pay;
    private RelativeLayout history;
    private RelativeLayout myPosts;
    private RelativeLayout myCar;
    private RelativeLayout about;
    private RelativeLayout agreement;
    private RelativeLayout signOut;
    private RelativeLayout edit;
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
//        settings = view.findViewById(R.id.profile_fragment_my_settings_rl);
        notification = view.findViewById(R.id.profile_fragment_my_notification_rl);
        promotionCode = view.findViewById(R.id.profile_fragment_promotional_code_rl);
        pay = view.findViewById(R.id.profile_fragment_my_pay_rl);
        history = view.findViewById(R.id.profile_fragment_my_order_rl);
        name = view.findViewById(R.id.profile_fragment_name);
        email = view.findViewById(R.id.profile_fragment_email);
        myPosts = view.findViewById(R.id.profile_fragment_my_post_rl);
        myCar = view.findViewById(R.id.profile_fragment_my_car_rl);
        about = view.findViewById(R.id.profile_fragment_my_about_rl);
        agreement = view.findViewById(R.id.profile_fragment_my_agreement_rl);
        signOut = view.findViewById(R.id.profile_fragment_my_sign_out_rl);
        profile = view.findViewById(R.id.profile_fragment_profile);
        edit = view.findViewById(R.id.profile_fragment_my_edit_rl);
    }

    private void listener() {
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse("https://firebasestorage.googleapis.com/v0/b/pitak-37337.appspot.com/o/pitak%20%D0%BE%D0%BF%D0%B8%D1%81%D0%B0%D0%BD%D0%B8%D0%B5.pdf?alt=media&token=0517a897-926b-46cb-8191-d0c63167e9a4"), "application/pdf");
                    startActivity(intent);
                } catch (Exception e) {
                    startActivity(new Intent(getContext(), AboutActivity.class));
                }
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MSharedPreferences.get(getContext(), "who", "").equals(Statics.PASSENGER)) {
                    RegisterClientActivity.start(getContext(), true);
                } else if (MSharedPreferences.get(getContext(), "who", "").equals(Statics.DRIVER)) {
                    RegisterDriverActivity.start(getContext(), true);
                }
            }
        });
//        settings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (MSharedPreferences.get(getContext(), "who", "").equals(Statics.PASSENGER)) {
//                    RegisterClientActivity.start(getContext(), true);
//                } else if (MSharedPreferences.get(getContext(), "who", "").equals(Statics.DRIVER)) {
//                    RegisterDriverActivity.start(getContext(), true);
//                }
//            }
//        });
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
        agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AgreementActivity.class));
            }
        });
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle(getResources().getString(R.string.sign_out));
                alert.setMessage(getResources().getString(R.string.are_you_sure_to_sign_out));
                alert.setNeutralButton(getResources().getString(R.string.cancel), null);
                alert.setPositiveButton(getResources().getString(R.string.sign_out), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getContext(), RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        MSharedPreferences.clear(getContext());
                        MSharedPreferences.set(getContext(), "first", false);
                    }
                });
                alert.show();
            }
        });
        myCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCarActivity.start(getContext());
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
        if (user.getProfilePhoto() != null)
            Statics.loadImage(profile, user.getProfilePhoto().getUrl(), true);
        if (MSharedPreferences.get(getContext(), "who", "").equals(Statics.PASSENGER)) {
            myCar.setVisibility(View.GONE);
        } else if (MSharedPreferences.get(getContext(), "who", "").equals(Statics.DRIVER)) {
            myCar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setView();
    }

}
