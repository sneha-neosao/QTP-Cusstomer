package adapters.ViewHolders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grocery.QTPmart.R;

public class LifetimeOfferViewHolder extends RecyclerView.ViewHolder {

    public ImageView imageOffers, lock;
    public TextView titleOffer, descOffer1, descOffer2, amount, minAmount, showDetails,hideDetails;
    public TextView proceed;
    public TextView subTotalAmount, couponPer, couponApplied, total, vatPercent, vatPercentAmount, shippingCharges, grandTotalAmount;
    public TableLayout detailsTable;
    public RelativeLayout relativeLayout;

    public LifetimeOfferViewHolder(@NonNull View itemView) {
        super(itemView);

        imageOffers = itemView.findViewById(R.id.imageOffers);
        titleOffer = itemView.findViewById(R.id.titleOffer);
        descOffer1 = itemView.findViewById(R.id.descOffer1);
        descOffer2 = itemView.findViewById(R.id.descOffer2);
        amount = itemView.findViewById(R.id.amount);
        proceed = itemView.findViewById(R.id.proceed);
        minAmount = itemView.findViewById(R.id.minAmount);
        showDetails = itemView.findViewById(R.id.showDetails);
        hideDetails = itemView.findViewById(R.id.hideDetails);
        lock = itemView.findViewById(R.id.lock);
        relativeLayout = itemView.findViewById(R.id.rl_checked);

        subTotalAmount = itemView.findViewById(R.id.subTotalAmount);
        couponPer = itemView.findViewById(R.id.couponPer);
        couponApplied = itemView.findViewById(R.id.couponApplied);
        total = itemView.findViewById(R.id.total);
        vatPercent = itemView.findViewById(R.id.vatPercent);
        vatPercentAmount = itemView.findViewById(R.id.vatPercentAmount);
        shippingCharges = itemView.findViewById(R.id.shippingCharges);
        grandTotalAmount = itemView.findViewById(R.id.grandTotalAmount);
        detailsTable = itemView.findViewById(R.id.detailsTable);

    }


}
