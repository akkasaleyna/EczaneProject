package com.example.aleyna.denemeeczane;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoktorUyeOlActivity extends AppCompatActivity {
    private EditText editTextDoktorAd, editTextDoktorUnvan, editTextDoktorBrans, editTextHastane, editTextEPosta, editTextDoktorKullaniciAd, editTextDoktorSifre,editTextDoktorSifre2;
    private Button buttonDoktorUyeOl;
    private Kontroller kontrol;
    private DoktorlarDaoInterface doktorlarDIF;
    private crudInterface hataDIF;
    Intent intent;
    private Sifreleme sifreleme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doktor_uye_ol);
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_title);
        TextView actionbar_title = (TextView)getSupportActionBar().getCustomView().findViewById(R.id.actionBar_centered);
        actionbar_title.setText("Doktor Üye Ol");

        sifreleme = new Sifreleme();
        editTextDoktorAd = findViewById(R.id.editTextDoktorAd);
        editTextDoktorUnvan = findViewById(R.id.editTextDoktorUnvan);
        editTextDoktorBrans = findViewById(R.id.editTextDoktorBrans);
        editTextHastane = findViewById(R.id.editTextHastane);
        editTextEPosta = findViewById(R.id.editTextEPosta);
        editTextDoktorKullaniciAd = findViewById(R.id.editTextDoktorKullaniciAd);
        editTextDoktorSifre = findViewById(R.id.editTextDoktorSifre);
        editTextDoktorSifre2 = findViewById(R.id.editTextDoktorSifre2);
        buttonDoktorUyeOl = findViewById(R.id.buttonDoktorUyeOl);

        kontrol = new Kontroller(getApplicationContext());
        doktorlarDIF = ApiUtils.getDoktorlarDaoInterface();
        hataDIF = ApiUtils.getcrudInterface();

        buttonDoktorUyeOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!kontrol.editTextDolumu(editTextDoktorAd,"Doktor Adı Boş Olamaz!")){
                    return;
                }
                if(!kontrol.editTextDolumu(editTextDoktorKullaniciAd,"Kullanıcı Adı Boş Olamaz!")){
                    return;
                }
                if(!kontrol.editTextDolumu(editTextDoktorSifre,"Şifre Boş Olamaz!")){
                    return;
                }
                if(!kontrol.editTextlerEsitmi(editTextDoktorSifre, editTextDoktorSifre2,"Şifreler Uyuşmuyor!")){
                    return;
                }
                if(!kontrol.editTextDolumu(editTextEPosta,"E Posta Boş Olamaz!")){
                    return;
                }
                if(!kontrol.editTextDolumu(editTextHastane,"Hastane Boş Olamaz!")){
                    return;
                }
                if(!kontrol.editTextDolumu(editTextDoktorBrans,"Branş Boş Olamaz!")){
                    return;
                }
                if(!kontrol.editTextDolumu(editTextDoktorUnvan,"Unvan Boş Olamaz!")){
                    return;
                }

                String sifre = sifreleme.md5Sifreleme(editTextDoktorSifre.getText().toString(),editTextDoktorKullaniciAd.getText().toString());

                doktorlarDIF.doktorEkle(editTextDoktorAd.getText().toString(),editTextDoktorUnvan.getText().toString(),editTextDoktorBrans.getText().toString(),editTextHastane.getText().toString(),editTextEPosta.getText().toString(),editTextDoktorKullaniciAd.getText().toString(), sifre).enqueue(new Callback<DoktorlarCevap>() {
                    @Override
                    public void onResponse(Call<DoktorlarCevap> call, final Response<DoktorlarCevap> response) {
                       if (response.body().getSuccess()==0) {

                           Toast.makeText(getApplicationContext(),response.body().getMessage(), Toast.LENGTH_SHORT).show();
                       }
                       else {
                           intent = new Intent(DoktorUyeOlActivity.this, SistemeGirisActivity.class);
                           startActivity(intent);
                           Toast.makeText(getApplicationContext(),response.body().getMessage(), Toast.LENGTH_SHORT).show();
                       }
                    }
                    @Override
                    public void onFailure(Call<DoktorlarCevap> call, Throwable t) {

                    }
                });
            }
        });

    }

}
