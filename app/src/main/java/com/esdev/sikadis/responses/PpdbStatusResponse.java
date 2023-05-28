package com.esdev.sikadis.responses;

import com.google.gson.annotations.SerializedName;

public class PpdbStatusResponse {
    @SerializedName("success")
    private boolean success;
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private PpdbStatusData data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PpdbStatusData getData() {
        return data;
    }

    public void setData(PpdbStatusData data) {
        this.data = data;
    }

    public class PpdbStatusData {
        @SerializedName("academic_year")
        private String academicYear;
        @SerializedName("ppdb")
        private int ppdb;
        @SerializedName("status")
        private int status;

        public String getAcademicYear() {
            return academicYear;
        }

        public void setAcademicYear(String academicYear) {
            this.academicYear = academicYear;
        }

        public int getPpdb() {
            return ppdb;
        }

        public void setPpdb(int ppdb) {
            this.ppdb = ppdb;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
