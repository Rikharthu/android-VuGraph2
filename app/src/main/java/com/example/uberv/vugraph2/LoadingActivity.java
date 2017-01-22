package com.example.uberv.vugraph2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.uberv.vugraph2.utils.FileUtils;
import com.example.uberv.vugraph2.utils.PreferencesUtils;

import org.w3c.dom.Text;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoadingActivity extends AppCompatActivity {
    public static final String LOG_TAG=LoadingActivity.class.getSimpleName();

    @BindView(R.id.loading_activity_progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.loading_activity_logo_label)
    TextView logoTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        ButterKnife.bind(this);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.vugraph_preview);
        bitmap = FileUtils.getCircularBitmap(bitmap);
        FileUtils.saveBitmapToCache(this, "test", bitmap);
        Bitmap newBitmap = FileUtils.readBitmapFromCache(this, "test");

        new SetupAsyncTask(this, logoTv).execute();
    }

    private class SetupAsyncTask extends AsyncTask<Void, Integer, Void> {

        private Context context;
        private Activity activity;
        private View sharedElement;

        public SetupAsyncTask(Activity activity, View sharedElement) {
            this.activity = activity;
            this.context = activity;
            this.sharedElement = sharedElement;
        }

        @Override
        protected Void doInBackground(Void... params) {
            // check authentication
            VuGraphUser user = PreferencesUtils.getCurrentUser();
//            if(user!=null && user.isAuthorized()){
////                if(!user.getAvatarUrl().isEmpty()){
//////                    Bitmap bitmap = FileUtils.readBitmapFromFile(new File(user.getAvatarUrl()));
//////                    Log.d(LOG_TAG,bitmap.toString());
////                }
//            }

            for (int i = 0; i <= 25; i++) {
                try {
                    Thread.sleep(15);
                    publishProgress(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = new Intent(context, RegisterActivity.class);
            // TODO add flags
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(activity, sharedElement, "logo");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent, options.toBundle());
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mProgressBar.setProgress(values[0] * 4);
            super.onProgressUpdate(values);
        }
    }


}
