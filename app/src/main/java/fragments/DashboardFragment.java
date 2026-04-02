package fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bolaware.viewstimerstory.Momentz;
import com.bolaware.viewstimerstory.MomentzCallback;
import com.bolaware.viewstimerstory.MomentzView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.grocery.QTPmart.Activity.CartActivity;
//import com.grocery.QTPmart.Activity.FAQListActivity;
//import com.grocery.QTPmart.Activity.MainDrawerActivity;
//import com.grocery.QTPmart.Activity.NewSearchFragment;
//import com.grocery.QTPmart.Adapters.FaqAdapter;
import com.grocery.QTPmart.R;

import activities.MainDrawerActivity;
import network.Response.ResponseGetFaqList;
import network.Response.ResponseGetSlider;
import network.ServiceGenrator;
import util.Session_management;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.VISIBLE;

public class DashboardFragment extends Fragment implements MomentzCallback {

    private Session_management sessionManagement;

    private CardView cvOfferZone,cvOrders,cvCategories,cvBaqala,cvTrackGrocery,cvFavouriteList;

    FloatingActionButton fabFAQ;

    ConstraintLayout container1;
    private Momentz storyMoment;
    List<MomentzView> listOfViews = new ArrayList<>();
    ArrayList<String> imgUrls=new ArrayList<>();
    ArrayList<ResponseGetFaqList.FAQResult> faqList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_dashboard, container, false);
        sessionManagement = new Session_management(getContext());

        /*Initialize components*/
        cvOfferZone = view.findViewById(R.id.cvOfferZone);
        cvOrders = view.findViewById(R.id.cvOrders);
        cvCategories = view.findViewById(R.id.cvCategories);
        cvBaqala = view.findViewById(R.id.cvBaqala);
        cvTrackGrocery = view.findViewById(R.id.cvTrackGrocery);
        cvFavouriteList = view.findViewById(R.id.cvFavouriteList);

        fabFAQ = view.findViewById(R.id.fabFAQ);
        MainDrawerActivity.tvTitle.setVisibility(View.GONE);
        MainDrawerActivity.reelLyt.setVisibility(VISIBLE);
        MainDrawerActivity.notification_iv.setVisibility(VISIBLE);
        MainDrawerActivity.search_iv.setVisibility(VISIBLE);
        MainDrawerActivity.ll_nav_title.setVisibility(View.GONE);

        /*Orders Click*/
        cvOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new MainDrawerActivity().selectedId=2;
//                MainDrawerActivity.bottomNavigation.show(2,true);
//                loadFragment(new OrderFragment());
            }
        });

        /*Favourite list click*/
        cvFavouriteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new MainDrawerActivity().selectedId=4;
//                MainDrawerActivity.bottomNavigation.show(4,false);
//                loadFragment(new FavouriteFragment());
            }
        });

        /*Nested Scroll */
        NestedScrollView scroller = view.findViewById(R.id.nsDashboardFrag);
        if (scroller != null) {
            scroller.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    if (scrollY > oldScrollY) {
                        Log.i("TAG", "Scroll DOWN");
                        fabFAQ.setVisibility(View.GONE);
                        MainDrawerActivity.bottomNavigation.setVisibility(View.GONE);
                    }
                    if (scrollY < oldScrollY) {
                        Log.i("TAG", "Scroll UP");
                        fabFAQ.setVisibility(View.VISIBLE);
                        MainDrawerActivity.bottomNavigation.setVisibility(View.VISIBLE);
                    }

                    if (scrollY == 0) {

                    }

                    if (scrollY == ( v.getMeasuredHeight() - v.getChildAt(0).getMeasuredHeight() )) {
                        Log.i("TAG", "BOTTOM SCROLL");
                    }
                }
            });
        }

        fabFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showFAQDialog();
            }
        });

        container1 = view.findViewById(R.id.container);
        getBannerData();


        return view;
    }

    public void getBannerData(){
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();

        ServiceGenrator.getApiInterface().getSlider().enqueue(new Callback<ResponseGetSlider>() {
            @Override
            public void onResponse(Call<ResponseGetSlider> call, Response<ResponseGetSlider> response) {
                progressDialog.dismiss();
                if(response.body().isStatus()){
                    container1.setVisibility(View.VISIBLE);
                    if(response.body().getSliderResult()!=null){
                        ArrayList<ResponseGetSlider.SliderResult> posters=response.body().getSliderResult();

                        for(int i=0;i<posters.size();i++)
                        {
                            ImageView imageView = new ImageView(getContext());

                            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                            imageView.setLayoutParams(layoutParams);
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                            listOfViews.add(new MomentzView(imageView,5));

                            imgUrls.add(posters.get(i).getBanner_image());
                        }
                        prepareStories();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseGetSlider> call, Throwable t) {
                progressDialog.dismiss();
            }
        });

    }

    private void prepareStories() {

        ConstraintLayout container = getActivity().findViewById(R.id.container);

        storyMoment =  new Momentz(getContext(), listOfViews, container, this, R.drawable.status_color_home_banner);

        storyMoment.start();
    }


    private void showFAQDialog(){

        Dialog faqDialog=new Dialog(requireActivity());
//        faqDialog.setContentView(R.layout.dialog_faq);
        int width = (int)(requireActivity().getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(requireActivity().getResources().getDisplayMetrics().heightPixels*0.90);

        TextView btnShowAll = null; //faqDialog.findViewById(R.id.btnShowAll);
        RecyclerView rvFaq = null; //faqDialog.findViewById(R.id.rvFaq);
        ProgressBar pbFaq = null; //faqDialog.findViewById(R.id.pbFaq);


        faqDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,height);
        faqDialog.getWindow().setGravity(Gravity.BOTTOM);
        faqDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        faqDialog.getWindow().getAttributes().windowAnimations =  R.style.DialogAnimation;
        faqDialog.show();


        //FaqAdapter faqAdapter = new FaqAdapter(getActivity(),faqList);

        callGetFaqListApi(rvFaq,pbFaq);


//        btnShowAll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                faqDialog.dismiss();
//                startActivity(new Intent(getActivity(), FAQListActivity.class));
//
//
//            }
//        });
    }


    public void loadFragment(Fragment fragment) {
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.nav_supplier_fragment,fragment,fragment.getTag()).commit();
    }

    private void callGetFaqListApi(RecyclerView rvFaq,ProgressBar pbFaq){
        faqList.clear();
        pbFaq.setVisibility(View.VISIBLE);
        ServiceGenrator.getApiInterface().getFaqList().enqueue(new Callback<ResponseGetFaqList>() {
            @Override
            public void onResponse(Call<ResponseGetFaqList> call, Response<ResponseGetFaqList> response) {
                if(response.isSuccessful()){
                    pbFaq.setVisibility(View.GONE);
                    if(response.body().isStatus()){
                        rvFaq.setVisibility(View.VISIBLE);
                        if(response.body().getFAQResult()!=null) {

                            if(response.body().getFAQResult().size()>=5){
                                for(int i=0;i<5;i++){
                                    ResponseGetFaqList.FAQResult faqResult = new ResponseGetFaqList.FAQResult();
                                    faqResult.setFaqid(response.body().getFAQResult().get(i).getFaqid());
                                    faqResult.setQuestion(response.body().getFAQResult().get(i).getQuestion());
                                    faqResult.setAnswer(response.body().getFAQResult().get(i).getAnswer());
                                    faqList.add(faqResult);
                                }
                            }else{
                                faqList=response.body().getFAQResult();
                            }
                            //FaqAdapter faqAdapter = new FaqAdapter(getActivity(), faqList);
                            //rvFaq.setAdapter(faqAdapter);

                        }
                    }else{
                        showToast(response.body().getMessage());
                    }
                }else{
                    showToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseGetFaqList> call, Throwable t) {
                pbFaq.setVisibility(View.GONE);
                showToast(t.getMessage());
            }
        });
    }

    private void showToast(String message){
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void done() {

    }

    @Override
    public void onNextCalled(@NonNull View view, @NonNull Momentz momentz, int i) {
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
                                for (int i = 0; i <listOfViews.size(); i++) {
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
       /* String str = imgUrls.get(index);
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
        });*/


    }
}