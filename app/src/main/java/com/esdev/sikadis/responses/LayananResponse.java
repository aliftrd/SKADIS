package com.esdev.sikadis.responses;

import com.google.gson.annotations.SerializedName;

public class LayananResponse {
    @SerializedName("success")
    private boolean success;
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private LayananData data;

    public boolean isSuccess() {
        return success;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public LayananData getData() {
        return data;
    }

    public static class LayananData {
        @SerializedName("PUSAT_BANTUAN")
        private String pusatBantuan;
        @SerializedName("PHONE")
        private String phone;
        @SerializedName("JAM_KANTOR")
        private String jamKantor;
        @SerializedName("EMAIL")
        private String email;

        public String getPusatBantuan() {
            return pusatBantuan;
        }

        public String getPhone() {
            return phone;
        }

        public String getJamKantor() {
            return jamKantor;
        }

        public String getEmail() {
            return email;
        }
    }
}
