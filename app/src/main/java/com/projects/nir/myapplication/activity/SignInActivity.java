package com.projects.nir.myapplication.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.projects.nir.myapplication.Entities.UserProfile;
import com.projects.nir.myapplication.R;
import com.projects.nir.myapplication.Entities.User;
import com.projects.nir.myapplication.database.DataBaseConstants;
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
        TextView restoreTextView = (TextView) findViewById(R.id.restoreLink);

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

        restoreTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
                builder.setTitle("Insert Phone Number");

                // Set up the input
                final EditText input = new EditText(SignInActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String text = input.getText().toString();
                        if (text.equals(""))
                            Toast.makeText(SignInActivity.this, "Empty value is not allowed", Toast.LENGTH_SHORT).show();
                        else {

                            _DataAccessLayer.GetUserInfoByPhoneNumber(text, new IAsyncCallBack() {
                                @Override
                                public void SearchTaskDone(String result) {
                                    User temp = JsonParser.ParseUser(result);
                                    if (temp != null) {
                                        sendSMS(text, "User name: " + temp.UserName + "\nPassword: " + temp.Password);
                                    }
                                    else
                                        Toast.makeText(SignInActivity.this,"Phone Number was not found",Toast.LENGTH_LONG).show();
                                }
                            });

                            /*UserProfile temp = new UserProfile(userId, text, type);

                            _DataAccessLayer.AddUserDetailToDataBase(temp, new IAsyncCallBack() {
                                @Override
                ,                public void SearchTaskDone(String result) {
                                    if (JsonParser.ParseInsertResult(result) >= 0)
                                        getUserDetails(userId);
                                }
                            });

                            _MyUserDetailsListAdapter.notifyDataSetChanged();
                            findViewById(R.id.deleteImageButton).setEnabled(true);
                            findViewById(R.id.myDetailsInputSearch).setEnabled(true);*/
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    //---sends an SMS message to another device---
    private void sendSMS(String phoneNumber, String message)
    {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {

                /*switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }*/
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {

             /*   switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }*/
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }
}
