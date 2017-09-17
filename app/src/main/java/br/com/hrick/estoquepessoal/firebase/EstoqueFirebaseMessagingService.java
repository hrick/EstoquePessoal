package br.com.hrick.estoquepessoal.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;

import br.com.hrick.estoquepessoal.R;
import br.com.hrick.estoquepessoal.view.main.MainActivity;

/**
 * Created by Meg on 16/09/2017.
 */

public class EstoqueFirebaseMessagingService extends FirebaseMessagingService {

    public static final int ID_NOTIFICATION = 123;

    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);
        String title =  intent.getStringExtra("gcm.notification.title");
        String body =  intent.getStringExtra("gcm.notification.body");


        Intent toMain = new Intent(this, MainActivity.class);

        PendingIntent activity = PendingIntent.getActivity(this, 0, toMain, 0);

        Notification build = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(activity)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID_NOTIFICATION, build);
    }
}
