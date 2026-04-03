package activities.story;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.grocery.QTPmart.R;

import java.util.ArrayList;

import xute.storyview.StoryModel;
import xute.storyview.StoryPlayerProgressView;
import xute.storyview.StoryPreference;

public class StoryPlayer extends Activity implements StoryPlayerProgressView.StoryPlayerListener {
    public static final String STORY_IMAGE_KEY = "storyImages";
    StoryPlayerProgressView storyPlayerProgressView;
    ImageView imageView;
    TextView name;
    TextView time;
    ArrayList<StoryModel> stories;
    StoryPreference storyPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(xute.storyview.R.layout.activity_story_player);
        storyPlayerProgressView = findViewById(xute.storyview.R.id.progressBarView);
        name = findViewById(xute.storyview.R.id.storyUserName);
        time = findViewById(xute.storyview.R.id.storyTime);
        storyPlayerProgressView.setSingleStoryDisplayTime(2000);
        imageView = findViewById(xute.storyview.R.id.storyImage);
        storyPreference = new StoryPreference(this);
        Intent intent = getIntent();
        if (intent != null) {
            stories = intent.getParcelableArrayListExtra(STORY_IMAGE_KEY);
            initStoryProgressView();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        storyPlayerProgressView.cancelAnimation();
    }

    private void initStoryProgressView() {
        if (stories != null && stories.size() > 0) {
            storyPlayerProgressView.setStoryPlayerListener(this);
            storyPlayerProgressView.setProgressBarsCount(stories.size());
            setTouchListener();
        }
    }

    private void setTouchListener() {
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    //pause
                    storyPlayerProgressView.pauseProgress();
                    return true;
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    //resume
                    storyPlayerProgressView.resumeProgress();
                    return true;
                }else {
                    return false;
                }
            }
        });
    }


    @Override
    public void onStartedPlaying(int index) {
        loadImage(index);
        name.setText(stories.get(index).name);
        time.setText(stories.get(index).time);
        storyPreference.setStoryVisited(stories.get(index).imageUri);
    }

    @Override
    public void onFinishedPlaying() {
        finish();
    }

    private void loadImage(int index) {
        if(!this.isDestroyed()) {
            storyPlayerProgressView.pauseProgress();
            Glide.with(this).clear(imageView);
            Glide.with(this)
             .load(R.drawable.image_reel)
             .transition(DrawableTransitionOptions.withCrossFade(500))
             .addListener(new RequestListener<Drawable>() {
                 @Override
                 public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                     storyPlayerProgressView.resumeProgress();
                     return false;
                 }

                 @Override
                 public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                     storyPlayerProgressView.resumeProgress();
                     return false;
                 }
             })
             .into(imageView);
        }
    }
}
