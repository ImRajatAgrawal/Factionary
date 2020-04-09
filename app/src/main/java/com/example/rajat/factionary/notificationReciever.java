package com.example.rajat.factionary;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class notificationReciever extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createanotification(Context context){
        try {
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Intent mainactivityintent = new Intent(context, MainActivity.class);
            mainactivityintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel nc = new NotificationChannel("rajat", "this is a test", importance);
//            nc.setDescription("notification Activity Example");
//            nm.createNotificationChannel(nc);

            PendingIntent pi = PendingIntent.getActivity(context, 100, mainactivityintent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, null).setContentIntent(pi)
                    .setSmallIcon(R.drawable.ic_house).setContentTitle("Fact of the Day!!!")
                    .setContentText("rajat will get job in compass").setAutoCancel(true);
            nm.notify(100, builder.build());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        createanotification(context);

    }


}



