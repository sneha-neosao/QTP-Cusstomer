package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import adapters.OrderItemAdapter;
import adapters.ViewHolders.SubOrdersHolder;
import ModelClass.NewOrderItem;
import ModelClass.NewSuborder;
import com.grocery.QTPmart.R;

import java.util.ArrayList;

public class OrderDetailAdapter1 extends RecyclerView.Adapter<SubOrdersHolder>
{
    Context context;
    ArrayList<NewSuborder> newSuborderArrayList;
    ArrayList<NewOrderItem> newOrderItemArrayList;
    NewSuborder newSuborder;
    NewOrderItem newOrderItem;
    String orderId;

    public OrderDetailAdapter1(Context context,
                               ArrayList<NewSuborder> newSuborderArrayList,String orderId )
    {
        this.context=context;
        this.newSuborderArrayList=newSuborderArrayList;
        this.orderId=orderId;
        // this.newOrderItemArrayList=newOrderItemArrayList;
    }

    @NonNull
    @Override
    public SubOrdersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_expanded_orderdetail_new, parent, false);
        return new SubOrdersHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubOrdersHolder holder, int position) {

        newSuborder=newSuborderArrayList.get(position);
        holder.txt_seller_price.setText("AED "+newSuborder.getGrandtotal());
        holder.txt_seller_name.setText("#"+newSuborder.getOrderRef());
        //holder.txt_order_time.setText(newSuborder.getOrderDate()+" "+newSuborder.getOrderTime());
        holder.txt_order_status.setText(newSuborder.getOrderStatus1());
        // holder.txt_order_subtotal.setText("AED "+newSuborder.getSubTotal());
        //  holder.txt_order_serviceCharge.setText("AED "+newSuborder.getShipping());
        // holder.txt_order_number.setText(newSuborder.getOrderRef());
        // holder.txt_order_date.setText(newSuborder.getOrderDate());
        // holder.txt_order_total.setText("AED "+newSuborder.getGrandtotal());
        //  holder.txt_order_paymode.setText(newSuborder.getOrderTransactionType());
        //  holder.txt_order_deliAddress.setText(newSuborder.getAddressLine1()+" "+newSuborder.getAddressLine2());


        holder.img_seller_logo.requestFocus();

        newOrderItemArrayList=newSuborder.getOrderItems();
        if(newOrderItemArrayList.size()>0){
            OrderItemAdapter orderItemAdapter=new OrderItemAdapter(context,newOrderItemArrayList,orderId);
            holder.rv_orderItem.setAdapter(orderItemAdapter);
        }

        if(newSuborder.getOrderStatus().equals("2"))
        {
            Glide.with(context).load(R.drawable.pending_cv).into(holder.img_seller_logo);
        }
        else if(newSuborder.getOrderStatus().equals("5") || newSuborder.getOrderStatus().equals("8") || newSuborder.getOrderStatus().equals("11"))
        {
            Glide.with(context).load(R.drawable.processing).into(holder.img_seller_logo);
            //Glide.with(context).load(R.drawable.delivered).into(holder.confirm_imageS);
            holder.confirm_imageS.setVisibility(View.VISIBLE);
            holder.ivProcessingDoneTrack.setVisibility(View.GONE);
        }
        else if(newSuborderArrayList.get(holder.getAdapterPosition()).getOrderStatus().equals("3"))
        {
//            Glide.with(context).load(R.drawable.delivered_accepted).into(holder.img_seller_logo);
//            //Glide.with(context).load(R.drawable.delivered).into(holder.confirm_imageS);
//            holder.confirm_imageS.setVisibility(View.GONE);
//            holder.ivProcessingDoneTrack.setVisibility(View.VISIBLE);
        }
        else if(newSuborder.getOrderStatus().equals("4"))
        {
            Glide.with(context).load(R.drawable.ready_for_pickup).into(holder.img_seller_logo);
            // Glide.with(context).load(R.drawable.delivered).into(holder.confirm_imageS);
            holder.confirm_imageS.setVisibility(View.GONE);
            holder.ivProcessingDoneTrack.setVisibility(View.VISIBLE);
        }
        else if(newSuborder.getOrderStatus().equals("6"))
        {
            Glide.with(context).load(R.drawable.ready_for_pickup).into(holder.img_seller_logo);
            //Glide.with(context).load(R.drawable.delivered).into(holder.confirm_imageS);
            holder.confirm_imageS.setVisibility(View.GONE);
            holder.ivProcessingDoneTrack.setVisibility(View.VISIBLE);
            //Glide.with(context).load(R.drawable.delivered).into(holder.otw_imageS);
            holder.otw_imageS.setVisibility(View.GONE);
            holder.ivOTWDoneTrack.setVisibility(View.VISIBLE);
        }
        else if(newSuborder.getOrderStatus().equals("7"))
        {
            Glide.with(context).load(R.drawable.delivered).into(holder.img_seller_logo);
            //Glide.with(context).load(R.drawable.delivered).into(holder.confirm_imageS);
            holder.confirm_imageS.setVisibility(View.GONE);
            holder.ivProcessingDoneTrack.setVisibility(View.VISIBLE);
            // Glide.with(context).load(R.drawable.delivered).into(holder.otw_imageS);
            holder.otw_imageS.setVisibility(View.GONE);
            holder.ivOTWDoneTrack.setVisibility(View.VISIBLE);
            //Glide.with(context).load(R.drawable.delivered).into(holder.delivered_imageS);
            holder.delivered_imageS.setVisibility(View.GONE);
            holder.ivDeliveredDoneTrack.setVisibility(View.VISIBLE);
        }
        else if(newSuborder.getOrderStatus().equals("9"))
        {
            holder.ll_status_track.setVisibility(View.GONE);
            Glide.with(context).load(R.drawable.calceled).into(holder.img_seller_logo);
        }
        else if(newSuborder.getOrderStatus().equals("1"))
        {
            Glide.with(context).load(R.drawable.order_placed).into(holder.img_seller_logo);
        }
        else if(newSuborder.getOrderStatus().equals("21"))
        {
            Glide.with(context).load(R.drawable.partially_accepted).into(holder.img_seller_logo);
        }else if(newSuborder.getOrderStatus().equals("12")){
            holder.ll_status_track.setVisibility(View.GONE);
            Glide.with(context).load(R.drawable.awaiting_payment).into(holder.img_seller_logo);
        }else if(newSuborder.getOrderStatus().equals("24")){
            holder.ll_status_track.setVisibility(View.GONE);
            Glide.with(context).load(R.drawable.awaiting_payment).into(holder.img_seller_logo);
        }else{
            holder.ll_status_track.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return newSuborderArrayList.size();
    }
}
