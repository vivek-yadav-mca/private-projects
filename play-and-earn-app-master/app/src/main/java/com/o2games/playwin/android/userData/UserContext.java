package com.o2games.playwin.android.userData;

import com.o2games.playwin.android.model.User;

public class UserContext {

    //    Context context;
//    private static Map<String, AllGameInOne> allGameInOneMap = new HashMap<>();
//    private static DBHelper dbHelper;
//    private static SharedPreferences readSPref_userData; // Not in use

    //    private static Boolean isFirstTimeUser = false;
    private static Boolean googleForceUpdate = false;
    private static User loggedInUser = null;
    private static Long nextWinnerAnnouncementTime = null;
    private static Boolean isDailyWinnerLeaderboard = false;
    private static Boolean isWeeklyWinnerLeaderboard = false;
    private static String userAdverId = null;
    private static Boolean autoCleanLeaderboard = false;
    private static long clean_leaderboard_interval = 48;
    public static Long leaderboardDeletion_futureTime = null;

    private static Boolean checkFraudUser = true;
    public static Long userCoinBalance = null;
    private static Boolean isPaypalPaymentEnabled = false;
    private static Boolean isAmazonPaymentEnabled = false;
    private static Boolean isUSD_1_Disable = false;

    public static Integer maxInGameCoins = null;
    public static Integer minInGameCoins = null;
    public static Integer maxGameQuizCoins = null;
    public static Integer minGameQuizCoins = null;

//    private static String upstoxReferralLink = null;
//    private static String gamezopLink = null;
//    private static String qurekaLink = null;
//    private static Boolean isCPALeadOfferwallEnabled = false;
//    private static String cpaLeadOfferwallLink = null;
//    private static String uniqueUserCpaLeadOfferwallLink = null;
//
//    private static Boolean isAdmobEnabled = false;
//    private static Boolean isAdmobBanner = false;
//    private static Boolean isAdmobInterstitial = false;
//    private static Boolean isAdmobRewarded = false;
//
//    private static Boolean isTapdaqEnabled = false;
//    private static Boolean isTapdaqBanner = false;
//    private static Boolean isTapdaqInterstitial = false;
//    private static Boolean isTapdaqRewarded = false;
//    private static Boolean isTapdaqNative = false;

    public static Boolean isTapjoy_enabled = false;
    public static Boolean isTapjoy_offerwall = false;

    public static Boolean isAppLovin_enabled = false;
    public static Boolean isAppLovin_banner = false;
    public static Boolean isAppLovin_interstitial = false;
    public static Boolean isAppLovin_rewarded = false;
    public static Boolean isAppLovin_mediumNative = false;
    public static Boolean isAppLovin_manualNative = false;

    public static Boolean isAppodeal_enabled = false;
    public static Boolean isAppodeal_banner = false;
    public static Boolean isAppodeal_interstitial = false;
    public static Boolean isAppodeal_rewarded = false;
    public static Boolean isAppodeal_native = false;

//    public static Boolean isUnity_enabled = false;
//    //    public static Boolean isUnity_banner = false;
//    public static Boolean isUnity_interstitial = false;
//    public static Boolean isUnity_rewarded = false;
//
//    private static Boolean isIronSourceEnabled = false;
//    private static Boolean isIronSourceOfferwall = false;

    private UserContext() {
    }

    public static Boolean getGoogleForceUpdate() {
        return googleForceUpdate;
    }

    public static void setGoogleForceUpdate(Boolean googleForceUpdate) {
        UserContext.googleForceUpdate = googleForceUpdate;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User loggedInUser) {
        UserContext.loggedInUser = loggedInUser;
    }

    public static Long getNextWinnerAnnouncementTime() {
        return nextWinnerAnnouncementTime;
    }

    public static void setNextWinnerAnnouncementTime(Long nextWinnerAnnouncementTime) {
        UserContext.nextWinnerAnnouncementTime = nextWinnerAnnouncementTime;
    }

    public static Boolean getIsDailyWinnerLeaderboard() {
        return isDailyWinnerLeaderboard;
    }

    public static void setIsDailyWinnerLeaderboard(Boolean isDailyWinnerLeaderboard) {
        UserContext.isDailyWinnerLeaderboard = isDailyWinnerLeaderboard;
    }

    public static Boolean getIsWeeklyWinnerLeaderboard() {
        return isWeeklyWinnerLeaderboard;
    }

    public static void setIsWeeklyWinnerLeaderboard(Boolean isWeeklyWinnerLeaderboard) {
        UserContext.isWeeklyWinnerLeaderboard = isWeeklyWinnerLeaderboard;
    }

    public static String getUserAdverId() {
        return userAdverId;
    }

    public static void setUserAdverId(String userAdverId) {
        UserContext.userAdverId = userAdverId;
    }

    public static Boolean getAutoCleanLeaderboard() {
        return autoCleanLeaderboard;
    }

    public static void setAutoCleanLeaderboard(Boolean autoCleanLeaderboard) {
        UserContext.autoCleanLeaderboard = autoCleanLeaderboard;
    }

    public static long getClean_leaderboard_interval() {
        return clean_leaderboard_interval;
    }

    public static void setClean_leaderboard_interval(long clean_leaderboard_interval) {
        UserContext.clean_leaderboard_interval = clean_leaderboard_interval;
    }

    public static Long getLeaderboardDeletion_futureTime() {
        return leaderboardDeletion_futureTime;
    }

    public static void setLeaderboardDeletion_futureTime(Long leaderboardDeletion_futureTime) {
        UserContext.leaderboardDeletion_futureTime = leaderboardDeletion_futureTime;
    }



    public static Boolean getCheckFraudUser() {
        return checkFraudUser;
    }

    public static void setCheckFraudUser(Boolean checkFraudUser) {
        UserContext.checkFraudUser = checkFraudUser;
    }

    public static Long getUserCoinBalance() {
        return userCoinBalance;
    }

    public static void setUserCoinBalance(Long userCoinBalance) {
        UserContext.userCoinBalance = userCoinBalance;
    }

    public static Boolean getIsPaypalPaymentEnabled() {
        return isPaypalPaymentEnabled;
    }

    public static void setIsPaypalPaymentEnabled(Boolean isPaypalPaymentEnabled) {
        UserContext.isPaypalPaymentEnabled = isPaypalPaymentEnabled;
    }

    public static Boolean getIsAmazonPaymentEnabled() {
        return isAmazonPaymentEnabled;
    }

    public static void setIsAmazonPaymentEnabled(Boolean isAmazonPaymentEnabled) {
        UserContext.isAmazonPaymentEnabled = isAmazonPaymentEnabled;
    }

    public static Boolean getIsUSD_1_Disable() {
        return isUSD_1_Disable;
    }

    public static void setIsUSD_1_Disable(Boolean isUSD_1_Disable) {
        UserContext.isUSD_1_Disable = isUSD_1_Disable;
    }

    public static Integer getMaxInGameCoins() {
        return maxInGameCoins;
    }

    public static void setMaxInGameCoins(Integer maxInGameCoins) {
        UserContext.maxInGameCoins = maxInGameCoins;
    }

    public static Integer getMinInGameCoins() {
        return minInGameCoins;
    }

    public static void setMinInGameCoins(Integer minInGameCoins) {
        UserContext.minInGameCoins = minInGameCoins;
    }

    public static Integer getMaxGameQuizCoins() {
        return maxGameQuizCoins;
    }

    public static void setMaxGameQuizCoins(Integer maxGameQuizCoins) {
        UserContext.maxGameQuizCoins = maxGameQuizCoins;
    }

    public static Integer getMinGameQuizCoins() {
        return minGameQuizCoins;
    }

    public static void setMinGameQuizCoins(Integer minGameQuizCoins) {
        UserContext.minGameQuizCoins = minGameQuizCoins;
    }


    /**
     *
     *
     * **/


//    public static String getUpstoxReferralLink() {
//        return upstoxReferralLink;
//    }
//
//    public static void setUpstoxReferralLink(String upstoxReferralLink) {
//        UserContext.upstoxReferralLink = upstoxReferralLink;
//    }
//
//    public static String getGamezopLink() {
//        return gamezopLink;
//    }
//
//    public static void setGamezopLink(String gamezopLink) {
//        UserContext.gamezopLink = gamezopLink;
//    }
//
//    public static String getQurekaLink() {
//        return qurekaLink;
//    }
//
//    public static void setQurekaLink(String qurekaLink) {
//        UserContext.qurekaLink = qurekaLink;
//    }
//
//    public static Boolean getIsCPALeadOfferwallEnabled() {
//        return isCPALeadOfferwallEnabled;
//    }
//
//    public static void setIsCPALeadOfferwallEnabled(Boolean isCPALeadOfferwallEnabled) {
//        UserContext.isCPALeadOfferwallEnabled = isCPALeadOfferwallEnabled;
//    }
//
//    public static String getCpaLeadOfferwallLink() {
//        return cpaLeadOfferwallLink;
//    }
//
//    public static void setCpaLeadOfferwallLink(String cpaLeadOfferwallLink) {
//        UserContext.cpaLeadOfferwallLink = cpaLeadOfferwallLink;
//    }
//
//    public static String getUniqueUserCpaLeadOfferwallLink() {
//        return uniqueUserCpaLeadOfferwallLink;
//    }
//
//    public static void setUniqueUserCpaLeadOfferwallLink(String uniqueUserCpaLeadOfferwallLink) {
//        UserContext.uniqueUserCpaLeadOfferwallLink = uniqueUserCpaLeadOfferwallLink;
//    }

    /**
     *
     *
     * **/

//    public static DBHelper getDbHelper() {
//        return dbHelper;
//    }
//
//    public static void setDbHelper(DBHelper dbHelper) {
//        UserContext.dbHelper = dbHelper;
//    }

//    public static SharedPreferences getReadSPref_userData() {
//        return readSPref_userData;
//    }
//
//    public static void setReadSPref_userData(SharedPreferences readSPref_userData) {
//        UserContext.readSPref_userData = readSPref_userData;
//    }


//    public static Map<String, AllGameInOne> getAllGameInOneMap() {
//        return allGameInOneMap;
//    }
//
//    public static void setAllGameInOneMap(Map<String, AllGameInOne> allGameInOneMap) {
//        UserContext.allGameInOneMap = allGameInOneMap;
//    }

    /**
     * To update allGameInOne data by key gameId
     *
     * @param allGameInOne
     */
//    public static void updateGameDataById(AllGameInOne allGameInOne) {
//        allGameInOneMap.put(allGameInOne.getGameId(), allGameInOne);
//    }

    /**
     * To get game data by gameId
     *
     * @param gameId
     * @return
     */
//    public static AllGameInOne getAllGameInOneMapByGameId(String gameId) {
//        if(allGameInOneMap != null && allGameInOneMap.containsKey(gameId)) {
//            return allGameInOneMap.get(gameId);
//        } else {
//            if (loggedInUser != null) {
//                return dbHelper.getFreeAdGameDataByUserIdAndGameId(loggedInUser.getId(), gameId);
//            }
//        }
//        return dbHelper.getFreeAdGameDataByUserIdAndGameId(loggedInUser.getId(), gameId);
//    }

//    public static void setAllGameInOneMapByGameIdAndData(String gameId, AllGameInOne allGameInOne) {
//        if (allGameInOneMap != null) {
//            allGameInOneMap.put(gameId, allGameInOne);
//        }
//    }

//    private SpUserModel getUserData_sPref() {
////        SharedPreferences readSPref_forUserData = getSharedPreferences(Constants.SHARED_PREF_COMMON, Context.MODE_PRIVATE);
////        SharedPreferences readSPref_forUserData = getReadSPref_userData(Constants.SHARED_PREF_COMMON, Context.MODE_PRIVATE);
//
//        String userId = readSPref_userData.getString(Constants.SP_USER_ID, UserContext.getLoggedInUser().getId());
//        String userName = readSPref_userData.getString(Constants.SP_USER_NAME, UserContext.getLoggedInUser().getUserName());
//        String userEmail = readSPref_userData.getString(Constants.SP_USER_EMAIL, UserContext.getLoggedInUser().getUserEmail());
//        String userGoogleId = readSPref_userData.getString(Constants.SP_USER_GOOGLE_ID, UserContext.getLoggedInUser().getUserGoogleAccountId());
//        String userPhotoUrl = readSPref_userData.getString(Constants.SP_USER_PHOTO_URL, UserContext.getLoggedInUser().getUserPhotoUrl());
//        String userAuthId = readSPref_userData.getString(Constants.SP_USER_AUTH_UID, UserContext.getLoggedInUser().getAuthUid());
//        String userAdverId = readSPref_userData.getString(Constants.SP_USER_ADVER_ID, UserContext.getUserAdverId());
//
//        SpUserModel spUserModel = new SpUserModel(userId, userName, userEmail, userGoogleId, userPhotoUrl, userAuthId, userAdverId);
//        return spUserModel;
//    }

    /**
     *
     *
     * **/

//    public static Boolean getIsAdmobEnabled() {
//        return isAdmobEnabled;
//    }
//
//    public static void setIsAdmobEnabled(Boolean isAdmobEnabled) {
//        UserContext.isAdmobEnabled = isAdmobEnabled;
//    }
//
//    public static Boolean getIsAdmobBanner() {
//        return isAdmobBanner;
//    }
//
//    public static void setIsAdmobBanner(Boolean isAdmobBanner) {
//        UserContext.isAdmobBanner = isAdmobBanner;
//    }
//
//    public static Boolean getIsAdmobInterstitial() {
//        return isAdmobInterstitial;
//    }
//
//    public static void setIsAdmobInterstitial(Boolean isAdmobInterstitial) {
//        UserContext.isAdmobInterstitial = isAdmobInterstitial;
//    }
//
//    public static Boolean getIsAdmobRewarded() {
//        return isAdmobRewarded;
//    }
//
//    public static void setIsAdmobRewarded(Boolean isAdmobRewarded) {
//        UserContext.isAdmobRewarded = isAdmobRewarded;
//    }


    /**
     *
     * * * * *
     *
     * **/

//    public static Boolean getIsTapdaqEnabled() {
//        return isTapdaqEnabled;
//    }
//
//    public static void setIsTapdaqEnabled(Boolean isTapdaqEnabled) {
//        UserContext.isTapdaqEnabled = isTapdaqEnabled;
//    }
//
//    public static Boolean getIsTapdaqBanner() {
//        return isTapdaqBanner;
//    }
//
//    public static void setIsTapdaqBanner(Boolean isTapdaqBanner) {
//        UserContext.isTapdaqBanner = isTapdaqBanner;
//    }
//
//    public static Boolean getIsTapdaqInterstitial() {
//        return isTapdaqInterstitial;
//    }
//
//    public static void setIsTapdaqInterstitial(Boolean isTapdaqInterstitial) {
//        UserContext.isTapdaqInterstitial = isTapdaqInterstitial;
//    }
//
//    public static Boolean getIsTapdaqRewarded() {
//        return isTapdaqRewarded;
//    }
//
//    public static void setIsTapdaqRewarded(Boolean isTapdaqRewarded) {
//        UserContext.isTapdaqRewarded = isTapdaqRewarded;
//    }
//
//    public static Boolean getIsTapdaqNative() {
//        return isTapdaqNative;
//    }
//
//    public static void setIsTapdaqNative(Boolean isTapdaqNative) {
//        UserContext.isTapdaqNative = isTapdaqNative;
//    }


    /**
     *
     * * * * *
     *
     * **/

    public static Boolean getIsTapjoy_enabled() {
        return isTapjoy_enabled;
    }

    public static void setIsTapjoy_enabled(Boolean isTapjoy_enabled) {
        UserContext.isTapjoy_enabled = isTapjoy_enabled;
    }

    public static Boolean getIsTapjoy_offerwall() {
        return isTapjoy_offerwall;
    }

    public static void setIsTapjoy_offerwall(Boolean isTapjoy_offerwall) {
        UserContext.isTapjoy_offerwall = isTapjoy_offerwall;
    }

    /**
     *
     * * * * *
     *
     * **/

    public static Boolean getIsAppLovin_enabled() {
        return isAppLovin_enabled;
    }

    public static void setIsAppLovin_enabled(Boolean isAppLovin_enabled) {
        UserContext.isAppLovin_enabled = isAppLovin_enabled;
    }

    public static Boolean getIsAppLovin_banner() {
        return isAppLovin_banner;
    }

    public static void setIsAppLovin_banner(Boolean isAppLovin_banner) {
        UserContext.isAppLovin_banner = isAppLovin_banner;
    }

    public static Boolean getIsAppLovin_interstitial() {
        return isAppLovin_interstitial;
    }

    public static void setIsAppLovin_interstitial(Boolean isAppLovin_interstitial) {
        UserContext.isAppLovin_interstitial = isAppLovin_interstitial;
    }

    public static Boolean getIsAppLovin_rewarded() {
        return isAppLovin_rewarded;
    }

    public static void setIsAppLovin_rewarded(Boolean isAppLovin_rewarded) {
        UserContext.isAppLovin_rewarded = isAppLovin_rewarded;
    }

    public static Boolean getIsAppLovin_mediumNative() {
        return isAppLovin_mediumNative;
    }

    public static void setIsAppLovin_mediumNative(Boolean isAppLovin_mediumNative) {
        UserContext.isAppLovin_mediumNative = isAppLovin_mediumNative;
    }

    public static Boolean getIsAppLovin_manualNative() {
        return isAppLovin_manualNative;
    }

    public static void setIsAppLovin_manualNative(Boolean isAppLovin_manualNative) {
        UserContext.isAppLovin_manualNative = isAppLovin_manualNative;
    }

    /**
     *
     * * * * *
     *
     * **/

    public static Boolean getIsAppodeal_enabled() {
        return isAppodeal_enabled;
    }

    public static void setIsAppodeal_enabled(Boolean isAppodeal_enabled) {
        UserContext.isAppodeal_enabled = isAppodeal_enabled;
    }

    public static Boolean getIsAppodeal_banner() {
        return isAppodeal_banner;
    }

    public static void setIsAppodeal_banner(Boolean isAppodeal_banner) {
        UserContext.isAppodeal_banner = isAppodeal_banner;
    }

    public static Boolean getIsAppodeal_interstitial() {
        return isAppodeal_interstitial;
    }

    public static void setIsAppodeal_interstitial(Boolean isAppodeal_interstitial) {
        UserContext.isAppodeal_interstitial = isAppodeal_interstitial;
    }

    public static Boolean getIsAppodeal_rewarded() {
        return isAppodeal_rewarded;
    }

    public static void setIsAppodeal_rewarded(Boolean isAppodeal_rewarded) {
        UserContext.isAppodeal_rewarded = isAppodeal_rewarded;
    }

    public static Boolean getIsAppodeal_native() {
        return isAppodeal_native;
    }

    public static void setIsAppodeal_native(Boolean isAppodeal_native) {
        UserContext.isAppodeal_native = isAppodeal_native;
    }

    /**
     *
     * * * * *
     *
     * **/

//    public static Boolean getIsUnity_enabled() {
//        return isUnity_enabled;
//    }
//
//    public static void setIsUnity_enabled(Boolean isUnity_enabled) {
//        UserContext.isUnity_enabled = isUnity_enabled;
//    }
//
////    public static Boolean getIsUnity_banner() {
////        return isUnity_banner;
////    }
////
////    public static void setIsUnity_banner(Boolean isUnity_banner) {
////        UserContext.isUnity_banner = isUnity_banner;
////    }
//
//    public static Boolean getIsUnity_interstitial() {
//        return isUnity_interstitial;
//    }
//
//    public static void setIsUnity_interstitial(Boolean isUnity_interstitial) {
//        UserContext.isUnity_interstitial = isUnity_interstitial;
//    }
//
//    public static Boolean getIsUnity_rewarded() {
//        return isUnity_rewarded;
//    }
//
//    public static void setIsUnity_rewarded(Boolean isUnity_rewarded) {
//        UserContext.isUnity_rewarded = isUnity_rewarded;
//    }
//
//    /**
//     *
//     * * * * *
//     *
//     * **/
//
//
//    public static Boolean getIsIronSourceEnabled() {
//        return isIronSourceEnabled;
//    }
//
//    public static void setIsIronSourceEnabled(Boolean isIronSourceEnabled) {
//        UserContext.isIronSourceEnabled = isIronSourceEnabled;
//    }
//
//    public static Boolean getIsIronSourceOfferwall() {
//        return isIronSourceOfferwall;
//    }
//
//    public static void setIsIronSourceOfferwall(Boolean isIronSourceOfferwall) {
//        UserContext.isIronSourceOfferwall = isIronSourceOfferwall;
//    }

}