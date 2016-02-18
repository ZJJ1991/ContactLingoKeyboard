package com.example.jzhou.contactlingokeyboard;

import android.accessibilityservice.AccessibilityService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Switch;
import android.widget.Toast;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ahsanmanzoor on 13/02/2016.
 */
public class ContactAccess extends AccessibilityService {
    public String NUMBER;
    private SharedPreferences languagePreference;

    public ContactAccess() {
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        //Log.d("CONTACT_LINGO", event.toString());
        if( event.getEventType() == AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED && event.getPackageName().equals("com.android.mms")) {
            List<CharSequence> text = event.getText();
            if((text.toString().charAt(1) == '+' || text.toString().charAt(1) == '0')  && text.toString().length() >= 7) {
                int length = text.toString().length();
                NUMBER = text.toString().substring(1, length - 1);
               // NUMBER = text.toString();
                Log.d("CONTACT_LINGO 1", NUMBER);
                if ( ifSaved(NUMBER))
                {
                    MyKeyboard.NUMBER = NUMBER;
                    //Log.d("CONTACT_LINGO 1", NUMBER);
                    System.out.println("ALREADY EXIST");
                }
                else {
                    addData(NUMBER);
                }
            }
        }
        else if(event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED && event.getPackageName().equals("com.android.mms")) {
            List<CharSequence> allText = event.getText();
            String NAME = allText.get(0).toString();
            Log.d("CONTACT_LINGO :", NAME);
            if (NAME.equals("Navigate up") || NAME.equals("Enter message") || NAME.equals("Send")) {
            } else {
                NUMBER = getContactData(NAME);
                if (NUMBER != null) {
                    if (ifSaved(NUMBER)) {
                        //Log.d("CONTACT_LINGO 1", NUMBER);
                        MyKeyboard.NUMBER = NUMBER;
                        System.out.println("ALREADY EXIST");
                    } else {
                        addData(NUMBER);
                    }
                } else System.out.println("NO SAVED CONTACT");
            }
        }
    }

    private String getContactData(String NAME) {
        String value = null;
        Cursor cursor = getContentResolver()
                .query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME}, null, null, null);
        cursor.moveToFirst();
        ArrayList contactsNumber = new ArrayList<String>();
        ArrayList contactsName = new ArrayList<String>();
        int position =0;
        while (cursor.moveToNext()) {
            contactsName.add(cursor.getString(1));
            contactsNumber.add(cursor.getString(0));
        }

        for(position=0; position < contactsName.size(); position++){
            if(contactsName.get(position).equals(NAME)){
                System.out.println(contactsNumber.get(position));
                value =  String.valueOf(contactsNumber.get(position));
                break;
            }
        }
        return value;
    }

    public void addData(String NUMBER) {
        ContentValues new_data = new ContentValues();
        new_data.put(Provider.BasicData.CONTACT, NUMBER);
        new_data.put(Provider.BasicData.FIRST_LANG, "ENGLISH");
        new_data.put(Provider.BasicData.SECOND_LANG, "FINNISH");
        getContentResolver().insert(Provider.BasicData.CONTENT_URI, new_data);
    }

    public boolean ifSaved(String NUMBER){
        String[] projection = new String[]{ Provider.BasicData.CONTACT};
        Cursor cursor = getContentResolver().query(Provider.BasicData.CONTENT_URI, projection, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String contact = cursor.getString(0);
                if (NUMBER.equals(contact))
                {
                    return true;
                }
            } while (cursor.moveToNext());
        }
        return false;
    }

    @Override
    public void onInterrupt() {

    }
}
