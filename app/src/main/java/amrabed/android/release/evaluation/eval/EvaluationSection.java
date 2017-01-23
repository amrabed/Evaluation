package amrabed.android.release.evaluation.eval;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import amrabed.android.release.evaluation.R;
import amrabed.android.release.evaluation.core.DayList;

/**
 * Evaluation section
 *
 * @author AmrAbed
 */

public class EvaluationSection extends Fragment //implements LoaderManager.LoaderCallbacks<List<Day>>
{
	private static final String TAG = EvaluationSection.class.getName();

	private DayList dayList;
	private ViewPager pager;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
	{
		final View view = inflater.inflate(R.layout.day_view, parent, false);
		pager = (ViewPager) view.findViewById(R.id.pager);

//		getLoaderManager().initLoader(0, null, this);
		//ToDo: Move to AsyncTaskLoader
		dayList = DayList.load();
		pager.setAdapter(new SectionPagerAdapter(getActivity(), getChildFragmentManager(), dayList));
		pager.setCurrentItem(this.dayList.size() - 1);

		return view;
	}

//	@Override
//	public void onLoaderReset(Loader<List<Day>> loader)
//	{
//		Log.e(TAG, "OnLoaderReset");
//	}
//
//	@Override
//	public Loader<List<Day>> onCreateLoader(int i, Bundle bundle)
//	{
//		return new AsyncTaskLoader<List<Day>>(getActivity())
//		{
//			@Override
//			public List<Day> loadInBackground()
//			{
//				return ApplicationEvaluation.getDatabase().getAllEntries();
//			}
//		};
//	}
//
//	@Override
//	public void onLoadFinished(Loader<List<Day>> loader, List<Day> dayList)
//	{
//		Log.i(TAG, "Load finished: " + dayList.toString());
//		this.dayList = dayList;
//		// ToDo: API 14
//		pager.setAdapter(new SectionPagerAdapter(getChildFragmentManager(), dayList));
//		pager.setCurrentItem(this.dayList.size() - 1);
//
//	}
}
