package com.example.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

//Класс предназначенный для пользованием BitmapFactory т.к. его размер ограничен - нужно скейлить изображение

public class PictureUtils {
    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight){
        //Чтение размера фото на диске
        BitmapFactory.Options options= new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        int inSampleSize = 1;

        if(srcHeight > destHeight || srcWidth > destWidth){
            float heighScale = srcHeight / destHeight;
            float widthScale = srcWidth / destWidth;

            inSampleSize = Math.round(Math.max(heighScale, widthScale));
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(path, options);

    }

    //Метод проверяет размер экрана и уменьшает изображение до этого размера.
    // Виджет ImageView, в который загружается изображение, всегда меньше размера экрана,
    // так что эта оценка весьма консервативна.
    public  static Bitmap getScaledBitmap(String path, Activity activity){
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return  getScaledBitmap(path, size.x, size.y);
    }
}
