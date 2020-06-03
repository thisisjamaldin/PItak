package com.nextinnovation.pitak.item_detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nextinnovation.pitak.R;
import com.nextinnovation.pitak.data.MainRepository;
import com.nextinnovation.pitak.model.report.Report;
import com.nextinnovation.pitak.model.report.ReportRequest;
import com.nextinnovation.pitak.model.report.ReportResponse;
import com.nextinnovation.pitak.utils.MToast;
import com.nextinnovation.pitak.utils.Statics;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportActivity extends AppCompatActivity implements ReportAdapter.onItemClick {

    private RecyclerView recyclerView;
    private ReportAdapter adapter;
    private EditText editText;
    private Button send;
    private long advertId;
    private long reportId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        findView();
        listener();
        getReports();
    }

    private void findView() {
        adapter = new ReportAdapter(this);
        advertId = getIntent().getLongExtra("id", 0);
        editText = findViewById(R.id.report_edit);
        send = findViewById(R.id.report_send);
        recyclerView = findViewById(R.id.report_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void listener() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Statics.getString(editText).length() < 20) {
                    editText.setError(getResources().getString(R.string.minimum_symbol_20));
                } else {
                    ReportRequest report = new ReportRequest(Statics.getString(editText), advertId, reportId);
                    MainRepository.getService().report(report, Statics.getToken(ReportActivity.this)).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            MToast.show(ReportActivity.this, getResources().getString(R.string.posted));
                            finish();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            MToast.showInternetError(ReportActivity.this);
                        }
                    });
                }

            }
        });
    }

    private void getReports() {
        MainRepository.getService().getReports().enqueue(new Callback<ReportResponse>() {
            @Override
            public void onResponse(Call<ReportResponse> call, Response<ReportResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.addList(response.body().getResult());
                }
            }

            @Override
            public void onFailure(Call<ReportResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(int pos) {
        this.reportId = adapter.getList().get(pos).getId();
        for (Report r : adapter.getList()) {
            r.setClicked(false);
        }
        adapter.getList().get(pos).setClicked(true);
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }
}