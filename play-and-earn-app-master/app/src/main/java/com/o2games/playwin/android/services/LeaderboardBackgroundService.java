package com.o2games.playwin.android.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.o2games.playwin.android.Constants;
import com.o2games.playwin.android.FirebaseDataService;
import com.o2games.playwin.android.Game;
import com.o2games.playwin.android.Utils;
import com.o2games.playwin.android.fragment.LeaderboardFragment;
import com.o2games.playwin.android.model.LeaderboardModel;
import com.o2games.playwin.android.userData.UserContext;

import java.util.Timer;
import java.util.TimerTask;

public class LeaderboardBackgroundService extends Service {

    private static final String TAG = LeaderboardFragment.class.getName();
    private final FirebaseDataService firebaseDataService = new FirebaseDataService(this);
    public static final String sqlTotal_CashCoinsCOL = Game.TOTAL_CASH_COINS.getId();
    DatabaseReference databaseRef;

    FirebaseRemoteConfig remoteConfig;
    public static final String REFRESH_TIME = "leaderboard_refresh_time";

//    long remoteConfig_refresh_time;

    public static final int notify = 300000;  //interval between two services(Here Service run every 5 Minute)
    private Handler mHandler = new android.os.Handler();   //run on another Thread to avoid crash
    private Timer mTimer = null;    //timer handling

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        databaseRef = FirebaseDatabase.getInstance().getReference();
        initializeRemoteCongfig();

//        if (mTimer != null) // Cancel if already existed
//            mTimer.cancel();
//        else
//            mTimer = new Timer();   //recreate new
//        mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, remoteConfig_refresh_time);   //Schedule task
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();    //For Cancel Timer
//        Toast.makeText(this, "Service is Destroyed", Toast.LENGTH_SHORT).show();
    }

    //class TimeDisplay for handling task
    class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {
//                    Toast.makeText(LeaderboardBackgroundService.this, "Service is running", Toast.LENGTH_SHORT).show();
                    refreshLeaderboardData();
                }
            });
        }
    }

    private void refreshLeaderboardData() {
        String userId = UserContext.getLoggedInUser().getId();
        String userName = UserContext.getLoggedInUser().getUserName();
        String userPhotoUrl = UserContext.getLoggedInUser().getUserPhotoUrl();

        String sql_total_COIN = firebaseDataService.getCoinBalance();
//        String sql_total_COIN = UserContext.getAllGameInOneMapByGameId(this, sql_Total_CashCoinsCOL).getCoins();
        long userTotal_COIN = Long.parseLong(sql_total_COIN);

//        String sqlTotal_Cash = UserContext.getAllGameInOneMapByGameId(this, sqlTotal_CashCoinsCOL).getCash();
//        long userTotal_CASH = Long.parseLong(sqlTotal_Cash);

//        String sql_totalCoins = UserContext.getAllGameInOneMapByGameId(sqlTotal_CashCoinsCOL).getCoins();
//        Map<String, AllGameInOne> allGameInOneMap = UserContext.getAllGameInOneMap();
//        if (allGameInOneMap != null) {
//            userTotal_COIN = (allGameInOneMap.get(sqlTotal_CashCoinsCOL) != null) ? allGameInOneMap.get(sqlTotal_CashCoinsCOL).getCoins() : null;
//        }

        String adverID = UserContext.getUserAdverId();
        String ipv4 = Utils.getIPAddress(true); // IPv4
        Utils.getIPAddress(false); // IPv6

        String authUid = UserContext.getLoggedInUser().getAuthUid();
        LeaderboardModel modelData = new LeaderboardModel(userId, authUid, userName, userPhotoUrl, userTotal_COIN);

        databaseRef
                .child(Constants.LEADERBOARD_TABLE)
//                .child(Constants.WALLET_BALANCE)
                .child(UserContext.getLoggedInUser().getId())
                .setValue(modelData);

    }

    private void initializeRemoteCongfig() {
        FirebaseRemoteConfigSettings remoteConfigSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0).build();
        remoteConfig = FirebaseRemoteConfig.getInstance();

        remoteConfig.setConfigSettingsAsync(remoteConfigSettings);

        remoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "remote config is fetched.");
                    long remoteConfig_refresh_time = remoteConfig.getLong("leaderboard_refresh_time");

                    if (mTimer != null) { // Cancel if already existed
                        mTimer.cancel();
                    } else
                        mTimer = new Timer();   //recreate new
                    mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, remoteConfig_refresh_time);   //Schedule task
                } else {
                    Log.e(TAG, "Exception while remote config is fetched.");
                }
            }
        });
    }

//    private void fetchRemoteConfigData() {
//        initializeRemoteCongfif();
////        remoteConfig = FirebaseRemoteConfig.getInstance();
//
////        currentVersion = remoteConfig.getString(KEY_NEW_VERSION);
////        refreshTime = remoteConfig .getString(REFRESH_TIME);
////
////        String telegram_channel_link = remoteConfig.getString("telegram_channel_link");
////
////        remoteConfig_refresh_time = remoteConfig.getLong("leaderboard_refresh_time");
//    }

}