package com.baqspace.proto;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baqspace.model.FeedPost;
import com.baqspace.model.User;
import com.squareup.picasso.Picasso;
import com.threepin.fireexit_wcf.Configurator;
import com.threepin.fireexit_wcf.FireExitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Tshepo Lebusa on 19/08/2016.
 */
public class CustomNewsFeedAdapter extends ArrayAdapter<Feeds> {

    View feedView;
    View imageView;
    TextView timeStamp;
    TextView newsFeed;

    TextView feedLikes;
    TextView poster;

    Input i = new Input();
    Output o = new Output();
    Handler handler;

    List<IMG> images = new ArrayList<>();
    User session = null;

    public CustomNewsFeedAdapter(Context context, List<Feeds> feeds, List<IMG> imgUri, User session) {
        super(context, R.layout.custom_layout_newsfeed, feeds);
        images = imgUri;
        this.session = session;

        handler = new Handler();
    }


    String user;
    String disImg;

    String feedImg;
    String source;

    String post;
    String eventType;

    int likeCount;
    String time;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater tempInflater = LayoutInflater.from(getContext());

        feedView = tempInflater.inflate(R.layout.custom_layout_newsfeed, parent, false);

        user = getItem(position).UserName;
        disImg = getItem(position).userPicture;
        time = getItem(position).timeStemp;

        post = getItem(position).NewsFeed1;
        eventType = getItem(position).Event;

        feedImg = images.get(position).Img_Name;
         likeCount = getItem(position).likes;

        setupComponents();



        //onCreateZoomedView();







        feedView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                String tempStr = "<b>" + getItem(position).Event.toUpperCase(Locale.ENGLISH) + "</b>" + "<br />" + getItem(position).NewsFeed1;

                session.post = new FeedPost(getItem(position).UserName, getItem(position).userPicture, images.get(position).Img_Name, tempStr, getItem(position).likes, getItem(position).timeStemp);

                Intent intent = new Intent(getContext(), FeedView.class);
                intent.putExtra("currentSession", session);

                getContext().startActivity(intent);


                return true;
            }
        });


        return feedView;
    }

    private void setupComponents() {
        imageView = feedView.findViewById(R.id.feed_image);
        timeStamp = (TextView) feedView.findViewById(R.id.feed_timestamp);
        newsFeed = (TextView) feedView.findViewById(R.id.feed_news);

        feedLikes = (TextView) feedView.findViewById(R.id.feed_likes);
        poster = (TextView) feedView.findViewById(R.id.feed_owner);


        //display information

        poster.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
        poster.setText(user);

        timeStamp.setText(time.substring(0));

        source = "<b>" + eventType.toUpperCase(Locale.ENGLISH) + "</b>" + "<br />" + post;

        newsFeed.setText(Html.fromHtml(source));

        feedLikes.setText(likeCount + "");

        Picasso.with(getContext()).load(feedImg).into((ImageView) imageView);


    }



    class Async extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {

            //Establishing connection between mobile and wcf
            FireExitClient client = new FireExitClient(i.WCF_ADDRESS);

            //Calling the appropriate method "LikeFeed"
            client.configure(new Configurator("http://tempuri.org/", "IService1", "LikeFeed"));

            //Passing parameters to the given method
            client.addParameter("input", i);
            try {

                //instantiating feeds list to store data from WCF
                //o.feeds = new ArrayList<>();    //text
                //o.images = new ArrayList<>();   //image

                o = client.call(o); //getting data back
                Toast.makeText(getContext(), "Like sent", Toast.LENGTH_LONG).show();
                //Toast.makeText(MainActivity.this, "successful connection", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                //Toast.makeText(MainActivity.this, "could not retrieve data", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            return o;
        }

        @Override
        protected void onPostExecute(final Object o) {
            super.onPostExecute(o);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    Output out = (Output) o;

                }
            });
        }
    }


    private Animator mCurrentAnimator;

    private int mShortAnimationDuration;


    private void onCreateZoomedView()
    {


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("must zoom please");
                Toast.makeText(getContext(), "must zoom please", Toast.LENGTH_LONG).show();
                zoomImageFromThumb(imageView, feedImg);
            }
        });
        // Retrieve and cache the system's default "short" animation time.
        mShortAnimationDuration = 1000;
    }


    private void zoomImageFromThumb(final View thumbView, String imageResID)
    {
        System.out.println(imageResID);
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }


        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) feedView.findViewById(
                R.id.expanded_image);

        Picasso.with(getContext()).load(imageResID).into(expandedImageView);

        //expandedImageView.setImageResource(imageResId);
        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).

        thumbView.getGlobalVisibleRect(startBounds);
        feedView.findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);


        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        }
        else
        {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }


        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left, finalBounds.left)).with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                startBounds.top, finalBounds.top)).with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));

        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

    }

}
