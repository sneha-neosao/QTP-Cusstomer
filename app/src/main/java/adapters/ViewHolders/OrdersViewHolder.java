package adapters.ViewHolders;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.grocery.QTPmart.R;
import com.ncorti.slidetoact.SlideToActView;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrdersViewHolder extends RecyclerView.ViewHolder{

    //main order card
    public CardView mcard_main;
    public EditText edt_blank;
    public Button btn_repeat_order,btn_cancel_order,btnRetryOrder,btnRetryPayment;
    public RecyclerView recyclerView;
   public LinearLayout ll_main_card,ll_order_detail,ll_order_status,llStar,llStar2,llRating,
           llDeliveryBoyName,llDeliveryBoyContact,llDeliveryBoyCurrentLocation,llDeliveryBoyDetails,llDeliveryFeedback,ll_status_track;
    public ImageView img_order_status;
    public TextView txt_orderNum,txt_order_dateTime,txt_order_payStatus,txt_orderTotal_Qty,
            txt_orderStatus,txt_order_number,txt_order_date,txt_order_paymode,
            txt_order_deliAddress,txt_order_Qty,txt_days_ago,
            txt_orderSubtotal,txt_orderShipping,txt_orderTax,txt_orderDis,txt_orderGrand,tvShowMore,tvShowLess,tvRating,
            tvDeliveryBoyName,tvDeliveryBoyContact,tvDeliveryBoyCurrentLocation,tvAssignDeliveryMenMessage;

    //detail order card
    public LinearLayout ll_detail_card;
    public ImageView img_seller_logo,img_order_up_arrow,ivDeliveryFeedback;

    public SlideToActView btn_repeat_order1;
    public CardView cvMain,cvOrderDetails;

    public CircleImageView confirm_imageS,otw_imageS,delivered_imageS;
    public CircleImageView ivProcessingDoneTrack,ivOTWDoneTrack,ivDeliveredDoneTrack;


    public ImageView ivStar1_1,ivStar1_2,ivStar1_3,ivStar1_4,ivStar1_5,ivRating;
    public ImageView ivStar2_1,ivStar2_2,ivStar2_3,ivStar2_4,ivStar2_5;

    public OrdersViewHolder(@NonNull View itemView) {
        super(itemView);
//        mcard_main=itemView.findViewById(R.id.mcard_main);
//        btn_repeat_order=itemView.findViewById(R.id.btn_repeat_order);
//        btn_cancel_order=itemView.findViewById(R.id.btn_cancel_order);
//        //main order card
//        ll_main_card=itemView.findViewById(R.id.ll_main_card);
//        img_order_status=itemView.findViewById(R.id.img_order_status);
//        txt_orderNum=itemView.findViewById(R.id.txt_orderNum);
//        txt_order_dateTime=itemView.findViewById(R.id.txt_order_dateTime);
//        txt_order_payStatus=itemView.findViewById(R.id.txt_order_payStatus);
//        txt_orderTotal_Qty=itemView.findViewById(R.id.txt_orderTotal_Qty);
//        txt_orderStatus=itemView.findViewById(R.id.txt_orderStatus);
//        recyclerView=itemView.findViewById(R.id.rv_orderDetail);
//        img_order_up_arrow=itemView.findViewById(R.id.img_order_up_arrow);
//        txt_order_Qty=itemView.findViewById(R.id.txt_order_Qty);
//
//        txt_orderSubtotal=itemView.findViewById(R.id.txt_orderSubtotal);
//        txt_orderShipping=itemView.findViewById(R.id.txt_orderShipping);
//        txt_orderTax=itemView.findViewById(R.id.txt_orderTax);
//        txt_orderDis=itemView.findViewById(R.id.txt_orderDis);
//        txt_orderGrand=itemView.findViewById(R.id.txt_orderGrand);
//
//
//        ll_order_detail=itemView.findViewById(R.id.ll_order_details);
//        btn_repeat_order1=itemView.findViewById(R.id.btn_repeat_order1);
//        ll_order_status=itemView.findViewById(R.id.ll_order_status);
//        cvMain=itemView.findViewById(R.id.cvMain);
//        tvShowMore=itemView.findViewById(R.id.tvShowMore);
//        tvShowLess=itemView.findViewById(R.id.tvShowLess);
//        llStar=itemView.findViewById(R.id.llStar);
//        llStar2=itemView.findViewById(R.id.llStar2);
//        cvOrderDetails=itemView.findViewById(R.id.cvOrderDetails);
//
//        confirm_imageS=itemView.findViewById(R.id.confirm_image);
//        otw_imageS=itemView.findViewById(R.id.otw_image);
//        delivered_imageS=itemView.findViewById(R.id.delivered_image);
//
//        ivStar1_1=itemView.findViewById(R.id.ivStar1_1);
//        ivStar1_2=itemView.findViewById(R.id.ivStar1_2);
//        ivStar1_3=itemView.findViewById(R.id.ivStar1_3);
//        ivStar1_4=itemView.findViewById(R.id.ivStar1_4);
//        ivStar1_5=itemView.findViewById(R.id.ivStar1_5);
//
//        ivStar2_1=itemView.findViewById(R.id.ivStar2_1);
//        ivStar2_2=itemView.findViewById(R.id.ivStar2_2);
//        ivStar2_3=itemView.findViewById(R.id.ivStar2_3);
//        ivStar2_4=itemView.findViewById(R.id.ivStar2_4);
//        ivStar2_5=itemView.findViewById(R.id.ivStar2_5);
//
//        llRating=itemView.findViewById(R.id.llRating);
//        ivRating=itemView.findViewById(R.id.ivRating);
//        tvRating=itemView.findViewById(R.id.tvRating);
//
//        tvDeliveryBoyName=itemView.findViewById(R.id.tvDeliveryBoyName);
//        tvDeliveryBoyContact=itemView.findViewById(R.id.tvDeliveryBoyContact);
//        tvDeliveryBoyCurrentLocation=itemView.findViewById(R.id.tvDeliveryBoyCurrentLocation);
//
//        llDeliveryBoyName=itemView.findViewById(R.id.llDeliveryBoyName);
//        llDeliveryBoyContact=itemView.findViewById(R.id.llDeliveryBoyContact);
//        llDeliveryBoyCurrentLocation=itemView.findViewById(R.id.llDeliveryBoyCurrentLocation);
//        llDeliveryBoyDetails=itemView.findViewById(R.id.llDeliveryBoyDetails);
//        llDeliveryFeedback=itemView.findViewById(R.id.llDeliveryFeedback);
//        txt_days_ago=itemView.findViewById(R.id.txt_days_ago);
//        ivDeliveryFeedback=itemView.findViewById(R.id.ivDeliveryFeedback);
//        btnRetryPayment=itemView.findViewById(R.id.btnRetryPayment);
//        btnRetryOrder=itemView.findViewById(R.id.btnRetryOrder);
//
//        ll_status_track=itemView.findViewById(R.id.ll_status_track);
//        tvAssignDeliveryMenMessage=itemView.findViewById(R.id.tvAssignDeliveryMenMessage);
//
//        ivProcessingDoneTrack=itemView.findViewById(R.id.ivProcessingDoneTrack);
//        ivOTWDoneTrack=itemView.findViewById(R.id.ivOTWDoneTrack);
//        ivDeliveredDoneTrack=itemView.findViewById(R.id.ivDeliveredDoneTrack);
    }
}
