package com.example.aleyna.denemeeczane;

import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MekanlarActivity extends AppCompatActivity {

    private RecyclerView rv;
    private double enlem;
    private double boylam;
    private ArrayList<Mekanlar> mekanlarArrayList;
    private ArrayList<String> ilveIlceArrayList;
    private MekanRVAdapter adapter;
    private EczanedekiIlaclarDaoInterface eczanedekiIlaclarDIF;
    private EczanelerDaoInterface eczanelerDIF;
    private ArrayList<String> recListemiz;
    private ArrayList<String> fiyatListemiz;
    private ArrayList<Integer> ilacAdetListesi;
    List<Eczaneler> eczanelerListe;
    private List<EczanedekiIlaclar> eczanedekiIlaclarListe;
    ArrayList<String> eczanelerinIDleriListe;
    private int i, j;
    private ArrayList<String> varOlanlar;
    private ArrayList<String> sonucEcz;
    private ArrayList<String> mekanGetirdenAlinanListe;
    private ArrayList<Double> doubleListe;
    private double[] siraliDizi;
    String ilAdi="";
    String ilceAdi="";
    int sayac=0;
    int p=0;
    private ArrayList<String> deneme;
    private int gelenRecSize;
    private String geleneczaneIDSI;
    String enlemS;
    String boylamS;
    String id;
    final Context context = this;
    //ProgressDialog oturumProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yerler);

        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_title_black);
        TextView actionbar_title = (TextView)getSupportActionBar().getCustomView().findViewById(R.id.actionBar_centered);
        actionbar_title.setText("Bulunan Eczaneler");


        rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        eczanelerDIF = ApiUtils.getEczanelerDaoInterface();

        recListemiz = new ArrayList<>();
        fiyatListemiz = new ArrayList<>();
        ilacAdetListesi = new ArrayList<>();
        sonucEcz = new ArrayList<>();
        varOlanlar = new ArrayList<>();
        deneme = new ArrayList<>();
        doubleListe = new ArrayList<>();
        siraliDizi = new double[7];

        enlem=Double.parseDouble(getIntent().getStringExtra("enlem"));
        boylam=Double.parseDouble(getIntent().getStringExtra("boylam"));
        mekanlarArrayList = new ArrayList<>();
        varOlanlar=getIntent().getStringArrayListExtra("ECZANEVAR");
        gelenRecSize=Integer.parseInt(getIntent().getStringExtra("recSize"));
        recListemiz = getIntent().getStringArrayListExtra("recListemiz");
        fiyatListemiz = getIntent().getStringArrayListExtra("fiyatListemiz");
        ilacAdetListesi = getIntent().getIntegerArrayListExtra("ilacAdetListesi");


        Log.e("recListeninSize",String.valueOf(gelenRecSize));
        Log.e("mekanlardakiEnlem",String.valueOf(enlem));
        Log.e("mekanlardakiBoylam",String.valueOf(boylam));

        Log.e("şimdi gönderildi  : ", varOlanlar.toString());

        Collections.sort(varOlanlar);
        Log.e("Sıralı şimdi  : ", varOlanlar.toString());

        for(int i=0;i<varOlanlar.size();i++){
            int sayac=0;
            if(!varOlanlar.get(i).isEmpty()) {
                for(int j=0;j<varOlanlar.size();j++) {
                    if(!(i>varOlanlar.size()-1)) {
                        if (varOlanlar.get(i).equals(varOlanlar.get(j))) {

                            sayac++;

                            if (sayac == gelenRecSize) {
                                sonucEcz.add(varOlanlar.get(i));
                                i = i + gelenRecSize;
                            }
                        } else {
                            continue;
                        }
                    }
                }
            }
        }

        if(sonucEcz.isEmpty())
        {
            alert();

        }
        for(int i=0;i<sonucEcz.size();i++) {
            Log.e("sonucEcz", sonucEcz.get(i).toString());
        }
        for(int i=0;i<sonucEcz.size();i++) {
            Mekanlar m=new Mekanlar(sonucEcz.get(i).toString(),"",enlem,boylam,"");
            mekanlarArrayList.add(m);
        }



        adapter = new MekanRVAdapter(MekanlarActivity.this, mekanlarArrayList);
        rv.setAdapter(adapter);



    public void sirala(ArrayList<Double> sirali){


        for(int m=0;m<sirali.size();m++){

            Log.e("HAYDEEEEĞ",sirali.get(m).toString());
        }

    }


    public void alert(){

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View goster = layoutInflater.inflate(R.layout.alert_eczane_bulunamadi,null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setView(goster);


        alertDialogBuilder.setTitle("Eczane Bulunamadı");

        alertDialogBuilder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent nn = new Intent(MekanlarActivity.this, YeniIlveIlceActivity.class);
                nn.putExtra("enlem", String.valueOf(enlem));
                nn.putExtra("boylam", String.valueOf(boylam));
                nn.putStringArrayListExtra("recListemiz", recListemiz);
                nn.putIntegerArrayListExtra("ilacAdetListesi", ilacAdetListesi);
                nn.putStringArrayListExtra("fiyatListemiz", fiyatListemiz);
                startActivity(nn);
            }
        });
        alertDialogBuilder.setNegativeButton("Anasayfaya Dön", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent nn = new Intent(MekanlarActivity.this, AnasayfaActivity.class);
                startActivity(nn);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
