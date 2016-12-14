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

    public static void AdjustView(boolean flag)
    {
//
        find_flag = flag;
        if (flag == true)
        {
            editText.setVisibility(View.VISIBLE);
            button.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
            editText.setText("");
        }
        else
        {
            editText.setVisibility(View.GONE);
            button.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        }
    }
    private void GetData()
    {
        String boxdata = Util.GetStringFromReference(mainView.getContext(), Constants.BOXA_DATA);
        if (!Util.isConnection(mainView.getContext()))
        {


            if (boxdata.equals("")) return;
            try {
                JSONArray jsonArray = new JSONArray(boxdata);
                BoxAdapter adapter = new BoxAdapter(mainView.getContext(), jsonArray);
                listView.setAdapter(adapter);
                AdjustView(false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return;
        }
        final String tag_json_arry = "json_array_req";
        String str = Util.GetStringFromReference(mainView.getContext(), Constants.USER_DATA);
        String url = "";
        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONObject param = new JSONObject();
            param.put("userid", jsonObject.getInt("userid"));
            param.put("userbid", jsonObject.getInt("userbid"));
            url = Constants.GETVIEW_URL+"?userid=" + jsonObject.getInt("userid") + "&userbid=" + jsonObject.getInt("userbid");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        final ProgressDialog pDialog = new ProgressDialog(mainView.getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.POST, url,
                null,  new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(tag_json_arry, response.toString());
                Util.SaveStringToReference(mainView.getContext(), response.toString(), Constants.BOXA_DATA);
                FindAdapter adapter = new FindAdapter(mainView.getContext(), response);
                listView.setAdapter(adapter);
                AdjustView(false);
                pDialog.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(tag_json_arry, "Error: " + error.getMessage());
                pDialog.hide();
                String boxdata = Util.GetStringFromReference(mainView.getContext(), Constants.BOXA_DATA);
                if (boxdata.equals("")) return;
                try {
                    JSONArray jsonArray = new JSONArray(boxdata);
                    FindAdapter adapter = new FindAdapter(mainView.getContext(), jsonArray);
                    listView.setAdapter(adapter);
                    AdjustView(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

// Adding request to request queue
        VipApplication.getInstance().addToRequestQueue(req, tag_json_arry);
    }
    private void PostLocation()
    {
        String str = Util.GetStringFromReference(mainView.getContext(), Constants.USER_DATA);
        String url = Constants.GETLOCATION_URL;
        JSONObject jsonObject = new JSONObject();
        try {
            JSONObject jsonUser = new JSONObject(str);
            jsonObject.put("userid", jsonUser.getInt("userid"));
            jsonObject.put("userbid", jsonUser.getInt("userbid"));
            jsonObject.put("lat", Constants.user_latitude);
            jsonObject.put("lng", Constants.user_longitude);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(tag_json_obj, response.toString());
                        ((MainActivity) getActivity()).ReplaceFragment(3);
                     //   GetData();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(tag_json_obj, "ErrorLocation: " + error.getMessage());
                ((MainActivity) getActivity()).ReplaceFragment(3);
//                GetData();
            }
        }) ;
        VipApplication.getInstance().addToRequestQueue(jsonObjReq, "location");
    }
    private void initView() {
        mainView.setFocusableInTouchMode(true);
        mainView.requestFocus();
//        mainView.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                Log.i("tag", "keyCode: " + keyCode);
//                if( keyCode == KeyEvent.KEYCODE_BACK ) {
//                    Log.i("tag", "onKey Back listener is working!!!");
//                     {
//                        Log.i("tag", "onKey Back listener is true!!!");
//
//                        ((MainActivity) getActivity()).BackPress();
//                        return true;
//                    }
//
//
//                } else {
//                    return false;
//                }
//            }
//        });
        button = (Button) mainView.findViewById(R.id.button3);
        editText = (EditText) mainView.findViewById(R.id.editText);
        listView = (ListView) mainView.findViewById(R.id.listview);
        linearLayout = (LinearLayout) mainView.findViewById(R.id.resultLayout);
//        AdjustView(true);
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
                PostLocation();
            }
        });

    }

    // TODO: Rename method, update argument and hook method into UI event

}
