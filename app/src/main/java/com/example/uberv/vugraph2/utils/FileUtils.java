package com.example.uberv.vugraph2.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

public class FileUtils {

    public static final String LOG_TAG = FileUtils.class.getSimpleName();

    /** reads bitmap from a specified file */
    public static Bitmap readBitmapFromFile(File file) {
        Bitmap bitmap = null;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),options);
        return bitmap;
    }

    public static Bitmap readBitmapFromCache(Context context,String imageName){
        File cache = context.getCacheDir();
        File image = new File(cache,imageName);
        return readBitmapFromFile(image);
    }

    public static void saveFile(String url,byte[] bytes){
        FileOutputStream outputStream;
        try{
            outputStream=new FileOutputStream(url);
            outputStream.write(bytes);
            outputStream.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static String saveBitmapToCache(Context context, String imageName,Bitmap bitmap){
        File cache = context.getCacheDir();
        File image = new File(cache,imageName);
        FileOutputStream outputStream;
        try{
            outputStream=new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
            outputStream.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return image.getAbsolutePath();
    }

    public static Bitmap getCircularBitmap(Bitmap bitmap)
    {
        Bitmap output;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        } else {
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        float r = 0;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            r = bitmap.getHeight() / 2;
        } else {
            r = bitmap.getWidth() / 2;
        }

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

}
