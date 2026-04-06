package adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import activities.EditAddressLocationActivity;
import ModelClass.AddressModel;
import com.grocery.QTPmart.R;

import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressView>{

    Context context;
    ArrayList<AddressModel> addressModels;
    ItemOnClickListener itemOnClickListener;
    private  int  showCheckedImage ;

    public AddressAdapter(Context context, ArrayList<AddressModel> addressModels,ItemOnClickListener itemOnClickListener){
        this.context=context;
        this.addressModels=addressModels;
        this.itemOnClickListener=itemOnClickListener;
    }

    @NonNull
    @Override
    public AddressView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout_address, parent, false);
        return new AddressView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressView holder, int position) {
            /*holder.txt_type.setText(addressModels.get(position).getCsdTypeName());
            holder.txt_address.setText(addressModels.get(position).getCusAdd1()+","+
                    addressModels.get(position).getCusAdd2()+","+addressModels.get(position).getCity()
                    +","+addressModels.get(position).getState());*/
            if(showCheckedImage==position){
                holder.rl_checked1.setVisibility(View.VISIBLE);
                holder.cvAddress1.setVisibility(View.VISIBLE);
                holder.cvAddress.setVisibility(View.GONE);
                holder.txt_type1.setText(addressModels.get(position).getCsdTypeName());
                holder.txt_address1.setText(addressModels.get(position).getCusAdd1()+","+
                        addressModels.get(position).getCusAdd2()+","+addressModels.get(position).getCity()
                        +","+addressModels.get(position).getState());
            }
            else
            {
                holder.rl_checked.setVisibility(View.GONE);
                holder.cvAddress1.setVisibility(View.GONE);
                holder.cvAddress.setVisibility(View.VISIBLE);
                holder.txt_type.setText(addressModels.get(position).getCsdTypeName());
                holder.txt_address.setText(addressModels.get(position).getCusAdd1()+","+
                        addressModels.get(position).getCusAdd2()+","+addressModels.get(position).getCity()
                        +","+addressModels.get(position).getState());
            }

            holder.itemView.setOnClickListener(view -> {

                itemOnClickListener.onItemClick(position,addressModels);

            });

            holder.ivEditAddress.setOnClickListener(view->{
                Intent intent = new Intent(context, EditAddressLocationActivity.class);
                intent.putExtra("addressModel",addressModels.get(position));
                intent.putExtra("editAddressFlag",position);
                context.startActivity(intent);
            });

             holder.ivEditAddress1.setOnClickListener(view->{
                Intent intent = new Intent(context, EditAddressLocationActivity.class);
                intent.putExtra("addressModel",addressModels.get(position));
                intent.putExtra("editAddressFlag",position);
                context.startActivity(intent);
            });



    }

    public void showCheckedImage(int showCheckedImage){
        this.showCheckedImage = showCheckedImage;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return addressModels.size();
    }

    public class AddressView extends RecyclerView.ViewHolder{

        TextView txt_address,txt_type,txt_address1,txt_type1;
        RelativeLayout rl_checked,rl_checked1;
        CardView cvAddress,cvAddress1;
        ImageView ivEditAddress,ivEditAddress1;

        public AddressView(@NonNull View itemView) {
            super(itemView);

            txt_address=itemView.findViewById(R.id.txt_address);
            txt_type=itemView.findViewById(R.id.txt_type);
            rl_checked=itemView.findViewById(R.id.rl_checked);
            txt_address1=itemView.findViewById(R.id.txt_address1);
            txt_type1=itemView.findViewById(R.id.txt_type1);
            rl_checked1=itemView.findViewById(R.id.rl_checked1);
            cvAddress=itemView.findViewById(R.id.cvAddress);
            cvAddress1=itemView.findViewById(R.id.cvAddress1);
            ivEditAddress=itemView.findViewById(R.id.ivEditAddress);
            ivEditAddress1=itemView.findViewById(R.id.ivEditAddress1);

        }
    }

    void setItems(ArrayList<AddressModel> list)
    {
        this.addressModels.clear();
        this.addressModels=list;
        notifyDataSetChanged();
    }

    public interface ItemOnClickListener{
        public void onItemClick(int position,ArrayList<AddressModel> addressModels);
    }
}
