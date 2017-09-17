package br.com.hrick.estoquepessoal.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Meg on 16/09/2017.
 */

public class EstoqueFirebaseInstanceIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        FirebaseInstanceId firebaseInstanceId = FirebaseInstanceId.getInstance();
        String refreshedToken = firebaseInstanceId.getToken();
        Log.d("FirebaseIdService","Token Message: " + refreshedToken);
    }
}
