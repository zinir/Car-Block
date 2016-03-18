package com.projects.nir.myapplication.interfaces;

import android.database.Cursor;

import com.projects.nir.myapplication.Entities.BlockedRelation;
import com.projects.nir.myapplication.Entities.User;
import com.projects.nir.myapplication.Entities.UserProfile;
import com.projects.nir.myapplication.database.DataBaseConstants;
import com.projects.nir.myapplication.search.IAsyncCallBack;

import java.util.ArrayList;

/**
 * Created by zilkha on 04/03/2016.
 */
public interface IDataAccessLayer {
    void getUser(int userId,IAsyncCallBack searchCallBack);
    void AddUserToDataBase(User Item,IAsyncCallBack searchCallBack);
    void UpdateUserImage(int userId, String imageUri,IAsyncCallBack searchCallBack);
    void getUserDetails(int userId,IAsyncCallBack searchCallBack);
    void getProfile(int profileId,IAsyncCallBack searchCallBack);
    void SearchCars(int userId,String search,int searchId,IAsyncCallBack searchCallBack);
    void GetBlocked(int userId,IAsyncCallBack searchCallBack);
    void deleteUserDetails(String key,int id,IAsyncCallBack searchCallBack);
    void AddBlockingRelation(BlockedRelation Item,IAsyncCallBack searchCallBack);
    void DeleteBlockingRelation(BlockedRelation Item,IAsyncCallBack searchCallBack);
    void VerifyLogin(String userName,String password,IAsyncCallBack searchCallBack);
    void IsUserNameExists(String userName,IAsyncCallBack searchCallBack);
    void GetBlockingCars(int userId,IAsyncCallBack searchCallBack);
    void GetPhoneNumber(int userId,IAsyncCallBack searchCallBack);
    void AddUserDetailToDataBase(UserProfile item,IAsyncCallBack searchCallBack);
}
