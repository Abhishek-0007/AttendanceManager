package com.example.attendancemanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AvatarPage extends AppCompatActivity {
    private ViewPager viewPager;
    private LinearLayout linearLayout;
    private SliderAdapter sliderAdapter;
    private TextView mDots[];
    public Button next, back;
    private int currentPage = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar_page);
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        back = (Button) findViewById(R.id.backBtn);
        viewPager.addOnPageChangeListener(viewListener);
        next = (Button) findViewById(R.id.nextBtn);
        //this.next = next;
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AvatarPage.this, login.class);
                startActivity(intent);
            }
        });
    }
    public void addDotsIndicator(int position) {
        mDots = new TextView[3];
        linearLayout.removeAllViews();
        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setLinkTextColor(getResources().getColor(R.color.colorWhiteTransparent));
            linearLayout.addView(mDots[i]);
        }
        if(mDots.length > 0){
            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
            currentPage = i;
            if (i == 0 || i == 1 ) {
                next.setEnabled(false);
                next.setVisibility(View.INVISIBLE);
                next.setText("");
            }else {
                next.setEnabled(true);
                next.setVisibility(View.VISIBLE);
                next.setText("NEXT");
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };
}