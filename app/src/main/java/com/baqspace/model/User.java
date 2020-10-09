package com.baqspace.model;

import com.baqspace.enumeration.USER_TYPE;

import java.io.Serializable;

/**
 * Created by Tshepo Lebusa on 09/08/2016.
 */
public class User implements Serializable {

    private int ID = 0;
    private String username = "";
    private String password = "";
    private USER_TYPE session;

    public String displayPictureURL;
    public int Profile_ID;
    public String Email;
    public String Registration_Date;
    public String Subscription;

    public int stars;
    public int active;

    public String extraData;
    public int profileToRate;
    public String profileName;


    public FeedPost post;



    public double latitude;
    public double longitude;



    public String traderName;

    public double trLatitude;
    public double trLongitude;






    public User()
    {

    }

    public User(String username, String password, String session)
    {

        setUsername(username);
        setPassword(password);
        setSession(session);
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setSession(String session) {
        this.session = USER_TYPE.valueOf(session);
    }

    public USER_TYPE getSession() {
        return session;
    }


    @Override
    public String toString() {
        String res = "";
        res += "username: " + getUsername() + "\n";
        res += "password: " + getPassword() + "\n";

        res += "Session: " + getSession().getType() + "\n";

        return res;
    }
}
