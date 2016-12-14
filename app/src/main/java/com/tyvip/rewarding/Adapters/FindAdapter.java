package com.tyvip.rewarding.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tyvip.rewarding.R;
import com.tyvip.rewarding.Utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bryden on 12/8/16.
 */

public class FindAdapter extends BaseAdapter {

    private Context context;
    private JSONArray list;

    LayoutInflater layoutInflater;
    public FindAdapter(Context context, JSONArray list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.length();
    }

    @Override
    public JSONObject getItem(int i) {
        try {
            return list.getJSONObject(i);
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null)
        {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.fragment_find_item, null);

        }
        TextView textView1, textView2, textView3, textView4, textView5;

        JSONObject model = getItem(i);
        textView1 = (TextView) view.findViewById(R.id.textView2);
        textView2 = (TextView) view.findViewById(R.id.textView3);
        textView3 = (TextView) view.findViewById(R.id.textView4);
        textView4 = (TextView) view.findViewById(R.id.textView5);
        textView5 = (TextView) view.findViewById(R.id.textView6);

        try {
            if (model != null) {
                textView1.setText("Message: " + model.getString("Message"));
                textView2.setText("bid: " +  model.getInt("bid"));
                textView3.setText("reward: " +  model.getString("reward"));
                textView4.setText("claim: " +  model.getInt("claim"));
                textView5.setText("creationdate: " +  model.getString("creationdate"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }
}
