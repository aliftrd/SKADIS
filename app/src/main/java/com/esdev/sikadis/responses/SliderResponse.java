package com.esdev.sikadis.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SliderResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private SliderData data;

    // getter methods

    public boolean isSuccess() {
        return success;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public SliderData getData() {
        return data;
    }

    public static class SliderData {
        @SerializedName("sliders")
        private List<Slider> sliders;

        // getter method

        public List<Slider> getSliders() {
            return sliders;
        }
    }

    public static class Slider {
        @SerializedName("title")
        private String title;

        @SerializedName("image")
        private String image;

        @SerializedName("created_at")
        private String createdAt;

        // getter methods

        public String getTitle() {
            return title;
        }

        public String getImage() {
            return image;
        }

        public String getCreatedAt() {
            return createdAt;
        }
    }
}


