package com.davilapps.youlocator;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OneFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OneFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static ArrayList<String> result = new ArrayList<>();


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public OneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OneFragment newInstance(String param1, String param2) {
        OneFragment fragment = new OneFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    Button searchButton;
    EditText searchContact;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_one, container, false);



        searchContact = (EditText) v.findViewById(R.id.editSearchText);

        searchButton = (Button) v.findViewById(R.id.buttonSearch);

        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //make your toast here
                //final List<String> infoList = new ArrayList<String>();

                System.out.println("INSIDE POR FIN");
                ParseQuery<ParseObject> query = ParseQuery.getQuery("MyFirstClass");
                query.whereEqualTo("email", searchContact.getText().toString());
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {

                            List<List<String>> totalResult = new ArrayList<>();
//                            ArrayList<String> result = new ArrayList<>();
                            System.out.println("RAW ARRAY" + result);
                             String mLat;
                            String mLon;

//                            String userNameFinal = object.getString("Username");
                            String emailPlayer =  object.getString("email");
//                            double mLatitude = object.getDouble("latitude");
//                            double mLongitude = object.getDouble("longitude");
                            System.out.println("Testing email: " + emailPlayer);

                            result.add(emailPlayer);
//                            mLat = String.valueOf("mLatitude");
//                            result.add(mLat);
//                            mLon = String.valueOf("mLongitude");
//                            result.add(mLon);
//                            result.add("userNameFinal");
//                            totalResult.add(result);
//                            //System.out.println("ADDED + mLat: " + result + mLatitude + mLongitude + emailPlayer + userNameFinal);
//                            System.out.println("TOTAL RESULT" + emailPlayer);

                            saveArrayList(result, "email");
                            System.out.println("TEST ARRAY SAVE: " + result);

                            getArrayList( "email");
                            System.out.println("GET ARRAY LIST: " + result);

                            totalResult.add(result);
                            System.out.println("TOTAL ARRAY LIST: " + totalResult);


                            for (String member : result){
                                Log.i("Member name: ", member);
                            }



                            // ADD VALUES TO A NEW CLASS or new table?
                            // HERE CREATE A NEW CLASS IN PARSE THEN SAVE THE OBJECTS IN THIS NEW CLASS FRIENDS CLASS
//                            final ParseObject MyFriendsClass = new ParseObject("MyFriendsClass"); // When I press panic, it creates a new record
//                            MyFriendsClass.put("latitude", mLat);
//                            MyFriendsClass.put("longitude", mLon);
//                            MyFriendsClass.put("Username", userNameFinal);
//                            MyFriendsClass.put("alerttext", "");
//                            MyFriendsClass.put("email", emailPlayer);
//
//                            MyFriendsClass.saveInBackground(new SaveCallback() {
//                                @Override
//                                public void done(ParseException e) {
//                                    if (e == null) {
//                                        // Success
//                                        System.out.println("Friend Class created");
//                                    } else {
//                                        // Error
//                                        System.out.println("Friend Class NOT created");
//                                    }
//                                }
//                            });
                        }
                    }
                });
            }
        });
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void saveArrayList(ArrayList<String> list, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public ArrayList<String> getArrayList(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);

    }
}
