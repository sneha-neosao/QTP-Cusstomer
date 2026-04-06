package activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import BuildConfig;
import Config.ApiBaseURL;
import PaymentDataModels.CreateOrderResponseDto;
import PaymentDataModels.ResponseOrderModel;
import PaymentDataModels1.OrderResponse;
import PaymentDataModels1.ResponseOrderModel1;

import com.google.firebase.installations.interop.BuildConfig;
import com.grocery.QTPmart.R;
import network.ApiInterface;
import network.RequestBookPayment;
import network.ServiceGenrator;
import util.DatabaseHandler;
import util.Session_management;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
//import payment.sdk.android.PaymentClient;
//import payment.sdk.android.cardpayment.CardPaymentData;
//import payment.sdk.android.cardpayment.CardPaymentRequest;
import retrofit2.Call;
import retrofit2.Callback;

import static Config.BaseURL.KEY_EMAIL;
import static Config.BaseURL.KEY_ID;
import static Config.BaseURL.KEY_MOBILE;
import static Config.BaseURL.KEY_NAME;

public class NetworkPaymentActivity  extends AppCompatActivity
{
    private String message,totalQuantity,totalAmount,subTotalSuccess,couponSuccess="",
            couponSuccessText,totalSuccess,vatSuccess,TYPE_COUPON,OrderTransactionType,
            shippingChargeSuccess,grandSuccess,vatSuccessPer,addressSuccess,cmid="",cmcode="",promo="",
            couponType,orderID;

    private String TAG="NetworkPaymentActivity";
    private String PaymentGatewayRef="";
    boolean isCouponApplied=false;
    ProgressDialog progressDialog;
    private DatabaseHandler db;
    private Session_management sessionManagement;
    private String vatCharge,shippingCharge,couponCodeText,discount,grand,nextlimit,total_atm;
    double grandTotal;

    String shippingAddress,shippingCity,shippingCountry,shippingLatitude,shippingLongitude,orderStatus,couponCodeText1,shippingProvince,shippingAddressType;

    Double subTotal, discountInPercentage, discountInAmount, total1, vatRate1, vatTotal, deliveryCharges, grandTotal1;

    String name,email,mobile,custId,deviceName,appVersion,cartID;

    Boolean isCouponApplied1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instamojo_payment);
        sessionManagement = new Session_management(getApplicationContext());
        progressDialog=new ProgressDialog(this);
        db = new DatabaseHandler(this);

        /*totalQuantity=getIntent().getStringExtra("totalQuantity");
        totalAmount = getIntent().getStringExtra("totalAmount");
        //subTotalSuccess = getIntent().getStringExtra("subTotalSuccess");
        subTotal = getIntent().getDoubleExtra("subTotalSuccess",0);
        addressSuccess =getIntent().getStringExtra("addressSuccess");
        couponSuccess = getIntent().getStringExtra("couponSuccess");
        couponSuccessText = getIntent().getStringExtra("couponSuccessText");
        totalSuccess= getIntent().getStringExtra("totalSuccess");
        vatSuccess= getIntent().getStringExtra("vatSuccess");
        shippingChargeSuccess= getIntent().getStringExtra("shippingChargeSuccess");
        grandSuccess = getIntent().getStringExtra("grandSuccess");
        vatSuccessPer = getIntent().getStringExtra("vatSuccessPer");
        TYPE_COUPON= getIntent().getStringExtra("TYPE_COUPON");
        OrderTransactionType = getIntent().getStringExtra("OrderTransactionType");
        isCouponApplied = getIntent().getBooleanExtra("isCouponApplied",false);
        cmid = getIntent().getStringExtra("isCouponID");
        cmcode = getIntent().getStringExtra("cmcode");
        vatCharge= getIntent().getStringExtra("vatCharge");
        shippingCharge= getIntent().getStringExtra("shippingCharge");
        couponCodeText=getIntent().getStringExtra("couponCodeText");
        //discount= getIntent().getStringExtra("discount");
        discountInAmount= getIntent().getDoubleExtra("discount",0);
        grand= String.valueOf(getIntent().getDoubleExtra("grand",0));
        grandTotal= getIntent().getDoubleExtra("grand",0);
        nextlimit =getIntent().getStringExtra("nextlimit");
        total_atm= getIntent().getStringExtra("total_atm");
        couponType= getIntent().getStringExtra("TYPE_COUPON");
*/


        subTotal= getIntent().getDoubleExtra("SubTotal",0);
        discountInAmount= getIntent().getDoubleExtra("discountInAmount",0);
        vatTotal= getIntent().getDoubleExtra("vatTotal",0);
        deliveryCharges= getIntent().getDoubleExtra("deliveryCharges",0);
        total1= getIntent().getDoubleExtra("total1",0);
        grandTotal= getIntent().getDoubleExtra("grandTotal",0);
        grandSuccess= getIntent().getStringExtra("grandSuccess");

        shippingAddress= getIntent().getStringExtra("shippingAddress");
        shippingCity= getIntent().getStringExtra("shippingCity");
        shippingProvince= getIntent().getStringExtra("shippingProvince");
        shippingAddressType= getIntent().getStringExtra("shippingAddressType");
        shippingCountry= getIntent().getStringExtra("shippingCountry");
        shippingLatitude= getIntent().getStringExtra("shippingLatitude");
        shippingLongitude= getIntent().getStringExtra("shippingLongitude");
        orderStatus= getIntent().getStringExtra("OrderStatus");
        cartID= getIntent().getStringExtra("cartID");

        cmid = getIntent().getStringExtra("isCouponID");
        cmcode = getIntent().getStringExtra("cmcode");
        promo = getIntent().getStringExtra("cmcode");
        TYPE_COUPON= getIntent().getStringExtra("TYPE_COUPON");
        nextlimit =getIntent().getStringExtra("nextlimit");
        couponCodeText1 =getIntent().getStringExtra("couponCodeText");
        isCouponApplied1 =getIntent().getBooleanExtra("isCouponApplied",false);

        if(isCouponApplied1){
            promo=cmcode;
        }else{
            promo=couponCodeText1;
        }


        name =  sessionManagement.getUserDetails().get(KEY_NAME);
        custId =  sessionManagement.getUserDetails().get(KEY_ID);
        mobile =  sessionManagement.getUserDetails().get(KEY_MOBILE);
        email =  sessionManagement.getUserDetails().get(KEY_EMAIL);
//        appVersion = BuildConfig.VERSION_NAME;




        //int amt=(int)grandTotal;

        //Log.e(TAG, "onCreate: "+amt );
        //subTotalSuccess=subTotalSuccess.replace("AED ","");

        //setCreateOrder2(grandTotal);
        setCreateOrder1l();
    }


    public void setCreateOrder(double amt) {
        progressDialog.show();
        RequestBookPayment requestBookPayment=new RequestBookPayment();
        requestBookPayment.setSubTotal(subTotalSuccess);
        requestBookPayment.setDiscount(discount);
        requestBookPayment.setEmail(sessionManagement.getUserDetails().get(KEY_EMAIL));
        requestBookPayment.setCity(sessionManagement.getLocationCity());

        requestBookPayment.setTax(vatCharge);
        requestBookPayment.setShipping(shippingCharge);
        requestBookPayment.setTotal(totalAmount);
        requestBookPayment.setCountry(sessionManagement.getCountry());
        requestBookPayment.setGrandtotal(amt);
        requestBookPayment.setFirstName(sessionManagement.getUserDetails().get(KEY_NAME));
        requestBookPayment.setAddressLine1(sessionManagement.getAddress());
        requestBookPayment.setCustID(sessionManagement.getUserDetails().get(KEY_ID));
        requestBookPayment.setMobile(sessionManagement.getUserDetails().get(KEY_MOBILE));
        requestBookPayment.setLatitude(sessionManagement.getLatPref());
        requestBookPayment.setLongitude(sessionManagement.getLangPref());
        requestBookPayment.setDeviceName("Android");
//        requestBookPayment.setAppVersion(BuildConfig.VERSION_NAME);
        requestBookPayment.setProvince(shippingProvince);
        requestBookPayment.setAddressType(shippingAddressType);

        if (isCouponApplied) {
            requestBookPayment.setCMCode(cmcode);
            requestBookPayment.setPromo(cmcode);
            requestBookPayment.setCMID(cmid);
            requestBookPayment.setCouponType(couponType);
            requestBookPayment.setDecidedExisitingLimit(nextlimit);
        }
        else{
            requestBookPayment.setCMCode("");
            requestBookPayment.setPromo(couponCodeText);
            requestBookPayment.setCMID("");
            requestBookPayment.setCouponType("");
            requestBookPayment.setDecidedExisitingLimit("0");
        }

     /*   HashMap<String, String> param = new HashMap<>();

        param.put("custID", sessionManagement.getUserDetails().get(KEY_ID));
        param.put("SubTotal", String.format("%.2f", Double.parseDouble(total_atm)));
        param.put("Total", String.format("%.2f", Double.parseDouble(totalAmount)));
        param.put("FirstName", sessionManagement.getUserDetails().get(KEY_NAME));
        param.put("Mobile", sessionManagement.getUserDetails().get(KEY_MOBILE));
        param.put("email", sessionManagement.getUserDetails().get(KEY_EMAIL));
        param.put("AddressLine1", sessionManagement.getAddress());
        param.put("City", sessionManagement.getLocationCity());
        param.put("country", sessionManagement.getCountry());
        param.put("DeviceName","Android");
        param.put("latitude", sessionManagement.getLatPref());
        param.put("longitude", sessionManagement.getLangPref());
        param.put("tax", String.format("%.2f", vatCharge));
        param.put("shipping", String.format("%.2f", shippingCharge));
        param.put("discount",String.format("%.2f", discount));
        param.put("grandtotal",String.format("%.2f", grand));

        if (isCouponApplied) {
            param.put("CMID", cmid);
            param.put("CMCode", cmcode);
            param.put("Promo", cmcode);
            param.put("couponType", couponType);
            param.put("DecidedExisitingLimit", nextlimit);
        }
        else
        {
            param.put("CMID","" );
            param.put("CMCode","");
            param.put("Promo", couponCodeText);
            param.put("couponType", "");
            param.put("DecidedExisitingLimit", "0");
        }
*/
        ServiceGenrator.getApiInterface().createOrderService(requestBookPayment).enqueue(
                new Callback<ResponseOrderModel>() {
                    @Override
                    public void onResponse(Call<ResponseOrderModel> call, retrofit2.Response<ResponseOrderModel> response) {
                        String URL = call.request().url().toString();
                        System.out.println("Retrofit URL : " + URL);
                        if (response.code() == 200) {
                            if(response.body().getStatus()) {
                                orderID=response.body().getOrderID();
                                PaymentGatewayRef=response.body().getOrderResponse().getReference();
                                Log.e(TAG, "onResponse: "+PaymentGatewayRef );
                                CreateOrderResponseDto responseDto = response.body().getOrderResponse();
                                progressDialog.dismiss();
//                                CardPaymentRequest request = new CardPaymentRequest(responseDto.getPaymentLinks().getPaymentAuthorization().getHref(),
//                                        responseDto.getPaymentLinks().getPayment().getHref().split("=")[1]);
//
//                                PaymentClient paymentClient = new PaymentClient(NetworkPaymentActivity.this);
//                                paymentClient.launchCardPayment(request, 0);

                                db.clearCart();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    SharedPreferences preferences = NetworkPaymentActivity.this.getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                                    preferences.edit().putInt("cardqnty", db.getCartCount()).apply();
                                }
                                //clear cart

                            }
                            else
                            {
                                progressDialog.dismiss();
                                finish();
                            }
                            Toast.makeText(NetworkPaymentActivity.this,""+response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        } else{
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseOrderModel> call, Throwable t) {
                        progressDialog.dismiss();
                        finish();
                    }
                }
        );
    }

    public void setCreateOrder2(double amt) {
        progressDialog.show();
        /*RequestBookPayment requestBookPayment=new RequestBookPayment();
        requestBookPayment.setSubTotal(subTotalSuccess);
        requestBookPayment.setDiscount(discount);
        requestBookPayment.setEmail(sessionManagement.getUserDetails().get(KEY_EMAIL));
        requestBookPayment.setCity(sessionManagement.getLocationCity());

        requestBookPayment.setTax(vatCharge);
        requestBookPayment.setShipping(shippingCharge);
        requestBookPayment.setTotal(totalAmount);
        requestBookPayment.setCountry(sessionManagement.getCountry());
        requestBookPayment.setGrandtotal(amt);
        requestBookPayment.setFirstName(sessionManagement.getUserDetails().get(KEY_NAME));
        requestBookPayment.setAddressLine1(sessionManagement.getAddress());
        requestBookPayment.setCustID(sessionManagement.getUserDetails().get(KEY_ID));
        requestBookPayment.setMobile(sessionManagement.getUserDetails().get(KEY_MOBILE));
        requestBookPayment.setLatitude(sessionManagement.getLatPref());
        requestBookPayment.setLongitude(sessionManagement.getLangPref());
        requestBookPayment.setDeviceName("Android");

        if (isCouponApplied) {
            requestBookPayment.setCMCode(cmcode);
            requestBookPayment.setPromo(cmcode);
            requestBookPayment.setCMID(cmid);
            requestBookPayment.setCouponType(couponType);
            requestBookPayment.setDecidedExisitingLimit(nextlimit);
        }
        else{
            requestBookPayment.setCMCode("");
            requestBookPayment.setPromo(couponCodeText);
            requestBookPayment.setCMID("");
            requestBookPayment.setCouponType("");
            requestBookPayment.setDecidedExisitingLimit("0");
        }*/

        if (isCouponApplied) {
            promo=cmcode;
        }
        else
        {
            cmid="";
            cmcode="";
            promo=couponCodeText;
            couponType="";
            nextlimit="0";
        }

     /*   HashMap<String, String> param = new HashMap<>();

        param.put("custID", sessionManagement.getUserDetails().get(KEY_ID));
        param.put("SubTotal", String.format("%.2f", Double.parseDouble(total_atm)));
        param.put("Total", String.format("%.2f", Double.parseDouble(totalAmount)));
        param.put("FirstName", sessionManagement.getUserDetails().get(KEY_NAME));
        param.put("Mobile", sessionManagement.getUserDetails().get(KEY_MOBILE));
        param.put("email", sessionManagement.getUserDetails().get(KEY_EMAIL));
        param.put("AddressLine1", sessionManagement.getAddress());
        param.put("City", sessionManagement.getLocationCity());
        param.put("country", sessionManagement.getCountry());
        param.put("DeviceName","Android");
        param.put("latitude", sessionManagement.getLatPref());
        param.put("longitude", sessionManagement.getLangPref());
        param.put("tax", String.format("%.2f", vatCharge));
        param.put("shipping", String.format("%.2f", shippingCharge));
        param.put("discount",String.format("%.2f", discount));
        param.put("grandtotal",String.format("%.2f", grand));

        if (isCouponApplied) {
            param.put("CMID", cmid);
            param.put("CMCode", cmcode);
            param.put("Promo", cmcode);
            param.put("couponType", couponType);
            param.put("DecidedExisitingLimit", nextlimit);
        }
        else
        {
            param.put("CMID","" );
            param.put("CMCode","");
            param.put("Promo", couponCodeText);
            param.put("couponType", "");
            param.put("DecidedExisitingLimit", "0");
        }
*/
//        Log.e("resq","sub"+subTotalSuccess+"dis:"+discount+"email:"+sessionManagement.getUserDetails().get(KEY_EMAIL)+"qty:"+totalQuantity+"coupon:"+couponType+"vat:"+
//                vatCharge+"mCode:"+cmcode+"shipping:"+shippingCharge+"total:"+totalAmount+"country:"+shippingCountry+"grand:"+
//                String.format("%.2f", grandTotal)+"nAME"+sessionManagement.getUserDetails().get(KEY_NAME)+"LIMIT:"+nextlimit+"promo:"+promo+"address:"+shippingAddress+"cmid:"+
//                cmid+"city:"+shippingCity+"province:"+shippingProvince+"addressType:"+shippingAddressType+"Custid:"+sessionManagement.getUserDetails().get(KEY_ID)+"mob:"+sessionManagement.getUserDetails().get(KEY_MOBILE)+"DevNmae:"+"Android"+"lat:"+
//                shippingLatitude+"Long:"+shippingLongitude+"Version:"+BuildConfig.VERSION_NAME);
        ServiceGenrator.getApiInterface().bookorderWithOnlinePayment(subTotalSuccess,discount,sessionManagement.getUserDetails().get(KEY_EMAIL),totalQuantity,couponType,
                vatCharge,cmcode,shippingCharge,totalAmount,shippingCountry,
                String.format("%.2f", grandTotal),sessionManagement.getUserDetails().get(KEY_NAME),nextlimit,promo,shippingAddress,
                cmid,shippingCity,sessionManagement.getUserDetails().get(KEY_ID),sessionManagement.getUserDetails().get(KEY_MOBILE),"Android",
                shippingLatitude,shippingLongitude, BuildConfig.VERSION_NAME,shippingProvince,shippingAddressType
                ).enqueue(
                new Callback<ResponseOrderModel1>() {
                    @Override
                    public void onResponse(Call<ResponseOrderModel1> call, retrofit2.Response<ResponseOrderModel1> response) {
                        String URL = call.request().url().toString();
                        System.out.println("Retrofit URL : " + URL);
                        if (response.code() == 200) {
                            if(response.body().getStatus()) {
                                orderID=response.body().getOrderID();
                                PaymentGatewayRef=response.body().getOrderResponse().getReference();
                                Log.e(TAG, "onResponse: "+PaymentGatewayRef );
                                OrderResponse responseDto = response.body().getOrderResponse();
                                progressDialog.dismiss();
//                                CardPaymentRequest request = new CardPaymentRequest(responseDto.getLinks().getPaymentAuthorization().getHref(),
//                                        responseDto.getLinks().getPayment().getHref().split("=")[1]);
//
//                                PaymentClient paymentClient = new PaymentClient(NetworkPaymentActivity.this);
//                                paymentClient.launchCardPayment(request, 0);

                                db.clearCart();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    SharedPreferences preferences = NetworkPaymentActivity.this.getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                                    preferences.edit().putInt("cardqnty", db.getCartCount()).apply();
                                }
                                //clear cart

                            }
                            else
                            {
                                progressDialog.dismiss();
                                finish();
                            }
                            //Toast.makeText(NetworkPaymentActivity.this,""+response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        } else{
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseOrderModel1> call, Throwable t) {
                        progressDialog.dismiss();
                        finish();
                    }
                }
        );
    }

    private void setCreateOrder1l() {

        //Log.e(TAG, "continueUrl: "+OrderTransType );
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiBaseURL.OrderContinueOnline, new com.android.volley.Response.Listener<String>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(String response) {
                Log.e("ordermake", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    /*String msg = jsonObject.getString("message");
                    String orderCode = jsonObject.getString("orderCode");
                    String orderDate = jsonObject.getString("orderDate");*/
                     orderID = jsonObject.getString("orderID");
                    if (status) {
                        progressDialog.dismiss();
                        //OrderPlacedSuccessDialog(orderCode,orderDate,grandSuccess,msg);
                        orderID=jsonObject.getString("orderID");
                       // PaymentGatewayRef=response.body().getOrderResponse().getReference();
                        PaymentGatewayRef=jsonObject.getJSONObject("orderResponse").getString("reference");
                        Log.e(TAG, "onResponse: "+PaymentGatewayRef );
                        //CreateOrderResponseDto responseDto = response.body().getOrderResponse();
                       // CreateOrderResponseDto responseDto = new CreateOrderResponseDto();

                        progressDialog.dismiss();
                        /*CardPaymentRequest request1 = new CardPaymentRequest(responseDto.getPaymentLinks().getPaymentAuthorization().getHref(),
                                responseDto.getPaymentLinks().getPayment().getHref().split("=")[1]);*/

//                       CardPaymentRequest request = new CardPaymentRequest(jsonObject.getJSONObject("orderResponse").getJSONObject("_links").getJSONObject("payment-authorization").getString("href"),
//                                jsonObject.getJSONObject("orderResponse").getJSONObject("_links").getJSONObject("payment").getString("href").split("=")[1]);
//
//                       /* CardPaymentRequest request = new CardPaymentRequest("https://api-gateway.sandbox.ngenius-payments.com/transactions/paymentAuthorization",
//                                "c6d477c8053cc253");*/
//                        Log.e("requestGateWayUr",String.valueOf(request.getGatewayUrl()));
//                        Log.e("requestNWCode",String.valueOf(request.getCode()));
//                        PaymentClient paymentClient = new PaymentClient(NetworkPaymentActivity.this);
//                        paymentClient.launchCardPayment(request, 0);

                        db.clearCart();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            SharedPreferences preferences = NetworkPaymentActivity.this.getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                            preferences.edit().putInt("cardqnty", db.getCartCount()).apply();
                        }
                    } else {
                        progressDialog.dismiss();
                        //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e(TAG, "onErrorResponse: " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();

                param.put("SubTotal", String.format("%.2f", subTotal));
                param.put("discount",String.format("%.2f", discountInAmount));
                param.put("email", sessionManagement.getUserDetails().get(KEY_EMAIL));
                param.put("OrderStatus", "" + orderStatus);
                param.put("tax",  String.format("%.2f", vatTotal));
                param.put("shipping", String.format("%.2f", deliveryCharges));
                param.put("Total",  String.format("%.2f", total1));
                param.put("country", shippingCountry);
                param.put("grandtotal",String.format("%.2f", grandTotal));
                param.put("FirstName", sessionManagement.getUserDetails().get(KEY_NAME));
                param.put("AddressLine1", shippingAddress);
                param.put("City", shippingCity);
                param.put("custID", sessionManagement.getUserDetails().get(KEY_ID));
                param.put("Mobile", sessionManagement.getUserDetails().get(KEY_MOBILE));
                param.put("DeviceName","Android");
                param.put("latitude", shippingLatitude);
                param.put("longitude", shippingLongitude);
                param.put("appVersion", BuildConfig.VERSION_NAME);
                param.put("Province", shippingProvince);
                param.put("AddressType", shippingAddressType);


                if (isCouponApplied1) {
                    param.put("CMID", cmid);
                    param.put("CMCode", cmcode);
                    param.put("Promo", cmcode);
                    param.put("couponType", couponType);
                    param.put("DecidedExisitingLimit", nextlimit);
                }
                else
                {
                    param.put("CMID","" );
                    param.put("CMCode","");
                    param.put("Promo", couponCodeText1);
                    param.put("couponType", "");
                    param.put("DecidedExisitingLimit", "0");
                }

                Log.e(TAG, "getParams: " + param.toString());
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
        RequestQueue requestQueue = Volley.newRequestQueue(NetworkPaymentActivity.this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("request_code", String.valueOf(requestCode));
        Log.d("resultt_code", String.valueOf(resultCode));
        if (data == null) {
            Toast.makeText(this, "transaction cancelled", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            if (requestCode == 0) {

//                CardPaymentData cardPaymentData = CardPaymentData.getFromIntent(data);
//                Log.e(TAG, "onActivityResult: "+cardPaymentData.getCode() );
//                if (cardPaymentData.getCode() == 2) {
//                    continueUrl(Integer.parseInt(totalQuantity),totalAmount,subTotalSuccess,addressSuccess,couponSuccess
//                            ,couponSuccessText,totalSuccess,vatSuccess,shippingChargeSuccess,grandSuccess,
//                            vatSuccessPer,TYPE_COUPON,OrderTransactionType,PaymentGatewayRef);
//                }
//                if (cardPaymentData.getCode() !=2)
//                {
//                    transFailed(PaymentGatewayRef);
//                }
            }
        }
    }

    private void transFailed(String PaymentGatewayRef){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiBaseURL.OrderContinuePayment, new com.android.volley.Response.Listener<String>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(String response) {
                Log.e("ordermake", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    String msg = jsonObject.getString("message");
                    if (status) {
//                        startActivity(new Intent(NetworkPaymentActivity.this,FailedOrderActivity.class));
                        finish();
                        Toast.makeText(NetworkPaymentActivity.this, "Transaction failed", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(NetworkPaymentActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e(TAG, "onErrorResponse: " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("OrderStatus", "22");
                param.put("orderID", "" + orderID);
                param.put("IsPaymentSuccessful","0");
                param.put("PaymentGatewayRef",PaymentGatewayRef);
                param.put("cartID",cartID);
                param.put("custID",sessionManagement.getUserDetails().get(KEY_ID));
                if (isCouponApplied1) {
                    param.put("CMID", cmid);
                    param.put("CMCode", cmcode);
                    param.put("Promo", cmcode);
                    param.put("couponType", couponType);
                    param.put("DecidedExisitingLimit", nextlimit);
                }
                else
                {
                    param.put("CMID","" );
                    param.put("CMCode","");
                    param.put("Promo", couponCodeText1);
                    param.put("couponType", "");
                    param.put("DecidedExisitingLimit", "0");
                }

                Log.e(TAG, "getParams: " + param.toString());
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
        RequestQueue requestQueue = Volley.newRequestQueue(NetworkPaymentActivity.this);
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

    private void continueUrl(final int totalItems, final String totalAmount, String subTotalSuccess,
                             String addressSuccess, String couponSuccess,String couponSuccessText,
                             String totalSuccess,String vatSuccess,
                             String shippingChargeSuccess,String grandSuccess,
                             String vatSuccessPer, String couponType,
                             String OrderTransType, String PaymentGatewayRef) {
        Log.e(TAG, "continueUrl: "+OrderTransType );
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiBaseURL.OrderContinuePayment, new com.android.volley.Response.Listener<String>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(String response) {
                Log.e("ordermake", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    String msg = jsonObject.getString("message");

                    if (status) {
                        String orderCode = jsonObject.getString("orderCode");
                        String orderDate = jsonObject.getString("orderDate");
                        String orderRef = jsonObject.getString("orderRef");
                        progressDialog.dismiss();
                        /*Intent intent=new Intent(NetworkPaymentActivity.this,OrderSuccessActivity.class);
                        intent.putExtra("msg",msg);
                        intent.putExtra("amount",subTotalSuccess);
                        intent.putExtra("discount",couponSuccess);
                        intent.putExtra("totalAmount",totalSuccess);
                        intent.putExtra("vatCharge",vatSuccess);
                        intent.putExtra("shippingCharge",shippingChargeSuccess);
                        intent.putExtra("grand",grandSuccess);
                        intent.putExtra("vatRate",vatSuccessPer);
                        intent.putExtra("address",addressSuccess);
                        intent.putExtra("isCoupon",isCouponApplied);
                        intent.putExtra("couponSuccessText",couponSuccessText);
                        startActivity(intent);
                        finish();*/

                        OrderPlacedSuccessDialog(orderRef,orderDate,grandSuccess,msg);

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e(TAG, "onErrorResponse: " + error);
            }
        }) {
            //OrderID:2547
            //IsPaymentSuccessful:1
            //OrderStatus:1
           // PaymentGatewayRef:

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("OrderStatus", "1");
                param.put("orderID", "" + orderID);
                param.put("IsPaymentSuccessful","1");
                param.put("PaymentGatewayRef",PaymentGatewayRef);
                param.put("cartID",cartID);
                param.put("custID",sessionManagement.getUserDetails().get(KEY_ID));
                if (isCouponApplied1) {
                    param.put("CMID", cmid);
                    param.put("CMCode", cmcode);
                    param.put("Promo", cmcode);
                    param.put("couponType", couponType);
                    param.put("DecidedExisitingLimit", nextlimit);
                }
                else
                {
                    param.put("CMID","" );
                    param.put("CMCode","");
                    param.put("Promo", couponCodeText1);
                    param.put("couponType", "");
                    param.put("DecidedExisitingLimit", "0");
                }

                Log.e(TAG, "getParams: " + param.toString());
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
        RequestQueue requestQueue = Volley.newRequestQueue(NetworkPaymentActivity.this);
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


    private void OrderPlacedSuccessDialog(String orderNumber,String orderDate,String orderTotal,String msg){
        Dialog bottomSheetDialog=new Dialog(NetworkPaymentActivity.this);
        bottomSheetDialog.setContentView(R.layout.item_layout_bottom_place_order);
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);

        TextView tvMessage=bottomSheetDialog.findViewById(R.id.tvMessage);
        TextView tvOrderNumber=bottomSheetDialog.findViewById(R.id.tvOrderNumber);
        TextView tvOrderDate=bottomSheetDialog.findViewById(R.id.tvOrderDate);
        TextView tvOrderTotal=bottomSheetDialog.findViewById(R.id.tvOrderTotal);
        TextView tvClose=bottomSheetDialog.findViewById(R.id.tvClose);

        bottomSheetDialog.getWindow().setLayout(width,height);
        bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        bottomSheetDialog.show();

        tvMessage.setText(msg);
        tvOrderNumber.setText(orderNumber);
        tvOrderDate.setText(orderDate);
        tvOrderTotal.setText(orderTotal);

        tvClose.setOnClickListener(view -> {
            startActivity(new Intent(NetworkPaymentActivity.this, MainDrawerActivity.class)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK )
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        });

    }
}
