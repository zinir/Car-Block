package com.projects.nir.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.projects.nir.myapplication.Entities.BlockedRelation;
import com.projects.nir.myapplication.Entities.UserProfile;
import com.projects.nir.myapplication.R;
import com.projects.nir.myapplication.adapters.BlockedUnblockCarsListAdapter;
import com.projects.nir.myapplication.adapters.OptionInterface;
import com.projects.nir.myapplication.database.JsonParser;
import com.projects.nir.myapplication.interfaces.IDataAccessLayer;
import com.projects.nir.myapplication.search.IAsyncCallBack;
import com.projects.nir.myapplication.database.WebDb;

import java.util.ArrayList;

public class BlockCarsActivity extends Activity {

    private IDataAccessLayer _DataAccessLayer;              // reference to database helper
    private BlockedUnblockCarsListAdapter _SearchResultCarsListAdapter;// extend adapter to gui list view
    private ListView _SearchResultCarListView;
    private ArrayList<UserProfile> _ResultCarList;
    private int searchId;
    private Object syncObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_cars);

        // get reference to db helper
        _DataAccessLayer = new WebDb(this,getString(R.string.preifx));

        final int userId = getIntent().getExtras().getInt(SignInActivity.USER_ID_KEY);

        _ResultCarList = new ArrayList<>();

        final EditText inputSearch = (EditText) findViewById(R.id.carsInputSearch);

        searchId = 0;

        syncObject = new Object();

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text

                if (!cs.toString().equals("")) {

                    synchronized (syncObject)
                    {
                        searchId++;
                    }

                     _DataAccessLayer.SearchCars(userId,cs.toString(),searchId,new IAsyncCallBack() {
                        @Override
                        public void SearchTaskDone(String result) {
                            ArrayList<UserProfile> searchResult = new ArrayList<>();
                            int serachIdResult =  JsonParser.ParseSearchCarResult(result,searchResult);
                            synchronized (syncObject)
                            {
                                if (serachIdResult == searchId)
                                {
                                    _ResultCarList.clear();
                                    _ResultCarList.addAll(searchResult);
                                    _SearchResultCarsListAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });
                }
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
        _SearchResultCarsListAdapter = new BlockedUnblockCarsListAdapter(this, userId, _DataAccessLayer, _ResultCarList, true, new OptionInterface() {
            @Override
            public void callBack(final View v, final UserProfile userProfile) {
                BlockedRelation blockedRelation = new BlockedRelation(userId,userProfile.userId,userProfile.get_Id());
                _DataAccessLayer.AddBlockingRelation(blockedRelation, new IAsyncCallBack() {
                    @Override
                    public void SearchTaskDone(String result) {
                        if (JsonParser.ParseInsertResult(result) >= 0)
                        {
                            v.setEnabled(false);
                            userProfile.isBlocked = 1;
                        }
                        else
                            userProfile.isBlocked = 0;


                    }
                });
            }
        });

        _SearchResultCarListView = (ListView) findViewById(R.id.searchCarResultListView);
        _SearchResultCarListView.setAdapter(_SearchResultCarsListAdapter);
    }


}
