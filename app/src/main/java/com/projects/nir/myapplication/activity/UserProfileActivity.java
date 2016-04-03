package com.projects.nir.myapplication.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.projects.nir.myapplication.R;
import com.projects.nir.myapplication.Entities.UserProfile;
import com.projects.nir.myapplication.ThirdParty.DebouncedOnClickListener;
import com.projects.nir.myapplication.adapters.UserProfileListAdapter;
import com.projects.nir.myapplication.database.DataBaseConstants;
import com.projects.nir.myapplication.database.JsonParser;
import com.projects.nir.myapplication.interfaces.IDataAccessLayer;
import com.projects.nir.myapplication.search.IAsyncCallBack;
import com.projects.nir.myapplication.database.WebDb;

import java.util.ArrayList;

public class UserProfileActivity extends Activity {

    private IDataAccessLayer _DataAccessLayer;              // reference to database helper
    private UserProfileListAdapter _MyUserDetailsListAdapter;// extend adapter to gui list view
    private ListView _UserDetailsListView;
    private int _SelectedItemPosition;
    private ArrayList<UserProfile> _UserDetailList;
    private int userId;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // get reference to db helper
        _DataAccessLayer = new WebDb(this,getString(R.string.preifx));

        _UserDetailList = new ArrayList<>();

        userId = getIntent().getExtras().getInt(SignInActivity.USER_ID_KEY);

        type = getIntent().getExtras().getInt("Type", DataBaseConstants.ProfileTable.AttribType.CAR_NUM);

        EditText inputSearch = (EditText) findViewById(R.id.myDetailsInputSearch);

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                UserProfileActivity.this._MyUserDetailsListAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
        // Search list
        _MyUserDetailsListAdapter = new UserProfileListAdapter(this, _UserDetailList);
        _UserDetailsListView = (ListView) findViewById(R.id.myDetailsListView);
        _UserDetailsListView.setAdapter(_MyUserDetailsListAdapter);

        registerForContextMenu(_UserDetailsListView);

        getUserDetails(userId);

        _SelectedItemPosition = -1;
        _UserDetailsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {
                _SelectedItemPosition = pos;
                return false;
            }
        });

        _UserDetailsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                _SelectedItemPosition = position;
            }
        });

        ImageButton delete = (ImageButton) findViewById(R.id.deleteImageButton);
        delete.setOnClickListener(new DebouncedOnClickListener(3000) {
            @Override
            public void onDebouncedClick(View v) {
                if (_SelectedItemPosition == -1)
                    return;

                _DataAccessLayer.deleteUserDetails("profileId", _UserDetailList.get(_SelectedItemPosition).get_Id(), new IAsyncCallBack() {
                    @Override
                    public void SearchTaskDone(String result) {
                        if (JsonParser.VerifyServerResultOk(result))
                            getUserDetails(userId);
                    }
                });
            }
        });

        ImageButton addAttrib = (ImageButton) findViewById(R.id.addAttribImageButton);
        addAttrib.setOnClickListener(new DebouncedOnClickListener(3000) {
            @Override
            public void onDebouncedClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
                if (type == DataBaseConstants.ProfileTable.AttribType.CAR_NUM) {
                    builder.setTitle("Insert Car Number");
                } else
                    builder.setTitle("Insert Phone Number");

                // Set up the input
                final EditText input = new EditText(UserProfileActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String text = input.getText().toString();
                        if (text.equals(""))
                            Toast.makeText(UserProfileActivity.this, "Empty value is not allowed", Toast.LENGTH_SHORT).show();
                        else
                        {
                            UserProfile temp = new UserProfile(userId, text, type);

                            _DataAccessLayer.AddUserDetailToDataBase(temp, new IAsyncCallBack() {
                                @Override
                                public void SearchTaskDone(String result) {
                                    if (JsonParser.ParseInsertResult(result) >= 0)
                                        getUserDetails(userId);
                                }
                            });

                            _MyUserDetailsListAdapter.notifyDataSetChanged();
                            findViewById(R.id.deleteImageButton).setEnabled(true);
                            findViewById(R.id.myDetailsInputSearch).setEnabled(true);
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

        findViewById(R.id.deleteImageButton).setEnabled(_UserDetailList.size() != 0);
        findViewById(R.id.myDetailsInputSearch).setEnabled(_UserDetailList.size() != 0);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.myDetailsListView) {
            MenuInflater inflater = new MenuInflater(UserProfileActivity.this);
            inflater.inflate(R.menu.menu_user_profile, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        // Handle item selection
        Intent intent;
        switch (itemId) {
            case R.id.deleteAllMyDetail:

                new AlertDialog.Builder(this)
                        .setTitle("Delete Confirmation")
                        .setMessage("Are you sure you want to delete " + _UserDetailList.get(_SelectedItemPosition).attrib + " ? ")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        _DataAccessLayer.deleteUserDetails("profileId", _UserDetailList.get(_SelectedItemPosition).get_Id(), new IAsyncCallBack() {
                                            @Override
                                            public void SearchTaskDone(String result) {
                                                if (JsonParser.VerifyServerResultOk(result))
                                                    getUserDetails(userId);
                                            }
                                        });
                                    }
                                })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();



               /* _DataAccessLayer.deleteUserDetails("userId", userId, new IAsyncCallBack() {
                    @Override
                    public void SearchTaskDone(String result) {
                        if (JsonParser.VerifyServerResultOk(result))
                            getUserDetails(userId);
                    }
                });*/
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void getUserDetails(int userId) {
        _DataAccessLayer.getUserDetails(userId, new IAsyncCallBack() {
            @Override
            public void SearchTaskDone(String result) {
                _UserDetailList.clear();
                ArrayList<UserProfile> temp = JsonParser.ParseUserProfile(result);
                if (temp != null) {
                    for (int i = 0; i < temp.size(); i++)
                        if (temp.get(i).attribType == type)
                            _UserDetailList.add(temp.get(i));
                }

                _MyUserDetailsListAdapter.notifyDataSetChanged();

                findViewById(R.id.deleteImageButton).setEnabled(_UserDetailList.size() != 0);
                findViewById(R.id.myDetailsInputSearch).setEnabled(_UserDetailList.size() != 0);
            }
        });

    }

}
