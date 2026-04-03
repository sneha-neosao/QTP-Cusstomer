package adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;


import com.grocery.QTPmart.R;

import java.util.ArrayList;

public class CustomPagerAdapter1 extends PagerAdapter {

    Context context;
    ArrayList<String> pager;

    public CustomPagerAdapter1(Context context, ArrayList<String> pager) {
        this.context = context;
        this.pager = pager;
    }

    @Override
    public int getCount() {
        return pager.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public  Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.pager_item, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv1);
        TextView textView1 = (TextView) view.findViewById(R.id.tv1);
        TextView textView2 = (TextView) view.findViewById(R.id.tv_msg_pager);
        //tv_msg_pager.setText(pager.get(position));
        if(position==0){
            imageView.setImageResource(R.drawable.group_1);
            textView1.setText("Shop your Daily Necessities");
            textView2.setText("Getting your every days good, now just a matter of some click !");
        }
        if(position==1){
            imageView.setImageResource(R.drawable.group_2);
            textView1.setText("Offers Fresh & Quality Groceries for you");
            textView2.setText("All item have superb freshness and are intended for your needs");
        }
        if(position==2){
            imageView.setImageResource(R.drawable.group_3);
            textView1.setText("Relax & Shop");
            textView2.setText("Shop Online and get Groceries Delivered from stores to your Home");
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
