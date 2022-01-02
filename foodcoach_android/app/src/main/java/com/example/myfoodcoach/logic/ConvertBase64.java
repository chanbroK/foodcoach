package com.example.myfoodcoach.logic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class ConvertBase64 {


    public static Bitmap decodeBase64(String base64Image) {
        byte[] decodeString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap decoded = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        return decoded;
    }


}
