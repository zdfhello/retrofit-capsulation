package com.ljd.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.ljd.myapplication.net.ErrorMessageFactory;
import com.ljd.myapplication.net.ResponseSubscriber;
import com.ljd.myapplication.net.UseCase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private UseCase checkUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Map<String,String> map = new HashMap<>();
        map.put("q","retrofit");
        map.put("since","2016-03-29");
        map.put("page","1");
        map.put("per_page","3");

        checkUpdate = new CheckUpdate(map);
        checkUpdate.execute(new CheckUpdateSubscriber());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        checkUpdate.unSubscribe();
    }

    class CheckUpdateSubscriber extends ResponseSubscriber<ResponseBody>{
        @Override
        public void onFailure(Throwable e) {
            Context context = MainActivity.this;
            Toast.makeText(context,
                    ErrorMessageFactory.create(context,(Exception)e),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSuccess(ResponseBody responseBody) {
            try {
                Log.d(TAG,responseBody.string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
