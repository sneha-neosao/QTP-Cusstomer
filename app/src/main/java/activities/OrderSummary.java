package activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import adapters.ImageAdapterData;
import adapters.LifetimeOffersAdapter;
import Config.ApiBaseURL;
import Config.BaseURL;
import ModelClass.LifetimeOffer;
import ModelClass.MyCalendarModel;
import ModelClass.NotifyModelUser;
import ModelClass.VatResult;
import com.grocery.QTPmart.R;
import com.grocery.QTPmart.MainActivity;
import network.ApiInterface;
import network.Request.RequestExtraCharges;
import network.Response.ResponseCoupon;
import network.Response.ResponseExtraCharges;
import network.Response.ResponseLifetimeOffers;
import network.ServiceGenrator;
import util.AppController;
import util.CustomVolleyJsonRequest;
import util.DatabaseHandler;
import util.ForClicktimings;
import util.Session_management;
import com.ncorti.slidetoact.SlideToActView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

import static Config.BaseURL.ADDRESS;
import static Config.BaseURL.KEY_EMAIL;
import static Config.BaseURL.KEY_ID;
import static Config.BaseURL.KEY_MOBILE;
import static Config.BaseURL.KEY_NAME;

public class OrderSummary extends AppCompatActivity implements ForClicktimings, View.OnClickListener {

    private static final String TAG = OrderSummary.class.getName();
    final String TYPE_OFFER="offer";
    private View view_credit;
    final String TYPE_COUPON="coupon";
    private static final int LIFETIME_OFFER = 101;
    String total_atm,OrderTransactionType="";
    String subTotalSuccess,couponSuccess,couponSuccessText,totalSuccess,vatSuccess,
            shippingChargeSuccess,grandSuccess,vatSuccessPer,addressSuccess;
    ArrayList<VatResult> vatResultArrayList;
    Double totalAmount;
    public static double amount =0;
    SlideToActView btn_confirm_slide,btn_proceed_slide,btn_continue_slide,btn_pay_slde;
    Button btn_AddAddress,btn_Contine;
    LinearLayout back,couponLayout,offer_rv_layout;
    TextView  txt_deliver, txtTotalItems, pPrice, pMrp, totalItms, price,
            DeliveryCharge, Amounttotal, txt_totalPrice,textview_mobile_delivery,deliver_tv;
    String dname;
    JSONArray array;
    RecyclerView recycler_itemsList;
    String timeslot;
    String addressid;
    String user_id;
    ProgressDialog progressDialog;
    private DatabaseHandler db;
    private Session_management sessionManagement;
    private TextView textview_name_ofdata, currency_indicator, ruppy, currency_indicator_2;
    private List<MyCalendarModel> calendarModelList = new ArrayList<>();
    ArrayList<LifetimeOffer> list = new ArrayList<>();
    LifetimeOffersAdapter adapter;

    boolean status=false;
    String payment_method="";

    int pos= 1;
    double vatRate=0;

    ResponseCoupon coupon=new ResponseCoupon();

    EditText couponCode;
    Button btnCoupon,btn_continue,btnContinue,btn_cnt_shop;
    TextView subTotalAmount, couponPerText, couponApplied, total,
            vatPercent, vatPercentAmount, shippingCharges, grandTotalAmount,shipping_address_tvs,payment_info_tvs,order_ids,delivery_tv;
    TableRow rowCoupon, totalRow;
    TableRow rowCouponf, totalRowf;
    TextView tv_shipping,txt_shipping_completed,txt_offer_completed,txt_place_order_completed
            ,order_detail_tv,order_place_tv,coupoun_tv,tv_add_address;
    boolean isCouponApplied = false;
    double couponPer=0, vatPer=0, vatCharge=0, shippingCharge=0,discount=0,grand=0;
    String couponCodeText = "",couponShippingCharges="",couponDescription="";
    RadioButton cod_rbtn,online_rbtn,card_rbtn,bring_rbtn;
    RadioGroup radio_grp;
    LinearLayout offers_layout,shipping_layout,order_details_layout,order_place_layout,
            ll_payment_mode,ll_shipping_offer;
    RecyclerView offers_rv;

    TextView subTotalAmountf, couponPerTextf, couponAppliedf, totalf, vatPercentf, vatPercentAmountf, shippingChargesf, grandTotalAmountf;
    TextView  shipping_tv,complete_order_tv,offer_tv,plc_order_tv;
    TextView txt_place_completed,complete_tv,txt_offer_shipping;
    String cmid="",cmcode="",nextlimit="",role="",supplierID="";

    OffersReceivers offersReceivers = new OffersReceivers();
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        sessionManagement = new Session_management(getApplicationContext());
        user_id = sessionManagement.userId();
        role=sessionManagement.role();
        array = new JSONArray();

        init();

        checkOffers();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        vatResultArrayList=new ArrayList<>();
        sessionManagement = new Session_management(this);
        sessionManagement.cleardatetime();
        db = new DatabaseHandler(this);

        dname = getIntent().getStringExtra("dName");
        addressid = getIntent().getStringExtra("dId");
        supplierID=getIntent().getStringExtra("supplierID");
        vatRate=getIntent().getDoubleExtra("vatRate",5);
        
        btn_confirm_slide = findViewById(R.id.btn_confirm_slide);
        couponCode = findViewById(R.id.couponCode);
        btnCoupon = findViewById(R.id.btnCoupon);
        btnCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCoupon();
            }
        });

        subTotalAmount = findViewById(R.id.subTotalAmount);
        btn_proceed_slide = findViewById(R.id.btn_place_order_slide);
        btn_continue_slide = findViewById(R.id.btn_continue_slide);
        btn_pay_slde = findViewById(R.id.btn_pay_slide);
        rowCoupon = findViewById(R.id.rowCoupon);
        totalRow = findViewById(R.id.totalRow);
        total = findViewById(R.id.total);
        couponPerText = findViewById(R.id.couponPer);
        couponApplied = findViewById(R.id.couponApplied);
        vatPercent = findViewById(R.id.vatPercent);
        vatPercentAmount = findViewById(R.id.vatPercentAmount);
        shippingCharges = findViewById(R.id.shippingCharges);
        grandTotalAmount = findViewById(R.id.grandTotalAmount);

        String subTotal=getIntent().getStringExtra("subTotal");

        subTotalAmountf = findViewById(R.id.subTotalAmountf);
        subTotalAmountf.setText(""+CartActivity.tv_total.getText());
        subTotalAmount.setText(""+CartActivity.tv_total.getText());


        rowCouponf = findViewById(R.id.rowCouponf);
        totalRowf = findViewById(R.id.totalRowf);
        totalf = findViewById(R.id.totalf);
        couponPerTextf = findViewById(R.id.couponPerf);
        couponAppliedf = findViewById(R.id.couponAppliedf);
        vatPercentf = findViewById(R.id.vatPercentf);
        vatPercentAmountf = findViewById(R.id.vatPercentAmountf);
        shippingChargesf = findViewById(R.id.shippingChargesf);
        grandTotalAmountf = findViewById(R.id.grandTotalAmountf);
        deliver_tv = findViewById(R.id.deliver_tv);
        shipping_address_tvs = findViewById(R.id.shipping_address_tvs);
        payment_info_tvs = findViewById(R.id.payment_info_tvs);
        online_rbtn = findViewById(R.id.online_rbtn);
        card_rbtn = findViewById(R.id.card_rbtn);
        bring_rbtn = findViewById(R.id.bring_rbtn);
        cod_rbtn = findViewById(R.id.cod_rbtn);
        order_ids = findViewById(R.id.order_ids);
        order_place_layout = findViewById(R.id.order_place_layout);
        ll_payment_mode = findViewById(R.id.ll_paymentMode);
        ll_shipping_offer = findViewById(R.id.ll_offer_shipping);
        order_details_layout = findViewById(R.id.order_details_layout);
        shipping_layout = findViewById(R.id.shipping_layout);
        offers_layout = findViewById(R.id.offers_layout);
        btn_continue = findViewById(R.id.btn_continue);
        btnContinue = findViewById(R.id.btnContinue);
        delivery_tv = findViewById(R.id.delivery_tv);
        btn_cnt_shop = findViewById(R.id.btn_cnt_shop);
        radio_grp = findViewById(R.id.radio_grp);
        txt_place_completed = findViewById(R.id.txt_place_completed);
        complete_tv = findViewById(R.id.complete_tv);
        tv_shipping = findViewById(R.id.tv_shipping);
        txt_shipping_completed = findViewById(R.id.txt_shipping_completed);
        txt_offer_completed = findViewById(R.id.txt_offer_completed);
        txt_place_order_completed = findViewById(R.id.txt_place_order_completed);
        order_detail_tv = findViewById(R.id.order_detail_tv);
        order_place_tv = findViewById(R.id.order_place_tv);
        coupoun_tv = findViewById(R.id.coupoun_tv);
        tv_add_address = findViewById(R.id.tv_add_address);
        offers_rv = findViewById(R.id.offers_rv);
        shipping_tv = findViewById(R.id.shipping_tv);
        complete_order_tv = findViewById(R.id.complete_order_tv);
        couponLayout = findViewById(R.id.couponLayout);
        offer_rv_layout = findViewById(R.id.offer_rv_layout);
        offer_tv = findViewById(R.id.offer_tv);
        txt_offer_shipping = findViewById(R.id.txt_offer_shipping);
        plc_order_tv = findViewById(R.id.plc_order_tv);
        view_credit = findViewById(R.id.view_credit);


        shipping_tv.setVisibility(View.VISIBLE);
        //complete_order_tv.setVisibility(View.INVISIBLE);
       // offer_tv.setVisibility(View.INVISIBLE);
      //  plc_order_tv.setVisibility(View.INVISIBLE);

        Log.d(TAG, "init: "+role);
        if(role.equals("supplier")){
            card_rbtn.setVisibility(View.VISIBLE);
            view_credit.setVisibility(View.VISIBLE);
        }



        txt_shipping_completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set2pos();
            }
        });

        txt_offer_completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set3Pos();
            }
        });

        txt_place_order_completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               set4Pos();
            }
        });

        //getExtraCharges();

        tv_shipping.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.buttons)));
        coupoun_tv.setTextColor(getResources().getColor(R.color.grey));
        tv_shipping.setTextColor(getResources().getColor(R.color.white));
        order_detail_tv.setTextColor(getResources().getColor(R.color.grey));
        order_place_tv.setTextColor(getResources().getColor(R.color.grey));


        btn_AddAddress = findViewById(R.id.btn_AddAddress);

        txtTotalItems = findViewById(R.id.txtTotalItems);
        currency_indicator = findViewById(R.id.currency_indicator);
        currency_indicator_2 = findViewById(R.id.currency_indicator_2);
        ruppy = findViewById(R.id.rupyy);
        btn_Contine = findViewById(R.id.btn_Contine);
        txt_deliver = findViewById(R.id.txt_deliver);
        textview_mobile_delivery = findViewById(R.id.textview_mobile_delivery);
        recycler_itemsList = findViewById(R.id.recycler_itemsList);
        textview_name_ofdata = findViewById(R.id.textview_name_ofdata);
        pPrice = findViewById(R.id.pPrice);
        pMrp = findViewById(R.id.pMrp);
        totalItms = findViewById(R.id.totalItms);
        price = findViewById(R.id.price);
        DeliveryCharge = findViewById(R.id.DeliveryCharge);
        Amounttotal = findViewById(R.id.Amounttotal);
        txt_totalPrice = findViewById(R.id.txt_totalPrice);
        back = findViewById(R.id.back);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
      //  btn_confirm_slide.setLocked(true);

        btn_continue_slide.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(@NotNull SlideToActView slideToActView) {
                if (textview_name_ofdata.getText().toString().isEmpty() || txt_deliver.getText().toString().isEmpty())
                {
                    btn_continue_slide.resetSlider();
                    Toast.makeText(OrderSummary.this, "Please add delivery address details.", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    pos=2;
                    progressDialog.show();
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms
                            progressDialog.dismiss();
                        }
                    }, 2000);
                    shipping_layout.setVisibility(View.GONE);
                    order_details_layout.setVisibility(View.VISIBLE);
                    order_place_layout.setVisibility(View.GONE);
                    offers_layout.setVisibility(View.GONE);

                    //shipping_tv.setVisibility(View.INVISIBLE);
                    //complete_order_tv.setVisibility(View.INVISIBLE);
                    offer_tv.setVisibility(View.VISIBLE);
                   // plc_order_tv.setVisibility(View.INVISIBLE);

                    tv_shipping.setVisibility(View.GONE);
                    txt_shipping_completed.setVisibility(View.VISIBLE);
                    order_detail_tv.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.buttons)));
                   // tv_shipping.setBackgroundTintList(null);
                    order_place_tv.setBackgroundTintList(null);
                    coupoun_tv.setBackgroundTintList(null);

                    coupoun_tv.setTextColor(getResources().getColor(R.color.grey));
                   // tv_shipping.setTextColor(getResources().getColor(R.color.grey));
                    order_detail_tv.setTextColor(getResources().getColor(R.color.white));
                    order_place_tv.setTextColor(getResources().getColor(R.color.grey));

                }
            }
        });

          btn_confirm_slide.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(@NotNull SlideToActView slideToActView) {

                    btn_confirm_slide.setLocked(false);
                    pos=3;
                    shipping_layout.setVisibility(View.GONE);
                    order_details_layout.setVisibility(View.GONE);
                    ll_payment_mode.setVisibility(View.VISIBLE);
                   // order_place_layout.setVisibility(View.VISIBLE);
                    offers_layout.setVisibility(View.GONE);

                   // shipping_tv.setVisibility(View.INVISIBLE);
                  //  complete_order_tv.setVisibility(View.INVISIBLE);
                  //  offer_tv.setVisibility(View.INVISIBLE);
                    plc_order_tv.setVisibility(View.VISIBLE);

                    order_detail_tv.setVisibility(View.GONE);
                    txt_offer_completed.setVisibility(View.VISIBLE);


                    order_place_tv.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.buttons)));
                    tv_shipping.setBackgroundTintList(null);
                  //  order_detail_tv.setBackgroundTintList(null);
                    coupoun_tv.setBackgroundTintList(null);

                    coupoun_tv.setTextColor(getResources().getColor(R.color.grey));
                    tv_shipping.setTextColor(getResources().getColor(R.color.grey));
                    order_detail_tv.setTextColor(getResources().getColor(R.color.grey));
                    order_place_tv.setTextColor(getResources().getColor(R.color.white));

            }

        });

          btn_pay_slde.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
              @Override
              public void onSlideComplete(@NotNull SlideToActView slideToActView) {
                   if (payment_method.isEmpty())
                {
                    btn_pay_slde.resetSlider();
                    Toast.makeText(OrderSummary.this, "Please select payment method", Toast.LENGTH_SHORT).show();
                }
                   else {
                       pos=4;
                       order_place_tv.setVisibility(View.GONE);
                       txt_place_order_completed.setVisibility(View.VISIBLE);
                       coupoun_tv.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.buttons)));
                       shipping_layout.setVisibility(View.GONE);
                       order_details_layout.setVisibility(View.GONE);
                       ll_payment_mode.setVisibility(View.GONE);
                       order_place_layout.setVisibility(View.VISIBLE);
                       offers_layout.setVisibility(View.GONE);
                   }
              }
          });

        btn_proceed_slide.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(@NotNull SlideToActView slideToActView) {
               /* int totalQuantity = db.getCartCount();
                continueUrl(totalQuantity,String.valueOf(totalAmount),subTotalSuccess,addressSuccess,couponSuccess,couponSuccessText,totalSuccess,vatSuccess,
                        shippingChargeSuccess,grandSuccess,vatSuccessPer,

                        TYPE_COUPON,
                        OrderTransactionType);*/


                if (txt_totalPrice.getText().toString() != null && !txt_totalPrice.getText().toString().equalsIgnoreCase("") && txt_deliver.getText().toString() != null && !txt_deliver.getText().toString().equalsIgnoreCase("")) {

                    //  String totalAmountt = totalAmount;
                    int totalQuantity = db.getCartCount();
                    Log.e("Payment","totalQuantity"+String.valueOf(totalQuantity)+"\n"+
                            "totalAmount"+String.valueOf(totalAmount)+"\n"+
                            "subTotalSuccess"+subTotal+"\n"+
                            "addressSuccess"+addressSuccess+"\n"+
                            "couponSuccess"+couponSuccess+"\n"+
                            "couponSuccessText"+couponSuccessText+"\n"+
                            "totalSuccess"+totalSuccess+"\n"+
                            "vatSuccess"+vatSuccess+"\n"+
                            "shippingChargeSuccess"+shippingChargeSuccess+"\n"+
                            "grandSuccess"+grandSuccess+"\n"+
                            "vatSuccessPer"+vatSuccessPer+"\n"+
                            "TYPE_COUPON"+TYPE_COUPON+"\n"+
                            "OrderTransactionType"+OrderTransactionType+"\n"+
                            "isCouponApplied"+isCouponApplied+"\n"+
                            "isCouponID"+cmid+"\n"+
                            "cmcode"+cmcode+"\n"+
                            "vatCharge"+String.valueOf(vatCharge)+"\n"+
                            "shippingCharge"+String.valueOf(shippingCharge)+"\n"+
                            "couponCodeText"+couponCodeText+"\n"+
                            "discount"+String.valueOf(discount)+"\n"+
                            "grand"+grand+"\n"+
                            "nextlimit"+nextlimit+"\n"+
                            "total_atm"+String.valueOf(total_atm));
                    if (!totalAmount.equals("") && totalQuantity > 0) {
                       // progressDialog.show();
                        Log.e(TAG, "onSlideComplete: "+OrderTransactionType );

                        if(OrderTransactionType.equals("CC"))
                        {
                            Intent intent=new Intent(OrderSummary.this,NetworkPaymentActivity.class);
                            intent.putExtra("totalQuantity",String.valueOf(totalQuantity));
                            intent.putExtra("totalAmount",String.valueOf(totalAmount));
                            intent.putExtra("subTotalSuccess",subTotal);
                            intent.putExtra("addressSuccess",addressSuccess);
                            intent.putExtra("couponSuccess",couponSuccess);
                            intent.putExtra("couponSuccessText",couponSuccessText);
                            intent.putExtra("totalSuccess",totalSuccess);
                            intent.putExtra("vatSuccess",vatSuccess);
                            intent.putExtra("shippingChargeSuccess",shippingChargeSuccess);
                            intent.putExtra("grandSuccess",grandSuccess);
                            intent.putExtra("vatSuccessPer",vatSuccessPer);
                            intent.putExtra("TYPE_COUPON",TYPE_COUPON);
                            intent.putExtra("OrderTransactionType",OrderTransactionType);
                            intent.putExtra("isCouponApplied",isCouponApplied);
                            intent.putExtra("isCouponID",cmid);
                            intent.putExtra("cmcode",cmcode);
                            intent.putExtra("vatCharge",String.valueOf(vatCharge));
                            intent.putExtra("shippingCharge",String.valueOf(shippingCharge));
                            intent.putExtra("couponCodeText",couponCodeText);
                            intent.putExtra("discount",String.valueOf(discount));
                            intent.putExtra("grand",grand);
                            intent.putExtra("nextlimit",nextlimit);
                            intent.putExtra("total_atm",String.valueOf(total_atm));
                            startActivity(intent);
                            finish();
                        }
                        else {
                            continueUrl(totalQuantity, String.valueOf(totalAmount),
                                    subTotalSuccess,
                                    addressSuccess,couponSuccess,couponSuccessText,
                                    totalSuccess,vatSuccess,
                                    shippingChargeSuccess,grandSuccess,vatSuccessPer,
                                    TYPE_COUPON,OrderTransactionType);
                        }


                    } else {
                        Toast.makeText(OrderSummary.this, "Something went wrong...please check your cart!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    showToast("Please Wait...");
                }
            }
        });


        btn_AddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent In = new Intent(getApplicationContext(), AddressLocationActivity.class);
//                startActivityForResult(In, 22);
                Intent In = new Intent(getApplicationContext(), AddAddress.class);
                startActivityForResult(In, 23);
                //  finish();
            }
        });

        tv_add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent In = new Intent(getApplicationContext(), AddressLocationActivity.class);
//                startActivityForResult(In, 22);
                Intent In = new Intent(getApplicationContext(), AddAddress.class);
                startActivityForResult(In, 23);
                //  finish();
            }
        });

        textview_name_ofdata.setText(sessionManagement.getUserDetails().get(KEY_NAME));
        txt_deliver.setText(sessionManagement.getUserDetails().get(ADDRESS));
        textview_mobile_delivery.setText(sessionManagement.getUserDetails().get(KEY_MOBILE));

        deliver_tv.setText("Shipping to : "+sessionManagement.getUserDetails().get(KEY_NAME)+", "+sessionManagement.getUserDetails().get(KEY_MOBILE) +", "+sessionManagement.getUserDetails().get(ADDRESS));
        delivery_tv.setText("Shipping to : "+sessionManagement.getUserDetails().get(KEY_NAME)+", "+sessionManagement.getUserDetails().get(KEY_MOBILE) +", "+sessionManagement.getUserDetails().get(ADDRESS));
        addressSuccess=deliver_tv.getText().toString();
        shipping_address_tvs.setText(sessionManagement.getUserDetails().get(KEY_NAME)+"\n"+sessionManagement.getUserDetails().get(ADDRESS));


        radio_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                  @SuppressLint("ResourceAsColor")
                                                  @Override
                                                  public void onCheckedChanged(RadioGroup group, int checkedId)
                                                  {
                                                      if (checkedId==R.id.cod_rbtn)
                                                      {
                                                          payment_method  = "COD";
                                                          OrderTransactionType="COD";

                                                          radio_grp.setBackgroundResource(R.drawable.square_bg);
                                                          online_rbtn.setBackgroundResource(R.color.transparent);
                                                          online_rbtn.setTextColor(getResources().getColor(R.color.black));

                                                          card_rbtn.setBackgroundResource(R.color.transparent);
                                                          card_rbtn.setTextColor(getResources().getColor(R.color.black));

                                                          bring_rbtn.setBackgroundResource(R.color.transparent);
                                                          bring_rbtn.setTextColor(getResources().getColor(R.color.black));

                                                          cod_rbtn.setBackgroundResource(R.color.buttons);
                                                          cod_rbtn.setTextColor(getResources().getColor(R.color.white));

                                                          payment_info_tvs.setText("Payment Method : COD");
                                                          btn_Contine.setEnabled(true);
                                                          if (adapter!= null)
                                                          {
                                                              adapter.payment_method= OrderTransactionType;
                                                          }
                                                      }
                                                      else if (checkedId==R.id.online_rbtn)
                                                      {
                                                          radio_grp.setBackgroundResource(R.drawable.square_bg);
                                                          cod_rbtn.setBackgroundResource(R.color.transparent);
                                                          cod_rbtn.setTextColor(getResources().getColor(R.color.black));

                                                          card_rbtn.setBackgroundResource(R.color.transparent);
                                                          card_rbtn.setTextColor(getResources().getColor(R.color.black));

                                                          bring_rbtn.setBackgroundResource(R.color.transparent);
                                                          bring_rbtn.setTextColor(getResources().getColor(R.color.black));

                                                          online_rbtn.setBackgroundResource(R.color.buttons);
                                                          online_rbtn.setTextColor(getResources().getColor(R.color.white));

                                                          payment_method  = "ONLINE";
                                                          OrderTransactionType="CC";
                                                          payment_info_tvs.setText("Payment Method : ONLINE");
                                                         // payment_info_tvs.setText("Payment Method : ONLINE \n Coming soon ");
                                                          btn_Contine.setEnabled(false);
                                                          if (adapter!= null)
                                                          {
                                                              adapter.payment_method= OrderTransactionType;
                                                          }

                                                      }
                                                      else if (checkedId==R.id.card_rbtn)
                                                      {
                                                          radio_grp.setBackgroundResource(R.drawable.square_bg);
                                                          cod_rbtn.setBackgroundResource(R.color.transparent);
                                                          cod_rbtn.setTextColor(getResources().getColor(R.color.black));

                                                          online_rbtn.setBackgroundResource(R.color.transparent);
                                                          online_rbtn.setTextColor(getResources().getColor(R.color.black));

                                                          bring_rbtn.setBackgroundResource(R.color.transparent);
                                                          bring_rbtn.setTextColor(getResources().getColor(R.color.black));


                                                          card_rbtn.setBackgroundResource(R.color.buttons);
                                                          card_rbtn.setTextColor(getResources().getColor(R.color.white));

                                                          payment_method  = "C";
                                                          OrderTransactionType="C";
                                                          payment_info_tvs.setText("Payment Method : Credit Card");
                                                         // payment_info_tvs.setText("Payment Method : ONLINE \n Coming soon ");
                                                          btn_Contine.setEnabled(false);
                                                          if (adapter!= null)
                                                          {
                                                              adapter.payment_method= OrderTransactionType;
                                                          }

                                                      }
                                                      else if (checkedId==R.id.bring_rbtn)
                                                      {
                                                          radio_grp.setBackgroundResource(R.drawable.square_bg);
                                                          cod_rbtn.setBackgroundResource(R.color.transparent);
                                                          cod_rbtn.setTextColor(getResources().getColor(R.color.black));

                                                          card_rbtn.setBackgroundResource(R.color.transparent);
                                                          card_rbtn.setTextColor(getResources().getColor(R.color.black));

                                                          online_rbtn.setBackgroundResource(R.color.transparent);
                                                          online_rbtn.setTextColor(getResources().getColor(R.color.black));


                                                          bring_rbtn.setBackgroundResource(R.color.buttons);
                                                          bring_rbtn.setTextColor(getResources().getColor(R.color.white));


                                                          payment_method  = "BT";
                                                          OrderTransactionType="BT";
                                                          payment_info_tvs.setText("Payment Method : Bring Terminal");
                                                          // payment_info_tvs.setText("Payment Method : ONLINE \n Coming soon ");
                                                          btn_Contine.setEnabled(false);
                                                          if (adapter!= null)
                                                          {
                                                              adapter.payment_method= OrderTransactionType;
                                                          }

                                                      }
                                                  }
                                              }
        );


        btn_cnt_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManagement.setFirstCouponUsed(true);
                db.clearCart();
                startActivity(new Intent(OrderSummary.this, MainActivity.class));
                finish();
            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textview_name_ofdata.getText().toString().isEmpty() || txt_deliver.getText().toString().isEmpty())
                {
                    Toast.makeText(OrderSummary.this, "Please add delivery address details.", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    pos=2;
                    shipping_layout.setVisibility(View.GONE);
                    order_details_layout.setVisibility(View.VISIBLE);
                    order_place_layout.setVisibility(View.GONE);
                    offers_layout.setVisibility(View.GONE);

                    shipping_tv.setVisibility(View.INVISIBLE);
                   // complete_order_tv.setVisibility(View.INVISIBLE);
                    offer_tv.setVisibility(View.VISIBLE);
                    plc_order_tv.setVisibility(View.INVISIBLE);

                    order_detail_tv.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.buttons)));
                    tv_shipping.setBackgroundTintList(null);
                    order_place_tv.setBackgroundTintList(null);
                    coupoun_tv.setBackgroundTintList(null);

                    coupoun_tv.setTextColor(getResources().getColor(R.color.grey));
                    tv_shipping.setTextColor(getResources().getColor(R.color.grey));
                    order_detail_tv.setTextColor(getResources().getColor(R.color.white));
                    order_place_tv.setTextColor(getResources().getColor(R.color.grey));

                }
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (payment_method.isEmpty())
                {
                    Toast.makeText(OrderSummary.this, "Please select payment method", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    pos=3;
                    shipping_layout.setVisibility(View.GONE);
                    order_details_layout.setVisibility(View.GONE);
                    order_place_layout.setVisibility(View.VISIBLE);
                    offers_layout.setVisibility(View.GONE);

                    shipping_tv.setVisibility(View.INVISIBLE);
                    //complete_order_tv.setVisibility(View.INVISIBLE);
                    offer_tv.setVisibility(View.INVISIBLE);
                    plc_order_tv.setVisibility(View.VISIBLE);

                    order_place_tv.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.buttons)));
                    tv_shipping.setBackgroundTintList(null);
                    order_detail_tv.setBackgroundTintList(null);
                    coupoun_tv.setBackgroundTintList(null);

                    coupoun_tv.setTextColor(getResources().getColor(R.color.grey));
                    tv_shipping.setTextColor(getResources().getColor(R.color.grey));
                    order_detail_tv.setTextColor(getResources().getColor(R.color.grey));
                    order_place_tv.setTextColor(getResources().getColor(R.color.white));
                }
            }
        });



        // todaydatee = calendarModelList.get(0).getYear() + "-" + calendarModelList.get(0).getMonthValue() + "-" + calendarModelList.get(0).getDate();
        currency_indicator.setText(sessionManagement.getCurrency());
        currency_indicator_2.setText(sessionManagement.getCurrency());
        ruppy.setText(sessionManagement.getCurrency());
        ArrayList<HashMap<String, String>> map = db.getCartAll();

        try {
            JSONArray object = new JSONArray(map);
            for (int i = 0; i < object.length(); i++) {
                Log.d("sadf", object.toString());
                JSONObject object1 = object.getJSONObject(i);

                JSONObject product_array = new JSONObject();

                product_array.put("qty", object1.getString("qty"));
                product_array.put("varient_id", object1.getString("varient_id"));
                product_array.put("product_image", object1.getString("product_image"));

                Log.d("sdf", product_array.toString());
                array.put(product_array);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (sessionManagement.isFirstCouponUsed()) {

            updateData();
        } else {

            firstCoupon();
        }

//        Log.d("sdfa", array.toString());
        ImageAdapterData adapters = new ImageAdapterData(OrderSummary.this, map);

        recycler_itemsList.setLayoutManager(new LinearLayoutManager(OrderSummary.this, LinearLayoutManager.HORIZONTAL, false));
        recycler_itemsList.setAdapter(adapters);
        btn_Contine.setOnClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
       // progressDialog.dismiss();
    }


    private void checkBook(final int totalItems,
                           final String totalAmount,
                           String subTotalSuccess,
                           String addressSuccess,
                           String couponSuccess,String couponSuccessText,
                           String totalSuccess,String vatSuccess,
                           String shippingChargeSuccess,String grandSuccess,String vatSuccessPer,
                           String couponType,
                           String OrderTransType)
    {
        HashMap<String, String> param = new HashMap<>();

        param.put("custID", sessionManagement.getUserDetails().get(KEY_ID));
        param.put("OrderStatus", "" + totalItems);
        param.put("SubTotal", String.format("%.2f", Double.parseDouble(total_atm)));
        param.put("Total", String.format("%.2f", Double.parseDouble(totalAmount)));
        param.put("FirstName", sessionManagement.getUserDetails().get(KEY_NAME));
        param.put("Mobile", sessionManagement.getUserDetails().get(KEY_MOBILE));
        param.put("email", sessionManagement.getUserDetails().get(KEY_EMAIL));
        param.put("AddressLine1", sessionManagement.getAddress());
        param.put("City", sessionManagement.getLocationCity());
        param.put("country", sessionManagement.getCountry());
        param.put("BranchCode", ApiInterface.branchcode);
        param.put("DeviceName","Android");
        param.put("OrderTransactionType",""+OrderTransType);
        param.put("latitude", sessionManagement.getLatPref());
        param.put("longitude", sessionManagement.getLangPref());

        param.put("tax", String.format("%.2f", vatCharge));
        param.put("shipping", String.format("%.2f", shippingCharge));


        param.put("couponType", couponType);
        param.put("Promo", couponCodeText);
        param.put("discount",String.format("%.2f", discount));
        param.put("grandtotal",String.format("%.2f", grand));

        if (isCouponApplied) {
            param.put("CMID", cmid);
            param.put("CMCode", cmcode);
            param.put("DecidedExisitingLimit", nextlimit);

        }
        else
        {
            param.put("CMID","" );
            param.put("CMCode","");
            param.put("DecidedExisitingLimit", "0");
        }

        Log.e(TAG, "getParams: " + param.toString());
    }


    private void continueUrl(final int totalItems,
                             final String totalAmount,
                             String subTotalSuccess,
                             String addressSuccess,
                             String couponSuccess,String couponSuccessText,
                             String totalSuccess,String vatSuccess,
                             String shippingChargeSuccess,String grandSuccess,String vatSuccessPer,
                             String couponType,
                             String OrderTransType) {

        Log.e(TAG, "continueUrl: "+OrderTransType );
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiBaseURL.OrderContinue, new Response.Listener<String>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(String response) {
                Log.e("ordermake", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    String msg = jsonObject.getString("message");
                    if (status) {

                        progressDialog.dismiss();
//                        Intent intent=new Intent(OrderSummary.this,OrderSuccessActivity.class);
//                        intent.putExtra("msg",msg);
//                        intent.putExtra("amount",subTotalSuccess);
//                        intent.putExtra("discount",couponSuccess);
//                        intent.putExtra("totalAmount",totalSuccess);
//                        intent.putExtra("vatCharge",vatSuccess);
//                        intent.putExtra("shippingCharge",shippingChargeSuccess);
//                        intent.putExtra("grand",grandSuccess);
//                        intent.putExtra("vatRate",vatSuccessPer);
//                        intent.putExtra("address",addressSuccess);
//                        intent.putExtra("isCoupon",isCouponApplied);
//                        intent.putExtra("coupon_code",cmcode);
//                        intent.putExtra("couponSuccessText",couponSuccessText);
//                        startActivity(intent);
                        finish();

                        /*ShowOrderSuccess(msg,addressSuccess,subTotalSuccess,couponSuccess,couponSuccessText,totalSuccess,vatSuccess,
                                shippingChargeSuccess,grandSuccess,vatSuccessPer);*/
//                        Intent intent = new Intent(getApplicationContext(), OrderSuccessful.class);
//                        intent.putExtra("msg", msg);
//                        startActivity(intent);
//
//                        finish();

                    } else {

                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e(TAG, "onErrorResponse: " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();

                param.put("custID", sessionManagement.getUserDetails().get(KEY_ID));
                param.put("OrderStatus", "" + totalItems);
                param.put("SubTotal", String.format("%.2f", Double.parseDouble(total_atm)));
                param.put("Total", String.format("%.2f", Double.parseDouble(totalAmount)));
                param.put("FirstName", sessionManagement.getUserDetails().get(KEY_NAME));
                param.put("Mobile", sessionManagement.getUserDetails().get(KEY_MOBILE));
                param.put("email", sessionManagement.getUserDetails().get(KEY_EMAIL));
                param.put("AddressLine1", sessionManagement.getAddress());
                param.put("City", sessionManagement.getLocationCity());
                param.put("country", sessionManagement.getCountry());
                param.put("BranchCode", ApiInterface.branchcode);
                param.put("DeviceName","Android");
                param.put("OrderTransactionType",""+OrderTransType);
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
        RequestQueue requestQueue = Volley.newRequestQueue(OrderSummary.this);
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void ShowOrderSuccess(String message,
                                  String addressSuccess,
                                  String subTotalSuccess,
                                  String couponSuccess,String couponSuccessText,
                                  String totalSuccess,String vatSuccess,
                                  String shippingChargeSuccess,String grandSuccess,
                                  String vatSuccessPer
                                  ) {

       // pos=4;
        progressDialog.dismiss();
//        Intent intent=new Intent(OrderSummary.this,OrderSuccessActivity.class);
//        intent.putExtra("msg",message);
//        intent.putExtra("amount",subTotalSuccess);
//        intent.putExtra("discount",couponSuccess);
//        intent.putExtra("totalAmount",totalSuccess);
//        intent.putExtra("vatCharge",vatSuccess);
//        intent.putExtra("shippingCharge",shippingChargeSuccess);
//        intent.putExtra("grand",grandSuccess);
//        intent.putExtra("vatRate",vatSuccessPer);
//        intent.putExtra("address",addressSuccess);
//        intent.putExtra("couponSuccessText",couponSuccessText);
//        startActivity(intent);
//        finish();


      /*  pos=5;

        coupoun_tv.setVisibility(View.GONE);
        txt_place_completed.setVisibility(View.VISIBLE);

        tv_shipping.setBackgroundTintList(null);
        order_detail_tv.setBackgroundTintList(null);
       // order_place_tv.setBackgroundTintList(null);
        coupoun_tv.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.buttons)));

        coupoun_tv.setTextColor(getResources().getColor(R.color.white));
        tv_shipping.setTextColor(getResources().getColor(R.color.grey));
        order_detail_tv.setTextColor(getResources().getColor(R.color.grey));
        order_place_tv.setTextColor(getResources().getColor(R.color.grey));

       *//* shipping_tv.setVisibility(View.INVISIBLE);
        complete_order_tv.setVisibility(View.VISIBLE);
        offer_tv.setVisibility(View.INVISIBLE);
        plc_order_tv.setVisibility(View.INVISIBLE);*//*

        shipping_layout.setVisibility(View.GONE);
        order_details_layout.setVisibility(View.GONE);
        order_place_layout.setVisibility(View.GONE);
        ll_payment_mode.setVisibility(View.GONE);
        offers_layout.setVisibility(View.VISIBLE);

        order_ids.setText(message);

*/
    }


    private void updateData()
    {

        pPrice.setText("" + db.getTotalAmount());
        price.setText("" + db.getTotalAmount());
        total_atm = String.valueOf(db.getTotalAmount());
        totalAmount = Double.parseDouble(total_atm);
        txtTotalItems.setText("" + db.getCartCount());
        totalItms.setText("" + db.getCartCount() + " " + " Items");

       // subTotalAmountf.setText("AED " + String.format("%.2f", Double.parseDouble(total_atm)));
        subTotalSuccess=subTotalAmountf.getText().toString();
        if (isCouponApplied)
        {

            rowCoupon.setVisibility(View.VISIBLE);
            totalRow.setVisibility(View.VISIBLE);
            rowCouponf.setVisibility(View.VISIBLE);
            totalRowf.setVisibility(View.VISIBLE);
           // ll_shipping_offer.setVisibility(View.VISIBLE);

            couponPerText.setText("Coupon " + String.format("%.2f", couponPer) + "%");
            couponPerTextf.setText("Coupon " + String.format("%.2f", couponPer) + "%");
            couponSuccessText=couponPerText.getText().toString();

            discount = totalAmount * couponPer * 0.01;
            couponApplied.setText("AED " + String.format("%.2f", discount));
            couponAppliedf.setText("AED " + String.format("%.2f", discount));
            couponSuccess=couponApplied.getText().toString();

            totalAmount = (totalAmount - discount);
            total.setText("AED " + String.format("%.2f", totalAmount));
            totalf.setText("AED " + String.format("%.2f", totalAmount));
            totalSuccess=total.getText().toString();

            vatCharge = 0.01 * vatPer * totalAmount;
            vatPercentAmount.setText("AED " + String.format("%.2f", vatCharge));
            vatPercentAmountf.setText("AED " + String.format("%.2f", vatCharge));
            vatSuccessPer=vatPercentAmount.getText().toString();

            //shippingChargeSuccess=shippingCharge;

            txt_offer_shipping.setText(couponDescription);
            shippingCharges.setText("AED " + String.format("%.2f", shippingCharge));
            shippingChargesf.setText("AED " + String.format("%.2f", shippingCharge));
            shippingChargeSuccess=shippingCharges.getText().toString();

            grand = totalAmount + vatCharge + shippingCharge;
            amount = totalAmount;

            grandTotalAmount.setText("AED " + String.format("%.2f", grand));
            grandTotalAmountf.setText("AED " + String.format("%.2f", grand));
            grandSuccess=grandTotalAmount.getText().toString();

            txt_totalPrice.setText(String.format("%.2f", grand));


        } else {

            vatPer = vatRate;
            Log.e("vatOrder",vatPer+"");
            vatPercent.setText("VAT " + String.format("%.2f", vatPer) + "%");
            vatPercentf.setText("VAT " + String.format("%.2f", vatPer) + "%");
            vatSuccess=vatPercent.getText().toString();
           // ll_shipping_offer.setVisibility(View.GONE);

            vatCharge = 0.01 * vatPer * Double.parseDouble(total_atm);
            vatPercentAmount.setText("AED " + String.format("%.2f", vatCharge));
            vatPercentAmountf.setText("AED " + String.format("%.2f", vatCharge));
            vatSuccessPer=vatPercentAmount.getText().toString();
            Log.e("vatCharge",vatCharge+"");
            rowCoupon.setVisibility(View.GONE);
            totalRow.setVisibility(View.GONE);
            rowCouponf.setVisibility(View.GONE);
            totalRowf.setVisibility(View.GONE);

            SharedPreferences preferences = getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
            shippingCharge = Double.parseDouble(preferences.getString("deliveryCharges", "0"));
            shippingCharges.setText("AED " + String.format("%.2f", shippingCharge));
            shippingChargesf.setText("AED " + String.format("%.2f", shippingCharge));
            shippingChargeSuccess=shippingCharges.getText().toString();

            grand = totalAmount + vatCharge + shippingCharge;
            amount = totalAmount;

            grandTotalAmount.setText("AED " + String.format("%.2f", grand));
            grandTotalAmountf.setText("AED " + String.format("%.2f", grand));
            grandSuccess=grandTotalAmount.getText().toString();

            txt_totalPrice.setText(String.format("%.2f", grand));
        }

        SharedPreferences preferences = getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);

        //vatPer = Double.parseDouble(preferences.getString("vatRate", "0"));


    }

    @Override
    public void getTimeSlot(String timeslot) {
        this.timeslot = timeslot;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 23) {
            txt_deliver.setText(sessionManagement.getUserDetails().get(ADDRESS));
            deliver_tv.setText("Shipping to : "+sessionManagement.getUserDetails().get(KEY_NAME)+", "+sessionManagement.getUserDetails().get(KEY_MOBILE) +", "+sessionManagement.getUserDetails().get(ADDRESS));
            delivery_tv.setText("Shipping to : "+sessionManagement.getUserDetails().get(KEY_NAME)+", "+sessionManagement.getUserDetails().get(KEY_MOBILE) +", "+sessionManagement.getUserDetails().get(ADDRESS));
            shipping_address_tvs.setText(sessionManagement.getUserDetails().get(KEY_NAME)+"\n"+sessionManagement.getUserDetails().get(ADDRESS));


        } else if (requestCode == LIFETIME_OFFER) {

            if (data != null) {
                continueUrl(db.getCartCount(), String.valueOf(db.getTotalAmount()),subTotalSuccess,addressSuccess,couponSuccess,couponSuccessText,totalSuccess,vatSuccess,
                        shippingChargeSuccess,grandSuccess,vatSuccessPer,"",OrderTransactionType);
            }
        }

    }

    public void checkCoupon() {

        HashMap params = new HashMap();
        params.put("BranchCode", "B001");
        params.put("amount", String.valueOf(db.getTotalAmount()));
        params.put("CouponCode", couponCode.getText().toString());
        params.put("custID", user_id);

        Volley.newRequestQueue(this).add(
                new StringRequest(Request.Method.POST,
                        ApiBaseURL.checkCoupon,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Gson gson = new Gson();
                                 coupon = gson.fromJson(response, ResponseCoupon.class);

                                    if (coupon.status) {

                                        isCouponApplied = true;
                                        couponPer = coupon.result.get(0).discountValue;
                                       // vatCharge = coupon.result.get(0).vatCharge;

                                        couponCodeText = coupon.result.get(0).cmCode;
                                        Toast.makeText(OrderSummary.this, coupon.message, Toast.LENGTH_SHORT).show();
                                        offer_rv_layout.setVisibility(View.GONE);
                                        couponLayout.setVisibility(View.VISIBLE);
                                        updateData();

                                    } else {

                                        Toast.makeText(OrderSummary.this, coupon.message, Toast.LENGTH_SHORT).show();
                                    }
                            }
                        },
                        new Response.ErrorListener() {
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

    public void firstCoupon() {

        HashMap params = new HashMap();
        params.put("BranchCode", "B001");
        params.put("amount", String.valueOf(db.getTotalAmount()));
        params.put("custID", user_id);

        Volley.newRequestQueue(this).add(
                new StringRequest(Request.Method.POST,
                        ApiBaseURL.firstCoupon,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Gson gson = new Gson();
                                coupon = gson.fromJson(response, ResponseCoupon.class);



                                if (coupon.status) {

                                    isCouponApplied = true;
                                    couponPer = coupon.result.get(0).discountValue;
                                    couponCode.setText(coupon.result.get(0).cmCode);
                                    cmcode = coupon.result.get(0).cmCode;
                                    cmid = coupon.result.get(0).cmid;
                                    nextlimit = String.valueOf(coupon.nextLimit);
                                    couponCode.setEnabled(false);
                                    btnCoupon.setEnabled(false);

                                    updateData();

                                } else {
                                    updateData();
                                    //  Toast.makeText(OrderSummary.this, coupon.message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
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

    private void showToast(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {

//        if (status)
//        {
//            if (txt_totalPrice.getText().toString() != null && !txt_totalPrice.getText().toString().equalsIgnoreCase("") && txt_deliver.getText().toString() != null && !txt_deliver.getText().toString().equalsIgnoreCase("")) {
//
//                //  String totalAmountt = totalAmount;
//                int totalQuantity = db.getCartCount();
//
//                if (isCouponApplied) {
//
//
//                    if (!totalAmount.equals("") && totalQuantity > 0) {
//                        continueUrl(totalQuantity, String.valueOf(totalAmount),TYPE_COUPON);
//                    } else {
//                        Toast.makeText(OrderSummary.this, "Something went wrong...please check your cart!", Toast.LENGTH_SHORT).show();
//                    }
//
//                } else {
//                    //String totalAmountt =db.getTotalAmount();
//                    Intent intent = new Intent(OrderSummary.this, OffersActivity.class);
//                    intent.putExtra("totalAmount", totalAmount);
//                    intent.putExtra("vatPer", vatPer);
//                    intent.putExtra("shippingCharge", shippingCharge);
//                    startActivityForResult(intent, LIFETIME_OFFER);
//
//                }
//
//                progressDialog.dismiss();
//
//
//            } else {
//                Snackbar.make(view, "Please wait", Snackbar.LENGTH_LONG).show();
//            }
//        }
//        else
//        {
            if (txt_totalPrice.getText().toString() != null && !txt_totalPrice.getText().toString().equalsIgnoreCase("") && txt_deliver.getText().toString() != null && !txt_deliver.getText().toString().equalsIgnoreCase("")) {

                //  String totalAmountt = totalAmount;
                int totalQuantity = db.getCartCount();
                    if (!totalAmount.equals("") && totalQuantity > 0) {
                      //  progressDialog.show();
                       /* continueUrl(totalQuantity, String.valueOf(totalAmount),subTotalSuccess,addressSuccess,couponSuccess,couponSuccessText,totalSuccess,vatSuccess,
                                shippingChargeSuccess,grandSuccess,vatSuccessPer,TYPE_COUPON,OrderTransactionType);
                  */  } else {
                        Toast.makeText(OrderSummary.this, "Something went wrong...please check your cart!", Toast.LENGTH_SHORT).show();
                    }

            } else {
                Snackbar.make(view, "Please wait", Snackbar.LENGTH_LONG).show();
            }
//        }

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

        }, new Response.ErrorListener() {
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

    private void getOffers() {
        //progressDialog.show();
        offers_rv.setVisibility(View.GONE);
        ServiceGenrator.getApiInterface().getLifetimeOffers(ApiInterface.branchcode).enqueue(
                new Callback<ResponseLifetimeOffers>() {
                    @Override
                    public void onResponse(Call<ResponseLifetimeOffers> call, retrofit2.Response<ResponseLifetimeOffers> response) {

                        if (response.isSuccessful()) {

                            if (response.body().status) {
                               // progressDialog.dismiss();
                                offers_rv.setVisibility(View.VISIBLE);
                                list.addAll(response.body().getResult());

                                adapter = new LifetimeOffersAdapter(OrderSummary.this, list);
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

                if(couponDescription!=null){
                    ll_shipping_offer.setVisibility(View.VISIBLE);
                }
                else {
                    ll_shipping_offer.setVisibility(View.GONE);
                }


                couponPer=Double.parseDouble(discountValue);
                vatPer=vatPers;
               // vatCharge=vatCharges;
                shippingCharge=shippingCharges;
                discount=discounts;
                grand=grands;



              //  subTotalAmountf.setText("AED " + String.format("%.2f", amount));

                couponPerTextf.setText("Coupon " + String.format("%.2f", Double.parseDouble(discountValue)) + "%");

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

                isCouponApplied = true;
                updateData();


//                shipping_layout.setVisibility(View.GONE);
//                order_details_layout.setVisibility(View.GONE);
//                order_place_layout.setVisibility(View.VISIBLE);
//                offers_layout.setVisibility(View.GONE);
//
//                shipping_tv.setVisibility(View.GONE);
//                complete_order_tv.setVisibility(View.GONE);
//                offer_tv.setVisibility(View.GONE);
//                plc_order_tv.setVisibility(View.VISIBLE);
//
//                order_place_tv.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange)));
//                tv_shipping.setBackgroundTintList(null);
//                order_detail_tv.setBackgroundTintList(null);
//                coupoun_tv.setBackgroundTintList(null);
//
//                coupoun_tv.setTextColor(getResources().getColor(R.color.grey));
//                tv_shipping.setTextColor(getResources().getColor(R.color.grey));
//                order_detail_tv.setTextColor(getResources().getColor(R.color.grey));
//                order_place_tv.setTextColor(getResources().getColor(R.color.white));
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        btn_proceed_slide.resetSlider();
        btn_confirm_slide.resetSlider();
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

    @Override
    public void onBackPressed() {
        if (pos==1)
        {
            super.onBackPressed();
        }
        if (pos==2)
        {
            pos=1;
            set2pos();
        }
        if (pos==3)
        {
            pos=2;
           set3Pos();
        }
        if (pos== 4)
        {
            pos=3;
           set4Pos();
        }
       /* if(pos==5){
            startActivity(new Intent(OrderSummary.this
                    , MainActivity.class));
            finish();
        }*/
    }

    @SuppressLint("NewApi")
    private void set2pos() {
        /*shipping_tv.setVisibility(View.VISIBLE);
        //complete_order_tv.setVisibility(View.INVISIBLE);
        offer_tv.setVisibility(View.INVISIBLE);
        plc_order_tv.setVisibility(View.INVISIBLE);*/
        btn_proceed_slide.resetSlider();
        btn_confirm_slide.resetSlider();
        btn_continue_slide.resetSlider();
        btn_pay_slde.resetSlider();
        shipping_layout.setVisibility(View.VISIBLE);
        order_details_layout.setVisibility(View.GONE);
        order_place_layout.setVisibility(View.GONE);
        ll_payment_mode.setVisibility(View.GONE);
        offers_layout.setVisibility(View.GONE);


        txt_offer_completed.setVisibility(View.GONE);
        txt_shipping_completed.setVisibility(View.GONE);
        order_detail_tv.setVisibility(View.VISIBLE);
        tv_shipping.setVisibility(View.VISIBLE);

        tv_shipping.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.buttons)));
        order_detail_tv.setBackgroundTintList(null);
        order_place_tv.setBackgroundTintList(null);
        coupoun_tv.setBackgroundTintList(null);

        coupoun_tv.setTextColor(getResources().getColor(R.color.grey));
        tv_shipping.setTextColor(getResources().getColor(R.color.white));
        order_detail_tv.setTextColor(getResources().getColor(R.color.grey));
        order_place_tv.setTextColor(getResources().getColor(R.color.grey));

    }

    @SuppressLint("NewApi")
    public void set3Pos()
    {
        btn_proceed_slide.resetSlider();
        btn_confirm_slide.resetSlider();
        btn_continue_slide.resetSlider();
        btn_pay_slde.resetSlider();


        shipping_layout.setVisibility(View.GONE);
        order_details_layout.setVisibility(View.VISIBLE);
        ll_payment_mode.setVisibility(View.GONE);
        order_place_layout.setVisibility(View.GONE);
        offers_layout.setVisibility(View.GONE);

       /* shipping_tv.setVisibility(View.INVISIBLE);
        complete_order_tv.setVisibility(View.INVISIBLE);
        offer_tv.setVisibility(View.VISIBLE);
        plc_order_tv.setVisibility(View.INVISIBLE);*/
        offer_tv.setVisibility(View.VISIBLE);
        txt_offer_completed.setVisibility(View.GONE);
        order_detail_tv.setVisibility(View.VISIBLE);


        order_detail_tv.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.buttons)));
        tv_shipping.setBackgroundTintList(null);
        order_place_tv.setBackgroundTintList(null);
        coupoun_tv.setBackgroundTintList(null);

        coupoun_tv.setTextColor(getResources().getColor(R.color.grey));
        tv_shipping.setTextColor(getResources().getColor(R.color.grey));
        order_detail_tv.setTextColor(getResources().getColor(R.color.white));
        order_place_tv.setTextColor(getResources().getColor(R.color.grey));
    }

    @SuppressLint("NewApi")
    public void set4Pos()
    {

        btn_proceed_slide.resetSlider();
        btn_confirm_slide.resetSlider();
        btn_continue_slide.resetSlider();
        btn_pay_slde.resetSlider();

        shipping_layout.setVisibility(View.GONE);
        order_details_layout.setVisibility(View.GONE);
        offers_layout.setVisibility(View.GONE);
        ll_payment_mode.setVisibility(View.VISIBLE);
        order_place_layout.setVisibility(View.GONE);


       /* shipping_tv.setVisibility(View.INVISIBLE);
        complete_order_tv.setVisibility(View.INVISIBLE);
        offer_tv.setVisibility(View.VISIBLE);
        plc_order_tv.setVisibility(View.INVISIBLE);*/

        order_place_tv.setVisibility(View.VISIBLE);
        txt_place_order_completed.setVisibility(View.GONE);

        order_place_tv.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.buttons)));
        tv_shipping.setBackgroundTintList(null);
        coupoun_tv.setBackgroundTintList(null);
        complete_tv.setBackgroundTintList(null);

        coupoun_tv.setTextColor(getResources().getColor(R.color.white));
        tv_shipping.setTextColor(getResources().getColor(R.color.grey));
        order_detail_tv.setTextColor(getResources().getColor(R.color.grey));
        order_place_tv.setTextColor(getResources().getColor(R.color.grey));
    }

/*
    public void getExtraCharges(){
        String tag_json_obj = "json_cart_list_req";
        String custID= sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
        Map<String, String> params = new HashMap<String, String>();
        params.put("custID", custID);
        params.put("BranchCode", ApiInterface.branchcode);
        params.put("supplierIds", supplierID);
        // params.put("SupplierID",);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ApiBaseURL.getExtraCharges, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("CheckApiCart", response.toString());
                Log.e("getExtraCharges", response.toString());

                try {
                    boolean status = response.getBoolean("status");

                    if (status) {
                        JSONArray vatResult = response.getJSONArray("vatResult");

                        double sv=0;
                        SharedPreferences preferences = getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                        shippingCharge = Double.parseDouble(preferences.getString("deliveryCharges", "0"));
                        for (int i = 0; i < vatResult.length(); i++) {

                            JSONObject object = vatResult.getJSONObject(i);

                            if (object.has("deliveryCharges"))
                            {
                                sv+=Double.parseDouble(object.getString("deliveryCharges"));
                            }
                        }
                        shippingCharge+=sv;
                        preferences.edit().putString("deliveryCharges",""+shippingCharge).apply();
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
*/
}