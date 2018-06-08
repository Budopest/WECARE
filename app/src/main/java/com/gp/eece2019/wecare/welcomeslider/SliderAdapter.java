package com.gp.eece2019.wecare.welcomeslider;

import android.content.Context;
import android.preference.PreferenceActivity;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gp.eece2019.wecare.R;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public int count()
    {
        return slide_images.length;
    }

    public SliderAdapter(Context context)
    {
        this.context=context;
    }

    public int[] slide_images = {
            R.drawable.calls_purple,
            R.drawable.pills,
            R.drawable.measurement

    };
    public String[] slide_headers = {
            "Easy Communications",
            "Keep Up With Your Medicines",
            "Monitor Your Condition"
    };

    public String[] slide_description = {
            "Make phone calls and send sms to your doctor or family members is very simple, also automatic cals and sms are send in emergency cases based on how serious the case is.",
            "Update your medicines and their doses with any changes your doctor made, have a notification to remind you with your medication time.",
            "Monitor your condition closely see your temperature, Heart rate,etc."
    };

    @Override
    public int getCount() {
        return slide_description.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull View container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.intro_slide, (ViewGroup) container,false);

        ImageView slideimageview = view.findViewById(R.id.slideimg);
        TextView header = view.findViewById(R.id.Header);
        TextView desc = view.findViewById(R.id.descreption);

        slideimageview.setImageResource(slide_images[position]);
        header.setText(slide_headers[position]);
        desc.setText(slide_description[position]);

        ((ViewGroup) container).addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((ConstraintLayout)object);
    }
}
