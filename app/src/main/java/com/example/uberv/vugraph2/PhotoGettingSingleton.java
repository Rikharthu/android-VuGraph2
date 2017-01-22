package com.example.uberv.vugraph2;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;

import java.util.HashMap;
import java.util.Map;

public class PhotoGettingSingleton {
    public static final String LOG_TAG = PhotoGettingSingleton.class.getSimpleName();

    // constants
    public static final String API_KEY = "144692221886299";
    public static final String API_SECRET = "_HYTnj3OyKmlsDnk6cWhqr842JA";
    public static final String CLOUD_NAME = "rikharthu";
    public static final int IMAGE_CACHE_SIZE=10;

    // instances
    private static Cloudinary cloudinary;
    private static PhotoGettingSingleton instance;
    private Context appContext;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;


    public static PhotoGettingSingleton getInstance(Context context) {
        if (instance == null) {
            synchronized (PhotoGettingSingleton.class) {
                if (instance == null) {
                    instance = new PhotoGettingSingleton(context);
                }
            }
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public String getRoundedAvatarURL(String imageName){
        return cloudinary.url().transformation(new Transformation()
                .width(400).height(400).radius("max").crop("crop").chain()
                .width(100)).generate(imageName+".png");
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    private PhotoGettingSingleton(Context context) {
        appContext=context.getApplicationContext();

        // setup cloudinary
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", CLOUD_NAME);
        config.put("api_key", API_KEY);
        config.put("api_secret", API_SECRET);
        cloudinary = new Cloudinary(config);

        // volley
        requestQueue= Volley.newRequestQueue(appContext);
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap>
                    cache = new LruCache<String, Bitmap>(IMAGE_CACHE_SIZE);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    public static Cloudinary getCloudinary() {
        return cloudinary;
    }
}
