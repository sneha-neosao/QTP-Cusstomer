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
import com.grocery.QTPmart.R;

import java.util.ArrayList;

import Config.BaseURL;
import ModelClass.NewOrderItem;
import adapters.ViewHolders.OrderItemHolder;
import network.Response.ResponseAddRatingReview;
import network.ServiceGenrator;
import retrofit2.Call;
import retrofit2.Callback;
import util.Session_management;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemHolder> {

    Context context;
    ArrayList<NewOrderItem> newOrderItemArrayList;
    ProgressDialog progressDialog;
    Session_management session_management;

    public ImageView ivStar1_1, ivStar1_2, ivStar1_3, ivStar1_4, ivStar1_5;
    String rating = "";
    String orderId;

    public OrderItemAdapter(Context context, ArrayList<NewOrderItem> list, String orderId) {
        this.context = context;
        this.orderId = orderId;
        this.newOrderItemArrayList = list;
        progressDialog = new ProgressDialog(context);
        session_management = new Session_management(context);
    }

    @NonNull
    @Override
    public OrderItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout_order_product, parent, false);
        return new OrderItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemHolder holder, int position) {

        if (newOrderItemArrayList == null || newOrderItemArrayList.isEmpty()) return;

        int pos = holder.getAdapterPosition();
        if (pos == RecyclerView.NO_POSITION) return;

        NewOrderItem item = newOrderItemArrayList.get(pos);

        // ✅ Name
        if (holder.txt_product_name != null)
            holder.txt_product_name.setText(item.getItemName());

        // ✅ Price
        try {
            double price = Double.parseDouble(item.getPerUnitPrice());
            if (holder.txt_product_price != null)
                holder.txt_product_price.setText("AED " + String.format("%.2f", price));
        } catch (Exception e) {
            if (holder.txt_product_price != null)
                holder.txt_product_price.setText("AED 0.00");
        }

        // ✅ Quantity
        if (holder.txt_product_quantity != null)
            holder.txt_product_quantity.setText("Qty : " + item.getQuantity());

        // ✅ UOM
        if (holder.txt_product_weight != null)
            holder.txt_product_weight.setText(item.getUom());

        // ✅ Cancel Strike
        if (item.getOrderItemStatus() != null && item.getOrderItemStatus().equals("9")) {

            if (holder.txt_product_name != null)
                holder.txt_product_name.setPaintFlags(holder.txt_product_name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            if (holder.txt_product_price != null)
                holder.txt_product_price.setPaintFlags(holder.txt_product_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            if (holder.txt_product_quantity != null)
                holder.txt_product_quantity.setPaintFlags(holder.txt_product_quantity.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            if (holder.txt_product_weight != null)
                holder.txt_product_weight.setPaintFlags(holder.txt_product_weight.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        // ✅ Image
        if (holder.img_order_product != null) {
            if (item.getImage() != null && !item.getImage().isEmpty()) {
                Glide.with(context)
                        .load(item.getImage())
                        .placeholder(R.drawable.noimageavailable)
                        .into(holder.img_order_product);
            } else {
                holder.img_order_product.setImageResource(R.drawable.noimageavailable);
            }
        }

        // ✅ Rating
        String ratingValue = String.valueOf(item.getRating());

        if (holder.ivProductFeedback != null) {
            switch (ratingValue) {
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

            if ("0".equals(ratingValue)) {
                holder.ivProductFeedback.setOnClickListener(v ->
                        showFeedbackDialog(orderId, item.getItemId(), holder));
            }
        }
    }

    @Override
    public int getItemCount() {
        return newOrderItemArrayList != null ? newOrderItemArrayList.size() : 0;
    }

    // ================= FEEDBACK DIALOG =================

    private void showFeedbackDialog(String orderId, String itemId, OrderItemHolder holder) {

        rating = "0";

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.item_layout_feedback_1);

        int height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.90);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, height);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        MaterialButton btnSubmit = dialog.findViewById(R.id.btnSubmit);
        ImageView close = dialog.findViewById(R.id.close);
        EditText edtFeedback = dialog.findViewById(R.id.edtFeedback);

        ivStar1_1 = dialog.findViewById(R.id.ivStar1_1);
        ivStar1_2 = dialog.findViewById(R.id.ivStar1_2);
        ivStar1_3 = dialog.findViewById(R.id.ivStar1_3);
        ivStar1_4 = dialog.findViewById(R.id.ivStar1_4);
        ivStar1_5 = dialog.findViewById(R.id.ivStar1_5);

        dialog.show();

        View.OnClickListener starClick = v -> {
            int id = v.getId();

            if (id == R.id.ivStar1_1) {
                rating = "1";
            } else if (id == R.id.ivStar1_2) {
                rating = "2";
            } else if (id == R.id.ivStar1_3) {
                rating = "3";
            } else if (id == R.id.ivStar1_4) {
                rating = "4";
            } else if (id == R.id.ivStar1_5) {
                rating = "5";
            }

            setRating(rating);
        };

        ivStar1_1.setOnClickListener(starClick);
        ivStar1_2.setOnClickListener(starClick);
        ivStar1_3.setOnClickListener(starClick);
        ivStar1_4.setOnClickListener(starClick);
        ivStar1_5.setOnClickListener(starClick);

        close.setOnClickListener(v -> dialog.dismiss());

        btnSubmit.setOnClickListener(v -> {
            if (rating.equals("0")) {
                Toast.makeText(context, "Please Select Star", Toast.LENGTH_SHORT).show();
            } else {
                addRatingReview(orderId, rating, itemId, edtFeedback.getText().toString(), dialog, holder);
            }
        });
    }

    // ================= API =================

    private void addRatingReview(String orderId, String rating, String itemId, String review,
                                 Dialog dialog, OrderItemHolder holder) {

        progressDialog.show();

        ServiceGenrator.getApiInterface()
                .addProductRatingReview(
                        session_management.getUserDetails().get(BaseURL.KEY_ID),
                        orderId, itemId, rating, review
                )
                .enqueue(new Callback<ResponseAddRatingReview>() {
                    @Override
                    public void onResponse(Call<ResponseAddRatingReview> call,
                                           retrofit2.Response<ResponseAddRatingReview> response) {

                        progressDialog.dismiss();
                        dialog.dismiss();

                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseAddRatingReview> call, Throwable t) {
                        progressDialog.dismiss();
                        dialog.dismiss();
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    // ================= STAR UI =================

    private void setRating(String rating) {

        int yellow = context.getResources().getColor(R.color.duskYellow);
        int grey = context.getResources().getColor(R.color.grey_60);

        ImageView[] stars = {ivStar1_1, ivStar1_2, ivStar1_3, ivStar1_4, ivStar1_5};

        for (int i = 0; i < stars.length; i++) {
            if (stars[i] != null) {
                stars[i].setImageTintList(ColorStateList.valueOf(i < Integer.parseInt(rating) ? yellow : grey));
            }
        }
    }
}