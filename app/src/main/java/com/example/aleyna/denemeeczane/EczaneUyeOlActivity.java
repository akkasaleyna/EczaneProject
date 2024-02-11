package com.example.aleyna.denemeeczane;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EczaneUyeOlActivity extends AppCompatActivity {

    private EditText editTextEczaneAdi,editTextEPosta,editTextEczaneKullaniciAdi,editTextEczaneSifre,editTextEczaneSifre2,editTextIlAdi
            ,editTextIlceAdi,editTextMahalleAdi,editTextSokakAdi,editTextCaddeAdi
            ,editTextNo,editTextTelNo;
    private Button buttonEczaneUyeOl;
    private Kontroller kontrol;
    private EczanelerDaoInterface eczanelerDIF;
    Intent intent;
    private Sifreleme sifreleme;
    String mahalleAdi="";
    String ilceAdi="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eczane_uye_ol);
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_title);
        TextView actionbar_title = (TextView)getSupportActionBar().getCustomView().findViewById(R.id.actionBar_centered);
        actionbar_title.setText("Eczane Üye Ol");

        sifreleme = new Sifreleme();
        editTextEczaneAdi = findViewById(R.id.editTextEczaneAdi);
        editTextEczaneKullaniciAdi = findViewById(R.id.editTextEczaneKullaniciAdi);
        editTextEczaneSifre = findViewById(R.id.editTextAdet);
        editTextEczaneSifre2 = findViewById(R.id.editTextEczaneSifre2);
        editTextIlAdi = findViewById(R.id.editTextIlAdi);
        editTextIlceAdi = findViewById(R.id.editTextIlceAdi);
        editTextMahalleAdi = findViewById(R.id.editTextMahalleAdi);
        editTextSokakAdi = findViewById(R.id.editTextSokakAdi);
        editTextCaddeAdi = findViewById(R.id.editTextCaddeAdi);
        editTextNo = findViewById(R.id.editTextNo);
        editTextEPosta = findViewById(R.id.editTextEPosta);
        editTextTelNo = findViewById(R.id.editTextTelNo);
        buttonEczaneUyeOl = findViewById(R.id.buttonEczaneUyeOl);

        kontrol = new Kontroller(getApplicationContext());
        eczanelerDIF = ApiUtils.getEczanelerDaoInterface();



        buttonEczaneUyeOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!kontrol.editTextDolumu(editTextEczaneAdi,"Eczane Adı Boş Olamaz!")){
                    return;
                }
                if(!kontrol.editTextDolumu(editTextIlAdi,"İl Adı Boş Olamaz!")){
                    return;
                }
                if(!kontrol.editTextDolumu(editTextIlceAdi,"İlçe Adı Boş Olamaz!")){
                    return;
                }
                if(!kontrol.editTextDolumu(editTextMahalleAdi,"Mahalle Adı Boş Olamaz!")){
                    return;
                }
                if(!kontrol.editTextDolumu(editTextTelNo,"Telefon Numarası Boş Olamaz!")){
                    return;
                }
                if(!kontrol.editTextDolumu(editTextEPosta,"E Posta Boş Olamaz!")){
                    return;
                }
                if(!kontrol.editTextDolumu(editTextEczaneKullaniciAdi,"Kullanıcı Adı Boş Olamaz!")){
                    return;
                }
                if(!kontrol.editTextDolumu(editTextEczaneSifre,"Şifre Boş Olamaz!")){
                    return;
                }
                if(!kontrol.editTextDolumu(editTextEczaneSifre2,"Şifre Tekrar Boş Olamaz!")){
                    return;
                }
                if(!kontrol.editTextlerEsitmi(editTextEczaneSifre,editTextEczaneSifre2,"Şifreler Uyuşmuyor")){
                    return;
                }

                final String telno = editTextTelNo.getText().toString().replaceAll("\\s","");
                final String key = "AIzaSyAoqvfBuA96tbG2DnuA2J1LKsAjUFXdHFM";
                String url = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=%2B90"+telno+"&inputtype=phonenumber&fields=geometry&key="+key;
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray mekanlar = jsonObject.getJSONArray("candidates");

                            for(int i=0; i<mekanlar.length(); i++){
                                JSONObject m = mekanlar.getJSONObject(i);
                                JSONObject geometry = m.getJSONObject("geometry");
                                JSONObject location = geometry.getJSONObject("location");

                                final String enlem = location.getString("lat");
                                final String boylam = location.getString("lng");




                                String konum = enlem+","+boylam;
                                String url2 = "https://maps.googleapis.com/maps/api/geocode/json?latlng="+konum+"&key="+key;
                                StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url2, new com.android.volley.Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {
                                            String type;
                                            JSONObject jsonObject = new JSONObject(response);
                                            JSONArray mekanlar2 = jsonObject.getJSONArray("results");

                                            if(mekanlar2.length()>0){
                                                JSONObject m2 = mekanlar2.getJSONObject(0);
                                                JSONArray adresler2 = m2.getJSONArray("address_components");

                                                for (int i = 0; i < adresler2.length(); i++) {
                                                    JSONObject a = adresler2.getJSONObject(i);
                                                    type = a.getString("types");

                                                    if (type.contentEquals("[\"administrative_area_level_2\",\"political\"]")) {
                                                        ilceAdi = a.getString("long_name");
                                                    }
                                                    if (type.contentEquals("[\"administrative_area_level_4\",\"political\"]")) {
                                                        mahalleAdi = a.getString("long_name");
                                                    }

                                                }


                                            }else{
                                                Toast.makeText(getApplicationContext(),"RESULT HATASI",Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        if (ilceAdi.contentEquals(editTextIlceAdi.getText().toString())&&mahalleAdi.contentEquals(editTextMahalleAdi.getText().toString())){

                                            //Her şey doğruysa kaydı yapar.
                                            String sifre = sifreleme.md5Sifreleme(editTextEczaneSifre.getText().toString(),editTextEczaneKullaniciAdi.getText().toString());
                                            eczanelerDIF.eczaneEkle(editTextEczaneAdi.getText().toString(),editTextEPosta.getText().toString(), editTextEczaneKullaniciAdi.getText().toString(), sifre, editTextIlAdi.getText().toString(), editTextIlceAdi.getText().toString(),editTextMahalleAdi.getText().toString(),editTextSokakAdi.getText().toString(),editTextCaddeAdi.getText().toString(),editTextNo.getText().toString(),editTextTelNo.getText().toString(),Double.parseDouble(enlem),Double.parseDouble(boylam)).enqueue(new Callback<EczanelerCevap>() {
                                                @Override
                                                public void onResponse(Call<EczanelerCevap> call, Response<EczanelerCevap> response) {
                                                    if (response.body().getSuccess()==0) {
                                                        Toast.makeText(getApplicationContext(),response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                    else {
                                                        intent = new Intent(EczaneUyeOlActivity.this, SistemeGirisActivity.class);
                                                        startActivity(intent);
                                                        Toast.makeText(getApplicationContext(),response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<EczanelerCevap> call, Throwable t) {

                                                }
                                            });
                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(), "Lütfen Adres Bilgilerinizi Doğru Giriniz", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new com.android.volley.Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                });
                                Volley.newRequestQueue(getApplicationContext()).add(stringRequest2);






                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                Volley.newRequestQueue(getApplicationContext()).add(stringRequest);









            }
        });
    }
}
