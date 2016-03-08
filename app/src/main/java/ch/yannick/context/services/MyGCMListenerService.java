package ch.yannick.context.services;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by Yannick on 06.03.2016.
 */
public class MyGCMListenerService extends GcmListenerService {
    private static final String LOG = "GcmListenerService";

    @Override
    public void onMessageReceived(String from,Bundle data){
        String message = data.getString("message");
        Log.d(LOG, "From: " + from);
        Log.d(LOG, "Message: " + message);

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

    }
}
