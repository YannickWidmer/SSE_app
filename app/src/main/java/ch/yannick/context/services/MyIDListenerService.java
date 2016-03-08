package ch.yannick.context.services;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Yannick on 06.03.2016.
 */
public class MyIDListenerService  extends InstanceIDListenerService {

    private static final String LOG = "MyInstanceIDLS";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        Intent intent = new Intent(this, MyRegistrationIntentService.class);
        startService(intent);
    }
    // [END refresh_token]
}