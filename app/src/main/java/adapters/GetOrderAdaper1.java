package adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import activities.CartActivity;
import adapters.ViewHolders.OrdersViewHolder;
import Config.ApiBaseURL;
import Config.BaseURL;
import ModelClass.NewGetOrderModel;
import ModelClass.NewOrderItem;
import ModelClass.NewSuborder;
import PaymentDataModels.CreateOrderResponseDto;
import PaymentDataModels1.ResponseOrderModel1;
import com.grocery.QTPmart.R;
import network.Response.ResponseAddDeliveryRatingReview;
import network.Response.ResponseAddRatingReview;
import network.Response.ResponseUpdateOrderStatus;
import network.Response.ResponseUpdatePaymentStatus;
import network.ServiceGenrator;
import util.AppController;
import util.CustomVolleyJsonRequest;
import util.DatabaseHandler;
import util.OrderCancelListner;
import util.Session_management;
import util.ViewNotifier;
import com.ncorti.slidetoact.SlideToActView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;

public class GetOrderAdaper1 extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    ArrayList<NewGetOrderModel> newGetOrderModelArrayList;
//    ArrayList<NewSuborder> newSuborderArrayList;
//    ArrayList<NewOrderItem> newOrderItemArrayList;
    NewSuborder newSuborder;
    private ViewNotifier viewNotifier;
    NestedScrollView nestedScrollView;
    private Session_management session_management;
    private DatabaseHandler dbcart;
    RecyclerView rv_order;
    NewOrderItem newOrderItem;
    View status_track,order_items;
    int tempPos=-1;
    int qty=0;
    ProgressDialog progressDialog;
    OrderCancelListner orderCancelListner;

    String custID;
    String deliveryRating;

    OnRetryOrderClickListener onRetryOrderClickListener;
    OnRetryPaymentClickListener onRetryPaymentClickListener;

    private boolean isLoadingAdded = false;

    private int VIEW_TYPE_ITEM = 0;
    private int VIEW_TYPE_LOADING = 1;
    int k=0;
    int uploadPosition=0;

    public GetOrderAdaper1(Context context, RecyclerView rv_order,
                           ArrayList<NewGetOrderModel> newGetOrderModelArrayList, OrderCancelListner orderCancelListner,OnRetryOrderClickListener onRetryOrderClickListener,
                           OnRetryPaymentClickListener onRetryPaymentClickListener)
    {
        this.context=context;
        this.rv_order=rv_order;
        this.newGetOrderModelArrayList=newGetOrderModelArrayList;
        dbcart = new DatabaseHandler(context);
        session_management=new Session_management(context);
        progressDialog=new ProgressDialog(context);
        this.onRetryOrderClickListener = onRetryOrderClickListener;
        this.onRetryPaymentClickListener = onRetryPaymentClickListener;
        this.orderCancelListner=orderCancelListner;
//        this.newSuborderArrayList=newSuborderArrayList;
//        this.newOrderItemArrayList=newOrderItemArrayList;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       /* View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_myorder_new_1, parent, false);
        return new OrdersViewHolder(view);*/

         if (viewType == VIEW_TYPE_ITEM){
             return new OrdersViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout_myorder_new_1, parent, false));
        } else{
             return new LoadingViewHolder(LayoutInflater.from(context).inflate(R.layout.item_progress_bar, parent, false));
         }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {
            OrdersViewHolder ordersViewHolder = (OrdersViewHolder) holder;
            ordersViewHolder.ll_order_status.setVisibility(View.GONE);
            ordersViewHolder.cvOrderDetails.setVisibility(View.GONE);
            ordersViewHolder.llStar.setVisibility(View.GONE);
            reset1(ordersViewHolder);
            ordersViewHolder.llRating.setVisibility(View.GONE);
            ordersViewHolder.llRating.setVisibility(View.GONE);


        NewGetOrderModel newGetOrderModel = newGetOrderModelArrayList.get(holder.getAdapterPosition());
        ArrayList<NewSuborder> newSuborderArrayList = newGetOrderModel.getSubOrders();
        dbcart = new DatabaseHandler(context);
        if (newSuborderArrayList != null && newSuborderArrayList.size() > 0) {
            // newSuborder=newSuborderArrayList.get(position);
            OrderDetailAdapter1 orderDetailAdapter = new OrderDetailAdapter1(context, newSuborderArrayList,newGetOrderModel.getOrderID());
            ordersViewHolder.recyclerView.setAdapter(orderDetailAdapter);
        }
        double tAmt = Double.parseDouble(newGetOrderModel.getGrandtotal());
            ordersViewHolder.txt_orderNum.setText("Order #" + newGetOrderModel.getOrderRef());
            ordersViewHolder.txt_orderSubtotal.setText("AED " + newGetOrderModel.getSubTotal());
            ordersViewHolder.txt_orderShipping.setText("AED " + String.format("%.2f", Double.parseDouble(newGetOrderModel.getShipping())));
            ordersViewHolder.txt_orderTax.setText("AED " + String.format("%.2f", Double.parseDouble(newGetOrderModel.getTax())));
            ordersViewHolder.txt_orderDis.setText("AED " + String.format("%.2f", Double.parseDouble(newGetOrderModel.getDiscount())));
            ordersViewHolder.txt_orderGrand.setText("AED " + String.format("%.2f", Double.parseDouble(newGetOrderModel.getGrandtotal())));

        Log.e("Shipping", newGetOrderModel.getShipping());

        //holder.txt_order_dateTime.setText(newGetOrderModel.getOrderDate()+" "+newGetOrderModel.getOrderTime());
            ordersViewHolder.txt_order_dateTime.setText(newGetOrderModel.getOrderPickUpDate());
        // holder.txt_order_payStatus.setText(newGetOrderModel.getOrderStatus());
            ordersViewHolder.txt_orderStatus.setText(newGetOrderModel.getOrderStatus1());
            ordersViewHolder.txt_orderTotal_Qty.setText("Total: AED " + String.format("%.2f", tAmt) + " / QTY : " + newGetOrderModel.getItemCount());
        //holder.txt_order_Qty.setText("QTY :"+newGetOrderModel.getItemCount());

            ordersViewHolder.txt_days_ago.setText(newGetOrderModel.getDateAgo());

            if(newGetOrderModel.getOrderStatus().equals("12")||newGetOrderModel.getOrderStatus().equals("24")){
                ordersViewHolder.llRating.setVisibility(View.GONE);
            }else{
                ordersViewHolder.llRating.setVisibility(View.VISIBLE);
            }

        if (newGetOrderModel.getOrderStatus().equals("2")) {
            //Visible:Pending
            //Hide:Accepted,On the way,Delivered
            ordersViewHolder.btn_cancel_order.setVisibility(View.VISIBLE);
            ordersViewHolder.confirm_imageS.setVisibility(View.VISIBLE);
            ordersViewHolder.otw_imageS.setVisibility(View.VISIBLE);
            ordersViewHolder.delivered_imageS.setVisibility(View.VISIBLE);
            ordersViewHolder.ivProcessingDoneTrack.setVisibility(View.GONE);
            ordersViewHolder.ivOTWDoneTrack.setVisibility(View.GONE);
            ordersViewHolder.ivDeliveredDoneTrack.setVisibility(View.GONE);
            Glide.with(context).load(R.drawable.pending_cv).into(ordersViewHolder.img_order_status);
        } else if (newGetOrderModel.getOrderStatus().equals("3")) {
            //Visible:Pending,Accepted
            //Hide:On the way,Delivered
//            Glide.with(context).load(R.drawable.delivered_accepted).into(ordersViewHolder.img_order_status);
            ordersViewHolder.confirm_imageS.setVisibility(View.GONE);
            ordersViewHolder.ivProcessingDoneTrack.setVisibility(View.VISIBLE);
        } else if (newGetOrderModel.getOrderStatus().equals("4")) {
            //Visible:Pending,Accepted
            //Hide:On the way,Delivered
            Glide.with(context).load(R.drawable.ready_for_pickup).into(ordersViewHolder.img_order_status);
            ordersViewHolder.confirm_imageS.setVisibility(View.GONE);
            ordersViewHolder.ivProcessingDoneTrack.setVisibility(View.VISIBLE);
        } else if (newGetOrderModel.getOrderStatus().equals("5")) {
            //Visible:Pending
            //Hide:Accepted,On the way,Delivered
            Glide.with(context).load(R.drawable.processing).into(ordersViewHolder.img_order_status);
            ordersViewHolder.confirm_imageS.setVisibility(View.VISIBLE);
            ordersViewHolder.ivProcessingDoneTrack.setVisibility(View.GONE);
        } else if (newGetOrderModel.getOrderStatus().equals("8")) {
            //Visible:Pending
            //Hide:Accepted,On the way,Delivered
            Glide.with(context).load(R.drawable.processing).into(ordersViewHolder.img_order_status);
            ordersViewHolder.confirm_imageS.setVisibility(View.VISIBLE);
            ordersViewHolder.ivProcessingDoneTrack.setVisibility(View.GONE);
        } else if (newGetOrderModel.getOrderStatus().equals("11")) {
            //Visible:Pending
            //Hide:Accepted,On the way,Delivered
            Glide.with(context).load(R.drawable.processing).into(ordersViewHolder.img_order_status);
            ordersViewHolder.confirm_imageS.setVisibility(View.VISIBLE);
            ordersViewHolder.ivProcessingDoneTrack.setVisibility(View.GONE);
        } else if (newGetOrderModel.getOrderStatus().equals("6")) {
            //Visible:Pending,Accepted,On the way
            //Hide:Delivered
            Glide.with(context).load(R.drawable.on_the_way).into(ordersViewHolder.img_order_status);
            ordersViewHolder.confirm_imageS.setVisibility(View.GONE);
            ordersViewHolder.ivProcessingDoneTrack.setVisibility(View.VISIBLE);
            ordersViewHolder.otw_imageS.setVisibility(View.GONE);
            ordersViewHolder.ivOTWDoneTrack.setVisibility(View.VISIBLE);
        } else if (newGetOrderModel.getOrderStatus().equals("7")) {
            //Visible:Pending,Accepted,On the way,Delivered
            //Hide:
            Glide.with(context).load(R.drawable.delivered).into(ordersViewHolder.img_order_status);
            ordersViewHolder.confirm_imageS.setVisibility(View.GONE);
            ordersViewHolder.ivProcessingDoneTrack.setVisibility(View.VISIBLE);
            ordersViewHolder.otw_imageS.setVisibility(View.GONE);
            ordersViewHolder.ivOTWDoneTrack.setVisibility(View.VISIBLE);
            ordersViewHolder.delivered_imageS.setVisibility(View.GONE);
            ordersViewHolder.ivDeliveredDoneTrack.setVisibility(View.VISIBLE);
        } else if (newGetOrderModel.getOrderStatus().equals("1")) {
            //Visible:Pending
            //Hide:Accepted,On the way,Delivered
            ordersViewHolder.btn_cancel_order.setVisibility(View.VISIBLE);
            ordersViewHolder.confirm_imageS.setVisibility(View.VISIBLE);
            ordersViewHolder.otw_imageS.setVisibility(View.VISIBLE);
            ordersViewHolder.delivered_imageS.setVisibility(View.VISIBLE);
            ordersViewHolder.ivProcessingDoneTrack.setVisibility(View.GONE);
            ordersViewHolder.ivOTWDoneTrack.setVisibility(View.GONE);
            ordersViewHolder.ivDeliveredDoneTrack.setVisibility(View.GONE);
            Glide.with(context).load(R.drawable.order_placed).into(ordersViewHolder.img_order_status);
        } else if (newGetOrderModel.getOrderStatus().equals("21")) {
            //Visible:Pending
            //Hide:Accepted,On the way,Delivered
            Glide.with(context).load(R.drawable.partially_accepted).into(ordersViewHolder.img_order_status);
            ordersViewHolder.confirm_imageS.setVisibility(View.VISIBLE);
            ordersViewHolder.otw_imageS.setVisibility(View.VISIBLE);
            ordersViewHolder.delivered_imageS.setVisibility(View.VISIBLE);
            ordersViewHolder.ivProcessingDoneTrack.setVisibility(View.GONE);
            ordersViewHolder.ivOTWDoneTrack.setVisibility(View.GONE);
            ordersViewHolder.ivDeliveredDoneTrack.setVisibility(View.GONE);
        } else if (newGetOrderModel.getOrderStatus().equals("9")) {
            //Visible:Pending
            //Hide:Accepted,On the way,Delivered
            ordersViewHolder.btn_cancel_order.setVisibility(View.GONE);
            Glide.with(context).load(R.drawable.calceled).into(ordersViewHolder.img_order_status);
            ordersViewHolder.confirm_imageS.setVisibility(View.VISIBLE);
            ordersViewHolder.otw_imageS.setVisibility(View.VISIBLE);
            ordersViewHolder.delivered_imageS.setVisibility(View.VISIBLE);
            ordersViewHolder.ivProcessingDoneTrack.setVisibility(View.GONE);
            ordersViewHolder.ivOTWDoneTrack.setVisibility(View.GONE);
            ordersViewHolder.ivDeliveredDoneTrack.setVisibility(View.GONE);
        } else if (newGetOrderModel.getOrderStatus().equals("24")) {
            Glide.with(context).load(R.drawable.awaiting_payment).into(ordersViewHolder.img_order_status);
            ordersViewHolder.btnRetryOrder.setVisibility(View.VISIBLE);
        } else if (newGetOrderModel.getOrderStatus().equals("12")) {
            Glide.with(context).load(R.drawable.awaiting_payment).into(ordersViewHolder.img_order_status);
            ordersViewHolder.btnRetryPayment.setVisibility(View.VISIBLE);
        } else {
            //Visible:Pending
            //Hide:Accepted,On the way,Delivered
            ordersViewHolder.btn_cancel_order.setVisibility(View.GONE);
            ordersViewHolder.confirm_imageS.setVisibility(View.VISIBLE);
            ordersViewHolder.otw_imageS.setVisibility(View.VISIBLE);
            ordersViewHolder.delivered_imageS.setVisibility(View.VISIBLE);
            ordersViewHolder.ivProcessingDoneTrack.setVisibility(View.GONE);
            ordersViewHolder.ivOTWDoneTrack.setVisibility(View.GONE);
            ordersViewHolder.ivDeliveredDoneTrack.setVisibility(View.GONE);
        }

        String isPaid = "";

        if (newGetOrderModel.getIsPaymentSuccessful().equals("true")) {
            isPaid = "PAID";
        } else if (newGetOrderModel.getIsPaymentSuccessful().equals("false")) {
            isPaid = "UNPAID";
        }


        if (newGetOrderModel.getOrderTransactionType() != null) {
            if (newGetOrderModel.getOrderTransactionType().equals("COD")) {
                ordersViewHolder.txt_order_payStatus.setText(isPaid + " -(COD)");
            } else if (newGetOrderModel.getOrderTransactionType().equals("CC")) {
                ordersViewHolder.txt_order_payStatus.setText(isPaid + " -(CC)");
            } else if (newGetOrderModel.getOrderTransactionType().equals("C")) {
                ordersViewHolder.txt_order_payStatus.setText("CREDIT -(C)");
            } else if (newGetOrderModel.getOrderTransactionType().equals("BT")) {
                ordersViewHolder.txt_order_payStatus.setText(isPaid + " -(BT)");
            } else {
                ordersViewHolder.txt_order_payStatus.setText("UNPAID");
            }
        } else {
            ordersViewHolder.txt_order_payStatus.setText("UNPAID");
        }

        /*Button Repeat Order*/
            ordersViewHolder.btn_repeat_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repeatOrderSweetAlertDialog(newSuborderArrayList, "Are you sure, you want repeat this order?");

            }
        });

            if(session_management.getBookOrder().equals("1")
                    &&position==0
                    &&(newGetOrderModel.getOrderStatus().equals("24")||newGetOrderModel.getOrderStatus().equals("12"))){
                ordersViewHolder.ll_order_status.setVisibility(View.VISIBLE);
                session_management.setBookOrder("");
            }

        /*Main Card Clikc*/
            ordersViewHolder.cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //tempPos=position;
                //notifyDataSetChanged();
                /*if(ordersViewHolder.tvShowLess.getVisibility()==View.GONE) {
                    Log.e("click", ordersViewHolder.getAdapterPosition() + "Item CLick");
                    ordersViewHolder.ll_order_status.setVisibility(View.VISIBLE);
                    ordersViewHolder.tvShowMore.setVisibility(View.VISIBLE);
                    ordersViewHolder.llStar.setVisibility(View.VISIBLE);
                    //holder.llRating.setVisibility(View.VISIBLE);
                    hideStar(newGetOrderModel.getOrderStatus(), ordersViewHolder, newGetOrderModel.getOrderResponse());

                }*/
                ordersViewHolder.ll_order_status.setVisibility(View.VISIBLE);
                ordersViewHolder.tvShowMore.setVisibility(View.VISIBLE);
                ordersViewHolder.cvMain.setEnabled(false);
                if(newGetOrderModel.getOrderStatus().equals("24")||newGetOrderModel.getOrderStatus().equals("12")){
                    ordersViewHolder.llStar.setVisibility(View.GONE);
                    ordersViewHolder.ll_status_track.setVisibility(View.GONE);
                    //ordersViewHolder.llRating.setVisibility(View.GONE);
                    if(newGetOrderModel.getOrderStatus().equals("24")){
                        ordersViewHolder.btnRetryOrder.setVisibility(View.VISIBLE);
                    }
                    if(newGetOrderModel.getOrderStatus().equals("12")){
                        ordersViewHolder.btnRetryPayment.setVisibility(View.VISIBLE);
                    }
                }else{
                    ordersViewHolder.ll_status_track.setVisibility(View.VISIBLE);
                    ordersViewHolder.llStar.setVisibility(View.VISIBLE);
                   // ordersViewHolder.llRating.setVisibility(View.VISIBLE);
                    ordersViewHolder.btnRetryOrder.setVisibility(View.GONE);
                    ordersViewHolder.btnRetryPayment.setVisibility(View.GONE);
                }


            }
        });

        /*Show More Click*/
            ordersViewHolder.tvShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordersViewHolder.tvShowMore.setVisibility(View.GONE);
                ordersViewHolder.llStar.setVisibility(View.GONE);
                ordersViewHolder.cvOrderDetails.setVisibility(View.VISIBLE);
                ordersViewHolder.tvShowLess.setVisibility(View.VISIBLE);
            }
        });

        /*Show Less*/
            ordersViewHolder.tvShowLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ordersViewHolder.ll_order_status.setVisibility(View.GONE);
                //ordersViewHolder.cvOrderDetails.setVisibility(View.GONE);
                //ordersViewHolder.tvShowLess.setVisibility(View.GONE);
               // ordersViewHolder.llStar.setVisibility(View.GONE);
                reset1(ordersViewHolder);
                //holder.llRating.setVisibility(View.GONE);
                rv_order.scrollToPosition(tempPos);
            }
        });

        /*Button Retry  Order*/
            ordersViewHolder.btnRetryOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onRetryOrderClickListener.onRetryOrderClick(holder.getAdapterPosition(), newGetOrderModelArrayList);
            }
        });

        /*Button Retry Paymnet*/
            ordersViewHolder.btnRetryPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onRetryPaymentClickListener.onRetryPaymentClick(holder.getAdapterPosition(), newGetOrderModelArrayList);
                showDialogForRetryPayment(newSuborderArrayList);
            }
        });



        /*Set Rating*/
        if (!newGetOrderModel.getRating().equals("0")) {
            ordersViewHolder.tvRating.setText(newGetOrderModel.getRating() + ".0");
            // holder.llRating.setVisibility(View.VISIBLE);
        } else {
            ordersViewHolder.tvRating.setText("");
            // holder.llRating.setVisibility(View.VISIBLE);
        }



        switch (newGetOrderModel.getRating()) {
            case "1":
                ordersViewHolder.ivStar1_1.setImageResource(R.drawable.star_1);
                ordersViewHolder.ivStar2_1.setImageResource(R.drawable.star_1);
                ordersViewHolder.ivRating.setImageResource(R.drawable.star_1);
                break;
            case "2":
                ordersViewHolder.ivStar1_2.setImageResource(R.drawable.star_2);
                ordersViewHolder.ivStar2_2.setImageResource(R.drawable.star_2);
                ordersViewHolder.ivRating.setImageResource(R.drawable.star_2);
                break;
            case "3":
                ordersViewHolder.ivStar1_3.setImageResource(R.drawable.star_3);
                ordersViewHolder.ivStar2_3.setImageResource(R.drawable.star_3);
                ordersViewHolder.ivRating.setImageResource(R.drawable.star_3);
                break;
            case "4":
                ordersViewHolder.ivStar1_4.setImageResource(R.drawable.star_4);
                ordersViewHolder.ivStar2_4.setImageResource(R.drawable.star_4);
                ordersViewHolder.ivRating.setImageResource(R.drawable.star_4);
                break;
            case "5":
                ordersViewHolder.ivStar1_5.setImageResource(R.drawable.star_5_new);
                ordersViewHolder.ivStar2_5.setImageResource(R.drawable.star_5_new);
                ordersViewHolder.ivRating.setImageResource(R.drawable.star_5_new);
                break;
            default:
                ordersViewHolder.ivStar1_1.setImageResource(R.drawable.star_1_grey);
                ordersViewHolder.ivStar1_2.setImageResource(R.drawable.star_2_grey);
                ordersViewHolder.ivStar1_3.setImageResource(R.drawable.star_3_grey);
                ordersViewHolder.ivStar1_4.setImageResource(R.drawable.star_4_grey);
                ordersViewHolder.ivStar1_5.setImageResource(R.drawable.star_5_grey);
                ordersViewHolder.ivStar2_1.setImageResource(R.drawable.star_1_grey);
                ordersViewHolder.ivStar2_2.setImageResource(R.drawable.star_2_grey);
                ordersViewHolder.ivStar2_3.setImageResource(R.drawable.star_3_grey);
                ordersViewHolder.ivStar2_4.setImageResource(R.drawable.star_4_grey);
                ordersViewHolder.ivStar2_5.setImageResource(R.drawable.star_5_grey);
                // holder.ivRating.setImageResource(R.drawable.star_5_grey);
                break;
        }

       /* if(newGetOrderModel.getDeliveryMan()==null && !newGetOrderModel.getOrderStatus().equals("9")){
            holder.btn_cancel_order.setVisibility(View.VISIBLE);
        }
        else {
            holder.btn_cancel_order.setVisibility(View.GONE);
        }*/


        /*Delivery boy details*/
            if (newGetOrderModel.getDeliveryManDetails() != null || !newGetOrderModel.getDeliveryManDetails().toString().equals("")) {
            if (newGetOrderModel.getDeliveryManDetails().getUserName() == null
                    && newGetOrderModel.getDeliveryManDetails().getMobile() == null
                    && newGetOrderModel.getDeliveryManDetails().getLatitude() == null
                    && newGetOrderModel.getDeliveryManDetails().getLatitude() == null) {
                ordersViewHolder.llDeliveryBoyDetails.setVisibility(View.VISIBLE);
                ordersViewHolder.tvAssignDeliveryMenMessage.setVisibility(View.VISIBLE);
                ordersViewHolder.tvAssignDeliveryMenMessage.setText(context.getString(R.string.assignDeliveryManMsg));
            } else {
            ordersViewHolder.llDeliveryBoyDetails.setVisibility(View.VISIBLE);
            ordersViewHolder.tvAssignDeliveryMenMessage.setVisibility(View.GONE);
            if (newGetOrderModel.getDeliveryManDetails().getUserName() != null) {
                ordersViewHolder.llDeliveryBoyName.setVisibility(View.VISIBLE);
                ordersViewHolder.tvDeliveryBoyName.setText(newGetOrderModel.getDeliveryManDetails().getUserName());
            } else {
                ordersViewHolder.llDeliveryBoyName.setVisibility(View.GONE);
            }

            if (newGetOrderModel.getDeliveryManDetails().getMobile() != null) {
                ordersViewHolder.llDeliveryBoyContact.setVisibility(View.VISIBLE);
                ordersViewHolder.tvDeliveryBoyContact.setText(newGetOrderModel.getDeliveryManDetails().getMobile());
            } else {
                ordersViewHolder.llDeliveryBoyContact.setVisibility(View.GONE);
            }

            /*Show delivery feedback if order is delivered*/
            if (newGetOrderModel.getOrderStatus().equals("7")) {
                ordersViewHolder.llDeliveryFeedback.setVisibility(View.VISIBLE);
                ordersViewHolder.llDeliveryFeedback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDeliveryFeedbackDialog(newGetOrderModel.getOrderID(), newGetOrderModel.getDeliveryManDetails().getId(), ordersViewHolder.ivDeliveryFeedback);
                    }
                });
            } else {
                ordersViewHolder.llDeliveryFeedback.setVisibility(View.GONE);

            }


            if (newGetOrderModel.getDeliveryManDetails().getLatitude() != null && newGetOrderModel.getDeliveryManDetails().getLatitude() != null) {
                ordersViewHolder.llDeliveryBoyCurrentLocation.setVisibility(View.GONE);
            } else {
                ordersViewHolder.llDeliveryBoyCurrentLocation.setVisibility(View.GONE);
            }
        }


        } else {
            ordersViewHolder.llDeliveryBoyName.setVisibility(View.GONE);
        }



        /*Cancel Order Button*/
            ordersViewHolder.btn_cancel_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
                sweetAlertDialog.show();
                sweetAlertDialog.setTitle("Do you want to cancel this order?");
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        cancelOrder(newGetOrderModel.getOrderID());
                    }
                });

                sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });

            }
        });


        //  holder.recyclerView.requestFocus(View.FOCUS_UP);
        /*if (tempPos==position)
        {
            holder.recyclerView.requestFocus(View.FOCUS_UP);
            holder.ll_main_card.setVisibility(View.GONE);
            holder.recyclerView.setVisibility(View.VISIBLE);
            holder.img_order_up_arrow.setVisibility(View.VISIBLE);
            holder.ll_order_detail.setVisibility(View.VISIBLE);
            holder.btn_repeat_order1.setVisibility(View.VISIBLE);
            rv_order.getLayoutManager().scrollToPosition(position);
        }
        else {
           // holder.recyclerView.requestFocus();
            holder.ll_main_card.setVisibility(View.VISIBLE);
            holder.recyclerView.setVisibility(View.GONE);
            holder.img_order_up_arrow.setVisibility(View.GONE);
            holder.ll_order_detail.setVisibility(View.GONE);
            holder.btn_repeat_order1.setVisibility(View.GONE);
            holder.btn_cancel_order.setVisibility(View.GONE);
        }*/

        /*holder.img_order_up_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.ll_main_card.setVisibility(View.VISIBLE);
                holder.recyclerView.setVisibility(View.GONE);
                holder.img_order_up_arrow.setVisibility(View.GONE);
                holder.ll_order_detail.setVisibility(View.GONE);
                holder.btn_repeat_order1.setVisibility(View.GONE);
                holder.btn_cancel_order.setVisibility(View.GONE);

                rv_order.scrollToPosition(tempPos);
               // nestedScrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });*/


        /*Rating and Review click*/
        ordersViewHolder.ivStar1_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFeedbackDialog(newGetOrderModel.getOrderID(), "1", ordersViewHolder);
            }
        });
            ordersViewHolder.ivStar1_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFeedbackDialog(newGetOrderModel.getOrderID(), "2", ordersViewHolder);
            }
        });
            ordersViewHolder.ivStar1_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFeedbackDialog(newGetOrderModel.getOrderID(), "3", ordersViewHolder);
            }
        });
            ordersViewHolder.ivStar1_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFeedbackDialog(newGetOrderModel.getOrderID(), "4", ordersViewHolder);
            }
        });

        ordersViewHolder.ivStar1_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFeedbackDialog(newGetOrderModel.getOrderID(), "5", ordersViewHolder);
            }
        });

        ordersViewHolder.ivStar2_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFeedbackDialog(newGetOrderModel.getOrderID(), "1", ordersViewHolder);
            }
        });

        ordersViewHolder.ivStar2_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFeedbackDialog(newGetOrderModel.getOrderID(), "2", ordersViewHolder);
            }
        });
            ordersViewHolder.ivStar2_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFeedbackDialog(newGetOrderModel.getOrderID(), "3", ordersViewHolder);
            }
        });
            ordersViewHolder.ivStar2_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFeedbackDialog(newGetOrderModel.getOrderID(), "4", ordersViewHolder);
            }
        });
            ordersViewHolder.ivStar2_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFeedbackDialog(newGetOrderModel.getOrderID(), "5", ordersViewHolder);
            }
        });

    } else {
            LoadingViewHolder loadingViewHolder =   (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setVisibility(View.VISIBLE);
        }

    }

    private void reset1(OrdersViewHolder holder) {
        // Always null-check before accessing
        if (holder.ll_order_status != null) {
            holder.ll_order_status.setVisibility(View.GONE);
        }
        if (holder.cvOrderDetails != null) {
            holder.cvOrderDetails.setVisibility(View.GONE);
        }
        if (holder.llStar != null) {
            holder.llStar.setVisibility(View.GONE);
        }
        if (holder.llRating != null) {
            holder.llRating.setVisibility(View.GONE);
        }
        if (holder.tvShowMore != null) {
            holder.tvShowMore.setVisibility(View.GONE);
        }
        if (holder.tvShowLess != null) {
            holder.tvShowLess.setVisibility(View.GONE);
        }
        if (holder.btnRetryOrder != null) {
            holder.btnRetryOrder.setVisibility(View.GONE);
        }
        if (holder.btnRetryPayment != null) {
            holder.btnRetryPayment.setVisibility(View.GONE);
        }
        if (holder.ll_status_track != null) {
            holder.ll_status_track.setVisibility(View.GONE);
        }
        if (holder.llDeliveryBoyDetails != null) {
            holder.llDeliveryBoyDetails.setVisibility(View.GONE);
        }
        if (holder.llDeliveryFeedback != null) {
            holder.llDeliveryFeedback.setVisibility(View.GONE);
        }
    }


    private void hideStar(String orderStatus, OrdersViewHolder holder, CreateOrderResponseDto orderResponse){
        if(orderStatus.equals("24")){
            holder.btnRetryOrder.setVisibility(View.VISIBLE);
            holder.btnRetryPayment.setVisibility(View.GONE);
            holder.llStar.setVisibility(View.GONE);
            holder.llStar2.setVisibility(View.GONE);
            holder.btn_cancel_order.setVisibility(View.GONE);
            holder.btn_repeat_order.setVisibility(View.GONE);
        }else if(orderStatus.equals("12")){
            holder.btnRetryPayment.setVisibility(View.VISIBLE);
            holder.btnRetryOrder.setVisibility(View.GONE);
            holder.llStar.setVisibility(View.GONE);
            holder.llStar2.setVisibility(View.GONE);
            holder.btn_cancel_order.setVisibility(View.GONE);
            holder.btn_repeat_order.setVisibility(View.GONE);
        }else{
            holder.llStar.setVisibility(View.VISIBLE);
            holder.btnRetryPayment.setVisibility(View.GONE);
            holder.btnRetryOrder.setVisibility(View.GONE);
        }
    }

    private void showDeliveryFeedbackDialog(String orderId,String deliveryMenId,ImageView imageView){

        Dialog deliveryFeedbackBottomSheetDialog=new Dialog(context);
        deliveryFeedbackBottomSheetDialog.setContentView(R.layout.item_layout_delivery_feedback);
        int width = (int)(context.getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(context.getResources().getDisplayMetrics().heightPixels*0.90);

        MaterialButton btnBack=deliveryFeedbackBottomSheetDialog.findViewById(R.id.btnBack);
        MaterialButton btnSubmit=deliveryFeedbackBottomSheetDialog.findViewById(R.id.btnSubmit);
        ImageView close=deliveryFeedbackBottomSheetDialog.findViewById(R.id.close);
        ImageView ivEmoji1=deliveryFeedbackBottomSheetDialog.findViewById(R.id.ivEmoji1);
        ImageView ivEmoji2=deliveryFeedbackBottomSheetDialog.findViewById(R.id.ivEmoji2);
        ImageView ivEmoji3=deliveryFeedbackBottomSheetDialog.findViewById(R.id.ivEmoji3);
        ImageView ivEmoji4=deliveryFeedbackBottomSheetDialog.findViewById(R.id.ivEmoji4);
        ImageView ivEmoji5=deliveryFeedbackBottomSheetDialog.findViewById(R.id.ivEmoji5);

        deliveryFeedbackBottomSheetDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,height);
        deliveryFeedbackBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        deliveryFeedbackBottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        deliveryFeedbackBottomSheetDialog.getWindow().getAttributes().windowAnimations =  R.style.DialogAnimation;
        deliveryFeedbackBottomSheetDialog.show();

        ivEmoji1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSubmit.setEnabled(true);
                deliveryRating="1";
                ivEmoji1.setColorFilter(ContextCompat.getColor(context, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
                ivEmoji2.setColorFilter(ContextCompat.getColor(context, R.color.emoji_default_color), android.graphics.PorterDuff.Mode.SRC_IN);
                ivEmoji3.setColorFilter(ContextCompat.getColor(context, R.color.emoji_default_color), android.graphics.PorterDuff.Mode.SRC_IN);
                ivEmoji4.setColorFilter(ContextCompat.getColor(context, R.color.emoji_default_color), android.graphics.PorterDuff.Mode.SRC_IN);
                ivEmoji5.setColorFilter(ContextCompat.getColor(context, R.color.emoji_default_color), android.graphics.PorterDuff.Mode.SRC_IN);
            }
        });
        ivEmoji2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSubmit.setEnabled(true);
                deliveryRating="2";
                ivEmoji1.setColorFilter(ContextCompat.getColor(context, R.color.emoji_default_color), android.graphics.PorterDuff.Mode.SRC_IN);
                ivEmoji2.setColorFilter(ContextCompat.getColor(context, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
                ivEmoji3.setColorFilter(ContextCompat.getColor(context, R.color.emoji_default_color), android.graphics.PorterDuff.Mode.SRC_IN);
                ivEmoji4.setColorFilter(ContextCompat.getColor(context, R.color.emoji_default_color), android.graphics.PorterDuff.Mode.SRC_IN);
                ivEmoji5.setColorFilter(ContextCompat.getColor(context, R.color.emoji_default_color), android.graphics.PorterDuff.Mode.SRC_IN);
            }
        });

        ivEmoji3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSubmit.setEnabled(true);
                deliveryRating="3";
                ivEmoji1.setColorFilter(ContextCompat.getColor(context, R.color.emoji_default_color), android.graphics.PorterDuff.Mode.SRC_IN);
                ivEmoji2.setColorFilter(ContextCompat.getColor(context, R.color.emoji_default_color), android.graphics.PorterDuff.Mode.SRC_IN);
                ivEmoji3.setColorFilter(ContextCompat.getColor(context, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
                ivEmoji4.setColorFilter(ContextCompat.getColor(context, R.color.emoji_default_color), android.graphics.PorterDuff.Mode.SRC_IN);
                ivEmoji5.setColorFilter(ContextCompat.getColor(context, R.color.emoji_default_color), android.graphics.PorterDuff.Mode.SRC_IN);
            }
        });
        ivEmoji4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSubmit.setEnabled(true);
                deliveryRating="4";
                ivEmoji1.setColorFilter(ContextCompat.getColor(context, R.color.emoji_default_color), android.graphics.PorterDuff.Mode.SRC_IN);
                ivEmoji2.setColorFilter(ContextCompat.getColor(context, R.color.emoji_default_color), android.graphics.PorterDuff.Mode.SRC_IN);
                ivEmoji3.setColorFilter(ContextCompat.getColor(context, R.color.emoji_default_color), android.graphics.PorterDuff.Mode.SRC_IN);
                ivEmoji4.setColorFilter(ContextCompat.getColor(context, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
                ivEmoji5.setColorFilter(ContextCompat.getColor(context, R.color.emoji_default_color), android.graphics.PorterDuff.Mode.SRC_IN);
            }
        });
        ivEmoji5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSubmit.setEnabled(true);
                deliveryRating="5";
                ivEmoji1.setColorFilter(ContextCompat.getColor(context, R.color.emoji_default_color), android.graphics.PorterDuff.Mode.SRC_IN);
                ivEmoji2.setColorFilter(ContextCompat.getColor(context, R.color.emoji_default_color), android.graphics.PorterDuff.Mode.SRC_IN);
                ivEmoji3.setColorFilter(ContextCompat.getColor(context, R.color.emoji_default_color), android.graphics.PorterDuff.Mode.SRC_IN);
                ivEmoji4.setColorFilter(ContextCompat.getColor(context, R.color.emoji_default_color), android.graphics.PorterDuff.Mode.SRC_IN);
                ivEmoji5.setColorFilter(ContextCompat.getColor(context, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deliveryFeedbackBottomSheetDialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDeliveryRatingReview(orderId,deliveryRating,deliveryMenId,imageView,deliveryFeedbackBottomSheetDialog);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deliveryFeedbackBottomSheetDialog.dismiss();

            }
        });
    }


    private void addDeliveryRatingReview(String orderId,String rating,String DeliveryMenId,ImageView imageView,Dialog deliveryFeedbackBottomSheetDialog){
        progressDialog.show();
        ServiceGenrator.getApiInterface().addDeliveryRatingReview(session_management.getUserDetails().get(BaseURL.KEY_ID),orderId,rating,DeliveryMenId).enqueue(new Callback<ResponseAddDeliveryRatingReview>() {
            @Override
            public void onResponse(Call<ResponseAddDeliveryRatingReview> call, retrofit2.Response<ResponseAddDeliveryRatingReview> response) {
                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    deliveryFeedbackBottomSheetDialog.dismiss();
                    if(response.body().isStatus()){
                        Toast.makeText(context,response.body().getMessage(), Toast.LENGTH_LONG).show();
                        setEmojiFromRating(rating,imageView);
                    }else{
                        progressDialog.dismiss();
                        deliveryFeedbackBottomSheetDialog.dismiss();
                        Toast.makeText(context,response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }else{
                    progressDialog.dismiss();
                    deliveryFeedbackBottomSheetDialog.dismiss();
                    Toast.makeText(context,response.message(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseAddDeliveryRatingReview> call, Throwable t) {
                progressDialog.dismiss();
                deliveryFeedbackBottomSheetDialog.dismiss();
                Toast.makeText(context,t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setEmojiFromRating(String rating,ImageView imageView){
        switch (rating){
            case "1":
                imageView.setImageResource(R.drawable.ic_emoji_1);
                break;
            case "2":
                imageView.setImageResource(R.drawable.ic_emoji_2);
                break;
            case "3":
                imageView.setImageResource(R.drawable.ic_emoji_3);
                break;
            case "4":
                imageView.setImageResource(R.drawable.ic_emoji_4);
                break;
            case "5":
                imageView.setImageResource(R.drawable.ic_emoji_5);
                break;

        }
    }


    private void showFeedbackDialog(String orderId,String rating,OrdersViewHolder holder){
        Dialog bottomSheetDialog=new Dialog(context);
        bottomSheetDialog.setContentView(R.layout.item_layout_feedback);
        int width = (int)(context.getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(context.getResources().getDisplayMetrics().heightPixels*0.90);

        MaterialButton btnBack=bottomSheetDialog.findViewById(R.id.btnBack);
        MaterialButton btnSubmit=bottomSheetDialog.findViewById(R.id.btnSubmit);
        ImageView close=bottomSheetDialog.findViewById(R.id.close);
        EditText edtFeedback=bottomSheetDialog.findViewById(R.id.edtFeedback);


        bottomSheetDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,height);
        bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        bottomSheetDialog.getWindow().getAttributes().windowAnimations =  R.style.DialogAnimation;
        bottomSheetDialog.show();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRatingReview(orderId,rating,edtFeedback.getText().toString(),bottomSheetDialog,holder);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.dismiss();

            }
        });
    }

    private void addRatingReview(String orderId,String rating,String review,Dialog bottomSheetDialog,OrdersViewHolder holder){
        progressDialog.show();
        ServiceGenrator.getApiInterface().addRatingReview(session_management.getUserDetails().get(BaseURL.KEY_ID),orderId,rating,review).enqueue(new Callback<ResponseAddRatingReview>() {
            @Override
            public void onResponse(Call<ResponseAddRatingReview> call, retrofit2.Response<ResponseAddRatingReview> response) {
                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    bottomSheetDialog.dismiss();
                    if(response.body().isStatus()){
                        Toast.makeText(context,response.body().getMessage(),Toast.LENGTH_LONG).show();
                        setRating(rating,holder);
                    }else{
                        Toast.makeText(context,response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }else{
                    progressDialog.dismiss();
                    bottomSheetDialog.dismiss();
                    Toast.makeText(context,response.message(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseAddRatingReview> call, Throwable t) {
                progressDialog.dismiss();
                bottomSheetDialog.dismiss();
                Toast.makeText(context,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setRating(String rating,OrdersViewHolder holder){
        switch (rating){
            case "1":
                holder.ivStar1_1.setImageResource(R.drawable.star_1);
                holder.ivStar2_1.setImageResource(R.drawable.star_1);
                holder.ivRating.setImageResource(R.drawable.star_1);
                break;
            case "2":
                holder.ivStar1_2.setImageResource(R.drawable.star_2);
                holder.ivStar2_2.setImageResource(R.drawable.star_2);
                holder.ivRating.setImageResource(R.drawable.star_2);
                break;
            case "3":
                holder.ivStar1_3.setImageResource(R.drawable.star_3);
                holder.ivStar2_3.setImageResource(R.drawable.star_3);
                holder.ivRating.setImageResource(R.drawable.star_3);
                break;
            case "4":
                holder.ivStar1_4.setImageResource(R.drawable.star_4);
                holder.ivStar2_4.setImageResource(R.drawable.star_4);
                holder.ivRating.setImageResource(R.drawable.star_4);
                break;
            case "5":
                holder.ivStar1_5.setImageResource(R.drawable.star_5_new);
                holder.ivStar2_5.setImageResource(R.drawable.star_5_new);
                holder.ivRating.setImageResource(R.drawable.star_5_new);
                break;
            default:
                holder.ivStar1_1.setImageResource(R.drawable.star_1_grey);
                holder.ivStar1_2.setImageResource(R.drawable.star_2_grey);
                holder.ivStar1_3.setImageResource(R.drawable.star_3_grey);
                holder.ivStar1_4.setImageResource(R.drawable.star_4_grey);
                holder.ivStar1_5.setImageResource(R.drawable.star_5_grey);
                holder.ivStar2_1.setImageResource(R.drawable.star_1_grey);
                holder.ivStar2_2.setImageResource(R.drawable.star_2_grey);
                holder.ivStar2_3.setImageResource(R.drawable.star_3_grey);
                holder.ivStar2_4.setImageResource(R.drawable.star_4_grey);
                holder.ivStar2_5.setImageResource(R.drawable.star_5_grey);
                // holder.ivRating.setImageResource(R.drawable.star_5_grey);
                break;
        }
    }

    private void cancelOrder(String orderID){
        progressDialog.show();
        String tag_json_obj = "json_cart_list_req";
        if(session_management.getUserDetails().get(BaseURL.KEY_ID)!=null) {
            custID = session_management.getUserDetails().get(BaseURL.KEY_ID);
        }
        Map<String, String> params = new HashMap<String, String>();
        if(custID!=null) {
            params.put("userID", custID);
        }
        params.put("orderID", orderID);
        // params.put("SupplierID",);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ApiBaseURL.cancelOrder, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("CheckApiCancel order", response.toString());
                progressDialog.dismiss();
                try {
                    boolean status = response.getBoolean("status");

                    if (status) {
                        orderCancelListner.onCancelClick();
                        Toast.makeText(context,response.getString("message").toString(),Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(context,response.getString("message").toString(),Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                VolleyLog.d("", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                }
            }
        });

        // Adding request to request queue
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    private void clearCart(ArrayList<NewSuborder> suborderArrayList,String msg){
        String tag_json_obj = "json_cart_list_req";
        if(session_management.getUserDetails().get(BaseURL.KEY_ID)!=null) {
            custID = session_management.getUserDetails().get(BaseURL.KEY_ID);
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("custId", custID);
        boolean status;

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ApiBaseURL.clearCart, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("clearCart", response.toString());
                boolean status;
               /* dbcart.clearCart();
                addToCart(suborderArrayList,msg); */
                try {
                  status = response.getBoolean("status");
                    if (status) {
                        dbcart.clearCart();
                       // addToCart(suborderArrayList,msg);
                    }
                    else {
                            Toast.makeText(context,response.getString("message"),Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                VolleyLog.d("", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                }
            }
        });

        // Adding request to request queue
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    private void getCartProducts(ArrayList<NewSuborder> suborderArrayList)
    {
        ArrayList<HashMap<String, String>> map = dbcart.getCartAll();
        if(map.size()>0){
            clearCart(suborderArrayList,"Clear Cart and add Items");
        }
        else {
          //  addToCart(suborderArrayList,"Add Items to cart");
        }
    }


    private void reset(OrdersViewHolder ordersViewHolder){
        ordersViewHolder.ll_order_status.setVisibility(View.GONE);
        ordersViewHolder.cvOrderDetails.setVisibility(View.GONE);
        ordersViewHolder.llStar.setVisibility(View.GONE);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
   /* public void addToCart(ArrayList<NewSuborder> suborderArrayList, String msg){
        String tag_json_obj = "json_cart_list_req";

        SweetAlertDialog sweetAlertDialog=new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
        sweetAlertDialog.setTitle(msg);
        sweetAlertDialog.show();
        Button btn = (Button) sweetAlertDialog.findViewById(R.id.confirm_button);
        btn.setText("Ok");

        LinearLayout.LayoutParams layoutParams  = new LinearLayout.LayoutParams(300, 130);
        layoutParams.setMargins(10,0,10,0);
        btn.setLayoutParams(layoutParams);
        btn.setBackground(context.getResources().getDrawable(R.drawable.custom_dialog_button));
        btn.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.buttons)));
        btn.setGravity(Gravity.CENTER);

        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                HashMap<String, String> params = new HashMap<String, String>();
                if(session_management.getUserDetails().get(BaseURL.KEY_ID)!=null) {
                     custID = session_management.getUserDetails().get(BaseURL.KEY_ID);
                }
                params.put("CustId",custID);

                for(int i=0;i<suborderArrayList.size();i++){
                    NewSuborder newSuborder=suborderArrayList.get(i);

                    ArrayList<NewOrderItem> newOrderItems=suborderArrayList.get(i).getOrderItems();
                    params.put("supplierID",newSuborder.getSupplierID());
                    for(int k=0;k<newOrderItems.size();k++){
                        NewOrderItem newOrderItem=new NewOrderItem();
                        params.put("varient_id",newOrderItems.get(k).getUnitID());
                        params.put("Price",newOrderItems.get(k).getPerUnitPrice());
                        params.put("Quantity",newOrderItems.get(k).getQuantity());
                        params.put("product_image",newOrderItems.get(k).getImage());
                        params.put("product_name",newOrderItems.get(k).getItemName());
                        params.put("unit_value",newOrderItems.get(k).getUom());
                        params.put("price",newOrderItems.get(k).getItemSellingprice());
                        params.put("category_id", newOrderItems.get(k).getItemId());
                        params.put("title", newOrderItems.get(k).getItemName());
                        params.put("status", "");
                        params.put("stock", "");
                        params.put("increament", "0");
                        params.put("vatRate", newOrderItems.get(k).getVatRate());
                        params.put("product_description", newOrderItems.get(k).getItemName());

                        try {
                            qty=Integer.parseInt(newOrderItems.get(k).getQuantity());
                        }catch (NumberFormatException e){

                        }

                        Log.e("Param", "onClick: "+newOrderItems.get(k).getItemId());
                        int qtyd = Integer.parseInt(dbcart.getInCartItemQtys(newOrderItems.get(k).getItemId()));
                        if (qtyd > 0)
                        {
                            qty=qty+qtyd;
                        }

                        dbcart.setCart(params, qty);
                        Map<String, String> para = new HashMap<String, String>();
                        para.put("CustId", custID);
                        para.put("ItemId",newOrderItems.get(k).getItemId());
                        para.put("Price",newOrderItems.get(k).getItemSellingprice());
                        para.put("Quantity",""+qty);
                        para.put("SupplierID",suborderArrayList.get(i).getSupplierID());
                        para.put("unitID",newOrderItems.get(k).getUnitID());
                        updateSharedPref();


                        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST, ApiBaseURL.Cart, para, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("AddtocartRes", response.toString());
                                try {
                                    boolean status = response.getBoolean("status");
                                    if (status) {

                                         // dbcart.setCart(params, qty);
                                           // updateSharedPref();
                                    }
                                    Toast.makeText(context, ""+response.getString("message"), Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                VolleyLog.d("", "Error: " + error.getMessage());
                                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                }
                            }
                        });

                        // Adding request to request queue
                        jsonObjReq.setRetryPolicy(new RetryPolicy() {
                            @Override
                            public int getCurrentTimeout() {
                                return 0;
                            }

                            @Override
                            public int getCurrentRetryCount() {
                                return 0;
                            }

                            @Override
                            public void retry(VolleyError error) throws VolleyError {

                            }
                        });
                        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
                        Log.e("cartRequest",jsonObjReq.toString());
                    }
                }
            }
        });


    }*/

    private void showDialogForRetryPayment(ArrayList<NewSuborder> suborderArrayList) {
        String tag_json_obj = "json_cart_list_req";
        Dialog dialog=new Dialog(context);
        dialog.setContentView(R.layout.dialog_retry_payment);

        int width = (int)(context.getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(context.getResources().getDisplayMetrics().heightPixels*0.90);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,height);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations =  R.style.DialogAnimation;
        dialog.show();


        ImageView close = dialog.findViewById(R.id.close);
        Button btn_orderConfirm_no = dialog.findViewById(R.id.btn_orderConfirm_no);
        Button btn_orderConfirm_yes = dialog.findViewById(R.id.btn_orderConfirm_yes);

        btn_orderConfirm_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                HashMap<String, String> params = new HashMap<String, String>();
                if(session_management.getUserDetails().get(BaseURL.KEY_ID)!=null) {
                    custID = session_management.getUserDetails().get(BaseURL.KEY_ID);
                }
                params.put("CustId",custID);

                for(int i=0;i<suborderArrayList.size();i++){
                    NewSuborder newSuborder=suborderArrayList.get(i);

                    ArrayList<NewOrderItem> newOrderItems=suborderArrayList.get(i).getOrderItems();
                    params.put("supplierID",newSuborder.getSupplierID());

                    for(k=0;k<newOrderItems.size();k++){
                        NewOrderItem newOrderItem=new NewOrderItem();
                        params.put("varient_id",newOrderItems.get(k).getUnitID());
                        params.put("Price",newOrderItems.get(k).getPerUnitPrice());
                        params.put("Quantity",newOrderItems.get(k).getQuantity());
                        params.put("product_image",newOrderItems.get(k).getImage());
                        params.put("product_name",newOrderItems.get(k).getItemName());
                        params.put("unit_value",newOrderItems.get(k).getUom());
                        params.put("price",newOrderItems.get(k).getItemSellingprice());
                        params.put("category_id", newOrderItems.get(k).getItemId());
                        params.put("ItemId", newOrderItems.get(k).getItemId());
                        params.put("title", newOrderItems.get(k).getItemName());
                        params.put("status", "");
                        params.put("stock", newOrderItems.get(k).getStockingType());
                        params.put("increament", "0");
                        params.put("vatRate", newOrderItems.get(k).getVatRate());
                        params.put("product_description", newOrderItems.get(k).getItemName());

                        try {
                            qty=Integer.parseInt(newOrderItems.get(k).getQuantity());
                        }catch (NumberFormatException e){

                        }

                        Log.e("Param", "onClick: "+newOrderItems.get(k).getItemId());
                        int qtyd = Integer.parseInt(dbcart.getInCartItemQtys(newOrderItems.get(k).getItemId()));
                        if (qtyd > 0)
                        {
                            qty=qty+qtyd;
                        }

                        dbcart.setCart(params, qty);
                        updateSharedPref();

                        /*Map<String, String> para = new HashMap<String, String>();
                        para.put("CustId", custID);
                        para.put("ItemId",newOrderItems.get(k).getItemId());
                        para.put("Price",newOrderItems.get(k).getItemSellingprice());
                        para.put("Quantity",newOrderItems.get(k).getQuantity());
                        para.put("SupplierID",suborderArrayList.get(i).getSupplierID());
                        para.put("unitID",newOrderItems.get(k).getUnitID());


                        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST, ApiBaseURL.Cart, para, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("AddtocartRes", response.toString());
                                try {
                                    boolean status = response.getBoolean("status");
                                    if (status) {
                                        if(k==2){
                                            context.startActivity(new Intent(context, CartActivity.class));
                                        }
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                VolleyLog.d("", "Error: " + error.getMessage());
                                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                }
                            }
                        });

                        // Adding request to request queue
                        jsonObjReq.setRetryPolicy(new RetryPolicy() {
                            @Override
                            public int getCurrentTimeout() {
                                return 0;
                            }

                            @Override
                            public int getCurrentRetryCount() {
                                return 0;
                            }

                            @Override
                            public void retry(VolleyError error) throws VolleyError {

                            }
                        });
                        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
                        Log.e("cartRequest",jsonObjReq.toString());*/
                    }

                    addToCart(newOrderItems,suborderArrayList.get(i).getSupplierID());
                }


            }
        });

        btn_orderConfirm_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
    }

    public void addToCart(ArrayList<NewOrderItem> newOrderItems,String supplierId)
    {
        String tag_json_obj = "json_cart_add_req";
        String custID= session_management.getUserDetails().get(BaseURL.KEY_ID);
        Map<String, String> params = new HashMap<String, String>();
        params.put("CustID", custID);
        params.put("ItemId",newOrderItems.get(uploadPosition).getItemId());
        params.put("Price", newOrderItems.get(uploadPosition).getItemSellingprice());
        params.put("Quantity",newOrderItems.get(uploadPosition).getQuantity());
        params.put("SupplierID",supplierId);
        params.put("unitID",newOrderItems.get(uploadPosition).getUnitID());

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ApiBaseURL.Cart, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("CheckApiCart", response.toString());
                try {
                    boolean status = response.getBoolean("status");
                    if (status) {
                        int c=newOrderItems.size()-1;
                        if(c>uploadPosition)
                        {
                            uploadPosition++;
                            addToCart(newOrderItems,supplierId);
                        }
                        else
                        {
                            context.startActivity(new Intent(context, CartActivity.class));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                VolleyLog.d("", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                }
            }
        });

        // Adding request to request queue
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    public void repeatOrderSweetAlertDialog(ArrayList<NewSuborder> suborderArrayList, String msg){
        String tag_json_obj = "json_cart_list_req";
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(msg)
                .setCancelText("Cancel")
                .setConfirmText("Ok")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        HashMap<String, String> params = new HashMap<String, String>();
                        if(session_management.getUserDetails().get(BaseURL.KEY_ID)!=null) {
                             custID = session_management.getUserDetails().get(BaseURL.KEY_ID);
                        }
                        params.put("CustId",custID);

                        for(int i=0;i<suborderArrayList.size();i++){
                            NewSuborder newSuborder=suborderArrayList.get(i);

                            ArrayList<NewOrderItem> newOrderItems=suborderArrayList.get(i).getOrderItems();
                            params.put("supplierID",newSuborder.getSupplierID());
                            for(int k=0;k<newOrderItems.size();k++){
                                NewOrderItem newOrderItem=new NewOrderItem();
                                params.put("varient_id",newOrderItems.get(k).getUnitID());
                                params.put("Price",newOrderItems.get(k).getPerUnitPrice());
                                params.put("Quantity",newOrderItems.get(k).getQuantity());
                                params.put("product_image",newOrderItems.get(k).getImage());
                                params.put("product_name",newOrderItems.get(k).getItemName());
                                params.put("unit_value",newOrderItems.get(k).getUom());
                                params.put("price",newOrderItems.get(k).getItemSellingprice());
                                params.put("ItemId", newOrderItems.get(k).getItemId());
                                params.put("unitID", newOrderItems.get(k).getUnitID());
                                params.put("title", newOrderItems.get(k).getItemName());
                                params.put("status", "");
                                params.put("stock", newOrderItems.get(k).getStockingType());
                                params.put("increament", "0");
                                params.put("vatRate", newOrderItems.get(k).getVatRate());
                                params.put("product_description", newOrderItems.get(k).getItemName());

                                try {
                                    qty=Integer.parseInt(newOrderItems.get(k).getQuantity());
                                }catch (NumberFormatException e){

                                }

                                Log.e("Param", "onClick: "+newOrderItems.get(k).getItemId());
                                int qtyd = Integer.parseInt(dbcart.getInCartItemQtys(newOrderItems.get(k).getItemId()));
                                if (qtyd > 0)
                                {
                                    qty=qty+qtyd;
                                }
///
                                dbcart.setCart(params, qty);
                                Map<String, String> para = new HashMap<String, String>();
                                para.put("CustId", custID);
                                para.put("ItemId",newOrderItems.get(k).getItemId());
                                para.put("Price",newOrderItems.get(k).getItemSellingprice());
                                para.put("Quantity",""+qty);
                                para.put("SupplierID",suborderArrayList.get(i).getSupplierID());
                                para.put("unitID",newOrderItems.get(k).getUnitID());
                                Log.e("cartRequest",para.toString());

                                updateSharedPref();

                                CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                                        ApiBaseURL.Cart, para, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d("AddtocartParams", response.toString());
                                        try {
                                            boolean status = response.getBoolean("status");
                                            if (status) {
                                                sweetAlertDialog.dismiss();
                                                // dbcart.setCart(params, qty);
                                                // updateSharedPref();
                                            }
                                            Toast.makeText(context, ""+response.getString("message"), Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        error.printStackTrace();
                                        VolleyLog.d("", "Error: " + error.getMessage());
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                        }
                                    }
                                });

                                // Adding request to request queue
                                jsonObjReq.setRetryPolicy(new RetryPolicy() {
                                    @Override
                                    public int getCurrentTimeout() {
                                        return 0;
                                    }

                                    @Override
                                    public int getCurrentRetryCount() {
                                        return 0;
                                    }

                                    @Override
                                    public void retry(VolleyError error) throws VolleyError {

                                    }
                                });
                                AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
                                Log.e("cartRequest",jsonObjReq.toString());
                            }
                        }
                    }
                })
                .show();
    }

    void updateSharedPref()
    {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (viewNotifier!=null){
                    viewNotifier.onViewNotify();
                }
                SharedPreferences preferences = context.getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                preferences.edit().putInt("cardqnty", dbcart.getCartCount()).apply();
            }
        } catch (IndexOutOfBoundsException e) {
            Log.d("qwer", e.toString());
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    public void setList(ArrayList<NewGetOrderModel> newGetOrderModelsList) {
        this.newGetOrderModelArrayList.addAll(newGetOrderModelsList);
        Log.e("setList",newGetOrderModelArrayList.toString());
        notifyDataSetChanged();
    }

    public void removeLoadingFooter() {
        if (isLoadingAdded) {
            isLoadingAdded = false;
            int position = newGetOrderModelArrayList.size()- 1;
            NewGetOrderModel result  = getItem(position);
            if (result == null) {
                newGetOrderModelArrayList.remove(position);
                notifyItemRemoved(position);
            }
        }
    }

    private NewGetOrderModel getItem(int position) {
        return newGetOrderModelArrayList.get(position);
    }

    public void addLoadingFooter() {
        if (!isLoadingAdded) {
            isLoadingAdded = true;
            addItem(null);
        }
    }

    public void addItem(NewGetOrderModel newGetOrderModel) {
        newGetOrderModelArrayList.add(newGetOrderModel);
        notifyItemInserted(newGetOrderModelArrayList.size() - 1);
    }


    @Override
    public int getItemCount() {
        return newGetOrderModelArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
         if(newGetOrderModelArrayList.get(position)==null){
             return VIEW_TYPE_LOADING;
        }else{
             return VIEW_TYPE_ITEM;
         }
    }



    public interface OnRetryOrderClickListener{
        public void onRetryOrderClick(int position,ArrayList<NewGetOrderModel> newGetOrderModelArrayList);
    }

    public interface OnRetryPaymentClickListener{
        public void onRetryPaymentClick(int position,ArrayList<NewGetOrderModel> newGetOrderModelArrayList);
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar  progressBar;
       public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar  = itemView.findViewById(R.id.progressBar);
        }
    }

}
