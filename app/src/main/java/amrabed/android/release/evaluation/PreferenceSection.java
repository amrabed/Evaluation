package amrabed.android.release.evaluation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import java.util.Set;

import amrabed.android.release.evaluation.db.DatabaseUpdater;

/**
 * Preferences Fragment
 *
 * @author AmrAbed
 */
public class PreferenceSection extends PreferenceFragment
		implements SharedPreferences.OnSharedPreferenceChangeListener
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, false);
		addPreferencesFromResource(R.xml.preferences);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		getActivity().setTitle(R.string.menu_preferences);
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onPause()
	{
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
		super.onPause();
	}


	@Override
	public void onSharedPreferenceChanged(SharedPreferences preferences, String key)
	{
		if (key.equals("reciteDays") || key.equals("memorizeDays") || key.equals("dietDays") || key
				.equals("fastingDays"))
		{
			getActivity().startService(new Intent(getActivity().getApplicationContext(),
					DatabaseUpdater.class));
			return;
		}
		switch (key)
		{
			case "recite":
				preferences.edit().putInt("reciteDays", getValue(key)).apply();
				break;
			case "memorize":
				preferences.edit().putInt("memorizeDays", getValue(key)).apply();
				break;
			case "diet":
				preferences.edit().putInt("dietDays", getValue(key)).apply();
				break;
			case "fasting":
				int v = getValue(key);
				preferences.edit().putInt("fastingDays", v).apply();
				if ((v & 0x08) == 0)
				{
					preferences.edit().remove("ldof").apply();
				}
				break;
		}
	}

	private int getValue(String key)
	{
		int val = 0;
		MultiSelectListPreference pref = (MultiSelectListPreference) findPreference(key);
		Set<String> s = pref.getValues();
		String[] values = new String[7];
		s.toArray(values);
		for (int i = 0; i < s.size(); i++)
		{
			int day = Integer.parseInt(values[i]);
			val |= 0x01 << day;
		}
		return val;
	}

}
