package com.nextinnovation.pitak.fragment.saved;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nextinnovation.pitak.R;
import com.nextinnovation.pitak.data.MainRepository;
import com.nextinnovation.pitak.fragment.main.RecyclerViewAdapter;
import com.nextinnovation.pitak.item_detail.ItemDetailActivity;
import com.nextinnovation.pitak.model.post.FavouritePostResponse;
import com.nextinnovation.pitak.utils.MToast;
import com.nextinnovation.pitak.utils.Statics;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SavedFragment extends Fragment implements RecyclerViewAdapter.onItemClick {

    private RecyclerView recyclerView;
    private static RecyclerViewAdapter adapter;
    private View searchLayout;
    private boolean hide;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved, null);
        initAllView(view);
        listener();
        getData(getContext());
        return view;
    }

    private void initAllView(View view) {
        searchLayout = view.findViewById(R.id.saved_search_layout_rl);
        adapter = new RecyclerViewAdapter(this, true, false);
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

    @Override
    public void onCall(int pos) {
        Statics.call("+" + adapter.getList().get(pos).getMobileNumber(), getContext());
    }

    @Override
    public void onSave(int pos, boolean save) {
        if (save) {
            MainRepository.getService().addToFavourite(adapter.getList().get(pos).getId(), Statics.getToken(getContext())).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    getData(getContext());
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    MToast.showInternetError(getContext());
                }
            });
        } else {
            MainRepository.getService().deleteFromFavourite(adapter.getList().get(pos).getId(), Statics.getToken(getContext())).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    getData(getContext());
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    MToast.showInternetError(getContext());
                }
            });
        }
    }

    @Override
    public void onClick(int pos) {
        ItemDetailActivity.start(getContext(), adapter.getList().get(pos).getId(), true);
    }

    @Override
    public void openWhatsapp(int pos) {
        Statics.openWhatsapp(adapter.getList().get(pos).getMobileNumber(), getContext());
    }

    public static void getData(final Context context) {
        MainRepository.getService().getFavourite(Statics.getToken(context)).enqueue(new Callback<FavouritePostResponse>() {
            @Override
            public void onResponse(Call<FavouritePostResponse> call, Response<FavouritePostResponse> response) {
                adapter.clear();
                if (response.isSuccessful() && response.body() != null && response.body().getResult() != null && !response.body().getResult().isEmpty()) {
                    adapter.addList(response.body().getResult());
                }
            }

            @Override
            public void onFailure(Call<FavouritePostResponse> call, Throwable t) {
                MToast.showInternetError(context);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getData(getContext());
    }
}
