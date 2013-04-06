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

		//���ֱ�Ӵ򿪳�������ʾ���˳�
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
		if (extras != null) {
			mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		//����mAppWidgetId�ж��Ƿ���ֱ�Ӵ�
		if(mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID){
			new AlertDialog.Builder(this)
			.setMessage("������С�������򿪱�����")
			.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					finish();
				}
			})
			.show();
		}
		
		//��ʼ���ؼ�
		databtn = (Button) findViewById(R.id.button1);
		databtn.setOnClickListener(databtn_listener);
		textview1 = (TextView) findViewById(R.id.textView1);

		//��ʾ��ʼ����
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
			//��ʼ��datepicker view
			LayoutInflater factory = LayoutInflater.from(MainActivity.this);
			final View v1 = factory.inflate(R.layout.datapicker, null);

			//�����Ի���ѡ������
			new AlertDialog.Builder(MainActivity.this).setTitle("������ʼ����").setView(v1)
					.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							//��ȡѡ�������
							DatePicker dp = (DatePicker) v1.findViewById(R.id.datepicker1);
							String a = String.valueOf(dp.getYear()) + '-' + String.valueOf(dp.getMonth() + 1) + '-'
									+ String.valueOf(dp.getDayOfMonth());
							textview1.setText(a);

							//����ѡ�������Ϊ��ʼ����
							SharedPreferences store = getSharedPreferences("store", 0);
							SharedPreferences.Editor editor = store.edit();
							editor.putString("start_date", a);
							editor.commit();

							//��ȡWidget����Ϣ
							Intent intent = getIntent();
							Bundle extras = intent.getExtras();
							int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
							if (extras != null) {
								mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
										AppWidgetManager.INVALID_APPWIDGET_ID);
							}
							AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(MainActivity.this);
							try {
								//����Widget������
								WidgetActivity.updateAppWidget(MainActivity.this, appWidgetManager, mAppWidgetId);
							} catch (ParseException e) {
								e.printStackTrace();
							}

							//����OK��������
							Intent resultValue = new Intent();
							resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
							setResult(RESULT_OK, resultValue);
							finish();
						}
					}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							finish();
						}
					}).show();
		};
	};
};
