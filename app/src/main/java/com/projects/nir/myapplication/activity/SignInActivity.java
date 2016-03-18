package com.projects.nir.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.projects.nir.myapplication.R;
import com.projects.nir.myapplication.Entities.User;
import com.projects.nir.myapplication.database.JsonParser;
import com.projects.nir.myapplication.interfaces.IDataAccessLayer;
import com.projects.nir.myapplication.search.IAsyncCallBack;
import com.projects.nir.myapplication.database.WebDb;


public class SignInActivity extends Activity {

    private IDataAccessLayer _DataAccessLayer;              // reference to database helper
    public static final String SHARED_PREFERENCES_NAME = "myCarsPreferences";
    public static final String USER_ID_KEY = "userId";
    public static final String USER_NAME_KEY = "userName";
    public static final String USER_NAME_IMAGE_URI_KEY = "userProfileImage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        // get reference to db helper
        //_DataAccessLayer = new DbOpenHelper(this);
        _DataAccessLayer = new WebDb(this);

        final SharedPreferences pref = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);

        final int userId = pref.getInt(USER_ID_KEY, -1);
        if (userId != -1) {
            _DataAccessLayer.getUser(userId, new IAsyncCallBack() {
                @Override
                public void SearchTaskDone(String result) {
                    Intent i = new Intent(SignInActivity.this, MainActivity.class);
                    User user = JsonParser.ParseUser(result);
                    if (user != null) {
                        i.putExtra(USER_ID_KEY, userId);
                        i.putExtra(USER_NAME_KEY, user.UserName);
                        i.putExtra(USER_NAME_IMAGE_URI_KEY, user.ImageUri);
                        startActivity(i);
                        finish();
                    }
                }
            });
        }

        final EditText userName = (EditText) findViewById(R.id.userNameEditText);
        final EditText password = (EditText) findViewById(R.id.passwordEditText);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        TextView signInTextView = (TextView) findViewById(R.id.signInLink);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 _DataAccessLayer.VerifyLogin(userName.getText().toString(), password.getText().toString(), new IAsyncCallBack() {
                    @Override
                    public void SearchTaskDone(String result) {
                        User user = JsonParser.ParseUser(result);
                        if (user != null) {
                            SharedPreferences.Editor edit = pref.edit();
                            edit.putInt(USER_ID_KEY, user.get_Id());
                            edit.commit();

                            Intent i = new Intent(SignInActivity.this, MainActivity.class);
                            i.putExtra(USER_ID_KEY, user.get_Id());
                            i.putExtra(USER_NAME_KEY, user.UserName);
                            i.putExtra(USER_NAME_IMAGE_URI_KEY, user.ImageUri);
                            startActivity(i);
                            finish();
                        } else
                            Toast.makeText(SignInActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });
    }

}
