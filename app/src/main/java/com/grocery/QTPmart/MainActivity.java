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

import activities.CartActivity;
import activities.LoginActivity;
import nl.joery.animatedbottombar.AnimatedBottomBar;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.android.gms.common.ConnectionResult;
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
import adapters.FavouriteAdapter;
import Config.ApiBaseURL;
import Config.BaseURL;
import fragments.CartFragment;
import fragments.CategoryFragment;
import fragments.Contact_Us_fragment;
import fragments.Edit_profile_fragment;
import fragments.FavouriteFragment;
import fragments.HomeFragment;
import fragments.OrderFragment;
import fragments.Reward_fragment;
import fragments.SearchFragment;
import fragments.Terms_and_Condition_fragment;
import ModelClass.ItemModel;
import ModelClass.NewPendingDataModel;
import com.grocery.QTPmart.R;
import network.ApiInterface;
import network.Response.RestItem;
import network.ServiceGenrator;
import util.AppController;
import util.CustomVolleyJsonRequest;
import util.DatabaseHandler;
import util.FetchAddressTask;
import util.FragmentClickListner;
import util.GooglePlayStoreAppVersionNameLoader;
import util.Session_management;
import util.WSCallerVersionListener;

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

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, FetchAddressTask.OnTaskCompleted,
        SharedPreferences.OnSharedPreferenceChangeListener,View.OnClickListener,
        WSCallerVersionListener {

    private static final String TAG = MainActivity.class.getName();
    private static final int REQUEST_LOCATION_PERMISSION = 100;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 3000;

    private final static int ID_HOME = R.id.navigation_home;
    private final static int ID_MY_ORDERS = R.id.navigation_my_orders;
    private final static int ID_SEARCH = R.id.navigation_search;
    private final static int ID_FAVOURITE = R.id.navigation_favourite;
    private final static int ID_CATEGORY = R.id.navigation_category;

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
    private DatabaseHandler dbcart;
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
    private FragmentClickListner fragmentClickListner;
    private TextView addres;
    String returnTo="";
    String id;

    FloatingActionButton fabMain, fabOne, fabTwo, fabThree, fabfour;
    LinearLayout parent_lay;

    Float translationY = 100f;
    OvershootInterpolator interpolator = new OvershootInterpolator();
    Boolean isMenuOpen = false;
    private DatabaseHandler db;

    ActionBarDrawerToggle toggle;
    ImageView menuSlider;

    public static AnimatedBottomBar bottomNavigation;
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
        new GooglePlayStoreAppVersionNameLoader(getApplicationContext(), this).execute();

        initLocation();
        initFloatActions();
        initNavigationDrawer();
        initBadges();

        cartLyt.setOnClickListener(v->{
            startActivity(new Intent(MainActivity.this, CartActivity.class));
        });
//        cart.setOnClickListener(v -> {
//
//            startActivity(new Intent(MainActivity.this, CartActivity.class));
//
//        });

        fragmentClickListner = new FragmentClickListner() {
            @Override
            public void onFragmentClick(boolean open) {
                if (open) {
                    bottomNavigation.selectTabById(ID_HOME, true);
                    loadFragment(new CartFragment());
                }
            }

            public void loadFavourites() {

                bottomNavigation.selectTabById(ID_FAVOURITE, true);
                loadFragment(new FavouriteFragment());
            }

            @Override
            public void onChangeHome(boolean open) {
                DecimalFormat dFormat = new DecimalFormat("##.#######");
                LatLng latLng = new LatLng(Double.parseDouble(sessionManagement.getLatPref()), Double.parseDouble(sessionManagement.getLangPref()));
                double latitude = Double.valueOf(dFormat.format(latLng.latitude));
                double longitude = Double.valueOf(dFormat.format(latLng.longitude));
                location.setLatitude(latitude);
                location.setLongitude(longitude);
                getAddress();
                bottomNavigation.selectTabById(ID_HOME, true);
                loadFragment(new HomeFragment(fragmentClickListner));
            }
        };

        initComponent();
        bottomNavigation.selectTabById(ID_HOME, true);
        loadFragment(new HomeFragment(fragmentClickListner));


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
                                HomeFragment fm = new HomeFragment(fragmentClickListner);
                                FragmentManager fragmentManager = getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                                        .addToBackStack(null).commit();
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

        db = new DatabaseHandler(this);
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

                String user_id = sessionManagement.userId();
                if(!user_id.equals(""));
                // updateFirebaseToken(task.getResult().getToken());
            }
        });

//        HomeViewPagerAdapter homeViewPagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager(), fragmentClickListner);
//        view_pager.setAdapter(homeViewPagerAdapter);
//        view_pager.setCurrentItem(0);
//
//        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                switch (position) {
//                    case 0:
//                        bottomNavigation.selectTabById(ID_HOME, true);
//                        break;
//                    case 1:
//                        bottomNavigation.selectTabById(ID_CATEGORY, true);
//
//                        break;
//                    case 2:
//                        bottomNavigation.selectTabById(ID_SEARCH, true);
//                        break;
//                    case 3:
//                        bottomNavigation.selectTabById(ID_FAVOURITE, true);
//                        break;
//                    case 4:
//                        bottomNavigation.selectTabById(ID_MY_ORDERS, true);
//                        break;
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

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
        navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        menuSlider = findViewById(R.id.sliderr);
//        view_pager = findViewById(R.id.view_pager);


    }

    private void initFloatActions() {

        fabOne.setAlpha(0f);
        fabTwo.setAlpha(0f);
        fabThree.setAlpha(0f);
        fabfour.setAlpha(0f);

        fabOne.setTranslationY(translationY);
        fabTwo.setTranslationY(translationY);
        fabThree.setTranslationY(translationY);
        fabfour.setTranslationY(translationY);

        fabMain.setOnClickListener(this);
        fabOne.setOnClickListener(this);
        fabTwo.setOnClickListener(this);
        fabThree.setOnClickListener(this);
        fabfour.setOnClickListener(this);


        closeMenu(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initNavigationDrawer() {

        drawer.getForeground().setAlpha(0);
        menuSlider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        View header = navigationView.getHeaderView(0);
        navigationView.getBackground().setColorFilter(0x80000000, PorterDuff.Mode.MULTIPLY);
        navigationView.setNavigationItemSelectedListener(this);
        nav_menu = navigationView.getMenu();
        viewpa = header.findViewById(R.id.viewpa);
       /* if (sessionManagement.isLoggedIn()) {
            viewpa.setVisibility(View.GONE);
        }*/


        My_Order = header.findViewById(R.id.my_orders);
        My_Reward = header.findViewById(R.id.my_reward);
        My_Walllet = header.findViewById(R.id.my_wallet);
        My_Cart = header.findViewById(R.id.my_cart);
        iv_profile = header.findViewById(R.id.iv_header_img);
        username = header.findViewById(R.id.tv_header_name);


        My_Order.setOnClickListener(v -> {
            drawer.closeDrawer(GravityCompat.START);
            if (sessionManagement.isLoggedIn()) {
//                Intent intent = new Intent(MainActivity.this, My_Order_activity.class);
//                startActivityForResult(intent, 4);
            } else {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        My_Reward.setOnClickListener(v -> {
            if (sessionManagement.isLoggedIn()) {

                drawer.closeDrawer(GravityCompat.START);

                Reward_fragment fm = new Reward_fragment();
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.contentPanel, fm);
                transaction.commit();

            } else {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }

        });

        My_Walllet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManagement.isLoggedIn()) {
                    drawer.closeDrawer(GravityCompat.START);
                    if (sessionManagement.userBlockStatus().equalsIgnoreCase("2")) {
//                        Wallet_fragment fm = new Wallet_fragment();
//                        FragmentManager manager = getSupportFragmentManager();
//                        FragmentTransaction transaction = manager.beginTransaction();
//                        transaction.replace(R.id.contentPanel, fm);
//                        transaction.commit();
                    } else {
                        showBloackDialog();
                    }


//                    Wallet_fragment fm = new Wallet_fragment();
//                    android.app.FragmentManager fragmentManager = getFragmentManager();
//                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                            .addToBackStack(null).commit();
                } else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        My_Cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dbcart.getCartCount() > 0) {
                    CartFragment favourite_fragment = new CartFragment();
                    FragmentManager manager1 = getSupportFragmentManager();
                    FragmentTransaction transaction1 = manager1.beginTransaction();
                    transaction1.replace(R.id.contentPanel, favourite_fragment);
                    transaction1.addToBackStack(null);
                    transaction1.commit();
                } else {
                    Toast.makeText(MainActivity.this, "No Item in Cart", Toast.LENGTH_SHORT).show();
                }
            }
        });

        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sessionManagement.isLoggedIn()) {
                    Edit_profile_fragment fm = new Edit_profile_fragment();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                            .addToBackStack(null).commit();
                } else {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                }
            }
        });

        sideMenu();

        if(sessionManagement.isLoggedIn()) {
            new Thread(this::getCartProducts).start();
        }
    }

    private void initLocation() {

        sessionManagement = new Session_management(MainActivity.this);
        dbcart = new DatabaseHandler(this);
        pref = getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
        pref.registerOnSharedPreferenceChangeListener(this);

        id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);

        addres = findViewById(R.id.address);
//        addres.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                startActivity(new Intent(MainActivity.this, AddressLocationActivity.class));
//            }
//        });
        returnTo =getIntent().getStringExtra("return");
        if(returnTo==null){
            returnTo="";
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

        int favCount=pref.getInt("favcount",0);
        // setBadge(ID_FAVOURITE, String.valueOf(favCount));

        /*pref.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
               bottomNavigation.setCount(ID_FAVOURITE, String.valueOf(dbcart.getWishlistCount()));
            }
        });*/

        if(favCount>0){
            setBadge(ID_FAVOURITE, String.valueOf(favCount));
        }
        else
        {
            bottomNavigation.clearBadgeAtTabId(ID_FAVOURITE);
        }
    }




    private void showFavourites() {

        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_popup_favourite, null);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        final PopupWindow popupWindow = new PopupWindow(view, width, height, false);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(false);
        popupWindow.setTouchable(true);


        RecyclerView recyclerView = view.findViewById(R.id.recyclerCart);
        ImageView close = view.findViewById(R.id.close);

        ServiceGenrator.getApiInterface().getFavouriteProductList(sessionManagement.getUserDetails().get(BaseURL.KEY_ID),"4").enqueue(
                new Callback<RestItem>() {
                    @Override
                    public void onResponse(Call<RestItem> call, retrofit2.Response<RestItem> response) {

                        if (response.isSuccessful()) {

                            if (response.body().isStatus()) {

                                popupWindow.showAtLocation(drawer, Gravity.CENTER, 0, 0);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    drawer.getForeground().setAlpha(150);
                                }

                                ArrayList<ItemModel> tempArrayList=response.body().getResult();
                                ArrayList<ItemModel> itemModelArrayList=new ArrayList<>();
                                for(int i=0;i<tempArrayList.size();i++) {
                                    ItemModel favitem=new ItemModel();

                                    favitem.setItemID(tempArrayList.get(i).getItemID());
                                    favitem.setItemName(tempArrayList.get(i).getItemName());
                                    favitem.setShortDes(tempArrayList.get(i).getShortDes());
                                    favitem.setImage(tempArrayList.get(i).getImage());
                                    favitem.setItemUnit(tempArrayList.get(i).getItemUnit());
                                    favitem.setMainSupplier(tempArrayList.get(i).getMainSupplier());
                                    favitem.setVatRate(tempArrayList.get(i).getVatRate());
                                    favitem.setFeedback(tempArrayList.get(i).getFeedback());
                                    favitem.setDiscount(tempArrayList.get(i).getDiscount());


                                    if (tempArrayList.get(i).getFixedPrice() != null && Double.parseDouble(tempArrayList.get(i).getFixedPrice()) > 0) {
                                        favitem.setItemSellingprice(tempArrayList.get(i).getFixedPrice());
                                        favitem.setFixedPrice(tempArrayList.get(i).getItemSellingprice());
                                    } else {
                                        favitem.setFixedPrice(tempArrayList.get(i).getFixedPrice());
                                        favitem.setItemSellingprice(tempArrayList.get(i).getItemSellingprice());
                                    }
                                    itemModelArrayList.add(favitem);
                                }
                                FavouriteAdapter favouriteAdapter = new FavouriteAdapter(MainActivity.this, itemModelArrayList,recyclerView);

                                recyclerView.setAdapter(favouriteAdapter);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<RestItem> call, Throwable t) {

                    }
                }
        );
        // recyclerView.setAdapter(new FavouriteAdapter(this, map, null,recyclerView));

        Button viewAll = view.findViewById(R.id.viewAll);
        viewAll.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                bottomNavigation.selectTabById(ID_FAVOURITE, true);
                loadFragment(new FavouriteFragment());
                drawer.getForeground().setAlpha(0);
                popupWindow.dismiss();

            }
        });


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

        close.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {

                popupWindow.dismiss();
                drawer.getForeground().setAlpha(0);
            }
        });



    }

    private void openMenu() {
        isMenuOpen = !isMenuOpen;
        fabMain.animate().setInterpolator(interpolator).rotation(45f).setDuration(300).start();

        fabOne.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabOne.setVisibility(View.VISIBLE);
        fabTwo.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabTwo.setVisibility(View.VISIBLE);
        fabThree.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabThree.setVisibility(View.VISIBLE);
        fabfour.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabfour.setVisibility(View.VISIBLE);
    }

    private void closeMenu() {
        isMenuOpen = !isMenuOpen;

        fabMain.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();

        fabOne.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabOne.setVisibility(View.GONE);
        fabTwo.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabTwo.setVisibility(View.GONE);
        fabThree.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabThree.setVisibility(View.GONE);
        fabfour.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabfour.setVisibility(View.GONE);
    }

    private void closeMenu(boolean value) {
        isMenuOpen = value;

        fabMain.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();

        fabOne.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabOne.setVisibility(View.GONE);
        fabTwo.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabTwo.setVisibility(View.GONE);
        fabThree.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabThree.setVisibility(View.GONE);
        fabfour.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabfour.setVisibility(View.GONE);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.fabMain) {
            Log.i(TAG, "onClick: fab main");
            if (isMenuOpen) {
                fabOne.setVisibility(View.GONE);
                fabTwo.setVisibility(View.GONE);
                fabThree.setVisibility(View.GONE);
                fabfour.setVisibility(View.GONE);
                closeMenu();
            } else {
                fabOne.setVisibility(View.VISIBLE);
                fabTwo.setVisibility(View.VISIBLE);
                fabThree.setVisibility(View.VISIBLE);
                fabfour.setVisibility(View.VISIBLE);
                openMenu();
            }
        } else if (id == R.id.fabOne) {
            Intent sendIntent1 = new Intent();
            sendIntent1.setAction(Intent.ACTION_SEND);
            sendIntent1.putExtra(Intent.EXTRA_TEXT, "Hi friends i am using ." + " http://play.google.com/store/apps/details?id=" + getPackageName() + " APP");
            sendIntent1.setType("text/plain");
            startActivity(sendIntent1);

            Log.i(TAG, "onClick: fab one");
            handleFabOne();
            if (isMenuOpen) {
                closeMenu();
            } else {
                openMenu();
            }
        } else if (id == R.id.fabTwo) {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
            }
        } else if (id == R.id.fabThree) {
            String smsNumber = "971504413221";
            openWhatsApp(smsNumber);
        } else if (id == R.id.fabfour) {
            if (isPermissionGranted()) {
                call_action();
            }

            Log.i(TAG, "onClick: fab four");
        }
    }

    private void openWhatsApp(String numberwhats) {
        boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
        if (isWhatsappInstalled) {
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(numberwhats) + "@s.whatsapp.net");//phone number without "+" prefix
            startActivity(sendIntent);
        } else {
            Uri uri = Uri.parse("market://details?id=com.whatsapp");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
            startActivity(goToMarket);
        }
    }

    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                    call_action();
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            case  REQUEST_LOCATION_PERMISSION : {// If the permission is granted, get the location,
                // otherwise, show a Toast
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    getLocation();
                    location = getLocation();
                    if (location != null) {
                        getAddress();
                    }
                    Log.e(TAG, "Granted");
//                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    return;
//                }
//                mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        if (location != null) {
////                                Log.e(TAG, "location create" + location.getLatitude() + " , " + location.getLongitude());
//                            new FetchAddressTask(MainActivity.this, MainActivity.this).execute(location);
//                        }
//                    }
//                });


                } else {
//                    Log.e(TAG, "permission denied" );

                    Toast.makeText(MainActivity.this, "Location permission is necessary", Toast.LENGTH_SHORT).show();
                    finish();

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void call_action() {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + "+971 504413221"));
        startActivity(callIntent);

    }

    private void handleFabOne() {
        Log.i(TAG, "handleFabOne: ");
    }

    private void showBloackDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
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

    @Override
    protected void onStart() {
        // new Thread(this::fetchBlockStatus).start();
        super.onStart();
    }

    private void updateFirebaseToken(String token) {

        sessionManagement = new Session_management(this);
        String user_id = sessionManagement.userId();
        ServiceGenrator.getApiInterface().updateFirebaseToken(user_id, token).enqueue(
                new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {


                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                }
        );
    }

    private void getLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(300000L);
        locationRequest.setFastestInterval(180000L);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        location = getLocation();
        if (location != null) {
            if (sessionManagement != null) {
                //  sessionManagement.setLocationPref(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                getAddress();
            }
        } else {
            setSupLocation();
        }
    }

    private void getAddress() {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        DecimalFormat dFormat = new DecimalFormat("#.######");
        if(location!=null) {
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
                sessionManagement.setCountry(country);
                sessionManagement.setLocationCity(city);
                // sessionManagement.setLocationPref(String.valueOf(latitude), String.valueOf(longitude));
                runOnUiThread(() -> addres.setText(returnedAddress.getAddressLine(0)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

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

        int locationPermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(MainActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_LOCATION_PERMISSION);
            Toast.makeText(MainActivity.this, "Go to settings and enable Location permissions", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        //stop
//        mFusedLocationClient.removeLocationUpdates(mLocationCallback);

    }

    @Override
    public boolean onSupportNavigateUp() {
       /* NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();*/
        return false;
    }

    public void loadFragment(Fragment fragment) {
        this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentPanel, fragment)
                .commitAllowingStateLoss();
    }

    private void initComponent() {
        bottomNavigation.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int lastIndex, @Nullable AnimatedBottomBar.Tab lastTab, int newIndex, @NonNull AnimatedBottomBar.Tab newTab) {
                int id = newTab.getId();
                if (id == R.id.navigation_home) {
                    parent_lay.setVisibility(View.VISIBLE);
                    loadFragment(new HomeFragment(fragmentClickListner));
                    selectedId = ID_HOME;
                } else if (id == R.id.navigation_my_orders) {
                    parent_lay.setVisibility(View.GONE);
                    loadFragment(new OrderFragment());
                    selectedId = ID_MY_ORDERS;
                } else if (id == R.id.navigation_search) {
                    parent_lay.setVisibility(View.GONE);
                    loadFragment(new SearchFragment());
                    selectedId = ID_SEARCH;
                } else if (id == R.id.navigation_favourite) {
                    parent_lay.setVisibility(View.GONE);
                    loadFragment(new FavouriteFragment());
                    selectedId = ID_FAVOURITE;
                } else if (id == R.id.navigation_category) {
                    parent_lay.setVisibility(View.GONE);
                    loadFragment(new CategoryFragment(fragmentClickListner));
                    selectedId = ID_CATEGORY;
                }
            }

            @Override
            public void onTabReselected(int index, @NonNull AnimatedBottomBar.Tab tab) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fm = null;
        Bundle args = new Bundle();
        if (id == R.id.sign) {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
        }

        else if (id == R.id.nav_my_profile) {
            // startActivity(new Intent(this, ProfileActivity.class));

        } else if (id == R.id.nav_aboutus) {
//            startActivity(new Intent(getApplicationContext(), About_us.class));
        } else if (id == R.id.nav_policy) {
            fm = new Terms_and_Condition_fragment();
            args.putString("url", TermsUrl);
            args.putString("title", getResources().getString(R.string.nav_terms));
            fm.setArguments(args);
        }

        else if (id == R.id.nav_contact) {
            fm = new Contact_Us_fragment();
            args.putString("url", SupportUrl);
            args.putString("title", getResources().getString(R.string.nav_terms));
            fm.setArguments(args);

        }

        else if (id == R.id.nav_share) {
            shareApp();
        } else if (id == R.id.nav_logout) {
            dbcart.clearCart();
            dbcart.clearWishlist();
            sessionManagement.logoutSession();
            sessionManagement.setCurrency("AED ", "AED ");
            finish();

        }

        if (fm != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                    .addToBackStack(null).commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void shareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi friends i am using ." + " http://play.google.com/store/apps/details?id=" + getPackageName() + " APP"); //getPackageName()
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void sideMenu() {

        if (sessionManagement.isLoggedIn()) {
            //  tv_number.setVisibility(View.VISIBLE);
            nav_menu.findItem(R.id.nav_logout).setVisible(true);
            nav_menu.findItem(R.id.nav_my_profile).setVisible(true);
            //   nav_menu.findItem(R.id.login).setVisible(true);
            nav_menu.findItem(R.id.sign).setVisible(false);
            nav_menu.findItem(R.id.nav_powerd).setVisible(true);

            username.setText("Welcome! " +
                    "" + sessionManagement.getUserDetails().get(BaseURL.KEY_NAME));

//            nav_menu.findItem(R.id.signup).setVisible(false);

//            nav_menu.findItem(R.id.nav_user).setVisible(true);
        } else {

            //tv_number.setVisibility(View.GONE);
//            tv_name.setText(getResources().getString(R.string.btn_login));
//            tv_name.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
//                    startActivity(i);
//                }
//            });
            nav_menu.findItem(R.id.login).setVisible(false);
            nav_menu.findItem(R.id.nav_my_profile).setVisible(false);
            nav_menu.findItem(R.id.nav_logout).setVisible(false);
            nav_menu.findItem(R.id.sign).setVisible(true);


            //            nav_menu.findItem(R.id.nav_user).setVisible(false);
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e(TAG, "onConnected: ");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "onConnectionSuspended: ");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: ");
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
                    if (!sessionManagement.getLatPref().equalsIgnoreCase("") && !sessionManagement.getLangPref().equalsIgnoreCase("")) {
                        DecimalFormat dFormat = new DecimalFormat("##.#######");
                        LatLng latLng = new LatLng(Double.parseDouble(sessionManagement.getLatPref()), Double.parseDouble(sessionManagement.getLangPref()));
                        double latitude = Double.valueOf(dFormat.format(latLng.latitude));
                        double longitude = Double.valueOf(dFormat.format(latLng.longitude));
//                        Log.i("TAG", latitude + "\n" + longitude);
                        Location locationA = new Location("cal 1");
                        locationA.setLatitude(latitude);
                        locationA.setLongitude(longitude);
                        double disInMetter = locationA.distanceTo(locations);
                        double disData = disInMetter / 1000;
                        DecimalFormat dFormatt = new DecimalFormat("#.#");
                        disData = Double.parseDouble(dFormatt.format(disData));
//                        Log.i(TAG, "in" + disData);
                        if (disData > 5.0) {
                            if (!enterInFirst) {
                                enterInFirst = true;
                                location = locations;
                                getAddress();

                                if (selectedId == ID_HOME) {
                                    loadFragment(new HomeFragment(fragmentClickListner));
                                }
                            }
                        } else {
                            enterInFirst = true;
                            if (addres.getText().toString().equalsIgnoreCase("")) {
                                if (selectedId == ID_HOME) {
                                    loadFragment(new HomeFragment(fragmentClickListner));
                                }
                                getAddress();
                            }
                        }
                    } else {
                        enterInFirst = true;
                        location = locations;
                        if (selectedId == ID_HOME) {
                            loadFragment(new HomeFragment(fragmentClickListner));
                        }
                        getAddress();
                    }
                }
            }).start();

        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onTaskCompleted(String result) {
//        Log.e(TAG, "onTaskCompleted: " + result);
        ((TextView) findViewById(R.id.address)).setText(result);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equalsIgnoreCase("cardqnty")) {
//            totalBudgetCount.setText(pref.getInt("cardqnty",0));
            int badgeCount = pref.getInt("cardqnty", 0);
            if (badgeCount > 0) {
                cartCount.setText("" + badgeCount);
                cartCount.setVisibility(View.VISIBLE);
            } else {
                cartCount.setVisibility(View.GONE);
            }
        }
        else if(key.equalsIgnoreCase("favcount")){
            int favCount=pref.getInt("favcount",0);
            if(favCount>0){
                setBadge(ID_FAVOURITE, String.valueOf(favCount));
            }
            else
            {
                bottomNavigation.clearBadgeAtTabId(ID_FAVOURITE);
            }
        } else if (key.equalsIgnoreCase("updateProfilePic")) {

            Log.e("in", "sharedperf");
            pref.edit().putString("updateProfilePic", "true");

        }
    }

    public void updateProfilePic(){

    }

    @Override
    protected void onDestroy() {
        pref.unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

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
                    dbcart.clearCart();
                    for (int i = 0; i < orderSubModels.size(); i++) {
                        NewPendingDataModel odModel = orderSubModels.get(i);
                        if (odModel.getDescription() != null && !odModel.getDescription().equalsIgnoreCase("")) {
                            double price = Double.parseDouble(odModel.getPrice()) / Double.parseDouble(odModel.getQty());
                            HashMap<String, String> map = new HashMap<>();
                            map.put("varient_id", odModel.getVarient_id());
                            map.put("product_name", odModel.getProduct_name());
                            map.put("category_id", odModel.getVarient_id());
                            map.put("title", odModel.getProduct_name());
                            map.put("price", String.valueOf(price));
                            map.put("mrp", odModel.getTotal_mrp());
                            map.put("product_image", odModel.getVarient_image());
                            map.put("status", "1");
                            map.put("in_stock", "");
                            map.put("unit_value", odModel.getQuantity() + "" + odModel.getUnit());
                            map.put("unit", "");
                            map.put("increament", "0");
                            map.put("rewards", "0");
                            map.put("stock", "0");
                            map.put("product_description", odModel.getDescription());

                            map.put("supplierID", odModel.getSupplierID());

                            if (!odModel.getQty().equalsIgnoreCase("0")) {
                                dbcart.setCart(map, Integer.parseInt(odModel.getQty()));
                            } else {
                                dbcart.removeItemFromCart(map.get("varient_id"));
                            }

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                pref.edit().putInt("cardqnty", dbcart.getCartCount()).apply();
                            }
                        }
                    }

                    loadFragment(new CartFragment());

                }

            }

        } else if (requestCode == 22) {
            DecimalFormat dFormat = new DecimalFormat("##.#######");
            LatLng latLng = new LatLng(Double.parseDouble(sessionManagement.getLatPref()), Double.parseDouble(sessionManagement.getLangPref()));
            double latitude = Double.valueOf(dFormat.format(latLng.latitude));
            double longitude = Double.valueOf(dFormat.format(latLng.longitude));
            location.setLatitude(latitude);
            location.setLongitude(longitude);
            Log.i("TAG 22", latitude + "\n" + longitude);
//            Location locationA = new Location("cal 1");
//                        Location locationB = new Location("cal 2");
//            locationA.setLatitude(latitude);
//            locationA.setLongitude(longitude);
//            location = locationA;
            // updateAddress();
            if (selectedId == ID_HOME) {
                loadFragment(new HomeFragment(fragmentClickListner));
            }
        } else if (resultCode == 8482) {
            DecimalFormat dFormat = new DecimalFormat("##.#######");
            LatLng latLng = new LatLng(Double.parseDouble(sessionManagement.getLatPref()), Double.parseDouble(sessionManagement.getLangPref()));
            double latitude = Double.valueOf(dFormat.format(latLng.latitude));
            double longitude = Double.valueOf(dFormat.format(latLng.longitude));
            location.setLatitude(latitude);
            location.setLongitude(longitude);
            Log.i("TAG 8482", latitude + "\n" + longitude);
//            Location locationA = new Location("cal 1");
//                        Location locationB = new Location("cal 2");
//            locationA.setLatitude(latitude);
//            locationA.setLongitude(longitude);
//            location = locationA;
            // updateAddress();
            if (selectedId == ID_HOME) {
                loadFragment(new HomeFragment(fragmentClickListner));
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getCartProducts()
    {
        String tag_json_obj = "json_cart_list_req";
        String custID= sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
        Map<String, String> params = new HashMap<String, String>();
        params.put("custID", custID);
        params.put("BranchCode", ApiInterface.branchcode);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ApiBaseURL.CartProducts, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("CheckApiCart", response.toString());

                try {
                    boolean status = response.getBoolean("status");

                    if (status) {
                        JSONArray jsonArray = response.getJSONArray("result");
                        dbcart.clearCart();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<>();
                            map.put("varient_id", jsonObject.getString("itemID"));
                            map.put("product_name", jsonObject.getString("itemName"));
                            map.put("category_id", jsonObject.getString("itemID"));
                            map.put("title", jsonObject.getString("itemName"));
                            map.put("price", jsonObject.getString("itemSellingprice"));
                            map.put("product_image",jsonObject.getString("image"));
                            map.put("status", "");
                            map.put("in_stock", "");
                            map.put("vatRate", jsonObject.getString("vatRate"));
                            map.put("unit_value", jsonObject.getString("uom"));
                            map.put("increament", "0");
                            map.put("product_description",jsonObject.getString("shortDes"));
                            map.put("supplierID",jsonObject.getString("supplierID"));
                            int qty=jsonObject.getInt("quantity");
                            dbcart.setCart(map, qty);
                        }

                        JSONObject deliveryChargesVAT = response.getJSONObject("deliveryChargesVAT");
                        JSONArray vatResult = deliveryChargesVAT.getJSONArray("vatResult");


                        SharedPreferences preferences = getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                        preferences.edit().putInt("cardqnty", dbcart.getCartCount()).apply();


                        for (int i = 0; i < vatResult.length(); i++) {

                            JSONObject object = vatResult.getJSONObject(i);

                            if (object.getString("id").equals("1"))
                                preferences.edit().putString("deliveryCharges", object.getString("rateInAMT")).apply();
                            else preferences.edit().putString("vatRate", object.getString("rateInPer")).apply();

                        }


                        if(returnTo.equals("Order")){
//                            loadFragment(new CartFragment());
//                            Intent intent = new Intent(MainActivity.this, OrderSummary.class);
//                            intent.putExtra("return","Order");
//                            startActivityForResult(intent,22);

                        }
                    }
                    else
                    {
                        dbcart.clearCart();
                        SharedPreferences preferences = getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                        preferences.edit().putInt("cardqnty", dbcart.getCartCount()).apply();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
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

    public void getAllFav(){

        ServiceGenrator.getApiInterface().getFavouriteProductList(sessionManagement.getUserDetails().get(BaseURL.KEY_ID),"All").enqueue(
                new Callback<RestItem>() {
                    @Override
                    public void onResponse(Call<RestItem> call, retrofit2.Response<RestItem> response) {

                        if (response.isSuccessful()) {

                            if (response.body().isStatus()) {

                                ArrayList<ItemModel> itemModelArrayList=response.body().getResult();
                                for(int i=0;i<itemModelArrayList.size();i++){
                                    ItemModel cc=itemModelArrayList.get(i);
                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("varient_id", itemModelArrayList.get(i).getItemID());
                                    map.put("product_name", itemModelArrayList.get(i).getItemName());
                                    map.put("price", itemModelArrayList.get(i).getItemSellingprice());
                                    map.put("product_image", itemModelArrayList.get(i).getImage());
                                    map.put("unit_value", itemModelArrayList.get(i).getItemUnit());
                                    map.put("product_description", itemModelArrayList.get(i).getShortDes());
                                    map.put("supplierID", itemModelArrayList.get(i).getMainSupplier());
                                    dbcart.setWishlist (map);
                                }
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<RestItem> call, Throwable t) {

                    }
                }
        );
    }

    @Override
    public void onGetResponse(boolean isUpdateAvailable) {
        Log.e("ResultAPPMAIN", String.valueOf(isUpdateAvailable));
        if (isUpdateAvailable) {
            showUpdateDialog();
        }
    }

    public void showUpdateDialog() {
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(this.getString(R.string.app_name));
        alertDialogBuilder.setMessage(this.getString(R.string.app_upto_date));
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(R.string.updateNow, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                dialog.cancel();
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialogBuilder.show();
    }
    public void setBadge(int id, String count) {
//        bottomNavigation.setBadgeAtTabId(id, new AnimatedBottomBar.Badge.Builder().setText(count).build());
    }
}
