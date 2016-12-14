package com.tyvip.rewarding.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tyvip.rewarding.MainActivity;
import com.tyvip.rewarding.R;
import com.tyvip.rewarding.Utils.Constants;
import com.tyvip.rewarding.Utils.Util;

import org.json.JSONException;
import org.json.JSONObject;


public class CardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    LinearLayout linearLayout;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CardFragment newInstance(String param1, String param2) {
        CardFragment fragment = new CardFragment();
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

    View mainView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mainView = inflater.inflate(R.layout.fragment_card, container, false);
        initView();

        return mainView;
    }

    private void initView() {

        TextView textView1, textView2;
        textView1 = (TextView) mainView.findViewById(R.id.textView4);
        textView2 = (TextView) mainView.findViewById(R.id.textView7);
        linearLayout = (LinearLayout) mainView.findViewById(R.id.textlayout);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) linearLayout.getLayoutParams();
//        int dptopx = Util.dpToPx(getContext(), 22);
//        Log.d("margin", dptopx + " dp " + height);
////        Toast.makeText(getActivity(), dptopx + " dp " + height, Toast.LENGTH_LONG).show();
//        layoutParams.setMargins(0, 0, 0, height  * 22 / 1280);
//        linearLayout.setLayoutParams(layoutParams);
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
        String str = Util.GetStringFromReference(mainView.getContext(), Constants.USER_DATA);
        try {
            JSONObject jsonObject = new JSONObject(str);
            textView1.setText(jsonObject.getString("fullname"));
            textView2.setText(jsonObject.getString("cardno"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
