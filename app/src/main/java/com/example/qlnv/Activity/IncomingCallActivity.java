package com.example.qlnv.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.qlnv.R;
import com.stringee.call.StringeeCall;
import com.stringee.common.StringeeAudioManager;
import com.stringee.common.StringeeConstant;
import com.stringee.listener.StatusListener;

import org.json.JSONObject;

import java.util.Set;

public class IncomingCallActivity extends AppCompatActivity {
    private StringeeCall stringeeCall;
    private StringeeCall.SignalingState state;
    private StringeeAudioManager audioManager;
    private FrameLayout vLocal;
    private FrameLayout vRemote;

    private Button btnCancel;
    private Button btnAnswer;
    private Button btnReject;

    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_call);

        initView();

        //init callListener
        initAnswer();
    }

    private void initAnswer() {
        String callId = getIntent().getStringExtra("call_id");
        stringeeCall = ChattingActivity.callMap.get(callId);

        // true if call is videoCall, false if call is not videoCall
        stringeeCall.enableVideo(true);

        //set quality of video : QUALITY_NORMAL, QUALITY_HD, QUALITY_FULLHD
        stringeeCall.setQuality(StringeeConstant.QUALITY_FULLHD);
        audioManager = StringeeAudioManager.create(this);
        audioManager.start(new StringeeAudioManager.AudioManagerEvents() {
            @Override
            public void onAudioDeviceChanged(StringeeAudioManager.AudioDevice selectedAudioDevice, Set<StringeeAudioManager.AudioDevice> availableAudioDevices) {
            }
        });
        stringeeCall.ringing(new StatusListener() {
            @Override
            public void onSuccess() {
                stringeeCall.setCallListener(new StringeeCall.StringeeCallListener() {
                    @Override
                    public void onSignalingStateChange(StringeeCall stringeeCall, StringeeCall.SignalingState signalingState, String s, int i, String s1) {
                        //get signalingState: CALLING, RINGING, ANSWERED, BUSY, ENDED
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                state = signalingState;
                                tvStatus.setText(state.toString());
                                if (state == StringeeCall.SignalingState.ENDED) {
                                    finish();
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(StringeeCall stringeeCall, int i, String s) {

                    }

                    @Override
                    public void onHandledOnAnotherDevice(StringeeCall stringeeCall, StringeeCall.SignalingState signalingState, String s) {
                        //do somethings when your call was handle on another device
                    }

                    @Override
                    public void onMediaStateChange(StringeeCall stringeeCall, StringeeCall.MediaState mediaState) {
                        //get mediaState: CONNECTED, DISCONNECTED
                    }

                    @Override
                    public void onLocalStream(StringeeCall stringeeCall) {
                        //get your localStream
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                vLocal.addView(stringeeCall.getLocalView());
                                stringeeCall.renderLocalView(true);
                            }
                        });
                    }

                    @Override
                    public void onRemoteStream(StringeeCall stringeeCall) {
                        //get your remoteStream
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                vRemote.addView(stringeeCall.getRemoteView());
                                stringeeCall.renderRemoteView(false);
                            }
                        });
                    }

                    @Override
                    public void onCallInfo(StringeeCall stringeeCall, JSONObject jsonObject) {

                    }
                });

            }
        });

    }

    private void initView() {
        vLocal = findViewById(R.id.v_local);
        vRemote = findViewById(R.id.v_remote);

        tvStatus = findViewById(R.id.tv_status);

        btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stringeeCall != null) {
                    stringeeCall.hangup();
                    finish();
                }
            }
        });

        btnAnswer = findViewById(R.id.btn_answer);
        btnAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stringeeCall != null) {
                    stringeeCall.answer();
                    btnAnswer.setVisibility(View.GONE);
                    btnReject.setVisibility(View.GONE);
                    btnCancel.setVisibility(View.VISIBLE);
                }
            }
        });

        btnReject = findViewById(R.id.btn_reject);
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stringeeCall != null) {
                    stringeeCall.reject();
                    btnAnswer.setVisibility(View.GONE);
                    btnReject.setVisibility(View.GONE);
                    btnCancel.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}