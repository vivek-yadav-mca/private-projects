package com.zero07.rssb.userModels;

import android.content.Context;

import java.util.Map;

public class UserService {

//    public static void loadUserData(Context context, String userId, User userResponse) {
//        User loggedInUser = UserRepo.getInstance(context).getUserData(userId);
//        if (loggedInUser == null) {
//            loggedInUser = uploadUserDataToDB(context, userResponse);
//        }
//        UserContext.setLoggedInUser(loggedInUser);
//    }

//    public static User uploadUserDataToDB(Context context, User userResponse) {
//        User user = null;
//        if (userResponse != null) {
//            user = new User(userResponse.getId(), userResponse.getUserName(), userResponse.getUserEmail(), userResponse.getUserGoogleAccountId(), userResponse.getUserPhotoUrl());
//            UserRepo.getInstance(context).insertUser(user);
//        }
//        return user;
//    }

//    public static void updateUserDataToFirebase(Context context) {
//        Map<String, User> allUserData = UserRepo.getInstance(context).getAllUserData();
//        for (Map.Entry<String, User> entry : allUserData.entrySet()) {
//            User user = entry.getValue();
//            FirebaseDataService.updateUserDataToFirebase(user);
//        }
//    }
}