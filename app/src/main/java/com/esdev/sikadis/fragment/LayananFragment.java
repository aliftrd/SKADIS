package com.esdev.sikadis.fragment;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.esdev.sikadis.responses.LayananResponse.LayananData;

import com.esdev.sikadis.R;
import com.esdev.sikadis.adapter.LayananAdapter;
import com.esdev.sikadis.responses.LayananResponse;

import android.text.Html;

public class LayananFragment extends Fragment implements LayananAdapter.LayananCallback {

    private TextView textPusatBantuan;
    private TextView textPhone;
    private TextView textJamKantor;
    private TextView textEmail;

    public LayananFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_layanan, container, false);

        // Inisialisasi TextView dengan ID yang sesuai
        textPusatBantuan = view.findViewById(R.id.textPusatBantuan);
        textPhone = view.findViewById(R.id.textPhone);
        textJamKantor = view.findViewById(R.id.textJamKantor);
        textEmail = view.findViewById(R.id.textEmail);

        // Panggil metode untuk mendapatkan data dari LayananAdapter
        LayananAdapter layananAdapter = new LayananAdapter();
        layananAdapter.getMeta(this); // Ubah menjadi metode getLayananData()

        return view;
    }

    @Override
    public void onSuccess(LayananResponse.LayananData layananData) {
        // Set data dari LayananResponse ke TextView

        String pusatBantuan = layananData.getPusatBantuan();
        String phone = layananData.getPhone();
        String jamKantor = layananData.getJamKantor();
        String email = layananData.getEmail();

        // Hapus tag HTML dari teks
        String strippedPusatBantuan = Html.fromHtml(pusatBantuan).toString();
        String strippedPhone = Html.fromHtml(phone).toString();
        String strippedJamKantor = Html.fromHtml(jamKantor).toString();
        String strippedEmail = Html.fromHtml(email).toString();

        textPusatBantuan.setText(strippedPusatBantuan);
        textPhone.setText(strippedPhone);
        textJamKantor.setText(strippedJamKantor);
        textEmail.setText(strippedEmail);
    }

    @Override
    public void onError(String message) {
        // Tangani jika terjadi kesalahan
        // Misalnya, tampilkan pesan kesalahan ke pengguna
    }
}
