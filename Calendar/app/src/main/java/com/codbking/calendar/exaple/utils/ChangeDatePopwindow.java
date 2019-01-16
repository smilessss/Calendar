package com.codbking.calendar.exaple.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.codbking.calendar.exaple.R;
import com.panghaha.it.timepackdemo.view.wheelview.OnWheelChangedListener;
import com.panghaha.it.timepackdemo.view.wheelview.OnWheelScrollListener;
import com.panghaha.it.timepackdemo.view.wheelview.WheelView;
import com.panghaha.it.timepackdemo.view.wheelview.adapter.AbstractWheelTextAdapter1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Author:  Chen.yuan
 * Email:   hubeiqiyuan2010@163.com
 * Date:    2016/7/28 17:37
 * Description:日期选择window
 */
public class ChangeDatePopwindow extends PopupWindow implements View.OnClickListener {

	public static Context context;
	private WheelView wvYear;
	private WheelView wvMonth;
	private WheelView wvDay;

	private TextView btnSure;
	private TextView btnCancel;

	private ArrayList<String> arry_years = new ArrayList<String>();
	private ArrayList<String> arry_months = new ArrayList<String>();
	private ArrayList<String> arry_days = new ArrayList<String>();
	private CalendarTextAdapter mYearAdapter;
	private CalendarTextAdapter mMonthAdapter;
	private CalendarTextAdapter mDaydapter;

	private String month;
	private String day;

	private String currentYear = getYear();
	private String currentMonth = getMonth();
	private String currentDay = getDay();

	private int maxTextSize = 26;
	private int minTextSize = 22;

	private boolean issetdata = false;

	private String selectYear;
	private String selectMonth;
	private String selectDay;

	private OnBirthListener onBirthListener;

	public ChangeDatePopwindow(final int width, final Context context) {
		super(context);
		this.context = context;
		View view=View.inflate(context, R.layout.dialog_myinfo_changebirth,null);
		wvYear = (WheelView) view.findViewById(R.id.wv_birth_year);
		wvMonth = (WheelView) view.findViewById(R.id.wv_birth_month);
		wvDay = (WheelView) view.findViewById(R.id.wv_birth_day);
		btnSure = (TextView) view.findViewById(R.id.btn_myinfo_sure);
		btnCancel = (TextView) view.findViewById(R.id.btn_myinfo_cancel);

		//设置SelectPicPopupWindow的View
		this.setContentView(view);



		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(width);
		//设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

		//设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		//设置SelectPicPopupWindow弹出窗体动画效果
//		this.setAnimationStyle(R.style.AnimBottom);
		//实例化一个ColorDrawable颜色为半透明
		//ColorDrawable dw = new ColorDrawable(0xb0000000);
		//设置SelectPicPopupWindow弹出窗体的背景
		//this.setBackgroundDrawable(dw);
		this.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popu_bg));

		btnSure.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

		if (!issetdata) {

			initData();

		}
		initYears();
		mYearAdapter = new CalendarTextAdapter(context, arry_years, setYear(currentYear), maxTextSize, minTextSize);
		wvYear.setVisibleItems(5);
		wvYear.setViewAdapter(mYearAdapter);
		wvYear.setCurrentItem(setYear(currentYear));

		initMonths(Integer.parseInt(month));
		mMonthAdapter = new CalendarTextAdapter(context, arry_months, setMonth(currentMonth), maxTextSize, minTextSize);
		wvMonth.setVisibleItems(5);
		wvMonth.setViewAdapter(mMonthAdapter);
		wvMonth.setCurrentItem(setMonth(currentMonth));
		weekMonth = currentMonth;
		initDays(Integer.parseInt(day));

		mDaydapter = new CalendarTextAdapter(context, arry_days, Integer.parseInt(currentDay) -1, maxTextSize, minTextSize);
		wvDay.setVisibleItems(5);
		wvDay.setViewAdapter(mDaydapter);
		wvDay.setCurrentItem(Integer.parseInt(currentDay) - 1);

		wvYear.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
				selectYear = currentText;

				setTextviewSize(currentText, mYearAdapter);
				currentYear = currentText.substring(0, currentText.length()-1).toString();
				Log.d("currentYear==",currentYear);
				setYear(currentYear);
				initMonths(Integer.parseInt(month));
				Log.d("hzs","year : weekMonth = " + weekMonth);
				setMonth(weekMonth);
				initDays(Integer.parseInt(day));
				mMonthAdapter = new CalendarTextAdapter(context, arry_months, 0, maxTextSize, minTextSize);
				wvMonth.setVisibleItems(5);
				wvMonth.setViewAdapter(mMonthAdapter);
				wvMonth.setCurrentItem(0);
				mDaydapter = new CalendarTextAdapter(context, arry_days, 0, maxTextSize, minTextSize);
				wvDay.setVisibleItems(5);
				wvDay.setViewAdapter(mDaydapter);
				wvDay.setCurrentItem(0);
				calDays(currentYear, month);
			}
		});

		wvYear.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
				Log.d("hzs","onScrollingFinished currentText = " + currentText);
				setTextviewSize(currentText, mYearAdapter);
			}
		});

		wvMonth.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) mMonthAdapter.getItemText(wheel.getCurrentItem());
				selectMonth = currentText;
				setTextviewSize(currentText, mMonthAdapter);
				setMonth(currentText.substring(0, currentText.length() - 1));
				weekMonth = currentText.substring(0, currentText.length() - 1);
				Log.d("hzs","month : weekMonth = " + weekMonth);
				initDays(Integer.parseInt(day));
				mDaydapter = new CalendarTextAdapter(context, arry_days, 0, maxTextSize, minTextSize);
				wvDay.setVisibleItems(5);
				wvDay.setViewAdapter(mDaydapter);
				wvDay.setCurrentItem(0);
				calDays(currentYear, month);
			}
		});

		wvMonth.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) mMonthAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, mMonthAdapter);
			}
		});

		wvDay.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) mDaydapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, mDaydapter);
				selectDay = currentText;
			}
		});

		wvDay.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) mDaydapter.getItemText(wheel.getCurrentItem());


				setTextviewSize(currentText, mDaydapter);
			}
		});
	}


	public void initYears() {
		for (int i = Integer.parseInt(getYear()) - 40; i <= Integer.parseInt(getYear()) + 40; i++) {
			arry_years.add(i + context.getResources().getString(R.string.year));
		}
	}

	public void initMonths(int months) {
		arry_months.clear();
		for (int i = 1; i <= months; i++) {
			arry_months.add(i + context.getResources().getString(R.string.month));
		}
	}

	private String weekMonth;
	public void initDays(int days) {
		arry_days.clear();
		Log.d("hzs","currentYear = " + currentYear + "weekMonth = " + weekMonth + "days = " + days);
		for (int i = 1; i <= days; i++) {
			arry_days.add(i + context.getResources().getString(R.string.days) + getWeek(currentYear + "-" + weekMonth + "-" + i));

			Log.d("hzs","currentYear = " + currentYear + "weekMonth = " + weekMonth + "days = " + i);


		}
	}

	private class CalendarTextAdapter extends AbstractWheelTextAdapter1 {
		ArrayList<String> list;

		protected CalendarTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize) {
			super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);
			this.list = list;
			setItemTextResource(R.id.tempValue);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return list.get(index) + "";
		}
	}

	public void setBirthdayListener(OnBirthListener onBirthListener) {
		this.onBirthListener = onBirthListener;
	}

	@Override
	public void onClick(View v) {

		if (v == btnSure) {
			if (onBirthListener != null) {
				onBirthListener.onClick(selectYear, selectMonth, selectDay);
				Log.d("cy",""+selectYear+""+selectMonth+""+selectDay);
			}
		} else if (v == btnSure) {

		}  else {
			dismiss();
		}
		dismiss();

	}

	public interface OnBirthListener {
		public void onClick(String year, String month, String day);
	}

	/**
	 * 设置字体大小
	 *
	 * @param curriteItemText
	 * @param adapter
	 */
	public void setTextviewSize(String curriteItemText, CalendarTextAdapter adapter) {
		ArrayList<View> arrayList = adapter.getTestViews();
		int size = arrayList.size();
		String currentText;
		for (int i = 0; i < size; i++) {
			TextView textvew = (TextView) arrayList.get(i);
			currentText = textvew.getText().toString();
			if (curriteItemText.equals(currentText)) {
				textvew.setTextSize(maxTextSize);
				textvew.setTextColor(context.getResources().getColor(R.color.color_323232));
			} else {
				textvew.setTextSize(minTextSize);
				textvew.setTextColor(context.getResources().getColor(R.color.huise));
			}
		}
	}

	public String getYear() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.YEAR)+"";
	}

	public String getMonth() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.MONTH) + 1+"";
	}

	public String getDay() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.DATE)+"";
	}

	public void initData() {
		Log.d("lml","getDay=="+getDay());
		setDate(getYear(), getMonth(), getDay());
//		this.currentDay = 1+"";
//		this.currentMonth = 1+"";
	}

	/**
	 * 设置年月日
	 * 
	 * @param year
	 * @param month
	 * @param day
	 */
	public void setDate(String year, String month, String day) {
		selectYear = year + context.getResources().getString(R.string.year);
		selectMonth = month + context.getResources().getString(R.string.month);
		selectDay = day + context.getResources().getString(R.string.days);
		issetdata = true;
		this.currentYear = year;
		this.currentMonth = month;
		this.currentDay = day;
//		if (year == getYear()) {
//			this.month = getMonth();
//		} else {
			this.month = 12+"";
		//}
		calDays(year, month);
	}

	/**
	 * 设置年份
	 * 
	 * @param year
	 */
	public int setYear(String year) {
		int yearIndex = 0;
		//if (!year.equals(getYear())) {
			this.month = 12+"";
//		} else {
//			this.month = getMonth();
//		}
		for (int i = Integer.parseInt(getYear()) - 40; i <= Integer.parseInt(getYear()) + 40; i++) {
			if (i == Integer.parseInt(year)) {
				return yearIndex;
			}
			yearIndex++;
		}
		return yearIndex;
	}

	/**
	 * 设置月份
	 * 
	 * @param month
	 * @param month
	 * @return
	 */
	public int setMonth(String month) {
		int monthIndex = 0;
		calDays(currentYear, month);
		for (int i = 1; i < Integer.parseInt(this.month); i++) {
			if (Integer.parseInt(month) == i) {
				return monthIndex;
			} else {
				monthIndex++;
			}
		}
		return monthIndex;
	}

	/**
	 * 计算每月多少天
	 * 
	 * @param month
	 * @param year
	 */
	public void calDays(String year, String month) {
		boolean leayyear = false;
		if (Integer.parseInt(year) % 4 == 0 && Integer.parseInt(year) % 100 != 0) {
			leayyear = true;
		} else {
			leayyear = false;
		}
//		for (int i = 1; i <= 12; i++) {
			switch (Integer.parseInt(month)) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				this.day = 31+"";
				break;
			case 2:
				if (leayyear) {
					this.day = 29+"";
				} else {
					this.day = 28+"";
				}
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				this.day = 30+"";
				break;
			}
//		}
//		if (year.equals( getYear()) && month .equals( getMonth())) {
//			this.day = getDay();
//		}
	}

	public static String getWeek(String time) {
		String Week = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(format.parse(time));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		int wek=c.get(Calendar.DAY_OF_WEEK);

		if (wek == 1) {
			Week += context.getResources().getString(R.string.sunday);
		}
		if (wek == 2) {
			Week += context.getResources().getString(R.string.monday);
		}
		if (wek == 3) {
			Week += context.getResources().getString(R.string.tuesday);
		}
		if (wek == 4) {
			Week += context.getResources().getString(R.string.wednesday);
		}
		if (wek == 5) {
			Week += context.getResources().getString(R.string.thursday);
		}
		if (wek == 6) {
			Week += context.getResources().getString(R.string.friday);
		}
		if (wek == 7) {
			Week +=context.getResources().getString(R.string.saturday);
		}
		return Week;
	}
}