package com.nextinnovation.pitak.item_detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nextinnovation.pitak.R;
import com.nextinnovation.pitak.model.report.Report;

import java.util.ArrayList;
import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

    private List<Report> list = new ArrayList<>();
    private onItemClick onItemClick;

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public void addList(List<Report> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public List<Report> getList() {
        return this.list;
    }

    public ReportAdapter(ReportAdapter.onItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public ReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
        return new ReportAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportAdapter.ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox report;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            report = itemView.findViewById(R.id.item_report);
            report.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    onItemClick.onClick(getAdapterPosition());
                }
            });
        }

        void bind(Report item) {
            report.setText(item.getName());
            if (item.isClicked()) {
                report.setChecked(true);
            } else {
                report.setChecked(false);
            }
        }


    }

    public interface onItemClick {
        void onClick(int pos);
    }
}
