package activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import adapters.CustomPagerAdapter1;
import com.grocery.QTPmart.R;

import java.util.ArrayList;


public class GetStartedActivity1 extends AppCompatActivity {

    ViewPager viewPager;
    ArrayList<String> arrayList;
    TabLayout tabLayout;
    Button btn_next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started_1);


        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tab_layout);
        btn_next =  findViewById(R.id.btn_next);
        arrayList = new ArrayList<>();

        arrayList.add("If you’re offered a seat on a rocket ship, don’t ask what seat! Just get on.");
        arrayList.add("If you’re offered a seat on a rocket ship, don’t ask what seat! Just get on.");
        arrayList.add("If you’re offered a seat on a rocket ship, don’t ask what seat! Just get on.");


        tabLayout.setupWithViewPager(viewPager);

        CustomPagerAdapter1 pagerAdapter = new CustomPagerAdapter1(this, arrayList);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setPageMargin(20);

        // whenever the page changes
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }
            @Override
            public void onPageSelected(int i) {

            }
            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });



        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(viewPager.getCurrentItem()==2){
                    finishOnboarding();
                }else{
                    viewPager.setCurrentItem(viewPager.getCurrentItem()+1,true);
                }
            }
        });


    }

    private void finishOnboarding() {
        SharedPreferences preferences = getSharedPreferences("GOGrocer", MODE_PRIVATE);

        preferences.edit().putBoolean("onboarding_complete",true).apply();

        Intent intent1 =new Intent(this,MainDrawerActivity.class);
        intent1.putExtra("loadFrag",1);
        startActivity(intent1);
        finish();

    }
}