package com.devjk.voicedictation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.service.autofill.TextValueSanitizer;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView txtDictate;
    private ImageView imgvIcon;
    private TextView txtIcon;

    private boolean isDictating;

    private SpeechRecognizer mRecognizer;
    private Intent intent;

    final int RESULT_SPEECH_RECOGNIZE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* 초기화 */
        txtDictate = findViewById(R.id.MainActivity_TextView_dictate);
        imgvIcon = findViewById(R.id.MainActivity_ImageView_Mic);
        txtIcon = findViewById(R.id.MainActivity_TextView_Mic);
        isDictating = false;

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

    /* 음성인식 중단 */
    private void stopSpeak(){
        mRecognizer.stopListening();
        isDictating = false;

        txtIcon.setText(R.string.touchStart);
        imgvIcon.setImageResource(R.drawable.mic_black);
    }

    /* 음성인식 시작 */
    private void speak(){

        try{
            /* 성공 시 스피치 다이얼로그를 보여준다. */
            intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());

            mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            mRecognizer.setRecognitionListener(listener);
            mRecognizer.startListening(intent);
            isDictating = true;

            txtIcon.setText(R.string.touchFinish);
            imgvIcon.setImageResource(R.drawable.mic_pink);

        }catch (ActivityNotFoundException e){
            /* 실패 - 구글 관련 ACTION_RECOGNIZE_SPEECH 를 설치해주어야 함.*/
            String appPackageName = "com.google.android.googlequicksearchbox";
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }

    }

    private RecognitionListener listener = new RecognitionListener() {
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
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = bundle.getStringArrayList(key);
            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);
            txtDictate.setText(mResult.get(0));
        }

        @Override
        public void onPartialResults(Bundle bundle) {

        }

        @Override
        public void onEvent(int i, Bundle bundle) {

        }
    };

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode){
//            case RESULT_SPEECH_RECOGNIZE:{
//                if(resultCode == RESULT_OK && data != null){
//                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                    txtDictate.setText(result.get(0));
//                }
//            }
//        }
//
//    }
}
