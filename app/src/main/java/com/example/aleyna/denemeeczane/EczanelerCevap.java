
package com.example.aleyna.denemeeczane;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EczanelerCevap {

    @SerializedName("eczaneler")
    @Expose
    private List<Eczaneler> eczaneler = null;
    @SerializedName("success")
    @Expose
    private Integer success;

    @SerializedName("message")
    @Expose
    private String message;

    public List<Eczaneler> getEczaneler() {
        return eczaneler;
    }

    public void setEczaneler(List<Eczaneler> eczaneler) {
        this.eczaneler = eczaneler;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
