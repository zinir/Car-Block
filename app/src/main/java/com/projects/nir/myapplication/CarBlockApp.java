package com.projects.nir.myapplication;

import android.app.Application;

/**
 * Created by zilkha on 04/04/2016.
 */
public class CarBlockApp extends Application
{
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    private int userId;


}
