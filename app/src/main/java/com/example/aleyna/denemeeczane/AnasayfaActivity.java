package com.example.aleyna.denemeeczane;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnasayfaActivity extends AppCompatActivity {

    private IlaclarDaoInterface doktorlarDIF;

    private Button buttonReceteGor , buttonSisGiris;
    private Intent intent;
    private EditText editTextRecKodu;
    //private Button buttonKonumAl, buttonGit;
    private String konumSaglayici = "gps";
    private int izinKontrol;
    private LocationManager locationManager;
    private RecetedekiIlaclarDaoInterface recIlaclarDIF;
    private ArrayList<String> recListemiz;
    private ArrayList<String> fiyatListemiz;
    private ArrayList<String> ilacIdListesi;
    private ArrayList<Integer> ilacAdetListesi;
    private double deneme;
    ProgressDialog oturumProgress;

    private double getDistanceFromLatLongInKm(double originLat,double originLong,double destinationLat, double destinationLong){

        double radius = 6371; // dünya yarıçapı km
        double dLat = deg2rad(destinationLat-originLat);
        double dLong = deg2rad(destinationLong-originLong);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(deg2rad(originLat)) * Math.cos(deg2rad(destinationLat))*
                        Math.sin(dLong/2) * Math.sin(dLong/2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double result = radius * c;// KM cinsinden mesafe
        return result;
    }

    private double deg2rad(double mDeg){
        return mDeg* (Math.PI/180);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);

        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_title);
        TextView actionbar_title = (TextView)getSupportActionBar().getCustomView().findViewById(R.id.actionBar_centered);
        actionbar_title.setText("");

        editTextRecKodu = findViewById(R.id.editTextRecKodu);
        //buttonGit = findViewById(R.id.buttonGit);
        //buttonKonumAl = findViewById(R.id.buttonKonumAl);
        recIlaclarDIF = ApiUtils.getRecetedekiIlaclarDaoInterface();
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        oturumProgress = new ProgressDialog(this);

        doktorlarDIF = ApiUtils.getIlaclarDaoInterface();

        buttonReceteGor = findViewById(R.id.buttonReceteGor);
        buttonSisGiris = findViewById(R.id.buttonSisGiris);


        deneme= getDistanceFromLatLongInKm(40.747985, 30.347032,40.745113, 30.342158);


        buttonReceteGor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                recIlaclarDIF.tumRecIlaclarByReceteKodu(editTextRecKodu.getText().toString()).enqueue(new Callback<RecetedekiIlaclarCevap>() {
                    @Override
                    public void onResponse(Call<RecetedekiIlaclarCevap> call, Response<RecetedekiIlaclarCevap> response) {
                        if(response.body().getSuccess()==0) {
                            Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else{

                            recListemiz=new ArrayList<>();
                            fiyatListemiz = new ArrayList<>();
                            ilacIdListesi = new ArrayList<>();
                            ilacAdetListesi = new ArrayList<>();
                            List<RecetedekiIlaclar> recetedekiIlaclarListe = response.body().getRecetedekiIlaclar();
                            int deger = recetedekiIlaclarListe.size();

                            for (int i = 0; i < recetedekiIlaclarListe.size(); i++) {
                                recListemiz.add(recetedekiIlaclarListe.get(i).getIlac().getIlacAd());
                                fiyatListemiz.add(recetedekiIlaclarListe.get(i).getIlac().getIlacFiyat());
                                ilacIdListesi.add(recetedekiIlaclarListe.get(i).getIlac().getIlacId());
                                ilacAdetListesi.add((recetedekiIlaclarListe.get(i).getYazilanIlacAdedi()));

                            }

                            intent = new Intent(AnasayfaActivity.this,FiyatActivity.class);
                            intent.putStringArrayListExtra("recListemiz", recListemiz);
                            intent.putStringArrayListExtra("fiyatListemiz", fiyatListemiz);
                            intent.putIntegerArrayListExtra("ilacAdetListesi",ilacAdetListesi);

                            oturumProgress.setTitle("Reçeteniz Görüntüleniyor");
                            oturumProgress.setMessage("Lütfen bekleyiniz");
                            oturumProgress.setCanceledOnTouchOutside(false);
                            oturumProgress.show();

                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onFailure(Call<RecetedekiIlaclarCevap> call, Throwable t) {
                    }
                });
            }
        });

        buttonSisGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent=new Intent(AnasayfaActivity.this,SistemeGirisActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        oturumProgress.dismiss();
        ActivityCompat.finishAffinity(AnasayfaActivity.this);
    }
}
