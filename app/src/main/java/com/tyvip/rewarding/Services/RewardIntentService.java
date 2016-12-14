package com.tyvip.rewarding.Services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.tyvip.rewarding.Utils.Constants;
import com.tyvip.rewarding.Utils.Util;

/**
 * Created by bryden on 12/10/16.
 */

public class RewardIntentService extends FirebaseInstanceIdService {
    private static final String TAG = "OneBabyIDService";


    @Override
    public void onTokenRefresh() {
//        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token)
    {
//        Util.SetStringData(getApplicationContext(), Constants.USER_TOKEN, token);
        Util.SaveStringToReference(getApplicationContext(), token, Constants.TOKEN);
    }
}