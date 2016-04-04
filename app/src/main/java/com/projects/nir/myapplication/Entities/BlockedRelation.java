package com.projects.nir.myapplication.Entities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.AdapterView;

import com.projects.nir.myapplication.ThirdParty.DebouncedOnClickListener;
import com.projects.nir.myapplication.adapters.BlockingCarsListAdapter;
import com.projects.nir.myapplication.database.JsonParser;
import com.projects.nir.myapplication.interfaces.IDataAccessLayer;
import com.projects.nir.myapplication.search.IAsyncCallBack;

import java.util.ArrayList;

public class BlockedRelation extends DebouncedOnClickListener implements AdapterView.OnItemSelectedListener {


    private int _relationId;
    public  int blockingUserId;
    public  int blockedUserId;
    public  int blockedUserProfileId;
    public String BlockingUserName;
    public boolean downloadInProgress;
    public ArrayList<String> userPhoneNumberList;
    BlockingCarsListAdapter _blockingCarsListAdapter;
    String selectedPhone;
    public Context  context;

    public BlockedRelation(int _Id, int blockingUserId, int blockedUserId, int blockedUserProfileId) {
        super(3000);
        this._relationId = _Id;
        this.blockingUserId = blockingUserId;
        this.blockedUserId = blockedUserId;
        this.blockedUserProfileId = blockedUserProfileId;
    }

    public BlockedRelation(int blockingUserId, int blockedUserId, int blockedUserProfileId) {
        super(3000);
        this.blockingUserId = blockingUserId;
        this.blockedUserId = blockedUserId;
        this.blockedUserProfileId = blockedUserProfileId;
    }

    public BlockedRelation(int _relationId, int blockingUserId, int blockedUserId, int blockedUserProfileId, String blockingUserName) {
        super(3000);
        this._relationId = _relationId;
        this.blockingUserId = blockingUserId;
        this.blockedUserId = blockedUserId;
        this.blockedUserProfileId = blockedUserProfileId;
        BlockingUserName = blockingUserName;
    }

    public int get_Id() {
        return _relationId;
    }

    public void loadPhoneNumbers(IDataAccessLayer dataAccessLayer,BlockingCarsListAdapter blockingCarsListAdapter)
    {
        _blockingCarsListAdapter = blockingCarsListAdapter;
        if (downloadInProgress == false)
        {
            downloadInProgress = true;
            dataAccessLayer.GetPhoneNumber(this.blockingUserId, new IAsyncCallBack() {
                @Override
                public void SearchTaskDone(String result) {
                    userPhoneNumberList = new ArrayList<String>();
                    userPhoneNumberList.addAll(JsonParser.ParsePhoneNumbers(result));
                    downloadInProgress = false;
                    _blockingCarsListAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       selectedPhone = userPhoneNumberList.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDebouncedClick(View v){

        String uri = "tel:" + selectedPhone;
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(uri));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        } else
            context.startActivity(intent);

    }
}

