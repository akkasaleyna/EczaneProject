package com.example.aleyna.denemeeczane;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FiyatActivity extends AppCompatActivity implements LocationListener  {
    private RecyclerView rv;
    private MyAdapter adapter;
    private Context mContext;
    private int izinKontrol;
    private LocationManager locationManager;
    private TextView textViewRecToplamFiyat,textViewMuadil;
    private String gelenReceteKodu;
    private Button buttonEczBul,buttonMuadilFiyat;

    private ArrayList<String> recListemiz;
    private ArrayList<String> fiyatListemiz;
    private ArrayList<Integer> ilacAdetListesi;
    private double toplamFiyat = 0;
    private String toplamMuadilFiyat;

    private ArrayList<String> olanEczaneler;
    private ArrayList<String> sonucEczaneler;
    List<Eczaneler> eczanelerListe;
    private ArrayList<String> ilveIlceArrayList;
    private EczanedekiIlaclarDaoInterface eczanedekiIlaclarDIF;
    private EczanelerDaoInterface eczanelerDIF;
    ArrayList<String> eczanelerinIDleriListe;
    String ilAdi="";
    String ilceAdi="";
    int x,y;
    int sayac=0;
    private Intent intent;
    ProgressDialog oturumProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiyat);
        oturumProgress = new ProgressDialog(this);

        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_title_black);
        TextView actionbar_title = (TextView)getSupportActionBar().getCustomView().findViewById(R.id.actionBar_centered);
        actionbar_title.setText("Reçetenizdeki İlaçlar");

        olanEczaneler = new ArrayList<>();
        sonucEczaneler = new ArrayList<>();
        recListemiz = new ArrayList<>();
        fiyatListemiz = new ArrayList<>();
        ilacAdetListesi = new ArrayList<>();
        textViewRecToplamFiyat = findViewById(R.id.textViewRecToplamFiyat);
        textViewMuadil = findViewById(R.id.textViewMuadil);
        buttonMuadilFiyat = findViewById(R.id.buttonMuadilFiyat);
        intent= new Intent(FiyatActivity.this, MekanlarActivity.class);
        eczanedekiIlaclarDIF = ApiUtils.getEczanedekiIlaclarDaoInterface();
        eczanelerDIF = ApiUtils.getEczanelerDaoInterface();

        rv = findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        buttonEczBul= findViewById(R.id.buttonEczBul);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        recListemiz = getIntent().getStringArrayListExtra("recListemiz");
        fiyatListemiz = getIntent().getStringArrayListExtra("fiyatListemiz");
        ilacAdetListesi = getIntent().getIntegerArrayListExtra("ilacAdetListesi");

        for(int i=0;i<recListemiz.size();i++){

            Log.e("RECLİSTEMİZ  : ", recListemiz.get(i).toString());
        }

        adapter = new MyAdapter(getApplicationContext(),recListemiz,fiyatListemiz,buttonMuadilFiyat,textViewMuadil,ilacAdetListesi);

        rv.setAdapter(adapter);
        toplamMuadilFiyat=adapter.getToplamdeger();

        for(int i=0; i < fiyatListemiz.size(); i++)
        {
            double carpimFiyat = Double.parseDouble(fiyatListemiz.get(i))*ilacAdetListesi.get(i);
            Log.e("Çarpımımız",String.valueOf(carpimFiyat));
            toplamFiyat = toplamFiyat + carpimFiyat;
        }

        textViewRecToplamFiyat.setText(String.valueOf(new DecimalFormat("##.##").format(toplamFiyat).replace(',','.'))+" ₺");

        izinKontrol = ContextCompat.checkSelfPermission(FiyatActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(izinKontrol != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(FiyatActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},100);
        }
        else {

            Location konum = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);

            if(konum != null){
                onLocationChanged(konum);

            }
            else {
                Toast.makeText(getApplicationContext(),"Konum Bulunamadı",Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onLocationChanged(Location location) {

        Double enlem= location.getLatitude();
        Double boylam=location.getLongitude();
        mekanGetir(String.valueOf(enlem),String.valueOf(boylam));

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

            if(requestCode ==100){

                izinKontrol = ContextCompat.checkSelfPermission(FiyatActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    Toast.makeText(getApplicationContext(),"İzin Verildi",Toast.LENGTH_SHORT).show();

                    Location konum = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);

                    if(konum != null){
                        onLocationChanged(konum);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"hata",Toast.LENGTH_SHORT).show();
                    }

                }else{

                    Toast.makeText(getApplicationContext(),"İzin Verilmedi",Toast.LENGTH_SHORT).show();
                }
            }
    }
    public void mekanGetir(final String enlem, final String boylam){

        String key = "AIzaSyAoqvfBuA96tbG2DnuA2J1LKsAjUFXdHFM";
        String konum = enlem+","+boylam;
        Log.e("enlem mekan getir içi", enlem);
        Log.e("boylam mekan getir içi", boylam);
        String url="https://maps.googleapis.com/maps/api/geocode/json?latlng="+konum+"&key="+key;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String type;
                    ilveIlceArrayList = new ArrayList<>();
                    eczanelerinIDleriListe = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray mekanlar = jsonObject.getJSONArray("results");
                    if (mekanlar.length() > 0) {

                        JSONObject m = mekanlar.getJSONObject(0);
                        JSONArray adresler = m.getJSONArray("address_components");

                        for (int i = 0; i < adresler.length(); i++) {
                            JSONObject a = adresler.getJSONObject(i);
                            type = a.getString("types");

                            if (type.contentEquals("[\"administrative_area_level_1\",\"political\"]")) {
                                ilAdi = a.getString("long_name");
                                //Log.e("İL : ",ilAdi);
                            }

                            if (type.contentEquals("[\"administrative_area_level_2\",\"political\"]")) {
                                ilceAdi = a.getString("long_name");
                                //Log.e("İLÇE : ",ilceAdi);
                            }
                        }
                    }else {

                        Toast.makeText(getApplicationContext(),"RESULT HATASI",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("İL mekan getir içi: ",ilAdi);
                Log.e("İLÇE dıf üstü: ",ilceAdi);

                //Serdivan Sakaryadaki eczaneleri buluyor ve id lerini bir listeye atıyor.
                eczanelerDIF.ilveIlceAdinaGoreEczaneler(ilAdi,ilceAdi).enqueue(new Callback<EczanelerCevap>() {
                    @Override
                    public void onResponse(Call<EczanelerCevap> call, retrofit2.Response<EczanelerCevap> response) {

                        eczanelerListe = response.body().getEczaneler();
                        for(int k=0;k<eczanelerListe.size();k++) {
                            eczanelerinIDleriListe.add(eczanelerListe.get(k).getEczaneId());

                        }

                        for(x=0;x<eczanelerinIDleriListe.size();x++) {
                            for (y=0; y < recListemiz.size(); y++) {
                                Log.e("x",String.valueOf(x));
                                Log.e("y",String.valueOf(y));

                                eczanedekiIlaclarDIF.eczaneID_ilacAd_gore_eczIlaclarVarmi(eczanelerinIDleriListe.get(x).toString(), recListemiz.get(y).toString(), ilacAdetListesi.get(y)).enqueue(new Callback<EczanedekiIlaclarCevap>() {
                                    @Override
                                    public void onResponse(Call<EczanedekiIlaclarCevap> call, retrofit2.Response<EczanedekiIlaclarCevap> response) {
                                        Log.e("loggggg",response.body().getMessage());
                                        Log.e("x",String.valueOf(x));
                                        Log.e("y",String.valueOf(y));
                                        if(response.body().getSuccess()==1){
                                            List<EczanedekiIlaclar>listem=response.body().getEczanedekiIlaclar();

                                            olanEczaneler.add(listem.get(0).getEczane().getEczaneId());



                                        }
                                        buttonEczBul.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                intent.putExtra("enlem",enlem);
                                                intent.putExtra("boylam",boylam);
                                                intent.putExtra("recSize",String.valueOf(recListemiz.size()));
                                                intent.putStringArrayListExtra("ECZANEVAR",olanEczaneler);
                                                intent.putStringArrayListExtra("recListemiz",recListemiz);
                                                intent.putIntegerArrayListExtra("ilacAdetListesi",ilacAdetListesi);
                                                intent.putStringArrayListExtra("fiyatListemiz",fiyatListemiz);
                                                //intent.putExtra("eczaneninIDSI",listem.get(0).getEczane().getEczaneId());
                                                startActivity(intent);
                                            }
                                        });

                                    }
                                    @Override
                                    public void onFailure(Call<EczanedekiIlaclarCevap> call, Throwable t) {
                                    }
                                });

                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<EczanelerCevap> call, Throwable t) {
                    }
                });
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }
    @Override
    public void onBackPressed() {
        finish();
        oturumProgress.dismiss();
        startActivity(new Intent(FiyatActivity.this,AnasayfaActivity.class));

        return;
    }

}
