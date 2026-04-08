package activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.installations.interop.BuildConfig;
import com.google.gson.Gson;
import adapters.AddressAdapter;
import adapters.LifetimeOffersAdapter;
import adapters.ViewHolders.ImageAdapterData1;
//import BuildConfig;
import Config.ApiBaseURL;
import Config.BaseURL;
import ModelClass.AddressModel;
import ModelClass.LifetimeOffer;
import ModelClass.OrderCalculationModel;
import com.grocery.QTPmart.R;

import fragments.OrderFragment;
import network.ApiInterface;
import network.Response.ResAddress;
import network.Response.ResponseCoupon;
import network.Response.ResponseLifetimeOffers;
import network.Response.ResponseUpdateOrderStatus;
import network.ServiceGenrator;
import util.AppController;
import util.CustomVolleyJsonRequest;
import util.DatabaseHandler;
import util.Session_management;
import com.ncorti.slidetoact.SlideToActView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static Config.BaseURL.ADDRESS;
import static Config.BaseURL.KEY_EMAIL;
import static Config.BaseURL.KEY_ID;
import static Config.BaseURL.KEY_MOBILE;
import static Config.BaseURL.KEY_NAME;
import static Config.BaseURL.SHIPPING_ADDRESS_POSITION;
import static Config.BaseURL.SHIPPING_Address;
import static Config.BaseURL.SHIPPING_Address1;
import static Config.BaseURL.SHIPPING_AddressType;
import static Config.BaseURL.SHIPPING_CUSTOMER_Email;
import static Config.BaseURL.SHIPPING_CUSTOMER_Id;
import static Config.BaseURL.SHIPPING_CUSTOMER_Mobile;
import static Config.BaseURL.SHIPPING_City;
import static Config.BaseURL.SHIPPING_Country;
import static Config.BaseURL.SHIPPING_Latitude;
import static Config.BaseURL.SHIPPING_Longitude;
import static Config.BaseURL.SHIPPING_Province;
import static Config.BaseURL.SHIPPING_State;

public class ShippingDetailActivity extends AppCompatActivity implements AddressAdapter.ItemOnClickListener
{

    ImageView img_applyCoupon_arrow;
    MaterialButton btn_next,btn_add_new_address,btn_next_offer;
    LinearLayout ll_stage_1,ll_stage_2,ll_stage_3,ll_stage_4,ll_apply_coupon,llBack,llApplyCoupon,llVoucherApplied,llVoucherLimit;
    TextView txt_apply,tvCouponDescription;
    ImageView img_offer,img_payment,img_place_order,ivClose;
    public static SlideToActView btn_payment_slide,btn_confirmOrder_slide,btn_next_slide,btn_next_offer_slide;
    String couponCodeText = "",couponShippingCharges="",couponDescription="";
    View view_stage1,view_stage2,view_stage3;

    ArrayList<LifetimeOffer> list = new ArrayList<>();
    LifetimeOffersAdapter adapter;
    RecyclerView offers_rv,rv_address;

    CheckedTextView id_cod,id_ol,id_credit,id_terminal;
    String payment_method="",OrderTransactionType="";
    private Session_management session_management;

    String cmid="",cmcode="",nextlimit="",role="",supplierID="",cartID="";
    String total_atm,address;
    String subTotalSuccess,couponSuccess,couponSuccessText,totalSuccess,vatSuccess,
            shippingChargeSuccess,grandSuccess,vatSuccessPer,addressSuccess;
    final String TYPE_COUPON="coupon";
    private DatabaseHandler db;
    boolean isCouponApplied = false;
    double couponPer=0, vatPer=0, vatCharge=0, shippingCharge=0,discount=0,grand=0,vatRate=0;;
    Double totalAmount;
    private static final String TAG = ShippingDetailActivity.class.getName();
    OffersReceivers offersReceivers = new OffersReceivers();

    EditText edt_mobile_login;

    /***/
    //
    TextView  txt_deliver, txtTotalItems, pPrice, pMrp, totalItms, price,
            DeliveryCharge, Amounttotal, txt_totalPrice,textview_mobile_delivery,deliver_tv;

    TextView subTotalAmountf, couponPerTextf, couponAppliedf, totalf, vatPercentf, vatPercentAmountf, shippingChargesf, grandTotalAmountf;

    TableRow rowCoupon, totalRow;
    TableRow rowCouponf, totalRowf;

    TextView subTotalAmount, couponPerText, couponApplied, total,
            vatPercent, vatPercentAmount, shippingCharges, grandTotalAmount,shipping_address_tvs,payment_info_tvs,order_ids,delivery_tv;

    TextView txt_place_completed,complete_tv,txt_offer_shipping;

    TextView txt_order_subtotal,txt_order_shipping,txt_vatRate,txt_grand_total,txt_selected_payment,txt_order_coupon,txt_order_total,txt_order_coupon_percentage,txt_sales_tax_percentage;

    public static double amount =0;

    String user_id;

    ResponseCoupon coupon=new ResponseCoupon();

    boolean status=false;

    RecyclerView recycler_itemsList;

    TextView txt_custName,txt_order_address,txt_cust_mobile,tvTitle,tvShippingLabel,tvOfferLabel,
            tvPaymentLabel,tvPlaceOrderLabel,tvVoucherLimit;
    LinearLayout llCoupon,llOrderTotal;

    String couponType="";
    String promo="";

    Double subTotal, discountInPercentage, discountInAmount, total1, vatRate1, vatTotal, deliveryCharges, grandTotal;

    AddressAdapter addressAdapter;
    ArrayList<AddressModel> addressModels=new ArrayList<>();

    String shippingAddress="";

    CardView cvCouponLayout,cvBringTerminal,cvCreditFaciltiy;

    int backStack=1;

    ProgressDialog progressDialog;
    String id="";
    String shippingCountry,shippingState,shippingCity,shippingAddress1,shippingLatitude,shippingLongitude,shippingProvince,shippingAddressType="Home";
    String shippingCustomerId,shippingCustomerName,shippingCustomerMobile,shippingCustomerEmail;

    CoordinatorLayout cl_next_slide,cl_next_offer_slide,cl_confirmOrder_slide,cl_payment_slide;

    boolean isVoucherApplied=false;

    /***/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_detail);

        img_applyCoupon_arrow=findViewById(R.id.img_applyCoupon_arrow);
        session_management = new Session_management(ShippingDetailActivity.this);
        db = new DatabaseHandler(this);

        address = getIntent().getStringExtra("dId");
        supplierID=getIntent().getStringExtra("supplierID");
        cartID=getIntent().getStringExtra("cartID");
        vatRate=getIntent().getDoubleExtra("vatRate",5);
        //vatRate=5;
        Log.e("vatCreate",vatRate+"");
        /***/

        user_id = session_management.userId();

        role=session_management.role();

        /***/
        id_cod=findViewById(R.id.id_cod);
        id_ol=findViewById(R.id.id_ol);
        id_credit=findViewById(R.id.id_credit);
        id_terminal=findViewById(R.id.id_terminal);
        cvBringTerminal=findViewById(R.id.cvBringTerminal);
        cvCreditFaciltiy=findViewById(R.id.cvCreditFaciltiy);

        rv_address=findViewById(R.id.rv_address);
        llVoucherApplied=findViewById(R.id.llVoucherApplied);
        llApplyCoupon=findViewById(R.id.llApplyCoupon);
        tvCouponDescription=findViewById(R.id.tvCouponDescription);

        btn_next=findViewById(R.id.btn_next);
        btn_add_new_address=findViewById(R.id.btn_add_new_address);
        btn_next_offer=findViewById(R.id.btn_next_offer);
        offers_rv = findViewById(R.id.offers_rv);

        ll_stage_1=findViewById(R.id.ll_stage_1);
        ll_stage_2=findViewById(R.id.ll_stage_2);
        ll_stage_3=findViewById(R.id.ll_stage_3);
        ll_stage_4=findViewById(R.id.ll_stage_4);
        ll_apply_coupon=findViewById(R.id.ll_apply_coupon);

        txt_apply=findViewById(R.id.txt_apply);

        btn_payment_slide=findViewById(R.id.btn_payment_slide);
        btn_confirmOrder_slide=findViewById(R.id.btn_confirmOrder_slide);

        img_offer=findViewById(R.id.img_offer);
        img_payment=findViewById(R.id.img_payment);
        img_place_order=findViewById(R.id.img_place_order);

        view_stage1=findViewById(R.id.view_stage1);
        view_stage2=findViewById(R.id.view_stage2);
        view_stage3=findViewById(R.id.view_stage3);

        /***/
        edt_mobile_login=findViewById(R.id.edt_mobile_login);
        //subTotalAmount = findViewById(R.id.subTotalAmount);
        //subTotalAmountf = findViewById(R.id.subTotalAmountf);
        //subTotalAmountf.setText(""+CartActivity.tv_total.getText());
       // subTotalAmount.setText(""+CartActivity.tv_total.getText());
        rowCoupon = findViewById(R.id.rowCoupon);
        totalRow = findViewById(R.id.totalRow);
        total = findViewById(R.id.total);
        couponPerText = findViewById(R.id.couponPer);
        couponApplied = findViewById(R.id.couponApplied);
        vatPercent = findViewById(R.id.vatPercent);
        vatPercentAmount = findViewById(R.id.vatPercentAmount);
        shippingCharges = findViewById(R.id.shippingCharges);
        grandTotalAmount = findViewById(R.id.grandTotalAmount);

        txtTotalItems = findViewById(R.id.txtTotalItems);
        recycler_itemsList = findViewById(R.id.recycler_itemsList);
        txt_order_subtotal = findViewById(R.id.txt_order_subtotal);
        txt_order_shipping = findViewById(R.id.txt_order_shipping);
        txt_order_coupon = findViewById(R.id.txt_order_coupon);
        txt_order_coupon_percentage = findViewById(R.id.txt_order_coupon_percentage);
        txt_sales_tax_percentage = findViewById(R.id.txt_sales_tax_percentage);
        txt_order_total = findViewById(R.id.txt_order_total);
        llCoupon = findViewById(R.id.llCoupon);
        llOrderTotal = findViewById(R.id.llOrderTotal);
        txt_vatRate = findViewById(R.id.txt_vatRate);
        txt_grand_total = findViewById(R.id.txt_grand_total);
        txt_selected_payment = findViewById(R.id.txt_selected_payment);

        txt_custName = findViewById(R.id.txt_custName);
        txt_order_address = findViewById(R.id.txt_order_address);
        txt_cust_mobile = findViewById(R.id.txt_cust_mobile);

        btn_next_slide = findViewById(R.id.btn_next_slide);
        btn_next_offer_slide = findViewById(R.id.btn_next_offer_slide);
        cvCouponLayout = findViewById(R.id.cvCouponLayout);

        llBack = findViewById(R.id.llBack);
        tvTitle = findViewById(R.id.tvTitle);

        tvShippingLabel = findViewById(R.id.tvShippingLabel);
        tvOfferLabel = findViewById(R.id.tvOfferLabel);
        tvPaymentLabel = findViewById(R.id.tvPaymentLabel);
        tvPlaceOrderLabel = findViewById(R.id.tvPlaceOrderLabel);


        cl_next_slide = findViewById(R.id.cl_next_slide);
        cl_next_offer_slide = findViewById(R.id.cl_next_offer_slide);
        cl_confirmOrder_slide = findViewById(R.id.cl_confirmOrder_slide);
        cl_payment_slide = findViewById(R.id.cl_payment_slide);
        llVoucherLimit = findViewById(R.id.llVoucherLimit);
        tvVoucherLimit = findViewById(R.id.tvVoucherLimit);
        ivClose = findViewById(R.id.ivClose);

        //txt_order_subtotal.setText(""+CartActivity.tv_total.getText());

        addressSuccess = session_management.getUserDetails().get(KEY_NAME)+", "+session_management.getUserDetails().get(KEY_MOBILE) +", "+session_management.getUserDetails().get(ADDRESS);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        /***/

        id=session_management.getUserDetails().get(BaseURL.KEY_ID);

        llBack.setOnClickListener(view -> {
            onBackPressed();
        });

        tvTitle.setText("Shipping");

       // getExtraCharges();

       // getOffers();

        shippingCustomerName= session_management.getUserDetails().get(KEY_NAME);


        /*SHIPPING_CUSTOMER_Id*/
        if(session_management.getShippingAddress().get(SHIPPING_CUSTOMER_Id).isEmpty()){
            shippingCustomerId= session_management.getUserDetails().get(KEY_ID);
        }else{
            shippingCustomerId=session_management.getShippingAddress().get(SHIPPING_CUSTOMER_Id);
        }

        /*SHIPPING_Address*/
        if(session_management.getShippingAddress().get(SHIPPING_Address).isEmpty()){
            shippingAddress= session_management.getUserLandmark()+","+ session_management.getUserStreet()
                    +","+session_management.getUserCity() +","+session_management.getUserState();
        }else{
            shippingAddress=session_management.getShippingAddress().get(SHIPPING_Address);
        }

        /*SHIPPING_Address1*/
        if(session_management.getShippingAddress().get(SHIPPING_Address1).isEmpty()){
            shippingAddress1= session_management.getAddress();
        }else{
            shippingAddress1=session_management.getShippingAddress().get(SHIPPING_Address1);
        }

        /*SHIPPING_City*/
        if(session_management.getShippingAddress().get(SHIPPING_City).isEmpty()){
            shippingCity= session_management.getLocationCity();
        }else{
            shippingCity=session_management.getShippingAddress().get(SHIPPING_City);
        }

        /*SHIPPING_State*/
        if(session_management.getShippingAddress().get(SHIPPING_State).isEmpty()){
            shippingState= session_management.getUserState();
        }else{
            shippingState=session_management.getShippingAddress().get(SHIPPING_State);
        }

        /*SHIPPING_Country*/
        if(session_management.getShippingAddress().get(SHIPPING_Country).isEmpty()){
            shippingCountry= session_management.getCountry();
        }else{
            shippingCountry=session_management.getShippingAddress().get(SHIPPING_Country);
        }

        /*SHIPPING_Latitude*/
        if(session_management.getShippingAddress().get(SHIPPING_Latitude).isEmpty()){
            shippingLatitude= session_management.getLatPref();
        }else{
            shippingLatitude=session_management.getShippingAddress().get(SHIPPING_Latitude);
        }

        /*SHIPPING_Longitude*/
        if(session_management.getShippingAddress().get(SHIPPING_Longitude).isEmpty()){
            shippingLongitude= session_management.getLangPref();
        }else{
            shippingLongitude=session_management.getShippingAddress().get(SHIPPING_Longitude);
        }

        /*SHIPPING_CUSTOMER_Mobile*/
        if(session_management.getShippingAddress().get(SHIPPING_CUSTOMER_Mobile).isEmpty()){
            shippingCustomerMobile= session_management.getUserDetails().get(KEY_MOBILE);
        }else{
            shippingCustomerMobile=session_management.getShippingAddress().get(SHIPPING_CUSTOMER_Mobile);
        }

        /*SHIPPING_CUSTOMER_Email*/
        if(session_management.getShippingAddress().get(SHIPPING_CUSTOMER_Email).isEmpty()){
            shippingCustomerEmail= session_management.getUserDetails().get(KEY_EMAIL);
        }else{
            shippingCustomerEmail=session_management.getShippingAddress().get(SHIPPING_CUSTOMER_Email);
        }

        /*SHIPPING_Province*/
        if(session_management.getShippingAddress().get(SHIPPING_Province).isEmpty()){
            shippingProvince= session_management.getUserState();
        }else{
            shippingProvince=session_management.getShippingAddress().get(SHIPPING_Province);
        }

        //SHIPPING_AddressType
        if(session_management.getShippingAddress().get(SHIPPING_AddressType).isEmpty()){
            shippingAddressType="Home";
        }else{
            shippingAddressType=session_management.getShippingAddress().get(SHIPPING_AddressType);
        }
       // getProfileAddress();


        //String subTotal=getIntent().getStringExtra("subTotal");
        btn_add_new_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(ShippingDetailActivity.this, AddressLocationActivity2.class);
                intent.putExtra("addressType","NewAddress");
                startActivity(intent);
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_offer.setImageResource(R.drawable.offer_active_new);

                view_stage1.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(
                        ShippingDetailActivity.this, R.color.colorPrimary)));

                ll_stage_1.setVisibility(View.GONE);
                ll_stage_2.setVisibility(View.VISIBLE);
                btn_next.setVisibility(View.GONE);

            }
        });

        btn_next_slide.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(@NotNull SlideToActView slideToActView) {

                if( addressModels==null || addressModels.isEmpty() ){
                    btn_next_slide.resetSlider();
                    Toast.makeText(ShippingDetailActivity.this, "Please Add Address", Toast.LENGTH_SHORT).show();
                }else{
                    couponAndOffer();
                }

            }
        });

        cl_next_slide.setOnClickListener(view -> {
            if( addressModels==null || addressModels.isEmpty() ){
               // btn_next_slide.resetSlider();
                Toast.makeText(ShippingDetailActivity.this, "Please Add Address", Toast.LENGTH_SHORT).show();
            }else{
                couponAndOffer();
            }
        });

        btn_next_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                img_payment.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(
                        ShippingDetailActivity.this, R.color.colorPrimary)));

                view_stage2.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(
                        ShippingDetailActivity.this, R.color.colorPrimary)));

                ll_stage_2.setVisibility(View.GONE);
                ll_stage_3.setVisibility(View.VISIBLE);
            }
        });

        btn_next_offer_slide.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(@NotNull SlideToActView slideToActView) {
                getOrderCalculation();

                setPaymentMethod();

            }
        });

        cl_next_offer_slide.setOnClickListener(view -> {
            getOrderCalculation();
            setPaymentMethod();
        });

        btn_payment_slide.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(@NotNull SlideToActView slideToActView) {

                if (payment_method.isEmpty())
                {
                    btn_payment_slide.resetSlider();
                    Toast.makeText(ShippingDetailActivity.this, "Please select payment method", Toast.LENGTH_SHORT).show();
                }else{
                    confirmOrder();
                }
            }
        });

        cl_payment_slide.setOnClickListener(view -> {
            if (payment_method.isEmpty())
            {
                //btn_payment_slide.resetSlider();
                Toast.makeText(ShippingDetailActivity.this, "Please select payment method", Toast.LENGTH_SHORT).show();
            }else{
                confirmOrder();
            }
        });

        btn_confirmOrder_slide.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(@NotNull SlideToActView slideToActView) {

                int totalQuantity = db.getCartCount();

                Log.e("OrderData",
                "custID"+":"+ session_management.getUserDetails().get(KEY_ID)+"\n"+
                "OrderStatus"+":"+ "" + totalQuantity+"\n"+
                "SubTotal"+":"+ String.format("%.2f", subTotal)+"\n"+
                "Total"+":"+ String.format("%.2f", total1)+"\n"+
                "FirstName"+":"+ session_management.getUserDetails().get(KEY_NAME)+"\n"+
                "Mobile"+":"+ session_management.getUserDetails().get(KEY_MOBILE)+"\n"+
                "email"+":"+ session_management.getUserDetails().get(KEY_EMAIL)+"\n"+
                "AddressLine1"+":"+ shippingAddress+"\n"+
                "City"+":"+ shippingCity+"\n"+
                "Province"+":"+ shippingProvince+"\n"+
                "AddressType"+":"+ shippingAddressType+"\n"+
                "country"+":"+ shippingCountry+"\n"+
                "DeviceName"+":"+"Android"+"\n"+
                "OrderTransactionType"+":"+OrderTransactionType+"\n"+
                "latitude"+":"+ shippingLatitude+"\n"+
                "longitude"+":"+ shippingLongitude+"\n"+
                "tax"+":"+ String.format("%.2f", vatTotal)+"\n"+
                "shipping"+":"+ String.format("%.2f", deliveryCharges)+"\n"+
                "discount"+":"+String.format("%.2f", discountInAmount)+"\n"+
                "grandtotal"+":"+String.format("%.2f", grandTotal)+"\n"+
                "appVersion"+":"+ BuildConfig.VERSION_NAME+"\n"+
                        "CMID"+":"+""+"\n"+
                        "CMCode"+":"+""+"\n"+
                        "Promo"+":"+couponCodeText+"\n"+
                        "couponType"+":"+""+"\n"+
                        "DecidedExisitingLimit"+":"+"");



                if(OrderTransactionType.equals("CC"))
                {

                    callNetworkActivity(totalQuantity);

                    /*Intent intent=new Intent(ShippingDetailActivity.this,NetworkPaymentActivity.class);
                    intent.putExtra("SubTotal",subTotal);//double
                    intent.putExtra("discountInAmount",discountInAmount);//double
                    intent.putExtra("OrderStatus",totalQuantity);
                    intent.putExtra("TYPE_COUPON",TYPE_COUPON);
                    intent.putExtra("vatTotal",vatTotal);//double
                    intent.putExtra("cmcode",cmcode);
                    intent.putExtra("deliveryCharges",deliveryCharges);//double
                    intent.putExtra("total1",total1);//double
                    intent.putExtra("shippingCountry",shippingCountry);
                    intent.putExtra("grandTotal",grandTotal);//double
                    intent.putExtra("nextlimit",nextlimit);
                    intent.putExtra("shippingAddress",shippingAddress);
                    intent.putExtra("isCouponID",cmid);
                    intent.putExtra("shippingCity",shippingCity);
                    intent.putExtra("shippingLatitude",shippingLatitude);
                    intent.putExtra("shippingLongitude",shippingLongitude);
                    intent.putExtra("couponCodeText",couponCodeText);
                    intent.putExtra("isCouponApplied",isCouponApplied);
                    intent.putExtra("cartID",cartID);
                    startActivity(intent);
                    finish();*/
                }
                else {
                    continueUrl(totalQuantity, couponType,OrderTransactionType);
                }
            }
        });

        cl_confirmOrder_slide.setOnClickListener(view -> {

            new MainDrawerActivity().loadFragment(new OrderFragment());

            int totalQuantity = db.getCartCount();
            if(OrderTransactionType.equals("CC"))
            {
                callNetworkActivity(totalQuantity);
                Intent intent=new Intent(ShippingDetailActivity.this,NetworkPaymentActivity.class);
                intent.putExtra("SubTotal",subTotal);//double
                intent.putExtra("discountInAmount",discountInAmount);//double
                intent.putExtra("OrderStatus",totalQuantity);
                intent.putExtra("TYPE_COUPON",TYPE_COUPON);
                intent.putExtra("vatTotal",vatTotal);//double
                intent.putExtra("cmcode",cmcode);
                intent.putExtra("deliveryCharges",deliveryCharges);//double
                intent.putExtra("total1",total1);//double
                intent.putExtra("shippingCountry",shippingCountry);
                intent.putExtra("grandTotal",grandTotal);//double
                intent.putExtra("nextlimit",nextlimit);
                intent.putExtra("shippingAddress",shippingAddress);
                intent.putExtra("isCouponID",cmid);
                intent.putExtra("shippingCity",shippingCity);
                intent.putExtra("shippingLatitude",shippingLatitude);
                intent.putExtra("shippingLongitude",shippingLongitude);
                intent.putExtra("couponCodeText",couponCodeText);
                intent.putExtra("isCouponApplied",isCouponApplied);
                intent.putExtra("cartID",session_management.getCartId());
                startActivity(intent);
                finish();
            }
            else {
                continueUrl(totalQuantity, couponType,OrderTransactionType);
            }
        });


        /***/



        if (session_management.isFirstCouponUsed()) {

            //updateData();
        } else {

            firstCoupon();
        }


        /***/


        final TapTargetSequence sequence = new TapTargetSequence(this)
                .targets(
                        // Likewise, this tap target will target the search button
                        TapTarget.forView(img_offer,"offer", "Your Custom Text to explain the target view.")
                                .dimColor(R.color.colorPrimary)
                                .outerCircleColor(R.color.colorAccent)
                                .targetCircleColor(R.color.colorPrimary)
                                .textColor(android.R.color.black)
                                .cancelable(false)
                                .id(1)
                )
                .listener(new TapTargetSequence.Listener() {
                    // This listener will tell us when interesting(tm) events happen in regards
                    // to the sequence
                    @Override
                    public void onSequenceFinish() {
                        // Executes when sequence of instruction get completes.
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                        Log.d("TapTargetView", "Clicked on " + lastTarget.id());
                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                        final AlertDialog dialog = new AlertDialog.Builder(ShippingDetailActivity.this)
                                .setTitle("Uh oh")
                                .setMessage("You canceled the sequence")
                                .setPositiveButton("OK", null).show();
                        TapTargetView.showFor(dialog,
                                TapTarget.forView(dialog.getButton(DialogInterface.BUTTON_POSITIVE), "Uh oh!", "You canceled the sequence at step " + lastTarget.id())
                                        .cancelable(false)
                                        .tintTarget(false), new TapTargetView.Listener() {
                                    @Override
                                    public void onTargetClick(TapTargetView view) {
                                        super.onTargetClick(view);
                                        dialog.dismiss();
                                    }
                                });
                    }
                });

        //sequence.start();



    }


    private void callNetworkActivity(int totalQuantity){
        Intent intent=new Intent(ShippingDetailActivity.this,NetworkPaymentActivity.class);
        intent.putExtra("SubTotal",subTotal);//double
        intent.putExtra("discountInAmount",discountInAmount);//double
        intent.putExtra("OrderStatus",totalQuantity);
        intent.putExtra("TYPE_COUPON",couponType);
        intent.putExtra("vatTotal",vatTotal);//double
        intent.putExtra("cmcode",cmcode);
        intent.putExtra("deliveryCharges",deliveryCharges);//double
        intent.putExtra("total1",total1);//double
        intent.putExtra("shippingCountry",shippingCountry);
        intent.putExtra("grandTotal",grandTotal);//double
        intent.putExtra("grandSuccess",grandSuccess);//double
        intent.putExtra("nextlimit",nextlimit);
        intent.putExtra("shippingAddress",shippingAddress);
        intent.putExtra("isCouponID",cmid);
        intent.putExtra("shippingCity",shippingCity);
        intent.putExtra("shippingLatitude",shippingLatitude);
        intent.putExtra("shippingLongitude",shippingLongitude);
        intent.putExtra("couponCodeText",couponCodeText);
        intent.putExtra("isCouponApplied",isCouponApplied);
        intent.putExtra("cartID",cartID);
        intent.putExtra("shippingProvince",shippingProvince);
        intent.putExtra("shippingAddressType",shippingAddressType);
        startActivity(intent);
        finish();
    }


    /***/



    public void checkCoupon() {
        progressDialog.show();

        HashMap params = new HashMap();
        params.put("BranchCode", "B001");
        params.put("amount", String.valueOf(db.getTotalAmount()));
        params.put("CouponCode", edt_mobile_login.getText().toString());
        params.put("custID", user_id);

        Volley.newRequestQueue(this).add(
                new StringRequest(Request.Method.POST,
                        ApiBaseURL.checkCoupon,
                        new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("CouponReponse",response);
                                progressDialog.dismiss();
                                Gson gson = new Gson();
                                coupon = gson.fromJson(response, ResponseCoupon.class);

                                if (coupon.status) {

                                    isCouponApplied = true;
                                    couponPer = coupon.result.get(0).discountValue;
                                    // vatCharge = coupon.result.get(0).vatCharge;

                                    couponCodeText = coupon.result.get(0).cmCode;
                                    Toast.makeText(ShippingDetailActivity.this, coupon.message, Toast.LENGTH_SHORT).show();
                                    //offer_rv_layout.setVisibility(View.GONE);
                                   // couponLayout.setVisibility(View.VISIBLE);
                                    //updateData();
                                    //couponType = coupon.result.get(0).couponType;
                                    couponType = "coupon";
                                    promo = coupon.result.get(0).cmCode;
                                    cmcode = coupon.result.get(0).cmCode;
                                    offers_rv.setVisibility(View.GONE);
                                    img_applyCoupon_arrow.setVisibility(View.GONE);
                                    //llApplyCoupon.setVisibility(View.GONE);
                                    //llCouponApplied.setVisibility(View.VISIBLE);
                                    //tvCouponDescription.setText(coupon.result.get(0).cmDescription);

                                } else {

                                    Toast.makeText(ShippingDetailActivity.this, coupon.message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                error.printStackTrace();
                                progressDialog.dismiss();
                            }
                        }){

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Log.e("CouponPar",params.toString());
                        return params;
                    }
                }
        );
    }

    public void checkVoucher(String couponCode,String custId) {
        progressDialog.show();

        HashMap params = new HashMap();
        params.put("BranchCode", "B001");
        params.put("amount", String.valueOf(db.getTotalAmount()));
        params.put("CouponCode", couponCode);
        params.put("custID", custId);

        Volley.newRequestQueue(this).add(
                new StringRequest(Request.Method.POST,
                        ApiBaseURL.checkCoupon,
                        new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("CouponReponse",response);
                                progressDialog.dismiss();
                                Gson gson = new Gson();
                                coupon = gson.fromJson(response, ResponseCoupon.class);

                                if (coupon.status) {

                                    isVoucherApplied=true;
                                    isCouponApplied = true;
                                    couponPer = coupon.result.get(0).discountValue;
                                    // vatCharge = coupon.result.get(0).vatCharge;

                                    couponCodeText = coupon.result.get(0).cmCode;
                                    Toast.makeText(ShippingDetailActivity.this, coupon.message, Toast.LENGTH_SHORT).show();
                                    //offer_rv_layout.setVisibility(View.GONE);
                                    // couponLayout.setVisibility(View.VISIBLE);
                                    //updateData();
                                    //couponType = coupon.result.get(0).couponType;
                                    //couponType = coupon.result.get(0).couponType;
                                    couponType = "coupon";
                                    promo = coupon.result.get(0).cmCode;
                                    cmcode = coupon.result.get(0).cmCode;
                                    cmid = coupon.result.get(0).cmid;
                                    nextlimit = coupon.result.get(0).limitExisitingUserUses;
                                    offers_rv.setVisibility(View.GONE);
                                    llApplyCoupon.setVisibility(View.GONE);
                                    llVoucherApplied.setVisibility(View.VISIBLE);
                                    tvCouponDescription.setText(coupon.result.get(0).cmDescription);

                                } else {

                                    offers_rv.setVisibility(View.GONE);
                                    llApplyCoupon.setVisibility(View.GONE);
                                    llVoucherApplied.setVisibility(View.GONE);
                                    llVoucherLimit.setVisibility(View.VISIBLE);
                                    tvVoucherLimit.setText(coupon.message);
                                    ivClose.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            offers_rv.setVisibility(View.VISIBLE);
                                            llApplyCoupon.setVisibility(View.VISIBLE);
                                            llVoucherLimit.setVisibility(View.GONE);
                                        }
                                    });
                                    Toast.makeText(ShippingDetailActivity.this, coupon.message, Toast.LENGTH_SHORT).show();
                                }

                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                error.printStackTrace();
                                progressDialog.dismiss();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Log.e("CouponPar",params.toString());
                        return params;
                    }
                }
        );
    }

    public void firstCoupon() {

        HashMap params = new HashMap();
        params.put("BranchCode", "B001");
        params.put("amount", String.valueOf(db.getTotalAmount()));
        params.put("custID", user_id);

        Volley.newRequestQueue(this).add(
                new StringRequest(Request.Method.POST,
                        ApiBaseURL.firstCoupon,
                        new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Gson gson = new Gson();
                                coupon = gson.fromJson(response, ResponseCoupon.class);

                                if (coupon.status) {

                                    isCouponApplied = true;
                                    couponPer = coupon.result.get(0).discountValue;
                                    edt_mobile_login.setText(coupon.result.get(0).cmCode);
                                    cmcode = coupon.result.get(0).cmCode;
                                    cmid = coupon.result.get(0).cmid;
                                    nextlimit = String.valueOf(coupon.nextLimit);
                                    edt_mobile_login.setEnabled(false);
                                    txt_apply.setEnabled(false);

                                    //updateData();

                                } else {
                                   // updateData();
                                    //  Toast.makeText(OrderSummary.this, coupon.message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                error.printStackTrace();
                            }
                        }){

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        return params;
                    }
                }
        );
    }

    private void checkOffers() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiBaseURL.getOffersActive, response -> {
            Log.d("HomeTopSelling", response);
            try {
                JSONObject jsonObjectResponse = new JSONObject(response);
                boolean statuss = jsonObjectResponse.getBoolean("status");
                status = statuss;

                getOffers();
            }
            catch (JSONException e) {
                e.printStackTrace();
            } finally {



            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        requestQueue.getCache().clear();
        stringRequest.setRetryPolicy(new RetryPolicy() {
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
        requestQueue.add(stringRequest);
    }

    /***/

    private void getOffers() {
        Log.e("backStack",backStack+"");
        progressDialog.show();
        //offers_rv.setVisibility(View.GONE);
        list.clear();
        ServiceGenrator.getApiInterface().getLifetimeOffers(ApiInterface.branchcode).enqueue(
                new Callback<ResponseLifetimeOffers>() {
                    @Override
                    public void onResponse(Call<ResponseLifetimeOffers> call, Response<ResponseLifetimeOffers> response) {

                        if (response.isSuccessful()) {

                            if (response.body().status) {
                                 progressDialog.dismiss();
                                //offers_rv.setVisibility(View.VISIBLE);
                                list.addAll(response.body().getResult());
                                total_atm = String.valueOf(db.getTotalAmount());
                                totalAmount = Double.parseDouble(total_atm);
                                SharedPreferences preferences = getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                                vatPer = Double.parseDouble(preferences.getString("vatRate", "0"));
                                shippingCharge = Double.parseDouble(preferences.getString("deliveryCharges", "0"));
                                adapter = new LifetimeOffersAdapter(ShippingDetailActivity.this, list);
                                adapter.setProductDetails(totalAmount, vatPer, shippingCharge);
                                offers_rv.setAdapter(adapter);
                                offers_rv.setAnimation(null);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseLifetimeOffers> call, Throwable t) {

                    }
                }
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter  = new IntentFilter("offers");
//        registerReceiver(offersReceivers, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (offersReceivers!= null)
        {
            unregisterReceiver(offersReceivers);
        }
    }


    public void getAddress(String userID){

        ServiceGenrator.getApiInterface().getCustomerAddresses(userID).enqueue(
                new Callback<ResAddress>() {
                    @Override
                    public void onResponse(Call<ResAddress> call, Response<ResAddress> response) {

                        if (response.isSuccessful()) {

                            if (response.body().isStatus())
                            {
                                addressModels.addAll(response.body().getResult());
                                Log.e("AddressModels",addressModels.toString());
                               addressAdapter.notifyDataSetChanged();

                            }
                            else {
                               // rv_address.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResAddress> call, Throwable t) {

                    }
                });

    }



    private void continueUrl(final int totalItems, String couponType, String OrderTransType){

        Log.e(TAG, "continueUrl: "+OrderTransType );
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiBaseURL.OrderContinue, new com.android.volley.Response.Listener<String>() {
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
                        if (isCouponApplied) {
                            updateOrderStatus( orderCode,session_management.getUserDetails().get(KEY_ID),"1",
                                    OrderTransType,cmcode,
                                    cmcode, couponType,
                                    cmid,nextlimit);
                        }
                        else
                        {
                            updateOrderStatus( orderCode,session_management.getUserDetails().get(KEY_ID),"1",
                                    OrderTransType,couponCodeText,
                                    "", "",
                                    "","0");
                        }

                        OrderPlacedSuccessDialog(orderRef,orderDate,grandSuccess,msg);
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        //btn_confirmOrder_slide.resetSlider();
                        DatabaseHandler db = new DatabaseHandler(ShippingDetailActivity.this);
                        db.clearCart();
                        Intent intent1 =new Intent(ShippingDetailActivity.this,MainDrawerActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent1.putExtra("loadFrag",2);
                        startActivity(intent1);
                        finish();
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
                /*{country=India, appVersion=0.0.4, FirstName=Testuser5  malbari, CMCode=, latitude=16.703116,
                        discount=0.00, tax=8.14, City=Kolhapur, OrderTransactionType=COD, SubTotal=162.75,
                        Mobile=0565898186, OrderStatus=7, AddressLine1=Abu Dhabi Add  ,Abu Dhabi ,Kolhapur,Abu Dhabi, Promo=,
                        shipping=20.00, couponType=, Total=162.75, DecidedExisitingLimit=0, custID=42, grandtotal=190.89, CMID=,
                        email=shahnawaz.malbari@gmail.com, DeviceName=Android, longitude=74.240786}*/
                param.put("custID", session_management.getUserDetails().get(KEY_ID));
                param.put("OrderStatus", "" + totalItems);
                param.put("SubTotal", String.format("%.2f", subTotal));
                param.put("Total", String.format("%.2f", total1));
                param.put("FirstName", session_management.getUserDetails().get(KEY_NAME));
                param.put("Mobile", session_management.getUserDetails().get(KEY_MOBILE));
                param.put("email", session_management.getUserDetails().get(KEY_EMAIL));
                param.put("AddressLine1", shippingAddress);
                param.put("City", shippingCity);
                param.put("country", shippingCountry);
                param.put("DeviceName","Android");
                param.put("OrderTransactionType",""+OrderTransType);
                param.put("latitude", shippingLatitude);
                param.put("longitude", shippingLongitude);
                param.put("tax", String.format("%.2f", vatTotal));
                param.put("shipping", String.format("%.2f", deliveryCharges));
                param.put("discount",String.format("%.2f", discountInAmount));
                param.put("grandtotal",String.format("%.2f", grandTotal));
                param.put("appVersion", BuildConfig.VERSION_NAME);
                param.put("Province", shippingProvince);
                param.put("AddressType", shippingAddressType);

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
        RequestQueue requestQueue = Volley.newRequestQueue(ShippingDetailActivity.this);
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



    public void getOrderCalculation(){
        //progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiBaseURL.OrderCalculation, response -> {
            Log.e("OrderCalculation", ""+response);
            try {
                JSONObject jsonObjectResponse = new JSONObject(response);

                boolean status = jsonObjectResponse.getBoolean("status");

                if (status) {


                    Gson gson = new Gson();

                    OrderCalculationModel orderCalculationModel = gson.fromJson(jsonObjectResponse.getString("result"), OrderCalculationModel.class);

                    subTotal = orderCalculationModel.getSubTotal();
                    discountInPercentage = orderCalculationModel.getDiscountInPercentage();
                    discountInAmount = orderCalculationModel.getDiscountInAmount();
                    total1 = orderCalculationModel.getTotal();
                    vatRate1 = orderCalculationModel.getVatRate();
                    vatTotal = orderCalculationModel.getVatTotal();
                    deliveryCharges = orderCalculationModel.getDeliveryCharges();
                    grandTotal = orderCalculationModel.getGrandTotal();

                }
                else if(!status)
                {
                   // noData.setVisibility(View.VISIBLE);
                   // assign_recy.setVisibility(View.GONE);
                   // progressDialog.dismiss();
                }
            } catch (JSONException e) {
               // progressDialog.dismiss();
                e.printStackTrace();
            } finally {

            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                //progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("custID", session_management.getUserDetails().get(KEY_ID));
                //params.put("custID", "42");
                params.put("couponType", couponType);
                params.put("Promo", promo);
                Log.e("OrderCalculationPAra", ""+params);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        stringRequest.setRetryPolicy(new RetryPolicy() {
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
        requestQueue.add(stringRequest);
    }

    private void ConfirmOrderBackPressDialog(){
        Dialog bottomSheetDialog=new Dialog(ShippingDetailActivity.this);
        bottomSheetDialog.setContentView(R.layout.item_layout_bottom_confirmorder);
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);

        MaterialButton btn_orderConfirm_yes=bottomSheetDialog.findViewById(R.id.btn_orderConfirm_yes);
        MaterialButton btn_orderConfirm_no=bottomSheetDialog.findViewById(R.id.btn_orderConfirm_no);

        bottomSheetDialog.getWindow().setLayout(width,height);
        bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        bottomSheetDialog.getWindow().getAttributes().windowAnimations =  R.style.DialogAnimation;
        bottomSheetDialog.show();

        btn_orderConfirm_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                img_place_order.setImageResource(R.drawable.order_stage_4);
                tvPlaceOrderLabel.setTextColor(getResources().getColor(R.color.mediumGrey));
                view_stage3.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(ShippingDetailActivity.this, R.color.mediumGrey)));
                ll_stage_4.setVisibility(View.GONE);
                btn_confirmOrder_slide.resetSlider();
                btn_confirmOrder_slide.setVisibility(View.GONE);
                cl_confirmOrder_slide.setVisibility(View.GONE);
                ll_stage_3.setVisibility(View.VISIBLE);
                tvTitle.setText("Payment");
                btn_payment_slide.resetSlider();
                backStack = 3;
            }
        });

        btn_orderConfirm_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.dismiss();
                int totalQuantity = db.getCartCount();

                if(OrderTransactionType.equals("CC"))
                {
                    callNetworkActivity(totalQuantity);
                }
                else {
                    continueUrl(totalQuantity,couponType,OrderTransactionType);
                }
            }
        });
    }

    /*On Address item click*/
    @Override
    public void onItemClick(int position, ArrayList<AddressModel> addressModels) {
//        session_management.setShippingAddressPosition(position);
        addressAdapter.showCheckedImage(position);
        shippingAddress= addressModels.get(position).getCusAdd1()+","+ addressModels.get(position).getCusAdd2()+","+addressModels.get(position).getCity() +","+addressModels.get(position).getState();
        //val position = recyclerView.getChildLayoutPosition(view)
        rv_address.smoothScrollToPosition(position);

        shippingCustomerId=addressModels.get(position).getCustID();
        shippingAddress1=addressModels.get(position).getCusAdd1();
        shippingCity=addressModels.get(position).getCity();
        shippingState=addressModels.get(position).getState();
        shippingCountry=addressModels.get(position).getCountry();
        shippingLatitude= addressModels.get(position).getLatitude();
        shippingLongitude=addressModels.get(position).getLongitude();
        shippingCustomerMobile=addressModels.get(position).getCusMob();
        shippingCustomerEmail=addressModels.get(position).getCusEmail();
        shippingProvince=addressModels.get(position).getState();
        shippingAddressType=addressModels.get(position).getCsdTypeName();

        session_management.saveShippingAddress(addressModels.get(position).getCustID(),
                shippingAddress,addressModels.get(position).getCusAdd1(),
                addressModels.get(position).getCity(),addressModels.get(position).getState(), addressModels.get(position).getCountry(),
                addressModels.get(position).getLatitude(),addressModels.get(position).getLongitude(),addressModels.get(position).getCusMob(),
                addressModels.get(position).getCusEmail(),addressModels.get(position).getState(),addressModels.get(position).getCsdTypeName());
    }

    private void showNewAddress(int position)
    {
//        session_management.setShippingAddressPosition(position);
        addressAdapter.showCheckedImage(position);
        shippingAddress= addressModels.get(position).getCusAdd1()+","+ addressModels.get(position).getCusAdd2()+","+addressModels.get(position).getCity() +","+addressModels.get(position).getState();

        rv_address.smoothScrollToPosition(position);

        shippingCustomerId=addressModels.get(position).getCustID();
        shippingAddress1=addressModels.get(position).getCusAdd1();
        shippingCity=addressModels.get(position).getCity();
        shippingState=addressModels.get(position).getState();
        shippingCountry=addressModels.get(position).getCountry();
        shippingLatitude= addressModels.get(position).getLatitude();
        shippingLongitude=addressModels.get(position).getLongitude();
        shippingCustomerMobile=addressModels.get(position).getCusMob();
        shippingCustomerEmail=addressModels.get(position).getCusEmail();
        shippingProvince=addressModels.get(position).getState();
        shippingAddressType=addressModels.get(position).getCsdTypeName();
        session_management.saveShippingAddress(addressModels.get(position).getCustID(),
                shippingAddress,addressModels.get(position).getCusAdd1(),
                addressModels.get(position).getCity(),addressModels.get(position).getState(), addressModels.get(position).getCountry(),
                addressModels.get(position).getLatitude(),addressModels.get(position).getLongitude(),addressModels.get(position).getCusMob(),
                addressModels.get(position).getCusEmail(),addressModels.get(position).getState(),addressModels.get(position).getCsdTypeName());
    }


    public class OffersReceivers extends BroadcastReceiver
    {

        @SuppressLint("NewApi")
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();

            if (bundle!= null)
            {
                String msg  = bundle.getString("msg");
                double amount;
                double vatPers, vatCharges, shippingCharges, discounts, grands,totalAmounts;
                String discountValue;

                amount=bundle.getDouble("subtotal");
                discounts = bundle.getDouble("coupon");
                discountValue =bundle.getString("couponper");
                totalAmounts = bundle.getDouble("total");
                vatCharges = bundle.getDouble("vat");
                vatPers = bundle.getDouble("vatper");
                shippingCharges = bundle.getDouble("shipping");
                grands = bundle.getDouble("grand_total");
                cmid = bundle.getString("cmid");
                cmcode = bundle.getString("cmcode");
                nextlimit = bundle.getString("nextlimit");
                couponDescription = bundle.getString("description");

                couponPer=Double.parseDouble(discountValue);
                vatPer=vatPers;
                // vatCharge=vatCharges;
                shippingCharge=shippingCharges;
                discount=discounts;
                grand=grands;

                couponType ="offer";
                promo = cmcode;

                //  subTotalAmountf.setText("AED " + String.format("%.2f", amount));

              /*  couponPerTextf.setText("Coupon " + String.format("%.2f", Double.parseDouble(discountValue)) + "%");

                couponAppliedf.setText("AED " + String.format("%.2f", discounts));

                totalf.setText("AED " + String.format("%.2f", totalAmounts));

                vatPercentf.setText("VAT " + String.format("%.2f", vatPers) + "%");

                //vatPercentAmountf.setText("AED " + String.format("%.2f", vatCharges));

                shippingChargesf.setText("AED " + String.format("%.2f", shippingCharges));

                grandTotalAmountf.setText("AED " + String.format("%.2f", grands));

                rowCouponf.setVisibility(View.VISIBLE);
                totalRowf.setVisibility(View.VISIBLE);

                couponLayout.setVisibility(View.GONE);
                offer_rv_layout.setVisibility(View.VISIBLE);
*/
                isCouponApplied = true;
                cvCouponLayout.setVisibility(View.GONE);
                //updateData();

            }
        }
    }

    private void couponAndOffer(){
        img_offer.setImageResource(R.drawable.offer_active_new);
        tvOfferLabel.setTextColor(getResources().getColor(R.color.colorPrimary));

        view_stage1.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(
                ShippingDetailActivity.this, R.color.colorPrimary)));

        ll_stage_1.setVisibility(View.GONE);
        btn_next.setVisibility(View.GONE);
        btn_next_slide.setVisibility(View.GONE);
        cl_next_slide.setVisibility(View.GONE);
        tvTitle.setText("Offer");


        ll_stage_2.setVisibility(View.VISIBLE);
       // btn_next_offer_slide.setVisibility(View.VISIBLE);
        cl_next_offer_slide.setVisibility(View.VISIBLE);
        backStack = 2;



        img_applyCoupon_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_applyCoupon_arrow.setVisibility(View.GONE);
                ll_apply_coupon.setVisibility(View.VISIBLE);
                txt_apply.setVisibility(View.VISIBLE);
            }
        });

        txt_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edt_mobile_login.getText().toString().isEmpty()){
                    Toast.makeText(ShippingDetailActivity.this, "Coupon code required", Toast.LENGTH_SHORT).show();
                }else{
                    checkCoupon();
                }

            }
        });

        checkOffers();

        if(!session_management.getCouponCode().isEmpty() && !session_management.getCouponType().isEmpty()){
            //checkVoucher(session_management.getCouponCode(),"42");
            checkVoucher(session_management.getCouponCode(),user_id);
        }
    }

    private void setPaymentMethod(){
        img_payment.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(ShippingDetailActivity.this, R.color.colorPrimary)));

        view_stage2.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(ShippingDetailActivity.this, R.color.colorPrimary)));
        ll_stage_2.setVisibility(View.GONE);
        btn_next_offer_slide.setVisibility(View.GONE);
        cl_next_offer_slide.setVisibility(View.GONE);
        ll_stage_3.setVisibility(View.VISIBLE);
        tvTitle.setText("Payment");
        tvPaymentLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
        backStack = 3;

        Log.e("Role",role);
        if(role.equals("supplier")){
            //cvBringTerminal.setVisibility(View.VISIBLE);
            cvCreditFaciltiy.setVisibility(View.VISIBLE);
            //view_credit.setVisibility(View.VISIBLE);
        }else{
           // cvBringTerminal.setVisibility(View.GONE);
            cvCreditFaciltiy.setVisibility(View.GONE);
        }

        id_cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(id_ol.isChecked()){
                    id_ol.setChecked(false);
                }
                else if(id_credit.isChecked()){
                    id_credit.setChecked(false);
                }
                else if(id_terminal.isChecked()){
                    id_terminal.setChecked(false);
                }
                id_cod.setChecked(true);
                payment_method  = "COD";
                OrderTransactionType="COD";
                txt_selected_payment.setText(id_cod.getText().toString());

                //  Toast.makeText(ShippingDetailActivity.this, payment_method, Toast.LENGTH_SHORT).show();
            }
        });

        id_ol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(id_cod.isChecked()){
                    id_cod.setChecked(false);
                }
                else if(id_credit.isChecked()){
                    id_credit.setChecked(false);
                }
                else if(id_terminal.isChecked()){
                    id_terminal.setChecked(false);
                }
                id_ol.setChecked(true);
                payment_method  = "ONLINE";
                OrderTransactionType="CC";
                txt_selected_payment.setText(id_ol.getText().toString());
                //  Toast.makeText(ShippingDetailActivity.this, payment_method, Toast.LENGTH_SHORT).show();
            }
        });

        id_credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id_ol.isChecked()){
                    id_ol.setChecked(false);
                }
                else if(id_cod.isChecked()){
                    id_cod.setChecked(false);
                }
                else if(id_terminal.isChecked()){
                    id_terminal.setChecked(false);
                }
                id_credit.setChecked(true);
                payment_method  = "C";
                OrderTransactionType="C";
                txt_selected_payment.setText(id_credit.getText().toString());
                //  Toast.makeText(ShippingDetailActivity.this, payment_method, Toast.LENGTH_SHORT).show();
            }
        });

        id_terminal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id_ol.isChecked()){
                    id_ol.setChecked(false);
                }
                else if(id_credit.isChecked()){
                    id_credit.setChecked(false);
                }
                else if(id_cod.isChecked()){
                    id_cod.setChecked(false);
                }
                id_terminal.setChecked(true);
                payment_method  = "BT";
                OrderTransactionType="BT";
                //id_terminal.setText(id_terminal.getText().toString());
                txt_selected_payment.setText(id_terminal.getText().toString());
                //  Toast.makeText(ShippingDetailActivity.this, payment_method, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmOrder(){

        img_place_order.setImageResource(R.drawable.order_placed_active_new);

        view_stage3.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(
                ShippingDetailActivity.this, R.color.colorPrimary)));


        ll_stage_3.setVisibility(View.GONE);
        ll_stage_4.setVisibility(View.VISIBLE);
        //btn_confirmOrder_slide.setVisibility(View.VISIBLE);
        cl_confirmOrder_slide.setVisibility(View.VISIBLE);
        tvTitle.setText("Place Order");
        tvPlaceOrderLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
        backStack = 4;

        txtTotalItems.setText("Basket Items (" + db.getCartCount()+")");

        //ArrayList<HashMap<String, String>> map = db.getInStockCartItem();
        ArrayList<HashMap<String, String>> map = db.getCartAll();
        ArrayList<HashMap<String, String>> map1 = new ArrayList<>();
        for(int i=0;i<map.size();i++){
            if (map.get(i).get("stock") != null && map.get(i).get("stock").equals("Stock")) {
                map1.add(map.get(i));
            }
        }
        ImageAdapterData1 adapters = new ImageAdapterData1(ShippingDetailActivity.this, map1);
        recycler_itemsList.setLayoutManager(new LinearLayoutManager(ShippingDetailActivity.this, LinearLayoutManager.VERTICAL, false));
        recycler_itemsList.setAdapter(adapters);


        /**Set Order details*/
        /*set sub total*/
        txt_order_subtotal.setText("AED " + String.format("%.2f", subTotal));
        subTotalSuccess=txt_order_subtotal.getText().toString();
        /*Set discount percentage*/
        if(isVoucherApplied){
            txt_order_coupon_percentage.setText("Coupon ");
        }else{
            txt_order_coupon_percentage.setText("Coupon " + String.format("%.2f", discountInPercentage) + "%");
        }
        couponSuccessText=txt_order_coupon_percentage.getText().toString();
        /*Set Discount amount*/
        txt_order_coupon.setText("- AED " + String.format("%.2f", discountInAmount));
        couponSuccess=txt_order_coupon.getText().toString();
        /*Set Total Amount*/
        txt_order_total.setText("AED " + String.format("%.2f", total1));
        totalSuccess=txt_order_total.getText().toString();
        /*Set Shipping charges*/
        txt_order_shipping.setText("AED " + String.format("%.2f", deliveryCharges));
        shippingChargeSuccess=txt_order_shipping.getText().toString();
        /*Set Vat Percentage*/
        txt_sales_tax_percentage.setText("Sale Tax " + String.format("%.2f", vatRate1) + "%");
        vatSuccess=txt_sales_tax_percentage.getText().toString();
        /*Set Vat Amount*/
        txt_vatRate.setText("AED " + String.format("%.2f", vatTotal));
        vatSuccessPer=txt_vatRate.getText().toString();
        /*Set Grand Total*/
        txt_grand_total.setText("AED " + String.format("%.2f", grandTotal));
        grandSuccess=txt_grand_total.getText().toString();

        /**Shipping Address*/
        txt_custName.setText(session_management.getUserDetails().get(KEY_NAME));

        if(shippingAddress.equals("")){
            //shippingAddress= addressModels.get(0).getCusAdd1()+","+ addressModels.get(0).getCusAdd2()+","+addressModels.get(0).getCity() +","+addressModels.get(0).getState();
            txt_order_address.setText(shippingAddress);
        }else{
            txt_order_address.setText(shippingAddress);
        }
        txt_cust_mobile.setText(session_management.getUserDetails().get(KEY_MOBILE));
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        switch (backStack){
            case 1:
                super.onBackPressed();
                break;
            case 2:
                img_offer.setImageResource(R.drawable.order_stage_2);
                view_stage1.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(ShippingDetailActivity.this, R.color.mediumGrey)));
                tvOfferLabel.setTextColor(getResources().getColor(R.color.mediumGrey));
                ll_stage_2.setVisibility(View.GONE);
                ll_stage_1.setVisibility(View.VISIBLE);
                btn_next_offer_slide.setVisibility(View.GONE);
                cl_next_offer_slide.setVisibility(View.GONE);
                //btn_next_slide.setVisibility(View.VISIBLE);
                cl_next_slide.setVisibility(View.VISIBLE);
                tvTitle.setText("Shipping");
                btn_next_slide.resetSlider();
                backStack = 1;
                break;
            case 3:
                img_payment.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(ShippingDetailActivity.this, R.color.mediumGrey)));
                tvPaymentLabel.setTextColor(getResources().getColor(R.color.mediumGrey));
                view_stage2.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(ShippingDetailActivity.this, R.color.mediumGrey)));
                ll_stage_3.setVisibility(View.GONE);
                ll_stage_2.setVisibility(View.VISIBLE);
                //btn_next_offer_slide.setVisibility(View.VISIBLE);
                cl_next_offer_slide.setVisibility(View.VISIBLE);
                tvTitle.setText("Offer");
                btn_next_offer_slide.resetSlider();
                backStack = 2;
                break;
            case 4:
                ConfirmOrderBackPressDialog();
                /*img_place_order.setImageResource(R.drawable.order_stage_4);
                tvPlaceOrderLabel.setTextColor(getResources().getColor(R.color.mediumGrey));
                view_stage3.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(ShippingDetailActivity.this, R.color.mediumGrey)));
                ll_stage_4.setVisibility(View.GONE);
                btn_confirmOrder_slide.resetSlider();
                btn_confirmOrder_slide.setVisibility(View.GONE);
                ll_stage_3.setVisibility(View.VISIBLE);
                tvTitle.setText("Payment");
                btn_payment_slide.resetSlider();
                backStack = 3;*/
                break;

        }

    }

    private void OrderPlacedSuccessDialog(String orderNumber,String orderDate,String orderTotal,String msg){
        Dialog bottomSheetDialog=new Dialog(ShippingDetailActivity.this);
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
        bottomSheetDialog.getWindow().getAttributes().windowAnimations =  R.style.DialogAnimation;
        bottomSheetDialog.show();

        tvMessage.setText(msg);
        tvOrderNumber.setText(orderNumber);
        tvOrderDate.setText(orderDate);
        tvOrderTotal.setText(orderTotal);

        tvClose.setOnClickListener(view -> {
            startActivity(new Intent(ShippingDetailActivity.this, MainDrawerActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK )
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        });

    }

    private void updateOrderStatus(String OrderID, String custID, String OrderStatus,
                                   String OrderTransactionType, String Promo,
                                   String CMCode, String couponType,
                                   String CMID, String DecidedExisitingLimit
    ){
        progressDialog.show();
        ServiceGenrator.getApiInterface().updateOrderStatus(OrderID, custID, OrderStatus,
                OrderTransactionType, Promo,
                CMCode, couponType,
                CMID, DecidedExisitingLimit).enqueue(new Callback<ResponseUpdateOrderStatus>() {
            @Override
            public void onResponse(Call<ResponseUpdateOrderStatus> call, Response<ResponseUpdateOrderStatus> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    if(response.body().isStatus()){

                    }else{
                        Toast.makeText(ShippingDetailActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(ShippingDetailActivity.this,response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUpdateOrderStatus> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ShippingDetailActivity.this,t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    void getProfileAddress()
    {
        addressModels.clear();
        AddressModel addressModel = new AddressModel();
        addressModel.setCsdid("");
        addressModel.setCustID(session_management.getUserDetails().get(BaseURL.KEY_ID));
        addressModel.setCsdTypeName("Home");
        addressModel.setCusAdd1(session_management.getUserLandmark());
        addressModel.setCusAdd2(session_management.getUserStreet());
        addressModel.setCity(session_management.getUserCity());
        addressModel.setState(session_management.getUserState());
        addressModel.setCountry(session_management.getUserCountry());
        addressModel.setZipcode(session_management.getUserPinCode());
        addressModel.setArea(session_management.getUserDetails().get(BaseURL.ADDRESS));
        addressModel.setLatitude(session_management.getLatPref());
        addressModel.setLongitude(session_management.getLangPref());
        addressModel.setCusMob(session_management.getUserDetails().get(BaseURL.KEY_MOBILE));
        addressModel.setCusEmail(session_management.getUserDetails().get(BaseURL.KEY_EMAIL));

        addressModels.add(addressModel);

        if(addressAdapter==null) {
            addressAdapter = new AddressAdapter(ShippingDetailActivity.this, addressModels, ShippingDetailActivity.this);
            rv_address.setAdapter(addressAdapter);
            if (getIntent().getIntExtra("addAddress", 0) == 1) {
                showNewAddress(addressModels.size() - 1);
            }
            LinearSnapHelper snapHelper = new LinearSnapHelper();
            snapHelper.attachToRecyclerView(rv_address);

            rv_address.setOnFlingListener(snapHelper);
            addressAdapter.showCheckedImage(session_management.getShippingAddressPosition());
        }
        else
        {
            addressAdapter.notifyDataSetChanged();
        }
        getAddress(id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getProfileAddress();
    }
}