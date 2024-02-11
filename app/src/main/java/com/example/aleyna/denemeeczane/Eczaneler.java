
package com.example.aleyna.denemeeczane;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Eczaneler implements Serializable {

    @SerializedName("eczane_id")
    @Expose
    private String eczaneId;
    @SerializedName("eczane_ad")
    @Expose
    private String eczaneAd;
    @SerializedName("eposta")
    @Expose
    private String eposta;
    @SerializedName("kullanici_adi")
    @Expose
    private String kullaniciAdi;
    @SerializedName("sifre")
    @Expose
    private String sifre;
    @SerializedName("ilAdi")
    @Expose
    private String ilAdi;
    @SerializedName("ilceAdi")
    @Expose
    private String ilceAdi;
    @SerializedName("mahalleAdi")
    @Expose
    private String mahalleAdi;
    @SerializedName("sokakAdi")
    @Expose
    private String sokakAdi;
    @SerializedName("caddeAdi")
    @Expose
    private String caddeAdi;
    @SerializedName("no")
    @Expose
    private String no;
    @SerializedName("telNo")
    @Expose
    private String telNo;
    @SerializedName("enlem")
    @Expose
    private double enlem;
    @SerializedName("boylam")
    @Expose
    private double boylam;

    public String getEczaneId() {
        return eczaneId;
    }

    public void setEczaneId(String eczaneId) {
        this.eczaneId = eczaneId;
    }

    public String getEczaneAd() {
        return eczaneAd;
    }

    public void setEczaneAd(String eczaneAd) {
        this.eczaneAd = eczaneAd;
    }

    public String getEposta() {
        return eposta;
    }

    public void setEposta(String eposta) {
        this.eposta = eposta;
    }

    public String getKullaniciAdi() {
        return kullaniciAdi;
    }

    public void setKullaniciAdi(String kullaniciAdi) {
        this.kullaniciAdi = kullaniciAdi;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public String getIlAdi() {
        return ilAdi;
    }

    public void setIlAdi(String ilAdi) {
        this.ilAdi = ilAdi;
    }

    public String getIlceAdi() {
        return ilceAdi;
    }

    public void setIlceAdi(String ilceAdi) {
        this.ilceAdi = ilceAdi;
    }

    public String getMahalleAdi() {
        return mahalleAdi;
    }

    public void setMahalleAdi(String mahalleAdi) {
        this.mahalleAdi = mahalleAdi;
    }

    public String getSokakAdi() {
        return sokakAdi;
    }

    public void setSokakAdi(String sokakAdi) {
        this.sokakAdi = sokakAdi;
    }

    public String getCaddeAdi() {
        return caddeAdi;
    }

    public void setCaddeAdi(String caddeAdi) {
        this.caddeAdi = caddeAdi;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public double getEnlem() {
        return enlem;
    }

    public void setEnlem(double enlem) {
        this.enlem = enlem;
    }

    public double getBoylam() {
        return boylam;
    }

    public void setBoylam(double boylam) {
        this.boylam = boylam;
    }

}
