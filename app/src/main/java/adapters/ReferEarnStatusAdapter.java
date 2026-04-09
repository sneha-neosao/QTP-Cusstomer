package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ModelClass.ResultReferEarn;
import com.grocery.QTPmart.R;

import java.util.ArrayList;

public class ReferEarnStatusAdapter extends  RecyclerView.Adapter<ReferEarnStatusAdapter.MyViewHolder>{


    ArrayList<ResultReferEarn> list;
    Context context;

    public ReferEarnStatusAdapter(Context context, ArrayList<ResultReferEarn> list)
    {
        this.list=list;
        this.context=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_signup_orders, parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ResultReferEarn model = list.get(position);
        holder.txt_cutname.setText(model.getCustName());
        holder.txt_order_count.setText(model.getFirstOrderPlaced());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setItems(ArrayList<ResultReferEarn> list) {
        this.list=list;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_cutname,txt_order_count;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_cutname=itemView.findViewById(R.id.txt_cutname);
            txt_order_count=itemView.findViewById(R.id.txt_order_count);

        }
    }
}
