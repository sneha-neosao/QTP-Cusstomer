package adapters.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grocery.QTPmart.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class SubOrdersHolder extends RecyclerView.ViewHolder{
    //detail order card
    public LinearLayout ll_detail_card,ll_status_track;
    public RecyclerView rv_orderItem;
    public CircleImageView confirm_imageS,otw_imageS,delivered_imageS;
    public ImageView img_seller_logo,img_order_up_arrow;
    public TextView txt_seller_name,txt_seller_address,txt_seller_price,txt_order_time,txt_order_status,
            txt_order_subtotal,txt_order_serviceCharge,txt_order_total,txt_order_number,txt_order_date,
            txt_order_paymode,txt_order_deliAddress;

    public CircleImageView ivProcessingDoneTrack,ivOTWDoneTrack,ivDeliveredDoneTrack;

    public SubOrdersHolder(@NonNull View itemView) {
        super(itemView);

        //detail card
//        ll_detail_card=itemView.findViewById(R.id.ll_detail_card);
        ll_status_track=itemView.findViewById(R.id.ll_status_track);
        img_seller_logo=itemView.findViewById(R.id.img_seller_logo);
        img_order_up_arrow=itemView.findViewById(R.id.img_order_up_arrow);
        txt_seller_name=itemView.findViewById(R.id.txt_seller_name);
//        txt_seller_address=itemView.findViewById(R.id.txt_seller_address);
        txt_seller_price=itemView.findViewById(R.id.txt_seller_price);
//        txt_order_time=itemView.findViewById(R.id.txt_order_time);
        txt_order_status=itemView.findViewById(R.id.txt_order_status);
        txt_order_subtotal=itemView.findViewById(R.id.txt_order_subtotal);
//        txt_order_serviceCharge=itemView.findViewById(R.id.txt_order_serviceCharge);
        txt_order_total=itemView.findViewById(R.id.txt_order_total);
//        txt_order_number=itemView.findViewById(R.id.txt_order_number);
//        txt_order_date=itemView.findViewById(R.id.txt_order_date);
//        txt_order_paymode=itemView.findViewById(R.id.txt_order_paymode);
//        txt_order_deliAddress=itemView.findViewById(R.id.txt_order_deliAddress);
        rv_orderItem=itemView.findViewById(R.id.rv_orderItem);
        confirm_imageS=itemView.findViewById(R.id.confirm_image);
        otw_imageS=itemView.findViewById(R.id.otw_image);
        delivered_imageS=itemView.findViewById(R.id.delivered_image);

        ivProcessingDoneTrack=itemView.findViewById(R.id.ivProcessingDoneTrack);
        ivOTWDoneTrack=itemView.findViewById(R.id.ivOTWDoneTrack);
        ivDeliveredDoneTrack=itemView.findViewById(R.id.ivDeliveredDoneTrack);

    }
}
