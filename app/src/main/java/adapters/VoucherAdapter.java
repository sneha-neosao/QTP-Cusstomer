package adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.grocery.QTPmart.R;
import network.Response.ResponseGetUserVouchers;

import java.util.ArrayList;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.AddressView>{

    Context context;
    ArrayList<ResponseGetUserVouchers.VoucherResult> voucherList;
    ItemOnClickListener itemOnClickListener;

    public VoucherAdapter(Context context, ArrayList<ResponseGetUserVouchers.VoucherResult> voucherList,ItemOnClickListener itemOnClickListener){
        this.context=context;
        this.voucherList=voucherList;
        this.itemOnClickListener=itemOnClickListener;
    }

    @NonNull
    @Override
    public AddressView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_voucher, parent, false);
        return new AddressView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressView holder, int position) {

        if(voucherList.get(position).getCouponType().equals("Voucher"))
        {
            holder.tvAmount.setText("AED "+voucherList.get(position).getReturnAmount());
        }
        else if(voucherList.get(position).getCouponType().equals("ReferNEarn"))
        {
            holder.tvAmount.setText(voucherList.get(position).getDiscountValue()+"%");
        }

            holder.btnRedeemVoucher.setOnClickListener(view -> {
                itemOnClickListener.onItemClick(position,voucherList);
            });

    }


    @Override
    public int getItemCount() {
        return voucherList.size();
    }

    public class AddressView extends RecyclerView.ViewHolder{

        TextView tvAmount;
        Button btnRedeemVoucher;


        public AddressView(@NonNull View itemView) {
            super(itemView);

            tvAmount=itemView.findViewById(R.id.tvAmount);
            btnRedeemVoucher=itemView.findViewById(R.id.btnRedeemVoucher);


        }
    }

    public interface ItemOnClickListener{
        public void onItemClick(int position,ArrayList<ResponseGetUserVouchers.VoucherResult> voucherList);
    }


}
