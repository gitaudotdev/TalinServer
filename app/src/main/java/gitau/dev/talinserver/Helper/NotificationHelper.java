package gitau.dev.talinserver.Helper;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import gitau.dev.talinserver.R;

public class NotificationHelper extends ContextWrapper {
    private static final String CODEBENDER_CHANEL_ID = "gitau.dev.talinserver.codeKid";
    private static final String CODEBENDER_CHANEL_NAME = "Talin Server";

    private NotificationManager manager;


    public NotificationHelper(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) //Only works if Android API is 26 or Higher
            createChannel();
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {

        NotificationChannel codekidChannel = new NotificationChannel(CODEBENDER_CHANEL_ID,
                CODEBENDER_CHANEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT);
        codekidChannel.enableLights(false);
        codekidChannel.enableVibration(true);
        codekidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(codekidChannel);


    }

    public NotificationManager getManager() {
        if(manager == null)
            manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        return  manager;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getTalinNotification(String title, String body, PendingIntent intent, Uri soundUri)
    {
        return new Notification.Builder(getApplicationContext(),CODEBENDER_CHANEL_ID)
                .setContentIntent(intent)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_local_shipping_black_24dp)
                .setSound(soundUri)
                .setAutoCancel(false);
    }

    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getTalinNotification(String title, String body, Uri soundUri)
    {
        return new Notification.Builder(getApplicationContext(),CODEBENDER_CHANEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_local_shipping_black_24dp)
                .setSound(soundUri)
                .setAutoCancel(false);
    }
}
