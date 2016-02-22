package com.example.jzhou.contactlingokeyboard;

        import android.content.Intent;

        import android.support.v7.app.ActionBarActivity;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onResume() {
        super.onResume();
        startActivity(new Intent(getApplicationContext(), Settings.class));
        finish();
    }





}
