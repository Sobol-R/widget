package banana.digital.widjet;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MyWidget extends AppWidgetProvider {

    public static final String REFRESH_ACTION = "REFRESH_ACTION";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        Date date = new Date();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String minstr = (minute < 10 ? "0" + String.valueOf(minute) : String.valueOf(minute));

        for (int i = 0; i < appWidgetIds.length; i++) {
            int currentId = appWidgetIds[i];

            final Bundle widgetOptions = appWidgetManager.getAppWidgetOptions(currentId);
            final int minHeight = widgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
            final RemoteViews views;
//            if (minHeight > 100) {
//                views = new RemoteViews((context.getPackageName()), )
//            }

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.clock_widget_horizontal);
            views.setTextViewText(R.id.time_output, String.valueOf(hour)
                    + " : " + minstr);

            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            final int color = sharedPreferences.getInt("BACKGROUND_COLOR_" + appWidgetIds[i], Color.BLACK);
            views.setInt(R.id.root_view, "setBackgroundColor", color);

            Intent intent = new Intent(context, SettingsActivity.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, currentId);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.root_view, pendingIntent);

            appWidgetManager.updateAppWidget(currentId, views);
        }
        scheduleUpdates(context);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        final Intent intent = new Intent(context, MyWidget.class);
        intent.setAction(REFRESH_ACTION);
        context.sendBroadcast(intent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction() != null && intent.getAction().equals(REFRESH_ACTION)) {
            final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName name  = new ComponentName(context, MyWidget.class);
            final int[] appWidgetIds = appWidgetManager.getAppWidgetIds(name);
            onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }

    private void scheduleUpdates(Context context) {
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Intent intent = new Intent(context, MyWidget.class);
        intent.setAction(REFRESH_ACTION);
        final PendingIntent pendingIntent= PendingIntent.getBroadcast(context, 0, intent, 0);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis() + 60 * 1000);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        final long time = calendar.getTimeInMillis();

        alarmManager.setExact(AlarmManager.RTC, time, pendingIntent);
    }

}
