package com.glyme.weekno;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

public class WidgetActivity extends AppWidgetProvider {
    public static WidgetActivity Widget = null;
    public static Context context;
    public static AppWidgetManager appWidgetManager;
    public static int appWidgetIds[];

    // 计算两个日期的周数差
    private static int diffOfWeek(String d1, String d2) throws ParseException {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        java.util.Date date = myFormatter.parse(d1);
        java.util.Date mydate = myFormatter.parse(d2);
        int week = (int) Math.ceil((date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000 * 7.0) + 1 / 7.0);
        return week;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        if (null == context)
            context = WidgetActivity.context;
        if (null == appWidgetManager)
            appWidgetManager = WidgetActivity.appWidgetManager;
        if (null == appWidgetIds)
            appWidgetIds = WidgetActivity.appWidgetIds;

        WidgetActivity.Widget = this;
        WidgetActivity.context = context;
        WidgetActivity.appWidgetManager = appWidgetManager;
        WidgetActivity.appWidgetIds = appWidgetIds;

        // 更新所有Widgets
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];

            try {
                updateAppWidget(context, appWidgetManager, appWidgetId);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId)
            throws ParseException {

        // 读取起始日期
        SharedPreferences store = context.getSharedPreferences("store", 0);
        String startdate = store.getString("start_date", "1992-06-02");

        // 读取当前日期
        final Calendar c = Calendar.getInstance();
        String currentdate = String.valueOf(c.get(Calendar.YEAR)) + '-' + String.valueOf(c.get(Calendar.MONTH) + 1)
                + '-' + String.valueOf(c.get(Calendar.DAY_OF_MONTH));

        CharSequence text = "本周是第" + String.valueOf(diffOfWeek(currentdate, startdate)) + "周";

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget);

        remoteViews.setTextViewText(R.id.content, text);
        remoteViews.setTextViewText(R.id.title, currentdate);

        // Tell the widget manager
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

}
