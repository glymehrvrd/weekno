package com.glyme.weekno;

import java.text.ParseException;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RemoteViews;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Button databtn;
	private TextView textview1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//如果直接打开程序，则提示并退出
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
		if (extras != null) {
			mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		//根据mAppWidgetId判断是否是直接打开
		if(mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID){
			new AlertDialog.Builder(this)
			.setMessage("请从添加小部件处打开本程序")
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					finish();
				}
			})
			.show();
		}
		
		//初始化控件
		databtn = (Button) findViewById(R.id.button1);
		databtn.setOnClickListener(databtn_listener);
		textview1 = (TextView) findViewById(R.id.textView1);

		//显示起始日期
		SharedPreferences store = getSharedPreferences("store", 0);
		String startdate = store.getString("start_date", "1992-06-02");

		textview1.setText(startdate);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private View.OnClickListener databtn_listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			//初始化datepicker view
			LayoutInflater factory = LayoutInflater.from(MainActivity.this);
			final View v1 = factory.inflate(R.layout.datapicker, null);

			//弹出对话框选择日期
			new AlertDialog.Builder(MainActivity.this).setTitle("设置起始日期").setView(v1)
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							//获取选择的日期
							DatePicker dp = (DatePicker) v1.findViewById(R.id.datepicker1);
							String a = String.valueOf(dp.getYear()) + '-' + String.valueOf(dp.getMonth() + 1) + '-'
									+ String.valueOf(dp.getDayOfMonth());
							textview1.setText(a);

							//保存选择的日期为开始日期
							SharedPreferences store = getSharedPreferences("store", 0);
							SharedPreferences.Editor editor = store.edit();
							editor.putString("start_date", a);
							editor.commit();

							//获取Widget的信息
							Intent intent = getIntent();
							Bundle extras = intent.getExtras();
							int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
							if (extras != null) {
								mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
										AppWidgetManager.INVALID_APPWIDGET_ID);
							}
							AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(MainActivity.this);
							try {
								//更新Widget的内容
								WidgetActivity.updateAppWidget(MainActivity.this, appWidgetManager, mAppWidgetId);
							} catch (ParseException e) {
								e.printStackTrace();
							}

							//返回OK给启动器
							Intent resultValue = new Intent();
							resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
							setResult(RESULT_OK, resultValue);
							finish();
						}
					}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							finish();
						}
					}).show();
		};
	};
};
