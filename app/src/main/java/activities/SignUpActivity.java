package activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import Config.ApiBaseURL;
import Config.BaseURL;

import com.grocery.QTPmart.MainActivity;
import com.grocery.QTPmart.R;
import util.AppController;
import util.CustomVolleyJsonRequest;
import util.DatabaseHandler;
import util.Session_management;
import com.hbb20.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class SignUpActivity extends AppCompatActivity {
    static final String TAG="SignUpActivity";
    //new
    TextInputEditText etName, etPhone, etEmail, etPAss,edt_referal_code;
   // Button btnSignUP;
    TextView btnLogin,btnSignUP;
    ImageView sign_in_img,iv_back_login;


    ProgressDialog progressDialog;
    String emailPattern, token;
    LinearLayout skip;
    LinearLayout flag_view;
    TextView country_c;
    private Session_management session_management;
    DatabaseHandler dbHandler;
    private String countryCode = "";
    private int countryFlag = -1;
    ArrayList<HashMap<String, String>> map;
    int uploadPosition=0;
    String returnTo="";

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        newBase = LocaleHelper.onAttach(newBase);
//        super.attachBaseContext(newBase);
//    }

    ImageView fb_login_circular_button,ivGoogleSignIn;

    private GoogleSignInClient mGoogleSignInClient;

    private static final int RC_SIGN_IN = 9001;
    private CallbackManager mCallbackManager;

    private FirebaseAuth mAuth;

    LoginButton fb_login_button;
    CountryCodePicker ccp1;

    private FirebaseAuth.AuthStateListener authStateListener;
    private AccessTokenTracker accessTokenTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);
        session_management = new Session_management(SignUpActivity.this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        init();

    }

    private void init() {

        emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

//        token = FirebaseInstanceId.getInstance().getToken();
//        FirebaseApp.initializeApp(this);
//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(task -> {
//                    if (!task.isSuccessful()) {
//                        token = "";
//                        Log.i("Login", "getInstanceId failed", task.getException());
//                        return;
//                    }
//                    // Get new Instance ID token
//                    token = task.getResult().getToken();
//                });

//        flag_view = findViewById(R.id.flag_view);
//        country_c = findViewById(R.id.country_c);

        etName = findViewById(R.id.edt_name);
        etPhone = findViewById(R.id.edt_mobile);
        etEmail = findViewById(R.id.edt_email);
        etPAss = findViewById(R.id.edt_pass);
        sign_in_img = findViewById(R.id.sign_in_img);
        fb_login_button = findViewById(R.id.fb_login_button);
        fb_login_circular_button = findViewById(R.id.ivFbSignIn);
        ivGoogleSignIn = findViewById(R.id.ivGoogleSignIn);
        edt_referal_code = findViewById(R.id.edt_referal_code);
        ccp1 = findViewById(R.id.ccp1);
        skip = findViewById(R.id.skip);

        iv_back_login = findViewById(R.id.iv_back_login);

        iv_back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });




        //btnSignUP = findViewById(R.id.btnSignUP);
        btnSignUP = findViewById(R.id.btn_Signup);
       // btnLogin = findViewById(R.id.btn_Login);

        if(returnTo==null){
            returnTo="";
        }else{
            returnTo =getIntent().getStringExtra("return");
        }
        if (dbHandler == null) {
            dbHandler = new DatabaseHandler(this);
        }
//        country_c.setText("");

       /* skip.setOnClickListener(v -> {
            session_management.createLoginSession("", "", "", "", "", true,"");
            finish();
        });*/

/*
        flag_view.setOnClickListener(view -> startActivityForResult(new Intent(SignUpActivity.this, FlagActivity.class), 15));
*/

       /* btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            if(returnTo.equals("Order")){
                intent.putExtra("return","Order");
            }
            startActivity(intent);
            finishAffinity();
        });*/

        sign_in_img.setOnClickListener(v -> {
            if (etName.getText().toString().trim().equalsIgnoreCase("")) {
                Toast.makeText(getApplicationContext(), "Full name required!", Toast.LENGTH_SHORT).show();
            } else if (etEmail.getText().toString().trim().equalsIgnoreCase("")) {
                Toast.makeText(getApplicationContext(), "Email id required!", Toast.LENGTH_SHORT).show();
            } else if (!etEmail.getText().toString().trim().matches(emailPattern)) {
                Toast.makeText(getApplicationContext(), "Valid Email id required!", Toast.LENGTH_SHORT).show();
            } else if (etPhone.getText().toString().trim().equalsIgnoreCase("")) {
                Toast.makeText(getApplicationContext(), "Mobile Number required!", Toast.LENGTH_SHORT).show();
            } else if (etPhone.getText().toString().trim().length() < 9) {
                Toast.makeText(getApplicationContext(), "Valid Mobile Number required!", Toast.LENGTH_SHORT).show();
            } else if (etPAss.getText().toString().trim().equalsIgnoreCase("")) {
                Toast.makeText(getApplicationContext(), "Password required!", Toast.LENGTH_SHORT).show();
            } else if (!isOnline()) {
                Toast.makeText(getApplicationContext(), "Please check your Internet Connection!", Toast.LENGTH_SHORT).show();
            } else {

//                if (country_c.getText().toString() != null && !country_c.getText().toString().equalsIgnoreCase("")) {
//
//                } else {
//                    Toast.makeText(SignUpActivity.this, "Please select a country code!..", Toast.LENGTH_SHORT).show();
//                }
                progressDialog.show();
                signUpUrl();

            }
        });


        ivGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        fb_login_button.setReadPermissions("email","public_profile","user_friends");

        fb_login_circular_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fb_login_button.performClick();
            }
        });

        fb_login_button.registerCallback(   mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG,"onSuccess:"+loginResult);
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG,"onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG,"onError : "+error);
            }
        });

        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if (firebaseAuth.getCurrentUser() == null){
                    //Do anything here which needs to be done after signout is complete
                    updateUI(null);
                }
                else
                {
                    updateUI(user);
                }

            }
        };

        accessTokenTracker=new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if(currentAccessToken!=null)
                {
                    mAuth.signOut();
                    com.facebook.login.LoginManager.getInstance().logOut();
                    if(mGoogleSignInClient!=null)
                        mGoogleSignInClient.signOut();
                }
            }
        };

    }

    private void signUpUrl() {

//        if (token != null && !token.equalsIgnoreCase("")) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiBaseURL.SignUp, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("SignUP", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean status = jsonObject.getBoolean("status");
                        String msg = jsonObject.getString("message");
                        if (status) {
                            JSONObject resultObj = jsonObject.getJSONObject("result");

                            String user_name = resultObj.getString("custName");
                            String id = resultObj.getString("custID");
                            String user_email = resultObj.getString("cusEmail");
                            String user_phone = resultObj.getString("cusMob");
                            String user_dob = resultObj.getString("dob");
                            String user_country = resultObj.getString("country");
                            String user_state = resultObj.getString("state");
                            String user_city = resultObj.getString("city");
                            String user_zipcode = resultObj.getString("zipcode");
                            String user_countrycode = resultObj.getString("countrycode");
                            String area = resultObj.getString("area");
                            String password = "";
                            //  String otp_value = resultObj.getString("otp_value");
                            String block = resultObj.getString("custStatus");
                         //   String is_verified = resultObj.getString("is_verified");
                            String address1 = resultObj.getString("cusAdd1");
                            String address2 = resultObj.getString("cusAdd2");

                            String address = address1+" , "+address2;
                            if(address==null || address.equals(null) || address.equals("null"))
                            {
                                address="";
                            }
                            String st="1";
                            if(block.equals("Active"))
                            {
                                st="2";
                            }

                            session_management.setStreetArea(address1);
                            session_management.setHouseBuilding(address2);

                            session_management.createLoginSession(id, user_email, user_name, user_phone, password,address,"customer","");
                            session_management.setUserBlockStatus(st);

                            session_management.setUserid(id);
                            session_management.setUserFullName(user_name);
                            session_management.setUserMobile(user_phone);
                            session_management.setUserEmail(user_email);
                            session_management.setUserDOB(user_dob);
                            session_management.setUserCountry(user_country);
                            session_management.setUserState(user_state);
                            session_management.setUserCity(user_city);
                            session_management.setUserLandmark(address1);
                            session_management.setUserStreet(address2);
                            session_management.setAddress(area);
                            session_management.setUserPinCode(user_zipcode);
                            session_management.setUserCountryCode(user_countrycode);

                            if (dbHandler.getCartCount() > 0)
                            {
                                map = dbHandler.getCartAll();
                                addToCart();
                            }
                            else {
                                progressDialog.dismiss();
                                if(returnTo.equals("Order")){
//                                    Intent intent = new Intent(getApplicationContext(), CartActivity.class);
//                                    startActivity(intent);
//                                    finish();
                                }else{
                                    Intent intent = new Intent(getApplicationContext(), MainDrawerActivity.class);
                                    intent.putExtra("loadFrag",1);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                }
            }, error -> progressDialog.dismiss()) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("custName", etName.getText().toString().trim());
                    param.put("cusMob", etPhone.getText().toString().trim());
                    param.put("cusEmail", etEmail.getText().toString().trim());
                    param.put("CPassword", etPAss.getText().toString().trim());
                    param.put("DeviceName", "Android");
                    param.put("countryCode", ccp1.getSelectedCountryCode());
                    param.put("referralCode", edt_referal_code.getText().toString().trim());

                    Log.d("TAG", "getParams: "+param);
                    return param;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.getCache().clear();
            requestQueue.add(stringRequest);
//        } else {
//            FirebaseInstanceId.getInstance().getInstanceId()
//                    .addOnCompleteListener(task -> {
//                        if (!task.isSuccessful()) {
//                            token = "";
//                            Log.i("Login", "getInstanceId failed", task.getException());
//                            return;
//                        }
//
//                        // Get new Instance ID token
//                        token = task.getResult().getToken();
//                        signUpUrl();
//                    });
//        }


    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    void handleFacebookToken(AccessToken token)
    {
        AuthCredential credential= FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Log.e(TAG,"sign in with credential successful");
                    FirebaseUser user=mAuth.getCurrentUser();
                    updateUI(user);
                }
                else {
                    // If sign in fails, display a message to the user.
                    Log.e(TAG, "signInWithCredential:failure", task.getException());

                    updateUI(null);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 15) {
            if (data != null && data.getBooleanExtra("flagSelected", false)) {
                countryCode = data.getStringExtra("countrycode");
                countryFlag = data.getIntExtra("countryflag", -1);
                if (countryCode.equalsIgnoreCase("")) {
                    Toast.makeText(SignUpActivity.this, "Please select a vaild country code!..", Toast.LENGTH_SHORT).show();
                } else {
                    country_c.setText(countryCode);
                }
            }
        }

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.e("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.e("TAG", "Google sign in failed", e);
            }
        }
    }

    public void addToCart()
    {
        String tag_json_obj = "json_cart_add_req";
        String custID= session_management.getUserDetails().get(BaseURL.KEY_ID);
        Map<String, String> params = new HashMap<String, String>();
        params.put("CustID", custID);
        params.put("ItemId",map.get(uploadPosition).get("ItemId"));
        params.put("Price", map.get(uploadPosition).get("price"));
        params.put("Quantity",map.get(uploadPosition).get("qty"));
        params.put("SupplierID",map.get(uploadPosition).get("supplierID"));

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ApiBaseURL.Cart, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("CheckApiCart", response.toString());
                try {
                    boolean status = response.getBoolean("status");
                    if (status) {
                        int c=dbHandler.getCartCount()-1;
                        if(c>uploadPosition)
                        {
                            uploadPosition++;
                            addToCart();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            if(returnTo.equals("Order")){
                                intent.putExtra("return","Order");
                            }
                            startActivity(intent);
                            finish();
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

    public void onClickNext(View view) {
        startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
        finish();
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {

        if (user != null) {
            progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiBaseURL.GetCustomer, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SignUP", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean status = jsonObject.getBoolean("status");
                    String msg = jsonObject.getString("message");
                    if (status) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject obj = jsonArray.getJSONObject(i);

                            String user_id = obj.getString("custID");
                            String user_fullname = obj.getString("custName");
                            String user_email = obj.getString("cusEmail");
                            String user_phone = obj.getString("cusMob");
                            String user_dob = obj.getString("dob");
                            String user_country = obj.getString("country");
                            String user_state = obj.getString("state");
                            String user_city = obj.getString("city");
                            String user_zipcode = obj.getString("zipcode");
                            String user_countrycode = obj.getString("countrycode");
                            String area = obj.getString("area");
                            String password ="";
                            String block = obj.getString("custStatus");
                            String address1 = obj.getString("cusAdd1");
                            String address2 = obj.getString("cusAdd2");
                            String latitude = obj.getString("latitude");
                            String longitude = obj.getString("longitude");
                            String isSupplier = obj.getString("isSupplier");
                            String role="customer";
                            String supplierId="";
                            if(isSupplier!=null && !isSupplier.equals("") && !isSupplier.equals("null"))
                            {
                                role="supplier";
                                supplierId= obj.getString("supplierID");
                            }
                            String address = address1+" , "+address2;
                            if(address==null || address.equals(null) || address.equals("null"))
                            {
                                address="";
                            }
                            String st="1";
                            if(block.equals("Active"))
                            {
                                st="2";
                            }
                            // progressDialog.dismiss();
                            SharedPreferences.Editor editor = getSharedPreferences(BaseURL.MyPrefreance, MODE_PRIVATE).edit();
                            editor.putString(BaseURL.KEY_MOBILE, user_phone);
                            editor.putString(BaseURL.KEY_PASSWORD, password);
                            editor.apply();
                            session_management.setStreetArea(address1);
                            session_management.setHouseBuilding(address2);
                            session_management.createLoginSession(user_id, user_email, user_fullname, user_phone, password, address,role,supplierId);
                            session_management.setUserBlockStatus(st);

                            session_management.setUserid(user_id);
                            session_management.setUserFullName(user_fullname);
                            session_management.setUserMobile(user_phone);
                            session_management.setUserEmail(user_email);
                            session_management.setUserDOB(user_dob);
                            session_management.setUserCountry(user_country);
                            session_management.setUserState(user_state);
                            session_management.setUserCity(user_city);
                            session_management.setUserLandmark(address1);
                            session_management.setUserStreet(address2);
                            session_management.setAddress(area);
                            session_management.setUserPinCode(user_zipcode);
                            session_management.setUserCountryCode(user_countrycode);

                            if (latitude == null && longitude== null)
                            {
                                session_management.setLocationPref("","");
                            }
                            else
                            {
                                session_management.setLocationPref(latitude,longitude);
                            }

                            if (dbHandler.getCartCount() > 0)
                            {
                                map = dbHandler.getCartAll();
                                addToCart();
                            }
                            else {
                                progressDialog.dismiss();

                                Intent intent = new Intent(getApplicationContext(), MainDrawerActivity.class);
                                intent.putExtra("loadFrag",1);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                finish();

                                /*if (returnTo.equals("Order")){
                                    Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                    finish();
                                }else{
                                    Intent intent = new Intent(getApplicationContext(), MainDrawerActivity.class);
                                     intent.putExtra("loadFrag",1);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                    finish();
                                }*/
                            }

                        }

                        /*JSONObject resultObj = jsonObject.getJSONObject("result");

                        String user_name = resultObj.getString("custName");
                        String id = resultObj.getString("custID");
                        String user_email = resultObj.getString("cusEmail");
                        String user_phone = resultObj.getString("cusMob");
                        String password = "";
                        //  String otp_value = resultObj.getString("otp_value");
                        String block = resultObj.getString("custStatus");
                        //   String is_verified = resultObj.getString("is_verified");
                        String address1 = resultObj.getString("cusAdd1");
                        String address2 = resultObj.getString("cusAdd2");

                        String address = address1 + " , " + address2;
                        if (address == null || address.equals(null) || address.equals("null")) {
                            address = "";
                        }
                        String st = "1";
                        if (block.equals("Active")) {
                            st = "2";
                        }

                        session_management.setStreetArea(address1);
                        session_management.setHouseBuilding(address2);

                        session_management.createLoginSession(id, user_email, user_name, user_phone, password, address, "customer", "");
                        session_management.setUserBlockStatus(st);
                        if (dbHandler.getCartCount() > 0) {
                            map = dbHandler.getCartAll();
                            addToCart();
                        } else {
                            progressDialog.dismiss();
                            if (returnTo.equals("Order")) {
                                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(getApplicationContext(), MainDrawerActivity.class);
                                 intent.putExtra("loadFrag",1);
                                startActivity(intent);
                                finish();
                            }

                        }*/
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                    } else {
                        logoutAll();
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful())
                                {
                                    Log.d(TAG,"deleted");
                                }
                                else{
                                    Log.d(TAG,"failed delete");
                                }
                            }
                        });
                        progressDialog.dismiss();

                        if (user.getDisplayName() == null || user.getDisplayName().isEmpty()) {
                        } else {
                            etName.setText(user.getDisplayName());
                            etName.setEnabled(false);
                            etName.setFocusable(false);
                        }

                        if (user.getEmail() == null || user.getEmail().isEmpty()) {
                        } else {
                            etEmail.setText(user.getEmail());
                            etEmail.setEnabled(false);
                            etEmail.setFocusable(false);
                        }


                        if(user.getPhoneNumber()==null || user.getPhoneNumber().isEmpty()){
                        }else{
                            etPhone.setText(user.getPhoneNumber());
                            etPhone.setEnabled(false);
                            etPhone.setFocusable(false);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
                progressDialog.dismiss();
            }
        }, error -> progressDialog.dismiss()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("cusEmail", user.getEmail());
                Log.d("TAG", "getParams: " + param);
                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        //mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener!=null)
        {
            //mAuth.removeAuthStateListener(authStateListener);
        }
    }

    void logoutAll()
    {
        mAuth.signOut();
        com.facebook.login.LoginManager.getInstance().logOut();
        if(mGoogleSignInClient!=null)
            mGoogleSignInClient.signOut();
    }
}
