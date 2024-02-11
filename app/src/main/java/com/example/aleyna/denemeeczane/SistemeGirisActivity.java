package com.example.aleyna.denemeeczane;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SistemeGirisActivity extends AppCompatActivity {

    private Button buttonGiris, buttonUyeOL;
    private RadioGroup radioGrubu;
    private RadioButton radioButtonDoktor, radioButtonEczane;
    private EditText editTextKulAdi, editTextSifre;
    Intent intent;
    private Kontroller kontrol;
    private List<Eczaneler> eczanelerListe;
    private List<Doktorlar> doktorlarListe;

    private EczanelerDaoInterface eczanelerDIF;
    private DoktorlarDaoInterface doktorlarDIF;
    private crudInterface hataDIF;

    private SharedPreferences sp;
    private  SharedPreferences.Editor editor;
    private String username,password,radioB;
    ProgressDialog oturumProgress;

    private Sifreleme sifreleme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sisteme_giris);
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_title);
        TextView actionbar_title = (TextView)getSupportActionBar().getCustomView().findViewById(R.id.actionBar_centered);
        actionbar_title.setText(" ");

        kontrol = new Kontroller(getApplicationContext());

        sifreleme = new Sifreleme();


        radioGrubu = findViewById(R.id.radioGrubu);
        radioButtonDoktor = findViewById(R.id.radioButtonDoktor);
        radioButtonEczane = findViewById(R.id.radioButtonEczane);
        buttonGiris = findViewById(R.id.buttonGiris);
        buttonUyeOL = findViewById(R.id.buttonUyeOl);
        editTextKulAdi = findViewById(R.id.editTextKulAdi);
        editTextSifre = findViewById(R.id.editTextSifre);
        oturumProgress = new ProgressDialog(this);

        sp = getSharedPreferences("GirisBilgi", MODE_PRIVATE);
        editor = sp.edit();

        username = sp.getString("username","null");
        password = sp.getString("password","null");
        radioB = sp.getString("radioB","null");


        eczanelerDIF = ApiUtils.getEczanelerDaoInterface();
        doktorlarDIF = ApiUtils.getDoktorlarDaoInterface();
        hataDIF = ApiUtils.getcrudInterface();


        if(!(username.equals("null"))&& !(password.equals("null"))){
            if(radioB.equals("doktor")){
                startActivity( new Intent(SistemeGirisActivity.this,DoktorActivity.class));
            }
            if (radioB.equals("eczane")){
                startActivity( new Intent(SistemeGirisActivity.this,EczaneActivity.class));
            }

        }

        buttonGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!kontrol.editTextDolumu(editTextKulAdi,"Kullanıcı Adı Boş Olamaz!")){
                    return;
                }
                if(!kontrol.editTextDolumu(editTextSifre,"Şifre Boş Olamaz!")){
                    return;
                }

                String sifre = sifreleme.md5Sifreleme(editTextSifre.getText().toString(),editTextKulAdi.getText().toString());

                oturumProgress.setTitle("Oturum Açılıyor");
                oturumProgress.setMessage("Hesabınıza giriş yapılıyor. Lütfen bekleyiniz.");
                oturumProgress.setCanceledOnTouchOutside(false);
                oturumProgress.show();

                int secilenRadio= radioGrubu.getCheckedRadioButtonId();
                switch (secilenRadio) {
                    case R.id.radioButtonDoktor: {
                            intent = new Intent(SistemeGirisActivity.this,DoktorActivity.class);
                            doktorlarDIF.doktorlarAra(editTextKulAdi.getText().toString(), sifre).enqueue(new Callback<DoktorlarCevap>() {
                                @Override
                                public void onResponse(Call<DoktorlarCevap> call, Response<DoktorlarCevap> response) {
                                    if (response.body().getSuccess()==0){
                                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        oturumProgress.dismiss();
                                    }
                                    else{
                                        doktorlarListe = response.body().getDoktorlar();
                                        for (Doktorlar d : doktorlarListe) {

                                            editor.putString("kullaniciId",d.getDoktorId());
                                            editor.putString("username",editTextKulAdi.getText().toString());
                                            editor.putString("password",editTextSifre.getText().toString());
                                            editor.putString("drAdi",d.getDoktorAd());
                                            editor.putString("radioB","doktor");
                                            editor.commit();


                                        }

                                        startActivity(intent);
                                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void onFailure(Call<DoktorlarCevap> call, Throwable t) {

                                }
                            });
                        break;
                    }
                    case R.id.radioButtonEczane: {

                        intent = new Intent(SistemeGirisActivity.this, EczaneActivity.class);
                        eczanelerDIF.eczanelerAra(editTextKulAdi.getText().toString(), sifre).enqueue(new Callback<EczanelerCevap>() {
                            @Override
                            public void onResponse(Call<EczanelerCevap> call, final Response<EczanelerCevap> response) {
                                if (response.body().getSuccess()==0){
                                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    oturumProgress.dismiss();
                                }
                                else{
                                    eczanelerListe = response.body().getEczaneler();
                                    for (Eczaneler e : eczanelerListe) {

                                        editor.putString("kullaniciId",e.getEczaneId());
                                        editor.putString("username",editTextKulAdi.getText().toString());
                                        editor.putString("password",editTextSifre.getText().toString());
                                        editor.putString("eczaneAdi",e.getEczaneAd());
                                        editor.putString("radioB","eczane");
                                        editor.commit();


                                    }
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<EczanelerCevap> call, Throwable t) {
                            }
                        });
                        break;
                        }  default:
                        Toast.makeText(SistemeGirisActivity.this
                                ,"Lütfen Bir Alan Seçiniz",Toast.LENGTH_SHORT).show();
                }

            }
        });

        buttonUyeOL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int secilenRadio= radioGrubu.getCheckedRadioButtonId();
                switch (secilenRadio) {
                    case R.id.radioButtonDoktor:
                    {
                        intent = new Intent(SistemeGirisActivity.this,DoktorUyeOlActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.radioButtonEczane:
                    {
                        intent = new Intent(SistemeGirisActivity.this,EczaneUyeOlActivity.class);
                        startActivity(intent);
                        break;
                    }default:
                        Toast.makeText(SistemeGirisActivity.this
                                ,"Lütfen Bir Alan Seçiniz",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void onBackPressed() {
        startActivity(new Intent(SistemeGirisActivity.this,AnasayfaActivity.class));
        return;
    }
}
