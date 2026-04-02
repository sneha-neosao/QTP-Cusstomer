package activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import adapters.LabelAdapter;
import adapters.ProductImageSliderAdapter;
import adapters.RecommendedAdapter;
import adapters.ReviewAdapter;
import adapters.VarientAdapter;
//import com.grocery.QTPmart.BuildConfig;
import Config.ApiBaseURL;
import Config.BaseURL;
import ModelClass.ImageModel;
import ModelClass.LabelModel;
import ModelClass.NewCartModel;
import ModelClass.ProductDetailModel;
import ModelClass.ReviewModel;
import ModelClass.VarientModel;

import com.google.firebase.installations.interop.BuildConfig;
import com.grocery.QTPmart.R;
import network.Response.ResProductDetail;
import network.Response.ResReviews;
import network.ServiceGenrator;
import util.AppController;
import util.CommonFunctions;
import util.CustomVolleyJsonRequest;
import util.DatabaseHandler;
import util.Session_management;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static Config.BaseURL.KEY_ID;
import static Config.BaseURL.MyPrefreance;

public class ProductDetailActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener, VarientAdapter.ItemOnClickListener {

    LinearLayout ll_share_others, ll_download, ll_facebook_share, ll_whatsapp_share, ll_blank, llRecommendations,llBack,llProductDetailMAin,llRecentSearch,ll_rating_count;
    ImageView img_label, imgFavorite, img_zoom, img_qtp_choice, img_minus, img_add, img_product;
    SliderView slider_view;
    AppCompatRatingBar ratingBar,ratingBarWithRatingCount;
    TextView txt_prodName, txt_new_price, txt_old_price, txt_rating_count, txt_total_rating,
            txt_rating, txt_review_count, txt_desc, tvItemCount,tvBackToTop,txt_currency,tvCopy,tvViewMoreReview;
    RecyclerView rv_variant, rv_reviews, rv_recommended, rv_recentSearch;
    String itemID = "";
    Integer from;
    ProgressBar pb_average, pb_poor, pb_good, pb_very_good, pb_excellent;
    MaterialCardView cv_review, cv_review_count,cvDetail;
    private List<NewCartModel> topSelling = new ArrayList<>();
    private List<LabelModel> labelModelArrayList = new ArrayList<>();
    private List<NewCartModel> recentSelling = new ArrayList<>();

    private Session_management session_management;
    SharedPreferences sharedPreferences;

    private DatabaseHandler dbcart;

    int itemCount = 0;

    DatabaseHandler dbHandler;

    ArrayList<ImageModel> imageModels = new ArrayList<>();

    private SharedPreferences pref;

    TextView cartCount,tvReview;
    RelativeLayout rlCart;
    ImageView search;

    VarientAdapter varientAdapter;

    String fileUri;
    ArrayList<Uri> listOfUris;
    private static final int PERMISSION_REQUEST_CODE = 200;

    String itemSellingPrice,unitId,itemName,itemId,categoryId,fixedPrice,productImage,mainSupplier,unitValue,shortDescription,stockingType;
    int oldScrollYNes;
    int oldScrollxNes;
    int newScrollYNes;

    NestedScrollView nestedScrollView;

    private long downloadID;

    private long refid;
    ArrayList<Long> list = new ArrayList<>();
    private DownloadManager downloadManager;

    ProductDetailModel productDetailModel;
    ProgressDialog progressDialog;

    String shareTo="";
    String appPackage="";
    ArrayList<Uri> imageUriArray = new ArrayList<Uri>();

    LinearLayout llWas,llNow;
    TextView tvNow,offer_label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_deatail);

        sharedPreferences = getSharedPreferences(MyPrefreance, MODE_PRIVATE);
        session_management = new Session_management(this);

        pref = getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
        pref.registerOnSharedPreferenceChangeListener(this);

        pb_average = findViewById(R.id.pb_average);
        pb_poor = findViewById(R.id.pb_poor);
        pb_good = findViewById(R.id.pb_good);
        pb_very_good = findViewById(R.id.pb_very_good);
        pb_excellent = findViewById(R.id.pb_excellent);

        img_label = findViewById(R.id.img_label);
        imgFavorite = findViewById(R.id.imgFavorite);
        img_zoom = findViewById(R.id.img_zoom);
        img_qtp_choice = findViewById(R.id.img_qtp_choice);
        img_minus = findViewById(R.id.img_minus);
        img_add = findViewById(R.id.img_add);
        img_product = findViewById(R.id.img_product);

        ll_share_others = findViewById(R.id.ll_share_others);
        ll_download = findViewById(R.id.ll_download);
        ll_facebook_share = findViewById(R.id.ll_facebook_share);
        ll_whatsapp_share = findViewById(R.id.ll_whatsapp_share);
//        ll_blank = findViewById(R.id.ll_blank);
        llRecommendations = findViewById(R.id.llRecommendations);
        llProductDetailMAin = findViewById(R.id.llProductDetailMAin);

        slider_view = findViewById(R.id.slider_view);
        ratingBar = findViewById(R.id.ratingBar);

        txt_prodName = findViewById(R.id.txt_prodName);
        txt_currency = findViewById(R.id.tvCurrency);
        txt_new_price = findViewById(R.id.txt_new_price);
        txt_old_price = findViewById(R.id.txt_old_price);
        txt_rating_count = findViewById(R.id.txt_rating_count);
        txt_total_rating = findViewById(R.id.txt_total_rating);
        txt_rating = findViewById(R.id.txt_rating);
        txt_review_count = findViewById(R.id.txt_review_count);
        txt_desc = findViewById(R.id.txt_desc);

        tvItemCount = findViewById(R.id.tvItemCount);

        rv_variant = findViewById(R.id.rv_variant);
        rv_reviews = findViewById(R.id.rv_reviews);
        rv_recommended = findViewById(R.id.rv_recommended);
        rv_recentSearch = findViewById(R.id.rv_recentSearch);

        cvDetail = findViewById(R.id.cvDetail);
        cv_review_count = findViewById(R.id.cv_review_count);
        cv_review = findViewById(R.id.cv_review);
        tvBackToTop = findViewById(R.id.tvBackToTop);
        rlCart = findViewById(R.id.rlCart);

        cartCount = findViewById(R.id.cartCount);
        llRecentSearch = findViewById(R.id.llRecentSearch);

        search = findViewById(R.id.search);
        llBack = findViewById(R.id.llBack);
        tvCopy = findViewById(R.id.tvCopy);
        tvReview = findViewById(R.id.tvReview);
        tvViewMoreReview = findViewById(R.id.tvViewMoreReview);

        ratingBarWithRatingCount = findViewById(R.id.ratingBarWithRatingCount);
        ll_rating_count = findViewById(R.id.ll_rating_count);

        llNow = findViewById(R.id.llNow);
        llWas = findViewById(R.id.llWas);
        tvNow = findViewById(R.id.tvNow);
        offer_label = findViewById(R.id.offer_label);
        ImageView img_logo = findViewById(R.id.img_logo);

        img_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonFunctions.callMainDrawerActivity(ProductDetailActivity.this);
            }
        });

        dbcart = new DatabaseHandler(this);

        dbHandler = new DatabaseHandler(this);

        itemID = getIntent().getStringExtra("itemID");
        from =getIntent().getIntExtra("from",0);

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        /*if (from == 1) {
            ll_blank.setVisibility(View.VISIBLE);
            ProductTabActivity.tabLayout.setVisibility(View.GONE);
        } else {
            ll_blank.setVisibility(View.GONE);
        }*/

        //getProductDetail(itemID);
        getReviews();
        getRecommended();
        recentDeal();
        initBadges();









        /*view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        ProductTabActivity.tabLayout.setVisibility(View.VISIBLE);
                        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                        // Toast.makeText(getActivity(), "Back Pressed", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
                return false;
            }
        });*/

        ArrayList<HashMap<String, String>> map = dbHandler.getCartAll();

//        rlCart.setOnClickListener(view -> {
//            startActivity(new Intent(this, CartActivity.class));
//        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //startActivity(new Intent(ProductDetailActivity.this, SearchActivity.class));
//                startActivity(new Intent(ProductDetailActivity.this,NewSeearchActivity.class)
//                        .putExtra("fromIntent",0));
            }
        });

        llBack.setOnClickListener(view -> {
            finish();
        });

         nestedScrollView = (NestedScrollView)findViewById(R.id.myScroll);

        if (nestedScrollView != null) {

            /*nestedScrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    final int scrollViewHeight = nestedScrollView.getHeight();
                    if (scrollViewHeight > 0) {
                        nestedScrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        final View lastView = nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);
                        final int lastViewBottom = lastView.getBottom() + nestedScrollView.getPaddingBottom();
                        final int deltaScrollY = lastViewBottom - scrollViewHeight - nestedScrollView.getScrollY();
                        *//* If you want to see the scroll animation, call this. *//*
                        //nestedScrollView.smoothScrollBy(0, deltaScrollY);
                        *//* If you don't want, call this. *//*
                        //nestedScrollView.scrollBy(0, deltaScrollY);
                        Log.e("delta",deltaScrollY+"");
                        Log.e("scrollViewHeight",scrollViewHeight+"");
                        tvBackToTop.setVisibility(View.VISIBLE);
                    }
                }
            });*/

            nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    oldScrollxNes=oldScrollX;
                    oldScrollYNes = oldScrollY;
                    Log.e("scrollY",scrollY+"");
                    Log.e("viewHeight",v.getMeasuredHeight()+"");
                    if (scrollY > oldScrollY) {
                        Log.e("TAG", scrollY+"Scroll DOWN"+oldScrollY);
                        //tvBackToTop.setVisibility(View.GONE);

                    }
                    if (scrollY < oldScrollY) {
                        Log.e("TAG", scrollY+"Scroll UP"+oldScrollY);
                       // tvBackToTop.setVisibility(View.GONE);
                    }

                    if (scrollY == 0) {
                        Log.e("TAG", "TOP SCROLL"+scrollY);
                        tvBackToTop.setVisibility(View.GONE);

                    }

                    newScrollYNes=(v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight());
                    if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                        Log.e("TAG", "BOTTOM SCROLL2"+newScrollYNes);
                        tvBackToTop.setVisibility(View.VISIBLE);
                    }

                }
            });
        }




        tvBackToTop.setOnClickListener(view -> {

            ObjectAnimator animator=ObjectAnimator.ofInt(nestedScrollView, "scrollY",0 );
            animator.setDuration(1500);
            animator.start();
           // nestedScrollView.setSmoothScrollingEnabled(true);
           // nestedScrollView.fullScroll(NestedScrollView.FOCUS_UP);

            //nestedScrollView.smoothScrollTo(0,0);
            //nestedScrollView.fullScroll(NestedScrollView.FOCUS_UP);
            Log.e("oldScrollYNes",oldScrollYNes+"");
            /*ObjectAnimator objectAnimator = ObjectAnimator.ofInt(llProductDetailMAin, "scrollY", nestedScrollView.getTop(),0).setDuration(1500);
            objectAnimator.start();*/

           // slideToTop(llProductDetailMAin);
            /*nestedScrollView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    nestedScrollView.fullScroll(View.FOCUS_UP);
                    nestedScrollView.setSmoothScrollingEnabled(true);
                    nestedScrollView.smoothScrollTo(0,0);
                    nestedScrollView.animate().setDuration(10000);
                }
            }, 1000);*/
        });

    }



    public void shareImage(String url) {
        Picasso.get().load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                try {
                    File mydir = new File(Environment.getExternalStorageDirectory() + "/QTP_Share");
                    if (!mydir.exists()) {
                        mydir.mkdirs();
                    }

                    fileUri = mydir.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg";
                    FileOutputStream outputStream = new FileOutputStream(fileUri);

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
                Uri uri= Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), BitmapFactory.decodeFile(fileUri),null,null));
                // use intent to share image
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*");
                share.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(share, "Share Image"));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });
    }

    public void shareMulti(String folderName,String shareTo){

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath().toString()+"/Qtp Share"+ "/"+folderName;
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: "+ files.length);
        ArrayList<Uri> arrayList1 = new ArrayList<>();
        Uri uri=null;
        for (int i = 0; i < files.length; i++)
        {
            Log.d("FilesName", "FileName:" + files[i].getName());

            uri = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()), BuildConfig.APPLICATION_ID + ".provider", files[i]);

            imageUriArray.add(uri);
            arrayList1.add(uri);
        }

        switch (shareTo){
            case "others":
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                intent.putExtra(Intent.EXTRA_STREAM, arrayList1);
                //A content: URI holding a stream of data associated with the Intent, used with ACTION_SEND to supply the data being sent.
                intent.setType("image/*"); //any kind of images can support.
                startActivity(Intent.createChooser(intent, "Send Multiple Images"));
                break;
            case "facebook":
                // use intent to share image
                Intent facebookIntent = new Intent(Intent.ACTION_SEND);
                facebookIntent.setType("image/*");
                facebookIntent.setPackage(appPackage);
                facebookIntent.putExtra(Intent.EXTRA_STREAM, arrayList1);
                startActivity(facebookIntent);
                break;
            case "whatsapp":
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("image/*");
                whatsappIntent.setPackage(appPackage);
               // whatsappIntent.putExtra(Intent.EXTRA_STREAM, arrayList1);
                whatsappIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, arrayList1);
                startActivity(whatsappIntent);
                break;
        }


        // use intent to share image


   /*     File pictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //Get a top-level public external storage directory for placing files of a particular type.
        // This is where the user will typically place and manage their own files,
        // so you should be careful about what you put here to ensure you don't
        // erase their files or get in the way of their own organization...
        // pulled from Standard directory in which to place pictures that are available to the user to the File object

        String[] listOfPictures = pictures.list();
        //Returns an array of strings with the file names in the directory represented by this file. The result is null if this file is not a directory.


        ArrayList<Uri> arrayList = new ArrayList<>();
        if (listOfPictures!=null) {
            for (int i = 0; i < files.length; i++)
            {
                Log.d("Files", "FileName:" + files[i].getName());
            }
            *//*for (String name : listOfPictures) {
                uri = Uri.parse("file://" + pictures.toString() + "/" + name );
                //fileUri = pictures.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg";

               *//**//*uri = FileProvider.getUriForFile(
                        ProductDetailActivity.this,
                        "${applicationId}.provider", //(use your app signature + ".provider" )
                        pictures);*//**//*

                //uri = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()), BuildConfig.APPLICATION_ID + ".provider", pictures);
                arrayList.add(uri);
            }*//*
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
            intent.putExtra(Intent.EXTRA_STREAM, arrayList);
            //A content: URI holding a stream of data associated with the Intent, used with ACTION_SEND to supply the data being sent.
            intent.setType("image/*"); //any kind of images can support.
            startActivity(Intent.createChooser(intent, "Send Multiple Images"));*/
            /*chooser = Intent.createChooser(intent, "Send Multiple Images");//choosers title
            startActivity(chooser);*/



    }

    public void shareMultipleImages(){
        Uri uri1 = Uri.parse("https://cdn.pixabay.com/photo/2015/12/01/20/28/road-1072823_960_720.jpg");
        Uri uri2 = Uri.parse("https://cdn.pixabay.com/photo/2015/12/01/20/28/road-1072823_960_720.jpg");
        Uri uri3 = Uri.parse("https://cdn.pixabay.com/photo/2015/12/01/20/28/road-1072823_960_720.jpg");

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.");
        intent.setType("image/*"); /* This example is sharing jpeg images. */

        ArrayList<Uri> files = new ArrayList<Uri>();
        files.add(uri1);
        files.add(uri2);
        files.add(uri3);

        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
        startActivity(Intent.createChooser(intent , "Share image"));
    }

    public  void slideToTop(View view){
        TranslateAnimation animate = new TranslateAnimation(0,0,0,oldScrollYNes);
        animate.setDuration(1500);
        view.startAnimation(animate);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //animate.setFillAfter(true);
                nestedScrollView.smoothScrollTo(0,0);
                //view.animate().translationX(0).translationY(0).setDuration(2000);
               // LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
               // view.layout(0, 0,0, 0);
                // change the coordinates of the view object itself so that on click listener reacts to new position
                //view.layout(lp.getLeft(), lp.getTop(), lp.getRight()+200, view.getBottom());
                view.clearAnimation();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //nestedScrollView.smoothScrollTo(0,0);
        //view.setVisibility(View.VISIBLE);

        /*Animation slide = AnimationUtils.loadAnimation(ProductDetailActivity.this, R.anim.slide_down_from_top);
        view.startAnimation(slide);*/
    }


    public void onClickCart1(View view) {
//        startActivity(new Intent(this, CartActivity.class));
    }

    private void initBadges() {

        int badgeCount = pref.getInt("cardqnty", 0);
        if (badgeCount > 0) {
            cartCount.setText("" + badgeCount);
            cartCount.setVisibility(View.VISIBLE);
        } else {
            cartCount.setVisibility(View.GONE);
        }

    }

    public void getProductDetail(String itemId) {
        ServiceGenrator.getApiInterface().getProductDetailsByProductId(itemId).enqueue(
                new Callback<ResProductDetail>() {
                    @Override
                    public void onResponse(Call<ResProductDetail> call, Response<ResProductDetail> response) {

                        if (response.isSuccessful()) {

                            if (response.body().getStatus().equals("true")) {
                                if(response.body().getResult()!=null){
                                    productDetailModel = response.body().getResult();
                                    setDetail(productDetailModel);
                                }

                                if(response.body().getLabels()!=null){
                                    labelModelArrayList = response.body().getLabels();
                                }


                                /*for(int i=0;i<productDetailModel.getImages().size();i++){
                                    ImageModel imageModel=new ImageModel();
                                    imageModel.setImagePath(productDetailModel.getImages().get(i).getImagePath());
                                    imageModels.add(imageModel);
                                }*/

                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ResProductDetail> call, Throwable t) {

                    }
                });
    }

    public void getReviews() {

        ServiceGenrator.getApiInterface().getProductReview(itemID, "0").enqueue(
                new Callback<ResReviews>() {
                    @Override
                    public void onResponse(Call<ResReviews> call, Response<ResReviews> response) {

                        if (response.isSuccessful()) {

                            if (response.body().isStatus()) {
                                LinearLayoutManager layoutManager
                                        = new LinearLayoutManager(ProductDetailActivity.this, LinearLayoutManager.VERTICAL, false);
                                ArrayList<ReviewModel> reviewModelList = response.body().getReviewList();
                                ReviewAdapter reviewAdapter = new ReviewAdapter(ProductDetailActivity.this, reviewModelList);
                                rv_reviews.setAdapter(reviewAdapter);
                                rv_reviews.setLayoutManager(layoutManager);

                                if(response.body().getReviewList()!=null&&response.body().getReviewList().size()>4){
                                    tvViewMoreReview.setVisibility(View.VISIBLE);
                                }else{
                                    tvViewMoreReview.setVisibility(View.GONE);
                                }

                                if (!reviewModelList.isEmpty()) {
                                    cv_review.setVisibility(View.VISIBLE);
                                }else{
                                    cv_review.setVisibility(View.GONE);
                                }

                                if (!response.body().getResult().getTotalRating().equals("0")) {
                                    cv_review_count.setVisibility(View.VISIBLE);
                                }else{
                                    cv_review_count.setVisibility(View.GONE);
                                }


                                pb_excellent.setProgress(Integer.parseInt(response.body().getResult().getExcellent()));
                                pb_very_good.setProgress(Integer.parseInt(response.body().getResult().getVeryGood()));
                                pb_good.setProgress(Integer.parseInt(response.body().getResult().getGood()));
                                pb_average.setProgress(Integer.parseInt(response.body().getResult().getAverage()));
                                pb_poor.setProgress(Integer.parseInt(response.body().getResult().getPoor()));

                                txt_total_rating.setText(response.body().getResult().getTotalRating());
                                txt_rating.setText(response.body().getResult().getRatings()+" Ratings");
                                txt_review_count.setText(response.body().getResult().getReviews()+" Reviews");

                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ResReviews> call, Throwable t) {

                    }
                });
    }

    public void setDetail(ProductDetailModel productDetailModel) {



        imageModels = new ArrayList<>();
        /*get  product images list*/
        if(productDetailModel.getImages()!=null || !productDetailModel.getImages().isEmpty()){
            imageModels = productDetailModel.getImages();
        }

        /*Add main product image at position 0*/
        if(productDetailModel.getImage()!=null || !productDetailModel.getImage().isEmpty()){
            ImageModel imageModel = new ImageModel();
            imageModel.setImagePath(productDetailModel.getImage());
            imageModels.add(0,imageModel);
        }

        /*Add variant images*/
        ImageModel imageModel2 = new ImageModel();
        for(int j=0;j<productDetailModel.getVariants().size();j++){
            if(!productDetailModel.getVariants().get(j).getImage().isEmpty()||!productDetailModel.getVariants().get(j).getImage().equals("")){
                imageModel2.setImagePath(productDetailModel.getVariants().get(j).getImage());
                imageModels.add(j+1,imageModel2);
            }

        }
       /* if(imageModel2.getImagePath()!=null){
            for(int i=1;i<=productDetailModel.getVariants().size();i++){
                imageModels.add(i,imageModel2);
            }
        }*/




        Log.e("imageMOdel",imageModels.toString());
        /*Product Image slider*/
        ProductImageSliderAdapter adapter = new ProductImageSliderAdapter(ProductDetailActivity.this, imageModels);
        slider_view.setSliderAdapter(adapter);
        slider_view.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        slider_view.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);

        Picasso.get().load(productDetailModel.getImage()).
                error(R.mipmap.ic_launcher).into(img_product);

        /*Varient*/
        LinearLayoutManager layoutManager = new LinearLayoutManager(ProductDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
        varientAdapter = new VarientAdapter(ProductDetailActivity.this, productDetailModel.getVariants(),ProductDetailActivity.this);
        rv_variant.setLayoutManager(layoutManager);
        rv_variant.setAdapter(varientAdapter);



        /*ImageModel imageModel1 = new ImageModel();
        ImageModel imageModel2 = new ImageModel();
        ImageModel imageModel3 = new ImageModel();
        ImageModel imageModel4 = new ImageModel();
        imageModel1.setImagePath(productDetailModel.getImage());
        imageModel2.setImagePath("http://qtp.ae/QTPMobileApp/images/Product/342_Applegolden_1.PNG");
        imageModel3.setImagePath("http://qtp.ae/QTPMobileApp/images/Product/338_AppleRed_1.PNG");
        imageModel4.setImagePath("http://qtp.ae/QTPMobileApp/images/Product/4877_APRICOTJORDAN_1.PNG");
        imageModels.add(imageModel1);
        imageModels.add(imageModel2);
        imageModels.add(imageModel3);
        imageModels.add(imageModel4);*/

       /* img_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               *//* getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_supplier_fragment, fragment).commitAllowingStateLoss();
                *//*
                Fragment homepage = new ViewPhotoFragment();
                FragmentTransaction fragmentManager =getActivity().getSupportFragmentManager()
                        .beginTransaction();
                Bundle bundle=new Bundle();
                bundle.putString("imgPath", productDetailModel.getImage()); //key and value
                if(imageModels!=null){
                    bundle.putSerializable("imageList",imageModels);
                }
                homepage.setArguments(bundle);
                fragmentManager.replace(R.id.co_product, homepage);
                fragmentManager.addToBackStack(null);
                fragmentManager.commit();
            }
        });*/

        /*Product data*/
       // String itemSellingPrice,unitId,itemName,itemId,categoryId,fixedPrice,productImage,mainSupplier,unitValue,shortDescription;

        itemSellingPrice = productDetailModel.getItemSellingprice();
        unitId = productDetailModel.getUnitID();
        itemName = productDetailModel.getItemName();
        itemId = productDetailModel.getItemID();
        categoryId = productDetailModel.getCategoryID();
        fixedPrice = productDetailModel.getFixedPrice();
        productImage = productDetailModel.getImage();
        mainSupplier = productDetailModel.getMainSupplier();
        unitValue = productDetailModel.getUom();
        shortDescription = productDetailModel.getShortDes();
        stockingType = productDetailModel.getStockingType();


        /*Zoom image click*/
        img_zoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ProductDetailActivity.this,ViewProductImageActivity.class);
//                intent.putExtra("imgPath", productDetailModel.getImage());
//                if (imageModels != null) {
//                    intent.putExtra("imageList", imageModels);
//                }
//                startActivity(intent);

            }
        });


        /*Download icon click*/
        ll_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //downloadFile(productDetailModel.getImage(), ProductDetailActivity.this);

                if (checkPermission()) {
                    //downloadImage(productDetailModel.getImage());
                    for (int i = 0; i < imageModels .size(); i++) {
                        downloadImage(imageModels.get(i).getImagePath(),productDetailModel.getItemName(), productDetailModel.getItemName()+i);
                    }
                } else {
                    requestPermission();
                }
            }
        });

        /*Set Product Name*/
        txt_prodName.setText(productDetailModel.getItemName());
        /*Set Product description*/
        if(productDetailModel.getShortDes()!=null&&!productDetailModel.getShortDes().isEmpty()){
            cvDetail.setVisibility(View.VISIBLE);
            txt_desc.setText(productDetailModel.getShortDes());
        }else{
            cvDetail.setVisibility(View.GONE);
        }

        /*Set Selling price*/
        /*if(session_management.getCurrency()!=null||!session_management.getCurrency().isEmpty()){
            Log.e("Currnc",session_management.getCurrency());
            txt_currency.setText(session_management.getCurrency());
        }*/
        txt_currency.setText(session_management.getCurrency());
        txt_new_price.setText(productDetailModel.getItemSellingprice());

        /*Set Fixed price*/
        if (productDetailModel.getFixedPrice() != null && Double.parseDouble(productDetailModel.getFixedPrice())>0.00) {
            llWas.setVisibility(View.VISIBLE);
            tvNow.setVisibility(View.VISIBLE);
            txt_old_price.setVisibility(View.VISIBLE);
            txt_old_price.setText(session_management.getCurrency()+" "+productDetailModel.getFixedPrice());
            txt_old_price.setPaintFlags(txt_old_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            llWas.setVisibility(View.GONE);
            tvNow.setVisibility(View.GONE);
            txt_old_price.setVisibility(View.GONE);
        }

        /*if(!productDetailModel.getFixedPrice().equals("0.00")){
            txt_old_price.setVisibility(View.VISIBLE);
            txt_old_price.setText(productDetailModel.getFixedPrice());
            txt_old_price.setPaintFlags(txt_old_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            txt_old_price.setVisibility(View.GONE);
        }*/

        /*Prodcuct label*/
        if (productDetailModel.getProductLabel() != null && !productDetailModel.getProductLabel().isEmpty()) {
            Picasso.get()
                    .load(productDetailModel.getProductLabel())
                    .placeholder(R.drawable.product_1)
                    .into(img_label);

            img_label.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog=new Dialog(ProductDetailActivity.this);
                    dialog.setContentView(R.layout.alert_dialog_categories);
                    RecyclerView rv_labels=dialog.findViewById(R.id.rv_labels);
                    ImageView imageView=dialog.findViewById(R.id.close);

                    if(labelModelArrayList!=null){
                        List<LabelModel> labelModels = labelModelArrayList;
                        rv_labels.setLayoutManager(new GridLayoutManager(ProductDetailActivity.this, 2));
                        LabelAdapter labelAdapter = new LabelAdapter(labelModels,ProductDetailActivity.this);
                        rv_labels.setAdapter(labelAdapter);
                        labelAdapter.notifyDataSetChanged();
                    }


                    int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
                    int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);

                    dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,height);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                    dialog.show();

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            });
        } else {
            img_label.setVisibility(View.GONE);
        }

        /*Customer Rating count*/
        if(productDetailModel.getCustomerRating()!=null && !productDetailModel.getCustomerRating().equals("null")
                &&!productDetailModel.getCustomerRating().equals("0")){
            ll_rating_count.setVisibility(View.VISIBLE);
            ratingBarWithRatingCount.setRating(Float.parseFloat(productDetailModel.getCustomerRating()));
            if(!productDetailModel.getRatingUserCount().equals("0")){
                txt_rating_count.setText(productDetailModel.getCustomerRating() + "(" + productDetailModel.getRatingUserCount() + ")");
                tvReview.setText("Reviews " + "(" + productDetailModel.getRatingUserCount() + ")");
            }
            else{
                txt_rating_count.setText(productDetailModel.getCustomerRating());
                tvReview.setText("Reviews ");
            }

        }else{
            ll_rating_count.setVisibility(View.GONE);
        }


        /*Set Offer*/
        if (productDetailModel.getFixedPrice() != null && Double.parseDouble(productDetailModel.getFixedPrice())>0) {

            if(productDetailModel.getDiscount()!=null) {
               offer_label.setText(productDetailModel.getDiscount() + "% OFF");
               offer_label.setVisibility(View.VISIBLE);
            }
            else {
               offer_label.setVisibility(View.GONE);
            }
        }
        else
        {
            offer_label.setVisibility(View.GONE);
        }


        /*Rating bar and Qtp chice image*/
        if (productDetailModel.getAdminRating()!=null && !productDetailModel.getAdminRating().equals("null")
                && !productDetailModel.getAdminRating().equals("0.0")) {
            ratingBar.setVisibility(View.VISIBLE);
            img_qtp_choice.setVisibility(View.VISIBLE);

            ratingBar.setRating(Float.parseFloat(productDetailModel.getAdminRating()));
        } else {
            ratingBar.setVisibility(View.GONE);
            img_qtp_choice.setVisibility(View.GONE);
        }


        /*Set Item Count*/
        int qtyd = Integer.parseInt(dbcart.getInCartItemQtys(unitId));
        if (qtyd > 0) {
            tvItemCount.setText(String.valueOf(qtyd));
        } else {
            tvItemCount.setText("0");
        }

        /*Plus icon click*/
        img_add.setOnClickListener(view -> {
            try {
                if (dbHandler == null) {
                    dbHandler = new DatabaseHandler(ProductDetailActivity.this);
                }
               // double price = Double.parseDouble(productDetailModel.getItemSellingprice());
                //int i = Integer.parseInt(dbHandler.getInCartItemQtys(productDetailModel.getUnitID()));
                double price = Double.parseDouble(itemSellingPrice);
                int i = Integer.parseInt(dbHandler.getInCartItemQtys(unitId));
                Log.e("item_id", i + "");
                tvItemCount.setText("" + (i + 1));
                String p = String.format("%.2f", (price * (i + 1)));
               // updateMultiply(productDetailModel, (i + 1));
                updateMultiply1( (i + 1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        /*Minus icon click*/
        img_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Integer.parseInt(dbHandler.getInCartItemQtys(unitId));
                double price = Double.parseDouble(itemSellingPrice);
                if ((i - 1) < 0 || (i - 1) == 0) {
                    tvItemCount.setText("" + 0);
                    String p = String.format("%.2f", price);
                } else {
                    tvItemCount.setText("" + (i - 1));

                }
                //updateMultiply(productDetailModel, (i - 1));
                if(!tvItemCount.getText().equals("0")) {
                    updateMultiply1((i - 1));
                }

                /*int i = Integer.parseInt(dbcart.getInCartItemQtys(unitId));
                if ((i - 1) > 0) {
                    tvItemCount.setText("" +(i - 1));
                    img_minus.setEnabled(true);
                } else {
                    tvItemCount.setText("" +0);
                   // tvItemCount.setVisibility(View.GONE);
                    //img_minus.setVisibility(View.GONE);
                    img_minus.setEnabled(false);
                }
                updateMultiply1(i - 1);*/
            }
        });

        /*Share icon click*/
        ll_share_others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareTo ="others";
                /*Dialog dialog = new Dialog(ProductDetailActivity.this);
                dialog.setContentView(R.layout.alert_dialog_share_image);

                int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
                int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);

                dialog.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);

                dialog.show();*/

                /*Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + "http://qtp.ae/QTPMobileApp/images/Product/342_Applegolden_1.PNG"));
                try {
                    startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    //ToastHelper.MakeShortText("Whatsapp have not been installed.");
                }*/
                if (checkPermission()) {
                   //shareImage(imageModels.get(0).getImagePath());
                    /*for (int i = 0; i < imageModels .size(); i++) {
                        downloadShareImage(imageModels.get(i).getImagePath(),productDetailModel.getItemName(), productDetailModel.getItemName()+i);
                    }*/

                    downloadShareImage(productDetailModel.getItemName());
                    //shareMulti(productDetailModel.getItemName());

                    //shareMulti("Capsicum Red (Europe / Egypt)");

                    //share(this,);

                    /*ArrayList<Uri> imageUris = new ArrayList<Uri>();
                    Uri imageUri = Uri.parse("https://tinyjpg.com/images/social/website.jpg");
                    imageUris.add(imageUri); // Add your image URIs here
                    imageUris.add(imageUri);

                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                    shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
                    shareIntent.setType("image/*");
                    startActivity(Intent.createChooser(shareIntent, null));*/
                } else {
                    requestPermission();
                }

                //shareMultipleImages();


            }
        });

        /*Share Whatsapp click*/
        ll_whatsapp_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareTo ="whatsapp";
                if (checkPermission()) {
                    if (isAppInstalled(ProductDetailActivity.this, "com.whatsapp.w4b")) {
                    appPackage = "com.whatsapp.w4b";
                    downloadShareImage(productDetailModel.getItemName());
                    //do ...
                } else if (isAppInstalled(ProductDetailActivity.this, "com.whatsapp")) {
                        appPackage = "com.whatsapp";
                        downloadShareImage(productDetailModel.getItemName());
                    }else {
                        Toast.makeText(ProductDetailActivity.this, "whatsApp is not installed", Toast.LENGTH_LONG).show();
                    }
                } else {
                    requestPermission();
                }
               /* Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.whatsapp");
                startActivity(launchIntent);

                String appPackage="";
                if (isAppInstalled(ProductDetailActivity.this, "com.whatsapp.w4b")) {
                    appPackage = "com.whatsapp.w4b";
                    //do ...
                } else if (isAppInstalled(ProductDetailActivity.this, "com.whatsapp")) {
                    appPackage = "com.whatsapp";
                    *//*Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                    sendIntent.setType("text/plain");
                    sendIntent.setPackage(appPackage);
                    startActivity(sendIntent);*//*

                    imageModels.get(0).getImagePath();
                    Uri uri1 = Uri.parse("android.resource://com.code2care.example.whatsappintegrationexample/drawable/image1");
                    Uri uri2 = Uri.parse("android.resource://com.code2care.example.whatsappintegrationexample/drawable/image2");
                    Uri uri3 = Uri.parse("android.resource://com.code2care.example.whatsappintegrationexample/drawable/image3");

                    ArrayList<Uri> imageUriArray = new ArrayList<Uri>();
                    imageUriArray.add(uri1);
                    imageUriArray.add(uri2);
                    Uri imgUri = Uri.parse("http://qtp.ae/QTPMobileApp/images/Product/342_Applegolden_1.PNG");
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, "Text caption message!!");
                    intent.setType("text/plain");
                    intent.setType("image/*");
                    intent.setPackage("com.whatsapp");
                    intent.putExtra(Intent.EXTRA_STREAM, imgUri);
                    //intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, (ArrayList<? extends Parcelable>)imageUriArray);
                    startActivity(intent);
                    //do ...
                } else {
                    Toast.makeText(ProductDetailActivity.this, "whatsApp is not installed", Toast.LENGTH_LONG).show();
                }*/



                /*Picasso.with(getApplicationContext()).load(imageModels.get(0).getImagePath()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        try {
                            File mydir = new File(Environment.getExternalStorageDirectory() + "/QTP_WhatsApp");
                            if (!mydir.exists()) {
                                mydir.mkdirs();
                            }

                            fileUri = mydir.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg";
                            FileOutputStream outputStream = new FileOutputStream(fileUri);

                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                            outputStream.flush();
                            outputStream.close();
                        } catch(IOException e) {
                            e.printStackTrace();
                        }
                        Uri uri= Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), BitmapFactory.decodeFile(fileUri),null,null));
                        // use intent to share image
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("image/*");
                        share.setPackage("com.whatsapp");
                        share.putExtra(Intent.EXTRA_STREAM, uri);
                        startActivity(share);
                        //startActivity(Intent.createChooser(share, "Share Image"));
                    }
                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                    }
                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });*/

            }
        });


        /*Facebook icon click*/
        ll_facebook_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareTo ="facebook";
                if (checkPermission()) {
                    if (isAppInstalled(ProductDetailActivity.this, "com.facebook.katana")) {
                        appPackage = "com.facebook.katana";
                        downloadShareImage(productDetailModel.getItemName());
                    }else {
                        Toast.makeText(ProductDetailActivity.this, "Facebook is not installed", Toast.LENGTH_LONG).show();
                    }
                } else {
                    requestPermission();
                }


                /*if (isFacebookAppInstalled()) {
                    Picasso.with(getApplicationContext()).load(imageModels.get(0).getImagePath()).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            try {
                                File mydir = new File(Environment.getExternalStorageDirectory() + "/QTP_Facebook");
                                if (!mydir.exists()) {
                                    mydir.mkdirs();
                                }

                                fileUri = mydir.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg";
                                FileOutputStream outputStream = new FileOutputStream(fileUri);

                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                                outputStream.flush();
                                outputStream.close();
                            } catch(IOException e) {
                                e.printStackTrace();
                            }
                            Uri uri= Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), BitmapFactory.decodeFile(fileUri),null,null));
                            // use intent to share image
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("image/*");
                            share.setPackage("com.facebook.katana");
                            share.putExtra(Intent.EXTRA_STREAM, uri);
                            startActivity(share);
                            //startActivity(Intent.createChooser(share, "Share Image"));
                        }
                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                        }
                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "facebook app not installing", Toast.LENGTH_SHORT).show();
                }*/

            }
        });


        /*Text view copy click*/
        tvCopy.setOnClickListener(view -> {

            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label",txt_desc.getText().toString() );
            clipboard.setPrimaryClip(clip);

            Snackbar.make(this.findViewById(android.R.id.content),"Copied",Snackbar.LENGTH_SHORT).show();

        });

        /*Favourite*/
        favouriteState(productDetailModel);
        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(session_management.isLoggedIn()){
                    addToFav(productDetailModel.getItemID(), productDetailModel);
                }
                else {
                    Intent intent = new Intent(ProductDetailActivity.this, LoginActivity.class);
                    startActivity(intent);

                }
            }
        });

    }

    public boolean isFacebookAppInstalled() {
        try {
            getApplicationContext().getPackageManager().getApplicationInfo("com.facebook.katana", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private boolean isAppInstalled(Context ctx, String packageName) {
        PackageManager pm = ctx.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    private void updateMultiply(ProductDetailModel productDetailModel, int i) {
        HashMap<String, String> map = new HashMap<>();
        map.put("varient_id", productDetailModel.getUnitID());
        map.put("product_name", productDetailModel.getItemName());
        map.put("category_id", productDetailModel.getCategoryID());
        map.put("ItemId", productDetailModel.getItemID());
        map.put("title", productDetailModel.getItemName());
        map.put("price", productDetailModel.getItemSellingprice());
        map.put("mrp", productDetailModel.getFixedPrice());
        map.put("product_image", productDetailModel.getImage());
        map.put("status", "0");
        map.put("in_stock", "");
        map.put("increament", "0");
        map.put("supplierID", productDetailModel.getMainSupplier());
        map.put("unit_value", productDetailModel.getUom());
        map.put("product_description", productDetailModel.getShortDes());
        map.put("unitID", productDetailModel.getUnitID());
        if (i > 0) {
            if (dbcart.isInCart(map.get("varient_id"))) {
                if (session_management.isLoggedIn()) {
                    addToCart(map, i, "update");
                } else {
                    dbcart.setCart(map, i);
                    updateSharedPref();
                }

            } else {
                if (session_management.isLoggedIn()) {
                    addToCart(map, i, "add");
                } else {
                    dbcart.setCart(map, i);
                    updateSharedPref();

                }
            }
        } else {
            if (session_management.isLoggedIn()) {
                addToCart(map, i, "delete");
            } else {
                dbcart.removeItemFromCart(map.get("varient_id"));
            }
        }
    }

    private void updateMultiply1(int i) {
        HashMap<String, String> map = new HashMap<>();
        map.put("varient_id", unitId);
        map.put("product_name", itemName);
        map.put("category_id", categoryId);
        map.put("ItemId", itemId);
        map.put("title", itemName);
        map.put("price",itemSellingPrice);
        map.put("mrp", fixedPrice);
        map.put("product_image", productImage);
        map.put("status", "0");
        map.put("stock", stockingType);
        map.put("increament", "0");
        map.put("supplierID", mainSupplier);
        map.put("unit_value", unitValue);
        map.put("product_description", shortDescription);
        map.put("unitID", unitId);

        if (i > 0) {
            if (dbcart.isInCart(map.get("varient_id"))) {
                if (session_management.isLoggedIn()) {
                    addToCart(map, i, "update");
                } else {
                    dbcart.setCart(map, i);
                    updateSharedPref();
                }

            } else {
                if (session_management.isLoggedIn()) {
                    addToCart(map, i, "add");
                } else {
                    dbcart.setCart(map, i);
                    updateSharedPref();

                }
            }
        } else {
            if (session_management.isLoggedIn()) {
                addToCart(map, i, "delete");
            } else {
                dbcart.removeItemFromCart(map.get("varient_id"));
            }
        }
    }

    private void updateMultiply2(ProductDetailModel productDetailModel, int i) {
        try {
            Log.i("Product_id", productDetailModel.getItemID());
            Log.d("supplier_id", "" + productDetailModel.getMainSupplier());
            HashMap<String, String> map = new HashMap<>();
            map.put("varient_id", productDetailModel.getUnitID());
            map.put("product_name", productDetailModel.getItemName());
            map.put("category_id", productDetailModel.getCategoryID());
            map.put("title", productDetailModel.getItemName());
            map.put("price", productDetailModel.getItemSellingprice());
            map.put("product_image", productDetailModel.getImage());
            map.put("status", "");
            map.put("in_stock", "");
            map.put("unit_value", productDetailModel.getUom());
            map.put("vatRate", productDetailModel.getVatRate());
            map.put("increament", "0");
            map.put("supplierID", productDetailModel.getMainSupplier());
            map.put("product_description", productDetailModel.getShortDes());
            map.put("unitID", productDetailModel.getUnitID());
            map.put("ItemId", productDetailModel.getItemID());

            if (i > 0) {
                if (dbcart.isInCart(map.get("varient_id"))) {
                    if (session_management.isLoggedIn()) {
                        addToCart(map, i, "update");
                    } else {
                        dbcart.setCart(map, i);
                    }
                } else {
                    if (session_management.isLoggedIn()) {
                        addToCart(map, i, "add");
                    } else {
                        dbcart.setCart(map, i);
                    }
                }
            } else {
                if (session_management.isLoggedIn()) {
                    addToCart(map, i, "delete");
                } else {
                    dbcart.removeItemFromCart(map.get("varient_id"));
                }
            }
            updateSharedPref();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    void updateSharedPref() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                SharedPreferences preferences = getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                preferences.edit().putInt("cardqnty", dbcart.getCartCount()).apply();
                //categorygridquantity.onCartItemAddOrMinus();
            }
        } catch (IndexOutOfBoundsException e) {
            Log.d("qwer", e.toString());
        }
    }

    public void getRecommended() {
        topSelling.clear();
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiBaseURL.recommended, response -> {
//            Log.e("recommended", response);
//
//            try {
//
//                JSONObject jsonObjectResponse = new JSONObject(response);
//                boolean status = jsonObjectResponse.getBoolean("status");
//                if (status) {
//                    JSONArray jsonArray = jsonObjectResponse.getJSONArray("result");
//                    List<NewCartModel> listorl = new ArrayList<>();
//                    // JSONArray jsonArrayLabel = jsonObjectResponse.getJSONArray("labels");
//                    List<LabelModel> listLabel = new ArrayList<>();
//                    /*for (int i = 0; i < jsonArrayLabel.length(); i++) {
//                        JSONObject jsonObject = jsonArrayLabel.getJSONObject(i);
//                        LabelModel labelModel = new LabelModel();
//                        labelModel.setLableText(jsonObject.getString("lableText"));
//                        labelModel.setImagePath(jsonObject.getString("imagePath"));
//
//                        listLabel.add(labelModel);
//                    }*/
//
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        NewCartModel topModel = new NewCartModel();
//                        topModel.setProduct_id(jsonObject.getString("itemID"));
//                        topModel.setProduct_name(jsonObject.getString("itemName"));
//                        topModel.setDescription(jsonObject.getString("shortDes"));
//                        topModel.setProduct_image(jsonObject.getString("image"));
//                        topModel.setUnit(jsonObject.getString("uom"));
//                        topModel.setMainSupplier(jsonObject.getString("mainSupplier"));
//                        topModel.setVatRate(jsonObject.getString("vatRate"));
//                        topModel.setFeedback(jsonObject.getString("adminRating"));
//                        topModel.setDiscount(jsonObject.getString("discount"));
//
//                        topModel.setUnitID(jsonObject.getString("unitID"));
//                        topModel.setUomId(jsonObject.getString("uomId"));
//                        topModel.setAdminRating(jsonObject.getString("adminRating"));
//                        topModel.setStockingType(jsonObject.getString("stockingType"));
//                        topModel.setStockingType(jsonObject.getString("stockingType"));
//                        topModel.setCustomerRating(jsonObject.getString("customerRating"));
//                        topModel.setRatingUserCount(jsonObject.getString("ratingUserCount"));
//                        topModel.setProductLabel(jsonObject.getString("productLabel"));
//                        topModel.setCategoryID(jsonObject.getString("categoryID"));
//                        topModel.setItemSubCategory(jsonObject.getString("itemSubCategory"));
//
//
//                        topModel.setVarient_id("0");
//
//                        if (jsonObject.getString("fixedPrice") != null && Double.parseDouble(jsonObject.getString("fixedPrice")) > 0) {
//                            topModel.setItemSellingprice(jsonObject.getString("fixedPrice"));
//                            topModel.setFixedPrice(jsonObject.getString("itemSellingprice"));
//                        } else {
//                            topModel.setFixedPrice(jsonObject.getString("fixedPrice"));
//                            topModel.setItemSellingprice(jsonObject.getString("itemSellingprice"));
//                        }
//                        listorl.add(topModel);
//                    }
//
//                    //  screenLists.add(new MainScreenList("TOP SELLING", topSelling, recentSelling, dealOftheday, whatsNew, SubCategoryActivity.subcateList,labelModelArrayList));
//
//                    topSelling.addAll(listorl);
//                    if (topSelling.isEmpty()) {
//                        llRecommendations.setVisibility(View.GONE);
//                    } else {
//                        llRecommendations.setVisibility(View.VISIBLE);
//                    }
//                    LinearLayoutManager layoutManager
//                            = new LinearLayoutManager(ProductDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
//                    rv_recommended.setLayoutManager(layoutManager);
//                    rv_recommended.setAdapter(new RecommendedAdapter(ProductDetailActivity.this, topSelling, rv_recommended));
//                    //  labelModelArrayList.addAll(listLabel);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } finally {
//                // DealOfTheDay();// recentDeal();
//            }
//
//        }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // DealOfTheDay();// recentDeal();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
////                params.put("lat", session_management.getLatPref());
////                params.put("lng", session_management.getLangPref());
////                params.put("city", session_management.getLocationCity());
//                return params;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(ProductDetailActivity.this);
//        requestQueue.getCache().clear();
//        stringRequest.setRetryPolicy(new RetryPolicy() {
//            @Override
//            public int getCurrentTimeout() {
//                return 60000;
//            }
//
//            @Override
//            public int getCurrentRetryCount() {
//                return 0;
//            }
//
//            @Override
//            public void retry(VolleyError error) throws VolleyError {
//
//            }
//        });
//        requestQueue.add(stringRequest);
    }

    private void recentDeal() {
        recentSelling.clear();
        String custId = "";
        if (session_management.isLoggedIn()) {
            custId = session_management.getUserDetails().get(KEY_ID);
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiBaseURL.topRecentViewsAll + "?custId=" + custId, response -> {
            Log.d("topRecentViews", response);
            try {

                JSONObject jsonObjectResponse = new JSONObject(response);
                boolean status = jsonObjectResponse.getBoolean("status");
                if (status) {
                    JSONArray jsonArray = jsonObjectResponse.getJSONArray("result");
                    List<NewCartModel> listorl = new ArrayList<>();
                    // JSONArray jsonArrayLabel = jsonObjectResponse.getJSONArray("labels");
                    List<LabelModel> listLabel = new ArrayList<>();
                    /*for (int i = 0; i < jsonArrayLabel.length(); i++) {
                        JSONObject jsonObject = jsonArrayLabel.getJSONObject(i);
                        LabelModel labelModel = new LabelModel();
                        labelModel.setLableText(jsonObject.getString("lableText"));
                        labelModel.setImagePath(jsonObject.getString("imagePath"));

                        listLabel.add(labelModel);
                    }*/

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        NewCartModel topModel = new NewCartModel();
                        topModel.setProduct_id(jsonObject.getString("itemID"));
                        topModel.setProduct_name(jsonObject.getString("itemName"));
                        topModel.setDescription(jsonObject.getString("shortDes"));
                        topModel.setProduct_image(jsonObject.getString("image"));
                        topModel.setUnit(jsonObject.getString("uom"));
                        topModel.setMainSupplier(jsonObject.getString("mainSupplier"));
                        topModel.setVatRate(jsonObject.getString("vatRate"));
                        topModel.setFeedback(jsonObject.getString("adminRating"));
                        topModel.setDiscount(jsonObject.getString("discount"));

                        topModel.setUnitID(jsonObject.getString("unitID"));
                        topModel.setUomId(jsonObject.getString("uomId"));
                        topModel.setAdminRating(jsonObject.getString("adminRating"));
                        topModel.setStockingType(jsonObject.getString("stockingType"));
                        topModel.setStockingType(jsonObject.getString("stockingType"));
                        topModel.setCustomerRating(jsonObject.getString("customerRating"));
                        topModel.setRatingUserCount(jsonObject.getString("ratingUserCount"));
                        topModel.setProductLabel(jsonObject.getString("productLabel"));
                        topModel.setCategoryID(jsonObject.getString("categoryID"));
                        topModel.setItemSubCategory(jsonObject.getString("itemSubCategory"));


                        topModel.setVarient_id("0");

                        if (jsonObject.getString("fixedPrice") != null && Double.parseDouble(jsonObject.getString("fixedPrice")) > 0) {
                            topModel.setItemSellingprice(jsonObject.getString("fixedPrice"));
                            topModel.setFixedPrice(jsonObject.getString("itemSellingprice"));
                        } else {
                            topModel.setFixedPrice(jsonObject.getString("fixedPrice"));
                            topModel.setItemSellingprice(jsonObject.getString("itemSellingprice"));
                        }
                        listorl.add(topModel);
                    }

                    //  screenLists.add(new MainScreenList("TOP SELLING", topSelling, recentSelling, dealOftheday, whatsNew, SubCategoryActivity.subcateList,labelModelArrayList));

                    recentSelling.addAll(listorl);

                    if (recentSelling.isEmpty()) {
                        llRecentSearch.setVisibility(View.GONE);
                    } else {
                        llRecentSearch.setVisibility(View.VISIBLE);
                    }
                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(ProductDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    rv_recentSearch.setLayoutManager(layoutManager);
                    rv_recentSearch.setAdapter(new RecommendedAdapter(ProductDetailActivity.this, recentSelling, rv_recentSearch));
                    //rv_recentSearch.setAdapter(new RecommendedAdapter(ProductDetailActivity.this, recentSelling, rv_recentSearch));
                    //  labelModelArrayList.addAll(listLabel);
                }else{
                    llRecentSearch.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                // DealOfTheDay();// recentDeal();
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // DealOfTheDay();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//                params.put("lat", session_management.getLatPref());
//                params.put("lng", session_management.getLangPref());
//                params.put("city", session_management.getLocationCity());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ProductDetailActivity.this);
        requestQueue.getCache().clear();
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(stringRequest);
    }

    public void downloadFile(String uRl, Context context) {
        /*File myDir = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOWNLOADS),"/QTP");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }*/


        /*DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(uRl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        // request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"/QTP");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        long reference = manager.enqueue(request);
        manager.enqueue(request);*/

        Log.e("ProductDetailFragment", "downloadFile: " + uRl);

       /* DownloadManager mgr = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(uRl);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);

        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE).setAllowedOverMetered(true)
                .setAllowedOverRoaming(true).setTitle("QTP - " + "Downloading " + uRl)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);

                mgr.enqueue(request);*/


        //.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, uRl)
        /*String folder = "/QTP";
        try { //V28 Below
            request.setDestinationInExternalPublicDir(folder, uRl);//v 28 allow to create and it deprecated method(v28+)

        } catch (Exception e) {

            //For Android  28+
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, uRl);//(Environment.DIRECTORY_PICTURES,"picname.jpeg")
        }
*/


    }

    private void updateMultiply(int pos, int i) {
        ArrayList<HashMap<String, String>> map = dbHandler.getCartAll();
        try {
            if (i > 0) {
                if (session_management.isLoggedIn()) {
                    addToCart(map.get(pos), i, "update");
                } else {
                    dbHandler.setCart(map.get(pos), i);
                }
            } else {
                if (session_management.isLoggedIn()) {
                    addToCart(map.get(pos), i, "delete");
                } else {
                    dbHandler.removeItemFromCart(map.get(pos).get("varient_id"));
                    //list.remove(pos);
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                SharedPreferences preferences = getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                preferences.edit().putInt("cardqnty", dbHandler.getCartCount()).apply();
            }
            updateintent(dbHandler, ProductDetailActivity.this);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public void addToCart(HashMap<String, String> map, int qty, String action) {
        ProgressDialog progressDialog = new ProgressDialog(ProductDetailActivity.this);
        progressDialog.show();
        String tag_json_obj = "json_cart_list_req";
        String custID = session_management.getUserDetails().get(BaseURL.KEY_ID);
        Map<String, String> params = new HashMap<String, String>();
        params.put("CustId", custID);
        params.put("ItemId", map.get("ItemId"));
        params.put("Price", map.get("price"));
        params.put("Quantity", "" + qty);
        params.put("unitID", map.get("varient_id"));
        // params.put("SupplierID","S1002");
        params.put("SupplierID", map.get("supplierID"));


        Log.d("addToCart__", "" + params);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ApiBaseURL.Cart, params, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("CheckApiCart", response.toString());
                try {
                    boolean status = response.getBoolean("status");
                    if (status) {
                        if (action.equals("delete")) {
                            progressDialog.dismiss();
                            dbHandler.removeItemFromCart(map.get("varient_id"));
                            //list.remove(map);
                        } else {
                            progressDialog.dismiss();
                            dbHandler.setCart(map, qty);

                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            SharedPreferences preferences = getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                            preferences.edit().putInt("cardqnty", dbHandler.getCartCount()).apply();
                        }
                        updateintent(dbHandler, ProductDetailActivity.this);
                        //checkEmptyCartListener.onCartChange();
                    }

                    Toast.makeText(ProductDetailActivity.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                VolleyLog.d("", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                }
            }
        });

        // Adding request to request queue
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    /*public void addToCart1(ProductDetailModel productDetailModel,int qty,String action)
    {
        ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.show();
        String tag_json_obj = "json_cart_list_req";
        String custID= session_management.getUserDetails().get(BaseURL.KEY_ID);
        Map<String, String> params = new HashMap<String, String>();
        params.put("CustId", custID);
        params.put("ItemId",productDetailModel.getItemID());
        params.put("Price", productDetailModel.getItemSellingprice());
        params.put("Quantity",""+qty);
        params.put("unitID",productDetailModel.getUnitID());
        // params.put("SupplierID","S1002");
        params.put("SupplierID",productDetailModel.getMainSupplier());


        Log.d("addToCart__", ""+params);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ApiBaseURL.Cart, params, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("CheckApiCart", response.toString());
                try {
                    boolean status = response.getBoolean("status");
                    if (status) {
                        if(action.equals("delete")){
                            progressDialog.dismiss();
                            dbHandler.removeItemFromCart(productDetailModel.getItemID());
                            //list.remove(map);
                            //notifyDataSetChanged();

                        }
                        else {
                            progressDialog.dismiss();
                            dbHandler.setCart((HashMap<String, String>) params, qty);

                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            SharedPreferences preferences = getContext().getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                            preferences.edit().putInt("cardqnty", dbHandler.getCartCount()).apply();
                        }
                        updateintent(dbHandler, getContext());
                        //checkEmptyCartListener.onCartChange();
                    }

                    Toast.makeText(getContext(), ""+response.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                VolleyLog.d("", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                }
            }
        });

        // Adding request to request queue
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }*/

    private void updateintent(DatabaseHandler dbHandler, Context context) {
        //checkEmptyCartListener.onCartChange();
        //txt_new_price.setText(session_management.getCurrency() + " " + dbHandler.getTotalAmount());
        //txt_new_price.setText(dbHandler.getTotalAmount());
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                SharedPreferences preferences = context.getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                preferences.edit().putInt("cardqnty", dbHandler.getCartCount()).apply();

                if (dbHandler.getCartCount() == 0) {
                    // notifier.onViewNotify();
                }
            }
        } catch (Exception ep) {
            ep.printStackTrace();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equalsIgnoreCase("cardqnty")) {

            int badgeCount = pref.getInt("cardqnty", 0);
            if (badgeCount > 0) {
                cartCount.setText("" + badgeCount);
                cartCount.setVisibility(View.VISIBLE);
            } else {
                cartCount.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onItemClick(int position, ArrayList<VarientModel> varientModel) {
        slider_view.setCurrentPagePosition(position);
        varientAdapter.selectedItemPosition(position);

        itemSellingPrice = varientModel.get(position).getItemSellingprice();
        unitId = varientModel.get(position).getUnitID();
        //itemName = varientModel.get(position).getItemName();
        itemId = varientModel.get(position).getItemID();
       // categoryId = varientModel.get(position).getCategoryID();
        //fixedPrice = varientModel.get(position).getFixedPrice();
        productImage = varientModel.get(position).getImage();
        //mainSupplier = varientModel.get(position).getMainSupplier();
        unitValue = varientModel.get(position).getUom();
        //shortDescription = varientModel.get(position).getShortDes();
        stockingType = varientModel.get(position).getStockingType();


        if(varientModel.get(position).getItemSellingprice()!=null || !varientModel.get(position).getItemSellingprice().isEmpty()){
            txt_currency.setText(session_management.getCurrency());
            txt_new_price.setText(varientModel.get(position).getItemSellingprice());
            if(position==0&&Double.parseDouble(productDetailModel.getFixedPrice())>0.00){
                tvNow.setVisibility(View.VISIBLE);
                llWas.setVisibility(View.VISIBLE);
                txt_old_price.setVisibility(View.VISIBLE);
                txt_old_price.setText(session_management.getCurrency()+" "+productDetailModel.getFixedPrice());
                txt_old_price.setPaintFlags(txt_old_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }else{
                tvNow.setVisibility(View.GONE);
                llWas.setVisibility(View.GONE);
            }
        }

        if(dbHandler.isInCart(unitId)){
            try{
                Log.e("qty",dbHandler.getCartItemQty(unitId)+"");
                if(dbHandler.getCartItemQty(unitId)!=null||!dbHandler.getCartItemQty(unitId).isEmpty()){
                    tvItemCount.setText(dbHandler.getCartItemQty(unitId));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            tvItemCount.setText("0");
        }




        /*if(!varientModel.get(position).getFixedPrice().equals("0.00")){
            txt_old_price.setVisibility(View.VISIBLE);
            txt_old_price.setText(varientModel.get(position).getFixedPrice());
            txt_old_price.setPaintFlags(txt_old_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            txt_old_price.setVisibility(View.GONE);
        }*/

    }

    public void downloadImage(String downloadUrlOfImage,String folderName,String filename){
        //String filename = fileName+".jpg";
        //String downloadUrlOfImage = "http://qtp.ae/QTPMobileApp/images/Product/13_EMIRATESSPAGHETTI20x400GM_1.PNG";
        File direct =
                new File(Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        .getAbsolutePath() + "/" + "Qtp Mart" + "/");


        if (!direct.exists()) {
            direct.mkdir();
            Log.d("LOG_TAG", "dir created for first time");
        }

        DownloadManager dm = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadUri = Uri.parse(downloadUrlOfImage);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(filename)
                .setMimeType("image/jpeg")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,
                File.separator + "Qtp Share" + File.separator + folderName + File.separator + System.currentTimeMillis()+ ".jpg");

        dm.enqueue(request);
    }

    public void downloadShareImage(String folderName) {
        progressDialog.show();
        File direct = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + "Qtp Share" + "/" + folderName + "/");

        if(direct.exists()){
            direct.delete();
        }
        direct.mkdir();

        /*if (!direct.exists()) {
            direct.mkdir();
            Log.d("LOG_TAG", "dir created for first time");
        }else{
            direct.delete();
            direct.mkdir();
        }*/

        list.clear();
//.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        for (int i = 0; i < imageModels.size(); i++) {
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadUri = Uri.parse(imageModels.get(i).getImagePath());
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(folderName+i)
                .setMimeType("image/jpeg")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, File.separator + "Qtp Share" + File.separator + folderName + File.separator + System.currentTimeMillis()+ ".jpg");
            refid = downloadManager.enqueue(request);

            list.add(refid);

        }

    }



    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

                    // main logic
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ProductDetailActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        getProductDetail(itemID);
    }

    BroadcastReceiver onComplete = new BroadcastReceiver() {

        public void onReceive(Context ctxt, Intent intent) {

            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);


            Log.e("IN", "" + referenceId);

            list.remove(referenceId);


            if (list.isEmpty())
            {
                progressDialog.dismiss();
                if(productDetailModel!=null) {
                    if(!shareTo.equals("")){
                        shareMulti(productDetailModel.getItemName(),shareTo);
                    }

                }



                Log.e("INSIDE", "" + referenceId);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(ProductDetailActivity.this)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("GadgetSaint")
                                .setContentText("All Download completed");


//                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                notificationManager.notify(455, mBuilder.build());


            }

        }
    };

    @Override
    protected void onDestroy() {


        super.onDestroy();

        unregisterReceiver(onComplete);

    }

    void favouriteState( ProductDetailModel cc)
    {
        if(dbcart.isInWishlist(cc.getItemID()))
        {
          imgFavorite.setImageResource(R.drawable.ic_fill_favorite_24);
        }
        else
        {
            imgFavorite.setImageResource(R.drawable.ic_favorite_24);
        }

    }

    public void addToFav(String itemId, ProductDetailModel cc)
    {
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.show();
        String tag_json_obj = "json_cart_list_req";
        String custID= session_management.getUserDetails().get(BaseURL.KEY_ID);
        Map<String, String> params = new HashMap<String, String>();
        params.put("CustId", custID);
        params.put("ItemId",itemId);
        params.put("DeviceName","Android");

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ApiBaseURL.AddToFav, params, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("CheckAddRecentView", response.toString());
                try {
                    progressDialog.dismiss();
                    boolean status = response.getBoolean("status");
                    if (status) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put("varient_id", cc.getItemID());
                        map.put("product_name", cc.getItemName());
                        map.put("price", cc.getItemSellingprice());
                        map.put("product_image", cc.getImage());
                        map.put("unit_value", cc.getUom());
                        map.put("product_description", cc.getShortDes());
                        map.put("supplierID", cc.getMainSupplier());
                        dbcart.setWishlist (map);
                        favouriteState(cc);
                        //CommonFunctions.setFavouriteCounts(session_management.getUserDetails().get(BaseURL.KEY_ID));
                        updateSharedPrefFav();
                        //  myViewHolder.imgFavorite.setImageResource(R.drawable.ic_fill_favorite_24);
                    }

                    // Toast.makeText(context, ""+response.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                VolleyLog.d("", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                }
            }
        });

        // Adding request to request queue
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    void updateSharedPrefFav()
    {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                SharedPreferences preferences = getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                preferences.edit().putInt("favcount", dbcart.getWishlistCount()).apply();

                preferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
                    @Override
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                        MainDrawerActivity.bottomNavigation.setCount(4, String.valueOf(dbcart.getWishlistCount()));
                    }
                });
            }
        } catch (IndexOutOfBoundsException e) {
            Log.d("qwer", e.toString());
        }
    }
}