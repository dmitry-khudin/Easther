package com.tyvip.rewarding.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.tyvip.rewarding.Adapters.BoxAdapter;
import com.tyvip.rewarding.MainActivity;
import com.tyvip.rewarding.R;
import com.tyvip.rewarding.Utils.Constants;
import com.tyvip.rewarding.Utils.Util;
import com.tyvip.rewarding.VipApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class BoxaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public BoxaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BoxaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BoxaFragment newInstance(String param1, String param2) {
        BoxaFragment fragment = new BoxaFragment();
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


    private void GetData()
    {
        if (!Util.isConnection(mainView.getContext()))
        {

            String boxdata = Util.GetStringFromReference(mainView.getContext(), Constants.BOXA_DATA);
            if (boxdata.equals("")) return;
            try {
                JSONArray jsonArray = new JSONArray(boxdata);
                BoxAdapter adapter = new BoxAdapter(mainView.getContext(), jsonArray);
                listView.setAdapter(adapter);
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
                        BoxAdapter adapter = new BoxAdapter(mainView.getContext(), response);
                        listView.setAdapter(adapter);
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
                    BoxAdapter adapter = new BoxAdapter(mainView.getContext(), jsonArray);
                    listView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

// Adding request to request queue
        VipApplication.getInstance().addToRequestQueue(req, tag_json_arry);
    }
    View mainView;
    ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_boxa, container, false);
        initView();
        GetData();
        return mainView;
    }

    private void initView() {

        listView = (ListView) mainView.findViewById(R.id.listview);
        mainView.setFocusableInTouchMode(true);
        mainView.requestFocus();
        mainView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i("tag", "keyCode: " + keyCode);
                if( keyCode == KeyEvent.KEYCODE_BACK ) {
                    Log.i("tag", "onKey Back listener is working!!!");
                    ( (MainActivity) getActivity()).BackPress();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }


}
