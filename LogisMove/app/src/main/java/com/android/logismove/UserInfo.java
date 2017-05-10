package com.android.logismove;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 5/10/2017.
 */

public class UserInfo {
    private int id;
    private String phone;
    private String email;
    private String identity_card_num;

    public int getId ()
    {
        return id;
    }

    public void setId (int id)
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
        return identity_card_num;
    }

    public void setIdentityCardNum(String identityCardNum)
    {
        this.identity_card_num = identityCardNum;
    }

    @Override
    public String toString()
    {
        return "User Info [id = "+id+", phone = "+phone+", email = "+email+", identity_card_num = "+ identity_card_num +"]";
    }
}
