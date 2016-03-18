package com.projects.nir.myapplication.Entities;

public class UserProfile {

    private int _Id;
    public  int userId;
    public String attrib;
    public int attribType;
    public int isBlocked;

    public UserProfile(int _Id, int userId, String attrib, int attribType) {
        this._Id = _Id;
        this.userId = userId;
        this.attrib = attrib;
        this.attribType = attribType;
    }

    public UserProfile(int userId, String attrib, int attribType) {
        this.userId = userId;
        this.attrib = attrib;
        this.attribType = attribType;
    }

    public UserProfile(int userId, String attrib, int attribType, int isBlocked) {
        this.userId = userId;
        this.attrib = attrib;
        this.attribType = attribType;
        this.isBlocked = isBlocked;
    }

    public UserProfile(int _Id, int userId, String attrib, int attribType, int isBlocked) {
        this._Id = _Id;
        this.userId = userId;
        this.attrib = attrib;
        this.attribType = attribType;
        this.isBlocked = isBlocked;
    }

    public int get_Id() {
        return _Id;
    }
}

