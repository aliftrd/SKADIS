package com.esdev.sikadis.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.esdev.sikadis.R;
import com.esdev.sikadis.models.Artikel;
import com.esdev.sikadis.temp.TemporaryData;

import java.util.List;

public class DetailArtikelActivity extends AppCompatActivity {
    private TextView titleTextView;
    private TextView createdAtTextView;
    private ImageView thumbnailImageView;
    private TextView contentTextView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_artikel);

        // Inisialisasi komponen UI
        titleTextView = findViewById(R.id.titleTextView);
        createdAtTextView = findViewById(R.id.createdAtTextView);
        thumbnailImageView = findViewById(R.id.thumbnailImageView);
        contentTextView = findViewById(R.id.contentTextView);
        progressBar = findViewById(R.id.progressBar);

        // Set ProgressBar menjadi terlihat saat data sedang dimuat
        progressBar.setVisibility(View.VISIBLE);

        // Ambil data artikel dari TemporaryData
        List<Artikel> artikelList = TemporaryData.getArtikelList();

        // Ambil slug artikel dari intent
        String articleSlug = getIntent().getStringExtra("articleSlug");

        if (artikelList != null && !artikelList.isEmpty()) {
            // Cari artikel yang memiliki slug yang sesuai
            Artikel artikel = null;
            for (Artikel art : artikelList) {
                if (art.getSlug().equals(articleSlug)) {
                    artikel = art;
                    break;
                }
            }

            if (artikel != null) {
                String articleTitle = artikel.getTitle();
                String articleCreatedAt = artikel.getCreatedAt();
                String articleThumbnail = artikel.getThumbnail();
                String articleContent = artikel.getContent();

                // Tampilkan data pada UI
                titleTextView.setText(articleTitle);
                createdAtTextView.setText("Dibuat pada: " + articleCreatedAt);

                if (articleThumbnail != null && !articleThumbnail.isEmpty()) {
                    if (articleThumbnail.startsWith("http://")) {
                        // Replace HTTP with HTTPS in thumbnail URL
                        articleThumbnail = articleThumbnail.replace("http://", "https://");
                    }

                    // Menggunakan Glide untuk memuat gambar dari URL thumbnail
                    Glide.with(DetailArtikelActivity.this)
                            .load(articleThumbnail)
                            .placeholder(R.drawable.img_placeholder)
                            .error(R.drawable.img_placeholder)
                            .into(thumbnailImageView);
                } else {
                    thumbnailImageView.setImageResource(R.drawable.img_placeholder);
                }

                String content = articleContent.replaceAll("<[^>]*>", "");
                contentTextView.setText(content);

                // Sembunyikan ProgressBar setelah data selesai dimuat
                progressBar.setVisibility(View.GONE);

                Log.d("Detail Artikel", "Showing detail of article: " + articleTitle);
            } else {
                // Tampilkan pesan jika slug artikel tidak ditemukan
                Toast.makeText(DetailArtikelActivity.this, "Artikel tidak ditemukan", Toast.LENGTH_SHORT).show();
                // Sembunyikan ProgressBar karena tidak ada data yang ditampilkan
                progressBar.setVisibility(View.GONE);
            }
        } else {
            // Tampilkan pesan jika data artikel kosong
            Toast.makeText(DetailArtikelActivity.this, "Tidak ada data artikel", Toast.LENGTH_SHORT).show();
            // Sembunyikan ProgressBar karena tidak ada data yang ditampilkan
            progressBar.setVisibility(View.GONE);
        }
    }
}
