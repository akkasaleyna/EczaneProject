package com.example.aleyna.denemeeczane;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sifreleme {

    public String md5Sifreleme(String parola,String kullaniciAdi) {


        String sifrelenecekVeri = parola+kullaniciAdi;
        StringBuffer sb16 = new StringBuffer();
        try {
            MessageDigest messageDigestNesnesi = MessageDigest.getInstance("MD5");
            messageDigestNesnesi.update(sifrelenecekVeri.getBytes());
            byte messageDigestDizisi[] = messageDigestNesnesi.digest();

            for (int i = 0; i < messageDigestDizisi.length; i++) {
                sb16.append(Integer.toString((messageDigestDizisi[i] & 0xff) + 0x100, 16).substring(1));

            }
            Log.e("SifrelenmisHali",sb16.toString());


        }
        catch (NoSuchAlgorithmException ex){
            System.err.println(ex);
        }

        return sb16.toString();
    }
}