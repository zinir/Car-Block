package com.projects.nir.myapplication.database;

import android.content.Context;
import android.database.Cursor;
import android.util.Base64;
import android.util.Log;

import com.projects.nir.myapplication.Entities.BlockedRelation;
import com.projects.nir.myapplication.Entities.User;
import com.projects.nir.myapplication.Entities.UserProfile;
import com.projects.nir.myapplication.interfaces.IDataAccessLayer;
import com.projects.nir.myapplication.search.IAsyncCallBack;
import com.projects.nir.myapplication.search.SearchWebAsyncTask;

import java.security.SecureRandom;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by zilkha on 07/03/2016.
 */
public class WebDb implements IDataAccessLayer {
    Context _context;
    User temp = null;

    public WebDb(Context context) {
        _context = context;
    }

    @Override
    public void getUser(int userId, IAsyncCallBack searchCallBack) {
        temp = null;
        SearchWebAsyncTask searchWeb = new SearchWebAsyncTask(_context, searchCallBack);
        searchWeb.execute("http://www.carblock.netne.net/Scripts/GetUser.php?userId=" + String.valueOf(userId));
    }

    @Override
    public void AddUserToDataBase(User Item, IAsyncCallBack searchCallBack) {
        SearchWebAsyncTask searchWeb = new SearchWebAsyncTask(_context, searchCallBack);

/*        // Set up secret key spec for 128-bit AES encryption and decryption
        SecretKeySpec sks = null;
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed("any data used as random seed".getBytes());
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128, sr);
            sks = new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
        } catch (Exception e) {
        }

        // Encode the original data with AES
        byte[] username = null;
        byte[] firstname = null;
        byte[] password = null;
        byte[] imageuri = null;

        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, sks);
            username = c.doFinal(Item.UserName.getBytes());
            firstname = c.doFinal(Item.FirstName.getBytes());
            password = c.doFinal(Item.Password.getBytes());
            imageuri = c.doFinal(Item.ImageUri.getBytes());
        } catch (Exception e) {
        }

        String UserName = Base64.encodeToString(username, Base64.DEFAULT);
        String FirstName = Base64.encodeToString(firstname, Base64.DEFAULT);
        String Password = Base64.encodeToString(password, Base64.DEFAULT);
        String ImageUri = "";
        if (imageuri != null)
          ImageUri = Base64.encodeToString(imageuri, Base64.DEFAULT);*/

        searchWeb.execute("http://www.carblock.netne.net/Scripts/AddUserToDataBase.php?userName=" + Item.UserName + "&firstName=" + Item.FirstName + "&password=" + Item.Password + "&imageUrl=" + Item.ImageUri);
       // searchWeb.execute("http://www.carblock.netne.net/Scripts/AddUserToDataBase.php?userName=" + UserName + "&firstName=" + FirstName + "&password=" + Password + "&imageUrl=" + ImageUri);
    }

    @Override
    public void VerifyLogin(String userName, String password, IAsyncCallBack searchCallBack) {
        SearchWebAsyncTask searchWeb = new SearchWebAsyncTask(_context, searchCallBack);
        searchWeb.execute("http://www.carblock.netne.net/Scripts/VerifyLogin.php?userName=" + userName + "&password=" + password);
    }

    @Override
    public void IsUserNameExists(String userName, IAsyncCallBack searchCallBack) {
        SearchWebAsyncTask searchWeb = new SearchWebAsyncTask(_context, searchCallBack);
        searchWeb.execute("http://www.carblock.netne.net/Scripts/IsUserNameExists.php?userName=" + userName);
    }

    @Override
    public void AddUserDetailToDataBase(UserProfile item, IAsyncCallBack searchCallBack) {
        SearchWebAsyncTask searchWeb = new SearchWebAsyncTask(_context, searchCallBack);
        searchWeb.execute("http://www.carblock.netne.net/Scripts/AddUserDetailToDataBase.php?userId=" + item.userId + "&attrib=" + item.attrib + "&attribType=" + item.attribType);
    }

    @Override
    public void GetUserInfoByPhoneNumber(String phoneNumber, IAsyncCallBack searchCallBack) {
        SearchWebAsyncTask searchWeb = new SearchWebAsyncTask(_context, searchCallBack);
        searchWeb.execute("http://www.carblock.netne.net/Scripts/GetUserInfo.php?phoneNumber=" + phoneNumber);
    }

    @Override
    public void getUserDetails(int userId, IAsyncCallBack searchCallBack) {
        SearchWebAsyncTask searchWeb = new SearchWebAsyncTask(_context, searchCallBack);
        searchWeb.execute("http://www.carblock.netne.net/Scripts/GetUserDetails.php?userId=" + userId + "&exclude=false");
    }

    @Override
    public void getProfile(int profileId, IAsyncCallBack searchCallBack) {
        SearchWebAsyncTask searchWeb = new SearchWebAsyncTask(_context, searchCallBack);
        searchWeb.execute("http://www.carblock.netne.net/Scripts/GetProfile.php?profileId=" + profileId);
    }

    @Override
    public void deleteUserDetails(String key, int id, IAsyncCallBack searchCallBack) {
        SearchWebAsyncTask searchWeb = new SearchWebAsyncTask(_context, searchCallBack);
        searchWeb.execute("http://www.carblock.netne.net/Scripts/DeleteUserDetails.php?" + key + "=" + id);
    }


    @Override
    public void GetBlockingCars(int userId, IAsyncCallBack searchCallBack) {
        SearchWebAsyncTask searchWeb = new SearchWebAsyncTask(_context, searchCallBack);
        searchWeb.execute("http://www.carblock.netne.net/Scripts/GetBlockingCars.php?userId=" + String.valueOf(userId));
    }

    @Override
    public void SearchCars(int userId, String search, int serachId, IAsyncCallBack searchCallBack) {
        SearchWebAsyncTask searchWeb = new SearchWebAsyncTask(_context, searchCallBack);
        searchWeb.execute("http://www.carblock.netne.net/Scripts/SearchCars.php?userId=" + String.valueOf(userId) + "&search=" + search + "&searchId=" + serachId);
    }

    @Override
    public void GetPhoneNumber(int userId, IAsyncCallBack searchCallBack) {
        SearchWebAsyncTask searchWeb = new SearchWebAsyncTask(_context, searchCallBack);
        searchWeb.execute("http://www.carblock.netne.net/Scripts/GetPhoneNumber.php?userId=" + String.valueOf(userId));
    }

    @Override
    public void AddBlockingRelation(BlockedRelation Item, IAsyncCallBack searchCallBack) {
        SearchWebAsyncTask searchWeb = new SearchWebAsyncTask(_context, searchCallBack);
        searchWeb.execute("http://www.carblock.netne.net/Scripts/AddBlockingRelation.php?blockingUserId=" + Item.blockingUserId + "&blockedUserId=" + Item.blockedUserId + "&blockedUserProfileId=" + Item.blockedUserProfileId);
    }

    @Override
    public void DeleteBlockingRelation(BlockedRelation Item, IAsyncCallBack searchCallBack) {
        SearchWebAsyncTask searchWeb = new SearchWebAsyncTask(_context, searchCallBack);
        searchWeb.execute("http://www.carblock.netne.net/Scripts/DeleteBlockingRelation.php?blockingUserId=" + Item.blockingUserId + "&blockedUserProfileId=" + Item.blockedUserProfileId);
    }

    @Override
    public void GetBlocked(int userId, IAsyncCallBack searchCallBack) {
        SearchWebAsyncTask searchWeb = new SearchWebAsyncTask(_context, searchCallBack);
        searchWeb.execute("http://www.carblock.netne.net/Scripts/GetBlocked.php?blockingUserId=" + userId);
    }

    @Override
    public void UpdateUserImage(int userId, String imageUri, IAsyncCallBack searchCallBack) {
        SearchWebAsyncTask searchWeb = new SearchWebAsyncTask(_context, searchCallBack);
        searchWeb.execute("http://www.carblock.netne.net/Scripts/UpdateUserImage.php?userId=" + userId + "&imageUrl=" + imageUri);
    }

}
