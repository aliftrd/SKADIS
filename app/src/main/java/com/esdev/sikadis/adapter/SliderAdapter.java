package com.esdev.sikadis.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.esdev.sikadis.R;
import com.esdev.sikadis.responses.SliderResponse;

import java.util.List;

public class SliderAdapter extends PagerAdapter {

    private Context context;
    private List<SliderResponse.Slider> sliderList;

    public SliderAdapter(Context context, List<SliderResponse.Slider> sliderList) {
        this.context = context;
        this.sliderList = sliderList;
    }

    @Override
    public int getCount() {
        return sliderList != null ? sliderList.size() : 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_slider, container, false);
        ImageView imageView = view.findViewById(R.id.sliderView);
        String imageUrl = sliderList.get(position).getImage();

        if (imageUrl != null && !imageUrl.isEmpty()) {
            if (imageUrl.startsWith("http://")) {
                // Replace HTTP with HTTPS in the image URL
                imageUrl = imageUrl.replace("http://", "https://");
            }

            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.img_placeholder)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.img_placeholder);
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public void setData(List<SliderResponse.Slider> sliderList) {
        this.sliderList = sliderList;
        notifyDataSetChanged();
    }
}
