package banana.digital.widjet;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MyWidget extends AppWidgetProvider {
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
    }
}
