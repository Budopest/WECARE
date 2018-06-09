package com.gp.eece2019.wecare.welcomeslider;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gp.eece2019.wecare.MainActivity;
import com.gp.eece2019.wecare.R;
import com.gp.eece2019.wecare.login.SigninActivity;
import com.gp.eece2019.wecare.login.SignupActivity;

public class WelcomeScreen extends AppCompatActivity {

    private LinearLayout sdotslayout;
    private TextView[] sDots;
    private ViewPager slideview;
    private SliderAdapter sliderAdapter;
    private Button NXTB,PrevB;
    private int currentpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);

        sdotslayout = findViewById(R.id.dots);
        slideview = findViewById(R.id.viewpagerslide);
        NXTB = findViewById(R.id.nextbutton);
        PrevB = findViewById(R.id.prevbutton);

        sliderAdapter = new SliderAdapter(this);
        slideview.setAdapter(sliderAdapter);

        addDotsIndicator(0);
        slideview.addOnPageChangeListener(viewListener);

        NXTB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(currentpage==sDots.length-1) {

                    Intent i = new Intent(WelcomeScreen.this,SigninActivity.class);
                    startActivity(i);
                    finish();
                }
                else  slideview.setCurrentItem(currentpage+1);
            }
        });
        PrevB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                slideview.setCurrentItem(currentpage-1);
            }
        });

    }
    public void addDotsIndicator(int pos)
    {
        sDots = new TextView[new SliderAdapter(this).count()];
        sdotslayout.removeAllViews();

        for(int i=0;i<sDots.length;i++)
        {
            sDots[i] = new TextView(this);
            sDots[i].setText(Html.fromHtml("&#8226;"));
            sDots[i].setTextSize(40);
            sDots[i].setTextColor(getResources().getColor(R.color.dotscolor));

            sdotslayout.addView(sDots[i]);
        }
        if(sDots.length>0) {sDots[pos].setTextColor(Color.parseColor("#FFFFFF"));}
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addDotsIndicator(position);
            currentpage = position;

            if(position==0)
            {
                NXTB.setEnabled(true);
                PrevB.setEnabled(false);
                PrevB.setVisibility(View.INVISIBLE);
                NXTB.setText("Finish");
                PrevB.setText("");

            }
            else if (position==sDots.length-1)
            {
                NXTB.setEnabled(true);
                PrevB.setEnabled(true);
                PrevB.setVisibility(View.VISIBLE);
                NXTB.setText("Finish");
                PrevB.setText("Previous");
            }
            else {
                NXTB.setEnabled(true);
                PrevB.setEnabled(true);
                PrevB.setVisibility(View.VISIBLE);
                NXTB.setText("Next");
                PrevB.setText("Previous");
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}
