package activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.grocery.QTPmart.R;
import com.qamar.curvedbottomnaviagtion.CurvedBottomNavigation;
import util.FragmentClickListner;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import Config.BaseURL;
import ModelClass.Category_model;
import ModelClass.NewPendingDataModel;
import fragments.HomeFragment;
import network.ApiInterface;
import fragments.DashboardFragment;
import util.Session_management;
import xute.storyview.StoryModel;

public class MainDrawerActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener/*, *//*FetchAddressTask.OnTaskCompleted,
        SharedPreferences.OnSharedPreferenceChangeListener,View.OnClickListener,
        WSCallerVersionListener */{
    private static final String TAG = MainDrawerActivity.class.getName();
    private static final int REQUEST_LOCATION_PERMISSION = 100;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 3000;
    public CardView content_view, content_view1;
    private LinearLayout nav_view;
    public static boolean isHide = true;
    private boolean enterInFirst = false;
    boolean canGetLocation = false;
    double latitude = 0.0, longitude = 0.0;
    public static int menuClickFlag = 0;
    public static ImageView img_menu, iv_setting_drawer;
    public static CurvedBottomNavigation bottomNavigation;
    public int selectedId = 1;
    private final static int ID_HOME = 1;
    private final static int ID_MY_ORDERS = 2;
    private final static int ID_SEARCH = 3;
    private final static int ID_FAVOURITE = 4;
    private final static int ID_CATEGORY = 5;
    private FragmentClickListner fragmentClickListner;
    ImageView bell;
    private LocationManager locationManager;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
//    private DatabaseHandler dbcart;
    private Session_management sessionManagement;
    private TextView addres;
    String returnTo = "";
    String id;
//    private DatabaseHandler db;

    public static LinearLayout ll_nav_title;
    public static ImageView imageView_admin;
    public static LinearLayout ll_home, ll_dashboard, ll_orders, ll_delivery_boy,ll_refer,
            ll_faq, ll_contact, ll_locate, ll_about, ll_logout, ll_notification, ll_recently_viewed, ll_vouchers,ll_rate_app;
    public static ImageView iv_toolbar_logo;
    LinearLayout linearLayout;
    private LocationRequest locationRequest;
    private Location location;

    public static TextView tv_toolbar_title, tv_toolbar_morder;

    Location currentlocation;
    private SharedPreferences pref;
    HashMap user;
    int fromUpdate;
    public static Toolbar toolbar;
    String name, pass, email;
    public static TextView txt_mainOrder_number, tvUserName, tvUserContact, tvUserMail, tvTitle,
            tv_settings_drawer;

    public static RelativeLayout cartLyt,reelLyt;
    public static TextView cartCount,reelCount;
    String role;

    public static View view_home, view_dashboard, view_refer, view_delivery_boy, view_notification, view_recently_viewed, view_vouchers,view_rate_app;
    public static LinearLayout ll_login_drawer;

    private GestureDetectorCompat mDetector;
    private FusedLocationProviderClient mFusedLocationClient;
    public static EditText edt_search;
    public static ImageView reel_iv, notification_iv, cart, search_iv;

    CoordinatorLayout toolbar_nav;

//    StoryView storyView;

    RecyclerView recyclerImages;
    ArrayList<HashMap<String, String>> listarray;
    ArrayList<String> imageString = new ArrayList<>();
//    private BannerAdapter1 bannerAdapter;
    LinearLayoutManager linearLayoutManager;
    final int time = 3000;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    public static List<Category_model> cateList = new ArrayList<>();

    public static TextView tvNotificationCount, tvRecentViewCount, tvVoucherCount;

    public static ImageView img_logo;

    int currentVersion;

    AppUpdateManager appUpdateManager;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);

        this.content_view = (CardView) findViewById(R.id.main_content);
        this.content_view1 = (CardView) findViewById(R.id.main_content1);
        this.nav_view = (LinearLayout) findViewById(R.id.nav_view);

        try {
            mAuth = FirebaseAuth.getInstance();
        } catch (IllegalStateException e) {
            Log.e("MainDrawerActivity", "Firebase not initialized", e);
        }
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        initViews();

        img_menu = (ImageView) findViewById(R.id.img_menu);

        ll_home = findViewById(R.id.ll_home);
        ll_dashboard = findViewById(R.id.ll_dashboard);
        ll_nav_title = findViewById(R.id.ll_nav_title);
        ll_nav_title.setVisibility(View.GONE);
        ll_faq = findViewById(R.id.ll_faq);
        ll_contact = findViewById(R.id.ll_contact);
        ll_locate = findViewById(R.id.ll_locate);
        ll_about = findViewById(R.id.ll_about);
        ll_refer = findViewById(R.id.ll_refer);
        ll_logout = findViewById(R.id.ll_logout);
        tvTitle = findViewById(R.id.tvTitle);

        ll_notification = findViewById(R.id.ll_notification);
        ll_recently_viewed = findViewById(R.id.ll_recently_viewed);
        ll_vouchers = findViewById(R.id.ll_vouchers);
        ll_rate_app = findViewById(R.id.ll_rate_app);

        tvNotificationCount = findViewById(R.id.tvNotificationCount);
        tvVoucherCount = findViewById(R.id.tvVoucherCount);
        tvRecentViewCount = findViewById(R.id.tvRecentViewCount);

        toolbar_nav = findViewById(R.id.toolbar_nav);
        search_iv = findViewById(R.id.search_iv);
        img_menu = (ImageView) findViewById(R.id.img_menu);
        iv_setting_drawer = (ImageView) findViewById(R.id.iv_setting_drawer);
        tv_settings_drawer = findViewById(R.id.tv_settings_drawer);
        // tv_toolbar_title = (TextView) findViewById(R.id.tv_toolbar_title);

//        db = new DatabaseHandler(this);
        sessionManagement = new Session_management(MainDrawerActivity.this);
        pref = getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
//        pref.registerOnSharedPreferenceChangeListener(this);
//        dbcart = new DatabaseHandler(this);

        tvUserName = (TextView) findViewById(R.id.tv_user_name);
        tvUserContact = (TextView) findViewById(R.id.tv_user_contact);
        tvUserMail = (TextView) findViewById(R.id.tv_user_mail);
        imageView_admin = findViewById(R.id.imageView_admin);

        img_logo = findViewById(R.id.img_logo);
        reelCount = findViewById(R.id.reelCount);

        /*listarray = new ArrayList<>();
        bannerAdapter = new BannerAdapter(this, imageString,listarray);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);*/

        // dbcart.clearCart();

        /*Load Profile Image*/
        String getId = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
//        updateProfileImage(getId, imageView_admin);
        /*Picasso.get()
                .load(ApiBaseURL.IMG_URL_NEW+"profile_" +getId+ ".png")
                .placeholder(R.drawable.toy_face)
                .memoryPolicy(MemoryPolicy.NO_STORE, MemoryPolicy.NO_CACHE)
                .into(imageView_admin);*/


        imageView_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManagement.isLoggedIn()) {
                    if (isHide) {
                        MainDrawerActivity menuDrawerBack = MainDrawerActivity.this;
                        menuDrawerBack.showMenu(menuDrawerBack.content_view, menuDrawerBack.content_view1);
                        return;
                    }
                    MainDrawerActivity menuDrawerBack2 = MainDrawerActivity.this;
                    menuDrawerBack2.hideMenu(menuDrawerBack2.content_view, menuDrawerBack2.content_view1);
//                    loadFragment(new ProfileViewFragment());
//                    bottomNavigation.setVisibility(View.GONE);
                }
            }

        });

        tv_settings_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManagement.isLoggedIn()) {
                    if (isHide) {
                        MainDrawerActivity menuDrawerBack = MainDrawerActivity.this;
                        menuDrawerBack.showMenu(menuDrawerBack.content_view, menuDrawerBack.content_view1);
                        return;
                    }
                    MainDrawerActivity menuDrawerBack2 = MainDrawerActivity.this;
                    menuDrawerBack2.hideMenu(menuDrawerBack2.content_view, menuDrawerBack2.content_view1);
//                    loadFragment(new ProfileViewFragment());
//                    bottomNavigation.setVisibility(View.GONE);
                }

            }
        });

        tvUserName.setText(sessionManagement.getUserDetails().get(BaseURL.KEY_NAME));
        tvUserContact.setText(sessionManagement.getUserDetails().get(BaseURL.KEY_MOBILE));
        tvUserMail.setText(sessionManagement.getUserDetails().get(BaseURL.KEY_EMAIL));

        view_home = findViewById(R.id.view_home);
        view_dashboard = findViewById(R.id.view_dashboard);
        view_notification = findViewById(R.id.view_notification);
        view_recently_viewed = findViewById(R.id.view_recently_viewed);
        view_vouchers = findViewById(R.id.view_vouchers);
        view_rate_app = findViewById(R.id.view_rate_app);
        view_refer = findViewById(R.id.view_refer);

        ll_login_drawer = findViewById(R.id.ll_login_drawer);
        reel_iv = findViewById(R.id.reel_iv);
        notification_iv = findViewById(R.id.notification_iv);
        edt_search = findViewById(R.id.edt_search);

//        storyView = findViewById(R.id.storyView);
//        storyView.setActivityContext(MainDrawerActivity.this);
//        storyView.resetStoryVisits();
        ArrayList<StoryModel> uris = new ArrayList<>();
        uris.add(new StoryModel("https://picsum.photos/200/300", "", ""));
        uris.add(new StoryModel("https://cdn.pixabay.com/photo/2015/04/19/08/32/marguerite-729510__340.jpg", "", ""));
//        storyView.setImageUris(uris);

//        if (NetworkConnection.connectionChecking(this)) {
//            categoryUrl();
//        } else {
//            showToast(getString(R.string.no_internet));
//        }


        ll_login_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainDrawerActivity.this, LoginActivity.class));
                finish();
            }
        });

        ll_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                bottomNavigation.show(ID_HOME, true);
//                bottomNavigation.setVisibility(View.VISIBLE);
//                setToolbarAndLoadFragment("", new DashboardFragment());
//                viewSelector("Dashboard");
            }
        });

        notification_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                loadFragment(new NewNotificationFragment());
//                viewSelector("Notification");
            }
        });

        ll_notification.setOnClickListener(view -> {
//            bottomNavigation.show(ID_HOME, true);
//            bottomNavigation.setVisibility(View.VISIBLE);
//            setToolbarAndLoadFragment("", new NewNotificationFragment());
//            viewSelector("Notification");
        });

        ll_recently_viewed.setOnClickListener(view -> {
            //Intent intent = new Intent(this, ViewAll_TopDeals.class);
            //intent.putExtra("action_name", "Recent_Details_Fragment");
            // startActivity(intent);
//            bottomNavigation.show(ID_HOME, true);
//            bottomNavigation.setVisibility(View.VISIBLE);
//            setToolbarAndLoadFragment("", new RecentlyViewedFragment());
//            viewSelector("Recently Viewed");
        });

        ll_vouchers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                bottomNavigation.show(ID_HOME, true);
//                bottomNavigation.setVisibility(View.VISIBLE);
//                setToolbarAndLoadFragment("", new VoucherFragment());
//                viewSelector("Voucher");
            }
        });

        ll_rate_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("market://details?id=" + MainDrawerActivity.this.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + MainDrawerActivity.this.getPackageName())));
                }
//                viewSelector("Rate App");
            }
        });


        ll_refer.setOnClickListener(view -> {
            //  bottomNavigation.show(ID_HOME, true);
            // bottomNavigation.setVisibility(View.VISIBLE);
            // setToolbarAndLoadFragment("", new RecentlyViewedFragment());
            // viewSelector("Home");

//            Intent intent = new Intent(MainDrawerActivity.this, ReferActivity.class);
//            startActivity(intent);
        });

        ll_home.setOnClickListener(view -> {
            bottomNavigation.show(ID_HOME, true);
            setToolbarAndLoadFragment("", new HomeFragment(fragmentClickListner));
            viewSelector("Home");
        });

        reel_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                startActivity(new Intent(MainDrawerActivity.this, ReelsActivity.class));

               /* Dialog bottomSheetDialog=new Dialog(MainDrawerActivity.this);
                bottomSheetDialog.setContentView(R.layout.bottom_layout_order_success);
                int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
                int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);

                bottomSheetDialog.getWindow().setLayout(width,height);
                bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
                bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                bottomSheetDialog.show();*/
            }
        });

        edt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                loadFragment(new NewSearchFragment());
                //bottomNavigation.setVisibility(View.VISIBLE);
                //edt_search.setVisibility(View.GONE);
                //ll_nav_title.setVisibility(View.GONE);
                //reel_iv.setVisibility(View.VISIBLE);
                //notification_iv.setVisibility(View.VISIBLE);
                //search_iv.setVisibility(View.VISIBLE);
            }
        });

        search_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadFragment(new NewSearchFragment());
//                startActivity(new Intent(MainDrawerActivity.this, NewSeearchActivity.class)
//                        .putExtra("fromIntent", 0));
                //bottomNavigation.setVisibility(View.VISIBLE);
                //edt_search.setVisibility(View.GONE);
                //ll_nav_title.setVisibility(View.GONE);
                //reel_iv.setVisibility(View.VISIBLE);
                //notification_iv.setVisibility(View.VISIBLE);
                //search_iv.setVisibility(View.VISIBLE);
            }
        });

        this.content_view.post(new Runnable() {
            public void run() {
                content_view.setTranslationX(0.0f);
//                content_view.setRadius((float) dpToPx(getApplicationContext(), 0));
            }
        });

        this.img_menu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (isHide) {
                    MainDrawerActivity menuDrawerBack = MainDrawerActivity.this;
                    menuDrawerBack.showMenu(menuDrawerBack.content_view, menuDrawerBack.content_view1);
                    return;
                }
                MainDrawerActivity menuDrawerBack2 = MainDrawerActivity.this;
                menuDrawerBack2.hideMenu(menuDrawerBack2.content_view, menuDrawerBack2.content_view1);
            }
        });

        initComponent();




        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        // new GooglePlayStoreAppVersionNameLoader(getApplicationContext(), this).execute();

        //  checkAppUpdate();

        initLocation();


        if (sessionManagement.isLoggedIn()) {
//            if (NetworkConnection.connectionChecking(MainDrawerActivity.this)) {
//                new Thread(this::getCartProducts).start();
//                //new Thread(this::getAllFav).start();
//                //new Thread(this::getCounts).start();
//            } else {
//                showToast(getString(R.string.no_internet));
//            }
        }


        initBadges();

        int badgeCount = pref.getInt("cardqnty", 0);
        if (badgeCount > 0) {
            cartCount.setText("" + badgeCount);
            cartCount.setVisibility(View.VISIBLE);
        } else {
            cartCount.setVisibility(View.GONE);
        }

       /* int favCount=pref.getInt("favcount",0);
        Log.e("favCount",String.valueOf(favCount));
        if(favCount>0){
            bottomNavigation.setCount(ID_FAVOURITE, String.valueOf(favCount));
        }
        else
        {
            bottomNavigation.clearCount(ID_FAVOURITE);
        }*/

        /*Logout*/
        ll_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dbcart.clearCart();
                // sessionManagement.setCurrency("AED ", "AED ");
                //  dbcart.clearWishlist();
//                logoutAll();
//                sessionManagement.logoutSession();
                finish();
            }
        });

//        fragmentClickListner = new FragmentClickListner() {
//            @Override
//            public void onFragmentClick(boolean open) {
//                if (open) {
//                    bottomNavigation.show(ID_HOME, true);
//                    loadFragment(new CartFragment());
//                }
//            }
//
//            public void loadFavourites() {
//
//                bottomNavigation.show(ID_FAVOURITE, true);
//                loadFragment(new FavouriteFragment());
//            }
//
//            @Override
//            public void onChangeHome(boolean open) {
//                DecimalFormat dFormat = new DecimalFormat("##.#######");
//                LatLng latLng = new LatLng(Double.parseDouble(sessionManagement.getLatPref()), Double.parseDouble(sessionManagement.getLangPref()));
//                double latitude = Double.valueOf(dFormat.format(latLng.latitude));
//                double longitude = Double.valueOf(dFormat.format(latLng.longitude));
//                //location.setLatitude(latitude);
//                //  location.setLongitude(longitude);
//                // getAddress();
//                bottomNavigation.show(ID_HOME, true);
//                loadFragment(new HomeFragment(fragmentClickListner));
//            }
//        };

        img_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                bottomNavigation.show(ID_HOME, true);
//                setToolbarAndLoadFragment("", new HomeFragment(fragmentClickListner));
            }
        });

        if (getIntent().getIntExtra("loadFrag", 0) == 2) {
//            bottomNavigation.show(ID_MY_ORDERS, true);
//            sessionManagement.setBookOrder("1");
//            setToolbarAndLoadOrderFragment("", new OrderFragment());
        } else {
            bottomNavigation.show(ID_HOME, true);
            setToolbarAndLoadFragment("", new HomeFragment(fragmentClickListner));
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

       /* db = new DatabaseHandler(this);
        sessionManagement = new Session_management(MainDrawerActivity.this);*/

        if (sessionManagement.isLoggedIn()) {
            /*Dialog for suggestion*/
           /* if(getIntent().getIntExtra("loadFrag",0)==1){
                //showFavourites();
                showFullScreenDialog();
            }*/

//            if (NetworkConnection.connectionChecking(MainDrawerActivity.this)) {
//                showFavourites();
//
//            } else {
//                showToast(getString(R.string.no_internet));
//            }

            ll_login_drawer.setVisibility(View.GONE);
            ll_notification.setVisibility(View.VISIBLE);
            ll_vouchers.setVisibility(View.VISIBLE);
            ll_dashboard.setVisibility(View.GONE);
            ll_recently_viewed.setVisibility(View.VISIBLE);
            ll_logout.setVisibility(View.VISIBLE);
            ll_refer.setVisibility(View.VISIBLE);
        } else {
//            if (sessionManagement.isMainPopUpVisible()) {
//                showFullScreenDialog();
//            }
        }


        try {
            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                @Override
                public void onComplete(@NonNull Task<InstanceIdResult> task) {

                    Log.e("token", task.getResult().getToken());

//                String user_id = sessionManagement.userId();
//                if (!user_id.equals(""));
                    //  updateFirebaseToken(task.getResult().getToken());
                }
            });
        } catch (IllegalStateException e) {
            Log.e("MainDrawerActivity", "Firebase not initialized, skipping token fetch", e);
        }


    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {

        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.add(new CurvedBottomNavigation.Model(ID_HOME, "Home", R.drawable.ic_home));
        bottomNavigation.add(new CurvedBottomNavigation.Model(ID_CATEGORY, "Category", R.drawable.ic_category));
        bottomNavigation.add(new CurvedBottomNavigation.Model(ID_SEARCH, "Search", R.drawable.ic_loupe));
        bottomNavigation.add(new CurvedBottomNavigation.Model(ID_FAVOURITE, "Favourite", R.drawable.ic_favorite_24));
        bottomNavigation.add(new CurvedBottomNavigation.Model(ID_MY_ORDERS, "Orders", R.drawable.order));
        bottomNavigation.show(ID_HOME, true);

        cartLyt = findViewById(R.id.cartLyt);
        reelLyt = findViewById(R.id.reelLyt);
        // bell = findViewById(R.id.bell);
        cart = findViewById(R.id.cart);
        cartCount = findViewById(R.id.cartCount);

        cartLyt.setOnClickListener(v -> {
//            startActivity(new Intent(MainDrawerActivity.this, CartActivity.class));
        });

    }

    public void setToolbarAndLoadOrderFragment(String title, Fragment fragment) {
        menuClickFlag = 1;

        toolbar_nav.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        //ll_nav_title.setVisibility(View.VISIBLE);
//        iv_toolbar_logo.setVisibility(View.GONE);
//        tv_toolbar_title.setVisibility(View.VISIBLE);
        // tv_toolbar_title.setText(title);
        search_iv.setColorFilter(ContextCompat.getColor(MainDrawerActivity.this, android.R.color.white),
                PorterDuff.Mode.MULTIPLY);
        img_menu.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon_nav_menu_white));

        MainDrawerActivity menuDrawerBack2 = MainDrawerActivity.this;
        menuDrawerBack2.hideMenu(menuDrawerBack2.content_view, menuDrawerBack2.content_view1);
        Bundle bundle = new Bundle();
        bundle.putInt("fromBookOrder", 1);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_supplier_fragment, fragment).addToBackStack(null).commitAllowingStateLoss();

    }

    public void setToolbarAndLoadFragment(String title, Fragment fragment) {
        menuClickFlag = 1;

        toolbar_nav.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        ll_nav_title.setVisibility(View.VISIBLE);
//        iv_toolbar_logo.setVisibility(View.GONE);
//        tv_toolbar_title.setVisibility(View.VISIBLE);
        // tv_toolbar_title.setText(title);
        search_iv.setColorFilter(ContextCompat.getColor(MainDrawerActivity.this, android.R.color.white),
                PorterDuff.Mode.MULTIPLY);
        img_menu.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon_nav_menu_white));

        MainDrawerActivity menuDrawerBack2 = MainDrawerActivity.this;
        menuDrawerBack2.hideMenu(menuDrawerBack2.content_view, menuDrawerBack2.content_view1);
        loadFragment(fragment);

    }

    public void initComponent() {
        bottomNavigation.setOnClickMenuListener(model -> {
            switch (model.getId()) {
                case ID_HOME:
                    loadFragment(new HomeFragment(fragmentClickListner));
                    // viewSelector("Home");
                    selectedId = ID_HOME;
                    break;
                case ID_MY_ORDERS:
                    // loadFragment(new OrderFragment());
                    selectedId = ID_MY_ORDERS;
                    break;
                case ID_SEARCH:
                    // loadFragment(new NewSearchFragment());
                    selectedId = ID_SEARCH;
                    break;
                case ID_FAVOURITE:
                    // loadFragment(new FavouriteFragment());
                    selectedId = ID_FAVOURITE;
                    break;
                case ID_CATEGORY:
                    // loadFragment(new CategoryFragment(fragmentClickListner));
                    selectedId = ID_CATEGORY;
                    break;
            }
            return null;
        });
    }

    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_supplier_fragment, fragment).addToBackStack(null).commitAllowingStateLoss();
    }

    public ObjectAnimator hideMenu(View view, View view2) {
        this.isHide = true;
        toolbar_nav.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        img_menu.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon_nav_menu_white));

        view2.animate().scaleX(1.0f).scaleY(1.0f).translationX(0.0f).setDuration(300);
        view.animate().scaleX(1.0f).scaleY(1.0f).translationX(0.0f).setDuration(300).setListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
//                content_view.setRadius((float) dpToPx(getApplicationContext(), 0));
            }
        }).start();

        if (menuClickFlag != 1) {
            setSystemBarColor(MainDrawerActivity.this, R.color.colorPrimary);
        } else {
            setSystemBarColor(MainDrawerActivity.this, R.color.colorPrimary);
        }
        return null;
    }

    public ObjectAnimator showMenu(View view, View view2) {
        toolbar_nav.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        img_menu.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon_nav_menu_white));

        setSystemBarColor(MainDrawerActivity.this, R.color.colorPrimary);
        this.isHide = false;
        view2.animate().scaleX(0.9f).scaleY(0.78f).translationX(((float) view.getWidth()) / 2.1f).setDuration(300);
        view.animate().scaleX(0.9f).scaleY(0.85f).translationX(((float) view.getWidth()) / 1.8f).setDuration(300).setListener(new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animator) {
                super.onAnimationStart(animator);
//                content_view.setRadius((float) dpToPx(getApplicationContext(), 15));
            }
        }).start();
        return null;
    }

//    public static int dpToPx(Context context, int i) {
//        return Math.round(TypedValue.applyDimension(1, (float) i, context.getResources().getDisplayMetrics()));
//    }

    public static void setSystemBarColor(Activity activity, int i) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            window.addFlags(Integer.MIN_VALUE);
            window.clearFlags(67108864);
            window.setStatusBarColor(activity.getResources().getColor(i));
        }
    }

//    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key != null) {
            if (key.equalsIgnoreCase("cardqnty")) {
//            totalBudgetCount.setText(pref.getInt("cardqnty",0));
                int badgeCount = pref.getInt("cardqnty", 0);
                if (badgeCount > 0) {
                    cartCount.setText("" + badgeCount);
                    cartCount.setVisibility(View.VISIBLE);
                } else {
                    cartCount.setVisibility(View.GONE);
                }
            } else if (key.equalsIgnoreCase("favcount")) {
                int favCount = pref.getInt("favcount", 0);
                if (favCount > 0) {
                    //  bottomNavigation.setCount(ID_FAVOURITE, String.valueOf(favCount));
                } else {
                    // bottomNavigation.clearCount(ID_FAVOURITE);
                }
            } else if (key.equalsIgnoreCase("updateProfilePic")) {

                Log.e("in", "sharedperf");
                pref.edit().putString("updateProfilePic", "true");

            }
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//
//            case 1: {
//
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
//                    //  call_action();
//                } else {
//                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
//                }
//                return;
//            }
//
//            case REQUEST_LOCATION_PERMISSION: {// If the permission is granted, get the location,
//                // otherwise, show a Toast
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////                    getLocation();
//                    location = getLocation();
//                    if (location != null) {
//                        getAddress();
//                    }
//                    Log.e(TAG, "Granted");
////                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
////                    return;
////                }
////                mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
////                    @Override
////                    public void onSuccess(Location location) {
////                        if (location != null) {
//////                                Log.e(TAG, "location create" + location.getLatitude() + " , " + location.getLongitude());
////                            new FetchAddressTask(MainActivity.this, MainActivity.this).execute(location);
////                        }
////                    }
////                });
//
//
//                } else {
////                    Log.e(TAG, "permission denied" );
//
//                    Toast.makeText(MainDrawerActivity.this, "Location permission is necessary", Toast.LENGTH_SHORT).show();
//                    finish();
//
//                }
//                return;
//            }
//
//            // other 'case' lines to check for other
//            // permissions this app might request
//        }
//    }

//    private void showFavourites() {
//
//        Dialog dialog = new Dialog(MainDrawerActivity.this);
//        dialog.setContentView(R.layout.layout_popup_fav);
//
//        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
//        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);
//
//        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, height);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//
//        // dialog.show();
//
//        FrameLayout nav_supplier_fragment = findViewById(R.id.nav_supplier_fragment);
//        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerCart);
//
//        ImageView close = dialog.findViewById(R.id.close);
//
//        ServiceGenrator.getApiInterface().getFavouriteProductList(sessionManagement.getUserDetails().get(BaseURL.KEY_ID), "4").enqueue(
//                new Callback<RestItem>() {
//                    @Override
//                    public void onResponse(Call<RestItem> call, retrofit2.Response<RestItem> response) {
//
//                        if (response.isSuccessful()) {
//
//                            if (response.body().isStatus()) {
//
//                                if (response.body().getResult() != null) {
//                                    dialog.show();
//                                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                    nav_view.getForeground().setAlpha(150);
//                                }*/
//                                    ArrayList<ItemModel> tempArrayList = response.body().getResult();
//                                    ArrayList<ItemModel> itemModelArrayList = new ArrayList<>();
//                                    Log.e("tempArrayList", String.valueOf(tempArrayList.size()));
//                                    for (int i = 0; i < tempArrayList.size(); i++) {
//                                        ItemModel favitem = new ItemModel();
//
//                                        favitem.setItemID(tempArrayList.get(i).getItemID());
//                                        favitem.setCategoryID(tempArrayList.get(i).getCategoryID());
//                                        favitem.setItemName(tempArrayList.get(i).getItemName());
//                                        favitem.setShortDes(tempArrayList.get(i).getShortDes());
//                                        favitem.setImage(tempArrayList.get(i).getImage());
//                                        favitem.setItemUnit(tempArrayList.get(i).getItemUnit());
//                                        favitem.setMainSupplier(tempArrayList.get(i).getMainSupplier());
//                                        favitem.setVatRate(tempArrayList.get(i).getVatRate());
//                                        favitem.setFeedback(tempArrayList.get(i).getFeedback());
//                                        favitem.setDiscount(tempArrayList.get(i).getDiscount());
//                                        favitem.setUnitID(tempArrayList.get(i).getUnitID());
//                                        favitem.setUom(tempArrayList.get(i).getUom());
//                                        favitem.setItemSellingprice(tempArrayList.get(i).getItemSellingprice());
//                                        favitem.setFixedPrice(tempArrayList.get(i).getFixedPrice());
//                                        favitem.setStockingType(tempArrayList.get(i).getStockingType());
//
//
//                                        if (tempArrayList.get(i).getFixedPrice() != null && Double.parseDouble(tempArrayList.get(i).getFixedPrice()) > 0) {
//                                            favitem.setItemSellingprice(tempArrayList.get(i).getFixedPrice());
//                                            favitem.setFixedPrice(tempArrayList.get(i).getItemSellingprice());
//                                        } else {
//                                            favitem.setFixedPrice(tempArrayList.get(i).getFixedPrice());
//                                            favitem.setItemSellingprice(tempArrayList.get(i).getItemSellingprice());
//                                        }
//                                        itemModelArrayList.add(favitem);
//
//
//                                    }
//                                    FavouriteAdapter favouriteAdapter = new FavouriteAdapter(MainDrawerActivity.this, itemModelArrayList, recyclerView);
//
//                                    recyclerView.setAdapter(favouriteAdapter);
//                                }
//
//
//                            }
//                            // dialog.dismiss();
//
//                        }
//
//
//                        if (sessionManagement.isMainPopUpVisible()) {
//                            showFullScreenDialog();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<RestItem> call, Throwable t) {
//                        if (sessionManagement.isMainPopUpVisible()) {
//                            showFullScreenDialog();
//                        }
//                    }
//                }
//        );
//        // recyclerView.setAdapter(new FavouriteAdapter(this, map, null,recyclerView));
//
//        // Button viewAll = dialog.findViewById(R.id.viewAll);
//        TextView txtViewAll = dialog.findViewById(R.id.txtViewAll);
//        txtViewAll.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.M)
//            @Override
//            public void onClick(View view) {
//
//                bottomNavigation.show(ID_FAVOURITE, true);
//                loadFragment(new FavouriteFragment());
//                //  drawer.getForeground().setAlpha(0);
//                dialog.dismiss();
//
//            }
//        });
//
//
//        /*recyclerView.setAdapter(new BaseAdapter() {
//            @Override
//            public int getCount() {
//                return map.size();
//            }
//
//            @Override
//            public Object getItem(int i) {
//                return map.get(i);
//            }
//
//            @Override
//            public long getItemId(int i) {
//                return 0;
//            }
//
//            @Override
//            public View getView(int i, View view, ViewGroup viewGroup) {
//
//                view = inflater.inflate(R.layout.favourite_layout_item, null);
//
//                view.findViewById(R.id.txt_close).setVisibility(View.GONE);
//                view.findViewById(R.id.quantityLayout).setVisibility(View.GONE);
//
//                HashMap<String, String> item = map.get(i);
//
//                ((TextView) view.findViewById(R.id.currency_indicator)).setText(sessionManagement.getCurrency());
//
//                Picasso.with(MainActivity.this)
//                        .load(ApiBaseURL.IMG_URL + item.get("product_image"))
//                        .into((ImageView)view.findViewById(R.id.prodImage));
//
//                ((TextView) view.findViewById(R.id.txt_pName)).setText(item.get("product_name"));
//
//                double sprice = Double.parseDouble(item.get("price"));
//                String p = String.format("%.2f",sprice);
//
//                ((TextView)view.findViewById(R.id.txt_Pprice1)).setText(p.substring(0, p.length()-3));
//                ((TextView)view.findViewById(R.id.txt_Pprice2)).setText(p.substring(p.length()-3));
//
//                view.setOnClickListener(new View.OnClickListener() {
//                    @SuppressLint("NewApi")
//                    @Override
//                    public void onClick(View view) {
//
//                        fragmentClickListner.loadFavourites();
//                        popupWindow.dismiss();
//                        drawer.getForeground().setAlpha(0);
//                    }
//                });
//
//                return view;
//            }
//        });*/
//
////        new Handler().postDelayed(new Runnable() {
////            @SuppressLint("NewApi")
////            @Override
////            public void run() {
////
////                if (fav.size() > 0) {
////                    popupWindow.showAtLocation(drawer, Gravity.CENTER, 0, 0);
////                    drawer.getForeground().setAlpha(150);
////                }
////            }
////        }, 1000);
//
//        close.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("NewApi")
//            @Override
//            public void onClick(View view) {
//
//                dialog.dismiss();
//                //drawer.getForeground().setAlpha(0);
//            }
//        });
//    }

//    private void showFullScreenDialog() {
//        Dialog dialog = new Dialog(MainDrawerActivity.this, android.R.style.Theme_Light);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.layout_popup_full_screen);
//
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//
//        ImageView close = dialog.findViewById(R.id.close);
//        ImageView ivPopUp = dialog.findViewById(R.id.ivPopUp);
//        Button btnShopNow = dialog.findViewById(R.id.btnShopNow);
//
//        if (NetworkConnection.connectionChecking(MainDrawerActivity.this)) {
//            ServiceGenrator.getApiInterface().getMainPopup("home").enqueue(new Callback<ResponseMainPopUp>() {
//                @Override
//                public void onResponse(Call<ResponseMainPopUp> call, retrofit2.Response<ResponseMainPopUp> response) {
//                    Log.e("main", response.toString());
//                    if (response.isSuccessful()) {
//                        if (response.body().isStatus()) {
//                            Picasso.get().load(response.body().getResult().getBanner_image()).error(R.mipmap.ic_launcher)
//                                    .into(ivPopUp, new com.squareup.picasso.Callback() {
//                                                @Override
//                                                public void onSuccess() {
//                                                    new Handler().postDelayed(new Runnable() {
//                                                        @Override
//                                                        public void run() {
//                                                            dialog.show();
//                                                            sessionManagement.setMainPopUp(false);
//                                                            btnShopNow.setOnClickListener(new View.OnClickListener() {
//                                                                @RequiresApi(api = Build.VERSION_CODES.M)
//                                                                @Override
//                                                                public void onClick(View view) {
//                                                                    dialog.dismiss();
//                                                                    Intent intent = new Intent(MainDrawerActivity.this, BannerItemsActivity.class);
//                                                                    intent.putExtra("banner_id", response.body().getResult().getBanner_id());
//                                                                    startActivity(intent);
//
//                                                                }
//                                                            });
//                                                        }
//                                                    }, 2000);
//
//                                                }
//
//                                                @Override
//                                                public void onError(Exception e) {
//
//                                                }
//                                            }
//                                    );
//
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseMainPopUp> call, Throwable t) {
//
//                }
//            });
//        } else {
//            showToast(getString(R.string.no_internet));
//        }
//
//        close.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("NewApi")
//            @Override
//            public void onClick(View view) {
//
//                dialog.dismiss();
//
//            }
//        });
//    }

    private void initLocation() {

        // sessionManagement = new Session_management(MainDrawerActivity.this);


        id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);

        addres = findViewById(R.id.address);
//        addres.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                startActivity(new Intent(MainActivity.this, AddressLocationActivity.class));
//            }
//        });
        returnTo = getIntent().getStringExtra("return");
        if (returnTo == null) {
            returnTo = "";
        }
        if (checkAndRequestPermissions()) {
            getLocationRequest();
        } else {
            setSupLocation();
        }
    }


    private void initBadges() {

        int badgeCount = pref.getInt("cardqnty", 0);
        if (badgeCount > 0) {
            cartCount.setText("" + badgeCount);
            cartCount.setVisibility(View.VISIBLE);
        } else {
            cartCount.setVisibility(View.GONE);
        }

        /*int favCount=pref.getInt("favcount",0);
        if(favCount>0){
            bottomNavigation.setCount(ID_FAVOURITE, String.valueOf(favCount));
        }
        else
        {
            bottomNavigation.clearCount(ID_FAVOURITE);
        }*/
    }


    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }


    private void showBloackDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainDrawerActivity.this);
        alertDialog.setCancelable(true);
        alertDialog.setMessage("You are blocked from backend.\n Please Contact with customer care!");
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }

    private void updateFirebaseToken(String token) {

        sessionManagement = new Session_management(this);
//        String user_id = sessionManagement.userId();
//        ServiceGenrator.getApiInterface().updateFirebaseToken(user_id, token).enqueue(
//                new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
//
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                    }
//                }
//        );
    }


    private void getLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(300000L);
        locationRequest.setFastestInterval(180000L);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        location = getLocation();
        if (location != null) {
            if (sessionManagement != null) {
                //     sessionManagement.setLocationPref(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                getAddress();
            }
        } else {
            setSupLocation();
        }
    }


    private void getAddress() {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(MainDrawerActivity.this, Locale.getDefault());
        DecimalFormat dFormat = new DecimalFormat("#.######");
        if (location != null) {
            double latitude = Double.parseDouble(dFormat.format(location.getLatitude()));
            double longitude = Double.parseDouble(dFormat.format(location.getLongitude()));

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("Address:\n");
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }

                String city = addresses.get(0).getLocality();
                String country = addresses.get(0).getCountryName();
//                sessionManagement.setCountry(country);
//                sessionManagement.setLocationCity(city);
                //       sessionManagement.setLocationPref(String.valueOf(latitude), String.valueOf(longitude));
                /* runOnUiThread(() -> addres.setText(returnedAddress.getAddressLine(0)));*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @SuppressLint("MissingPermission")
    public Location getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                setSupLocation();
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return null;
                    }
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }


    @SuppressLint("MissingPermission")
    private void setSupLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
//                    new FetchAddressTask(AddressLocationActivity.this, AddressLocationActivity.this).execute(location);
                getAddress();
            }
        });
    }

    private boolean checkAndRequestPermissions() {

        int locationPermission = ContextCompat.checkSelfPermission(MainDrawerActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(MainDrawerActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_LOCATION_PERMISSION);
            Toast.makeText(MainDrawerActivity.this, "Go to settings and enable Location permissions", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    @Override
    public void onLocationChanged(Location locations) {
        if (locations != null) {
//            Log.e(TAG, "onLocationChanged: " + locations.getLatitude() + "\n" + locations.getLongitude());
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
//                    if (!sessionManagement.getLatPref().equalsIgnoreCase("") && !sessionManagement.getLangPref().equalsIgnoreCase("")) {
//                        DecimalFormat dFormat = new DecimalFormat("##.#######");
//                        LatLng latLng = new LatLng(Double.parseDouble(sessionManagement.getLatPref()), Double.parseDouble(sessionManagement.getLangPref()));
//                        double latitude = Double.valueOf(dFormat.format(latLng.latitude));
//                        double longitude = Double.valueOf(dFormat.format(latLng.longitude));
////                        Log.i("TAG", latitude + "\n" + longitude);
//                        Location locationA = new Location("cal 1");
//                        locationA.setLatitude(latitude);
//                        locationA.setLongitude(longitude);
//                        double disInMetter = locationA.distanceTo(locations);
//                        double disData = disInMetter / 1000;
//                        DecimalFormat dFormatt = new DecimalFormat("#.#");
//                        disData = Double.parseDouble(dFormatt.format(disData));
////                        Log.i(TAG, "in" + disData);
//                        if (disData > 5.0) {
//                            if (!enterInFirst) {
//                                enterInFirst = true;
//                                location = locations;
//                                getAddress();
//
//                                if (selectedId == ID_HOME) {
//                                    loadFragment(new HomeFragment(fragmentClickListner));
//                                }
//                            }
//                        } else {
//                            enterInFirst = true;
//                           /* if (addres.getText().toString().equalsIgnoreCase("")) {
//                                if (selectedId == ID_HOME) {
//                                    loadFragment(new HomeFragment(fragmentClickListner));
//                                }
//                                getAddress();
//                            }*/
//                        }
//                    } else {
//                        enterInFirst = true;
//                        location = locations;
//                        if (selectedId == ID_HOME) {
//                            loadFragment(new HomeFragment(fragmentClickListner));
//                        }
//                        getAddress();
//                    }
                }
            }).start();

        }
    }

//    @Override
//    public void onClick(View view) {
//
//    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

//    @Override
//    public void onTaskCompleted(String result) {
//        ((TextView) findViewById(R.id.address)).setText(result);
//    }

//    @Override
//    protected void onDestroy() {
//        pref.unregisterOnSharedPreferenceChangeListener(this);
//        super.onDestroy();
//    }

//    @Override
//    public void onGetResponse(boolean isUpdateAvailable) {
//        Log.e("ResultAPPMAIN", String.valueOf(isUpdateAvailable));
//        if (isUpdateAvailable) {
//            showUpdateDialog();
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (resultCode == RESULT_OK){
//
//        }
        if (requestCode == 4) {
            if (data != null && data.getExtras() != null) {
//                String activityIdentify = data.getExtras().getString("actIdentfy");
//                if (activityIdentify != null && activityIdentify.equalsIgnoreCase("past")) {
//
//                }
                ArrayList<NewPendingDataModel> orderSubModels = (ArrayList<NewPendingDataModel>) data.getSerializableExtra("datalist");
                if (orderSubModels != null) {
//                    dbcart.clearCart();
//                    for (int i = 0; i < orderSubModels.size(); i++) {
//                        NewPendingDataModel odModel = orderSubModels.get(i);
//                        if (odModel.getDescription() != null && !odModel.getDescription().equalsIgnoreCase("")) {
//                            double price = Double.parseDouble(odModel.getPrice()) / Double.parseDouble(odModel.getQty());
//                            HashMap<String, String> map = new HashMap<>();
//                            map.put("varient_id", odModel.getVarient_id());
//                            map.put("product_name", odModel.getProduct_name());
//                            map.put("category_id", odModel.getVarient_id());
//                            map.put("title", odModel.getProduct_name());
//                            map.put("price", String.valueOf(price));
//                            map.put("mrp", odModel.getTotal_mrp());
//                            map.put("product_image", odModel.getVarient_image());
//                            map.put("status", "1");
//                            map.put("in_stock", "");
//                            map.put("unit_value", odModel.getQuantity() + "" + odModel.getUnit());
//                            map.put("unit", "");
//                            map.put("increament", "0");
//                            map.put("rewards", "0");
//                            map.put("stock", "0");
//                            map.put("product_description", odModel.getDescription());
//                            //map.put("product_description", odModel.getDescription());
//
//                            map.put("supplierID", odModel.getSupplierID());
//
//                            if (!odModel.getQty().equalsIgnoreCase("0")) {
//                                dbcart.setCart(map, Integer.parseInt(odModel.getQty()));
//                            } else {
//                                dbcart.removeItemFromCart(map.get("varient_id"));
//                            }
//
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                pref.edit().putInt("cardqnty", dbcart.getCartCount()).apply();
//                            }
//                        }
//                    }
//
//                    loadFragment(new CartFragment());

                }

            }

        } else if (requestCode == 22) {
//            DecimalFormat dFormat = new DecimalFormat("##.#######");
//            LatLng latLng = new LatLng(Double.parseDouble(sessionManagement.getLatPref()), Double.parseDouble(sessionManagement.getLangPref()));
//            double latitude = Double.valueOf(dFormat.format(latLng.latitude));
//            double longitude = Double.valueOf(dFormat.format(latLng.longitude));
//            location.setLatitude(latitude);
//            location.setLongitude(longitude);
//            Log.i("TAG 22", latitude + "\n" + longitude);
////            Location locationA = new Location("cal 1");
////                        Location locationB = new Location("cal 2");
////            locationA.setLatitude(latitude);
////            locationA.setLongitude(longitude);
////            location = locationA;
//            // updateAddress();
//            if (selectedId == ID_HOME) {
//                loadFragment(new HomeFragment(fragmentClickListner));
//            }
        } else if (resultCode == 8482) {
//            DecimalFormat dFormat = new DecimalFormat("##.#######");
//            LatLng latLng = new LatLng(Double.parseDouble(sessionManagement.getLatPref()), Double.parseDouble(sessionManagement.getLangPref()));
//            double latitude = Double.valueOf(dFormat.format(latLng.latitude));
//            double longitude = Double.valueOf(dFormat.format(latLng.longitude));
//            location.setLatitude(latitude);
//            location.setLongitude(longitude);
//            Log.i("TAG 8482", latitude + "\n" + longitude);
////            Location locationA = new Location("cal 1");
////                        Location locationB = new Location("cal 2");
////            locationA.setLatitude(latitude);
////            locationA.setLongitude(longitude);
////            location = locationA;
//            // updateAddress();
//            if (selectedId == ID_HOME) {
//                loadFragment(new HomeFragment(fragmentClickListner));
//            }
        } else if (requestCode == 121) {
            assert data != null;
//            String itemID = data.getExtras().getString("itemID");
//            Fragment homepage = new ProductDetailFragment();
//            FragmentTransaction fragmentManager = (MainDrawerActivity.this).getSupportFragmentManager()
//                    .beginTransaction();
//            Bundle bundle = new Bundle();
//            bundle.putString("itemID", itemID); //key and value
//            homepage.setArguments(bundle);
//            fragmentManager.replace(R.id.nav_supplier_fragment, homepage);
//            fragmentManager.commit();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private void getCartProducts() {
        String tag_json_obj = "json_cart_list_req";
        //String custID= sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
        String custID;


        Map<String, String> params = new HashMap<String, String>();
        if (sessionManagement.getUserDetails().get(BaseURL.KEY_ID) == null || sessionManagement.getUserDetails().get(BaseURL.KEY_ID).isEmpty()) {
            custID = "null";
            params.put("custID", custID);
        } else {
            custID = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
            params.put("custID", custID);
        }
        params.put("BranchCode", ApiInterface.branchcode);

        Log.e("getCart", "GetCart:" + params);
    }

//        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
//                ApiBaseURL.CartProducts, params, new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.e("CheckApiCart123", response.toString());
//
//                try {
//                    boolean status = response.getBoolean("status");
//
//                    if (status) {
//                        JSONArray jsonArray = response.getJSONArray("result");
//                        dbcart.clearCart();
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//                            HashMap<String, String> map = new HashMap<>();
//                            map.put("varient_id", jsonObject.getString("unitID"));
//                            map.put("product_name", jsonObject.getString("itemName"));
//                            map.put("category_id", jsonObject.getString("itemID"));
//                            map.put("title", jsonObject.getString("itemName"));
//                            map.put("price", jsonObject.getString("itemSellingprice"));
//                            map.put("product_image", jsonObject.getString("image"));
//                            map.put("status", "");
//                            map.put("stock", jsonObject.getString("stockingType"));
//                            map.put("vatRate", jsonObject.getString("vatRate"));
//                            map.put("unit_value", jsonObject.getString("uom"));
//                            map.put("increament", "0");
//                            map.put("product_description", jsonObject.getString("shortDes"));
//                            map.put("supplierID", jsonObject.getString("supplierID"));
//                            map.put("unitID", jsonObject.getString("unitID"));
//                            map.put("ItemId", jsonObject.getString("itemID"));
//                            int qty = jsonObject.getInt("quantity");
//                            dbcart.setCart(map, qty);
//                        }
//
//                        JSONObject deliveryChargesVAT = response.getJSONObject("deliveryChargesVAT");
//                        JSONArray vatResult = deliveryChargesVAT.getJSONArray("vatResult");
//
//
//                        SharedPreferences preferences = getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
//                        preferences.edit().putInt("cardqnty", dbcart.getCartCount()).apply();
//
//
//                        for (int i = 0; i < vatResult.length(); i++) {
//
//                            JSONObject object = vatResult.getJSONObject(i);
//
//                            if (object.getString("id").equals("1"))
//                                preferences.edit().putString("deliveryCharges", object.getString("rateInAMT")).apply();
//                            else
//                                preferences.edit().putString("vatRate", object.getString("rateInPer")).apply();
//
//                        }
//
//                        if (getIntent().getIntExtra("goToCart", 0) == 1) {
//                            startActivity(new Intent(MainDrawerActivity.this, CartActivity.class));
//                        } else {
//                            Log.e("gocart", "Go cart is 0");
//                        }
//
//
//                       /* if(returnTo.equals("Order")){
////                            loadFragment(new CartFragment());
//                            Intent intent = new Intent(MainDrawerActivity.this, OrderSummary.class);
//                            intent.putExtra("return","Order");
//                            startActivityForResult(intent,22);
//
//                        }*/
//                    } else {
//                        dbcart.clearCart();
//                        SharedPreferences preferences = getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
//                        preferences.edit().putInt("cardqnty", dbcart.getCartCount()).apply();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//                VolleyLog.d("", "Error: " + error.getMessage());
//                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                }
//            }
//        });
//
//        // Adding request to request queue
//        jsonObjReq.setRetryPolicy(new RetryPolicy() {
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
//        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
//
//    }
//
//
//    public void getAllFav() {
//
//        ServiceGenrator.getApiInterface().getFavouriteProductList(sessionManagement.getUserDetails().get(BaseURL.KEY_ID), "All").enqueue(
//                new Callback<RestItem>() {
//                    @Override
//                    public void onResponse(Call<RestItem> call, retrofit2.Response<RestItem> response) {
//
//                        if (response.isSuccessful()) {
//
//                            if (response.body().isStatus()) {
//
//                                ArrayList<ItemModel> itemModelArrayList = response.body().getResult();
//                                for (int i = 0; i < itemModelArrayList.size(); i++) {
//                                    ItemModel cc = itemModelArrayList.get(i);
//                                    HashMap<String, String> map = new HashMap<>();
//                                    map.put("varient_id", itemModelArrayList.get(i).getItemID());
//                                    map.put("product_name", itemModelArrayList.get(i).getItemName());
//                                    map.put("price", itemModelArrayList.get(i).getItemSellingprice());
//                                    map.put("product_image", itemModelArrayList.get(i).getImage());
//                                    map.put("unit_value", itemModelArrayList.get(i).getItemUnit());
//                                    map.put("product_description", itemModelArrayList.get(i).getShortDes());
//                                    map.put("supplierID", itemModelArrayList.get(i).getMainSupplier());
//
//                                    dbcart.setWishlist(map);
//
//                                }
//
//                                SharedPreferences preferences = getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
//                                preferences.edit().putInt("favcount", itemModelArrayList.size()).apply();
//                                int favCount = pref.getInt("favcount", 0);
//                                if (favCount > 0) {
//                                    // bottomNavigation.setCount(ID_FAVOURITE, String.valueOf(itemModelArrayList.size()));
//                                } else {
//                                    // bottomNavigation.clearCount(ID_FAVOURITE);
//                                }
//                            }
//
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<RestItem> call, Throwable t) {
//
//                    }
//                }
//        );
//    }
//
//
//    public void getCounts() {
//        ServiceGenrator.getApiInterface().getCounts(sessionManagement.getUserDetails().get(BaseURL.KEY_ID)).enqueue(new Callback<ResponseGetCounts>() {
//            @Override
//            public void onResponse(Call<ResponseGetCounts> call, retrofit2.Response<ResponseGetCounts> response) {
//                if (response.isSuccessful()) {
//
//                    if (response.body() != null) {
//                        if (response.body().isStatus()) {
//                            if (response.body().getResult() != null) {
//                                tvNotificationCount.setText(String.valueOf(response.body().getResult().getNotificationCount()));
//                                tvVoucherCount.setText(String.valueOf(response.body().getResult().getVoucharCount()));
//                                tvRecentViewCount.setText(String.valueOf(response.body().getResult().getRecentCount()));
//                                String.valueOf(response.body().getResult().getReelCount());
//                                if(response.body().getResult().getReelCount() != 0){
//                                    reelCount.setVisibility(View.VISIBLE);
//                                    reelCount.setText(String.valueOf(response.body().getResult().getReelCount()));
//                                }else{
//                                    reelCount.setVisibility(View.GONE);
//                                }
//                                /*if(response.body().getResult().getFavouriteCount()==0){
//                                    MainDrawerActivity.bottomNavigation.clearCount(ID_FAVOURITE);
//                                }else {
//                                    MainDrawerActivity.bottomNavigation.setCount(ID_FAVOURITE, String.valueOf(response.body().getResult().getFavouriteCount()));
//                                }*/
//                                SharedPreferences preferences = getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
//                                preferences.edit().putInt("favcount", response.body().getResult().getFavouriteCount()).apply();
//                                int favCount = pref.getInt("favcount", 0);
//                                if (favCount > 0) {
//                                    bottomNavigation.setCount(ID_FAVOURITE, String.valueOf(response.body().getResult().getFavouriteCount()));
//                                } else {
//                                    bottomNavigation.clearCount(ID_FAVOURITE);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseGetCounts> call, Throwable t) {
//
//            }
//        });
//    }
//
//    public void showUpdateDialog() {
//        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this);
//        alertDialogBuilder.setTitle(this.getString(R.string.app_name));
//        alertDialogBuilder.setMessage(this.getString(R.string.app_upto_date));
//        alertDialogBuilder.setCancelable(false);
//        alertDialogBuilder.setPositiveButton(R.string.updateNow, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
//                dialog.cancel();
//            }
//        });
//        alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        alertDialogBuilder.show();
//    }
//
    public void viewSelector(String fragmentName) {
        switch (fragmentName) {
            case "Home": {
                view_home.setBackgroundColor(getResources().getColor(R.color.white));
                view_dashboard.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_notification.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_recently_viewed.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_vouchers.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_rate_app.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_refer.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                break;
            }
            case "Dashboard": {
                view_home.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_dashboard.setBackgroundColor(getResources().getColor(R.color.white));
                view_notification.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_recently_viewed.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_vouchers.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_rate_app.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_refer.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                break;
            }
            case "Notification": {
                view_home.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_dashboard.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_notification.setBackgroundColor(getResources().getColor(R.color.white));
                view_recently_viewed.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_vouchers.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_rate_app.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_refer.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                break;
            }
            case "Recently Viewed": {
                view_home.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_dashboard.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_notification.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_recently_viewed.setBackgroundColor(getResources().getColor(R.color.white));
                view_vouchers.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_rate_app.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_refer.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                break;
            }

            case "Voucher": {
                view_home.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_dashboard.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_notification.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_recently_viewed.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_vouchers.setBackgroundColor(getResources().getColor(R.color.white));
                view_rate_app.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_refer.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                break;
            }
            case "Rate App": {
                view_home.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_dashboard.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_notification.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_recently_viewed.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_vouchers.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_rate_app.setBackgroundColor(getResources().getColor(R.color.white));
                view_refer.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                break;
            }
            case "Refer Earn": {
                view_home.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_dashboard.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_notification.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_recently_viewed.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_vouchers.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_rate_app.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                view_refer.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            }
        }

    }
//
//    private void makeGetSliderRequest() {
//        imageString.clear();
//        String tag_json_obj = "json_category_req";
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("parent", "");
//        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET, ApiBaseURL.BANNER, params,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            if (response != null && response.length() > 0) {
//                                boolean status = response.getBoolean("status");
//                                if (status) {
//
//                                    JSONArray jsonArray = response.getJSONArray("result");
//                                    if (jsonArray.length() <= 0) {
//                                        recyclerImages.setVisibility(View.GONE);
//                                    } else {
//                                        listarray.clear();
//                                        for (int i = 0; i < jsonArray.length(); i++) {
//                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                            HashMap<String, String> url_maps = new HashMap<String, String>();
//                                            url_maps.put("banner_name", jsonObject.getString("banner_name"));
//                                            url_maps.put("banner_id", jsonObject.getString("banner_id"));
//                                            url_maps.put("banner_image", ApiBaseURL.IMG_URL + jsonObject.getString("banner_image"));
//                                            imageString.add(ApiBaseURL.IMG_URL + jsonObject.getString("banner_image"));
//                                            listarray.add(url_maps);
//                                        }
//
//
//                                        bannerAdapter.notifyDataSetChanged();
//
//
//                                        final Timer timer = new Timer();
//                                        timer.schedule(new TimerTask() {
//
//                                            @Override
//                                            public void run() {
//
//                                                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() < (bannerAdapter.getItemCount() - 1)) {
//
//                                                    linearLayoutManager.smoothScrollToPosition(recyclerImages, new RecyclerView.State(), linearLayoutManager.findLastCompletelyVisibleItemPosition() + 1);
//                                                } else if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == (bannerAdapter.getItemCount() - 1)) {
//
//                                                    linearLayoutManager.smoothScrollToPosition(recyclerImages, new RecyclerView.State(), 0);
//                                                }
//                                            }
//                                        }, 0, time);
//
//                                    }
//                                } else {
//                                    recyclerImages.setVisibility(View.GONE);
//                                }
//                            } else {
//                                recyclerImages.setVisibility(View.GONE);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String, String> param = new HashMap<>();
//                return param;
//            }
//        };
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.getCache().clear();
//        requestQueue.add(jsonObjReq);
//        Log.e("request", requestQueue.toString());
//    }
//
//    @Override
//    public void onBackPressed() {
//        //super.onBackPressed();
//        BackPressDialog();
//    }
//
//    private void BackPressDialog() {
//        Dialog bottomSheetDialog = new Dialog(MainDrawerActivity.this);
//        bottomSheetDialog.setContentView(R.layout.item_layout_back_confirmorder);
//        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
//        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);
//
//        MaterialButton btn_orderConfirm_yes = bottomSheetDialog.findViewById(R.id.btn_orderConfirm_yes);
//        MaterialButton btn_orderConfirm_no = bottomSheetDialog.findViewById(R.id.btn_orderConfirm_no);
//        recyclerImages = bottomSheetDialog.findViewById(R.id.recycler_image_slider);
//
//        bottomSheetDialog.getWindow().setLayout(width, height);
//        bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
//        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        bottomSheetDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
//        bottomSheetDialog.show();
//        listarray = new ArrayList<>();
//        bannerAdapter = new BannerAdapter1(this, imageString, listarray);
//        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        recyclerImages.setAdapter(bannerAdapter);
//        recyclerImages.setLayoutManager(linearLayoutManager);
//        if (NetworkConnection.connectionChecking(MainDrawerActivity.this)) {
//            makeGetSliderRequest();
//        } else {
//            showToast(getString(R.string.no_internet));
//        }
//
//
//        btn_orderConfirm_no.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bottomSheetDialog.dismiss();
//                sessionManagement.setMainPopUp(false);
//                sessionManagement.setCategoryPopUp("");
//                finish();
//            }
//        });
//
//        btn_orderConfirm_yes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                bottomSheetDialog.dismiss();
//
//            }
//        });
//    }
//
//    void logoutAll() {
//        mAuth.signOut();
//        com.facebook.login.LoginManager.getInstance().logOut();
//        if (mGoogleSignInClient != null)
//            mGoogleSignInClient.signOut();
//    }
//
//    private void categoryUrl() {
//        cateList.clear();
//        // Tag used to cancel the request
//        String tag_json_obj = "json_get_address_req";
//
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("parent", "");
//        //params.put("BranchCode", ApiInterface.branchcode);
//
//        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET,
//                ApiBaseURL.Categories, params, new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d("categoryProduct", response.toString());
//                try {
//                    if (response != null && response.length() > 0) {
//                        boolean status = response.getBoolean("status");
//                        if (status) {
//                            JSONArray array = response.getJSONArray("result");
//                            for (int i = 0; i < array.length(); i++) {
//
//                                JSONObject object = array.getJSONObject(i);
//                                Category_model model = new Category_model();
//
//
//                                //model.setDetail(object.getString("description"));
//                                model.setCat_id(object.getString("categoryId"));
//                                model.setImage(object.getString("image"));
//                                model.setTitle(object.getString("categoryName"));
//                                ;
//
//                                //model.setSub_array(object.getJSONArray("subCategories"));
//                                cateList.add(model);
//                            }
//
//                        }
//                    } else {
//                        // Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//            }
//        });
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.getCache().clear();
//        jsonObjReq.setRetryPolicy(new RetryPolicy() {
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
//        requestQueue.add(jsonObjReq);
//
//    }
//
//
//    public static void updateProfileImage(String getId, ImageView imageView) {
//        Long time = System.currentTimeMillis();
//        Picasso.get()
//                .load(ApiBaseURL.IMG_URL_NEW + "profile_" + getId + ".png" + "?" + time)
//                .placeholder(R.drawable.toy_face)
//                .memoryPolicy(MemoryPolicy.NO_STORE, MemoryPolicy.NO_CACHE)
//                .into(imageView);
//    }
//
//    private void showToast(String message) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (sessionManagement.isLoggedIn()) {
//            if (NetworkConnection.connectionChecking(MainDrawerActivity.this)) {
//                new Thread(this::getCounts).start();
//            } else {
//                showToast(getString(R.string.no_internet));
//            }
//        }
//    }
//
//
//    private void checkAppUpdate(){
//        // Creates instance of the manager.
//        appUpdateManager = AppUpdateManagerFactory.create(this);
//
//        // Returns an intent object that you use to check for an update.
//        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
//
//        // Checks that the platform will allow the specified type of update.
//
//        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
//            Log.e("appUpdateInfo",String.valueOf(appUpdateInfo));
//            int onlineVersion = appUpdateInfo.availableVersionCode();
//            Log.e("onlineVersion",String.valueOf(onlineVersion));
//            Log.e("currentVersion",String.valueOf(currentVersion));
//
//            if(appUpdateInfo.updateAvailability()==UpdateAvailability.UPDATE_AVAILABLE){
//                showUpdateDialog();
//            }
//
//            /*if (currentVersion < onlineVersion) {
//                //show dialog
//                showUpdateDialog();
//            }*/
//        });
//    }

}