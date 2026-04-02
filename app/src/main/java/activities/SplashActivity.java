package activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.grocery.QTPmart.R;

//import util.Session_management;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;
//    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;
    SharedPreferences sharedPreferences;
//    Session_management session_management;
//    private DatabaseHandler dbcart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setFinishOnTouchOutside(true);
        //  AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        session_management = new Session_management(Splash.this);
//        dbcart = new DatabaseHandler(this);
//        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(this)) {
            redirectionScreen();
//        }else {
//            if(!hasGPSDevice(this)){
//                finish();
//            }
//            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(this)) {
//                enableLoc();
//            }
//        }
//        session_management.setCurrency("AED ", "AED ");
//        session_management.setCouponCode("","");
//        session_management.setMainPopUp(true);
//        session_management.setCartPopUp(true);
//        session_management.setCategoryPopUp(true);
//        fetchBlockStatus();
    }

//    private void fetchBlockStatus() {
//        if (!session_management.userId().equalsIgnoreCase("")){
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiBaseURL.USERBLOCKAPI, response -> {
//                Log.d("adresssHoww",response);
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    boolean status = jsonObject.getBoolean("status");
//                    String msg = jsonObject.getString("message");
//                    if (status){
//                        session_management.setUserBlockStatus("2");
//                    }
//                    else {
//                        session_management.setUserBlockStatus("1");
//
//                        ShowLoginAlerts(msg);
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }, error -> {
//            }){
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    HashMap<String,String> param = new HashMap<>();
//                    param.put("custId",session_management.userId());
//                    return param;
//                }
//            };
//            RequestQueue requestQueue = Volley.newRequestQueue(Splash.this);
//            requestQueue.getCache().clear();
//            requestQueue.add(stringRequest);
//        }
//    }


    private void redirectionScreen(){
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
//                Intent intent1 =new Intent(SplashActivity.this,LoginActivity.class);

                /* Create an Intent that will start the Menu-Activity. */
//                session_management = new Session_management(Splash.this);
//                SharedPreferences preferences =
//                        getSharedPreferences("GOGrocer", MODE_PRIVATE);
//
//                if(!preferences.getBoolean("onboarding_complete",false))
//                {
//                    Intent onboarding = new Intent(Splash.this, GetStartedActivity1.class);
//                    startActivity(onboarding);
//
//                    finish();
//                }else if(session_management.isLoggedIn()) {
//                    Intent intent1 =new Intent(Splash.this,MainDrawerActivity.class);
//                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    intent1.putExtra("loadFrag",1);
//                    startActivity(intent1);
//                    finish();
//                } else if (session_management.isSkip()){
//                    Intent intent1 =new Intent(Splash.this,MainDrawerActivity.class);
//                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent1);
//                    finish();
//                } else {
                    Intent intent1 =new Intent(SplashActivity.this,LoginActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent1);
                    finish();
//                }
            }
        },SPLASH_DISPLAY_LENGTH);

    }

//    private boolean hasGPSDevice(Context context) {
//        final LocationManager mgr = (LocationManager) context
//                .getSystemService(Context.LOCATION_SERVICE);
//        if (mgr == null)
//            return false;
//        final List<String> providers = mgr.getAllProviders();
//        if (providers == null)
//            return false;
//        return providers.contains(LocationManager.GPS_PROVIDER);
//    }
//    private void enableLoc() {
//
//        if (googleApiClient == null) {
//            googleApiClient = new GoogleApiClient.Builder(getApplicationContext()).addApi(LocationServices.API).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
//                        @Override
//                        public void onConnected(Bundle bundle) {
//                        }
//
//                        @Override
//                        public void onConnectionSuspended(int i) {
//                            googleApiClient.connect();
//                        }
//                    })
//                    .addOnConnectionFailedListener(connectionResult -> Log.e("Location error","Location error " + connectionResult.getErrorCode())).build();
//            googleApiClient.connect();
//
//            LocationRequest locationRequest = LocationRequest.create();
//            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//            locationRequest.setInterval(7 * 1000);  //30 * 1000
//            locationRequest.setFastestInterval(5 * 1000); //5 * 1000
//            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                    .addLocationRequest(locationRequest);
//
//            builder.setAlwaysShow(true);
//
//            PendingResult<LocationSettingsResult> result =
//                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient,builder.build());
//            result.setResultCallback(result1 -> {
//
//                final Status status = result1.getStatus();
//                if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
//                    try {
//                        // Show the dialog by calling startResolutionForResult(),
//                        // and check the result in onActivityResult().
//                        status.startResolutionForResult(Splash.this, REQUEST_LOCATION);
//
//                    } catch (IntentSender.SendIntentException e) {
//                        // Ignore the error.
//                    }
//                }
//            });
//        }
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
// Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        redirectionScreen();

                        break;
                    case Activity.RESULT_CANCELED:
                        finish();
                        break;
                }
                break;
        }
    }

//    @SuppressLint("NewApi")
//    public void ShowLoginAlerts(String message) {
//
//
//        SweetAlertDialog alertDialog = new SweetAlertDialog(Splash.this, SweetAlertDialog.WARNING_TYPE);
//        alertDialog.setTitleText(message);
//        alertDialog.setConfirmText("OK");
//        alertDialog.setCancelText("Logout");
//        alertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//            @Override
//            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                finishAffinity();
//            }
//        });
//
//        alertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//            @Override
//            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                dbcart.clearCart();
//                dbcart.clearWishlist();
//                session_management.logoutSession();
//                session_management.setCurrency("AED ", "AED ");
//                finish();
//
//
//            }
//        });
//        alertDialog.show();
//        Button btns = (Button) alertDialog.findViewById(R.id.confirm_button);
//        LinearLayout.LayoutParams layoutParams1s  = new LinearLayout.LayoutParams(300, 130);
//        layoutParams1s.setMargins(10,0,10,0);
//        btns.setLayoutParams(layoutParams1s);
//        btns.setBackground(getResources().getDrawable(R.drawable.custom_dialog_button));
//        btns.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
//        btns.setGravity(Gravity.CENTER);
//
//
//        Button btn1s = (Button) alertDialog.findViewById(R.id.cancel_button);
//        LinearLayout.LayoutParams layoutParams12  = new LinearLayout.LayoutParams(300, 130);
//        layoutParams12.setMargins(10,0,10,0);
//        btn1s.setLayoutParams(layoutParams12);
//        btn1s.setBackground(getResources().getDrawable(R.drawable.custom_dialog_button));
//        btn1s.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryDark)));
//        btn1s.setGravity(Gravity.CENTER);
//
//
//
//    }
}