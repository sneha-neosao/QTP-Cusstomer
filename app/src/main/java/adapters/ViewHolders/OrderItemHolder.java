package adapters.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grocery.QTPmart.R;

public class OrderItemHolder extends RecyclerView.ViewHolder{

   public ImageView img_order_product,ivProductFeedback;
   public TextView txt_product_name,txt_product_weight,txt_product_quantity,txt_product_price;

    public OrderItemHolder(@NonNull View itemView) {
        super(itemView);

        /*img_order_product=itemView.findViewById(R.id.img_order_product);
        txt_product_name=itemView.findViewById(R.id.txt_product_name);
        txt_product_weight=itemView.findViewById(R.id.txt_product_weight);
        txt_product_quantity=itemView.findViewById(R.id.txt_product_quantity);
        txt_product_price=itemView.findViewById(R.id.txt_product_price);
        ivProductFeedback=itemView.findViewById(R.id.ivProductFeedback);*/
    }
}
