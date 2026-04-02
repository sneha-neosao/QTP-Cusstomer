package adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ModelClass.VarientModel;
import com.grocery.QTPmart.R;
import util.Session_management;

import java.util.ArrayList;

public class VarientAdapter extends RecyclerView.Adapter<VarientAdapter.MyViewHolder>{

    Context context;
    ItemOnClickListener itemOnClickListener;
    ArrayList<VarientModel> varientModels;
    int selectedItem;
    private Session_management session_management;

    public VarientAdapter(Context context, ArrayList<VarientModel> varientModels,ItemOnClickListener itemOnClickListener){
        this.context=context;
        this.varientModels=varientModels;
        this.itemOnClickListener=itemOnClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout_varient, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        session_management = new Session_management(context);
        if(Float.parseFloat(varientModels.get(position).getSavingInAMT())>0.0){
            holder.ll_saving.setVisibility(View.VISIBLE);
            holder.txt_saving.setText(varientModels.get(position).getSavingInAMT()+" AED");
        }else{
            holder.ll_saving.setVisibility(View.GONE);
        }

        holder.txt_unit.setText(varientModels.get(position).getUom());

        holder.txt_unit_price.setText("AED "+varientModels.get(position).getItemSellingprice());

        if(Float.parseFloat(varientModels.get(position).getSavingInPercentage())>0.0){
            holder.ll_discount.setVisibility(View.VISIBLE);
            holder.txt_discount.setText(varientModels.get(position).getSavingInPercentage()+"% OFF");
        }else{
            holder.ll_discount.setVisibility(View.GONE);
        }

        Log.e("selectedItemPOs",selectedItem+"");
        if(selectedItem==position){
            holder.ll_unit.setBackground(context.getResources().getDrawable(R.drawable.outlined_square_aqua));
            holder.txt_unit.setTextColor(context.getResources().getColor(R.color.aqua));
            holder.txt_unit_price.setTextColor(context.getResources().getColor(R.color.aqua));
        }else{
            holder.ll_unit.setBackground(context.getResources().getDrawable(R.drawable.outlined_square_grey_1));
            holder.txt_unit.setTextColor(context.getResources().getColor(R.color.grey_1));
            holder.txt_unit_price.setTextColor(context.getResources().getColor(R.color.grey_1));
        }


        holder.itemView.setOnClickListener(view -> {
            itemOnClickListener.onItemClick(position,varientModels);
           // selectedItem=position;

        });



    }

    @Override
    public int getItemCount() {
        return varientModels.size();
    }

    public void selectedItemPosition(int selectedItem){
        this.selectedItem=selectedItem;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView txt_saving,txt_unit,txt_unit_price,txt_discount;
        public LinearLayout ll_saving,ll_unit,ll_discount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ll_discount=itemView.findViewById(R.id.ll_discount);
            ll_saving=itemView.findViewById(R.id.ll_saving);
            ll_unit=itemView.findViewById(R.id.ll_unit);

            txt_saving=itemView.findViewById(R.id.txt_saving);
            txt_unit=itemView.findViewById(R.id.txt_unit);
            txt_unit_price=itemView.findViewById(R.id.txt_unit_price);
            txt_discount=itemView.findViewById(R.id.txt_discount);

        }
    }

    public interface ItemOnClickListener{
        public void onItemClick(int position,ArrayList<VarientModel> varientModel);
    }
}
