package com.nextinnovation.pitak.fragment.saved;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nextinnovation.pitak.R;
import com.nextinnovation.pitak.fragment.main.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SavedFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private List<String> strings = new ArrayList<>();
    private View searchLayout;
    private boolean hide;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved, null);
        strings.add("");
        strings.add("");
        strings.add("");
        strings.add("");
        strings.add("");
        strings.add("");
        strings.add("");
        strings.add("");
        initAllView(view);
        listener();
        return view;
    }

    private void initAllView(View view) {
        searchLayout = view.findViewById(R.id.saved_search_layout_rl);
        adapter = new RecyclerViewAdapter();
        adapter.setList(strings, true);
        recyclerView = view.findViewById(R.id.saved_fragment_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void listener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 10 && !hide && ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition() > 0) {
                    searchLayout.animate().translationY(-700).setDuration(400);
                    hide = true;
                } else if (dy < -10 && hide) {
                    searchLayout.animate().translationY(0).setDuration(400);
                    hide = false;
                }
            }
        });
    }
}
