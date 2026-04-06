package activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import Config.ApiBaseURL;
import Config.BaseURL;
import ModelClass.AddressModel;
import ModelClass.CityModel;
import ModelClass.CountryModel;
import ModelClass.StateModel;
import com.grocery.QTPmart.R;
import util.AppController;
import util.CustomVolleyJsonRequest;
import util.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditAddressActivity extends AppCompatActivity implements View.OnTouchListener {

    ImageView img_back;
    TextView txt_currentLocation;
    Button btn_next;
    List<String> countryNames=new ArrayList<>();
    List<String> cityNames=new ArrayList<>();
    List<String> stateNames=new ArrayList<>();
    List<String> countryCodes=new ArrayList<>();
    List<String> cityCodes=new ArrayList<>();
    List<String> stateCodes=new ArrayList<>();
    List<String> types=new ArrayList<>();
    List<CountryModel> countryModelList=new ArrayList<>();
    List<StateModel> stateModelList=new ArrayList<>();
    List<CityModel> cityModelList=new ArrayList<>();

    AutoCompleteTextView edt_country,edt_city,edt_state,edt_address;
    EditText edt_landmark,edt_street,edt_zipcode,edt_address_type,edt_area;

    int selectedAddress = -1;
    String countryCode="";
    String stateCode="";
    String address="";

    String CSDTypeName,custID,cusAdd1,cusAdd2,City,State,country,Zipcode,Area,latitude,longitude,cusMob,cusEmail,CSDID;
    int editAddressFlag=0;

    private ProgressDialog progressDialog;

    private Session_management session_management;

    private LinearLayout llCountry,llState,llCity;

    AddressModel addressModel;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        session_management = new Session_management(EditAddressActivity.this);

        edt_country=findViewById(R.id.edt_country);
        edt_city=findViewById(R.id.edt_city);
        edt_state=findViewById(R.id.edt_state);
        img_back=findViewById(R.id.img_back);
        btn_next=findViewById(R.id.btn_next);
        edt_landmark=findViewById(R.id.edt_landmark);
        edt_street=findViewById(R.id.edt_street);
        edt_zipcode=findViewById(R.id.edt_zipcode);
        edt_address=findViewById(R.id.edt_address);
        edt_address_type=findViewById(R.id.edt_address_type);
        llCountry=findViewById(R.id.llCountry);
        llState=findViewById(R.id.llState);
        llCity=findViewById(R.id.llCity);
        edt_area=findViewById(R.id.edt_area);

        types.add("Home");
        types.add("Office");

        addressModel = (AddressModel) getIntent().getSerializableExtra("addressModel");
        latitude=addressModel.getLatitude();
        longitude=addressModel.getLongitude();
        editAddressFlag=getIntent().getIntExtra("editAddressFlag",0);
        address=getIntent().getStringExtra("address");

       CSDTypeName = addressModel.getCsdTypeName();
        custID =  addressModel.getCustID();
        cusAdd1 = addressModel.getCusAdd1();//getIntent().getStringExtra("cusAdd1");
        cusAdd2 = addressModel.getCusAdd2();//getIntent().getStringExtra("cusAdd2");
        City = addressModel.getCity();//getIntent().getStringExtra("City");
        State= addressModel.getState();//getIntent().getStringExtra("State");
        country=addressModel.getCountry();//getIntent().getStringExtra("country");
        Zipcode=addressModel.getZipcode();//getIntent().getStringExtra("Zipcode");
        Area= addressModel.getArea();//getIntent().getStringExtra("Area");
        latitude= addressModel.getLatitude();//getIntent().getStringExtra("latitude");
        longitude= addressModel.getLongitude();//getIntent().getStringExtra("longitude");
        cusMob=addressModel.getCusMob();//getIntent().getStringExtra("cusMob");
        cusEmail=addressModel.getCusEmail();//getIntent().getStringExtra("cusEmail");
        CSDID=addressModel.getCsdid();//getIntent().getStringExtra("CSDID");

        countryCode=session_management.getUserCountryCode();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (EditAddressActivity.this,android.R.layout.simple_list_item_1,types);
        edt_address.setAdapter(adapter);
        edt_address.setThreshold(0);

        /*Set Address type*/
        edt_address_type.setText(CSDTypeName);
        /*Set country*/
        edt_country.setText(country);
        /*Set State*/
        edt_state.setText(State);
        /*Set City*/
        edt_city.setText(City);
        /*Set Landmark */
        edt_landmark.setText(cusAdd1);
        /*Set Street*/
        edt_street.setText(cusAdd2);
        /*Set Area*/
        edt_area.setText(Area);
        /*Set Zipcode*/
        edt_zipcode.setText(Zipcode);
        /*set Address*/
        edt_address.setText(address);

        edt_address_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence[] addressType = getResources().getStringArray(R.array.address_type);
                AlertDialog.Builder alertDialogBuilder = new MaterialAlertDialogBuilder(EditAddressActivity.this)
                        .setTitle("Select Address Type")
                        .setSingleChoiceItems(addressType, selectedAddress, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectedAddress = which;
                                edt_address_type.setText(addressType[which]);
                                dialog.dismiss();
                            }
                        });
                alertDialogBuilder.show();
            }
        });

            edt_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    edt_address.showDropDown();
                }
            });



            edt_country.setOnTouchListener(this);
            edt_state.setOnTouchListener(this);
            edt_city.setOnTouchListener(this);

                txt_currentLocation=findViewById(R.id.txt_currentLocation);

                edt_country.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //countryCode=countryCodes.get(position);
                        //Log.e("Addnewaddress", "onItemSelected: "+countryCode );

                        for(int i=0;i<countryModelList.size();i++){
                            if(countryModelList.get(i).getCountry_Name().equalsIgnoreCase(parent.getItemAtPosition(position).toString())){
                                countryCode=countryModelList.get(position).getCountry_ID();
                            }
                        }

                    }
                });

                edt_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //  stateCode=stateCodes.get(position);
                        //getCities(stateCode);
                        for(int i=0;i<stateModelList.size();i++){
                            if(stateModelList.get(i).getState_Name().equalsIgnoreCase(parent.getItemAtPosition(position).toString())){
                                stateCode=stateModelList.get(position).getState_ID();
                            }
                        }
                    }
                });

                img_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        onBackPressed();
                    }
                });

                btn_next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(edt_address_type.getText().toString().isEmpty()||edt_address_type.getText().toString().equalsIgnoreCase("null")){
                            showToast(getString(R.string.address_type_required));
                        }else if(edt_country.getText().toString().isEmpty()||edt_country.getText().toString().equalsIgnoreCase("null")){
                            showToast(getString(R.string.country_required));
                        }else if(edt_state.getText().toString().isEmpty()||edt_state.getText().toString().equalsIgnoreCase("null")){
                            showToast(getString(R.string.state_required));
                        }else if(edt_city.getText().toString().isEmpty()||edt_city.getText().toString().equalsIgnoreCase("null")){
                            showToast(getString(R.string.city_required));
                        }else if(edt_landmark.getText().toString().isEmpty()||edt_landmark.getText().toString().equalsIgnoreCase("null")){
                            showToast(getString(R.string.landmark_required));
                        }else if(edt_street.getText().toString().isEmpty()||edt_street.getText().toString().equalsIgnoreCase("null")){
                            showToast(getString(R.string.street_required));
                        }else if(edt_area.getText().toString().isEmpty()||edt_area.getText().toString().equalsIgnoreCase("null")){
                            showToast(getString(R.string.area_required));
                        }else if(edt_zipcode.getText().toString().isEmpty()||edt_zipcode.getText().toString().equalsIgnoreCase("null")){
                            showToast(getString(R.string.postcode_required));
                        }else if(edt_address.getText().toString().isEmpty()||edt_address.getText().toString().equalsIgnoreCase("null")){
                            showToast(getString(R.string.address_required));
                        }else{
                           if(editAddressFlag==0){
                               updateProfile(latitude,longitude, edt_area.getText().toString(),edt_country.getText().toString(),edt_state.getText().toString(),edt_city.getText().toString(),
                                       edt_landmark.getText().toString(),edt_zipcode.getText().toString(),edt_address_type.getText().toString(),edt_street.getText().toString(),
                                       countryCode);
                           }
                           else{

                               updateAddress(latitude,longitude, edt_area.getText().toString(),edt_country.getText().toString(),edt_state.getText().toString(),edt_city.getText().toString(),
                                       edt_landmark.getText().toString(),edt_zipcode.getText().toString(),edt_address_type.getText().toString(),edt_street.getText().toString(),CSDID);

                           }
                        }
                    }
                });
    }

    private void showToast(String string) {

        Toast.makeText(this,string,Toast.LENGTH_LONG).show();
    }



    public void getCountries() {
        progressDialog.show();
        countryNames.clear();
        countryCodes.clear();
        countryModelList.clear();
        String tag_json_obj = "json_cart_list_req";
        Map<String, String> params = new HashMap<String, String>();

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ApiBaseURL.getCountries, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Log.d("getCountries", response.toString());

                try {
                    boolean status = response.getBoolean("status");

                    if (status) {
                        JSONArray jsonArray = response.getJSONArray("result");
                        List<String> countries=new ArrayList<>();
                        List<String> countriesCode=new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            countries.add(jsonObject.getString("country_Name"));
                            countriesCode.add(jsonObject.getString("country_ID"));
                            CountryModel countryModel =new CountryModel();
                            countryModel.setCountry_Name(jsonObject.getString("country_Name"));
                            countryModel.setCountry_ID(jsonObject.getString("country_ID"));
                            countryModelList.add(countryModel);
                        }
                        countryNames.addAll(countries);
                        countryCodes.addAll(countriesCode);

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                (EditAddressActivity.this,android.R.layout.simple_list_item_1,countryNames);
                        edt_country.setAdapter(adapter);
                        edt_country.setThreshold(1);
                        edt_country.showDropDown();
                    }
                    else
                    {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
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

    public void getStates(String countryCode){
        progressDialog.show();
        stateNames.clear();
        stateCodes.clear();
        stateModelList.clear();
        String tag_json_obj = "json_cart_list_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("country_ID",countryCode);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ApiBaseURL.getStates, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("getStates", response.toString());
                progressDialog.dismiss();

                try {
                    boolean status = response.getBoolean("status");

                    if (status) {
                        JSONArray jsonArray = response.getJSONArray("result");
                        List<String> stateNamesList=new ArrayList<>();
                        List<String> stateCodeList=new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            stateNamesList.add(jsonObject.getString("state_Name"));
                            stateCodeList.add(jsonObject.getString("state_ID"));
                            StateModel stateModel =new StateModel();
                            stateModel.setState_Name(jsonObject.getString("state_Name"));
                            stateModel.setState_ID(jsonObject.getString("state_ID"));
                            stateModelList.add(stateModel);
                        }
                        stateNames.addAll(stateNamesList);
                        stateCodes.addAll(stateCodeList);

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                (EditAddressActivity.this,android.R.layout.simple_list_item_1,stateNames);
                        edt_state.setAdapter(adapter);
                        edt_state.setThreshold(1);
                        edt_state.showDropDown();

                    }
                    else
                    {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
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

    public void getCities(String stateID){
        progressDialog.show();
        cityNames.clear();
        cityCodes.clear();
        cityModelList.clear();
        String tag_json_obj = "json_cart_list_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("state_ID",stateID);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                ApiBaseURL.getCities, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Log.d("getCities", response.toString());

                try {
                    boolean status = response.getBoolean("status");

                    if (status) {
                        JSONArray jsonArray = response.getJSONArray("result");
                        List<String> countries=new ArrayList<>();
                        List<String> countriesCode=new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            countries.add(jsonObject.getString("city_Name"));
                            countriesCode.add(jsonObject.getString("city_ID"));
                            CityModel cityModel = new CityModel();
                            cityModel.setCity_Name(jsonObject.getString("city_Name"));
                            cityModel.setCity_ID(jsonObject.getString("city_ID"));
                            cityModelList.add(cityModel);
                        }
                        cityNames.addAll(countries);
                        cityCodes.addAll(countriesCode);

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                (EditAddressActivity.this,android.R.layout.simple_list_item_1,cityNames);
                        edt_city.setAdapter(adapter);
                        edt_city.setThreshold(1);
                        edt_city.showDropDown();
                    }
                    else
                    {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
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

    private void showOKDialog(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(EditAddressActivity.this)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", okListener)
                .create()
                .show();
    }


    private void showKeyBoard(AutoCompleteTextView yourEditText){
        yourEditText.requestFocus();
        InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(yourEditText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideKeyboard(EditText yourEditText){
        InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(yourEditText.getWindowToken(), 0);
    }

    @Override
    public boolean onTouch(View arg0, MotionEvent arg1) {


        if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
            int id = arg0.getId();
            if (id == R.id.edt_country) {
                // Your code goes here
                showKeyBoard(edt_country);
                edt_state.setText("");
                edt_city.setText("");
                edt_landmark.setText("");
                edt_street.setText("");
                edt_zipcode.setText("");
                getCountries();
            } else if (id == R.id.edt_state) {
                // Your code goes here
                showKeyBoard(edt_state);
                edt_city.setText("");
                edt_landmark.setText("");
                edt_street.setText("");
                edt_zipcode.setText("");
                getStates(countryCode);
            } else if (id == R.id.edt_city) {
                // Your code goes here
                showKeyBoard(edt_city);
                edt_landmark.setText("");
                edt_street.setText("");
                edt_zipcode.setText("");
                getCities(stateCode);
            }
        }
        return true;
    }

    private void updateProfile(String latitude, String longitude,String Area,
                           String country,String state,String city,String landmark,
                           String zipcode,String type,String street,String countryCode)
    {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();

        String dob = "",custName="",custID="",cusMob="",cusEmail="";
        if(session_management.getUserDOB()!=null && !session_management.getUserDOB().equals("null"))
        {
            String dtStart = session_management.getUserDOB();
            SimpleDateFormat spf;
            if(dtStart.contains("-")){
                spf = new SimpleDateFormat("dd-mm-yyyy");
            }else {
                spf = new SimpleDateFormat("dd/mm/yyyy");
            }
            try {
                Date date = spf.parse(dtStart);
                spf = new SimpleDateFormat("yyyy-mm-dd");
                dob=spf.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(session_management.getUserFullName()!=null){
            custName = session_management.getUserFullName();
        }

        if(session_management.getUserId()!=null){
            custID = session_management.getUserId();
        }
        if(session_management.getUserMobile()!=null){
            cusMob = session_management.getUserMobile();
        }
        if(session_management.getUserEmail()!=null){
            cusEmail = session_management.getUserEmail();
        }

        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("custName", custName);
        params.put("custID", custID);
        params.put("cusMob",  cusMob);
        params.put("cusEmail",  cusEmail);
        params.put("Countrycode", countryCode);
        params.put("cusAdd1", landmark);
        params.put("cusAdd2", street);
        params.put("DOB", dob);
        params.put("country", country);
        params.put("State", state);
        params.put("City", city);
        params.put("Area", Area);
        params.put("Zipcode", zipcode);
        Log.d("dsd", String.valueOf(params));

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, ApiBaseURL.ProfileUpdate1, params, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                Log.d("Tag", response.toString());
                progressDialog.dismiss();
                try
                {

                    boolean status = response.getBoolean("status");
                    String message = response.getString("message");

                    if (status) {
                        try
                        {
                            Session_management sessionManagement = new Session_management(EditAddressActivity.this);

                            sessionManagement.setUserCountry(country);
                            sessionManagement.setUserState(state);
                            sessionManagement.setUserCity(city);
                            sessionManagement.setUserLandmark(landmark);
                            sessionManagement.setUserStreet(street);
                            sessionManagement.setUserPinCode(zipcode);
                            sessionManagement.setUserCountryCode(countryCode);

                            SharedPreferences.Editor editor = getSharedPreferences(BaseURL.MyPrefreance, MODE_PRIVATE).edit();

                            editor.putString(BaseURL.USER_COUNTRY, country);
                            editor.putString(BaseURL.USER_STATE,state);
                            editor.putString(BaseURL.USER_CITY,city);
                            editor.putString(BaseURL.USER_LANDMARK,landmark);
                            editor.putString(BaseURL.USER_STREET,street);
                            editor.putString(BaseURL.ADDRESS,Area);
                            editor.putString(BaseURL.KEY_PINCODE,zipcode);
                            editor.putString(BaseURL.USER_COUNTRY_CODE,countryCode);
                            editor.apply();

                            onBackPressed();
                            Toast.makeText(getApplicationContext(), message + "", Toast.LENGTH_SHORT).show();
                            finish();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else{
                        Toast.makeText(EditAddressActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
    //                Toast.makeText(Search.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);

    }


        public void updateAddress(String latitude, String longitude,String Area,
                           String country,String state,String city,String landmark,
                           String zipcode,String type,String street,String CSDID)
        {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiBaseURL.UpdateAddress, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("addadrss", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean status = jsonObject.getBoolean("status");
                    String msg = jsonObject.getString("message");
                    if (status) {
                        onBackPressed();
                        Toast.makeText(getApplicationContext(), msg + "", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), msg + "", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("custID", session_management.getUserDetails().get(BaseURL.KEY_ID));
                param.put("latitude", latitude);
                param.put("longitude",longitude);
                param.put("country",country);
                param.put("State",state);
                param.put("CSDTypeName",type);
                param.put("cusAdd1",landmark);
                param.put("cusAdd2",street);
                param.put("City",city);
                param.put("Zipcode",zipcode);
                param.put("Area",Area);
                param.put("cusMob",session_management.getUserDetails().get(BaseURL.KEY_MOBILE));
                param.put("cusEmail",session_management.getUserDetails().get(BaseURL.KEY_EMAIL));
                param.put("CSDID",CSDID);

                Log.e("params", param.toString());
                return param;
            }
        };

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
        RequestQueue requestQueue = Volley.newRequestQueue(EditAddressActivity.this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);


    }
}