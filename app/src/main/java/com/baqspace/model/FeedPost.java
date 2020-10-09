package com.baqspace.model;

import java.io.Serializable;
import java.util.Locale;

/**
 * Created by Tshepo Lebusa on 20/10/2016.
 */
public class FeedPost implements Serializable{

    private String name;
    private String displayImage;

    private String feedImage;
    private String feedDescription;

    private int likes;
    private String time;


    public FeedPost(String name, String displayImage, String feedImage, String feedDescription, int likes, String time)
    {
        setName(name);
        setDisplayImage(displayImage);
        setFeedImage(feedImage);
        setFeedDescription(feedDescription);

        setLikes(likes);
        setTime(time);

    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public void setDisplayImage(String displayImage) {
        this.displayImage = displayImage;
    }

    public String getDisplayImage() {
        return displayImage;
    }


    public void setFeedImage(String feedImage) {
        this.feedImage = feedImage;
    }

    public String getFeedImage() {
        return feedImage;
    }


    public void setFeedDescription(String feedDescription) {
        this.feedDescription = feedDescription;
    }

    public String getFeedDescription() {
        return feedDescription;
    }


    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getLikes() {
        return likes;
    }


    public void setTime(String time) {

        if(time.toUpperCase(Locale.ENGLISH).contains("YESTERDAY"))
        {
            this.time = time.toUpperCase(Locale.ENGLISH);
            return;
        }
        else if(time.toUpperCase(Locale.ENGLISH).contains("MONDAY"))
        {
            this.time = time.toUpperCase(Locale.ENGLISH);
            return;
        }
        else if(time.toUpperCase(Locale.ENGLISH).contains("TUESDAY"))
        {
            this.time = time.toUpperCase(Locale.ENGLISH);
            return;
        }
        else if(time.toUpperCase(Locale.ENGLISH).contains("WEDNESDAY"))
        {
            this.time = time.toUpperCase(Locale.ENGLISH);
            return;
        }
        else if(time.toUpperCase(Locale.ENGLISH).contains("THURSDAY"))
        {
            this.time = time.toUpperCase(Locale.ENGLISH);
            return;
        }
        else if(time.toUpperCase(Locale.ENGLISH).contains("FRIDAY"))
        {
            this.time = time.toUpperCase(Locale.ENGLISH);
            return;
        }




          else  if(time.contains("m"))
        {
            this.time = time.replace("m", " MINUTES AGO");
        }
        else if(time.contains("d"))
        {
            this.time = time.replace("d", " DAYS AGO");
        }
        else if(time.contains("on"))
        {
            this.time = time.replace("on", " PAST").toUpperCase(Locale.ENGLISH);
        }
        else
        {
            this.time = time;
        }

    }

    public String getTime() {
        return time;
    }


}

