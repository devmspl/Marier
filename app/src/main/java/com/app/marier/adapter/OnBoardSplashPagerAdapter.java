package com.app.marier.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.app.marier.R;
import com.app.marier.activities.OnBoardSplashActivity;
import com.app.marier.staticmodel.OnBoardSwipeAdapterModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import me.relex.circleindicator.CircleIndicator;

public class OnBoardSplashPagerAdapter extends PagerAdapter {

    private final ArrayList<OnBoardSwipeAdapterModel> arraylist;
    private final ViewPager viewpager;
    // Context object
    Context context;



    // Layout Inflater
    LayoutInflater mLayoutInflater;
    BottomSheetBehavior<View> bottomSheetBehavior;

    public OnBoardSplashPagerAdapter(@NotNull OnBoardSplashActivity onBoardSplashActivity, @NotNull ArrayList<OnBoardSwipeAdapterModel> arrayList, ViewPager viewPager) {
        this.arraylist = arrayList;
        this.context = onBoardSplashActivity;
        this.viewpager = viewPager;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        // return the number of images
        return arraylist.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        // inflating the item.xml
        View itemView = mLayoutInflater.inflate(R.layout.onboardsplashviewpageritem, container, false);

        // referencing the image view from the item.xml file
        TextView tvtext1 = (TextView) itemView.findViewById(R.id.tvmaintext);
        TextView tvtext2 = (TextView) itemView.findViewById(R.id.tvmaintext2);
         ImageView imgmain = (ImageView) itemView.findViewById(R.id.imgmain);
         CircleIndicator circleIndicator  = (CircleIndicator) itemView.findViewById(R.id.indicator);
        circleIndicator.setViewPager(viewpager);

        // setting the image in the imageView
        tvtext1.setText(arraylist.get(position).getText1());
        tvtext2.setText(arraylist.get(position).getText2());
        imgmain.setImageResource(arraylist.get(position).getImage());

        // Adding the View
        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((LinearLayout) object);
    }

}
