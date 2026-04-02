package adapters;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import fragments.ProductFragment1;
import ModelClass.ProductSector;

import java.util.ArrayList;

public class TabSubAdapter extends FragmentPagerAdapter {

    //ArrayList<Category_model> sectorList;
    ArrayList<ProductSector> sectorList;

    public TabSubAdapter(FragmentManager childFragmentManager,ArrayList<ProductSector> sectorList)
    {
        super(childFragmentManager);
       // this.catList=catList;
        this.sectorList=sectorList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        Log.e("tabCatId",sectorList.get(position).getProduct_sector_id());
        //Log.e("tabCatId",sectorList.get(position).getCat_id());
        ProductFragment1 tabFragment = ProductFragment1.getInstance(sectorList.get(position));
       // ProductFragment1 tabFragment = ProductFragment1.getInstance(sectorList.get(position));
        return  tabFragment;


        //return null;
    }

    @Override
    public int getCount()
    {
        return sectorList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return sectorList.get(position).getProduct_sector_name();
        //return sectorList.get(position).getTitle();

    }

}
