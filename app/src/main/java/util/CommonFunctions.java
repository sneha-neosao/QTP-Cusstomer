package util;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import activities.MainDrawerActivity;
import fragments.FavouriteFragment;
import network.Response.ResponseGetCounts;
import network.ServiceGenrator;

import retrofit2.Call;
import retrofit2.Callback;

public class CommonFunctions {

    public static void callMainDrawerActivity(Activity activity){
        Intent intent1 =new Intent(activity, MainDrawerActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent1.putExtra("loadFrag",1);
        activity.startActivity(intent1);
        activity.finish();
    }

    public static void setFavouriteCounts(String userId){
        ServiceGenrator.getApiInterface().getCounts(userId).enqueue(new Callback<ResponseGetCounts>() {
            @Override
            public void onResponse(Call<ResponseGetCounts> call, retrofit2.Response<ResponseGetCounts> response) {
                if (response.isSuccessful()) {

                    if(response.body()!=null) {
                        if (response.body().isStatus()) {
                            if (response.body().getResult() != null) {
                                if(response.body().getResult().getFavouriteCount()==0){
                                    FavouriteFragment.noData.setVisibility(View.VISIBLE);
                                    MainDrawerActivity.bottomNavigation.clearCount(4);
                                }else {
                                    FavouriteFragment.noData.setVisibility(View.GONE);
                                    MainDrawerActivity.bottomNavigation.setCount(4, String.valueOf(response.body().getResult().getFavouriteCount()));
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseGetCounts> call, Throwable t) {

            }
        });
    }
}
