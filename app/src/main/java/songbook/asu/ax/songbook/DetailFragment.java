package songbook.asu.ax.songbook;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by EIS i7 Gamer on 2015-03-02.
 */
public class DetailFragment extends Fragment{

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    public static String IS_ADMIN = "is_admin";

    public DetailFragment(){
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        String songName = "";
        String melody = "";
        String songText = "";
        boolean isAdmin = false;

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(MainFragment.SONG_NAME) ) {
            songName = bundle.getString(MainFragment.SONG_NAME);
            melody = bundle.getString(MainFragment.MELODY);
            songText = bundle.getString(MainFragment.TEXT);


            if (bundle.containsKey(IS_ADMIN)){
                isAdmin = bundle.getBoolean(IS_ADMIN);
        }

        }

        TextView nameView = (TextView) rootView.findViewById(R.id.song_name_textview_detail);
        nameView.setText(songName);

        TextView melodyView = (TextView) rootView.findViewById(R.id.melody_textview_detail);
        melodyView.setText(melody);

        TextView textView = (TextView) rootView.findViewById(R.id.text_text_view_detail);
        textView.setText(songText);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}

