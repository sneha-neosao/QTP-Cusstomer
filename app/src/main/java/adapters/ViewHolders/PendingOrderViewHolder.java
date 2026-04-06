package adapters.ViewHolders;

import android.animation.AnimatorSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.grocery.QTPmart.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class PendingOrderViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_orderno, tv_status, tv_date, tv_time, tv_price, tv_item, relativetextstatus, tv_tracking_date, tv_pay_ableamount, tv_order_price_2, tv_wallet_amount, tv_coupon_amount, tv_delivery_amount, tv_total_pay;
        public TextView tv_pending_date, tv_pending_time, tv_confirm_date, tv_confirm_time, tv_delevered_date, tv_delevered_time, tv_cancel_date, tv_cancel_time;
        public View view1, view2, view3, view4, view5, view6;
        public CardView relative_background;
        public LinearLayout rr, price_deatils;
        public CircleImageView Confirm, Out_For_Deliverde, Delivered;
        public CircleImageView Confirm1, Out_For_Deliverde1, Delivered1;
        public TextView tv_methid1, tv_sub_total,tv_tax,tv_shipping,tv_discount;
        public ImageView info_price;
        public String method;
        //        CardView cardView;
        public TextView canclebtn, reorder_btn, order_details;

        public TextView iv_order_boy_name, iv_delivery_number;
        public LinearLayout delivery_boy_details, order_assing_lay;
        public ImageView iv_call_order, iv_order_detail_img;
        //        LinearLayout linearLayout;
        public LinearLayout l1;
        public LinearLayout btn_lay;
        public LinearLayout wallet_layout, coupon_layout, delivery_layout;

        public PendingOrderViewHolder(View view) {

            super(view);
//            tv_orderno = view.findViewById(R.id.tv_order_no);
//            tv_pay_ableamount = view.findViewById(R.id.tv_pay_ableamount);
//            order_assing_lay = view.findViewById(R.id.order_assing_lay);
//            iv_order_detail_img = view.findViewById(R.id.iv_order_detail_img);
//            iv_delivery_number = view.findViewById(R.id.iv_delivery_number);
//            iv_order_boy_name = view.findViewById(R.id.iv_order_boy_name);
//            delivery_boy_details = view.findViewById(R.id.delivery_boy_details);
//            iv_call_order = view.findViewById(R.id.iv_call_order);
//            tv_order_price_2 = view.findViewById(R.id.tv_order_price_2);
//            tv_coupon_amount = view.findViewById(R.id.tv_coupon_amount);
//            tv_total_pay = view.findViewById(R.id.tv_total_pay);
//            tv_delivery_amount = view.findViewById(R.id.tv_delivery_amount);
//            tv_wallet_amount = view.findViewById(R.id.tv_wallet_amount);
//            delivery_layout = view.findViewById(R.id.delivery_layout);
//            wallet_layout = view.findViewById(R.id.wallet_layout);
//            coupon_layout = view.findViewById(R.id.coupon_layout);
//            price_deatils = view.findViewById(R.id.price_deatils);
//            info_price = view.findViewById(R.id.info_price);
//            tv_status = view.findViewById(R.id.tv_order_status);
//            relativetextstatus = view.findViewById(R.id.status);
//            tv_tracking_date = view.findViewById(R.id.tracking_date);
//            tv_date = view.findViewById(R.id.tv_order_date);
//            tv_time = view.findViewById(R.id.tv_order_time);
//            tv_price = view.findViewById(R.id.tv_order_price);
//            tv_item = view.findViewById(R.id.tv_order_item);
//            canclebtn = view.findViewById(R.id.canclebtn);
//            reorder_btn = view.findViewById(R.id.reorder_btn);
//            l1 = view.findViewById(R.id.l1);
//            btn_lay = view.findViewById(R.id.btn_lay);
//            rr = view.findViewById(R.id.rrrr);
//            order_details = view.findViewById(R.id.order_details);
//
//            tv_sub_total = view.findViewById(R.id.tv_sub_total);
//            tv_tax = view.findViewById(R.id.tv_tax);
//            tv_shipping = view.findViewById(R.id.tv_shipping);
//            tv_discount = view.findViewById(R.id.tv_discount);

//            cardView = view.findViewById(R.id.card_view);

//            linearLayout = view.findViewById(R.id.l2);
//            //Payment Method
//            tv_methid1 = view.findViewById(R.id.method1);
//            //Date And Time
//            tv_pending_date = view.findViewById(R.id.pending_date);
////            tv_pending_time = (TextView) view.findViewById(R.id.pending_time);
//            tv_confirm_date = view.findViewById(R.id.confirm_date);
////            tv_confirm_time = (TextView) view.findViewById(R.id.confirm_time);
//            tv_delevered_date = view.findViewById(R.id.delevered_date);
////            tv_delevered_time = (TextView) view.findViewById(R.id.delevered_time);
//            tv_cancel_date = view.findViewById(R.id.cancel_date);
////            tv_cancel_time = (TextView) view.findViewById(R.id.cancel_time);
//            //Oredre Tracking
//            view1 = view.findViewById(R.id.view1);
//            view2 = view.findViewById(R.id.view2);
//            view3 = view.findViewById(R.id.view3);
//            view4 = view.findViewById(R.id.view4);
//            view5 = view.findViewById(R.id.view5);
//            view6 = view.findViewById(R.id.view6);
//            relative_background = view.findViewById(R.id.relative_background);
//
//            Confirm = view.findViewById(R.id.confirm_image);
//            Out_For_Deliverde = view.findViewById(R.id.delivered_image);
//            Delivered = view.findViewById(R.id.cancal_image);
//            Confirm1 = view.findViewById(R.id.confirm_image1);
//            Out_For_Deliverde1 = view.findViewById(R.id.delivered_image1);
//            Delivered1 = view.findViewById(R.id.cancal_image1);
        }


    }