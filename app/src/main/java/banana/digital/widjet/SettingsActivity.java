package banana.digital.widjet;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RemoteViews;
import android.widget.Toolbar;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        addPreferencesFromResource(R.xml.setting);

        final Toolbar toolbar = findViewById(R.id.setting_tb);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.done_item) {
                    createWidget();
                    finish();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void createWidget() {
        final Bundle extras = getIntent().getExtras();
        final int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
            final AppWidgetManager manager = AppWidgetManager.getInstance(this);
            final RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.clock_widget);
            manager.updateAppWidget(appWidgetId, remoteViews);

            final Intent resultIntent = new Intent();
            resultIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            setResult(RESULT_OK, resultIntent);
        }
    }
}
