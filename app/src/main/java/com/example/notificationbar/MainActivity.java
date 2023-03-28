package com.example.notificationbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.BitSet;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.buttonShowNotification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNotificationWithImage();

            }
        });
    }
    @SuppressLint("StaticFieldLeak")
    private void showNotificationWithImage(){

        new AsyncTask<String,Void, Bitmap>(){

            @SuppressLint("StaticFieldLeak")
            @Override
            protected Bitmap doInBackground(String... strings) {
                InputStream inputStream;
                try{
                    URL url = new URL(strings[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    inputStream = connection.getInputStream();
                    return BitmapFactory.decodeStream(inputStream);

                } catch (Exception ignored){

                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                showNotification(bitmap);
            }
        }.execute("http://infinityandroid.com/images/notification_image_4.jpg"); // url of an image
    }
    private void showNotification(Bitmap bitmap) {

        int notificationID = new Random().nextInt(100);
        String channelID = "notification_channel_2";

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext(), channelID
        );
        builder.setSmallIcon(R.drawable.ic_baseline_notifications);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentTitle("What is Virat");
        builder.setContentText("Indian international cricketer and former captain of the Indian national team who plays as a right-handed batsman for Royal Challengers Bangalore in the IPL and for Delhi in Indian domestic cricket.");
        builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap));
      //  builder.setStyle(new NotificationCompat.BigTextStyle().bigText("Indian international cricketer and former captain of the Indian national team who plays as a right-handed batsman for Royal Challengers Bangalore in the IPL and for Delhi in Indian domestic cricket."));
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            if(notificationManager!=null && notificationManager.getNotificationChannel(channelID)==null){
                NotificationChannel notificationChannel = new NotificationChannel(
                        channelID,
                        "Notification Channel 1",
                        NotificationManager.IMPORTANCE_HIGH
                );
                notificationChannel.setDescription("This notification channel is used to notify user");
                notificationChannel.enableVibration(true);
                notificationChannel.enableLights(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        Notification notification = builder.build();
        if(notificationManager!=null){
            notificationManager.notify(notificationID, notification);
        }

    }
}