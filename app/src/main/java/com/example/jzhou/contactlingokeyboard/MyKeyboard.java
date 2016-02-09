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
import android.view.inputmethod.InputMethodSubtype;

public class MyKeyboard extends InputMethodService implements KeyboardView.OnKeyboardActionListener {


    private  KeyboardView kv;
    private Keyboard keyboard;
    private Keyboard symbolsKeyboard;
    private SharedPreferences keyboardtypePreference;
    private boolean caps = false;
    public Contacts contacts;
    public  double packagename;

    @Override
    public View onCreateInputView() {
        contacts = new Contacts();
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
                keyboard = new Keyboard(this, R.xml.converse);
                break;
            case "4":
                keyboard = new Keyboard(this, R.xml.numeric);
                break;
            case "5":
                keyboard = new Keyboard(this, R.xml.finnish);
                symbolsKeyboard = new Keyboard(this, R.xml.symbols);
                break;
            case "6":
                keyboard = new Keyboard(this, R.xml.english_divide);
                symbolsKeyboard = new Keyboard(this, R.xml.symbol_divide);
                break;
            case "7":
                keyboard = new Keyboard(this, R.xml.multi);
                symbolsKeyboard = new Keyboard(this, R.xml.symbol_multi);
                break;
            case "8":
                keyboard = new Keyboard(this, R.xml.english);
                symbolsKeyboard = new Keyboard(this, R.xml.symbols);
                break;
        }
        kv.setKeyboard(keyboard);
        kv.setPreviewEnabled(false);
        kv.closing();
        packagename = contacts.getpackageName();
        Log.d("111", packagename + "packagename");

    }

    @Override
    protected void onCurrentInputMethodSubtypeChanged(InputMethodSubtype newSubtype) {
        super.onCurrentInputMethodSubtypeChanged(newSubtype);

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
 	    case 900:
                if(kv.getKeyboard()== keyboard) {
                    kv.setKeyboard(symbolsKeyboard);
                    kv.setPreviewEnabled(false);
                    kv.closing();
                }
                else{
                    kv.setKeyboard(keyboard);
                    kv.setPreviewEnabled(false);
                    kv.closing();
                }
                break;
            case 901:
                if(kv.getKeyboard()== keyboard) {
                    kv.setKeyboard(symbolsKeyboard);
                    kv.setPreviewEnabled(false);
                    kv.closing();
                }
                else{
                    kv.setKeyboard(keyboard);
                    kv.setPreviewEnabled(false);
                    kv.closing();
                }
                break;
            case 902:
                if(kv.getKeyboard()== keyboard) {
                    kv.setKeyboard(symbolsKeyboard);
                    kv.setPreviewEnabled(false);
                    kv.closing();
                }
                else{
                    kv.setKeyboard(keyboard);
                    kv.setPreviewEnabled(false);
                    kv.closing();
                }
                break;
            default:
                char code = (char) primaryCode;
                if (Character.isLetter(code) && caps){
                    code = Character.toUpperCase(code);
                    ic.commitText(String.valueOf(code), 1);
                }
                else
                    ic.commitText(String.valueOf(code), 1);
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
