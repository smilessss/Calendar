package com.codbking.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import static com.codbking.calendar.CalendarFactory.getMonthOfDayList;

/**
 * Created by codbking on 2016/12/18.
 * email:codbking@gmail.com
 * github:https://github.com/codbking
 * blog:http://www.jianshu.com/users/49d47538a2dd/latest_articles
 */

public class CalendarDateView extends CalendarViewPager implements CalendarTopView {
    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }

    HashMap<Integer, CalendarView> views = new HashMap<>();
    //    private CaledarTopViewChangeListener mCaledarLayoutChangeListener;
    private CalendarView.OnItemClickListener onItemClickListener;

    private LinkedList<CalendarView> cache = new LinkedList();

    private int MAXCOUNT = 6;


    private int row = 6;
    private int maxPager = 962;
    private CaledarAdapter mAdapter;
    private int calendarItemHeight = 0;


    public void setAdapter(CaledarAdapter adapter) {
        mAdapter = adapter;
        initData();
    }

    public void setOnItemClickListener(CalendarView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public CalendarDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CalendarDateView);
        row = a.getInteger(R.styleable.CalendarDateView_cbd_calendar_row, 6);
        a.recycle();
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int calendarHeight = 0;
        if (getAdapter() != null) {
            CalendarView view = (CalendarView) getChildAt(0);
            if (view != null) {
                calendarHeight = view.getMeasuredHeight();
                calendarItemHeight = view.getItemHeight();
            }
        }
        setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(calendarHeight, MeasureSpec.EXACTLY));
    }

    private void init() {
        final int[] dateArr = CalendarUtil.getYMD(new Date());

        setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                //Integer.MAX_VALUE
                return maxPager;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {

                CalendarView view;

                if (!cache.isEmpty()) {
                    view = cache.removeFirst();
                } else {
                    view = new CalendarView(container.getContext(), row);
                }


                view.setOnItemClickListener(onItemClickListener);
                view.setAdapter(mAdapter);

                boolean as = position == maxPager / 2;

                int month = (dateArr[1] + (position) - maxPager / 2) ;




                int year = dateArr[0];
                if (month > 12) {
                    year = year + month / 12;

                    month = (month % 12);

                }

                view.setData(getMonthOfDayList(year, month),position == maxPager / 2);
                view.setData(getMonthOfDayList(dateArr[0],dateArr[1]+position-maxPager/2),position==maxPager/2);
                container.addView(view);
               views.put(position, view);

                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
                cache.addLast((CalendarView) object);
                views.remove(position);
            }
        });

        addOnPageChangeListener(new SimpleOnPageChangeListener() {
            final int[] dateArr = CalendarUtil.getYMD(new Date());
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (onItemClickListener != null) {
//                    CalendarView view = views.get(position);
//                    Object[] obs = view.getSelect();
//                    onItemClickListener.onItemClick((View) obs[0], (int) obs[1],(CalendarBean) obs[2]);
                }
                getAdapter().notifyDataSetChanged();

            }
        });
    }

    public void setCurrentItem(int year, int months, int day) {
        Log.e("Tag", views.size() + "====");

        int[] curdata = CalendarUtil.getYMD(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String str1 = curdata[0] + "-" + curdata[1] + "-" + curdata[2];
        String str2 = year + "-" + months + "-" + day;
        Calendar bef = Calendar.getInstance();
        Calendar aft = Calendar.getInstance();
        try {
            bef.setTime(sdf.parse(str1));
            aft.setTime(sdf.parse(str2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        CalendarBean bean = new CalendarBean(year, months, day);

        int result = aft.get(Calendar.MONTH) - bef.get(Calendar.MONTH);
        int month = ((aft.get(Calendar.YEAR) - bef.get(Calendar.YEAR)) * 12);

        int monthNum = month + result;
        Log.d("smile000",result+"----"+month + "------" + monthNum);
        setCurrentItem(monthNum + maxPager / 2, false);

        CalendarView view = views.get(getCurrentItem());
        Object[] obs = view.getCaledarBeanPosition(bean);

        if (obs[0] != null) {
            ((View) obs[0]).performClick();
        }

        getAdapter().notifyDataSetChanged();
    }

    private void initData() {
        //Integer.MAX_VALUE
        setCurrentItem(maxPager / 2, false);
        getAdapter().notifyDataSetChanged();

    }

    @Override
    public int[] getCurrentSelectPositon() {
        CalendarView view = views.get(getCurrentItem());
        if (view == null) {
            view = (CalendarView) getChildAt(0);
        }
        if (view != null) {
            return view.getSelectPostion();
        }
        return new int[4];
    }

    @Override
    public int getItemHeight() {
        return calendarItemHeight;
    }

    @Override
    public void setCaledarTopViewChangeListener(CaledarTopViewChangeListener listener) {
//        mCaledarLayoutChangeListener = listener;
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public int getCurrentItem() {
        return super.getCurrentItem();
    }

    public void setCurrentItem() {

        int[] data = CalendarUtil.getYMD(new Date());
        CalendarBean bean = new CalendarBean(data[0], data[1], data[2]);
        setCurrentItem(maxPager / 2, false);
        CalendarView view = views.get(getCurrentItem());
        Object[] obs = view.getCaledarBeanPosition(bean);

        if (obs[0] != null) {
            ((View) obs[0]).performClick();
        }

        getAdapter().notifyDataSetChanged();
    }


}
