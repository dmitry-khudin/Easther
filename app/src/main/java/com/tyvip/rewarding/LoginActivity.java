package com.tyvip.rewarding;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.tyvip.rewarding.Utils.Constants;
import com.tyvip.rewarding.Utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    String phonenumber, password;
    EditText editText1, editText2;
    final String tag_json_obj = "json_obj_req";
    SwitchCompat switchCompat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initActionBar();
    }

    public void OnLogIn(View view)
    {
        if (!validate()) return;
        OnLogin();
    }
    RelativeLayout signinlayout;
    private void initActionBar() {
        signinlayout = (RelativeLayout) findViewById(R.id.signin);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("MY ACCOUNT");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        editText1 = (EditText) findViewById(R.id.editText2);
        editText2 = (EditText) findViewById(R.id.editText3);
        editText1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO ||
                        actionId == EditorInfo.IME_ACTION_NEXT)
                {
                    if (validate()) signinlayout.setEnabled(true);
                    else signinlayout.setEnabled(false);
                }
                return false;
            }
        });
        editText1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b)
                {
                    if (validate()) signinlayout.setEnabled(true);
                    else signinlayout.setEnabled(false);
                }
            }
        });
        editText2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO ||
                        actionId == EditorInfo.IME_ACTION_NEXT)
                {
                    if (validate()) signinlayout.setEnabled(true);
                    else signinlayout.setEnabled(false);
                }
                return false;
            }
        });
        editText2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b)
                {
                    if (validate()) signinlayout.setEnabled(true);
                    else signinlayout.setEnabled(false);
                }
            }
        });
        String val = Util.GetStringFromReference(this, Constants.LOGINED);

        switchCompat = (SwitchCompat) findViewById(R.id.switch1);
        if (val.equals("0"))
            switchCompat.setChecked(false);
        else
            switchCompat.setChecked(true);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    Util.SaveStringToReference(LoginActivity.this, "1", Constants.LOGINED);
                }
                else
                {
                    Util.SaveStringToReference(LoginActivity.this, "0", Constants.LOGINED);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(LoginActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    boolean validate()
    {
        phonenumber = editText1.getText().toString();
        password = editText2.getText().toString();
        if (phonenumber.equals(""))
        {
            editText1.setError("Phonenumber is empty!");
            return false;
        }
        if (password.equals(""))
        {
            editText2.setError("Password is empty!");
            return false;
        }
        return true;
    }

    private void PostFCM()
    {
        String str = Util.GetStringFromReference(this, Constants.USER_DATA);
        String url = Constants.FCM_URL;
        String fcmkey = Util.GetStringFromReference(this, Constants.TOKEN);

        if (fcmkey.equals(""))
        {
            fcmkey = FirebaseInstanceId.getInstance().getToken();
            Util.SaveStringToReference(this, fcmkey, Constants.TOKEN);
        }
        Log.d("fcm", fcmkey);
        JSONObject jsonObject = new JSONObject();
        try {
            JSONObject jsonUser = new JSONObject(str);
            jsonObject.put("userid", jsonUser.getInt("userid"));
            jsonObject.put("userbid", jsonUser.getInt("userbid"));
            jsonObject.put("mobile", phonenumber);
            jsonObject.put("regid", fcmkey);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(tag_json_obj, response.toString());
                        pDialog.dismiss();
                        if (switchCompat.isChecked())
                            Util.SaveStringToReference(LoginActivity.this, "1", Constants.LOGINED);
                        else Util.SaveStringToReference(LoginActivity.this, "0", Constants.LOGINED);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(tag_json_obj, "ErrorLocation: " + error.getMessage());
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "FCM error!", Toast.LENGTH_SHORT).show();
            }
        }) ;
        VipApplication.getInstance().addToRequestQueue(jsonObjReq, "FCM");
    }
    private void PostLocation()
    {
        String str = Util.GetStringFromReference(this, Constants.USER_DATA);
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
                        PostFCM();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                VolleyLog.d(tag_json_obj, "ErrorLocation: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Sign In error!", Toast.LENGTH_SHORT).show();
            }
        }) ;
        VipApplication.getInstance().addToRequestQueue(jsonObjReq, "location");
    }
    ProgressDialog pDialog;
    public void OnLogin()  {


        String url = Constants.SIGNIN_URL;

        if (!Util.isConnection(this))
        {
            Toast.makeText(getApplicationContext(), "Network is offline!", Toast.LENGTH_SHORT).show();
            return;
        }
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.PHONE, phonenumber);
        params.put(Constants.PASS, password);
        JSONObject param = new JSONObject();
        try {
            param.put(Constants.PHONE, phonenumber);
            param.put(Constants.PASS, password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, param,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(tag_json_obj, response.toString());
                        Util.SaveStringToReference(LoginActivity.this, response.toString(), Constants.USER_DATA);
                        Util.SaveStringToReference(LoginActivity.this, phonenumber, Constants.PHONE);
                        Util.SaveStringToReference(LoginActivity.this, password, Constants.PASS);
                        PostLocation();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(tag_json_obj, "ErrorLogin: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "You enterned wrong number or password!", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        }) ;

// Adding request to request queue
        VipApplication.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }



    public void OnJoin(View view)
    {
        Intent intent = new Intent(this, WebViewActivity.class);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.JOIN_URL));
        startActivity(browserIntent);
//        intent.putExtra("url", Constants.JOIN_URL);
//        intent.putExtra("title", Constants.TITLE_JOIN);
//        startActivity(intent);
    }
    public void OnRetrieve(View view)
    {
//        Intent intent = new Intent(this, WebViewActivity.class);
//        intent.putExtra("url", Constants.RETRIVE_URL);
//        intent.putExtra("title", Constants.TITLE_RETRIVE_PASS);
//        startActivity(intent);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.RETRIVE_URL));
        startActivity(browserIntent);
    }

    public void OnAppPolicy(View view)
    {
//        Intent intent = new Intent(this, WebViewActivity.class);
//        intent.putExtra("url", Constants.POLICY_URL);
//        intent.putExtra("title", Constants.TITLE_APP_POLICY);
//        startActivity(intent);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.POLICY_URL));
        startActivity(browserIntent);
    }


}
