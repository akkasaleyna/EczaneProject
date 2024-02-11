
package com.example.aleyna.denemeeczane;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EczanedekiIlaclar {

    @SerializedName("ecz_ilaclar_id")
    @Expose
    private String eczIlaclarId;
    @SerializedName("eczane")
    @Expose
    private Eczaneler eczane;
    @SerializedName("ilac")
    @Expose
    private Ilaclar ilac;
    @SerializedName("ilac_adet")
    @Expose
    private int ilacAdet;

    public String getEczIlaclarId() {
        return eczIlaclarId;
    }

    public void setEczIlaclarId(String eczIlaclarId) {
        this.eczIlaclarId = eczIlaclarId;
    }

    public Eczaneler getEczane() {
        return eczane;
    }

    public void setEczane(Eczaneler eczane) {
        this.eczane = eczane;
    }

    public Ilaclar getIlac() {
        return ilac;
    }

    public void setIlac(Ilaclar ilac) {
        this.ilac = ilac;
    }

    public int getIlacAdet() {
        return ilacAdet;
    }

    public void setIlacAdet(int ilacAdet) { this.ilacAdet = ilacAdet;}

}
