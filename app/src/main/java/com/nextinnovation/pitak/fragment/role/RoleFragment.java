package com.nextinnovation.pitak.fragment.role;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nextinnovation.pitak.R;
import com.nextinnovation.pitak.data.MainRepository;
import com.nextinnovation.pitak.fragment.main.MainFragment;
import com.nextinnovation.pitak.fragment.main.RecyclerViewAdapter;
import com.nextinnovation.pitak.item_detail.ItemDetailActivity;
import com.nextinnovation.pitak.main.MainActivity;
import com.nextinnovation.pitak.model.post.PostResponse;
import com.nextinnovation.pitak.model.post.PostSearch;
import com.nextinnovation.pitak.utils.MSharedPreferences;
import com.nextinnovation.pitak.utils.MToast;
import com.nextinnovation.pitak.utils.Statics;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoleFragment extends Fragment implements RecyclerViewAdapter.onItemClick {

    private int page = 0;
    private long size = 0;


    private RecyclerView recyclerView;
    public static RecyclerViewAdapter adapter;
    private View searchLayout;
    private EditText search;
    private ProgressBar loading;
    private boolean hide;
    private PostSearch postSearch = new PostSearch();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_role, null);

        initAllView(view);
        listener();
        getData(false);
        return view;
    }

    private void initAllView(View view) {
        loading = view.findViewById(R.id.role_fragment_loading);
        searchLayout = view.findViewById(R.id.role_search_layout_rl);
        search = view.findViewById(R.id.role_fragment_edit_search);
        adapter = new RecyclerViewAdapter(this, false, false);
        recyclerView = view.findViewById(R.id.role_fragment_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void listener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 10 && !hide && ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition() > 0) {
                    searchLayout.animate().translationY(-400).setDuration(250);
                    hide = true;
                } else if (dy < -10 && hide) {
                    searchLayout.animate().translationY(0).setDuration(250);
                    hide = false;
                }

            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && loading.getVisibility() == View.GONE) {
                    getData(false);
                }
            }
        });

        search.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (Statics.getString(search).length() == 0) {
                        postSearch.setTitle(null);
                    } else {
                        postSearch.setTitle(Statics.getString(search));
                    }
                    MainActivity.hideKeyboard(getActivity(), search);
                    page = 0;
                    getData(true);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onCall(int pos) {
        Statics.call("+" + adapter.getList().get(pos).getMobileNumber(), getContext());
    }

    @Override
    public void onSave(final int pos, final boolean save) {
        if (save) {
            MainRepository.getService().addToFavourite(adapter.getList().get(pos).getId(), Statics.getToken(getContext())).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
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
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    MToast.showInternetError(getContext());
                }
            });
        }
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < MainFragment.adapter.getList().size(); i++) {
                    if (MainFragment.adapter.getList().get(i).getId() == adapter.getList().get(pos).getId()) {
                        MainFragment.adapter.getList().get(i).setFavorite(save);
                        MainFragment.adapter.notifyItemChanged(i);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(int pos) {
        ItemDetailActivity.start(getContext(), adapter.getList().get(pos).getId(), false);
    }

    @Override
    public void openWhatsapp(int pos) {
        Statics.openWhatsapp(adapter.getList().get(pos).getMobileNumber(), getContext());
    }

    private void getData(final boolean search) {
        if (size != 0 && size == adapter.getList().size() && !search) {
            return;
        }
        loading.setVisibility(View.VISIBLE);
        if (MSharedPreferences.get(getContext(), "who", "").equals(Statics.PASSENGER)) {
            MainRepository.getService().searchDriver(postSearch, Statics.getToken(getContext()), page, null).enqueue(new Callback<PostResponse>() {
                @Override
                public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (search) {
                            adapter.clear();
                        }
                        adapter.addList(response.body().getResult().getContent());
                        page++;
                        loading.setVisibility(View.GONE);
                        size = response.body().getResult().getTotalElements();
                        if (response.body().getResult().getContent().isEmpty()) {
                            MToast.show(getContext(), getResources().getString(R.string.nothing_found));
                        }
                    } else {
                        MToast.showResponseError(getContext(), response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<PostResponse> call, Throwable t) {
                    MToast.showInternetError(getContext());
                    loading.setVisibility(View.GONE);
                }
            });
        }
        if (MSharedPreferences.get(getContext(), "who", "").equals(Statics.DRIVER)) {
            MainRepository.getService().searchPassenger(postSearch, Statics.getToken(getContext()), page, null).enqueue(new Callback<PostResponse>() {
                @Override
                public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (search) {
                            adapter.clear();
                        }
                        adapter.addList(response.body().getResult().getContent());
                        page++;
                        loading.setVisibility(View.GONE);
                        size = response.body().getResult().getTotalElements();
                        if (response.body().getResult().getContent().isEmpty()) {
                            MToast.show(getContext(), getResources().getString(R.string.nothing_found));
                        }
                    } else {
                        MToast.showResponseError(getContext(), response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<PostResponse> call, Throwable t) {
                    MToast.showInternetError(getContext());
                    loading.setVisibility(View.GONE);
                }
            });
        }
    }
}
