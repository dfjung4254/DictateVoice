package com.devjk.voicedictation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.service.autofill.TextValueSanitizer;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView txtDictate;
    private ImageView imgvIcon;
    private TextView txtIcon;

    private boolean isDictating;

    private SpeechRecognizer mRecognizer;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* 권한 체크 */
        setSpeechPermission();

        /* 초기화 */
        txtDictate = findViewById(R.id.MainActivity_TextView_dictate);
        txtDictate.setText("");
        imgvIcon = findViewById(R.id.MainActivity_ImageView_Mic);
        txtIcon = findViewById(R.id.MainActivity_TextView_Mic);
        isDictating = false;

        /* 음성인식 초기화 */
        initializeSpeechRecognizer();

        /* Onclick Listener */
        imgvIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isDictating){
                    /* 음성인식 재생 아닌 상태 - 시작 */
                    speak();
                }else{
                    /* 음성인식 재생 중인 상태 - 중단 */
                    stopSpeak();
                }

            }
        });
    }

    /* 권한 체크 */
    private void setSpeechPermission() {

        /* 권한 체크부터 하고 권한이 허용되어 있지 않으면 설정으로 넘어간다. */
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            if(!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    == PackageManager.PERMISSION_GRANTED)){

                Toast.makeText(this, "마이크권한을 허용하고 다시 시작하세요", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:"+getPackageName()));
                startActivity(intent);
                finish();
            }

        }


    }

    /* SpeechRecognizer Settings */
    private void initializeSpeechRecognizer() {

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        mRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> results = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if(results != null){
                    txtDictate.setText(results.get(0));
                }


            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
    }

    /* 음성인식 중단 */
    private void stopSpeak(){
        mRecognizer.stopListening();
        isDictating = false;

        txtIcon.setText(R.string.touchStart);
        imgvIcon.setImageResource(R.drawable.mic_black);
    }

    /* 음성인식 시작 */
    private void speak(){

            txtDictate.setText("여기 자막 나옴");
            mRecognizer.startListening(intent);
            isDictating = true;

            txtIcon.setText(R.string.touchFinish);
            imgvIcon.setImageResource(R.drawable.mic_pink);


    }

}
