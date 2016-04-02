package com.projects.nir.myapplication.activity;

import android.app.Activity;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.projects.nir.myapplication.R;
import com.projects.nir.myapplication.Entities.User;
import com.projects.nir.myapplication.ThirdParty.CircleImageView;
import com.projects.nir.myapplication.database.JsonParser;
import com.projects.nir.myapplication.interfaces.IDataAccessLayer;
import com.projects.nir.myapplication.search.IAsyncCallBack;
import com.projects.nir.myapplication.database.WebDb;

public class SignUpActivity extends Activity {

    private IDataAccessLayer _DataAccessLayer;              // reference to database helper
    private String selectedImagePath;
    CircleImageView profile_image;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // get reference to db helper
        _DataAccessLayer = new WebDb(this,getString(R.string.preifx));

        final EditText userName = (EditText) findViewById(R.id.signUpUserNameEditText);
        final EditText password = (EditText) findViewById(R.id.signUpPasswordEditText);
        final EditText firstName = (EditText) findViewById(R.id.signUpUserFirstNameEditText);
        profile_image = (CircleImageView) findViewById(R.id.signup_image);

        final Button signUpButton = (Button) findViewById(R.id.signUpButton);
        Button profileImageButton = (Button) findViewById(R.id.signUpProfileImageButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newUserName = userName.getText().toString();
                final String newPassword = password.getText().toString();
                final String newFirstName = firstName.getText().toString();

                if (newUserName.length() == 0)
                    Toast.makeText(SignUpActivity.this, "User Name is empty", Toast.LENGTH_LONG).show();
                else if (newFirstName.length() == 0)
                    Toast.makeText(SignUpActivity.this, "First Name is empty", Toast.LENGTH_LONG).show();
                else if (newPassword.length() < 5)
                    Toast.makeText(SignUpActivity.this, "Password lenght must be higher or equal to 5", Toast.LENGTH_LONG).show();
                else {
                    _DataAccessLayer.IsUserNameExists(newUserName, new IAsyncCallBack() {
                        @Override
                        public void SearchTaskDone(String result) {
                            if (JsonParser.ParseUserNameExists(result)) {
                                Toast.makeText(SignUpActivity.this, "User Name Already Exists", Toast.LENGTH_LONG).show();
                            } else {
                                User newUser = new User(newUserName, newFirstName, newPassword, selectedImagePath);
                                _DataAccessLayer.AddUserToDataBase(newUser, new IAsyncCallBack() {
                                    @Override
                                    public void SearchTaskDone(String result) {
                                        int insertResult = JsonParser.ParseInsertResult(result);
                                        if (insertResult == -1)
                                            Toast.makeText(SignUpActivity.this, "Sign Up Failed", Toast.LENGTH_LONG).show();
                                        else {
                                            Toast.makeText(SignUpActivity.this, "Sign Up Successful.", Toast.LENGTH_LONG).show();
                                            SignUpActivity.this.finish();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

        profileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select File"), 0);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode ==  RESULT_OK)
        {
            SharedPreferences pref = getSharedPreferences(SignInActivity.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
            String selectedImagePath = setImageFromUri(data.getData());
        }
    }

    private String setImageFromUri(Uri selectedImageUri) {
        selectedImagePath = "";
        try {
            String[] projection = {MediaStore.MediaColumns.DATA};
            CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                    null);
            Cursor cursor = cursorLoader.loadInBackground();
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            selectedImagePath = cursor.getString(column_index);
            setImageFromImagePath(selectedImagePath);
        } catch (Exception e) {
            profile_image.setImageResource(R.drawable.profile_user);
        }

        return selectedImagePath;
    }

    private void setImageFromImagePath(String selectedImagePath) {
        try {
            Bitmap bm;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(selectedImagePath, options);
            final int REQUIRED_SIZE = 200;
            int scale = 1;
            while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                    && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;
            options.inSampleSize = scale;
            options.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeFile(selectedImagePath, options);

            ExifInterface ei = new ExifInterface(selectedImagePath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bm = rotateImage(bm, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bm = rotateImage(bm, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bm = rotateImage(bm, 270);
                    break;
                // etc.
            }
            if (bm == null)
                profile_image.setImageResource(R.drawable.profile_user);
            else
                profile_image.setImageBitmap(bm);
        } catch (Exception e) {
            profile_image.setImageResource(R.drawable.profile_user);
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        return retVal;
    }

}
