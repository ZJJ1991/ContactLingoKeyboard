package com.example.jzhou.contactlingokeyboard;

import android.content.SharedPreferences;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

public class MyKeyboard extends InputMethodService implements KeyboardView.OnKeyboardActionListener {


    private  KeyboardView kv;
    private Keyboard keyboard;
    private SharedPreferences keyboardtypePreference;

    private boolean caps = false;

    @Override
    public View onCreateInputView() {
        kv = (KeyboardView)  getLayoutInflater().inflate(R.layout.keyboard, null);

        kv.setOnKeyboardActionListener(this);
        return kv;
    }

    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);
        keyboardtypePreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String keyboardPreference = keyboardtypePreference.getString("keyboardType", "2");
        switch (keyboardPreference) {
            case "1":
                keyboard = new Keyboard(this, R.xml.classic);
                break;
            case "2":
                keyboard = new Keyboard(this, R.xml.crazy);
                Log.d("keyboardtype", "crazy keyboard");
                break;
            case "3":
                keyboard = new Keyboard(this, R.xml.crazy);
                break;
        }
        kv.setKeyboard(keyboard);
        kv.invalidateAllKeys();
        kv.setPreviewEnabled(false);
        kv.closing();
    }

    private  void playClick(int keyCode){
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        switch (keyCode){
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
        playClick(primaryCode);
        switch (primaryCode){
            case Keyboard.KEYCODE_DELETE:
                ic.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_SHIFT:
                caps = !caps;
                keyboard.setShifted(caps);
                kv.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            default:
                char code = (char) primaryCode;
                if (Character.isLetter(code) && caps){
                    code = Character.toUpperCase(code);
                    ic.commitText(String.valueOf(code), 1);
                }
        }
    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
}
