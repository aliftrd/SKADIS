package com.esdev.sikadis.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.esdev.sikadis.responses.PostResponse;
import com.esdev.sikadis.activities.DetailArtikelActivity;
import com.esdev.sikadis.R;
import com.esdev.sikadis.retrofit.Api;
import com.esdev.sikadis.retrofit.ApiClient;
import com.esdev.sikadis.responses.PostResponse;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtikelAdapter extends RecyclerView.Adapter<ArtikelAdapter.ArtikelViewHolder> {
    private Context context;
    private List<PostResponse.Data> postList;
    private String nextPageUrl;

    public ArtikelAdapter(Context context, List<PostResponse.Data> postList) {
        this.context = context;
        this.postList = postList != null ? postList : new ArrayList<>();
        this.nextPageUrl = null;
    }

    @NonNull
    @Override
    public ArtikelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artikel, parent, false);
        return new ArtikelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtikelViewHolder holder, int position) {
        PostResponse.Data post = postList.get(position);
        String title = post.getTitle();
        if (title.length() > 33) {
            title = title.substring(0, 33) + " ...";
        }
        holder.titleTextView.setText(title);

        String articleCreatedAt = post.getCreatedAt();
        Drawable timeIcon = context.getResources().getDrawable(R.drawable.ic_time_2);
        timeIcon.setBounds(0, 0, timeIcon.getIntrinsicWidth(), timeIcon.getIntrinsicHeight());
        holder.createdAtTextView.setCompoundDrawables(timeIcon, null, null, null);
        holder.createdAtTextView.setCompoundDrawablePadding(5);
        holder.createdAtTextView.setText(articleCreatedAt);

        ViewGroup.LayoutParams layoutParams = holder.thumbnailImageView.getLayoutParams();
        holder.thumbnailImageView.setLayoutParams(layoutParams);

        String thumbnailUrl = post.getThumbnail();

        if (thumbnailUrl != null && !thumbnailUrl.isEmpty()) {
            if (thumbnailUrl.startsWith("http://")) {
                // Replace HTTP with HTTPS in thumbnail URL
                thumbnailUrl = thumbnailUrl.replace("http://", "https://");
            }

            Glide.with(context)
                    .load(thumbnailUrl)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.img_placeholder)
                            .error(R.drawable.img_placeholder)
                            .centerInside()
                    )
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            String errorMessage = e != null ? e.getMessage() : "Unknown error";
                            Log.e("Glide", "Error loading image: " + errorMessage);
                            holder.thumbnailImageView.setImageResource(R.drawable.img_placeholder);

                            if (e != null) {
                                e.logRootCauses("Glide Error");
                            }

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(holder.thumbnailImageView);
        } else {
            holder.thumbnailImageView.setImageResource(R.drawable.img_placeholder);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String articleSlug = post.getSlug();
                Log.d("Article Slug di Adapter", articleSlug); // Cetak articleSlug
                Intent intent = new Intent(context, DetailArtikelActivity.class);
                intent.putExtra("articleSlug", articleSlug);
                context.startActivity(intent);
            }
        });

        // Deteksi akhir halaman dan memuat halaman berikutnya
        if (position == postList.size() - 1 && nextPageUrl != null) {
            int visibleItemCount = holder.recyclerView.getLayoutManager().getChildCount();
            int totalItemCount = holder.recyclerView.getLayoutManager().getItemCount();
            int pastVisibleItems = ((LinearLayoutManager) holder.recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                loadNextPage();
            }
        }
    }


    private void loadNextPage() {
        Api api = ApiClient.getApiClient().create(Api.class);
        api.getPostsByUrl(nextPageUrl).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if (response.isSuccessful()) {
                    PostResponse postResponse = response.body();

                    if (postResponse != null && postResponse.isSuccess()) {
                        List<PostResponse.Data> newDataList = postResponse.getDataContainer().getPostsData().getDataList();
                        PostResponse.Links linksInfo = postResponse.getDataContainer().getPostsData().getLinks();
                        if (linksInfo != null) {
                            nextPageUrl = linksInfo.getNext();
                            addData(newDataList);
                        } else {
                            Log.e("API Response", "LinksInfo is null");
                        }
                    } else {
                        Log.e("API Response", "Error: " + postResponse.getMessage());
                    }
                } else {
                    Log.e("API Response", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Log.e("API Response", "Error: " + t.getMessage());
            }
        });
    }

    public void addData(List<PostResponse.Data> newDataList) {
        if (newDataList != null && !newDataList.isEmpty()) {
            int previousSize = postList.size();

            for (PostResponse.Data newData : newDataList) {
                boolean hasDuplicateSlug = false;

                for (PostResponse.Data existingData : postList) {
                    if (existingData.getSlug().equals(newData.getSlug())) {
                        hasDuplicateSlug = true;
                        break;
                    }
                }

                if (!hasDuplicateSlug) {
                    postList.add(newData);
                }
            }

            notifyItemRangeInserted(previousSize, postList.size() - previousSize);
        }
    }


    public void setData(List<PostResponse.Data> newDataList) {
        postList = newDataList;
        notifyDataSetChanged();
    }

    public void clearData() {
        postList.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void setFirstPageData(List<PostResponse.Data> postList, String nextPageUrl) {
        if (postList != null) {
            this.postList.clear(); // Bersihkan daftar yang ada
            this.postList.addAll(postList); // Tambahkan data baru ke dalam daftar
            this.nextPageUrl = nextPageUrl; // Perbarui URL halaman berikutnya
            notifyDataSetChanged(); // Beritahu adapter tentang perubahan data
        }
    }

    public class ArtikelViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView createdAtTextView;
        ImageView thumbnailImageView;
        RecyclerView recyclerView;

        public ArtikelViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            createdAtTextView = itemView.findViewById(R.id.createdAtTextView);
            thumbnailImageView = itemView.findViewById(R.id.thumbnailImageView);
            recyclerView = itemView.findViewById(R.id.recyclerView);
        }
    }
}

