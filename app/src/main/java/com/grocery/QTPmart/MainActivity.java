package com.grocery.QTPmart;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
//import nl.joery.animatedbottombar.AnimatedBottomBar;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
//mimport com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
//import Adapters.FavouriteAdapter;
import Config.ApiBaseURL;
import Config.BaseURL;
//import Fragments.CartFragment;
//import Fragments.CategoryFragment;
//import Fragments.Contact_Us_fragment;
//import Fragments.Edit_profile_fragment;
//import Fragments.FavouriteFragment;
//import Fragments.HomeFragment;
//import Fragments.OrderFragment;
//import Fragments.OrderFragment;
//import Fragments.Reward_fragment;
//import Fragments.SearchFragment;
//import Fragments.Terms_and_Condition_fragment;
import ModelClass.ItemModel;
import ModelClass.NewPendingDataModel;
import com.grocery.QTPmart.R;
import network.ApiInterface;
import network.Response.RestItem;
import network.ServiceGenrator;
//import util.AppController;
//import util.CustomVolleyJsonRequest;
//import util.DatabaseHandler;
//import util.FetchAddressTask;
//import util.FragmentClickListner;
//import util.GooglePlayStoreAppVersionNameLoader;
import util.Session_management;
//import util.WSCallerVersionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import static Config.BaseURL.SupportUrl;
import static Config.BaseURL.TermsUrl;

public class MainActivity extends AppCompatActivity /*implements
        NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, FetchAddressTask.OnTaskCompleted,
        SharedPreferences.OnSharedPreferenceChangeListener,View.OnClickListener,
        WSCallerVersionListener*/ {

    private static final String TAG = MainActivity.class.getName();
    private static final int REQUEST_LOCATION_PERMISSION = 100;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 3000;

    private final static int ID_HOME = 1;
    private final static int ID_MY_ORDERS = 2;
    private final static int ID_SEARCH = 3;
    private final static int ID_FAVOURITE = 4;
    private final static int ID_CATEGORY = 5;

    LinearLayout My_Order, My_Reward, My_Walllet, My_Cart;
    NavigationView navigationView;
    LinearLayout viewpa;
    TextView username;
    Toolbar toolbar;
    ImageView bell;
    double latitude = 0.0, longitude = 0.0;
    boolean canGetLocation = false;

    private LocationManager locationManager;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
//    private DatabaseHandler dbcart;
    private Session_management sessionManagement;
    private ImageView cart;
    RelativeLayout cartLyt;
    private TextView cartCount;
    private Menu nav_menu;
    private ImageView iv_profile;
    private FusedLocationProviderClient mFusedLocationClient;
    private SharedPreferences pref;
    private DrawerLayout drawer;
    private LocationRequest locationRequest;
    private Location location;
    private boolean enterInFirst = false;
//    private FragmentClickListner fragmentClickListner;
    private TextView addres;
    String returnTo="";
    String id;

    FloatingActionButton fabMain, fabOne, fabTwo, fabThree, fabfour;
    LinearLayout parent_lay;

    Float translationY = 100f;
    OvershootInterpolator interpolator = new OvershootInterpolator();
    Boolean isMenuOpen = false;
//    private DatabaseHandler db;

    ActionBarDrawerToggle toggle;
    ImageView menuSlider;

//    public static AnimatedBottomBar bottomNavigation;
    private int selectedId = 1;

//    ViewPager view_pager;

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.BLACK);

        }


        initViews();
//        new GooglePlayStoreAppVersionNameLoader(getApplicationContext(), this).execute();

        initLocation();
        initFloatActions();
        initNavigationDrawer();
        initBadges();

        cartLyt.setOnClickListener(v->{
//            startActivity(new Intent(MainActivity.this, CartActivity.class));
        });

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                try {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    Fragment fr = getSupportFragmentManager().findFragmentById(R.id.contentPanel);

                    final String fm_name = fr.getClass().getSimpleName();
                    Log.e("backstack: ", ": " + fm_name);
                    if (fm_name.contentEquals("Home_fragment")) {
                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        toggle.setDrawerIndicatorEnabled(true);
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                        }
                        toggle.syncState();

                    } else if (fm_name.contentEquals("My_order_fragment") ||
                            fm_name.contentEquals("Thanks_fragment")) {
                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                        toggle.setDrawerIndicatorEnabled(false);
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        }
                        toggle.syncState();

                        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                HomeFragment fm = new HomeFragment(fragmentClickListner);
//                                FragmentManager fragmentManager = getSupportFragmentManager();
//                                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                                        .addToBackStack(null).commit();
                            }
                        });
                    } else {

                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                        toggle.setDrawerIndicatorEnabled(false);
                        if (getSupportActionBar() != null) {
                            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        }
                        toggle.syncState();

                        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                onBackPressed();
                            }
                        });
                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

//        db = new DatabaseHandler(this);
        sessionManagement = new Session_management(MainActivity.this);
        //dbcart.clearWishlist();
        //  getAllFav();

        //  if (db.getWishlistCount() != 0){
        if(sessionManagement.isLoggedIn()){
            showFavourites();
        }
        //  }

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {

                Log.e("token", task.getResult().getToken());

//                String user_id = sessionManagement.userId();
//                if(!user_id.equals(""));
                // updateFirebaseToken(task.getResult().getToken());
            }
        });

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {

//        bottomNavigation = findViewById(R.id.bottomNavigation);
        initComponent();

        //navigation = findViewById(R.id.nav_view12);
        bell = findViewById(R.id.bell);
        cart = findViewById(R.id.cart);
        cartLyt = findViewById(R.id.cartLyt);
        cartCount = findViewById(R.id.cartCount);

        fabMain = findViewById(R.id.fabMain);
        fabOne = findViewById(R.id.fabOne);
        fabTwo = findViewById(R.id.fabTwo);
        fabThree = findViewById(R.id.fabThree);
        fabfour = findViewById(R.id.fabfour);

        parent_lay = findViewById(R.id.parent_lay);

        fabOne.setAlpha(0f);
        fabTwo.setAlpha(0f);
        fabThree.setAlpha(0f);
        fabfour.setAlpha(0f);

        fabOne.setTranslationY(translationY);
        fabTwo.setTranslationY(translationY);
        fabThree.setTranslationY(translationY);
        fabfour.setTranslationY(translationY);

        fabMain.setOnClickListener(v -> {
            if (isMenuOpen) {
                closeMenu();
            } else {
                openMenu();
            }
        });

        fabOne.setOnClickListener(v -> {
            String url = "https://api.whatsapp.com/send?phone=" + "+917058455132";
            try {
                PackageManager pm = getPackageManager();
                pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            } catch (PackageManager.NameNotFoundException e) {
                Toast.makeText(MainActivity.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });

        fabTwo.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + "+917058455132"));
            startActivity(intent);
        });

        fabThree.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"qtpmart@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Query regarding QTP Mart");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        });

        fabfour.setOnClickListener(v -> {
            String url = SupportUrl;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

    }

    private void openMenu() {
        isMenuOpen = !isMenuOpen;
        fabMain.animate().setInterpolator(interpolator).rotation(45f).setDuration(300).start();
        fabOne.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabTwo.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabThree.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabfour.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
    }

    private void closeMenu() {
        isMenuOpen = !isMenuOpen;
        fabMain.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();
        fabOne.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabTwo.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabThree.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabfour.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
    }

    private void initNavigationDrawer() {
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
//        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
//                if (id == R.id.nav_home) {
//                    bottomNavigation.selectTabById(R.id.navigation_home, true);
//                } else if (id == R.id.nav_my_orders) {
//                    bottomNavigation.selectTabById(R.id.navigation_my_orders, true);
//                } else if (id == R.id.nav_my_profile) {
//                    //loadFragment(new Edit_profile_fragment());
//                } else if (id == R.id.nav_logout) {
//                    sessionManagement.logoutSession();
//                    finish();
//                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        View headerView = navigationView.getHeaderView(0);
        username = headerView.findViewById(R.id.tv_header_name);
        iv_profile = headerView.findViewById(R.id.iv_header_img);
//        addres = headerView.findViewById(R.id.tv_header_address);

        if (sessionManagement.isLoggedIn()) {
            username.setText(sessionManagement.getUserDetails().get(BaseURL.KEY_NAME));
        }

    }

    private void initLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    private void initFloatActions() {

    }

    private void initBadges() {
        //dbcart = new DatabaseHandler(this);
        //updateCartCount();
    }

    public void updateCartCount() {
        //int count = dbcart.getCartCount();
        //if (count \u003e 0) {
        //    cartCount.setVisibility(View.VISIBLE);
        //    cartCount.setText(String.valueOf(count));
        //} else {
        //    cartCount.setVisibility(View.GONE);
        //}
    }


    private void showFavourites() {

        LayoutInflater inflater = getLayoutInflater();

//        View view = inflater.inflate(R.layout.layout_popup_favourite, null);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

//        final PopupWindow popupWindow = new PopupWindow(view, width, height, false);

//        popupWindow.setBackgroundDrawable(new BitmapDrawable());
//        popupWindow.setOutsideTouchable(false);
//        popupWindow.setTouchable(true);
//
//
//        RecyclerView recyclerView = view.findViewById(R.id.recyclerCart);
//        ImageView close = view.findViewById(R.id.close);

//        ServiceGenrator.getApiInterface().getFavouriteProductList(sessionManagement.getUserDetails().get(BaseURL.KEY_ID),"4").enqueue(
//                new Callback<RestItem>() {
//                    @Override
//                    public void onResponse(Call<RestItem> call, retrofit2.Response<RestItem> response) {
//
//                        if (response.isSuccessful()) {
//
//                            if (response.body().isStatus()) {
//
////                                popupWindow.showAtLocation(drawer, Gravity.CENTER, 0, 0);
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                    drawer.getForeground().setAlpha(150);
//                                }

//                                ArrayList<ItemModel> tempArrayList=response.body().getResult();
//                                ArrayList<ItemModel> itemModelArrayList=new ArrayList<>();
//                                for(int i=0;i<tempArrayList.size();i++) {
//                                    ItemModel favitem=new ItemModel();
//
//                                    favitem.setItemID(tempArrayList.get(i).getItemID());
//                                    favitem.setItemName(tempArrayList.get(i).getItemName());
//                                    favitem.setShortDes(tempArrayList.get(i).getShortDes());
//                                    favitem.setImage(tempArrayList.get(i).getImage());
//                                    favitem.setItemUnit(tempArrayList.get(i).getItemUnit());
//                                    favitem.setMainSupplier(tempArrayList.get(i).getMainSupplier());
//                                    favitem.setVatRate(tempArrayList.get(i).getVatRate());
//                                    favitem.setFeedback(tempArrayList.get(i).getFeedback());
//                                    favitem.setDiscount(tempArrayList.get(i).getDiscount());
//
//
//                                    if (tempArrayList.get(i).getFixedPrice() != null && Double.parseDouble(tempArrayList.get(i).getFixedPrice()) > 0) {
//                                        favitem.setItemSellingprice(tempArrayList.get(i).getFixedPrice());
//                                        favitem.setFixedPrice(tempArrayList.get(i).getItemSellingprice());
//                                    } else {
//                                        favitem.setFixedPrice(tempArrayList.get(i).getFixedPrice());
//                                        favitem.setItemSellingprice(tempArrayList.get(i).getItemSellingprice());
//                                    }
//                                    itemModelArrayList.add(favitem);
//                                }
//                                FavouriteAdapter favouriteAdapter = new FavouriteAdapter(MainActivity.this, itemModelArrayList,recyclerView);
//
//                                recyclerView.setAdapter(favouriteAdapter);
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
//        // recyclerView.setAdapter(new FavouriteAdapter(this, map, null,recyclerView));
//
//        Button viewAll = view.findViewById(R.id.viewAll);
//        viewAll.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.M)
//            @Override
//            public void onClick(View view) {
//
//                bottomNavigation.selectTabById(R.id.navigation_favourite, true);
//                loadFragment(new FavouriteFragment());
//                drawer.getForeground().setAlpha(0);
//                popupWindow.dismiss();
//
//            }
//        });


        /*recyclerView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return map.size();
            }

            @Override
            public Object getItem(int i) {
                return map.get(i);
            }

            @Override
            public long getItemId(int i) {
                return 0;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {

                view = inflater.inflate(R.layout.favourite_layout_item, null);

                view.findViewById(R.id.txt_close).setVisibility(View.GONE);
                view.findViewById(R.id.quantityLayout).setVisibility(View.GONE);

                HashMap<String, String> item = map.get(i);

                ((TextView) view.findViewById(R.id.currency_indicator)).setText(sessionManagement.getCurrency());

                Picasso.with(MainActivity.this)
                        .load(ApiBaseURL.IMG_URL + item.get("product_image"))
                        .into((ImageView)view.findViewById(R.id.prodImage));

                ((TextView) view.findViewById(R.id.txt_pName)).setText(item.get("product_name"));

                double sprice = Double.parseDouble(item.get("price"));
                String p = String.format("%.2f",sprice);

                ((TextView)view.findViewById(R.id.txt_Pprice1)).setText(p.substring(0, p.length()-3));
                ((TextView)view.findViewById(R.id.txt_Pprice2)).setText(p.substring(p.length()-3));

                view.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onClick(View view) {

                        fragmentClickListner.loadFavourites();
                        popupWindow.dismiss();
                        drawer.getForeground().setAlpha(0);
                    }
                });

                return view;
            }
        });*/

//        new Handler().postDelayed(new Runnable() {
//            @SuppressLint("NewApi")
//            @Override
//            public void run() {
//
//                if (fav.size() > 0) {
//                    popupWindow.showAtLocation(drawer, Gravity.CENTER, 0, 0);
//                    drawer.getForeground().setAlpha(150);
//                }
//            }
//        }, 1000);

//        close.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("NewApi")
//            @Override
//            public void onClick(View view) {
//
//                popupWindow.dismiss();
//                drawer.getForeground().setAlpha(0);
//            }
//        });
//
//
//
    }

    public void loadFragment(Fragment fragment) {
        this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentPanel, fragment)
                .commitAllowingStateLoss();
    }

    private void initComponent() {
//        bottomNavigation.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
//            @Override
//            public void onTabSelected(int lastIndex, @Nullable AnimatedBottomBar.Tab lastTab, int newIndex, @NonNull AnimatedBottomBar.Tab newTab) {
//                int id = newTab.getId();
//                if (id == R.id.navigation_home) {
//                    parent_lay.setVisibility(View.VISIBLE);
//                    //loadFragment(new HomeFragment(fragmentClickListner));
//                    selectedId = ID_HOME;
//                } else if (id == R.id.navigation_my_orders) {
//                    parent_lay.setVisibility(View.GONE);
//                    //loadFragment(new OrderFragment());
//                    selectedId = ID_MY_ORDERS;
//                } else if (id == R.id.navigation_search) {
//                    parent_lay.setVisibility(View.GONE);
//                    //loadFragment(new SearchFragment());
//                    selectedId = ID_SEARCH;
//                } else if (id == R.id.navigation_favourite) {
//                    parent_lay.setVisibility(View.GONE);
//                    //loadFragment(new FavouriteFragment());
//                    selectedId = ID_FAVOURITE;
//                } else if (id == R.id.navigation_category) {
//                    parent_lay.setVisibility(View.GONE);
//                    //loadFragment(new CategoryFragment(fragmentClickListner));
//                    selectedId = ID_CATEGORY;
//                }
//            }
//
//            @Override
//            public void onTabReselected(int index, @NonNull AnimatedBottomBar.Tab tab) {
//
//            }
//        });
    }

}
