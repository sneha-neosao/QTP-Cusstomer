package adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import fragments.OrderStatusFragments.AcceptedOrderFragment;
import fragments.OrderStatusFragments.AllOrdersFragment;
import fragments.OrderStatusFragments.CancelledOrderFragment;
import fragments.OrderStatusFragments.LastOrderFragment;
import fragments.OrderStatusFragments.OTWOrderFragment;
import fragments.OrderStatusFragments.PendingOrderFragment;

public class OrderTabAdapter extends FragmentPagerAdapter {

    int noOfTabs;
    private boolean isPagingEnabled = true;

   public static String[] titles = { "All","Pending", "Accepted", "On The Way", "Last Order","Cancelled"};
   public static int[] count = { };

    public OrderTabAdapter(@NonNull FragmentManager fragmentManager, int noOfTabs) {
        super(fragmentManager);

        this.noOfTabs = noOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                AllOrdersFragment tab1 = new AllOrdersFragment();
                return tab1;
            case 1:
                PendingOrderFragment tab2 = new PendingOrderFragment();
                return tab2;

            case 2:
                AcceptedOrderFragment tab3 = new AcceptedOrderFragment();
                return tab3;

            case 3:
                OTWOrderFragment tab4 = new OTWOrderFragment();
                return tab4;

            case 4:
                LastOrderFragment tab5 = new LastOrderFragment();
                return tab5;
            case 5:
                CancelledOrderFragment tab6 = new CancelledOrderFragment();
                return tab6;

            default:
                return null;
        }
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return titles[position];
    }

    @Override
    public int getCount() {

        return noOfTabs;
    }
}
