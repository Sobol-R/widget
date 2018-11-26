package banana.digital.widjet;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.RemoteViews;

import static banana.digital.widjet.MyWidget.REFRESH_ACTION;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        addPreferencesFromResource(R.xml.setting);

        findPreference("BACKGROUND_COLOR").setKey("BACKGROUND_COLOR_" + getWidgetId());

        final Toolbar toolbar = findViewById(R.id.setting_tb);
        toolbar.inflateMenu(R.menu.widget_menu);
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

        final Intent resultIntent = new Intent();
        resultIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, getWidgetId());
        setResult(RESULT_CANCELED, resultIntent);
    }

    private int getWidgetId() {
        final Bundle extras = getIntent().getExtras();
        return extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    private void createWidget() {
            final Intent resultIntent = new Intent();
            resultIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, getWidgetId());
            setResult(RESULT_OK, resultIntent);

            final Intent intent = new Intent(this, MyWidget.class);
            intent.setAction(REFRESH_ACTION);
            sendBroadcast(intent);
    }
}
