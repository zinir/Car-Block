package com.projects.nir.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.projects.nir.myapplication.Entities.BlockedRelation;
import com.projects.nir.myapplication.R;
import com.projects.nir.myapplication.Entities.UserProfile;
import com.projects.nir.myapplication.adapters.BlockedUnblockCarsListAdapter;
import com.projects.nir.myapplication.adapters.OptionInterface;
import com.projects.nir.myapplication.database.JsonParser;
import com.projects.nir.myapplication.interfaces.IDataAccessLayer;
import com.projects.nir.myapplication.search.IAsyncCallBack;
import com.projects.nir.myapplication.database.WebDb;

import java.util.ArrayList;

public class UnblockCarsActivity extends Activity {

    private IDataAccessLayer _DataAccessLayer;              // reference to database helper
    private BlockedUnblockCarsListAdapter _SearchResultCarsListAdapter;// extend adapter to gui list view
    private ListView _SearchResultCarListView;
    private ArrayList<UserProfile> _ResultCarList;
    private TextView notBlockingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unblock_cars);

        // get reference to db helper
        _DataAccessLayer = new WebDb(this);

        final int userId = getIntent().getExtras().getInt(SignInActivity.USER_ID_KEY);

        _ResultCarList = new ArrayList<>();

        final EditText inputSearch = (EditText) findViewById(R.id.carsInputSearch);

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                UnblockCarsActivity.this._SearchResultCarsListAdapter.getFilter().filter(cs);
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
        notBlockingTextView = (TextView) findViewById(R.id.notBlockingTextView);

        _DataAccessLayer.GetBlocked(userId, new IAsyncCallBack() {
            @Override
            public void SearchTaskDone(String result) {
                ArrayList<BlockedRelation> blockedRelationList = JsonParser.ParseBlockedRelations1(result);

                if ((blockedRelationList == null) || (blockedRelationList.size() == 0)) {
                    notBlockingTextView.setVisibility(View.VISIBLE);
                } else {
                    for (int i = 0; i < blockedRelationList.size(); i++) {
                        _DataAccessLayer.getProfile(blockedRelationList.get(i).blockedUserProfileId, new IAsyncCallBack() {
                            @Override
                            public void SearchTaskDone(String result) {
                                UpdateView(result);
                            }
                        });
                    }
                }

            }
        });

        // Search list
        _SearchResultCarsListAdapter = new BlockedUnblockCarsListAdapter(this, userId, _DataAccessLayer, _ResultCarList, false, new OptionInterface() {
            @Override
            public void callBack(final View v, UserProfile userProfile) {
                BlockedRelation blockedRelation = new BlockedRelation(userId, userProfile.userId, userProfile.get_Id());
                        _DataAccessLayer.DeleteBlockingRelation(blockedRelation, new IAsyncCallBack() {
                            @Override
                            public void SearchTaskDone(String result) {
                                if (JsonParser.VerifyServerResultOk(result))
                                    v.setEnabled(false);
                            }
                        });
            }
        });

        _SearchResultCarListView = (ListView) findViewById(R.id.searchCarResultListView);
        _SearchResultCarListView.setAdapter(_SearchResultCarsListAdapter);
    }

    private synchronized void UpdateView(String result) {
        ArrayList<UserProfile> temp = JsonParser.ParseUserProfile(result);
        _ResultCarList.addAll(temp);
        _SearchResultCarsListAdapter.notifyDataSetChanged();

        if (_ResultCarList.size() == 0) {
            notBlockingTextView.setVisibility(View.VISIBLE);
        } else
            notBlockingTextView.setVisibility(View.GONE);
    }


}
