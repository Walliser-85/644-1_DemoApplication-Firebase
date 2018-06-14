package ch.hevs.aislab.demo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.Locale;

import ch.hevs.aislab.demo.ui.BaseActivity;

public class LocaleManager {

    /**
     * @param context Current application context
     * @param selectedLanguage String representation of the selected language (e.g. "en", "de", ..)
     */
    public static void updateLanguage(Context context, String selectedLanguage) {
        if (!selectedLanguage.isEmpty()) {
            Locale locale = new Locale(selectedLanguage);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());

            SharedPreferences.Editor edit = context.getSharedPreferences(BaseActivity.PREFS_NAME, 0).edit();
            edit.putString(BaseActivity.PREFS_LNG, selectedLanguage);
            edit.commit();
        }
    }
}
