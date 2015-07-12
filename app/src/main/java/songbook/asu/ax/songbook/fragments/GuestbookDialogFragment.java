package songbook.asu.ax.songbook.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import songbook.asu.ax.songbook.R;

/**
 * Created by EIS i7 Gamer on 2015-07-11.
 */
public class GuestbookDialogFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_form, container, false);


        return rootView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        return super.onCreateDialog(savedInstanceState);
    }


}
