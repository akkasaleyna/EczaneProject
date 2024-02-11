package com.example.aleyna.denemeeczane;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.NonNull;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MekanRVAdapter extends RecyclerView.Adapter<MekanRVAdapter.CardTasarimTutucu> {
    private Context mContext;
    private List<Mekanlar> mekanlarListe;
    private EczanelerDaoInterface eczDIF = ApiUtils.getEczanelerDaoInterface();

    public MekanRVAdapter(Context mContext, List<Mekanlar> mekanlarListe) {
        this.mContext = mContext;
        this.mekanlarListe = mekanlarListe;
    }



    public  class CardTasarimTutucu extends RecyclerView.ViewHolder{
        private TextView textViewMekanAdi, textViewAdres,textViewAdres2, textViewUzaklik;
        private Button buttonHaritadaGor;



        public CardTasarimTutucu(@NonNull View itemView) {
            super(itemView);

            textViewMekanAdi = itemView.findViewById(R.id.textViewMekanAdi);
            textViewAdres = itemView.findViewById(R.id.textViewAdres);
            textViewAdres2 = itemView.findViewById(R.id.textViewAdres2);
            textViewUzaklik = itemView.findViewById(R.id.textViewUzaklik);
            buttonHaritadaGor = itemView.findViewById(R.id.buttonHaritadaGor);

        }
    }

    @NonNull
    @Override
    public CardTasarimTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.yerler_card_tasarim,parent,false);
        return new CardTasarimTutucu(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardTasarimTutucu holder, final int position) {
        final Mekanlar mekan = mekanlarListe.get(position);
        final String oid=mekanlarListe.get(position).getMekan_id();

        holder.textViewAdres.setText(mekan.getAdres());

                eczDIF.eczIdsineGoreEnlemBoylam(oid).enqueue(new Callback<EczanelerCevap>() {
                    @Override
                    public void onResponse(Call<EczanelerCevap> call, Response<EczanelerCevap> response) {

                        List<Eczaneler> sonucEczListesi = response.body().getEczaneler();
                        holder.textViewMekanAdi.setText(String.valueOf(sonucEczListesi.get(0).getEczaneAd()));
                        holder.textViewAdres.setText(String.valueOf(sonucEczListesi.get(0).getMahalleAdi())+" "+
                                String.valueOf(sonucEczListesi.get(0).getSokakAdi())+" "+
                                String.valueOf(sonucEczListesi.get(0).getCaddeAdi())+" "+
                                String.valueOf(sonucEczListesi.get(0).getNo()));
                        holder.textViewAdres2.setText(String.valueOf(sonucEczListesi.get(0).getIlAdi())+"/"+
                                String.valueOf(sonucEczListesi.get(0).getIlceAdi()));
                        String enlemS=(String.valueOf(sonucEczListesi.get(0).getEnlem()));
                        String boylamS=(String.valueOf(sonucEczListesi.get(0).getBoylam()));
                        String id=(String.valueOf(sonucEczListesi.get(0).getEczaneId()));



                        String key ="AIzaSyAoqvfBuA96tbG2DnuA2J1LKsAjUFXdHFM";
                        String konumOrigin = mekanlarListe.get(position).getEnlem()+","+mekanlarListe.get(position).getBoylam();
                        final String konumDes = enlemS+","+boylamS;
                        String url="https://maps.googleapis.com/maps/api/distancematrix/json?origins="+konumOrigin+"&destinations="+konumDes+"&key="+key;

                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray rows = jsonObject.getJSONArray("rows");
                                    JSONObject rowsIlk = rows.getJSONObject(0);
                                    JSONArray r = rowsIlk.getJSONArray("elements");
                                    JSONObject elements = r.getJSONObject(0);
                                    JSONObject a= elements.getJSONObject("distance");
                                    String text =a.getString("text");
                                    String abc =text.substring(0,4) ;
                                    //abc.trim();
                                    holder.textViewUzaklik.setText(abc.trim()+" km");
                                    Log.e("TRIM",abc);

                                    holder.buttonHaritadaGor.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            Uri location = Uri.parse("geo:"+konumDes+"?z=20");
                                            Intent mapIntent = new Intent(Intent.ACTION_VIEW,location);

                                            PackageManager packageManager =mContext.getPackageManager();
                                            List<ResolveInfo> activities = packageManager.queryIntentActivities(mapIntent,0);
                                            boolean isIntentSafe = activities.size()>0;
                                            if(isIntentSafe){
                                                String title = "Uygulama Seçiniz";
                                                Intent chooser = Intent.createChooser(mapIntent,title);
                                                if (mapIntent.resolveActivity(packageManager)!=null){
                                                    mContext.startActivity(chooser);
                                                }
                                            }
                                        }
                                    });

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        Volley.newRequestQueue(mContext).add(stringRequest);
                    }

                    @Override
                    public void onFailure(Call<EczanelerCevap> call, Throwable t) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return mekanlarListe.size();
    }
}
