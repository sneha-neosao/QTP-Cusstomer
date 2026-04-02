package adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import fragments.ProductFragment2;
import ModelClass.ProductSector;

import java.util.ArrayList;


public class TabAdapter2 extends FragmentPagerAdapter {

   // ArrayList<HomeCate> subCatList;
    ArrayList<ProductSector> sectorList;
    //ArrayList<ResponseGetAllSubOfSubCategories.AllSubOfSubCategories> subSubCatList;

    public TabAdapter2(FragmentManager childFragmentManager, ArrayList<ProductSector> sectorList ) {
        super(childFragmentManager);
       // this.subCatList=subCatList;
       // this.subSubCatList=subSubCatList;
        this.sectorList=sectorList;
    }

//    public int getItemPosition(Object object) {
//        return POSITION_NONE;
//    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        ProductFragment2 tabFragment = ProductFragment2.getInstance(sectorList.get(position));
        return  tabFragment;
//        switch (position)
//        {
//            case 0:
//               TabFragment tabFragment = new TabFragment();
//               return  tabFragment;
//            case 1:
//                TabFragment tabFragments = new TabFragment();
//                return  tabFragments;
//            case 2:
//                TabFragment tabFragmen = new TabFragment();
//                return  tabFragmen;
//            case 3:
//                TabFragment tab = new TabFragment();
//                return  tab;
//            case 4:
//                TabFragment tabF = new TabFragment();
//                return  tabF;
//            case 5:
//            TabFragment tabFr = new TabFragment();
//            return  tabFr;
//            case 6:
//                TabFragment tabFra = new TabFragment();
//                return  tabFra;
//            case 7:
//                TabFragment tabFragmentss = new TabFragment();
//                return  tabFragmentss;
//            default:
//                return null;
//        }
    }

    @Override
    public int getCount() {
        return sectorList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return sectorList.get(position).getProduct_sector_name();
//        switch (position)
//        {
//            case 0:
//                return "Appliances";
//
//            case 1:
//                return "Book";
//
//            case 2:
//                return "Electronics";
//
//            case 3:
//                return "Personal Care";
//
//            case 4:
//                return "Fashion";
//
//            case 5:
//                return "Sports";
//
//            case 6:
//                return "Travel";
//
//            default:
//                return null;
//
//        }

    }

}
