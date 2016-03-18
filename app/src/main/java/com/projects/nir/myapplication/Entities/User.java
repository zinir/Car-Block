package com.projects.nir.myapplication.Entities;

public class User {


    private int _Id;
    public String UserName;
    public String FirstName;
    public String Password;
    public String ImageUri;


    public User(int _Id, String userName, String firstName, String password, String imageUri) {
        this._Id = _Id;
        UserName = userName;
        FirstName = firstName;
        Password = password;
        ImageUri = imageUri;
    }

    public User(String userName, String firstName, String password, String imageUri) {
        UserName = userName;
        FirstName = firstName;
        Password = password;
        ImageUri = imageUri;
    }


    public int get_Id() {
        return _Id;
    }
}

