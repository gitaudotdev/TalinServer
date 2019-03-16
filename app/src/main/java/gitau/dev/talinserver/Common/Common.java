package gitau.dev.talinserver.Common;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Locale;

import gitau.dev.talinserver.Models.Request;
import gitau.dev.talinserver.Models.User;
import gitau.dev.talinserver.Remote.APIService;
import gitau.dev.talinserver.Remote.FCMRetrofitClient;
import gitau.dev.talinserver.Remote.IGeoCoordinates;
import gitau.dev.talinserver.Remote.RetrofitClient;

public class Common {
    public static final String SHIPPERS_TABLE = "Shippers";
    public static final String ORDERS_TO_BE_SHIPPED_TABLE = "OrdersToBeShipped";

    public  static User currentUser;
    public static Request currentRequest;

    public static  String topicName = "News";

    public  static final String UPDATE = "Update";
    public static final String DELETE = "Delete";

    public static final String PHONE_TEXT = "userPhone";

    public static final int PICK_IMAGE_REQUEST = 100;

    public static final String Base_Url = "https://maps.googleapis.com/";
    public static final String fcm_Url = "https://fcm.googleapis.com/";

    public static String convertCodeToStatus(String code) {
        if(code.equals("0"))
            return "Placed";
        else if (code.equals("1"))
            return "On my Way";
        else if (code.equals("2"))
            return "Shipping";
        else
            return "Shipped";
    }

    public static IGeoCoordinates getGeoCodeService(){
        return RetrofitClient.getClient(Base_Url).create(IGeoCoordinates.class);
    }

    public static APIService getFCMClient(){
        return FCMRetrofitClient.getRetrofit(fcm_Url).create(APIService.class);
    }

    public static Bitmap scaleBitmap (Bitmap bitmap , int newWidth,int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth,newHeight,Bitmap.Config.ARGB_8888);

        float scaleX = newWidth/(float)bitmap.getWidth();
        float scaleY = newHeight/(float)bitmap.getHeight();
        float pivotX=0,pivotY=0;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX,scaleY,pivotX,pivotY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap,0,0,new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }

    public static String getDate(long time){
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        StringBuilder date = new StringBuilder(DateFormat.format("dd:MM:yyyy HH:mm",calendar).toString());
        return date.toString();
    }
}
