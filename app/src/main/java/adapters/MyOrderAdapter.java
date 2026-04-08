package adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import adapters.ViewHolders.PastOrderViewHolder;
import adapters.ViewHolders.PendingOrderViewHolder;
import ModelClass.NewPendingOrderModel;
import com.grocery.QTPmart.R;
import util.Session_management;
import util.TodayOrderClickListner;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MyOrderAdapter extends RecyclerView.Adapter {

    SharedPreferences preferences;
    private List<NewPendingOrderModel> modelList;
    private Context context;
    private static int TYPE_Pending = 1;
    private static int TYPE_Past = 2;
    TodayOrderClickListner todayOrderClickListner;
    private Session_management session_management;
    private int lastPosition = -1;

    public MyOrderAdapter(Context context,List<NewPendingOrderModel> modelList,TodayOrderClickListner todayOrderClickListner,RecyclerView recyclerView) {
        this.context = context;
        this.modelList = modelList;
        this.todayOrderClickListner=todayOrderClickListner;
        session_management = new Session_management(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_Pending) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listtem_pendingorder, parent, false);
            return new PendingOrderViewHolder(view);

        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_my_past_order_rv, parent, false);
            return new PastOrderViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder myHolder, int position) {
        if (getItemViewType(position) == TYPE_Pending) {
            setPendingView((PendingOrderViewHolder) myHolder,position);


        } else {
            setPastView((PastOrderViewHolder) myHolder,position);


        }
//        switch (getItemViewType(position)) {
//            case "assigned":
//                MyAssignView myAssignView = (MyAssignView) holder;
//                myAssignView.assign_recy.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
//                myAssignView.assign_recy.setItemAnimator(new DefaultItemAnimator());
//                myAssignView.assign_recy.setAdapter(new My_Pending_Order_adapter(assignAndUnassigned.getTodayOrderModels(),todayOrderClickListner));
//                break;
//            case "unassigned":
//                MyUnAssignView myUnAssignView = (MyUnAssignView) holder;
//                myUnAssignView.unAssign_recy.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
//                myUnAssignView.unAssign_recy.setItemAnimator(new DefaultItemAnimator());
//                myUnAssignView.unAssign_recy.setAdapter(new My_Past_Order_adapter(assignAndUnassigned.getNextDayOrders(),todayOrderClickListner));
//                break;
//        }
    }

    private void setPendingView(PendingOrderViewHolder holder, int position){
        NewPendingOrderModel mList = modelList.get(position);
        holder.reorder_btn.setVisibility(View.GONE);
        holder.tv_orderno.setText(mList.getCart_id());

        holder.l1.setVisibility(View.GONE);
        if (mList.getOrder_status_code().equalsIgnoreCase("7")) {
            holder.relative_background.setCardBackgroundColor(getColor(0, 128, 0));
            holder.relativetextstatus.setText("Completed");
           // holder.l1.setVisibility(View.VISIBLE);
            holder.btn_lay.setVisibility(View.VISIBLE);
            holder.canclebtn.setVisibility(View.GONE);
            holder.Confirm.setVisibility(View.GONE);
            holder.Out_For_Deliverde.setVisibility(View.GONE);
            holder.Delivered.setVisibility(View.GONE);
            holder.Confirm1.setVisibility(View.VISIBLE);
            holder.Out_For_Deliverde1.setVisibility(View.VISIBLE);
            holder.Delivered1.setVisibility(View.VISIBLE);

        } else if (mList.getOrder_status_code().equalsIgnoreCase("2")) {
            holder.relativetextstatus.setText("Pending");
          //  holder.l1.setVisibility(View.VISIBLE);
            holder.btn_lay.setVisibility(View.VISIBLE);
            holder.Confirm.setVisibility(View.VISIBLE);
            holder.canclebtn.setVisibility(View.GONE);//change by me
            holder.Out_For_Deliverde.setVisibility(View.VISIBLE);
            holder.Delivered.setVisibility(View.VISIBLE);
            holder.Confirm1.setVisibility(View.GONE);
            holder.Out_For_Deliverde1.setVisibility(View.GONE);
            holder.Delivered1.setVisibility(View.GONE);
        } else if ( mList.getOrder_status_code().equalsIgnoreCase("3") ||mList.getOrder_status_code().equalsIgnoreCase("4") ||mList.getOrder_status_code().equalsIgnoreCase("5") ) {
            holder.relativetextstatus.setText("Confirmed");
           // holder.l1.setVisibility(View.VISIBLE);
            holder.btn_lay.setVisibility(View.VISIBLE);
            holder.canclebtn.setVisibility(View.GONE);
            holder.Confirm.setVisibility(View.GONE);
            holder.Out_For_Deliverde.setVisibility(View.VISIBLE);
            holder.Delivered.setVisibility(View.VISIBLE);
            holder.Confirm1.setVisibility(View.VISIBLE);
            holder.Out_For_Deliverde1.setVisibility(View.GONE);
            holder.Delivered1.setVisibility(View.GONE);
        } else if (mList.getOrder_status_code().equalsIgnoreCase("6")) {
            holder.relativetextstatus.setText("Out For Delivery");
            holder.btn_lay.setVisibility(View.VISIBLE);
            holder.canclebtn.setVisibility(View.GONE);
         //   holder.l1.setVisibility(View.VISIBLE);
            holder.Confirm.setVisibility(View.GONE);
            holder.Out_For_Deliverde.setVisibility(View.GONE);
            holder.Delivered.setVisibility(View.VISIBLE);
            holder.Confirm1.setVisibility(View.VISIBLE);
            holder.Out_For_Deliverde1.setVisibility(View.VISIBLE);
            holder.Delivered1.setVisibility(View.GONE);
        } else if (mList.getOrder_status_code().equalsIgnoreCase("9")) {
            holder.relative_background.setCardBackgroundColor(getColor(255, 0, 0));
            holder.relativetextstatus.setText("Cancelled");
//            holder.btn_lay.setVisibility(View.GONE);
            holder.canclebtn.setVisibility(View.GONE);
            holder.reorder_btn.setVisibility(View.VISIBLE);
            holder.order_details.setVisibility(View.VISIBLE);
           // holder.l1.setVisibility(View.GONE);
        }

        if (mList.getPayment_status() == null) {
            holder.tv_status.setText("Payment" + " " + "Pending");
        } else {
            if (mList.getPayment_status().equalsIgnoreCase("success") || mList.getPayment_status().equalsIgnoreCase("failed") || mList.getPayment_status().equalsIgnoreCase("COD")) {
                holder.tv_status.setText("Payment" + " " + mList.getPayment_status());
            }
        }

        holder.wallet_layout.setVisibility(View.GONE);


        holder.coupon_layout.setVisibility(View.GONE);


        holder.tv_delivery_amount.setVisibility(View.GONE);

        if (mList.getDboy_name() != null && !mList.getDboy_name().equalsIgnoreCase("")) {
            holder.order_assing_lay.setVisibility(View.VISIBLE);
            holder.iv_order_boy_name.setText(mList.getDboy_name());
            holder.iv_delivery_number.setText(mList.getDboy_phone());
        } else {
            holder.order_assing_lay.setVisibility(View.GONE);
        }

        holder.iv_order_detail_img.setOnClickListener(v -> {
            if (holder.delivery_boy_details.getVisibility() == View.VISIBLE) {
                holder.delivery_boy_details.setVisibility(View.GONE);
            } else {
                holder.delivery_boy_details.setVisibility(View.VISIBLE);
            }
        });

        holder.iv_call_order.setOnClickListener(v -> todayOrderClickListner.onCallToDeliveryBoy(mList.getDboy_phone()));


        holder.info_price.setVisibility(View.GONE);

        holder.tv_pending_date.setText(mList.getDelivery_date());
        holder.tv_confirm_date.setText(mList.getDelivery_date());
        holder.tv_delevered_date.setText(mList.getDelivery_date());
        holder.tv_cancel_date.setText(mList.getDelivery_date());

        if (mList.getPayment_method().equals("Store Pick Up")) {
            holder.tv_methid1.setText(mList.getPayment_method());
        } else if (mList.getPayment_method().equalsIgnoreCase("COD")) {
            holder.tv_methid1.setText("Cash On Delivery");
        } else if (mList.getPayment_method().equalsIgnoreCase("Cards")) {
            holder.tv_methid1.setText("PrePaid");
        } else if (mList.getPayment_method().equalsIgnoreCase("net_banking")) {
            holder.tv_methid1.setText("PrePaid");
        } else if (mList.getPayment_method().equalsIgnoreCase("Wallet")) {
            holder.tv_methid1.setText("Wallet");
        }



        holder.tv_date.setText(mList.getDelivery_date());
        holder.tv_tracking_date.setText(mList.getDelivery_date());

        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        String language = preferences.getString("language", "");

        if (language.contains("spanish")) {
            String timefrom = mList.getTime_slot();


            timefrom = timefrom.replace("pm", "م");
            timefrom = timefrom.replace("am", "ص");


            String time = timefrom;

            holder.tv_time.setText(time);
        } else {
            holder.tv_time.setText(mList.getTime_slot());
        }

        holder.tv_price.setText(session_management.getCurrency() + "" + mList.getPrice());

        if (mList.getShipping()!= null)
        {
            holder.tv_shipping.setText(session_management.getCurrency() + "" +mList.getShipping());
        }
        else {
            holder.tv_shipping.setText(session_management.getCurrency() + "" +0);
        }

        if (mList.getDiscount()!= null)
        {
            holder.tv_discount.setText(session_management.getCurrency() + "" +mList.getDiscount());
        }
        else {
            holder.tv_discount.setText(session_management.getCurrency() + "" +0);
        }

        if (mList.getTax()!= null)
        {
            holder.tv_tax.setText(session_management.getCurrency() + "" +mList.getTax());
        }
        else {
            holder.tv_tax.setText(session_management.getCurrency() + "" +0);
        }

        if (mList.getGrandtotal()!= null)
        {
            holder.tv_pay_ableamount.setText(session_management.getCurrency() + "" +mList.getGrandtotal());
        }
        else {
            holder.tv_pay_ableamount.setText(session_management.getCurrency() + "" +0);
        }

        if (mList.getSubTotal()!= null)
        {
            holder.tv_sub_total.setText(session_management.getCurrency() + "" +mList.getSubTotal());
        }
        else {
            holder.tv_sub_total.setText(session_management.getCurrency() + "" +0);
        }
//        if (mList.getRemaining_amount() != null && !mList.getRemaining_amount().equalsIgnoreCase("")) {
//            holder.tv_pay_ableamount.setText(session_management.getCurrency() + "" + mList.getRemaining_amount());
//            holder.tv_total_pay.setText(session_management.getCurrency() + "" + mList.getRemaining_amount());
//        } else {
//            holder.tv_pay_ableamount.setText(session_management.getCurrency() + "" + mList.getPrice());
//        }

        holder.tv_item.setText("" + mList.getData().size());

        holder.order_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todayOrderClickListner.onClickForOrderDetails(holder.getAdapterPosition(),"pending");
            }
        });

//        holder.reorder_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                todayOrderClickListner.onReorderClick(position,"pending");
//            }
//        });

//        holder.canclebtn.setOnClickListener(v -> {
////            showDeleteDialog(position);
//            todayOrderClickListner.onCancelClick(position,"pending");
//        });
    }

    private void setPastView(PastOrderViewHolder holder, int position){
        NewPendingOrderModel mList = modelList.get(position);
        holder.tv_orderno.setText(mList.getCart_id());
        holder.canclebtn.setVisibility(View.GONE);
        holder.reorder_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todayOrderClickListner.onReorderClick(position, "past");
            }
        });

        if (mList.getOrder_status_code().equalsIgnoreCase("7")) {
            holder.relative_background.setCardBackgroundColor(getColor(0, 128, 0));
            holder.relativetextstatus.setText("Completed");
            holder.l1.setVisibility(View.VISIBLE);
            holder.reorder_btn.setVisibility(View.VISIBLE);
            holder.Confirm.setVisibility(View.GONE);
            holder.Out_For_Deliverde.setVisibility(View.GONE);
            holder.Delivered.setVisibility(View.GONE);
            holder.Confirm1.setVisibility(View.VISIBLE);
            holder.Out_For_Deliverde1.setVisibility(View.VISIBLE);
            holder.Delivered1.setVisibility(View.VISIBLE);

        } else if (mList.getOrder_status_code().equalsIgnoreCase("2")) {
            holder.relativetextstatus.setText("Pending");
            holder.l1.setVisibility(View.VISIBLE);
            holder.reorder_btn.setVisibility(View.VISIBLE);
            holder.Confirm.setVisibility(View.VISIBLE);
            holder.Out_For_Deliverde.setVisibility(View.VISIBLE);
            holder.Delivered.setVisibility(View.VISIBLE);
            holder.Confirm1.setVisibility(View.GONE);
            holder.Out_For_Deliverde1.setVisibility(View.GONE);
            holder.Delivered1.setVisibility(View.GONE);

        } else if (mList.getOrder_status_code().equalsIgnoreCase("3")  ||mList.getOrder_status_code().equalsIgnoreCase("4") ||mList.getOrder_status_code().equalsIgnoreCase("5")) {
            holder.relativetextstatus.setText("Confirmed");
            holder.l1.setVisibility(View.VISIBLE);
            holder.reorder_btn.setVisibility(View.VISIBLE);
            holder.Confirm.setVisibility(View.GONE);
            holder.Out_For_Deliverde.setVisibility(View.VISIBLE);
            holder.Delivered.setVisibility(View.VISIBLE);
            holder.Confirm1.setVisibility(View.VISIBLE);
            holder.Out_For_Deliverde1.setVisibility(View.GONE);
            holder.Delivered1.setVisibility(View.GONE);
        } else if (mList.getOrder_status().equalsIgnoreCase("6")) {
            holder.relativetextstatus.setText("Out For Delivery");
            holder.reorder_btn.setVisibility(View.VISIBLE);
            holder.l1.setVisibility(View.VISIBLE);
            holder.Confirm.setVisibility(View.GONE);
            holder.Out_For_Deliverde.setVisibility(View.GONE);
            holder.Delivered.setVisibility(View.VISIBLE);
            holder.Confirm1.setVisibility(View.VISIBLE);
            holder.Out_For_Deliverde1.setVisibility(View.VISIBLE);
            holder.Delivered1.setVisibility(View.GONE);
        } else if (mList.getOrder_status().equalsIgnoreCase("9")) {
            holder.relative_background.setCardBackgroundColor(getColor(255, 0, 0));
            holder.relativetextstatus.setText("Cancelled");
            holder.reorder_btn.setVisibility(View.GONE);
            holder.l1.setVisibility(View.GONE);
        }

        if (mList.getPayment_status() == null) {
            holder.tv_status.setText("Payment:-" + " " + "Pending");
        } else {
            if (mList.getPayment_status().equalsIgnoreCase("success") || mList.getPayment_status().equalsIgnoreCase("failed") || mList.getPayment_status().equalsIgnoreCase("COD")) {
                holder.tv_status.setText("Payment:-" + " " + mList.getPayment_status());
            }
        }

        if (mList.getPaid_by_wallet() != null && !mList.getPaid_by_wallet().equalsIgnoreCase("") && !mList.getPaid_by_wallet().equalsIgnoreCase("0")) {
            holder.wallet_layout.setVisibility(View.VISIBLE);
            holder.tv_wallet_amount.setText("- " + session_management.getCurrency() + "" + mList.getPaid_by_wallet());
        } else {
            holder.wallet_layout.setVisibility(View.GONE);
        }

        if (mList.getCoupon_discount() != null && !mList.getCoupon_discount().equalsIgnoreCase("") && !mList.getCoupon_discount().equalsIgnoreCase("0")) {
            holder.coupon_layout.setVisibility(View.VISIBLE);
            holder.tv_coupon_amount.setText("- " + session_management.getCurrency() + "" + mList.getCoupon_discount());
        } else {
            holder.coupon_layout.setVisibility(View.GONE);
        }

        if (mList.getDel_charge() != null && !mList.getDel_charge().equalsIgnoreCase("")) {
            holder.tv_delivery_amount.setText(session_management.getCurrency() + "" + mList.getDel_charge());
            holder.tv_order_price_2.setText(session_management.getCurrency() + "" + ((int) (Double.parseDouble(mList.getPrice()) - Double.parseDouble(mList.getDel_charge()))));
        } else {
            holder.tv_order_price_2.setText(session_management.getCurrency() + "" + mList.getPrice());
            holder.tv_delivery_amount.setText(session_management.getCurrency() + " 0");
        }

//        holder.info_price.setOnClickListener(v -> {
//            if (holder.price_deatils.getVisibility() == View.VISIBLE) {
//                holder.price_deatils.setVisibility(View.GONE);
//            } else {
//                holder.price_deatils.setVisibility(View.VISIBLE);
//            }
//        });
        holder.info_price.setVisibility(View.GONE);

        holder.tv_pending_date.setText(mList.getDelivery_date());
        holder.tv_confirm_date.setText(mList.getDelivery_date());
        holder.tv_delevered_date.setText(mList.getDelivery_date());
        holder.tv_cancel_date.setText(mList.getDelivery_date());
        holder.tv_methid1.setText(mList.getPayment_method());
        holder.tv_date.setText(mList.getDelivery_date());
        holder.tv_tracking_date.setText(mList.getDelivery_date());

        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        String language = preferences.getString("language", "");
        if (language.contains("spanish")) {
            String timefrom = mList.getTime_slot();
            timefrom = timefrom.replace("pm", "م");
            timefrom = timefrom.replace("am", "ص");
            holder.tv_time.setText(timefrom);
        } else {
            holder.tv_time.setText(mList.getTime_slot());
        }

        holder.tv_price.setText(session_management.getCurrency() + "" + mList.getPrice());
        if (mList.getShipping()!= null)
        {
            holder.tv_shipping.setText(session_management.getCurrency() + "" +mList.getShipping());
        }
        else {
            holder.tv_shipping.setText(session_management.getCurrency() + "" +0);
        }

        if (mList.getDiscount()!= null)
        {
            holder.tv_discount.setText(session_management.getCurrency() + "" +mList.getDiscount());
        }
        else {
            holder.tv_discount.setText(session_management.getCurrency() + "" +0);
        }

        if (mList.getTax()!= null)
        {
            holder.tv_tax.setText(session_management.getCurrency() + "" +mList.getTax());
        }
        else {
            holder.tv_tax.setText(session_management.getCurrency() + "" +0);
        }

        if (mList.getGrandtotal()!= null)
        {
            holder.tv_pay_ableamount.setText(session_management.getCurrency() + "" +mList.getGrandtotal());
        }
        else {
            holder.tv_pay_ableamount.setText(session_management.getCurrency() + "" +0);
        }

        if (mList.getSubTotal()!= null)
        {
            holder.tv_sub_total.setText(session_management.getCurrency() + "" +mList.getSubTotal());
        }
        else {
            holder.tv_sub_total.setText(session_management.getCurrency() + "" +0);
        }
//        if (mList.getRemaining_amount() != null && !mList.getRemaining_amount().equalsIgnoreCase("")) {
//            holder.tv_pay_ableamount.setText(session_management.getCurrency() + "" + mList.getRemaining_amount());
//            holder.tv_total_pay.setText(session_management.getCurrency() + "" + mList.getRemaining_amount());
//        } else {
//            holder.tv_pay_ableamount.setText(session_management.getCurrency() + "" + mList.getPrice());
//        }
        holder.tv_item.setText(context.getResources().getString(R.string.tv_cart_item) + mList.getData().size());
        holder.tv_pending_date.setText(mList.getDelivery_date());
        holder.tv_confirm_date.setText(mList.getDelivery_date());
        holder.tv_delevered_date.setText(mList.getDelivery_date());
        holder.tv_cancel_date.setText(mList.getDelivery_date());

        holder.order_details.setOnClickListener(view -> {
            todayOrderClickListner.onClickForOrderDetails(position, "past");
        });
    }
    public int getColor(int r, int g, int b) {
        return Color.rgb(r, g, b);
    }
    @Override
    public int getItemViewType(int position) {
        if(modelList.get(position).getOrder_status_code().equals("7"))
        {
            return TYPE_Past;
        }
        else
        {
            return TYPE_Pending;
        }
//        switch (modelList.get(position).get()) {
//            case "assigned":
//                return 0;
//            case "unassigned":
//                return 1;
//        }
        //return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }




}
