package com.sunm.vd;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunm.vpandgrid.AdPageAdapter;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    private static final String INTENT_KEY = "secondActivity";
    private LinearLayout pageParent;
    private ViewPager page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        initView();
    }

    private void initView() {
        pageParent = (LinearLayout) findViewById(R.id.page_parent);
        page = (ViewPager) findViewById(R.id.page);

        List<View> pageChildViews = new ArrayList<>();
        for (int i=0; i< 4; i++){
            TextView textView = new TextView(this);
            textView.setText("No."+i);
            textView.setTextSize(20);
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            pageChildViews.add(textView);
        }

        page.setAdapter(new AdPageAdapter(pageChildViews));
    }

}
