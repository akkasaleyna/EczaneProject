package com.example.aleyna.denemeeczane;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface EczanelerDaoInterface {

    @GET("deneme/tum_eczaneler.php")
    Call<EczanelerCevap> tumEczaneler();

    @POST("deneme/eczane_login.php")
    @FormUrlEncoded
    Call<EczanelerCevap> eczanelerAra(@Field("kullanici_adi")String kullanici_adi,@Field("sifre")String sifre);

    @POST("deneme/eczaneKaydiRetroylaDeneme.php")
    @FormUrlEncoded
    Call<EczanelerCevap> eczaneEkle(@Field("eczane_ad")String eczane_ad
            ,@Field("eposta")String eposta
            ,@Field("kullanici_adi")String kullanici_adi
            ,@Field("sifre")String sifre
            ,@Field("ilAdi")String ilAdi
            ,@Field("ilceAdi")String ilceAdi
            ,@Field("mahalleAdi")String mahalleAdi
            ,@Field("sokakAdi")String sokakAdi
            ,@Field("caddeAdi")String caddeAdi
            ,@Field("no")String no
            ,@Field("telNo")String telNo
            ,@Field("enlem")double enlem
            ,@Field("boylam")double boylam);

    @POST("deneme/ilAdi_ilceAdinaGoreEczaneler.php")
    @FormUrlEncoded
    Call<EczanelerCevap> ilveIlceAdinaGoreEczaneler(@Field("ilAdi")String ilAdi,@Field("ilceAdi")String ilceAdi);

    @POST("deneme/eczaneID_gore_bilgiler.php")
    @FormUrlEncoded
    Call<EczanelerCevap> eczIdsineGoreEnlemBoylam(@Field("eczane_id")String eczane_id);

    @POST("deneme/update_eczaneSifre.php")
    @FormUrlEncoded
    Call<EczanelerCevap> update_eczaneSifre(@Field("eczane_id")String eczane_id, @Field("sifre")String sifre, @Field("yeniSifre")String yeniSifre);

    @POST("deneme/sadece_kullaniciAdiKontrol_ikiside.php")
    @FormUrlEncoded
    Call<EczanelerCevap> sadece_kullaniciAdiKontrol_ikiside(@Field("kullanici_adi")String kullanici_adi);

    @POST("deneme/update_eczaneler.php")
    @FormUrlEncoded
    Call<EczanelerCevap> eczaneGuncelle(@Field("eczane_id")String eczane_id
                                        ,@Field("yeniSifre")String yeniSifre
                                        ,@Field("eczane_ad")String eczane_ad
                                        ,@Field("eposta")String eposta
                                        ,@Field("kullanici_adi")String kullanici_adi
                                        ,@Field("ilAdi")String ilAdi
                                        ,@Field("ilceAdi")String ilceAdi
                                        ,@Field("mahalleAdi")String mahalleAdi
                                        ,@Field("sokakAdi")String sokakAdi
                                        ,@Field("caddeAdi")String caddeAdi
                                        ,@Field("no")String no
                                        ,@Field("telNo")String telNo
                                        ,@Field("enlem")double enlem
                                        ,@Field("boylam")double boylam);
}
