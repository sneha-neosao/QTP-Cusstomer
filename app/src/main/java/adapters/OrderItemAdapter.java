package adapters;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import adapters.ViewHolders.OrderItemHolder;
import Config.BaseURL;
import ModelClass.NewOrderItem;
import ModelClass.NewSuborder;
import com.grocery.QTPmart.R;
import network.Response.ResponseAddRatingReview;
import network.ServiceGenrator;
import util.Session_management;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static Config.ApiBaseURL.BASE_URL;
import static Config.ApiBaseURL.IMG_URL;

import retrofit2.Call;
import retrofit2.Callback;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemHolder>{


    Context context;
    ArrayList<NewSuborder> newSuborderArrayList;
    ArrayList<NewOrderItem> newOrderItemArrayList;
    NewSuborder newSuborder;
    NewOrderItem newOrderItem;
    ProgressDialog progressDialog;
    private Session_management session_management;
    public ImageView ivStar1_1,ivStar1_2,ivStar1_3,ivStar1_4,ivStar1_5;
    String rating="";
    String orderId;

    public OrderItemAdapter( Context context,ArrayList<NewOrderItem> newOrderItemArrayList,String orderId)
    {
        this.context=context;
        this.orderId=orderId;
        this.newOrderItemArrayList=newOrderItemArrayList;
        progressDialog=new ProgressDialog(context);
        session_management=new Session_management(context);
    }
    @NonNull
    @Override
    public OrderItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_order_product, parent, false);
        return new OrderItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemHolder holder, int position)
    {
        if(newOrderItemArrayList.size()>0)
        {
        NewOrderItem newOrderItem=newOrderItemArrayList.get(position);
        /*Set  Name*/
        holder.txt_product_name.setText(newOrderItem.getItemName());
        /*Set  Price*/
        double price= Double.parseDouble(newOrderItem.getPerUnitPrice());
        holder.txt_product_price.setText("AED "+String.format("%.2f", price ));
        /*Set Quantity*/
        holder.txt_product_quantity.setText("Qty : "+newOrderItem.getQuantity());
        /*Set UOM*/
        holder.txt_product_weight.setText(newOrderItem.getUom());
        /*Strike on cancel items*/
            if(newOrderItemArrayList.get(holder.getAdapterPosition()).getOrderItemStatus()!=null&&
                    newOrderItemArrayList.get(holder.getAdapterPosition()).getOrderItemStatus().equals("9")){
                holder.txt_product_name.setPaintFlags(holder.txt_product_name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.txt_product_price.setPaintFlags(holder.txt_product_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.txt_product_quantity.setPaintFlags(holder.txt_product_quantity.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.txt_product_weight.setPaintFlags(holder.txt_product_weight.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            /*Set Product Image*/
        if(newOrderItem.getImage()!=null){
            Glide.with(context)
                    .load(newOrderItem.getImage())
                    .placeholder(R.drawable.noimageavailable)
                    .into(holder.img_order_product);
        }
        else {
            Picasso.get()
                    .load(R.drawable.noimageavailable)
                    .placeholder(R.drawable.noimageavailable)
                    .into(holder.img_order_product);
        }
        }

        /*Product feedback */
        String rating=String.valueOf(newOrderItemArrayList.get(holder.getAdapterPosition()).getRating());
        switch (rating){
            case "1":
                holder.ivProductFeedback.setImageResource(R.drawable.star_1);
                break;
            case "2":
                holder.ivProductFeedback.setImageResource(R.drawable.star_2);
                break;
            case "3":
                holder.ivProductFeedback.setImageResource(R.drawable.star_3);
                break;
            case "4":
                holder.ivProductFeedback.setImageResource(R.drawable.star_4);
                break;
            case "5":
                holder.ivProductFeedback.setImageResource(R.drawable.star_5_new);
                break;
            default:
                holder.ivProductFeedback.setImageResource(R.drawable.star_4_grey);
                break;
        }
        if(rating.equals("0")) {
            holder.ivProductFeedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showFeedbackDialog(orderId,newOrderItemArrayList.get(holder.getAdapterPosition()).getItemId(), holder);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return newOrderItemArrayList.size();
    }

    private void showFeedbackDialog(String orderId,String itemId,OrderItemHolder holder){
        rating="0";
        Dialog bottomSheetDialog=new Dialog(context);
        bottomSheetDialog.setContentView(R.layout.item_layout_feedback_1);
        int width = (int)(context.getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(context.getResources().getDisplayMetrics().heightPixels*0.90);

        MaterialButton btnBack=bottomSheetDialog.findViewById(R.id.btnBack);
        MaterialButton btnSubmit=bottomSheetDialog.findViewById(R.id.btnSubmit);
        ImageView close=bottomSheetDialog.findViewById(R.id.close);
        EditText edtFeedback=bottomSheetDialog.findViewById(R.id.edtFeedback);

         ivStar1_1=bottomSheetDialog.findViewById(R.id.ivStar1_1);
         ivStar1_2=bottomSheetDialog.findViewById(R.id.ivStar1_2);
         ivStar1_3=bottomSheetDialog.findViewById(R.id.ivStar1_3);
         ivStar1_4=bottomSheetDialog.findViewById(R.id.ivStar1_4);
         ivStar1_5=bottomSheetDialog.findViewById(R.id.ivStar1_5);

        bottomSheetDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,height);
        bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        bottomSheetDialog.getWindow().getAttributes().windowAnimations =  R.style.DialogAnimation;
        bottomSheetDialog.show();

        ivStar1_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRating("1",holder);
                rating="1";
            }
        });
        ivStar1_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating="2";
                setRating("2",holder);
            }
        });

        ivStar1_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating="3";
                setRating("3",holder);
            }
        });

        ivStar1_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating="4";
                setRating("4",holder);
            }
        });

        ivStar1_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating="5";
                setRating("5",holder);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(rating.equals("0")){
                    Toast.makeText(context, "Please Select Star", Toast.LENGTH_SHORT).show();
                }else{
                    addRatingReview(orderId,rating,itemId,edtFeedback.getText().toString(),bottomSheetDialog,holder);
                    /*switch (rating){
                        case "1":
                            holder.ivProductFeedback.setImageResource(R.drawable.star_1);
                            break;
                        case "2":
                            holder.ivProductFeedback.setImageResource(R.drawable.star_2);
                            break;
                        case "3":
                            holder.ivProductFeedback.setImageResource(R.drawable.star_3);
                            break;
                        case "4":
                            holder.ivProductFeedback.setImageResource(R.drawable.star_4);
                            break;
                        case "5":
                            holder.ivProductFeedback.setImageResource(R.drawable.star_5_new);
                            break;
                        default:
                            holder.ivProductFeedback.setImageResource(R.drawable.star_4_grey);
                            break;
                    }*/
                }

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.dismiss();

            }
        });
    }

    private void addRatingReview(String orderId,String rating,String itemId,String review,Dialog bottomSheetDialog,OrderItemHolder holder){
        progressDialog.show();
        ServiceGenrator.getApiInterface().addProductRatingReview(session_management.getUserDetails().get(BaseURL.KEY_ID),
                orderId,itemId,rating,review).enqueue(new Callback<ResponseAddRatingReview>() {
            @Override
            public void onResponse(Call<ResponseAddRatingReview> call, retrofit2.Response<ResponseAddRatingReview> response) {
                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    bottomSheetDialog.dismiss();
                    if(response.body().isStatus()){
                        Toast.makeText(context,response.body().getMessage(),Toast.LENGTH_LONG).show();
                        switch (rating){
                            case "1":
                                holder.ivProductFeedback.setImageResource(R.drawable.star_1);
                                break;
                            case "2":
                                holder.ivProductFeedback.setImageResource(R.drawable.star_2);
                                break;
                            case "3":
                                holder.ivProductFeedback.setImageResource(R.drawable.star_3);
                                break;
                            case "4":
                                holder.ivProductFeedback.setImageResource(R.drawable.star_4);
                                break;
                            case "5":
                                holder.ivProductFeedback.setImageResource(R.drawable.star_5_new);
                                break;
                            default:
                                holder.ivProductFeedback.setImageResource(R.drawable.star_4_grey);
                                break;
                        }
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

    private void setRating(String rating,OrderItemHolder holder){
        switch (rating){
            case "1":
                ivStar1_1.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.duskYellow)));
                ivStar1_2.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.grey_60)));
                ivStar1_3.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.grey_60)));
                ivStar1_4.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.grey_60)));
                ivStar1_5.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.grey_60)));
                /*ivStar1_1.setImageResource(R.drawable.star_1);
                ivStar1_2.setImageResource(R.drawable.star_2_grey);
                ivStar1_3.setImageResource(R.drawable.star_3_grey);
                ivStar1_4.setImageResource(R.drawable.star_4_grey);
                ivStar1_5.setImageResource(R.drawable.star_5_grey);*/
                break;
            case "2":
                ivStar1_1.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.duskYellow)));
                ivStar1_2.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.duskYellow)));
                ivStar1_3.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.grey_60)));
                ivStar1_4.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.grey_60)));
                ivStar1_5.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.grey_60)));
                /*ivStar1_1.setImageResource(R.drawable.star_1_grey);
                ivStar1_2.setImageResource(R.drawable.star_2);
                ivStar1_3.setImageResource(R.drawable.star_3_grey);
                ivStar1_4.setImageResource(R.drawable.star_4_grey);
                ivStar1_5.setImageResource(R.drawable.star_5_grey);*/
                break;
            case "3":
                ivStar1_1.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.duskYellow)));
                ivStar1_2.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.duskYellow)));
                ivStar1_3.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.duskYellow)));
                ivStar1_4.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.grey_60)));
                ivStar1_5.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.grey_60)));
                /*ivStar1_1.setImageResource(R.drawable.star_1_grey);
                ivStar1_2.setImageResource(R.drawable.star_2_grey);
                ivStar1_3.setImageResource(R.drawable.star_3);
                ivStar1_4.setImageResource(R.drawable.star_4_grey);
                ivStar1_5.setImageResource(R.drawable.star_5_grey);*/
                break;
            case "4":
                ivStar1_1.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.duskYellow)));
                ivStar1_2.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.duskYellow)));
                ivStar1_3.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.duskYellow)));
                ivStar1_4.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.duskYellow)));
                ivStar1_5.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.grey_60)));
               /* ivStar1_1.setImageResource(R.drawable.star_1_grey);
                ivStar1_2.setImageResource(R.drawable.star_2_grey);
                ivStar1_3.setImageResource(R.drawable.star_3_grey);
                ivStar1_4.setImageResource(R.drawable.star_4);
                ivStar1_5.setImageResource(R.drawable.star_5_grey);*/
                break;
            case "5":
                ivStar1_1.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.duskYellow)));
                ivStar1_2.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.duskYellow)));
                ivStar1_3.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.duskYellow)));
                ivStar1_4.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.duskYellow)));
                ivStar1_5.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.duskYellow)));
               /* ivStar1_1.setImageResource(R.drawable.star_1_grey);
                ivStar1_2.setImageResource(R.drawable.star_2_grey);
                ivStar1_3.setImageResource(R.drawable.star_3_grey);
                ivStar1_4.setImageResource(R.drawable.star_4_grey);
                ivStar1_5.setImageResource(R.drawable.star_5_new);*/
                break;
            default:
                ivStar1_1.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.grey_60)));
                ivStar1_1.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.grey_60)));
                ivStar1_1.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.grey_60)));
                ivStar1_1.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.grey_60)));
                ivStar1_1.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.grey_60)));
                /*ivStar1_1.setImageResource(R.drawable.star_1_grey);
                ivStar1_2.setImageResource(R.drawable.star_2_grey);
                ivStar1_3.setImageResource(R.drawable.star_3_grey);
                ivStar1_4.setImageResource(R.drawable.star_4_grey);
                ivStar1_5.setImageResource(R.drawable.star_5_grey);*/
                break;
        }
    }
}
