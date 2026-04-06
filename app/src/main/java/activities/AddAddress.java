package activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import adapters.SearchAdapter;
import Config.ApiBaseURL;
import Config.BaseURL;
import ModelClass.SearchModel;
import com.grocery.QTPmart.R;
import network.ApiInterface;
import util.Session_management;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddAddress extends AppCompatActivity {
    Session_management session_management;
    LinearLayout back;
    Button Save;
    EditText pinCode, houseNo, Area, city, state, landmaark, name, mobNo, alterMob;
    TextInputLayout tpinCode, thouseNo, tArea, tcity, tstate, tlandmaark, tname, tmobNo, talterMob;

    RadioGroup radioGroup;
    RadioButton rHome, rWork;
    CardView currentLoc;
    String user_id;
    RecyclerView recyclerViewCity, recyclerViewSociety;
    String cityId, cityName, socetyId, SocetyName, landmaarkkkk, updtae, addressId, receiver_name, receiver_phone, house_no, landmark, state_st, pincode,streetArea;
    ProgressDialog progressDialog;
    SearchAdapter cityAdapter, societyAdapter;
    List<SearchModel> citylist = new ArrayList<>();
    List<SearchModel> societylist = new ArrayList<>();
    LinearLayout linearLatlang;
    String lat, longs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        init();
    }

    private void init() {
        session_management = new Session_management(AddAddress.this);

        cityName = session_management.getLocationCity();
        streetArea = session_management.getStreetArea();
        SocetyName = session_management.getHouseBuilding();


        back = findViewById(R.id.back);
        Save = findViewById(R.id.SaveBtn);
        //EditBtn = findViewById(R.id.EditBtn);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
//        if (updtae != "") {
//            updtae = getIntent().getStringExtra("update");
//            addressId = getIntent().getStringExtra("addId");
//            //     Log.d("fgh",addressId);
//            Save.setVisibility(View.GONE);
//          //  EditBtn.setVisibility(View.VISIBLE);
//        }
        back.setOnClickListener(v -> finish());
        Session_management sessionManagement = new Session_management(getApplicationContext());

        user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);


        currentLoc = findViewById(R.id.currentLoc);

        recyclerViewCity = findViewById(R.id.recyclerCity);
        recyclerViewSociety = findViewById(R.id.recyclerSociety);

        tpinCode = findViewById(R.id.input_layout_pinCode);
        thouseNo = findViewById(R.id.input_layout_HOuseNo);
        tArea = findViewById(R.id.input_layout_area);
        tcity = findViewById(R.id.input_layout_CIty);
        tstate = findViewById(R.id.input_layout_state);
        tlandmaark = findViewById(R.id.input_layout_landmark);
        tname = findViewById(R.id.input_layout_NAme);
        tmobNo = findViewById(R.id.input_layout_mobNo);
        talterMob = findViewById(R.id.input_layout_AltermobileNO);
        pinCode = (EditText) findViewById(R.id.input_pinCode1);
        houseNo = (EditText) findViewById(R.id.input_HouseNO1);
        Area = (EditText) findViewById(R.id.input_area);
        city = (EditText) findViewById(R.id.input_city);
        state = (EditText) findViewById(R.id.input_state1);
        landmaark = (EditText) findViewById(R.id.input_landmark);
        name = (EditText) findViewById(R.id.input_NAme1);
        mobNo = (EditText) findViewById(R.id.input_mobNO1);
        alterMob = (EditText) findViewById(R.id.input_AltermobileNO1);
        linearLatlang = findViewById(R.id.linearLatlang);

        if (SocetyName.equals(null))
        {
            houseNo.setText("");
        }
        else {
            houseNo.setText(SocetyName);
        }

        if (streetArea.equals(null))
        {
            Area.setText("");
        }
        else {
            Area.setText(streetArea);
        }

        if (cityName.equals(null))
        {
            city.setText("");
        }
        else {
            city.setText(cityName);
        }


        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sessionManagement.getLatPref().equals("") && sessionManagement.getLangPref().equals("")){
                    Toast.makeText(getApplicationContext(), "Please select pickup location", Toast.LENGTH_SHORT).show();
                }
                else if (houseNo.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter Building Name / House No.", Toast.LENGTH_SHORT).show();
                } else if (Area.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter Street / Area", Toast.LENGTH_SHORT).show();
                } else if (city.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter City", Toast.LENGTH_SHORT).show();
                }  else {

                    String address1=Area.getText().toString().trim();
                    String address2=houseNo.getText().toString().trim();
                    String cityName=city.getText().toString().trim();
                    if(isOnline()) {
                        saveAddress(address1, address2, cityName);
                    }
                    else
                    {
                        Toast.makeText(AddAddress.this, "Please Check internet Connection..!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        linearLatlang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(AddAddress.this, AddressLocationActivity.class));
            }
        });
    }


    private void saveAddress(String address1, String address2, String cityName) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiBaseURL.UpdateAddress, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("addadrss", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean status = jsonObject.getBoolean("status");
                    String msg = jsonObject.getString("message");
                    if (status) {
                        session_management.setLocationCity(cityName);
                        session_management.setStreetArea(address1);
                        session_management.setHouseBuilding(address2);
                        session_management.setAddress(address1+", "+address2+", "+cityName);
                        Toast.makeText(getApplicationContext(), msg + "", Toast.LENGTH_SHORT).show();
                       finish();
                    } else {
                        Toast.makeText(getApplicationContext(), msg + "", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
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
                param.put("cusAdd1", address1);
                param.put("cusAdd2",address2);
                param.put("BranchCode", ApiInterface.branchcode);

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
        RequestQueue requestQueue = Volley.newRequestQueue(AddAddress.this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
