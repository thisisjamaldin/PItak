package com.nextinnovation.pitak.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.nextinnovation.pitak.R;
import com.nextinnovation.pitak.data.MainRepository;
import com.nextinnovation.pitak.model.agreement.AgreementResponse;
import com.nextinnovation.pitak.model.report.ReportResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgreementActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);

        webView = findViewById(R.id.agreement_web_view);
        loading = findViewById(R.id.agreement_progress);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
        MainRepository.getService().getAgreement().enqueue(new Callback<AgreementResponse>() {
            @Override
            public void onResponse(Call<AgreementResponse> call, Response<AgreementResponse> response) {
                if (response.isSuccessful() && response.body()!=null){
                    webView.loadDataWithBaseURL(null, response.body().getResult().getResponseHtml(), "text/html","UTF-8", null);
                    loading.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<AgreementResponse> call, Throwable t) {

            }
        });
    }
}