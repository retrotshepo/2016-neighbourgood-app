package com.baqspace.proto;

import java.io.Serializable;

/**
 * Created by Tshepo Lebusa on 18/07/2016.
 */
public class Input implements Serializable {
    //public String WCF_ADDRESS = "http://c6ff0c7be7bf46938973dafb45b2e798.cloudapp.net/Service1.svc" ;
    //public String WCF_ADDRESS = "http://baqspacecloudservice.cloudapp.net/Service1.svc" ;
    public String WCF_ADDRESS = "http://7cf8419fdfad444c815a371653843b9a.cloudapp.net/Service1.svc";

    public String ID;
    public String userID;

    /*
    public Register reg = new Register();
    public SignIn login = new SignIn();
    public NewsFeeeds feeds = new NewsFeeeds();
    public ReviewFromMobile review = new ReviewFromMobile();
    public UserReview userReview = new UserReview();
    public MenuItemNew menuItemNew = new MenuItemNew();

    public image ImagimagetoSend = new image();
    public LikeFeed likeFeed = new LikeFeed();
    */

    public Register reg ;
    public SignIn login ;
    public NewsFeeeds feeds ;
    public ReviewFromMobile review ;
    public UserReview userReview ;
    public MenuItemNew newItem ;


    public SetLocation setLocation;

    public LikeFeed likeFeed ;

    public Input() {

        //Register reg = new Register();
    }

}


class Register implements Serializable {
    public String User_ID;
    public String User_Type;
    public String Password;
    public String Username;
    public String Email;
    public String Registration_Date;
    public String Subscription;
}

class SignIn implements Serializable {
    public String EmailAddress;
    public String Password;
}

class NewsFeeeds implements Serializable {
    public int User_ID;
    public String User_Type;
    public int Feed_ID;
    public String NewsFeed1;
    public String Event;
}

class LikeFeed implements Serializable {
    public int userID;
    public String userType;
    public int feedID;

}


class ReviewFromMobile implements Serializable {
    public String review;
    public int profileID;
    public int userID;
    public String userType;
    public int stars;
}


class image implements Serializable {
    public String filename;
    public long filesize;
    public String imageString;
}



class UserReview implements Serializable {
    public int userID;
    public String userType;
    public String userName;
}


class MenuItemNew implements Serializable {
    public int profileID;
    public String title;
    public String description;
    public double price;

}


class SetLocation implements Serializable{

    public int profileID;
    public String traderName;

    public double latitude;
    public double longitude;
}
