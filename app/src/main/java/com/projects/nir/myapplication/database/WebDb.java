package com.projects.nir.myapplication.database;

import android.content.Context;

import com.projects.nir.myapplication.CarBlockApp;
import com.projects.nir.myapplication.Entities.BlockedRelation;
import com.projects.nir.myapplication.Entities.User;
import com.projects.nir.myapplication.Entities.UserProfile;
import com.projects.nir.myapplication.interfaces.IDataAccessLayer;
import com.projects.nir.myapplication.search.IAsyncCallBack;
import com.projects.nir.myapplication.search.SearchWebAsyncTask;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by zilkha on 07/03/2016.
 */
public class WebDb implements IDataAccessLayer {
    Context _context;
    User temp = null;
    String _prefix ="";

    public WebDb(Context context,String prefix) {
        _context = context;
        _prefix = prefix;
    }

    @Override
    public void getUser(int userId, IAsyncCallBack searchCallBack) {
        CarBlockApp app = (CarBlockApp)_context.getApplicationContext();
        SearchWebAsyncTask searchWeb = new SearchWebAsyncTask(_context, searchCallBack,true,"Verify User");
        searchWeb.execute("http://www.carblock.netne.net/"+_prefix+"Scripts/GetUser.php?userId=" + String.valueOf(userId) + "&loggerId=" + app.getUserId());
    }

    @Override
    public void AddUserToDataBase(User Item, IAsyncCallBack searchCallBack) {
        CarBlockApp app = (CarBlockApp)_context.getApplicationContext();
        SearchWebAsyncTask searchWeb = new SearchWebAsyncTask(_context, searchCallBack,true,"Adding User");
        searchWeb.execute("http://www.carblock.netne.net/"+_prefix+"Scripts/AddUserToDataBase.php?userName=" + Item.UserName + "&firstName=" + Item.FirstName + "&password=" + Item.Password + "&imageUrl=" + Item.ImageUri + "&loggerId=" + app.getUserId());
    }

    @Override
    public void VerifyLogin(String userName, String password, IAsyncCallBack searchCallBack) {
        CarBlockApp app = (CarBlockApp)_context.getApplicationContext();
        SearchWebAsyncTask searchWeb = new SearchWebAsyncTask(_context, searchCallBack,true,"Verify Login");
        searchWeb.execute("http://www.carblock.netne.net/"+_prefix+"Scripts/VerifyLogin.php?userName=" + userName + "&password=" + password + "&loggerId=" + app.getUserId());
    }

    @Override
    public void IsUserNameExists(String userName, IAsyncCallBack searchCallBack) {
        CarBlockApp app = (CarBlockApp)_context.getApplicationContext();
        SearchWebAsyncTask searchWeb = new SearchWebAsyncTask(_context, searchCallBack,false,"");
        searchWeb.execute("http://www.carblock.netne.net/"+_prefix+"Scripts/IsUserNameExists.php?userName=" + userName + "&loggerId=" + app.getUserId());
    }

    @Override
    public void AddUserDetailToDataBase(UserProfile item, IAsyncCallBack searchCallBack) {
        CarBlockApp app = (CarBlockApp)_context.getApplicationContext();
        SearchWebAsyncTask searchWeb = new SearchWebAsyncTask(_context, searchCallBack,true,"Updating Information");
        searchWeb.execute("http://www.carblock.netne.net/"+_prefix+"Scripts/AddUserDetailToDataBase.php?userId=" + item.userId + "&attrib=" + item.attrib + "&attribType=" + item.attribType + "&loggerId=" + app.getUserId());
    }

    @Override
    public void getUserDetails(int userId, IAsyncCallBack searchCallBack) {
        CarBlockApp app = (CarBlockApp)_context.getApplicationContext();
        SearchWebAsyncTask searchWeb = new SearchWebAsyncTask(_context, searchCallBack,false,"");
        searchWeb.execute("http://www.carblock.netne.net/"+_prefix+"Scripts/GetUserDetails.php?userId=" + userId + "&exclude=false" + "&loggerId=" + app.getUserId());
    }

    @Override
    public void getProfile(int profileId, IAsyncCallBack searchCallBack) {
        CarBlockApp app = (CarBlockApp)_context.getApplicationContext();
        SearchWebAsyncTask searchWeb = new SearchWebAsyncTask(_context, searchCallBack,false,"");
        searchWeb.execute("http://www.carblock.netne.net/"+_prefix+"Scripts/GetProfile.php?profileId=" + profileId + "&loggerId=" + app.getUserId());
    }

    @Override
    public void deleteUserDetails(String key, int id, IAsyncCallBack searchCallBack) {
        CarBlockApp app = (CarBlockApp)_context.getApplicationContext();
        SearchWebAsyncTask searchWeb = new SearchWebAsyncTask(_context, searchCallBack,true,"Deleting...");
        searchWeb.execute("http://www.carblock.netne.net/"+_prefix+"Scripts/DeleteUserDetails.php?" + key + "=" + id + "&loggerId=" + app.getUserId());
    }


    @Override
    public void GetBlockingCars(int userId, IAsyncCallBack searchCallBack) {
        CarBlockApp app = (CarBlockApp)_context.getApplicationContext();
        SearchWebAsyncTask searchWeb = new SearchWebAsyncTask(_context, searchCallBack,true,"Retrieve Blocking Cars");
        searchWeb.execute("http://www.carblock.netne.net/"+_prefix+"Scripts/GetBlockingCars.php?userId=" + String.valueOf(userId) + "&loggerId=" + app.getUserId());
    }

    @Override
    public void SearchCars(int userId, String search, int searchId, IAsyncCallBack searchCallBack) {
        CarBlockApp app = (CarBlockApp)_context.getApplicationContext();
        SearchWebAsyncTask searchWeb = new SearchWebAsyncTask(_context, searchCallBack,false,"Searching");
        searchWeb.execute("http://www.carblock.netne.net/"+_prefix+"Scripts/SearchCars.php?userId=" + String.valueOf(userId) + "&search=" + search + "&searchId=" + searchId + "&loggerId=" + app.getUserId());
    }

    @Override
    public void GetPhoneNumber(int userId, IAsyncCallBack searchCallBack) {
        CarBlockApp app = (CarBlockApp)_context.getApplicationContext();
        SearchWebAsyncTask searchWeb = new SearchWebAsyncTask(_context, searchCallBack,false,"");
        searchWeb.execute("http://www.carblock.netne.net/"+_prefix+"Scripts/GetPhoneNumber.php?userId=" + String.valueOf(userId) + "&loggerId=" + app.getUserId());
    }

    @Override
    public void AddBlockingRelation(BlockedRelation Item, IAsyncCallBack searchCallBack) {
        CarBlockApp app = (CarBlockApp)_context.getApplicationContext();
        SearchWebAsyncTask searchWeb = new SearchWebAsyncTask(_context, searchCallBack,true,"Blocking...");
        searchWeb.execute("http://www.carblock.netne.net/"+_prefix+"Scripts/AddBlockingRelation.php?blockingUserId=" + Item.blockingUserId + "&blockedUserId=" + Item.blockedUserId + "&blockedUserProfileId=" + Item.blockedUserProfileId + "&loggerId=" + app.getUserId());
    }

    @Override
    public void DeleteBlockingRelation(BlockedRelation Item, IAsyncCallBack searchCallBack) {
        CarBlockApp app = (CarBlockApp)_context.getApplicationContext();
        SearchWebAsyncTask searchWeb = new SearchWebAsyncTask(_context, searchCallBack,true,"Unblocking");
        searchWeb.execute("http://www.carblock.netne.net/"+_prefix+"Scripts/DeleteBlockingRelation.php?blockingUserId=" + Item.blockingUserId + "&blockedUserProfileId=" + Item.blockedUserProfileId + "&loggerId=" + app.getUserId());
    }

    @Override
    public void GetBlocked(int userId, IAsyncCallBack searchCallBack) {
        CarBlockApp app = (CarBlockApp)_context.getApplicationContext();
        SearchWebAsyncTask searchWeb = new SearchWebAsyncTask(_context, searchCallBack,true,"Retrieving Information");
        searchWeb.execute("http://www.carblock.netne.net/"+_prefix+"Scripts/GetBlocked.php?blockingUserId=" + userId + "&loggerId=" + app.getUserId());
    }

    @Override
    public void UpdateUserImage(int userId, String imageUri, IAsyncCallBack searchCallBack) {
        CarBlockApp app = (CarBlockApp)_context.getApplicationContext();
        SearchWebAsyncTask searchWeb = new SearchWebAsyncTask(_context, searchCallBack,true,"Updating Image");
        try {
            //String query = URLEncoder.encode(imageUri, "utf-8");
            searchWeb.execute("http://www.carblock.netne.net/"+_prefix+"Scripts/UpdateUserImage.php?userId=" + userId + "&imageUrl=" + query + "&loggerId=" + app.getUserId());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void GetUserInfoByPhoneNumber(String phoneNumber, IAsyncCallBack searchCallBack) {
        CarBlockApp app = (CarBlockApp)_context.getApplicationContext();
        SearchWebAsyncTask searchWeb = new SearchWebAsyncTask(_context, searchCallBack,true,"Retrieving Information");
        searchWeb.execute("http://www.carblock.netne.net/"+_prefix+"Scripts/GetUserInfo.php?phoneNumber=" + phoneNumber + "&loggerId=" + app.getUserId());
    }

}
