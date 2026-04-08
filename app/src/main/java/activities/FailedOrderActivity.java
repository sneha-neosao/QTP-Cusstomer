package activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.grocery.QTPmart.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FailedOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_failed_order);
        SweetAlertDialog sweetAlertDialog=new SweetAlertDialog(FailedOrderActivity.this,SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();
        sweetAlertDialog.setTitle("Transaction Unsuccessful");
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                startActivity(new Intent(FailedOrderActivity.this,CartActivity.class));
                finish();
            }
        });

    }
}