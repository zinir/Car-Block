/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.projects.nir.myapplication.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.projects.nir.myapplication.R;
import com.projects.nir.myapplication.ThirdParty.CircleImageView;
import com.projects.nir.myapplication.database.DataBaseConstants;
import com.projects.nir.myapplication.database.JsonParser;
import com.projects.nir.myapplication.interfaces.IDataAccessLayer;
import com.projects.nir.myapplication.search.IAsyncCallBack;
import com.projects.nir.myapplication.database.WebDb;

/**
 * This example illustrates a common usage of the DrawerLayout widget
 * in the Android support library.
 * <p/>
 * <p>When a navigation (left) drawer is present, the host activity should detect presses of
 * the action bar's Up affordance as a signal to open and close the navigation drawer. The
 * ActionBarDrawerToggle facilitates this behavior.
 * Items within the drawer should fall into one of two categories:</p>
 * <p/>
 * <ul>
 * <li><strong>View switches</strong>. A view switch follows the same basic policies as
 * list or tab navigation in that a view switch does not create navigation history.
 * This pattern should only be used at the root activity of a task, leaving some form
 * of Up navigation active for activities further down the navigation hierarchy.</li>
 * <li><strong>Selective Up</strong>. The drawer allows the user to choose an alternate
 * parent for Up navigation. This allows a user to jump across an app's navigation
 * hierarchy at will. The application should treat this as it treats Up navigation from
 * a different task, replacing the current task stack using TaskStackBuilder or similar.
 * This is the only form of navigation drawer that should be used outside of the root
 * activity of a task.</li>
 * </ul>
 * <p/>
 * <p>Right side drawers should be used for actions, not navigation. This follows the pattern
 * established by the Action Bar that navigation should be to the left and actions to the right.
 * An action should be an operation performed on the current contents of the window,
 * for example enabling or disabling a data overlay on top of the current content.</p>
 */
public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private LinearLayout mDrawerLinearLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mOptionTitles;
    private int userId;
    private String userName;
    TextView userNameTextView;
    CircleImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userId = getIntent().getExtras().getInt(SignInActivity.USER_ID_KEY);
        userName = getIntent().getExtras().getString(SignInActivity.USER_NAME_KEY);
        String imagePath = getIntent().getExtras().getString(SignInActivity.USER_NAME_IMAGE_URI_KEY);

        mTitle = mDrawerTitle = getTitle();
        mOptionTitles = getResources().getStringArray(R.array.options_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.drawer_list_view);
        mDrawerLinearLayout = (LinearLayout) findViewById(R.id.left_drawer);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);

        userNameTextView = (TextView) findViewById(R.id.drawer_textView);
        userNameTextView.setText(userName);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.listview_item_drawer, mOptionTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(-1);
        }
        setImageFromImagePath(imagePath);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerLinearLayout);
        //  menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {

        if (position == 2) {
            Intent intent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select File"), 0);
        } else {
            // update the main content by replacing fragments
            Fragment fragment = new OptionsFragment();
            Bundle args = new Bundle();
            args.putInt(OptionsFragment.OPTION_NUMBER, position);
            args.putInt(SignInActivity.USER_ID_KEY, userId);
            fragment.setArguments(args);

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            mDrawerLayout.closeDrawer(mDrawerLinearLayout);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            SharedPreferences pref = getSharedPreferences(SignInActivity.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
            String selectedImagePath = setImageFromUri(data.getData());

            IDataAccessLayer _DataAccessLayer = new WebDb(this,getString(R.string.preifx));
            _DataAccessLayer.UpdateUserImage(userId, selectedImagePath, null);
        }
    }

    private String setImageFromUri(Uri selectedImageUri) {
        String selectedImagePath = "";
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


    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(mDrawerLinearLayout))
            mDrawerLayout.closeDrawer(mDrawerLinearLayout);
        else
            super.onBackPressed();
    }

    /**
     * Fragment that appears in the "content_frame", shows a planet
     */
    public static class OptionsFragment extends Fragment {
        public static final String OPTION_NUMBER = "option_number";
        private int userId;

        public OptionsFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_options, container, false);
            int option = getArguments().getInt(OPTION_NUMBER);
            userId = getArguments().getInt(SignInActivity.USER_ID_KEY);

            if (option == 0) {
                Intent i = new Intent(getActivity(), UserProfileActivity.class);
                i.putExtra(SignInActivity.USER_ID_KEY, userId);
                i.putExtra("Type", DataBaseConstants.ProfileTable.AttribType.CAR_NUM);
                startActivity(i);
            } else if (option == 1) {
                Intent i = new Intent(getActivity(), UserProfileActivity.class);
                i.putExtra(SignInActivity.USER_ID_KEY, userId);
                i.putExtra("Type", DataBaseConstants.ProfileTable.AttribType.PHONE_NUM);
                startActivity(i);

            } else if (option == 3) {
                final SharedPreferences pref = getActivity().getSharedPreferences(SignInActivity.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
                SharedPreferences.Editor edit = pref.edit();
                edit.putInt(SignInActivity.USER_ID_KEY, -1);
                edit.commit();
                Intent i = new Intent(getActivity(), SignInActivity.class);
                startActivity(i);
                getActivity().finish();
            } else if (option == 4) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Car Block 1.4")
                        .setMessage("Created by Nir Zilkha")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .show();
            }

            Button blockCarBtn = (Button) rootView.findViewById(R.id.blockCarButton);
            Button unblockCarBtn = (Button) rootView.findViewById(R.id.unblockCarButton);
            Button whoBlocksBtn = (Button) rootView.findViewById(R.id.whoBlocksButton);

            blockCarBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    IDataAccessLayer _DataAccessLayer = new WebDb(getActivity(),getString(R.string.preifx));
                    _DataAccessLayer.GetPhoneNumber(userId, new IAsyncCallBack() {
                        @Override
                        public void SearchTaskDone(String result) {
                            if (JsonParser.VerifyServerResultOk(result)) {
                                Intent i = new Intent(getActivity(), BlockCarsActivity.class);
                                i.putExtra(SignInActivity.USER_ID_KEY, userId);
                                startActivity(i);
                            } else
                                Toast.makeText(getActivity(), "You must have a phone in order to block a car", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });

            unblockCarBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(getActivity(), UnblockCarsActivity.class);
                    i.putExtra(SignInActivity.USER_ID_KEY, userId);
                    startActivity(i);
                }
            });

            whoBlocksBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), WhoBlocksMeActivity.class);
                    i.putExtra(SignInActivity.USER_ID_KEY, userId);
                    startActivity(i);
                }
            });

            return rootView;
        }
    }
}