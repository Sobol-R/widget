package banana.digital.widjet;

import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.RemoteViews;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MyWidget extends AppWidgetProvider {

    private static final String REFRESH_ACTION = "REFRESH_ACTION";

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
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.clock_widget);
            views.setTextViewText(R.id.time_output, String.valueOf(hour)
                    + " : " + minstr);
            appWidgetManager.updateAppWidget(currentId, views);
        }
        scheduleUpdates(context, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction() != null && intent.getAction().equals(REFRESH_ACTION)) {
            final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            final int[] appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_ID);
            onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }

    private void scheduleUpdates(Context context, int[] appWidgetIds) {
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Intent intent = new Intent(context, MyWidget.class);
        intent.setAction(REFRESH_ACTION);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds);
        final PendingIntent pendingIntent= PendingIntent.getBroadcast(context, 0, intent, 0);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis() + 60 * 1000);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        final long time = calendar.getTimeInMillis();

        alarmManager.setExact(AlarmManager.RTC, time, pendingIntent);
    }
}
