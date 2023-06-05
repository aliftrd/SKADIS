package com.esdev.sikadis.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AgamaResponse {

    @SerializedName("success")
    private boolean isSuccess;

    @SerializedName("code")
    private int statusCode;

    @SerializedName("message")
    private String statusMessage;

    @SerializedName("data")
    private List<String> religions;


    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public List<String> getReligions() {
        return religions;
    }

    public void setReligions(List<String> religions) {
        this.religions = religions;
    }
}

