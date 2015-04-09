package songbook.asu.ax.songbook.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;

import java.util.Date;

import songbook.asu.ax.songbook.EventAdapter;
import songbook.asu.ax.songbook.R;
import songbook.asu.ax.songbook.Utilities;
import songbook.asu.ax.songbook.data.SongContract;

/**
 * Created by Kristian on 2015-04-09.
 */
public class EventCalendarFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String LOG_TAG = EventCalendarFragment.class.getSimpleName();
    private EventAdapter futureEventAdapter;
    private EventAdapter pastEventAdapter;
    private EventAdapter todayEventAdapter;

    private static final int EVENT_FUTURE_LOADER = 0;
    private static final int EVENT_PAST_LOADER = 1;
    private static final int EVENT_TODAY_LOADER = 2;

    private static final String SELECTION_STRING_TODAY_EVENT = "event_date = ?";
    private static final String SELECTION_STRING_PAST_EVENT = "event_date < ?";
    private static final String SELECTION_STRING_FUTURE_EVENT = "event_date > ?";

    private static final String[] EVENT_COLUMNS = {
            SongContract.EventTable.NAME + "." + SongContract.EventTable._ID,
            SongContract.EventTable.COLUMN_EVENT_NAME,
            SongContract.EventTable.COLUMN_EVENT_DATE
    };

    public static final int COL_EVENT_NAME = 1;
    public static final int COL_EVENT_DATE = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_event, container, false);

        ListView listViewFutureEvents = (ListView) getActivity().findViewById(R.id.listview_future_events);
        ListView listViewPastEvents = (ListView) getActivity().findViewById(R.id.listview_past_events);
        ListView listViewTodayEvents = (ListView) getActivity().findViewById(R.id.listview_today_events);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(EVENT_TODAY_LOADER, null, this);
        getLoaderManager().initLoader(EVENT_FUTURE_LOADER,null,this);
        getLoaderManager().initLoader(EVENT_PAST_LOADER,null,this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = SongContract.EventTable.COLUMN_EVENT_NAME + " ASC";

        Uri eventUri = SongContract.EventTable.buildEventUri("");
        String[] dateList = new String[]{Utilities.formatDateString(new Date())};

        switch (id){
            case EVENT_TODAY_LOADER:
                return new CursorLoader(getActivity(),
                        eventUri,
                        EVENT_COLUMNS,
                        SELECTION_STRING_TODAY_EVENT,
                        dateList,
                        sortOrder
                );
            case EVENT_FUTURE_LOADER:
                return new CursorLoader(getActivity(),
                        eventUri,
                        EVENT_COLUMNS,
                        SELECTION_STRING_FUTURE_EVENT,
                        dateList,
                        sortOrder
                );
            case EVENT_PAST_LOADER:
                return new CursorLoader(getActivity(),
                        eventUri,
                        EVENT_COLUMNS,
                        SELECTION_STRING_PAST_EVENT,
                        dateList,
                        sortOrder
                );
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int id = loader.getId();

        switch (id){
            case EVENT_TODAY_LOADER:
                todayEventAdapter.swapCursor(data);
                break;
            case EVENT_FUTURE_LOADER:
                futureEventAdapter.swapCursor(data);
                break;
            case EVENT_PAST_LOADER:
                pastEventAdapter.swapCursor(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
