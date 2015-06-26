package network.login.phani.clientlogin;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Signin.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Signin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Signin extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Signin.
     */
    // TODO: Rename and change types and number of parameters
    public static Signin newInstance(String param1, String param2) {
        Signin fragment = new Signin();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Signin() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        signin();
    }

    private void signin() {
        new SiginTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.signin, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    private Handler liveRequestHandler = new Handler();

    private void liveRequest(boolean isFirstTime, String sessionId) {
        new LiveRequestTask().execute(String.valueOf(isFirstTime), sessionId);
    }

    public class LiveRequestTask extends AsyncTask<String, Void, Void> {
        private final String LOG_TAG=LiveRequestTask.class.getSimpleName();

        @Override
        protected Void doInBackground(String... params) {
            Log.d(LOG_TAG, "Triggering live request");
            Uri uri = Uri.parse("http://10.10.10.1/24online/webpages/liverequest.jsp?").buildUpon()
                    .appendQueryParameter("isfirsttime", params[0])
                    .appendQueryParameter("r", System.currentTimeMillis()+"").build();
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection)new URL(uri.toString()).openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Cookie", params[1]);
                connection.connect();
            } catch(IOException e) {
                Log.e(LOG_TAG, "Error", e);
            } finally {
                if(connection!=null) {
                    connection.disconnect();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d(LOG_TAG, "Waiting to show toast");
            Toast.makeText(getActivity(), "Client online check", Toast.LENGTH_SHORT).show();
        }
    }

    public class SiginTask extends AsyncTask<Void, Void, Map<String, List<String>>> {
        private final String LOG_TAG=SiginTask.class.getSimpleName();

        @Override
        protected Map<String, List<String>> doInBackground(Void... params) {
            Log.d(LOG_TAG, "Running in background");
            Uri uri = Uri.parse("http://10.10.10.1/24online/servlet/E24onlineHTTPClient?alerttime=null&checkClose=1&chrome=-1&dtold=0&guestmsgreq=false&ipaddress=10.100.172.229&isAccessDenied=false&login=Login&logintype=2&mac=14%3Ad6%3A4d%3Aa2%3A4c%3Ab4&message=&mode=191&orgSessionTimeout=-1&password=saibaba&popupalert=0&saveinfo=saveinfo&servername=10.10.10.1&sessionTimeout=-1&timeout=-1&url=null&username=ntrphani");
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection)new URL(uri.toString()).openConnection();
                connection.setRequestMethod("POST");
                connection.connect();
                return connection.getHeaderFields();
            } catch(IOException e) {
                Log.e(LOG_TAG, "Error", e);
                return null;
            } finally {
                if(connection != null) {
                    connection.disconnect();
                }
            }
        }

        @Override
        protected void onPostExecute(final Map<String, List<String>> responseHeaders) {
            super.onPostExecute(responseHeaders);
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
//            final String sessionId = sessionId(responseHeaders);
//            liveRequestHandler.postDelayed(new Runnable() {
//                private boolean isFirstTime = true;
//
//                @Override
//                public void run() {
//                    liveRequest(isFirstTime, sessionId);
//                    isFirstTime = false;
//                    liveRequestHandler.postDelayed(this, 180000);
//                }
//            }, 0);
        }

        private String sessionId(Map<String, List<String>> responseHeaders) {
            List<String> cookies = responseHeaders.get("Set-Cookie");
            for(String cookie: cookies) {
                if(cookie.startsWith("JSESSIONID=")) {
                    return cookie.split(";")[0];
                }
            }
            return null;
        }
    }
}
