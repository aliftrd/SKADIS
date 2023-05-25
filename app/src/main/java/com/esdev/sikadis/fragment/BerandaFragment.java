package com.esdev.sikadis.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.esdev.sikadis.retrofit.ApiClient;
import com.esdev.sikadis.retrofit.Api;
import com.esdev.sikadis.models.Artikel;
import com.esdev.sikadis.responses.PostResponse;
import com.esdev.sikadis.responses.SliderResponse;
import com.esdev.sikadis.R;
import com.esdev.sikadis.adapter.ArtikelAdapter;
import com.esdev.sikadis.adapter.SliderAdapter;
import com.esdev.sikadis.temp.TemporaryData;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BerandaFragment extends Fragment {
    private static final String TAG = "BerandaFragment";

    private RecyclerView recyclerView;
    private ArtikelAdapter adapter;
    private ProgressBar progressBar;

    private SwipeRefreshLayout swipeRefreshLayout;

    private SliderAdapter sliderAdapter;
    private ViewPager sliderViewPager;
    private Timer sliderTimer;
    private int currentPage = 0;

    private String nextPageUrl = null;
    private boolean isLoading = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beranda, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ArtikelAdapter(getContext(), null);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && nextPageUrl != null) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                        loadNextPage();
                    }
                }
            }
        });

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this::refreshData);

        // Inisialisasi ViewPager untuk Slider
        sliderViewPager = view.findViewById(R.id.sliderViewPager);
        sliderAdapter = new SliderAdapter(getContext(), new ArrayList<>());
        sliderViewPager.setAdapter(sliderAdapter);
        setupSliderTimer();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopSliderTimer();
    }

    private void setupSliderTimer() {
        if (sliderTimer == null) {
            sliderTimer = new Timer();
            sliderTimer.scheduleAtFixedRate(new SliderTimerTask(), 2000, 3500);
        }
    }

    private void stopSliderTimer() {
        if (sliderTimer != null) {
            sliderTimer.cancel();
            sliderTimer = null;
        }
    }

    private class SliderTimerTask extends TimerTask {
        @Override
        public void run() {
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (currentPage == sliderAdapter.getCount() - 1) {
                        currentPage = 0;
                    } else {
                        currentPage++;
                    }
                    sliderViewPager.setCurrentItem(currentPage, true);
                });
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
        loadSliders();
    }

    private void refreshData() {
        // Menampilkan indikator loading saat memuat ulang data
        swipeRefreshLayout.setRefreshing(true);

        // Memuat ulang data ke data awal
        nextPageUrl = null; // Mengatur nextPageUrl ke null untuk memulai dari awal
        adapter.clearData(); // Menghapus semua data dari adapter

        // Memuat data dan slider

        loadData();
        loadSliders();

        swipeRefreshLayout.setRefreshing(false);
    }



    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    private void loadSliders() {
        Api api = ApiClient.getApiClient().create(Api.class);
        Call<SliderResponse> call = api.getSliders();
        call.enqueue(new Callback<SliderResponse>() {
            @Override
            public void onResponse(Call<SliderResponse> call, Response<SliderResponse> response) {
                if (response.isSuccessful()) {
                    SliderResponse sliderResponse = response.body();
                    if (sliderResponse != null) {
                        List<SliderResponse.Slider> sliders = sliderResponse.getData().getSliders();
                        if (sliders != null && !sliders.isEmpty()) {
                            sliderAdapter.setData(sliders);
                            Log.d(TAG, "Sliders loaded successfully");
                        } else {
                            Log.d(TAG, "No sliders available");
                        }
                    } else {
                        Log.e(TAG, "Response body is null");
                    }
                } else {
                    Log.e(TAG, "Response unsuccessful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SliderResponse> call, Throwable t) {
                Log.e(TAG, "Request failed: " + t.getMessage());
            }
        });
    }

    private void loadData() {
        showProgressBar();
        isLoading = true;

        Api api = ApiClient.getApiClient().create(Api.class);
        Call<PostResponse> call = api.getPosts();
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                hideProgressBar();

                if (response.isSuccessful()) {
                    PostResponse postResponse = response.body();
                    if (postResponse != null) {
                        List<PostResponse.Data> data = postResponse.getDataContainer().getPostsData().getDataList();
                        if (data != null && !data.isEmpty()) {
                            nextPageUrl = postResponse.getDataContainer().getPostsData().getLinks().getNext();
                            adapter.setData(data);
                            TemporaryData.setArtikelList(convertToArtikelList(data));
                            Log.d(TAG, "Posts loaded successfully");
                        } else {
                            Log.d(TAG, "No posts available");
                        }
                    } else {
                        Log.e(TAG, "Response body is null");
                    }
                } else {
                    Log.e(TAG, "Response unsuccessful: " + response.code());
                }

                isLoading = false;
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                hideProgressBar();
                Log.e(TAG, "Request failed: " + t.getMessage());
                isLoading = false;
            }
        });
    }

    private void loadNextPage() {
        showProgressBar();
        isLoading = true;

        Api api = ApiClient.getApiClient().create(Api.class);
        Call<PostResponse> call = api.getPostsByUrl(nextPageUrl);
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                hideProgressBar();

                if (response.isSuccessful()) {
                    PostResponse postResponse = response.body();
                    if (postResponse != null) {
                        List<PostResponse.Data> data = postResponse.getDataContainer().getPostsData().getDataList(); // Update pemanggilan data
                        if (data != null && !data.isEmpty()) {
                            nextPageUrl = postResponse.getDataContainer().getPostsData().getLinks().getNext(); // Update pemanggilan data
                            adapter.addData(data);
                            Log.d(TAG, "Next page loaded successfully");

                            // Tambahkan data baru ke TemporaryData
                            List<Artikel> newArtikelList = convertToArtikelList(data);
                            List<Artikel> oldArtikelList = TemporaryData.getArtikelList();
                            oldArtikelList.addAll(newArtikelList);
                            TemporaryData.setArtikelList(oldArtikelList);
                        } else {
                            Log.d(TAG, "No posts available for next page");
                        }
                    } else {
                        Log.e(TAG, "Response body is null");
                    }
                } else {
                    Log.e(TAG, "Response unsuccessful: " + response.code());
                }

                isLoading = false;
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                hideProgressBar();
                Log.e(TAG, "Request failed: " + t.getMessage());
                isLoading = false;
            }
        });
    }

    private List<Artikel> convertToArtikelList(List<PostResponse.Data> dataList) {
        List<Artikel> artikelList = new ArrayList<>();
        for (PostResponse.Data data : dataList) {
            Artikel artikel = new Artikel();
            artikel.setTitle(data.getTitle());
            artikel.setSlug(data.getSlug());
            artikel.setCreatedAt(data.getCreatedAt());
            artikel.setThumbnail(data.getThumbnail());
            artikel.setContent(data.getContent());
            artikelList.add(artikel);
        }
        return artikelList;
    }
}
