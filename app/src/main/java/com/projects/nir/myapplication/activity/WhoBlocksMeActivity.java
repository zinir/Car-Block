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
import com.projects.nir.myapplication.adapters.BlockingCarsListAdapter;
import com.projects.nir.myapplication.database.JsonParser;
import com.projects.nir.myapplication.interfaces.IDataAccessLayer;
import com.projects.nir.myapplication.search.IAsyncCallBack;
import com.projects.nir.myapplication.database.WebDb;

import java.util.ArrayList;

public class WhoBlocksMeActivity extends Activity {

    private IDataAccessLayer _DataAccessLayer;              // reference to database helper
    private BlockingCarsListAdapter _BlockingCarsListAdapter;// extend adapter to gui list view
    private ListView _BlockingCarListView;
    private ArrayList<BlockedRelation> _BlockedDetailsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocking_cars);

        // get reference to db helper
        _DataAccessLayer = new WebDb(this);

        _BlockedDetailsList = new ArrayList<>();

        final int userId = getIntent().getExtras().getInt(SignInActivity.USER_ID_KEY);

        EditText inputSearch = (EditText) findViewById(R.id.blockingCarsInputSearch);

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                WhoBlocksMeActivity.this._BlockingCarsListAdapter.getFilter().filter(cs);
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

        final TextView blockingCarsTextView = (TextView)findViewById(R.id.blockingCarsTextView);

        // Search list
        _BlockingCarsListAdapter = new BlockingCarsListAdapter(this, _BlockedDetailsList,_DataAccessLayer);
        _BlockingCarListView = (ListView) findViewById(R.id.blockingCarListView);

        _BlockingCarListView.setAdapter(_BlockingCarsListAdapter);

        _DataAccessLayer.GetBlockingCars(userId, new IAsyncCallBack() {
            @Override
            public void SearchTaskDone(String result) {
                _BlockedDetailsList.clear();
                ArrayList<BlockedRelation> temp = JsonParser.ParseBlockedRelations(result);
                if (temp != null)
                    _BlockedDetailsList.addAll(temp);
                _BlockingCarsListAdapter.notifyDataSetChanged();

                if (_BlockedDetailsList.size() == 0)
                    blockingCarsTextView.setVisibility(View.VISIBLE);
                else
                    blockingCarsTextView.setVisibility(View.GONE);
            }
        });
    }
}
