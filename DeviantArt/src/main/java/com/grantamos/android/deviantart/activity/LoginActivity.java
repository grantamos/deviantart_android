package com.grantamos.android.deviantart.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.grantamos.android.deviantart.R;

/**
 * Created by Grant on 6/24/13.
 */
public class LoginActivity extends Activity {
    EditText usernameEditText;
    EditText passwordEditText;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setupViews();
    }

    public void setupViews(){
        usernameEditText = (EditText)findViewById(R.id.username_edit_text);
        passwordEditText = (EditText)findViewById(R.id.password_edit_text);
    }

    public void login(View loginButton){
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        Intent browseIntent = new Intent(this, BrowseActivity.class);
        startActivity(browseIntent);
    }
}