package com.example.attendancemanager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {
    TextView head, desc;
    ImageView main;
    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }
    public int[] slider_images = {
            R.drawable.code_icon,
            R.drawable.sleep_icon,
            R.drawable.attendance,
            R.drawable.right
    };

    public String[] slide_heading = {
            "Mark Your Attendance",
            "View Your Attendance",
            "Keep your Score",
    };

    public String[] slide_desc = {
            "A easy way to mark your attendance without any Chaos ! No proxy Zone!",
            "You can view your attendance any time for any particular class or the whole course!",
            "Just press the download button to keep the records 'safer' !",
    };

    @Override
    public int getCount() {
        return slide_heading.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (RelativeLayout) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        main = (ImageView) view.findViewById(R.id.imageView);
        head = (TextView) view.findViewById(R.id.head);
        desc = (TextView) view.findViewById(R.id.desc);

        main.setImageResource(slider_images[position]);
        head.setText(slide_heading[position]);
        desc.setText(slide_desc[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }
}
