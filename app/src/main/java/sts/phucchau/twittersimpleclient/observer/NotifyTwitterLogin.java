package sts.phucchau.twittersimpleclient.observer;

import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

public interface NotifyTwitterLogin {

    void notifyTwitterLogin(TwitterSession twitterSession, TwitterAuthToken twitterAuthToken, TwitterAuthClient twitterAuthClient);
}
