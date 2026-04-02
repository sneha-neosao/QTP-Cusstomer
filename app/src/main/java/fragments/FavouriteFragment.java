package fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import activities.MainDrawerActivity;
import adapters.FavouriteAdapter;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import Config.BaseURL;
import ModelClass.CartModel;
import ModelClass.ItemModel;
import com.grocery.QTPmart.R;

import network.Response.RestItem;
import network.ServiceGenrator;
import util.DatabaseHandler;
import util.Session_management;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouriteFragment extends Fragment {

    Button btn_ShopNOw;
    RecyclerView recyclerView;

    RelativeLayout viewCart,rl_main,rl1;
    TextView totalItems;
    public static RelativeLayout noData;
    public static TextView tv_total ;
    private List<CartModel> cartList = new ArrayList<>();
    private DatabaseHandler db;
    private Session_management sessionManagement;
    ArrayList<ItemModel> itemModelArrayList=new ArrayList<>();
    FavouriteAdapter favouriteAdapter;

    public FavouriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

        recyclerView=view.findViewById(R.id.recyclerCart);
        btn_ShopNOw=view.findViewById(R.id.btn_ShopNOw);
        viewCart=view.findViewById(R.id.viewCartItems);
        noData=view.findViewById(R.id.noData);
        rl_main=view.findViewById(R.id.rl_main);
        rl1=view.findViewById(R.id.rl1);
        sessionManagement = new Session_management(getActivity());
        sessionManagement.cleardatetime();
        db = new DatabaseHandler(getActivity());

        MainDrawerActivity.tvTitle.setVisibility(View.VISIBLE);
        MainDrawerActivity.tvTitle.setText("");
        MainDrawerActivity.reelLyt.setVisibility(View.VISIBLE);
        MainDrawerActivity.notification_iv.setVisibility(View.VISIBLE);
        MainDrawerActivity.search_iv.setVisibility(View.VISIBLE);
        MainDrawerActivity.ll_nav_title.setVisibility(View.GONE);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        recyclerView.setNestedScrollingEnabled(false);
        favouriteAdapter = new FavouriteAdapter(getContext(), new ArrayList(),recyclerView);
        SlideInBottomAnimationAdapter slideInBottomAnimationAdapter = new SlideInBottomAnimationAdapter(favouriteAdapter);
        slideInBottomAnimationAdapter.setDuration(1000);
        recyclerView.setAdapter(new AlphaInAnimationAdapter(slideInBottomAnimationAdapter));
        /*if (sessionManagement.isLoggedIn()) {

         *//*   if (db.getWishlistCount() == 0) {
                noData.setVisibility(View.VISIBLE);
                rl_main.setBackgroundResource(R.color.white);
                viewCart.setVisibility(View.GONE);
            }
        }else {
            if (db.getWishlistCount() == 0) {
                noData.setVisibility(View.VISIBLE);
                rl_main.setBackgroundResource(R.color.white);
                viewCart.setVisibility(View.GONE);
            }*//*
            String user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
            getAllFav(user_id,"All");
        }
        else
        {
            noData.setVisibility(View.VISIBLE);
            rl_main.setBackgroundResource(R.color.white);
            viewCart.setVisibility(View.GONE);
        }*/
       //String user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
        //getAllFav(user_id,"All");


      /*  FavouriteAdapter adapter = new FavouriteAdapter(getActivity(), map, () -> {
            if (db.getWishlistCount() == 0) {
                noData.setVisibility(View.VISIBLE);
                rl_main.setBackgroundResource(R.color.white);
                viewCart.setVisibility(View.GONE);
            }
        }, recyclerView);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
*/
      //  updateData();

        NestedScrollView scroller = (NestedScrollView) view.findViewById(R.id.scroll_view);

        if (scroller != null) {

            scroller.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    if (scrollY > oldScrollY) {
                        Log.i("TAG", "Scroll DOWN");
                        rl1.setVisibility(View.GONE);
                        MainDrawerActivity.reelLyt.setVisibility(View.GONE);
                        MainDrawerActivity.notification_iv.setVisibility(View.GONE);
                        MainDrawerActivity.tvTitle.setVisibility(View.VISIBLE);
                        MainDrawerActivity.tvTitle.setText("Favourite");
                        MainDrawerActivity.bottomNavigation.setVisibility(View.GONE);
                    }
                    if (scrollY < oldScrollY) {
                        Log.i("TAG", "Scroll UP");
                        MainDrawerActivity.bottomNavigation.setVisibility(View.VISIBLE);
                    }

                    if (scrollY == 0) {
                        Log.i("TAG", "TOP SCROLL");
                        rl1.setVisibility(View.VISIBLE);
                        MainDrawerActivity.reelLyt.setVisibility(View.VISIBLE);
                        MainDrawerActivity.notification_iv.setVisibility(View.VISIBLE);
                        MainDrawerActivity.tvTitle.setVisibility(View.GONE);
                        MainDrawerActivity.tvTitle.setText("");
                    }

                    if (scrollY == ( v.getMeasuredHeight() - v.getChildAt(0).getMeasuredHeight() )) {
                        Log.i("TAG", "BOTTOM SCROLL");
                    }
                }
            });
        }

        return view;


        }



    public void updateData() {
        tv_total.setText(sessionManagement.getCurrency()+ " " + db.getTotalAmount());
        totalItems.setText("" + db.getCartCount()+"  Total Items:");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
           // ((MainActivity) getActivity()).setCartCounter("" + db.getCartCount());
//        }

    }

    private void showBloackDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setCancelable(true);
        alertDialog.setMessage("You are blocked from backend.\n Please Contact with customer care!");
//        alertDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        });
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }



    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public void getAllFav(String Id,String top)
    {
        ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.show();
        itemModelArrayList.clear();
        ServiceGenrator.getApiInterface().getFavouriteProductList(Id,top).enqueue(
                new Callback<RestItem>() {
                    @Override
                    public void onResponse(Call<RestItem> call, Response<RestItem> response) {
                        progressDialog.dismiss();
                        if (response.isSuccessful()) {

                            if (response.body().isStatus()) {
                                ArrayList<ItemModel> tempArrayList=response.body().getResult();

                                for(int i=0;i<tempArrayList.size();i++) {
                                    ItemModel favitem=new ItemModel();

                                   /* favitem.setItemID(tempArrayList.get(i).getItemID());
                                    favitem.setItemName(tempArrayList.get(i).getItemName());
                                    favitem.setShortDes(tempArrayList.get(i).getShortDes());
                                    favitem.setImage(tempArrayList.get(i).getImage());
                                    favitem.setItemUnit(tempArrayList.get(i).getItemUnit());
                                    favitem.setMainSupplier(tempArrayList.get(i).getMainSupplier());
                                    favitem.setVatRate(tempArrayList.get(i).getVatRate());
                                    favitem.setFeedback(tempArrayList.get(i).getFeedback());
                                    favitem.setDiscount(tempArrayList.get(i).getDiscount());

                                    favitem.setUnitID(tempArrayList.get(i).getUnitID());
                                    favitem.setUomId(tempArrayList.get(i).getUomId());
                                    favitem.setUom(tempArrayList.get(i).getUom());
                                    favitem.setAdminRating(tempArrayList.get(i).getAdminRating());
                                    favitem.setStockingType(tempArrayList.get(i).getStockingType());
                                    favitem.setCustomerRating(tempArrayList.get(i).getCustomerRating());
                                    favitem.setRatingUserCount(tempArrayList.get(i).getRatingUserCount());
                                    favitem.setProductLabel(tempArrayList.get(i).getProductLabel());
                                    favitem.setCategoryID(tempArrayList.get(i).getCategoryID());
                                    favitem.setItemSubCategory(tempArrayList.get(i).getItemSubCategory());*/

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
                                    favitem.setUomId(tempArrayList.get(i).getUomId());
                                    favitem.setItemSellingprice(tempArrayList.get(i).getItemSellingprice());
                                    favitem.setFixedPrice(tempArrayList.get(i).getFixedPrice());
                                    favitem.setStockingType(tempArrayList.get(i).getStockingType());
                                    favitem.setAdminRating(tempArrayList.get(i).getAdminRating());
                                    favitem.setCustomerRating(tempArrayList.get(i).getCustomerRating());
                                    favitem.setRatingUserCount(tempArrayList.get(i).getRatingUserCount());
                                    favitem.setProductLabel(tempArrayList.get(i).getProductLabel());
                                    favitem.setItemSubCategory(tempArrayList.get(i).getItemSubCategory());

                                    if (tempArrayList.get(i).getFixedPrice() != null && Double.parseDouble(tempArrayList.get(i).getFixedPrice()) > 0) {
                                        favitem.setItemSellingprice(tempArrayList.get(i).getFixedPrice());
                                        favitem.setFixedPrice(tempArrayList.get(i).getItemSellingprice());
                                    } else {
                                        favitem.setFixedPrice(tempArrayList.get(i).getFixedPrice());
                                        favitem.setItemSellingprice(tempArrayList.get(i).getItemSellingprice());
                                    }
                                    itemModelArrayList.add(favitem);
                                }
                                //FavouriteAdapter favouriteAdapter = new FavouriteAdapter(getContext(), itemModelArrayList,recyclerView);
                                favouriteAdapter.setList(itemModelArrayList);
                                recyclerView.setVisibility(View.VISIBLE);
                                SlideInBottomAnimationAdapter slideInBottomAnimationAdapter = new SlideInBottomAnimationAdapter(favouriteAdapter);
                                slideInBottomAnimationAdapter.setDuration(1000);
                                recyclerView.setAdapter(new AlphaInAnimationAdapter(slideInBottomAnimationAdapter));
                            }
                            else
                            {
                                recyclerView.setVisibility(View.GONE);
                                noData.setVisibility(View.VISIBLE);
                               // rl_main.setBackgroundResource(R.color.white);
                               // viewCart.setVisibility(View.GONE);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<RestItem> call, Throwable t) {
                        progressDialog.dismiss();
                        recyclerView.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
                        rl_main.setBackgroundResource(R.color.white);
                        viewCart.setVisibility(View.GONE);
                    }
                }
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sessionManagement.isLoggedIn()) {
            recyclerView.setVisibility(View.GONE);
            String user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
            getAllFav(user_id,"All");
        }
        else
        {
            noData.setVisibility(View.VISIBLE);
            rl_main.setBackgroundResource(R.color.white);
            viewCart.setVisibility(View.GONE);
        }
    }
}
