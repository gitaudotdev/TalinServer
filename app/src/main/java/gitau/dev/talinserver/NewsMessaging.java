package gitau.dev.talinserver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

import gitau.dev.talinserver.Common.Common;
import gitau.dev.talinserver.Remote.APIService;
import info.hoang8f.widget.FButton;

public class NewsMessaging extends AppCompatActivity {

    MaterialEditText edtMessage,edtTitle;
    FButton btnSend;

    APIService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_messaging);

        mService = Common.getFCMClient();

        edtMessage = findViewById(R.id.edtMessage);
        edtTitle = findViewById(R.id.edtTitle);

        btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create Message
                Map<String,String> dataSend = new HashMap<>();
                dataSend.put("title",edtTitle.getText().toString());
                dataSend.put("message",edtMessage.getText().toString());
//                dataSend.put("message","Your Order"+key+"Was Updated");
//                DataMessage dataMessage = new DataMessage(token.getToken(),dataSend);

//                Notification notification = new Notification(edtTitle.getText().toString(),edtMessage.getText().toString());

//                Sender toTopic  = new Sender();
//                toTopic.to = new StringBuilder("/topics").append(Common.topicName).toString();
//                toTopic.notification = notification;

//                mService.sendNotification(dataSend)
//                        .enqueue(new Callback<MyResponse>() {
//                            @Override
//                            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
//                                if (response.isSuccessful())
//                                    Toast.makeText(NewsMessaging.this, "Message Sent", Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onFailure(Call<MyResponse> call, Throwable t) {
//                                Toast.makeText(NewsMessaging.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
            }
        });
    }
}
