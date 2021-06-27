package com.example.qlnv.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qlnv.Activity.Adapter.MessageListAdapter;
import com.example.qlnv.Activity.model.UserMessage;
import com.example.qlnv.R;
import com.example.qlnv.utils.Utils;
import com.stringee.StringeeClient;
import com.stringee.call.StringeeCall;
import com.stringee.call.StringeeCall2;
import com.stringee.exception.StringeeError;
import com.stringee.listener.StringeeConnectionListener;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ChattingActivity extends AppCompatActivity implements MqttCallback {
    private RecyclerView mMessageRecycler;
    MessageListAdapter mMessageAdapter;
    ArrayList<UserMessage> messageList = new ArrayList<>();
    LinearLayout chatbox;
    TextView chat_fr_name;
    Intent i;
    String  TOPIC;
    //=======stringee video call api ======================
    private String token = Utils.TOKEN_STRINGEE_1104;
    public static StringeeClient stringeeClient;
    public static Map<String, StringeeCall> callMap = new HashMap<>();
    ImageView calling;
    //=================================
    private static final String BROKER_URI = "tcp://broker.hivemq.com:1883";
    String mainTOPIC = "testtopic/";
    private static final int QOS = 2;
    // user name for the chat
    private static final String USER_NAME = Build.DEVICE;
    String name;
    EditText chattext;

    // global types
    private MqttAndroidClient client;
    private EditText textMessage;
    private String message;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chatting);
        chatbox=findViewById(R.id.layout_chatbox);
        chattext=findViewById(R.id.edittext_chatbox);
        textMessage = (EditText) findViewById(R.id.edittext_chatbox);
        calling = findViewById(R.id.call_btn);

        //=======RecycleView=====================================================
        mMessageRecycler = (RecyclerView) findViewById(R.id.messenger_view);
        chat_fr_name = findViewById(R.id.chat_friend_name);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mMessageRecycler.setLayoutManager(manager);
        mMessageRecycler.setHasFixedSize(true);
        mMessageAdapter = new MessageListAdapter(this, messageList);
        mMessageRecycler.setAdapter(mMessageAdapter);

        //=========================================================================


        //=================chia luồng chat giữa các nhân viên========================================================
        i = getIntent();
        chat_fr_name.setText(i.getStringExtra("nameUser"));
        int idAccEmpl = Integer.parseInt(i.getStringExtra("idAccEmpl"));
        int accid = Integer.parseInt(i.getStringExtra("accid"));
        if (accid>idAccEmpl){
            TOPIC = mainTOPIC + accid + i.getStringExtra("idAccEmpl");
        } else {
            TOPIC = mainTOPIC + i.getStringExtra("idAccEmpl")+ accid ;
        }
        Log.v("TOPIC", ""+TOPIC);
        //===========================================================================================================


        //======================setAction call button=======================================================
        initStringee();

        requirePermission();

        calling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChattingActivity.this, OutgoingCallActivity.class);
                intent.putExtra("from", stringeeClient.getUserId());
                intent.putExtra("to", i.getStringExtra("idAccEmpl"));
                startActivity(intent);
            }
        });


        //===========================================================================================================
        // when the activity is created call to connect to the broker
        connect();
        this.mHandler = new Handler();
        m_Runnable.run();

    }
    private final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {
            ChattingActivity.this.mHandler.postDelayed(m_Runnable,5000);
        }

    };
    private void connect(){
        // create a new MqttAndroidClient using the current context
        client = new MqttAndroidClient(this, BROKER_URI, USER_NAME);
        client.setCallback(this); // set this as callback to listen for messages

        try{
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true); // clean session in order to don't get duplicate messages each time we connect

            client.connect(options, new IMqttActionListener() {

                @Override
                public void onSuccess(IMqttToken iMqttToken) {
                    Toast.makeText(ChattingActivity.this, "Ready for chat", Toast.LENGTH_SHORT).show();
                    // once connected call to subscribe to receive messages
                    subscribe();
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                    Toast.makeText(ChattingActivity.this, "Unavailable chat, cause: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                }

            });
        } catch (MqttException e){
            Toast.makeText(this, "ERROR, client not connected to broker in " + BROKER_URI, Toast.LENGTH_LONG).show();
        }
    }

    public void publish(View view) {
        // we are in the right view?
        if (view.getId() == R.id.button_chatbox_send) {
            // we only publish if connected
            if (null != client && client.isConnected()) {

                message = textMessage.getText().toString();
                // we only publish if there is message to publish
                if (!message.isEmpty()) {
                    name = USER_NAME;
                    message = name+" " + message.toString();
                    textMessage.setText("");
                    MqttMessage mqttMessage = new MqttMessage(message.getBytes());
                    mqttMessage.setQos(QOS);
                    try {
                        client.publish(TOPIC, mqttMessage, null, new IMqttActionListener() {

                            @Override
                            public void onSuccess(IMqttToken iMqttToken) {
                                //Toast.makeText(ChattingActivity.this, "message sent", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                                Toast.makeText(ChattingActivity.this, "Failed on publish, cause: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                            }

                        });
                    } catch (MqttException e) {
                        Toast.makeText(this, " ERROR, an error occurs when publishing", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Toast.makeText(this, "WARNING, client not connected", Toast.LENGTH_LONG).show();
            }

        }
    }

    private void subscribe() {
        try {
            client.subscribe(TOPIC, QOS, null, new IMqttActionListener() {

                @Override
                public void onSuccess(IMqttToken iMqttToken) {
                    //Toast.makeText(MainActivity.this, "Subscribed to:" + TOPIC, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                    Toast.makeText(ChattingActivity.this, "Failed on subscribe, cause: " + throwable, Toast.LENGTH_LONG).show();
                }

            });

        } catch (MqttException e) {
            Toast.makeText(this, "ERROR, an error occurs when subscribing", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        Toast.makeText(this, "Connection lost!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        if (message.toString().contains(Build.DEVICE)) {
            String message1 = message.toString().substring(message.toString().indexOf(" "));
            messageList.add(new UserMessage(name, message1, currentTime));
        } else {
            String message1 = message.toString().substring(message.toString().indexOf(" "));
            messageList.add(new UserMessage("", message1, currentTime));
        }
        mMessageAdapter.notifyDataSetChanged();
        sendOnChannel1(i.getStringExtra("nameUser"),message.toString().substring(message.toString().indexOf(" ")));
    }
    //==========================================send notification================================================================
    private void sendOnChannel1(String title, String message)  {
        Notification notification = new NotificationCompat.Builder(this, NotificationApp.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_baseline_chat_24)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        Log.v("push","noti");
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        int notificationId = 1;
        notificationManager.notify(notificationId, notification);
    }

    private void requirePermission() {
        ActivityCompat.requestPermissions(ChattingActivity.this, new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
        }, 1);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(ChattingActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }
    private void initStringee() {
        stringeeClient = new StringeeClient(this);
        stringeeClient.setConnectionListener(new StringeeConnectionListener() {
            @Override
            public void onConnectionConnected(StringeeClient stringeeClient, boolean b) {
                // do something when connected to Stringee server
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "connected", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onConnectionDisconnected(StringeeClient stringeeClient, boolean b) {
                // do something when disconnected to Stringee server
            }

            @Override
            public void onIncomingCall(StringeeCall stringeeCall) {
                // do something when get incoming call
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callMap.put(stringeeCall.getCallId(), stringeeCall);
                        Intent intent = new Intent(ChattingActivity.this, IncomingCallActivity.class);
                        intent.putExtra("call_id", stringeeCall.getCallId());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onIncomingCall2(StringeeCall2 stringeeCall2) {

            }


            @Override
            public void onConnectionError(StringeeClient stringeeClient, StringeeError stringeeError) {
                // do something when connect to Stringee server in error
            }

            @Override
            public void onRequestNewToken(StringeeClient stringeeClient) {
                // do something when your token is out of date
            }

            @Override
            public void onCustomMessage(String s, JSONObject jsonObject) {

            }

            @Override
            public void onTopicMessage(String s, JSONObject jsonObject) {

            }
        });

        stringeeClient.connect(token);
    }
}
