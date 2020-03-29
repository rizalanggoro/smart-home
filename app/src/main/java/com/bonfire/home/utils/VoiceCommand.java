package com.bonfire.home.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;

public class VoiceCommand {
  private Activity activity;

  public VoiceCommand(Activity activity) {
    if (activity != null)
      this.activity = activity;
  }

  public interface voiceCommandListener {
    void onResult(ArrayList<String> results);
  }
  private voiceCommandListener listener;
  public VoiceCommand setListener(voiceCommandListener listener) {
    if (listener != null)
      this.listener = listener;
    return this;
  }

  public void startListening() {
    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    intent
        .putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        .putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        .putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10)
        .putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.activity.getPackageName());

    VoiceRecognitionListener voiceRecognitionListener = new VoiceRecognitionListener();
    SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this.activity);
    speechRecognizer.setRecognitionListener(voiceRecognitionListener);
    speechRecognizer.startListening(intent);
  }

  public class VoiceRecognitionListener implements RecognitionListener {
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
      if (listener != null)
        listener.onResult(null);
      Log.e("error", "error " + i);
    }

    @Override
    public void onResults(Bundle bundle) {
      ArrayList<String> _a = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
      if (_a != null && listener != null) {
        listener.onResult(_a);
      }
    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }
  }
}
