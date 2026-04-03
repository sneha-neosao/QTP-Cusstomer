package fragments;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.VISIBLE;
import static activities.SubCategoryActivity.subcateList;
import static Config.BaseURL.ADDRESS;
import static Config.BaseURL.CITY;
import static Config.BaseURL.KEY_ID;
import static Config.BaseURL.LAT;
import static Config.BaseURL.LONG;
import static Config.BaseURL.MyPrefreance;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
//import activities.DealActivity;
import activities.MainDrawerActivity;
import activities.SubCategoryActivity;
import activities.ViewAll_TopDeals;
import adapters.BannerAdapter;
import adapters.FavouriteAdapter;
import adapters.MainScreenAdapter;
import Config.ApiBaseURL;
import Config.BaseURL;
import ModelClass.HomeCate;
import ModelClass.ItemModel;
import ModelClass.LabelModel;
import ModelClass.MainScreenList;
import ModelClass.NewCartModel;
import com.grocery.QTPmart.R;
import network.ApiInterface;
import network.Response.ResponseGetAllSubOfSubCategories;
import network.Response.RestItem;
import network.ServiceGenrator;
import util.CustomVolleyJsonRequest;
import util.FragmentClickListner;
import util.GetCategories;
import util.NetworkConnection;
import util.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;

public class HomeFragment extends Fragment implements View.OnClickListener  {

    private static final String TAG = HomeFragment.class.getName();

    ViewPager viewPager;
    TabLayout tabLayout;

    public static HashMap heights;

    public TextView viewall_topdeals;
    String latitude, longitude, address, city;
    SharedPreferences sharedPreferences;
    ArrayList<String> imageString = new ArrayList<>();
    private RecyclerView recyclerImages;
    private BannerAdapter bannerAdapter;
    private LinearLayout llImageSlider;
    //private LinearLayout change_loc_lay;
    //private TextView change_loc;
    private Session_management session_management;
    private FragmentClickListner fragmentClickListner;
    private ViewPager2 viewPager2;
    private ProgressDialog progressDialog;

    private List<MainScreenList> screenLists = new ArrayList<>();
    private List<NewCartModel> topSelling = new ArrayList<>();
    private List<NewCartModel> whatsNew = new ArrayList<>();
    private List<NewCartModel> recentSelling = new ArrayList<>();
    private List<NewCartModel> dealOftheday = new ArrayList<>();
    private List<LabelModel> labelModelArrayList = new ArrayList<>();

    private MainScreenAdapter screenAdapter;
    private Context contexts;

    // SubCategory_adapter adapter;
    String catId = "001", title = "FOOD";
    //RecyclerView recyclerView;
    final int time = 3000;
    LinearSnapHelper linearSnapHelper;
    LinearLayoutManager linearLayoutManager;
    ArrayList<HashMap<String, String>> listarray ;

    CollapsingToolbarLayout htab_collapse_toolbar;
    AppBarLayout htab_appbar;
    RelativeLayout rl_fav_list;
    LinearLayout ll_home_bottom,ll_bottom_2;
    ScrollView myScroller;

    ImageView ivAdd;

    FloatingActionButton  fabMain,fabOne, fabTwo, fabThree, fabfour;
    LinearLayout parent_lay;



    Float translationY = 100f;
    OvershootInterpolator interpolator = new OvershootInterpolator();
    Boolean isMenuOpen = false;

    public HomeFragment() {
    }

    public HomeFragment(FragmentClickListner fragmentClickListner) {
        this.fragmentClickListner = fragmentClickListner;
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home, container, false);
        requireActivity().setTitle(getResources().getString(R.string.app_name));
        MainDrawerActivity.tvTitle.setVisibility(View.GONE);
        contexts = container.getContext();
        listarray = new ArrayList<>();
        sharedPreferences = requireContext().getSharedPreferences(MyPrefreance, MODE_PRIVATE);
        session_management = new Session_management(container.getContext());
        progressDialog = new ProgressDialog(container.getContext());
        progressDialog.setMessage("Please wait while loading..");
        progressDialog.setCancelable(false);
        latitude = sharedPreferences.getString(LAT, null);
        longitude = sharedPreferences.getString(LONG, null);
        address = sharedPreferences.getString(ADDRESS, null);
        city = sharedPreferences.getString(CITY, null);
        bannerAdapter = new BannerAdapter(getActivity(), imageString,listarray);
//        BottomNavigationView navBar = getActivity().findViewById(R.id.nav_view12);
        // loc.setText(address+", "+city+", "+postalCode);
        //change_loc_lay = view.findViewById(R.id.change_loc_lay);
        //change_loc = view.findViewById(R.id.change_loc);
        htab_collapse_toolbar = view.findViewById(R.id.htab_collapse_toolbar);
        htab_appbar = view.findViewById(R.id.htab_appbar);
        rl_fav_list = view.findViewById(R.id.rl_fav_list);
        ll_home_bottom = view.findViewById(R.id.ll_home_bottom);
        ll_bottom_2 = view.findViewById(R.id.ll_bottom_2);

        tabLayout = view.findViewById(R.id.tablayout);
        heights = new HashMap();
        viewall_topdeals = view.findViewById(R.id.viewall_topdeals);
        viewPager = view.findViewById(R.id.pager_product);
        viewPager2 = view.findViewById(R.id.viewpa_2);
        recyclerImages = view.findViewById(R.id.recycler_image_slider);
//        llImageSlider = view.findViewById(R.id.llImageSlider);

        ivAdd = view.findViewById(R.id.ivAdd);

        fabMain = view.findViewById(R.id.fabMain);
        fabOne = view.findViewById(R.id.fabOne);
        fabTwo = view.findViewById(R.id.fabTwo);
        fabThree = view.findViewById(R.id.fabThree);
        fabfour = view.findViewById(R.id.fabfour);
        parent_lay = view.findViewById(R.id.parent_lay);

        myScroller = view.findViewById(R.id.myScroller);

        screenLists.add(new MainScreenList("ALL", topSelling, recentSelling, dealOftheday, whatsNew,SubCategoryActivity.subcateList,labelModelArrayList));
        /*
        recyclerView = view.findViewById(R.id.recyclerSubCate);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);


        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.item_offset);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(contexts, recyclerView, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position)
            {
                Intent intent = new Intent(contexts, ProductTabActivity.class);
                intent.putExtra("cat_id", subcateList.get(position).getId());
                intent.putExtra("title", subcateList.get(position).getName());
                startActivityForResult(intent, 24);
            }
            @Override
            public void onLongItemClick(View view, int position)
            {

            }
        }));
*/
       /* if(session_management.isLoggedIn()){
            //showFavourites(view);
        }
        else {*/
        rl_fav_list.setVisibility(View.GONE);
        htab_appbar.setVisibility(VISIBLE);
        ll_home_bottom.setVisibility(VISIBLE);
        ll_bottom_2.setVisibility(VISIBLE);

        // screenLists.add(new MainScreenList("ALL", topSelling, recentSelling, dealOftheday, whatsNew,subcateList,labelModelArrayList));
        /*Main View Pager*/
        screenAdapter = new MainScreenAdapter(getActivity(), screenLists,labelModelArrayList);
        viewPager2.setAdapter(screenAdapter);

        /**Banner*/
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerImages.setLayoutManager(linearLayoutManager);
        recyclerImages.setAdapter(bannerAdapter);
        linearSnapHelper = new LinearSnapHelper();
        // linearSnapHelper.attachToRecyclerView(recyclerView);

        // change_loc.setOnClickListener(v -> startActivityForResult(new Intent(v.getContext(), AddressLocationActivity.class), 22));
        setTabs();
        if (isOnline()) {

            if(NetworkConnection.connectionChecking(getContext())) {
                makeGetSliderRequest();
                subCategoryUrl();
                getAllSubOfSubCategories();
            }else{
                showToast(getString(R.string.no_internet));
            }
            //  topSelling();
            //  whatsNew();
            //  DealOfTheDay();
            //  recentDeal();
            // topSelling();
        }

        initFloatActions();

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);

                Log.e("Scroll","PageScrollStateChanged :"+state);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.i("TAG Demo", String.valueOf(position));
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                Log.e("Scroll","onPageScrolled :"+position+"\n"+positionOffset+"\n"+positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("Pos",position+"");
                // if(tabLayout.getVisibility()== VISIBLE){
                if(position==0){
                    viewall_topdeals.setVisibility(View.GONE);
                    if(tabLayout!=null){
                        tabLayout.getTabAt(0).setIcon(R.drawable.ic_all);
                        tabLayout.getTabAt(0).getIcon().setTint(getContext().getResources().getColor(R.color.white));
                    }

                }else{
                    // tabLayout.getTabAt(0).setIcon(R.drawable.ic_all);
                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_all);
                    tabLayout.getTabAt(0).getIcon().setTint(getContext().getResources().getColor(R.color.black));
                    viewall_topdeals.setVisibility(VISIBLE);
                }
                //   }


            }
        });

        viewall_topdeals.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ViewAll_TopDeals.class);
            if (tabLayout.getTabAt(viewPager2.getCurrentItem()).getText().toString().equalsIgnoreCase("TOP SELLING")) {
                if (topSelling.size() > 0) {
                    intent.putExtra("action_name", "Top_Deals_Fragment");
                    //startActivityForResult(intent, 56);
                    startActivity(intent);
                } else
                {
                    Toast.makeText(contexts, "No Order found in your location!", Toast.LENGTH_SHORT).show();
                }
            } else if (tabLayout.getTabAt(viewPager2.getCurrentItem()).getText().toString().equalsIgnoreCase("RECENT SEARCHES")) {
                if (recentSelling.size() > 0) {
                    intent.putExtra("action_name", "Recent_Details_Fragment");
                    //startActivityForResult(intent, 56);
                    startActivity(intent);
                } else {
                    Toast.makeText(contexts, "No Order found in your location!", Toast.LENGTH_SHORT).show();
                }
            } else if (tabLayout.getTabAt(viewPager2.getCurrentItem()).getText().toString().equalsIgnoreCase("DEALS OF THE DAY")) {
                if (dealOftheday.size() > 0) {
//                    Intent intent1 = new Intent(v.getContext(), DealActivity.class);
//                    intent1.putExtra("action_name", "Deals_Fragment");
//                    startActivity(intent1);
                } else {
                    Toast.makeText(contexts, "No Order found in your location!", Toast.LENGTH_SHORT).show();
                }

            } else if (tabLayout.getTabAt(viewPager2.getCurrentItem()).getText().toString().equalsIgnoreCase("WHAT'S NEW")) {
                if (whatsNew.size() > 0) {
                    intent.putExtra("action_name", "Whats_New_Fragment");
                    startActivity(intent);
                    //startActivityForResult(intent, 56);
                } else {
                    Toast.makeText(contexts, "No Order found in your location!", Toast.LENGTH_SHORT).show();
                }
            }

        });

        init(view);




        return view;
    }

    private enum State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    public void init(View view){

        MainDrawerActivity.bottomNavigation.setVisibility(VISIBLE);
        // tabLayout.setVisibility(View.GONE);
        final AppBarLayout mAppBarLayout = view.findViewById(R.id.htab_appbar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            private String TAG="Toolbar";
            private State state;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    if (state != State.EXPANDED) {
                        MainDrawerActivity.bottomNavigation.setVisibility(VISIBLE);
                        //ivAdd.setVisibility(VISIBLE);
                        parent_lay.setVisibility(VISIBLE);
                        ivAdd.setVisibility(View.GONE);
                        MainDrawerActivity.edt_search.setVisibility(View.GONE);
                        MainDrawerActivity.ll_nav_title.setVisibility(View.GONE);
                        MainDrawerActivity.reelLyt.setVisibility(VISIBLE);
                        MainDrawerActivity.notification_iv.setVisibility(VISIBLE);
                        MainDrawerActivity.search_iv.setVisibility(VISIBLE);
                        /*TranslateAnimation animate = new TranslateAnimation(0,tabLayout.getWidth(),0,0);
                        animate.setDuration(2000);
                        animate.setFillAfter(true);
                        tabLayout.startAnimation(animate);
                        tabLayout.setVisibility(View.GONE);*/
                        //viewGoneAnimator(tabLayout);
                        tabLayout.setVisibility(View.GONE);
                        Log.d(TAG,"Expanded");
                    }
                    state = State.EXPANDED;
                    // tabLayout.setVisibility(View.GONE);
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != State.COLLAPSED) {
                        MainDrawerActivity.bottomNavigation.setVisibility(View.GONE);
                        ivAdd.setVisibility(View.GONE);
                        parent_lay.setVisibility(View.GONE);
                        MainDrawerActivity.edt_search.setVisibility(VISIBLE);
                        MainDrawerActivity.ll_nav_title.setVisibility(VISIBLE);
                        MainDrawerActivity.reelLyt.setVisibility(View.GONE);
                        MainDrawerActivity.notification_iv.setVisibility(View.GONE);
                        MainDrawerActivity.search_iv.setVisibility(View.GONE);
                        Log.d(TAG,"Collapsed");
                        tabLayout.setVisibility(VISIBLE);
                        //viewVisibleAnimator(tabLayout);
                    }
                    state = State.COLLAPSED;
                    //  tabLayout.setVisibility(View.VISIBLE);
                } else {
                    if (state != State.IDLE)
                    {
                        MainDrawerActivity.bottomNavigation.setVisibility(VISIBLE);
                        //   ivAdd.setVisibility(VISIBLE);
                        parent_lay.setVisibility(VISIBLE);
                        ivAdd.setVisibility(View.GONE);
                        MainDrawerActivity.edt_search.setVisibility(View.GONE);
                        MainDrawerActivity.ll_nav_title.setVisibility(View.GONE);
                        MainDrawerActivity.reelLyt.setVisibility(VISIBLE);
                        MainDrawerActivity.notification_iv.setVisibility(VISIBLE);
                        MainDrawerActivity.search_iv.setVisibility(VISIBLE);
                        //tabLayout.setVisibility(VISIBLE);
                        Log.d(TAG,"Idle");
                        //viewVisibleAnimator(tabLayout);
                    }
                    state = State.IDLE;
                    // tabLayout.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void setTabs(){
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {

            if (position == 0) {
                tab.setIcon(R.drawable.ic_all);
                tab.getIcon().setTint(getContext().getResources().getColor(R.color.white));
            }
            else if (position == 1) {
                if (screenLists.size()>0){
                    tab.setText(screenLists.get(1).getViewType());
                }
            }
            else if (position == 2) {
                if (screenLists.size()>0){
                    tab.setText(screenLists.get(2).getViewType());
                }
            }
            else if (position == 3) {
                tab.setText("RECENT SEARCHES");
            }
            else if (position == 4) {
                tab.setText("TOP DEALS");
            }
        });
        tabLayoutMediator.attach();
    }

    private void topSelling() {
        topSelling.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiBaseURL.topSelling, response -> {
            Log.e("TopSelling", response);
            try {
                JSONObject jsonObjectResponse = new JSONObject(response);
                boolean status = jsonObjectResponse.getBoolean("status");
                if (status) {
                    // setTabs();
                    JSONArray jsonArray = jsonObjectResponse.getJSONArray("result");
                    List<NewCartModel> listorl = new ArrayList<>();
                    JSONArray jsonArrayLabel = jsonObjectResponse.getJSONArray("labels");
                    List<LabelModel> listLabel=new ArrayList<>();
                    for (int i = 0; i < jsonArrayLabel.length(); i++) {
                        JSONObject jsonObject = jsonArrayLabel.getJSONObject(i);
                        LabelModel labelModel = new LabelModel();
                        labelModel.setLableText(jsonObject.getString("lableText"));
                        labelModel.setImagePath(jsonObject.getString("imagePath"));

                        listLabel.add(labelModel);
                    }

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        NewCartModel topModel=new NewCartModel();
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

                        if(jsonObject.getString("fixedPrice") != null && Double.parseDouble(jsonObject.getString("fixedPrice"))>0) {
                            topModel.setItemSellingprice(jsonObject.getString("fixedPrice"));
                            topModel.setFixedPrice(jsonObject.getString("itemSellingprice"));
                        }
                        else
                        {
                            topModel.setFixedPrice(jsonObject.getString("fixedPrice"));
                            topModel.setItemSellingprice(jsonObject.getString("itemSellingprice"));
                        }
                        listorl.add(topModel);
                    }

                    topSelling.addAll(listorl);
                    labelModelArrayList.addAll(listLabel);

                    screenLists.add(new MainScreenList("TOP SELLING", topSelling, recentSelling, dealOftheday, whatsNew,SubCategoryActivity.subcateList,labelModelArrayList));
                    screenAdapter.notifyDataSetChanged();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                whatsNew();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                whatsNew();
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

        RequestQueue requestQueue = Volley.newRequestQueue(contexts);
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

    private void whatsNew() {
        whatsNew.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiBaseURL.whatsnew, response -> {
            Log.e("WhatsNew", response);
            try {
                JSONObject jsonObjectResponse = new JSONObject(response);
                boolean status = jsonObjectResponse.getBoolean("status");
                if (status) {
                    // setTabs();
                    JSONArray jsonArray = jsonObjectResponse.getJSONArray("result");
                    List<NewCartModel> listorl=new ArrayList<>();
                    JSONArray jsonArrayLabel = jsonObjectResponse.getJSONArray("labels");
                    List<LabelModel> listLabel=new ArrayList<>();
                    for (int i = 0; i < jsonArrayLabel.length(); i++) {
                        JSONObject jsonObject = jsonArrayLabel.getJSONObject(i);
                        LabelModel labelModel = new LabelModel();
                        labelModel.setLableText(jsonObject.getString("lableText"));
                        labelModel.setImagePath(jsonObject.getString("imagePath"));

                        listLabel.add(labelModel);
                    }

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        NewCartModel topModel=new NewCartModel();
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
                        topModel.setFixedPrice(jsonObject.getString("fixedPrice"));
                        listorl.add(topModel);
                    }

                    whatsNew.addAll(listorl);
                    labelModelArrayList.addAll(listLabel);
                    screenLists.add(new MainScreenList("WHAT'S NEW", topSelling,recentSelling, dealOftheday, whatsNew,SubCategoryActivity.subcateList,labelModelArrayList));

                    screenAdapter.notifyDataSetChanged();

                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            } finally {
                recentDeal();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                recentDeal();
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

        RequestQueue requestQueue = Volley.newRequestQueue(contexts);
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

    private void DealOfTheDay() {
        dealOftheday.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiBaseURL.HomeDeal, response -> {
            Log.e("DealOfTheDay", response);
            try {
                JSONObject jsonObjectResponse = new JSONObject(response);

                boolean status = jsonObjectResponse.getBoolean("status");
                if (status) {
                    //       setTabs();
                    JSONArray jsonArray = jsonObjectResponse.getJSONArray("result");
                    List<NewCartModel> listorl=new ArrayList<>();

                    JSONArray jsonArrayLabel = jsonObjectResponse.getJSONArray("labels");
                    List<LabelModel> listLabel=new ArrayList<>();
                    for (int i = 0; i < jsonArrayLabel.length(); i++) {
                        JSONObject jsonObject = jsonArrayLabel.getJSONObject(i);
                        LabelModel labelModel = new LabelModel();
                        labelModel.setLableText(jsonObject.getString("lableText"));
                        labelModel.setImagePath(jsonObject.getString("imagePath"));

                        listLabel.add(labelModel);
                    }

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        NewCartModel topModel=new NewCartModel();
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

                        if(jsonObject.getString("fixedPrice") != null && Double.parseDouble(jsonObject.getString("fixedPrice"))>0) {
                            topModel.setItemSellingprice(jsonObject.getString("fixedPrice"));
                            topModel.setFixedPrice(jsonObject.getString("itemSellingprice"));
                        }
                        else
                        {
                            topModel.setFixedPrice(jsonObject.getString("fixedPrice"));
                            topModel.setItemSellingprice(jsonObject.getString("itemSellingprice"));
                        }
                        listorl.add(topModel);
                    }

                    dealOftheday.addAll(listorl);
                    labelModelArrayList.addAll(listLabel);

                    screenLists.add(new MainScreenList("DEALS OF THE DAY", topSelling,recentSelling, dealOftheday, whatsNew,SubCategoryActivity.subcateList,labelModelArrayList));
                    screenAdapter.notifyDataSetChanged();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                progressDialog.dismiss();
                //ivAdd.setVisibility(VISIBLE);
                parent_lay.setVisibility(VISIBLE);
                ivAdd.setVisibility(View.GONE);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
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

        RequestQueue requestQueue = Volley.newRequestQueue(contexts);
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

    private void recentDeal() {
        recentSelling.clear();
        String custId="";
        if(session_management.isLoggedIn()) {
            custId = session_management.getUserDetails().get(KEY_ID);
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiBaseURL.topRecentViews+"?custId="+custId, response -> {
            Log.e("RecentViews", response);
            try {
                JSONObject jsonObjectResponse = new JSONObject(response);

                boolean status = jsonObjectResponse.getBoolean("status");
                if (status) {
                    //     setTabs();
                    JSONArray jsonArray = jsonObjectResponse.getJSONArray("result");
                    List<NewCartModel> listorl = new ArrayList<>();

                    JSONArray jsonArrayLabel = jsonObjectResponse.getJSONArray("labels");
                    List<LabelModel> listLabel=new ArrayList<>();
                    for (int i = 0; i < jsonArrayLabel.length(); i++) {
                        JSONObject jsonObject = jsonArrayLabel.getJSONObject(i);
                        LabelModel labelModel = new LabelModel();
                        labelModel.setLableText(jsonObject.getString("lableText"));
                        labelModel.setImagePath(jsonObject.getString("imagePath"));

                        listLabel.add(labelModel);
                    }

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        NewCartModel topModel=new NewCartModel();
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

                        if(jsonObject.getString("fixedPrice") != null && Double.parseDouble(jsonObject.getString("fixedPrice"))>0) {
                            topModel.setItemSellingprice(jsonObject.getString("fixedPrice"));
                            topModel.setFixedPrice(jsonObject.getString("itemSellingprice"));
                        }
                        else
                        {
                            topModel.setFixedPrice(jsonObject.getString("fixedPrice"));
                            topModel.setItemSellingprice(jsonObject.getString("itemSellingprice"));
                        }
                        listorl.add(topModel);
                    }


                    /*screenLists.set(4,new MainScreenList("RECENT SEARCHES", topSelling,
                            recentSelling, dealOftheday, whatsNew,SubCategoryActivity.subcateList,labelModelArrayList));
*/
                    recentSelling.addAll(listorl);
                    labelModelArrayList.addAll(listLabel);
                    screenLists.add(new MainScreenList("RECENT SEARCHES", topSelling,recentSelling, dealOftheday, whatsNew,SubCategoryActivity.subcateList,labelModelArrayList));

                    screenAdapter.notifyDataSetChanged();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                DealOfTheDay();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DealOfTheDay();

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

        RequestQueue requestQueue = Volley.newRequestQueue(contexts);
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

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void makeGetSliderRequest() {
        imageString.clear();
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET, ApiBaseURL.BANNER, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("BAnner", response.toString());
                        try {
                            if (response != null && response.length() > 0) {
                                boolean status = response.getBoolean("status");
                                if (status) {

                                    JSONArray jsonArray = response.getJSONArray("result");
                                    if (jsonArray.length() <= 0) {
                                        recyclerImages.setVisibility(View.GONE);
                                    } else {
                                        listarray.clear();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            HashMap<String, String> url_maps = new HashMap<String, String>();
                                            url_maps.put("banner_name", jsonObject.getString("banner_name"));
                                            url_maps.put("banner_id", jsonObject.getString("banner_id"));
                                            url_maps.put("banner_image", ApiBaseURL.IMG_URL + jsonObject.getString("banner_image"));
                                            imageString.add(ApiBaseURL.IMG_URL + jsonObject.getString("banner_image"));
                                            listarray.add(url_maps);
                                        }


                                        bannerAdapter.notifyDataSetChanged();


                                        final Timer timer = new Timer();
                                        timer.schedule(new TimerTask() {

                                            @Override
                                            public void run() {

                                                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() < (bannerAdapter.getItemCount() - 1)) {

                                                    linearLayoutManager.smoothScrollToPosition(recyclerImages, new RecyclerView.State(), linearLayoutManager.findLastCompletelyVisibleItemPosition() + 1);
                                                }

                                                else if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == (bannerAdapter.getItemCount() - 1)) {

                                                    linearLayoutManager.smoothScrollToPosition(recyclerImages, new RecyclerView.State(), 0);
                                                }
                                            }
                                        }, 0, time);


                                    /*    for (HashMap<String, String> name : listarray) {
                                            CustomSlider textSliderView = new CustomSlider(getActivity());
                                            textSliderView.description(name.get("")).image(name.get("banner_image")).setScaleType(BaseSliderView.ScaleType.Fit);
                                            textSliderView.bundle(new Bundle());
                                            textSliderView.getBundle().putString("extra", name.get("banner_name"));
                                            textSliderView.getBundle().putString("extra", name.get("banner_id"));
        //                                home_list_banner.addSlider(textSliderView);
                                            //   banner_slider.addSlider(textSliderView);
                                            final String sub_cat = (String) textSliderView.getBundle().get("extra");
                                            textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                                @Override
                                                public void onSliderClick(BaseSliderView slider) {
                                                    //   Toast.makeText(getActivity(), "" + sub_cat, Toast.LENGTH_SHORT).show();
        //                                        Bundle args = new Bundle();
        //                                        android.app.Fragment fm = new Product_fragment();
        //                                        args.putString("id", sub_cat);
        //                                        fm.setArguments(args);
        //                                        FragmentManager fragmentManager = getFragmentManager();
        //                                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
        //                                                .addToBackStack(null).commit();
                                                }
                                            });
                                        }*/
                                    }
                                }
                                else
                                {
                                    recyclerImages.setVisibility(View.GONE);
                                }
                            }
                            else
                            {
                                recyclerImages.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);


    }

    private void subCategoryUrl() {
        subcateList.clear();

        progressDialog.show();

        Map<String, String> params = new HashMap<String, String>();
        params.put("BranchCode", ApiInterface.branchcode);


        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET,
                ApiBaseURL.AllSubCategories, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                // progressDialog.dismiss();
                Log.e("subcategories", response.toString());
                try {
                    if (response != null && response.length() > 0) {
                        boolean status = response.getBoolean("status");
                        if (status) {
                            //  setTabs();
                            JSONArray array = response.getJSONArray("result");
                            SubCategoryActivity.subcateList.clear();
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject object = array.getJSONObject(i);
                                HomeCate model = new HomeCate();

                                model.setId(object.getString("item_subCatID"));
                                model.setImages(object.getString("image"));
                                model.setName(object.getString("item_subCatName"));
                                model.setCategoryId(object.getString("categoryId"));
                                SubCategoryActivity.subcateList.add(model);
                                GetCategories.allSubCategoriesArrayList.add(model);
                            }

                            // screenLists.add(0,new MainScreenList("ALL", topSelling,recentSelling, dealOftheday, whatsNew,subcateList, labelModelArrayList));

                            screenAdapter.notifyDataSetChanged();
                        }
                    }
                    topSelling();
                    //    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // progressDialog.dismiss();
                topSelling();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("categoryId", catId);
                Log.e("param",param.toString());
                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();
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
        requestQueue.add(jsonObjReq);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 24) {
            if (data != null && data.getBooleanExtra("open", false)) {
                if (fragmentClickListner != null) {
                    fragmentClickListner.onFragmentClick(data.getBooleanExtra("open", false));
                }
            }
        } else if (requestCode == 56) {
            if (data != null && data.getBooleanExtra("carttogo", false)) {
                Log.e("call","If CAll");
                if (fragmentClickListner != null) {
                    fragmentClickListner.onFragmentClick(data.getBooleanExtra("carttogo", false));
                }
            } else {
                Log.e("call","Else CAll");
                topSelling();
            }
        }else if (requestCode == 22){
            if (fragmentClickListner != null) {
                fragmentClickListner.onChangeHome(true);
            }
        }
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
        switch (view.getId()) {
//            case R.id.fabMain:
//
//                Log.i(TAG, "onClick: fab main");
//                if (isMenuOpen) {
//                    fabOne.setVisibility(View.GONE);
//                    fabTwo.setVisibility(View.GONE);
//                    fabThree.setVisibility(View.GONE);
//                    fabfour.setVisibility(View.GONE);
//                    closeMenu();
//                } else {
//                    fabOne.setVisibility(View.VISIBLE);
//                    fabTwo.setVisibility(View.VISIBLE);
//                    fabThree.setVisibility(View.VISIBLE);
//                    fabfour.setVisibility(View.VISIBLE);
//                    openMenu();
//                }
//                break;
//            case R.id.fabOne:
//                Intent sendIntent1 = new Intent();
//                sendIntent1.setAction(Intent.ACTION_SEND);
//                sendIntent1.putExtra(Intent.EXTRA_TEXT, "Hi friends i am using ." + " http://play.google.com/store/apps/details?id=" + getContext().getPackageName() + " APP");
//                sendIntent1.setType("text/plain");
//                startActivity(sendIntent1);
//
//                Log.i(TAG, "onClick: fab one");
//                handleFabOne();
//                if (isMenuOpen) {
//                    closeMenu();
//                } else {
//                    openMenu();
//                }
//                break;
//            case R.id.fabTwo:
//                Uri uri = Uri.parse("market://details?id=" + getContext().getPackageName());
//                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
//                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
//                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
//                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//                try {
//                    startActivity(goToMarket);
//                } catch (ActivityNotFoundException e) {
//                    startActivity(new Intent(Intent.ACTION_VIEW,
//                            Uri.parse("http://play.google.com/store/apps/details?id=" + getContext().getPackageName())));
//                }
//                break;
//            case R.id.fabThree:
//                String smsNumber = "971504413221";
//                openWhatsApp(smsNumber);
//                break;
//
//
//            case R.id.fabfour:
//
//                if (isPermissionGranted()) {
//                    call_action();
//                }
//
//                Log.i(TAG, "onClick: fab four");
//                break;
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
            Toast.makeText(getContext(), "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
            startActivity(goToMarket);
        }
    }

    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = getContext().getPackageManager();
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
            if (getActivity().checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
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
                    Toast.makeText(getContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    call_action();
                } else {
                    Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

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

    @Override
    public void onResume() {
        super.onResume();
//        if(adapter!=null){
//           adapter.notifyDataSetChanged();
//        }

    }

    private void getAllSubOfSubCategories(){
        ServiceGenrator.getApiInterface().getAllSubOfSubCategories().enqueue(new Callback<ResponseGetAllSubOfSubCategories>() {
            @Override
            public void onResponse(Call<ResponseGetAllSubOfSubCategories> call, retrofit2.Response<ResponseGetAllSubOfSubCategories> response) {
                if(response.isSuccessful()){
                    if(response.body().isStatus()){
                        if(response.body().getAllSubOfSubCategories()!=null){
                            //ArrayList<ResponseGetAllSubOfSubCategories.AllSubOfSubCategories> subOfSubCategoriesList = new ArrayList<>();
                            //Log.e("subCatList",response.body().getAllSubOfSubCategories().toString());
                            GetCategories.allSubOfSubCategoriesArrayList=response.body().getAllSubOfSubCategories();
                            //GetCategories.setAllSubOfSubCategoriesArrayList(subOfSubCategoriesList);
                        }
                    }else{

                    }
                }else{

                }
            }

            @Override
            public void onFailure(Call<ResponseGetAllSubOfSubCategories> call, Throwable t) {

            }
        });
    }

    private void showToast(String message){
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }

    private void showFavourites() {

        Dialog dialog=new Dialog(getContext());
        dialog.setContentView(R.layout.layout_popup_fav);

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,height);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // dialog.show();

        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerCart);

        ImageView close = dialog.findViewById(R.id.close);

        ServiceGenrator.getApiInterface().getFavouriteProductList(session_management.getUserDetails().get(BaseURL.KEY_ID),"4").enqueue(
                new Callback<RestItem>() {
                    @Override
                    public void onResponse(Call<RestItem> call, retrofit2.Response<RestItem> response) {

                        if (response.isSuccessful()) {

                            if (response.body().isStatus()) {

                                dialog.show();
                                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    nav_view.getForeground().setAlpha(150);
                                }*/
                                ArrayList<ItemModel> tempArrayList=response.body().getResult();
                                ArrayList<ItemModel> itemModelArrayList=new ArrayList<>();
                                Log.e("tempArrayList",String.valueOf(tempArrayList.size()));
                                for(int i=0;i<tempArrayList.size();i++) {
                                    ItemModel favitem=new ItemModel();

                                    favitem.setItemID(tempArrayList.get(i).getItemID());
                                    favitem.setCategoryID(tempArrayList.get(i).getCategoryID());
                                    favitem.setItemName(tempArrayList.get(i).getItemName());
                                    favitem.setShortDes(tempArrayList.get(i).getShortDes());
                                    favitem.setImage(tempArrayList.get(i).getImage());
                                    favitem.setItemUnit(tempArrayList.get(i).getItemUnit());
                                    favitem.setMainSupplier(tempArrayList.get(i).getMainSupplier());
                                    favitem.setVatRate(tempArrayList.get(i).getVatRate());
                                    favitem.setFeedback(tempArrayList.get(i).getFeedback());
                                    favitem.setDiscount(tempArrayList.get(i).getDiscount());
                                    favitem.setUnitID(tempArrayList.get(i).getUnitID());
                                    favitem.setUom(tempArrayList.get(i).getUom());
                                    favitem.setItemSellingprice(tempArrayList.get(i).getItemSellingprice());
                                    favitem.setFixedPrice(tempArrayList.get(i).getFixedPrice());
                                    favitem.setStockingType(tempArrayList.get(i).getStockingType());


                                    if (tempArrayList.get(i).getFixedPrice() != null && Double.parseDouble(tempArrayList.get(i).getFixedPrice()) > 0) {
                                        favitem.setItemSellingprice(tempArrayList.get(i).getFixedPrice());
                                        favitem.setFixedPrice(tempArrayList.get(i).getItemSellingprice());
                                    } else {
                                        favitem.setFixedPrice(tempArrayList.get(i).getFixedPrice());
                                        favitem.setItemSellingprice(tempArrayList.get(i).getItemSellingprice());
                                    }
                                    itemModelArrayList.add(favitem);


                                }
                                FavouriteAdapter favouriteAdapter = new FavouriteAdapter(getContext(), itemModelArrayList,recyclerView);

                                recyclerView.setAdapter(favouriteAdapter);
                            }
                            // dialog.dismiss();

                        }
                    }

                    @Override
                    public void onFailure(Call<RestItem> call, Throwable t) {

                    }
                }
        );
        // recyclerView.setAdapter(new FavouriteAdapter(this, map, null,recyclerView));

        // Button viewAll = dialog.findViewById(R.id.viewAll);
        TextView txtViewAll = dialog.findViewById(R.id.txtViewAll);
        txtViewAll.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                //bottomNavigation.show(ID_FAVOURITE, true);
                //loadFragment(new FavouriteFragment());
                //  drawer.getForeground().setAlpha(0);
                new MainDrawerActivity().selectedId=4;
                MainDrawerActivity.bottomNavigation.show(4,true);
//                loadFragment(new OrderFragment());
                dialog.dismiss();

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

                dialog.dismiss();
                //drawer.getForeground().setAlpha(0);
            }
        });
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.nav_supplier_fragment,fragment,fragment.getTag()).commit();
    }
}