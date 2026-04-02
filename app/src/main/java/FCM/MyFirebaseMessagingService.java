package FCM;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.grocery.QTPmart.MainActivity;
import com.grocery.QTPmart.R;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    String value, type;
    int count = 0;
    NotificationManager notificationManager;
    String placeImage, placeId, placeTitle, placeMessage;
    String random_id;
    //String TYPE;
    public static int NOTIFICATION_ID = 0;
    String offercode;


    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        if (!token.isEmpty()) {
            Log.e("NEW_TOKEN",token);

        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a data payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
        }
        else if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            Map<String, String> data = remoteMessage.getData();
            handleDataMessage(remoteMessage);
        }

    }

    private void handleNotification(String title,String message) {
        Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
        resultIntent.putExtra("id", "");
        // check for image attachment
//        sendNotification("", message, "", resultIntent);

        int requestID = (int) System.currentTimeMillis();
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(requestID, getNotification(requestID, title,message, resultIntent));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void handleDataMessage(RemoteMessage remoteMessage) {
// if (remoteMessage.getData().get("type").equals("productOffer")){
// if (remoteMessage.getData().get("type").equals("productOffer")){
     placeImage = remoteMessage.getData().get("image");
     placeTitle = remoteMessage.getData().get("title");
     placeMessage = remoteMessage.getData().get("message");
     placeId = remoteMessage.getData().get("productCode");
     random_id = remoteMessage.getData().get("random_id");
     type = remoteMessage.getData().get("type");


     //Log.d("placeId", placeId);
     if (NOTIFICATION_ID != Integer.parseInt(random_id)) {
         NOTIFICATION_ID = Integer.parseInt(random_id);
         handleMessage(getApplicationContext());
     }

//     }}
// else if(remoteMessage.getData().get("type").equals("Offer")){
//     placeImage = remoteMessage.getData().get("image");
//     placeTitle = remoteMessage.getData().get("title");
//     placeMessage = remoteMessage.getData().get("message");
//     random_id = remoteMessage.getData().get("random_id");
//     type = remoteMessage.getData().get("type");
//     offercode = remoteMessage.getData().get("productCode");
//
//     //Log.d("placeId", placeId);
//     if (NOTIFICATION_ID != Integer.parseInt(random_id)) {
//         NOTIFICATION_ID= Integer.parseInt(random_id);
//         handleMessage(getApplicationContext());
//
//     }
// }
// else if(remoteMessage.getData().get("type").equals("order")) {
//
//     placeTitle = remoteMessage.getData().get("title");
//     placeMessage = remoteMessage.getData().get("message");
//     placeId = remoteMessage.getData().get("order_id");
//     random_id = remoteMessage.getData().get("random_id");
//     type = remoteMessage.getData().get("type");
//
//
//     if (NOTIFICATION_ID != Integer.parseInt(random_id)) {
//         NOTIFICATION_ID = Integer.parseInt(random_id);
//         handleMessage(getApplicationContext());
//
//
//     }
// }
    }

    private Notification getNotification(int requestID, String title, String message, Intent resultIntent) {


        NotificationCompat.Builder mNotifyBuilder = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            createNotificationChannel();
            mNotifyBuilder = new NotificationCompat.Builder(this, "1");

        } else {

            mNotifyBuilder = new NotificationCompat.Builder(this);
        }

        PendingIntent intent = PendingIntent.getActivity(getApplicationContext(), requestID, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotifyBuilder.setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(1)
                .setSmallIcon(R.drawable.small_logo);

        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        mNotifyBuilder.setDefaults(defaults);
        mNotifyBuilder.setContentIntent(intent);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {

            mNotifyBuilder.setColor(getResources().getColor(R.color.red));
        }

        return mNotifyBuilder.build();
    }

    private void createNotificationChannel() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "order";
            String description = "order";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("deprecation")
    private void handleMessage(Context mContext) {
        Bitmap remote_picture = null;
        int icon = R.drawable.small_logo;
        //if message and image url
//        if (type.equals("productOffer")) {
//            if (placeMessage != null && placeImage != null ) {
                try {
                    Log.v("TAG_MESSAGE", "" + placeMessage);
                    Log.v("TAG_IMAGE", "" + placeImage);
//                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                notificationManager.cancel(NOTIFICATION_ID);

                    NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
                    notiStyle.setSummaryText(placeMessage);

                    try {
                        remote_picture = BitmapFactory.decodeStream((InputStream) new URL(placeImage).getContent());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    notiStyle.bigPicture(remote_picture);
                    notificationManager = (NotificationManager) mContext
                            .getSystemService(Context.NOTIFICATION_SERVICE);
                    PendingIntent contentIntent = null;

                    Intent gotoIntent = new Intent();
                    gotoIntent.putExtra("type", "productOffer");
                    gotoIntent.putExtra("productCode", placeId);
                    gotoIntent.setClassName(mContext, getApplicationContext().getPackageName()+".Activitys.SplashScreenActivity");//Start activity when user taps on notification.
                    contentIntent = PendingIntent.getActivity(mContext,
                            (int) (Math.random() * 100), gotoIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                            mContext);
                    Notification notification = mBuilder.setSmallIcon(icon).setTicker(placeTitle).setWhen(0)
                            .setLargeIcon(((BitmapDrawable) getResources().getDrawable(R.drawable.small_logo)).getBitmap())
                            .setAutoCancel(true)
                            .setContentTitle(placeTitle)
                            .setPriority(1)
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(placeMessage))
                            .setContentIntent(contentIntent)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

                            .setContentText(placeMessage)
                            .setStyle(notiStyle).build();


                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                    count++;
                    notificationManager.notify(count, notification);//This will generate seperate notification each time server sends.

                } catch (Throwable e) {
                    e.printStackTrace();
                }
//            }


//        }
//        else if(type.equals("Offer"))
//        {
//            if (placeMessage != null && !TextUtils.isEmpty(placeImage)) {
//                try {
//                    Log.v("TAG_MESSAGE", "" + placeMessage);
//                    Log.v("TAG_IMAGE", "" + placeImage);
////                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
////                notificationManager.cancel(NOTIFICATION_ID);
//
//                    NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
//                    notiStyle.setSummaryText(placeMessage);
//
//                    try {
//                        remote_picture = BitmapFactory.decodeStream((InputStream) new URL(placeImage).getContent());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    notiStyle.bigPicture(remote_picture);
//                    notificationManager = (NotificationManager) mContext
//                            .getSystemService(Context.NOTIFICATION_SERVICE);
//                    PendingIntent contentIntent = null;
//
//                    Intent gotoIntent = new Intent();
//                    gotoIntent.putExtra("type", "Offer");
//                    gotoIntent.putExtra("product_id", offercode);
//                    gotoIntent.setClassName(mContext, getApplicationContext().getPackageName()+".Activitys.SplashScreenActivity");//Start activity when user taps on notification.
//                    contentIntent = PendingIntent.getActivity(mContext,
//                            (int) (Math.random() * 100), gotoIntent,
//                            PendingIntent.FLAG_UPDATE_CURRENT);
//                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
//                            mContext);
//                    Notification notification = mBuilder.setSmallIcon(icon).setTicker(placeTitle).setWhen(0)
//                            .setLargeIcon(((BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher_round)).getBitmap())
//                            .setAutoCancel(true)
//                            .setContentTitle(placeTitle)
//                            .setPriority(1)
//                            .setContentText(placeMessage)
//                            .setContentIntent(contentIntent)
//                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//
//                            .setStyle(notiStyle).build();
//
//
//                    notification.flags = Notification.FLAG_AUTO_CANCEL;
//                    count++;
//                    notificationManager.notify(count, notification);//This will generate seperate notification each time server sends.
//
//                } catch (Throwable e) {
//                    e.printStackTrace();
//                }
//            }
//            else{
//                    notificationManager = (NotificationManager) mContext
//                            .getSystemService(Context.NOTIFICATION_SERVICE);
//                    PendingIntent contentIntent = null;
//
//                    Intent gotoIntent = new Intent();
//                    gotoIntent.putExtra("type", "Offer");
//                    gotoIntent.putExtra("product_id", offercode);
//                    gotoIntent.setClassName(mContext, getApplicationContext().getPackageName()+".Activitys.SplashScreenActivity");//Start activity when user taps on notification.
//                    contentIntent = PendingIntent.getActivity(mContext,
//                            (int) (Math.random() * 100), gotoIntent,
//                            PendingIntent.FLAG_UPDATE_CURRENT);
//                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
//                            mContext);
//                    Notification notification = mBuilder.setSmallIcon(icon).setTicker(placeTitle).setWhen(0)
//                            .setLargeIcon(((BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher_round)).getBitmap())
//                            .setAutoCancel(true)
//                            .setContentTitle(placeTitle)
//                            .setPriority(1)
//                            .setContentText(placeMessage)
//                            .setContentIntent(contentIntent)
//                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)).build();
//
//                    notification.flags = Notification.FLAG_AUTO_CANCEL;
//                    count++;
//                    notificationManager.notify(count, notification);//This will generate seperate notification each time server send
//                }
//        }
//
//        else if(type.equals("order"))
//        {
//            Bitmap icons = BitmapFactory.decodeResource(getResources(),
//                    R.mipmap.ic_launcher_round);
//
//            notificationManager = (NotificationManager) mContext
//                    .getSystemService(Context.NOTIFICATION_SERVICE);
//            PendingIntent contentIntent = null;
//
//            Intent gotoIntent = new Intent();
//            gotoIntent.putExtra("type", "order");
//            gotoIntent.setClassName(mContext, getApplicationContext().getPackageName()+".Activitys.SplashScreenActivity");//Start activity when user taps on notification.
//            contentIntent = PendingIntent.getActivity(mContext,
//                    (int) (Math.random() * 100), gotoIntent,
//                    PendingIntent.FLAG_UPDATE_CURRENT);
//            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
//                    mContext);
//            Notification notification = mBuilder.setSmallIcon(icon).setTicker(placeTitle).setWhen(0)
//                    .setLargeIcon(icons)
//                    .setAutoCancel(true)
//                    .setContentTitle(placeTitle)
//                    .setPriority(1)
//                    .setContentText(placeMessage)
//                    .setContentIntent(contentIntent)
//                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)).build();
//
//            notification.flags = Notification.FLAG_AUTO_CANCEL;
//            count++;
//            notificationManager.notify(count, notification);
//        }
    }
}