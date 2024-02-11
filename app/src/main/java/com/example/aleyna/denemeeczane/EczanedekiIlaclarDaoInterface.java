package com.example.aleyna.denemeeczane;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface EczanedekiIlaclarDaoInterface {

    @POST("deneme/eczilaclar_by_eczane_id.php")
    @FormUrlEncoded
    Call<EczanedekiIlaclarCevap> tumEczIlaclarByEczaneID(@Field("eczane_id") String eczane_id);

    @POST("deneme/eczaneID_ilacIDye_gore_ilacAdediBul.php")
    @FormUrlEncoded
    Call<EczanedekiIlaclarCevap> ilacAdedi(@Field("eczane_id") String eczane_id, @Field("ilac_id") String ilac_id);

    @POST("deneme/eczanedeki_ilaclara_kayit_ekle.php")
    @FormUrlEncoded
    Call<EczanedekiIlaclarCevap> eczanedekiIlaclar_YeniKayit(@Field("eczane_id") String eczane_id, @Field("ilac_id") String ilac_id, @Field("ilac_adet") int ilac_adet);

    @POST("deneme/eczanedeki_ilaclar_guncelle.php")
    @FormUrlEncoded
    Call<EczanedekiIlaclarCevap> eczanedekiIlaclar_Guncelle(@Field("eczane_id") String eczane_id, @Field("ilac_id") String ilac_id, @Field("ilac_adet") int ilac_adet);

    @POST("deneme/eczane_adina_gore_eczIlaclar.php")
    @FormUrlEncoded
    Call<EczanedekiIlaclarCevap> eczAdinaGoreEczIlaclar(@Field("eczane_ad") String eczane_ad);

    @POST("deneme/eczaneID_ilacAd_gore_eczIlaclarVarmi.php")
    @FormUrlEncoded
    Call<EczanedekiIlaclarCevap> eczaneID_ilacAd_gore_eczIlaclarVarmi(@Field("eczane_id") String eczane_id, @Field("ilac_ad") String ilac_ad, @Field("ilac_adet") int ilac_adet);


}
