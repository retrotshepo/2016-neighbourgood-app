package com.baqspace.proto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tshepo Lebusa on 18/07/2016.
 */
public class Output
{
    /*
    public List<Feeds> feeds = new ArrayList<Feeds>();
    public List<Users> user = new ArrayList<Users>();
    public List<Items> menu = new ArrayList<Items>();
    public List<IMG> images = new ArrayList<>();
    */

    public List<Feeds> feeds ;
    public List<Users> user ;
    public List<Items> menu ;
    public List<IMG> images ;

    public List<StoreLocations> locations ;

    public Users single ;

    //public Users current = new Users();


    public List<cReviews> reviews ;


    public UserProfile VendorProfile ;
    public Contacts Cdetails ;

    public String Comfirmation = "";
}

class Feeds
{
    public int feedID;
    public String NewsFeed1;
    public String UserID;
    public String UserType;
    public String userPicture;
    public String UserName;
    public int newsImage;
    public String timeStemp;
    public String Event;
    public int likes;
}

class Users
{
    public int User_ID;
    public String User_Type;
    public String Password;
    public String UserName;
    public int Profile_ID;
    public String Email;
    public String Registration_Date;
    public String Subscription;
    public String UserImg;

    public int stars;
    public int active;


}

class Items
{
    public int Menu_ID;
    public String Item_Description;
    public String Item_Price;
    public String Profile_ID;
    public String Item_Name;
    public String Deleted;
    public int likes = 8;
}

class UserProfile
{
    public int Profile_ID;
    public String Profile_Bio;
    public String User_ID;
    public String User_Type;
    public String Stars;

}

class cReviews
{
    public int Review_ID;
    public String Review1;
    public String Profile_ID;
    public String trader;
    public String User_ID;
    public String UserName;
    public String User_Type;
    public String Stars;
    public String Date;

}

class Contacts
{
    public int Contact_ID;
    public String Website;
    public String Facebook;
    public String Instagram;
    public String Twitter;
    public String Profile_ID;

}

class IMG
{
    public int Image_ID;
    public String Img_Name;
    public String Img_;
}



class StoreLocations
{
    public int ProfileID;
    public String TraderName;

    public double Lat;
    public double Long;
}

