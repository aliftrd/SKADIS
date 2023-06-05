package com.esdev.sikadis.models;
import com.google.gson.annotations.SerializedName;

public class Siswa {
    @SerializedName("nik")
    private String nik;

    @SerializedName("fullname")
    private String fullname;

    @SerializedName("birthplace")
    private String birthplace;

    @SerializedName("birthdate")
    private String birthdate;

    @SerializedName("gender")
    private String gender;

    @SerializedName("religion")
    private String religion;

    @SerializedName("father_name")
    private String father_name;

    @SerializedName("father_occupation")
    private String father_occupation;

    @SerializedName("mother_name")
    private String mother_name;

    @SerializedName("mother_occupation")
    private String mother_occupation;

    @SerializedName("address")
    private String address;

    @SerializedName("guardian_name")
    private String guardian_name;

    @SerializedName("guardian_occupation")
    private  String guardian_occupation;

    @SerializedName("guardian_address")
    private  String guardian_address;

    public Siswa() {
    }

    public Siswa(String nik, String fullname, String birthplace, String birthdate, String gender, String religion, String father_name, String father_occupation, String mother_name, String mother_occupation, String address, String guardian_name, String guardian_occupation, String guardian_address) {
        this.nik = nik;
        this.fullname = fullname;
        this.birthplace = birthplace;
        this.birthdate = birthdate;
        this.gender = gender;
        this.religion = religion;
        this.father_name = father_name;
        this.father_occupation = father_occupation;
        this.mother_name = mother_name;
        this.mother_occupation = mother_occupation;
        this.address = address;
        this.guardian_name = guardian_name;
        this.guardian_occupation = guardian_occupation;
        this.guardian_address = guardian_address;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getFather_name() {
        return father_name;
    }

    public void setFather_name(String father_name) {
        this.father_name = father_name;
    }

    public String getFather_occupation() {
        return father_occupation;
    }

    public void setFather_occupation(String father_occupation) {
        this.father_occupation = father_occupation;
    }

    public String getMother_name() {
        return mother_name;
    }

    public void setMother_name(String mother_name) {
        this.mother_name = mother_name;
    }

    public String getMother_occupation() {
        return mother_occupation;
    }

    public void setMother_occupation(String mother_occupation) {
        this.mother_occupation = mother_occupation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGuardian_name() {
        return guardian_name;
    }

    public void setGuardian_name(String guardian_name) {
        this.guardian_name = guardian_name;
    }

    public String getGuardian_occupation() {
        return guardian_occupation;
    }

    public void setGuardian_occupation(String guardian_occupation) {
        this.guardian_occupation = guardian_occupation;
    }

    public String getGuardian_address() {
        return guardian_address;
    }

    public void setGuardian_address(String guardian_address) {
        this.guardian_address = guardian_address;
    }
}
