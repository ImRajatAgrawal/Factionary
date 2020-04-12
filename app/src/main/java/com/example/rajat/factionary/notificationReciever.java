package com.example.rajat.factionary;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

class getfactfromapi extends AsyncTask<String,Void,String>{

    @Override
    protected String doInBackground(String... strings) {
       try {
            String res="";
            URL url=new URL(strings[0]);
           HttpURLConnection cn= (HttpURLConnection) url.openConnection();
           cn.connect();
           InputStream is=cn.getInputStream();
           int data;

           data=is.read();
           while(data!=-1){
               res+=(char)data;
               data=is.read();
           }
           is.close();;
           cn.disconnect();
           return  res;
       }catch (Exception e) {
            e.printStackTrace();
               return null;

       }

    }
}
public class notificationReciever extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createanotification(Context context){
        try {
            getfactfromapi tsk=new getfactfromapi();
            String[] randomfactcategories={"trivia","date","year","math"};
            int category= new Random().nextInt(randomfactcategories.length);
            String result=tsk.execute("http://numbersapi.com/random/"+randomfactcategories[category]).get();
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
                    .setContentText(result).setAutoCancel(true);
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



