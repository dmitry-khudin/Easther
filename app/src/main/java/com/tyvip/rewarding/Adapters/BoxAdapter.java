package com.tyvip.rewarding.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import com.tyvip.rewarding.Models.BoxModel;
import com.tyvip.rewarding.R;
import com.tyvip.rewarding.Utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bryden on 12/8/16.
 */

public class BoxAdapter extends BaseAdapter {

    private Context context;
    private JSONArray list;

    LayoutInflater layoutInflater;
    public BoxAdapter(Context context, JSONArray list) {
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
            view = layoutInflater.inflate(R.layout.fragment_item, null);

        }
        TextView textView1, textView2;
        ImageView imageView, imgIcon;

        JSONObject model = getItem(i);
        textView1 = (TextView) view.findViewById(R.id.textView2);
        textView2 = (TextView) view.findViewById(R.id.textView3);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        imgIcon = (ImageView) view.findViewById(R.id.imageView2);

        try {
            if (model != null) {
                textView1.setText(model.getString("reward"));
                textView2.setText(model.getString("creationdate"));
                String imgURL = Constants.GETIMAGE_URL + model.getInt("bid") + ".png";
                Picasso.with(context).load(imgURL).error(R.drawable.ic_home).into(imageView);
                int flag = model.getInt("claim");
                if (flag == 0)
                    imgIcon.setColorFilter(Color.rgb(0, 0, 0));
                else if (flag == 1)
                    imgIcon.setColorFilter(Color.rgb(255, 0, 0));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }
}
