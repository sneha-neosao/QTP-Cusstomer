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
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
//import com.grocery.QTPmart.Config.ApiBaseURL;
//import com.grocery.QTPmart.Config.BaseURL;
//import com.grocery.QTPmart.R;
//import com.grocery.QTPmart.network.ApiInterface;
//import com.grocery.QTPmart.util.AppController;
//import com.grocery.QTPmart.util.CustomVolleyJsonRequest;
//import com.grocery.QTPmart.util.DatabaseHandler;
//import com.grocery.QTPmart.util.Session_management;
import com.google.android.material.textfield.TextInputEditText;
import com.grocery.QTPmart.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import util.Session_management;


public class LoginActivity extends AppCompatActivity  /*implements GoogleApiClient.OnConnectionFailedListener*/ {
    static final String TAG = "LoginActivity";
    Button SignIn;
    TextInputEditText edt_email, edt_pass;
    ImageView login_btn;
    TextView btn_click_here_login, tv_forgot_password;


    TextView forgotPAss, btnignUp;
    String token;
    ProgressDialog progressDialog;
    LinearLayout skip;
    LinearLayout flag_view;
    TextView country_c;
    private Session_management sessionManagement;
    private int countryFlag = -1;
    private String countryCode = "";
    //    DatabaseHandler dbHandler;
    int uploadPosition = 0;
    ArrayList<HashMap<String, String>> map;
    String returnTo = "";

    ImageView fb_login_circular_button, ivGoogleSignIn;

//    private GoogleSignInClient mGoogleSignInClient;

    private static final int RC_SIGN_IN = 9001;
//    private CallbackManager mCallbackManager;

//    private FirebaseAuth mAuth;

//    LoginButton fb_login_button;

//    private FirebaseAuth.AuthStateListener authStateListener;
//    private AccessTokenTracker accessTokenTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getIntent().getStringExtra("return") == null) {
            returnTo = "";
        } else {
            returnTo = getIntent().getStringExtra("return");
        }

        try {
            FirebaseApp.initializeApp(this);
            if (!FirebaseApp.getApps(this).isEmpty()) {
                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                token = "";
                                Log.e("Login", "getInstanceId failed", task.getException());
                                return;
                            }

                            // Get new Instance ID token
                            token = task.getResult().getToken();
                        });
            }
        } catch (Exception e) {
            Log.e("Login", "Firebase not initialized", e);
        }

        sessionManagement = new Session_management(LoginActivity.this);
        // new Thread(this::checkUserNotify).start();
//
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();

//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Initialize Firebase Auth
//        mAuth = FirebaseAuth.getInstance();

//        FacebookSdk.sdkInitialize(getApplicationContext());
//        mCallbackManager = CallbackManager.Factory.create();
        init();
    }

    private void init() {

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

//        if (dbHandler == null) {
//            dbHandler = new DatabaseHandler(this);
//        }
        edt_email = findViewById(R.id.edt_email);
        edt_pass = findViewById(R.id.edt_pass);
//        SignIn = findViewById(R.id.btn_Login);
//        flag_view = findViewById(R.id.flag_view);
//        country_c = findViewById(R.id.country_c);
        forgotPAss = findViewById(R.id.tv_forgot_password);
        btnignUp = findViewById(R.id.btn_click_here_login);
        skip = findViewById(R.id.skip);
        btn_click_here_login = findViewById(R.id.btn_click_here_login);
        tv_forgot_password = findViewById(R.id.tv_forgot_password);
//        login_btn = findViewById(R.id.login_btn);
        ivGoogleSignIn = findViewById(R.id.ivGoogleSignIn);

//        fb_login_button = findViewById(R.id.fb_login_button);
        fb_login_circular_button = findViewById(R.id.ivFbSignIn);

       /* try {
            PackageInfo info = getPackageManager().getPackageInfo("com.grocery.QTPmart", PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String sign= Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.e("MY KEY HASH:", sign);

                //6p7cFtZ3F/rnQYH+nXD2TUno+uk=

            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("exception:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("exception:", e.toString());
        }*/

//        country_c.setText("");

        //  checkOtpStatus();

//        flag_view.setOnClickListener(view -> startActivityForResult(new Intent(LoginActivity.this, FlagActivity.class), 15));

        skip.setOnClickListener(v -> {
            sessionManagement.createLoginSession("", "", "", "", "" ,"","", "");
            Intent intent = new Intent(LoginActivity.this, MainDrawerActivity.class);
            intent.putExtra("loadFrag",1);
            startActivity(intent);
            finish();
        });

        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassOtp.class);
                startActivity(intent);

            }
        });
        btn_click_here_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
                 finish();
            }
        });

//        login_btn.setOnClickListener(v -> {
//            if (edt_email.getText().toString().trim().equalsIgnoreCase("")) {
//                Toast.makeText(getApplicationContext(), "Mobile Number required!", Toast.LENGTH_SHORT).show();
//            } else if (edt_email.getText().toString().trim().length() < 9) {
//                Toast.makeText(getApplicationContext(), "Valid Mobile Number required!", Toast.LENGTH_SHORT).show();
//            } else if (edt_pass.getText().toString().trim().equalsIgnoreCase("")) {
//                Toast.makeText(getApplicationContext(), "Password required!", Toast.LENGTH_SHORT).show();
//            } else if (!isOnline()) {
//                Toast.makeText(getApplicationContext(), "Please check your Internet Connection!", Toast.LENGTH_SHORT).show();
//            } else {
////                if (country_c.getText().toString() != null && !country_c.getText().toString().equalsIgnoreCase("")) {
////
////
////                } else {
////                    Toast.makeText(LoginActivity.this, "Please select a country code!..", Toast.LENGTH_SHORT).show();
////                }
//                progressDialog.show();
//                loginUrl();
//
//
//            }
//        });

//        ivGoogleSignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signIn();
//            }
//        });

//        fb_login_button.setReadPermissions("email","public_profile","user_friends");
//
//        fb_login_circular_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fb_login_button.performClick();
//            }
//        });
//
//        fb_login_button.registerCallback(   mCallbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                Log.e(TAG,"onSuccess:"+loginResult);
//                handleFacebookToken(loginResult.getAccessToken());
//            }
//
//            @Override
//            public void onCancel() {
//                Log.e(TAG,"onCancel");
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                Log.e(TAG,"onError : "+error);
//            }
//        });

//        authStateListener=new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user=firebaseAuth.getCurrentUser();
//                if (firebaseAuth.getCurrentUser() == null){
//                    //Do anything here which needs to be done after signout is complete
//                    updateUI(null);
//                }
//                else
//                {
//                    updateUI(user);
//                }
//
//            }
//        };

//        accessTokenTracker=new AccessTokenTracker() {
//            @Override
//            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
//                if(currentAccessToken!=null)
//                {
//                    mAuth.signOut();
//                    com.facebook.login.LoginManager.getInstance().logOut();
//                    if(mGoogleSignInClient!=null)
//                        mGoogleSignInClient.signOut();
//                }
//            }
//        };
//
//        map = dbHandler.getCartAll();
//        Log.e("loginMap",String.valueOf(map));
//    }

//    void handleFacebookToken(AccessToken token)
//    {
//        AuthCredential credential= FacebookAuthProvider.getCredential(token.getToken());
//        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if(task.isSuccessful())
//                {
//                    Log.d(TAG,"sign in with credential successful");
//                    FirebaseUser user=mAuth.getCurrentUser();
//                    updateUI(user);
//                }
//                else {
//                    // If sign in fails, display a message to the user.
//                    Log.w(TAG, "signInWithCredential:failure", task.getException());
//
//                    updateUI(null);
//                }
//            }
//        });
//    }
//
//    private void signIn() {
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }


//        private void loginUrl () {
//
//            //  if (token != null && !token.equalsIgnoreCase("")) {
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiBaseURL.Login2, response -> {
//                Log.d("Login", response);
//
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    boolean status = jsonObject.getBoolean("status");
//                    String msg = jsonObject.getString("message");
//                    if (status) {
//                        progressDialog.dismiss();
//                        //JSONArray jsonArray = jsonObject.getJSONArray("result");
//                        //for (int i = 0; i < jsonArray.length(); i++) {
//
//                        //JSONObject obj = jsonArray.getJSONObject(i);
//                        JSONObject obj = jsonObject.getJSONObject("result");
//
//                        String user_id = obj.getString("custID");
//                        String user_fullname = obj.getString("custName");
//                        String user_email = obj.getString("cusEmail");
//                        String user_phone = obj.getString("cusMob");
//                        String user_dob = obj.getString("dob");
//                        String user_country = obj.getString("country");
//                        String user_state = obj.getString("state");
//                        String user_city = obj.getString("city");
//                        String user_zipcode = obj.getString("zipcode");
//                        String user_countrycode = obj.getString("countrycode");
//                        String area = obj.getString("area");
//                        String password = "";
//                        String block = obj.getString("custStatus");
//                        String address1 = obj.getString("cusAdd1");
//                        String address2 = obj.getString("cusAdd2");
//                        String latitude = obj.getString("latitude");
//                        String longitude = obj.getString("longitude");
//                        String isSupplier = obj.getString("isSupplier");
//                        String role = "customer";
//                        String supplierId = "";
//                        if (isSupplier != null && !isSupplier.equals("") && !isSupplier.equals("null")) {
//                            if (isSupplier.equals("1")) {
//                                role = "supplier";
//                                supplierId = obj.getString("supplierID");
//                            }
//
//                        }
//                        String address = address1 + " , " + address2;
//                        if (address == null || address.equals(null) || address.equals("null")) {
//                            address = "";
//                        }
//                        String st = "1";
//                        if (block.equals("Active")) {
//                            st = "2";
//                        }
//                        // progressDialog.dismiss();
//                        SharedPreferences.Editor editor = getSharedPreferences(BaseURL.MyPrefreance, MODE_PRIVATE).edit();
//                        editor.putString(BaseURL.KEY_MOBILE, user_phone);
//                        editor.putString(BaseURL.KEY_PASSWORD, password);
//                        editor.apply();
//                        sessionManagement.setStreetArea(address1);
//                        sessionManagement.setHouseBuilding(address2);
//                        sessionManagement.createLoginSession(user_id, user_email, user_fullname, user_phone, password, area, role, supplierId);
//                        sessionManagement.setUserBlockStatus(st);
//
//                        sessionManagement.setUserid(user_id);
//                        sessionManagement.setUserFullName(user_fullname);
//                        sessionManagement.setUserMobile(user_phone);
//                        sessionManagement.setUserEmail(user_email);
//                        sessionManagement.setUserDOB(user_dob);
//                        sessionManagement.setUserCountry(user_country);
//                        sessionManagement.setUserState(user_state);
//                        sessionManagement.setUserCity(user_city);
//                        sessionManagement.setUserLandmark(address1);
//                        sessionManagement.setUserStreet(address2);
//                        sessionManagement.setAddress(area);
//                        sessionManagement.setUserPinCode(user_zipcode);
//                        sessionManagement.setUserCountryCode(user_countrycode);
//
//                        if (latitude == null && longitude == null) {
//                            sessionManagement.setLocationPref("", "");
//                        } else {
//                            sessionManagement.setLocationPref(latitude, longitude);
//                        }
//
//                        if (dbHandler.getCartCount() > 0) {
//                            map = dbHandler.getCartAll();
//                            addToCart();
//                        } else {
//                            progressDialog.dismiss();
//
//                            if (returnTo.equals("Order")) {
//                                Intent intent = new Intent(getApplicationContext(), MainDrawerActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                intent.putExtra("loadFrag", 1);
//                                intent.putExtra("goToCart", 1);
//                                startActivity(intent);
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//                                finish();
//
//                            } else {
//                                Intent intent = new Intent(getApplicationContext(), MainDrawerActivity.class);
//                                intent.putExtra("loadFrag", 1);
//                                startActivity(intent);
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//                                finish();
//                            }
//                        }
//
//                        //}
//                    } else {
//                        progressDialog.dismiss();
//                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    progressDialog.dismiss();
//                }
//
//            }, (VolleyError error) -> {
//                progressDialog.dismiss();
//            }) {
//
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    HashMap<String, String> param = new HashMap<>();
////String cc = countryCode.replace("+","");
//                    param.put("cusMob", edt_email.getText().toString());
//                    param.put("CPassword", edt_pass.getText().toString());
////                    param.put("branchid", ApiInterface.branchcode);
//                    return param;
//                }
//            };
//
//            RequestQueue requestQueue = Volley.newRequestQueue(this);
//            requestQueue.getCache().clear();
//            requestQueue.add(stringRequest);
//            //  } else {
////            FirebaseInstanceId.getInstance().getInstanceId()
////                    .addOnCompleteListener(task -> {
////                        if (!task.isSuccessful()) {
////                            token = "";
////                            Log.i("Login", "getInstanceId failed", task.getException());
////                            return;
////                        }
////
////                        // Get new Instance ID token
////                        token = task.getResult().getToken();
////                        loginUrl();
////                    });
//            // }
//
//        }

//    private boolean isOnline() {
//        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        mCallbackManager.onActivityResult(requestCode,resultCode,data);
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 15) {
//            if (data != null && data.getBooleanExtra("flagSelected", false)) {
//                countryCode = data.getStringExtra("countrycode");
//                countryFlag = data.getIntExtra("countryflag", -1);
//                if (countryCode.equalsIgnoreCase("")) {
//                    Toast.makeText(LoginActivity.this, "Please select a vaild country code!..", Toast.LENGTH_SHORT).show();
//                } else {
//                    country_c.setText(countryCode);
//                }
//            }
//        }
//
//        if (requestCode == RC_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
//                firebaseAuthWithGoogle(account.getIdToken());
//            } catch (ApiException e) {
//                // Google Sign In failed, update UI appropriately
//                Log.w("TAG", "Google sign in failed", e);
//            }
//        }
//    }
//
//    private void firebaseAuthWithGoogle(String idToken) {
//        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.e(TAG, "signInWithCredential:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.e(TAG, "signInWithCredential:failure", task.getException());
//                            updateUI(null);
//                        }
//                    }
//                });
//    }
//
//
//    private void updateUI(FirebaseUser user)
//    {
//        if(user!=null) {
//            progressDialog.show();
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiBaseURL.GetCustomer, response -> {
//                Log.e("Login", response);
//
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    boolean status = jsonObject.getBoolean("status");
//                    String msg = jsonObject.getString("message");
//                    if (status) {
//                        JSONArray jsonArray = jsonObject.getJSONArray("result");
//                        for (int i = 0; i < jsonArray.length(); i++) {
//
//                            JSONObject obj = jsonArray.getJSONObject(i);
//
//                            String user_id = obj.getString("custID");
//                            String user_fullname = obj.getString("custName");
//                            String user_email = obj.getString("cusEmail");
//                            String user_phone = obj.getString("cusMob");
//                            String user_dob = obj.getString("dob");
//                            String user_country = obj.getString("country");
//                            String user_state = obj.getString("state");
//                            String user_city = obj.getString("city");
//                            String user_zipcode = obj.getString("zipcode");
//                            String user_countrycode = obj.getString("countrycode");
//                            String area = obj.getString("area");
//                            String password ="";
//                            String block = obj.getString("custStatus");
//                            String address1 = obj.getString("cusAdd1");
//                            String address2 = obj.getString("cusAdd2");
//                            String latitude = obj.getString("latitude");
//                            String longitude = obj.getString("longitude");
//                            String isSupplier = obj.getString("isSupplier");
//                            String role="customer";
//                            String supplierId="";
//
//                            if(isSupplier!=null && !isSupplier.equals("") && !isSupplier.equals("null"))
//                            {
//                                if(isSupplier.equals("1")){
//                                    role="supplier";
//                                    supplierId= obj.getString("supplierID");
//                                }
//                            }
//                            String address = address1+" , "+address2;
//                            if(address==null || address.equals(null) || address.equals("null"))
//                            {
//                                address="";
//                            }
//                            String st="1";
//                            if(block.equals("Active"))
//                            {
//                                st="2";
//                            }
//
//
//
//                            // progressDialog.dismiss();
//                            SharedPreferences.Editor editor = getSharedPreferences(BaseURL.MyPrefreance, MODE_PRIVATE).edit();
//                            editor.putString(BaseURL.KEY_MOBILE, user_phone);
//                            editor.putString(BaseURL.KEY_PASSWORD, password);
//                            editor.apply();
//                            sessionManagement.setStreetArea(address1);
//                            sessionManagement.setHouseBuilding(address2);
//                            sessionManagement.createLoginSession(user_id, user_email, user_fullname, user_phone, password, address,role,supplierId);
//                            sessionManagement.setUserBlockStatus(st);
//
//                            sessionManagement.setUserid(user_id);
//                            sessionManagement.setUserFullName(user_fullname);
//                            sessionManagement.setUserMobile(user_phone);
//                            sessionManagement.setUserEmail(user_email);
//                            sessionManagement.setUserDOB(user_dob);
//                            sessionManagement.setUserCountry(user_country);
//                            sessionManagement.setUserState(user_state);
//                            sessionManagement.setUserCity(user_city);
//                            sessionManagement.setUserLandmark(address1);
//                            sessionManagement.setUserStreet(address2);
//                            sessionManagement.setAddress(area);
//                            sessionManagement.setUserPinCode(user_zipcode);
//                            sessionManagement.setUserCountryCode(user_countrycode);
//
//                            if (latitude == null && longitude== null)
//                            {
//                                sessionManagement.setLocationPref("","");
//                            }
//                            else
//                            {
//                                sessionManagement.setLocationPref(latitude,longitude);
//                            }
//
//                            if (dbHandler.getCartCount() > 0)
//                            {
//                                map = dbHandler.getCartAll();
//                                addToCart();
//                            }
//                            else {
//                                progressDialog.dismiss();
//                                Intent intent = new Intent(getApplicationContext(), MainDrawerActivity.class);
//                                intent.putExtra("loadFrag",1);
//                                startActivity(intent);
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//                                finish();
//
//                                /*if (returnTo.equals("Order")){
//                                    Intent intent = new Intent(getApplicationContext(), CartActivity.class);
//                                    startActivity(intent);
//                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//                                    finish();
//                                }else{
//                                    Intent intent = new Intent(getApplicationContext(), MainDrawerActivity.class);
//                                     intent.putExtra("loadFrag",1);
//                                    startActivity(intent);
//                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//                                    finish();
//                                }*/
//                            }
//
//                        }
//                    } else {
//                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//                        logoutAll();
//                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//
//                                if(task.isSuccessful())
//                                {
//                                    Log.e(TAG,"deleted");
//                                }
//                                else{
//                                    Log.e(TAG,"failed delete");
//                                }
//                            }
//                        });
//                        progressDialog.dismiss();
//
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    progressDialog.dismiss();
//                }
//
//            }, (VolleyError error) -> {
//                progressDialog.dismiss();
//            }) {
//
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    HashMap<String, String> param = new HashMap<>();
//                    param.put("cusEmail", user.getEmail());
//                    /*param.put("CPassword", edt_pass.getText().toString());
//                    param.put("branchid", ApiInterface.branchcode);
//                    Log.d("ff", token);*/
//                    return param;
//                }
//            };
//
//            RequestQueue requestQueue = Volley.newRequestQueue(this);
//            requestQueue.getCache().clear();
//            requestQueue.add(stringRequest);
//        }
//    }
//
//
//    public void addToCart()
//    {
//        String tag_json_obj = "json_cart_add_req";
//        String custID= sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("CustID", custID);
//        params.put("ItemId",map.get(uploadPosition).get("ItemId"));
//        params.put("Price", map.get(uploadPosition).get("price"));
//        params.put("Quantity",map.get(uploadPosition).get("qty"));
//        params.put("SupplierID",map.get(uploadPosition).get("supplierID"));
//        params.put("unitID",map.get(uploadPosition).get("varient_id"));
//        Log.e("addCartLogin",String.valueOf(params));
//
//        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
//                ApiBaseURL.Cart, params, new com.android.volley.Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.e("CheckApiCart", response.toString());
//                try {
//                    boolean status = response.getBoolean("status");
//                    if (status) {
//                        int c=dbHandler.getCartCount()-1;
//                        if(c>uploadPosition)
//                        {
//                            uploadPosition++;
//                            addToCart();
//                        }
//                        else
//                        {
//                            progressDialog.dismiss();
//                            if(returnTo.equals("Order")){
//                                Intent intent = new Intent(getApplicationContext(), MainDrawerActivity.class);
//                                intent.putExtra("loadFrag",1);
//                                intent.putExtra("goToCart",1);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(intent);
//                                finish();
//                            }else {
//                                Intent intent = new Intent(getApplicationContext(), MainDrawerActivity.class);
//                                intent.putExtra("loadFrag",1);
//                                startActivity(intent);
//                                finish();
//                            }
//                        }
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    progressDialog.dismiss();
//                }
//
//
//            }
//        }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//                progressDialog.dismiss();
//                VolleyLog.e("", "Error: " + error.getMessage());
//                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                }
//            }
//        });
//
//        // Adding request to request queue
//        jsonObjReq.setRetryPolicy(new RetryPolicy() {
//            @Override
//            public int getCurrentTimeout() {
//                return 60000;
//            }
//
//            @Override
//            public int getCurrentRetryCount() {
//                return 0;
//            }
//
//            @Override
//            public void retry(VolleyError error) throws VolleyError {
//
//            }
//        });
//        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
//    /*@Override
//    protected void onStart() {
//        super.onStart();
//        mAuth.addAuthStateListener(authStateListener);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if(authStateListener!=null)
//        {
//            mAuth.removeAuthStateListener(authStateListener);
//        }
//    }*/
//
//    void logoutAll()
//    {
//        mAuth.signOut();
//        com.facebook.login.LoginManager.getInstance().logOut();
//        if(mGoogleSignInClient!=null)
//            mGoogleSignInClient.signOut();
//    }
    }
}