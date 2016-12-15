package com.tyvip.rewarding.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.vision.text.Line;
import com.tyvip.rewarding.Adapters.BoxAdapter;
import com.tyvip.rewarding.Adapters.FindAdapter;
import com.tyvip.rewarding.LoginActivity;
import com.tyvip.rewarding.MainActivity;
import com.tyvip.rewarding.R;
import com.tyvip.rewarding.Utils.Constants;
import com.tyvip.rewarding.Utils.Util;
import com.tyvip.rewarding.VipApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static boolean find_flag ;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    private View mainView;

    static  Button button;
    static EditText editText;
    static ListView listView;
    static LinearLayout linearLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_home, container, false);
        Log.d("frag", "create");
        initView();

        return mainView;
    }
    final String tag_json_obj = "tag_obj";



    private void initView() {
        mainView.setFocusableInTouchMode(true);
        mainView.requestFocus();
        button = (Button) mainView.findViewById(R.id.button3);
        editText = (EditText) mainView.findViewById(R.id.editText);
        listView = (ListView) mainView.findViewById(R.id.listview);
        linearLayout = (LinearLayout) mainView.findViewById(R.id.resultLayout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = editText.getText().toString();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                if (str.equals(""))
                {
                    editText.setError("Please input card number");
                    return;
                }
                if (Util.isConnection(getContext()))
                    ((MainActivity) getActivity()).ReplaceFragment(3);
                else
                {
                    {
                        Toast.makeText(getContext(), "No Internet connection! No result!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });

    }

    // TODO: Rename method, update argument and hook method into UI event

}
