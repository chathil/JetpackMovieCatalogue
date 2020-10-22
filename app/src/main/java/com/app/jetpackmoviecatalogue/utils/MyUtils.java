package com.app.jetpackmoviecatalogue.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyUtils {
    public static String getDate(String dateInput) {
        if (dateInput != null) {
            DateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date;
            try {
                date = readFormat.parse(dateInput);
            } catch (ParseException e) {
                e.printStackTrace();
                return "N/A";
            }
            DateFormat displayFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
            return displayFormat.format(date);
        }
        return "N/A";
    }

    public static String getYear(String inputDate){
        if (inputDate != null) {
            DateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date;
            try {
                date = readFormat.parse(inputDate);
            } catch (ParseException e) {
                e.printStackTrace();
                return "N/A";
            }
            DateFormat displayFormat = new SimpleDateFormat("yyyy");
            return displayFormat.format(date);
        }
        return "N/A";
    }

    public static boolean isReleased(String inputDate) {
        if (inputDate != null) {
            DateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            try {
                return (readFormat.parse(inputDate).after(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        }
        return false;
    }

    public static String getToday(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(c);
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

}
