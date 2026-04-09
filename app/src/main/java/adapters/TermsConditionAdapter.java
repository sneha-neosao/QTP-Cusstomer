package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;


import ModelClass.ResultTerms;
import com.grocery.QTPmart.R;

import java.util.ArrayList;

public class TermsConditionAdapter extends  RecyclerView.Adapter<TermsConditionAdapter.MyViewHolder>{
    ArrayList<ResultTerms> list;
    Context context;

    public TermsConditionAdapter(Context context, ArrayList<ResultTerms> list)
    {
        this.list=list;
        this.context=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_allterms_condition_product, parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ResultTerms model = list.get(position);
        holder.txt_termdetail.setText(model.getTcDescription());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_termdetail;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_termdetail=itemView.findViewById(R.id.txt_termdetail);

        }
    }
}
