package com.codbking.calendar.exaple;

import android.app.Activity;
import android.app.DatePickerDialog;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;


import android.os.Environment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bigkoo.pickerview.TimePickerView;
import com.codbking.calendar.CaledarAdapter;
import com.codbking.calendar.CalendarBean;
import com.codbking.calendar.CalendarDateView;
import com.codbking.calendar.CalendarUtil;
import com.codbking.calendar.CalendarView;
import com.codbking.calendar.exaple.utils.ChangeDatePopwindow;
import com.codbking.calendar.exaple.utils.ScreenUtils;
import com.panghaha.it.timepackdemo.MainActivity;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import java.util.List;


import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;



public class CalendarActivity extends Activity implements View.OnClickListener{

    @BindView(R.id.activity_main)
    LinearLayout activity_main;

    @BindView(R.id.ll_title)
    LinearLayout ll_title;

    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.calendarDateView)
    CalendarDateView mCalendarDateView;


    @BindView(R.id.tv_day)
    TextView tvDay;
    @BindView(R.id.iv_close)
    ImageView iv_close;

    private String tv_year;
    private String tv_month ;
    private String tv_day ;
    @BindViews({R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4, R.id.tv5, R.id.tv6})
    List<TextView> tvs;


    //set background image url
    private String imageUrl = Environment.getExternalStorageDirectory().getPath() + "/.blurpics/.blurWallpaper.jpg";
    private  String backgroundBroadcase = "IST_Change_wallpaper";
    private Bitmap imageBitmap = null;
    private  boolean isday = false;
    private CaledarAdapter ca;

    int[] data = CalendarUtil.getYMD(new Date());
    private int year = data[0];
    private int month = data[1];
    private  int day = data[2];

    /** 选择日期的dialog **/
     private WindowManager manger;
    private WindowManager.LayoutParams wm;
    private Window window;

    //set background update;
    private BackgroundBroadcase mBackgroundBroadcase ;

    private LinearLayout ll_item;

   private class BackgroundBroadcase extends BroadcastReceiver{

       @Override
       public void onReceive(Context context, Intent intent) {
           String message = intent.getStringExtra(backgroundBroadcase);
           imageBitmap = BitmapFactory.decodeFile(imageUrl);

           if (message.equals(backgroundBroadcase) && imageBitmap != null){
               activity_main.setBackground(new BitmapDrawable(imageBitmap));//取指定路径的模糊背景图片
           }else{
               activity_main.setBackgroundResource(R.drawable.main_bg);//用默认背景
           }
       }
   }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv_year =getResources().getString(R.string.year);
         tv_month =getResources().getString(R.string.month);
        tv_day =getResources().getString(R.string.day);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);
        getWindows();
        initView();
        tvDay.setTextColor(getResources().getColor(R.color.gray_background));
        ll_title.setOnClickListener(this);
        tvDay.setOnClickListener(this);
        iv_close.setOnClickListener(this);
    }
    private void getWindows(){
         window = getWindow();

         wm =window.getAttributes();
            wm.height = ViewGroup.LayoutParams.MATCH_PARENT;
            wm.width = ViewGroup.LayoutParams.MATCH_PARENT;
            window.setAttributes(wm);

//        mBackgroundBroadcase = new BackgroundBroadcase();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(backgroundBroadcase);
//        registerReceiver(mBackgroundBroadcase,filter);


    }



    private void getSelectDatePicker(){
       manger = (WindowManager) getSystemService(Service.WINDOW_SERVICE);
        @SuppressWarnings("deprecation")
        int widths = (ScreenUtils.getScreenWidth(activity_main.getContext()))/3;
        int width = widths+255;
        Log.d("lml","width--"+width);
        ChangeDatePopwindow mChangeBirthDialog = new ChangeDatePopwindow(width,CalendarActivity.this);
        mChangeBirthDialog.showAsDropDown(activity_main,745,370);
        mChangeBirthDialog.setBirthdayListener(new ChangeDatePopwindow.OnBirthListener() {

            @Override
            public void onClick(String year, String month, String day) {

                // TODO Auto-generated method stub
                Toast.makeText(CalendarActivity.this,year + "-" + month + "-" + day,Toast.LENGTH_LONG).show();
                year = year.substring(0,(year.length()-1));
                month = month.substring(0,month.length()-1);

                if (day.length()>3){
                    day = day.substring(0,day.length()-3);
                }else {
                    day = day.substring(0,day.length()-1);
                }

               mCalendarDateView.setCurrentItem(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(day));
            }
        });
    }

    private void initView() {
        imageBitmap = BitmapFactory.decodeFile(imageUrl);

        if (imageBitmap != null) {
            activity_main.setBackground(new BitmapDrawable(imageBitmap));//取指定路径的模糊背景图片
        } else {
            activity_main.setBackgroundResource(R.drawable.main_bg);//用默认背景
        }

        ca = new CaledarAdapter() {
            @Override
            public View getView(View convertView, ViewGroup parentView, int selectDatePostion,CalendarBean bean) {

                if (convertView == null) {
                    convertView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.item_calendar, null);
                }

                 ll_item = (LinearLayout)convertView.findViewById(R.id.ll_item);

               LinearLayout ll_item_day = (LinearLayout) convertView.findViewById(R.id.ll_item_day);
                TextView chinaText = (TextView) convertView.findViewById(R.id.chinaText);
                TextView text = (TextView) convertView.findViewById(R.id.text);

                text.setText("" + bean.day);

                if (selectDatePostion ==1){
                    ll_item_day.setBackgroundColor(getResources().getColor(R.color.transparents));
                }else {

                    ll_item_day.setBackground(getResources().getDrawable(R.drawable.background_item_xiaomi));
                }

                if (year == bean.year && month == bean.moth && day == bean.day ){
                    ll_item.setBackgroundColor(getResources().getColor(R.color.gray_background));
                }else{
                    ll_item.setBackgroundColor(getResources().getColor(R.color.transparents));
                }
                //设置农历显示月份
                if(bean.chinaDay.equals(tv_day)){
                    //判断有没有月
                    if (bean.chinaMonth.contains(tv_month)) {
                        chinaText.setText(bean.chinaMonth);
                    }else {
                        chinaText.setText(bean.chinaMonth+tv_month);
                    }
                }else{
                    chinaText.setText(bean.chinaDay);
                }
                if (bean.mothFlag != 0) {
                    text.setTextColor(0xff9299a1);
                    chinaText.setTextColor(0xff9299a1);
                } else {
                    text.setTextColor(0xffffffff);
                    chinaText.setTextColor(0xffffffff);
                }


                return convertView;
            }
        };
        mCalendarDateView.setAdapter(ca);


        mCalendarDateView.setOnItemClickListener(new CalendarView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, CalendarBean bean) {
                Log.d("lml","bean====="+bean.day);
                if (bean.year == year && bean.moth == month && bean.day == day ){

                    tvDay.setTextColor(getResources().getColor(R.color.gray_background));
                }else{
                    tvDay.setTextColor(getResources().getColor(R.color.unselect_day));
                }

                mTitle.setText(bean.year + tv_year + bean.moth + tv_month);

                String today = bean.year+"-"+bean.moth+"-"+bean.day;
                setweek(bean.year + "/" + bean.moth + "/" + 1);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = format.parse(today);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                Calendar calendar = Calendar.getInstance();
                calendar.setFirstDayOfWeek(Calendar.MONDAY);
                calendar.setTime(date);



            }


        });

        mTitle.setText(data[0] +tv_year+ data[1] + tv_month );

        setweek(data[0] + "/" + data[1] + "/" + 1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCalendarDateView.removeAllViews();
        finish();
    }

    public void setweek(String today) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date date = null;
        try {
            date = format.parse(today);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        tvs.get(0).setText(calendar.get(Calendar.WEEK_OF_YEAR)+"");

        calendar.add(Calendar.DATE, 7);
        tvs.get(1).setText(calendar.get(Calendar.WEEK_OF_YEAR)+"");

        calendar.add(Calendar.DATE, 7);
        tvs.get(2).setText(calendar.get(Calendar.WEEK_OF_YEAR)+"");

        calendar.add(Calendar.DATE, 7);
        tvs.get(3).setText(calendar.get(Calendar.WEEK_OF_YEAR)+"");

        calendar.add(Calendar.DATE, 7);
        tvs.get(4).setText(calendar.get(Calendar.WEEK_OF_YEAR)+"");

        calendar.add(Calendar.DATE, 7);
        tvs.get(5).setText(calendar.get(Calendar.WEEK_OF_YEAR)+"");


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_title:

                getSelectDatePicker();

                break;
            case R.id.tv_day:

                mCalendarDateView.setCurrentItem();

                mTitle.setText(data[0] + tv_year + data[1] +tv_month);
                break;
            case R.id.iv_close:
                finish();
                break;

        }

    }














}
