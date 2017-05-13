package com.android.logismove.models;


import io.realm.RealmObject;

/**
 * Created by Admin on 5/10/2017.
 */

public class UserInfo {
    private String id;
    private String phone;
    private String email;
    private String identityCardNum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getPhone ()
    {
        return phone;
    }

    public void setPhone (String phone)
    {
        this.phone = phone;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getIdentityCardNum()
    {
        return identityCardNum;
    }

    public void setIdentityCardNum(String identityCardNum)
    {
        this.identityCardNum = identityCardNum;
    }
}
