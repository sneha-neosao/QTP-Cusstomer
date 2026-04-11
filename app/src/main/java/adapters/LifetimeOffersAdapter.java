package adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import activities.MainDrawerActivity;
import activities.OrderSummary;
import activities.ShippingDetailActivity;
import adapters.ViewHolders.LifetimeOfferViewHolder;
import Config.ApiBaseURL;
import ModelClass.LifetimeOffer;
import com.grocery.QTPmart.R;
import network.ApiInterface;
import util.DatabaseHandler;
import util.Session_management;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Config.BaseURL.KEY_EMAIL;
import static Config.BaseURL.KEY_ID;
import static Config.BaseURL.KEY_MOBILE;
import static Config.BaseURL.KEY_NAME;

public class LifetimeOffersAdapter extends RecyclerView.Adapter<LifetimeOfferViewHolder> {

    Activity activity;
    List<LifetimeOffer> list;
   public String payment_method="";
    double amount;
    double vatPer, vatCharge, shippingCharge, discount, grand;

    public String IMG_URL = "http://qtp.ae/QTPMobileApp/Images/Coupon/";
    boolean isShown = false;
    private ProgressDialog progressDialog;
    private Session_management sessionManagement;
    private DatabaseHandler db;
    private int tempPosition=-1;
    private int tempPositionShown=-1;

    public LifetimeOffersAdapter(Activity activity, List<LifetimeOffer> list) {
        this.activity = activity;
        this.list = list;
        sessionManagement = new Session_management(activity);
        db = new DatabaseHandler(activity);
    }

    public void setProductDetails(double amount, double vatPer, double shippingCharge) {

        this.amount = amount;
        this.vatPer = vatPer;
        this.shippingCharge=shippingCharge;
    }

    @NonNull
    @Override
    public LifetimeOfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = activity.getLayoutInflater();

        return new LifetimeOfferViewHolder(inflater.inflate(R.layout.item_layout_offers_new, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LifetimeOfferViewHolder holder, int position) {


        LifetimeOffer offer = list.get(position);

        Log.e("image", IMG_URL + offer.imagePath);
        Picasso.get().load(ApiBaseURL.IMG_URL + offer.imagePath)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(holder.imageOffers);

        holder.titleOffer.setText(offer.cmName);
        holder.descOffer1.setText(offer.cmDescription);


        holder.descOffer2.setText(offer.discountValue + " " + offer.ctDescription);


        holder.subTotalAmount.setText("AED " + String.format("%.2f", amount));

        holder.couponPer.setText("Coupon " + String.format("%.2f", Double.parseDouble(offer.discountValue)) + "%");

        discount = amount * Double.parseDouble(offer.discountValue) * 0.01;

        holder.couponApplied.setText("AED " + String.format("%.2f", discount));

        double totalAmount = amount - discount;

        holder.total.setText("AED " + String.format("%.2f", totalAmount));

        holder.vatPercent.setText("VAT " + String.format("%.2f", vatPer) + "%");

        vatCharge = 0.01 * vatPer * totalAmount;
        holder.vatPercentAmount.setText("AED " + String.format("%.2f", vatCharge));

        holder.shippingCharges.setText("AED " + offer.getShippingCharges());

        grand = totalAmount + vatCharge + offer.getShippingCharges();

        holder.grandTotalAmount.setText("AED " + String.format("%.2f", grand));

        Log.e("grand",grand+"\n"+OrderSummary.amount);

        if (amount < Double.parseDouble(offer.minimumPurchaseAmount)) {

            holder.amount.setVisibility(View.GONE);
            holder.minAmount.setVisibility(View.VISIBLE);
            holder.minAmount.setText("Your purchase minimum order should be AED " + offer.minimumPurchaseAmount
             + " to avail this offer");
            holder.proceed.setText("Shop More");
            holder.showDetails.setVisibility(View.GONE);

        } else {

            holder.amount.setVisibility(View.VISIBLE);
            holder.minAmount.setVisibility(View.GONE);

            String discAmount = String.format("%.2f", (100 - Double.parseDouble(offer.discountValue)) * 0.01 * amount);
            holder.amount.setText("Total Amount After Discount AED " + discAmount);

            holder.proceed.setText("Proceed");
            holder.lock.setVisibility(View.GONE);
        }

        holder.proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (holder.proceed.getText().toString().equals("Proceed")) {
                    tempPosition=position;
                    //isShown=false;
                    notifyDataSetChanged();

                    if (tempPosition==position){
                        holder.relativeLayout.setVisibility(View.VISIBLE);
                        holder.detailsTable.setVisibility(View.VISIBLE);
                        holder.showDetails.setVisibility(View.GONE);
                        holder.hideDetails.setVisibility(View.VISIBLE);
                        //isShown=true;
                    }
                    else  {
                        holder.relativeLayout.setVisibility(View.GONE);
                        holder.detailsTable.setVisibility(View.GONE);
                        holder.hideDetails.setVisibility(View.GONE);
                        // holder.showDetails.setVisibility(View.VISIBLE);
                        //isShown=false;
                    }


                    holder.showDetails.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            holder.detailsTable.setVisibility(View.VISIBLE);
                            holder.hideDetails.setVisibility(View.VISIBLE);
                            holder.showDetails.setVisibility(View.GONE);


                if (isShown) {

                    holder.detailsTable.setVisibility(View.GONE);
                    holder.showDetails.setText("Show Details");
                    isShown = false;
                } else {

                    holder.detailsTable.setVisibility(View.VISIBLE);
                    holder.showDetails.setText("Hide Details");
                    isShown = true;
                }
                        }
                    });

                    holder.hideDetails.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            holder.detailsTable.setVisibility(View.GONE);
                            holder.hideDetails.setVisibility(View.GONE);
                            holder.showDetails.setVisibility(View.VISIBLE);

                        }
                    });

                    if (!payment_method.isEmpty())
                    {
                        if (payment_method.equals("ONLINE"))
                        {
                            Toast.makeText(activity, "Online payment mode coming soon", Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                    holder.relativeLayout.setVisibility(View.VISIBLE);
                            Intent intent = new Intent("offers");
//                            intent.putExtra("msg", msg);
                            intent.putExtra("subtotal",amount);
                            intent.putExtra("coupon",discount);
                            intent.putExtra("couponper",offer.discountValue);
                            intent.putExtra("total",totalAmount);
                            intent.putExtra("vat",vatCharge);
                            intent.putExtra("vatper",vatPer);
                            intent.putExtra("shipping",offer.getShippingCharges());
                            intent.putExtra("description",offer.getcDescription());
                            intent.putExtra("grand_total",grand);
                            intent.putExtra("cmid",offer.getCmid());
                            intent.putExtra("cmcode",offer.getCmCode());
                            intent.putExtra("nextlimit",offer.getLimitNumberOfUses());
                            activity.sendBroadcast(intent);
                            continueUrl(db.getCartCount(), String.valueOf(totalAmount), "offer", offer.cmCode,offer.discountValue);

                        }

                    }
                    else
                    {
                        Toast.makeText(activity, "Please select payment mode", Toast.LENGTH_SHORT).show();
                    }



                } else {

                    Intent intent=new Intent(activity, MainDrawerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    activity.startActivity(intent);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        activity.finishAndRemoveTask();
                    } else {
                        activity.finish();
                    }
                }
            }
        });

        if (tempPosition==position){
            holder.relativeLayout.setVisibility(View.VISIBLE);
            holder.detailsTable.setVisibility(View.VISIBLE);
            holder.showDetails.setVisibility(View.GONE);
            holder.hideDetails.setVisibility(View.VISIBLE);
            //isShown=true;
        }
        else  {
            holder.relativeLayout.setVisibility(View.GONE);
            holder.detailsTable.setVisibility(View.GONE);
            holder.hideDetails.setVisibility(View.GONE);
           // holder.showDetails.setVisibility(View.VISIBLE);
            //isShown=false;
        }


        holder.showDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.detailsTable.setVisibility(View.VISIBLE);
                holder.hideDetails.setVisibility(View.VISIBLE);
                holder.showDetails.setVisibility(View.GONE);


               /* if (isShown) {

                    holder.detailsTable.setVisibility(View.GONE);
                    holder.showDetails.setText("Show Details");
                    isShown = false;
                } else {

                    holder.detailsTable.setVisibility(View.VISIBLE);
                    holder.showDetails.setText("Hide Details");
                    isShown = true;
                }*/
            }
        });

        holder.hideDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.detailsTable.setVisibility(View.GONE);
                holder.hideDetails.setVisibility(View.GONE);
                holder.showDetails.setVisibility(View.VISIBLE);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    private void continueUrl(final int totalItems, final String totalAmount, String couponType, String promo, String discountValue) {

        progressDialog = new ProgressDialog(activity);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiBaseURL.OrderContinue, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("ordermake", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    String msg = jsonObject.getString("message");
                    if (status) {

                        db.clearCart();
//                        Intent intent = new Intent(activity, OrderSuccessful.class);
//                        intent.putExtra("msg", msg);
//                        activity.startActivity(intent);
                        Intent intent = new Intent("offers");
                        intent.putExtra("msg", msg);
                        intent.putExtra("subtotal",amount);
                        intent.putExtra("coupon",discount);
                        intent.putExtra("couponper",discountValue);
                        intent.putExtra("total",totalAmount);
                        intent.putExtra("vat",vatCharge);
                        intent.putExtra("vatper",vatPer);
                        intent.putExtra("shipping",shippingCharge);
                        intent.putExtra("grand_total",grand);
                        activity.sendBroadcast(intent);



//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                            activity.finishAndRemoveTask();
//                        } else {
//                            activity.finish();
//                        }

                    } else {

                        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("TAG", "onErrorResponse: " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();

                param.put("custID", sessionManagement.getUserDetails().get(KEY_ID));
                param.put("OrderStatus", "" + totalItems);
                param.put("SubTotal", String.format("%.2f", amount));
                param.put("Total", String.format("%.2f", Double.parseDouble(totalAmount)));
                param.put("FirstName", sessionManagement.getUserDetails().get(KEY_NAME));
                param.put("Mobile", sessionManagement.getUserDetails().get(KEY_MOBILE));
                param.put("email", sessionManagement.getUserDetails().get(KEY_EMAIL));
                param.put("AddressLine1", sessionManagement.getAddress());
                param.put("City", sessionManagement.getLocationCity());
                param.put("country", sessionManagement.getCountry());
                param.put("BranchCode", ApiInterface.branchcode);
                param.put("tax", String.format("%.2f", vatCharge));
                param.put("shipping", String.format("%.2f", shippingCharge));
                param.put("couponType", couponType);
                param.put("Promo", promo);
                param.put("discount",String.format("%.2f", discount));
                param.put("grandtotal",String.format("%.2f", grand));
                param.put("CMID","" );
                param.put("CMCode","");
                param.put("DecidedExisitingLimit", "0");
                param.put("latitude", sessionManagement.getLatPref());
                param.put("longitude", sessionManagement.getLangPref());

                Log.e("TAG", "getParams: " + param.toString());
                return param;
            }
        };
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 1;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.getCache().clear();
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 90000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(stringRequest);

    }

}
