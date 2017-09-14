package br.com.hrick.estoquepessoal.view;

import android.app.Application;

import com.facebook.FacebookSdk;

/**
 * Created by Meg on 13/09/2017.
 */

public class EstoquePessoalApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
