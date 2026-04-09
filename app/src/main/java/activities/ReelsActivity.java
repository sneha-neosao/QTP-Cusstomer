package activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.bolaware.viewstimerstory.Momentz;
import com.bolaware.viewstimerstory.MomentzCallback;
import com.bolaware.viewstimerstory.MomentzView;
import ModelClass.ReelsModel;
import com.grocery.QTPmart.R;
import network.Response.ResReels;
import network.ServiceGenrator;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReelsActivity extends AppCompatActivity implements MomentzCallback {

    private Momentz storyMoment;
    List<MomentzView> listOfViews = new ArrayList<>();
    ArrayList<String> imgUrls;

    ImageView img_reel_back;
    RelativeLayout rlNoData;
    ConstraintLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reels);
        img_reel_back=findViewById(R.id.img_reel_back);

        rlNoData = findViewById(R.id.rlNoData);
        container = findViewById(R.id.container);

        img_reel_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgUrls=new ArrayList<>();
        getReelData();

        ImageView imageView = new ImageView(ReelsActivity.this);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        listOfViews.add(new MomentzView(imageView,5));
        listOfViews.add(new MomentzView(imageView,5));
        prepareStories();
/*
        if(type.equals("video")){
            VideoView videoView = new VideoView(ReelsActivity.this);
            listOfViews.add(new MomentzView(videoView,5));
            // listOfViews.add(new MomentzView(posters.get(i).getBannerImage(),5));
        }
        else if(type.equals("image")){
            ImageView imageView = new ImageView(ReelsActivity.this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            listOfViews.add(new MomentzView(imageView,5));
        }
        imgUrls.add(BASE_URL+posters.get(i).getBannerImage());*/

    }

    public void getReelData(){

        ServiceGenrator.getApiInterface().getReelsList().enqueue(
                new Callback<ResReels>() {
                    @Override
                    public void onResponse(Call<ResReels> call, Response<ResReels> response) {

                        if (response.isSuccessful()) {

                            if (response.body().isStatus())
                            {
                                rlNoData.setVisibility(View.GONE);
                                container.setVisibility(View.VISIBLE);

                                ArrayList<ReelsModel> reelsModels=response.body().getResult();


                                for(int i=0;i<reelsModels.size();i++)
                                {
                                    String type=reelsModels.get(i).getFilename();
                                    Log.e("MomentzView", "onResponse:   "+type );

                                    String[] separated = type.split("\\.");
                                   String sep1= separated[0];
                                    String sep2=  separated[1];

                                    if(sep2.equals("mp4")){
                                        VideoView videoView = new VideoView(ReelsActivity.this);
                                        listOfViews.add(new MomentzView(videoView,5));

                                    }
                                    else if(sep2.equals("jpg") || sep2.equals("png") ){
                                        ImageView imageView = new ImageView(ReelsActivity.this);
                                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                        listOfViews.add(new MomentzView(imageView,5));
                                    }
                                    imgUrls.add(reelsModels.get(i).getReelLocation());
                                }
                                prepareStories();
                            }else{
                                rlNoData.setVisibility(View.VISIBLE);
                                container.setVisibility(View.GONE);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ResReels> call, Throwable t) {

                    }
                });


    }

    private void prepareStories() {

        ConstraintLayout container = findViewById(R.id.container);

//        storyMoment =  new Momentz(this, listOfViews, container, this, R.drawable.green_lightgrey_drawable);

//        storyMoment.start();
    }

    @Override
    public void done() {

    }

    @Override
    public void onNextCalled(@NotNull View view, @NotNull Momentz momentz, int i) {
        if(view instanceof VideoView)
        {
            momentz.pause(true);
            playVideo((VideoView) view,i, momentz);
        }
        else if(view instanceof ImageView)
        {
            momentz.pause(true);
            Picasso.get()
                    .load(imgUrls.get(i))
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .into((ImageView) view, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            momentz.resume();
                            if(i == listOfViews.size())
                            {
                                for (int i = 0; i <=listOfViews.size(); i++) {
                                    momentz.prev();
                                }

                            }

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
        }
    }

    private void playVideo(VideoView videoView, int index, Momentz momentz) {
        //String str = "https://images.all-free-download.com/footage_preview/mp4/triumphal_arch_paris_traffic_cars_326.mp4";
        String str = imgUrls.get(index);
        Uri uri = Uri.parse(str);

        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();
//        try {
//            Cache cache = new FileCache(new File(getExternalCacheDir(), "COMPITO_CACHE"));
//            HttpUrlSource source = new HttpUrlSource(str);
//            proxyCache = new HttpProxyCache(source, cache);
//
//            videoView.requestFocus();
//            videoView.start();
//        }catch (ProxyCacheException e) {
//            Log.e("check", "Error playing video", e);
//        }

        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mediaPlayer, int what, int extra) {
                if(what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START)
                {
                    momentz.editDurationAndResume(index, (videoView.getDuration()) / 1000);

                }
                return false;
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if(index == listOfViews.size())
                {
                    for (int i = 0; i < listOfViews.size(); i++) {
                        momentz.prev();
                    }

                }
            }
        });


    }

    public void shopNow(View view) {
        onBackPressed();
    }
}