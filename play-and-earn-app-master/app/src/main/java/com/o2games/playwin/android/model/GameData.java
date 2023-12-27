package com.o2games.playwin.android.model;

public class GameData {

    String userId;
    String gameId;
//    Long totalCoins;
//    int totalCoins;
    String chancesLeft;
    String totalCoins;

    public GameData() {
    }

    public GameData(String userId, String gameId, String chancesLeft, String totalCoins) {
        this.userId = userId;
        this.gameId = gameId;
        this.chancesLeft = chancesLeft;
        this.totalCoins = totalCoins;
    }

    public GameData(String userId, String totalCoins) {
        this.userId = userId;
        this.totalCoins = totalCoins;
    }


    //    public GameData(String userId, String gameId, String chancesLeft) {
//        this.userId = userId;
//        this.gameId = gameId;
//        this.chancesLeft = chancesLeft;
//    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getChancesLeft() {
        return chancesLeft;
    }

    public void setChancesLeft(String chancesLeft) {
        this.chancesLeft = chancesLeft;
    }

    public String getTotalCoins() {
        return totalCoins;
    }

    public void setTotalCoins(String totalCoins) {
        this.totalCoins = totalCoins;
    }

//    public HashMap<String, Object> updateChanceLeft() {
//        HashMap<String, Object> totalCoins = new HashMap<>();
//        totalCoins.put("totalCoins", getTotalCoins());
//
//        return totalCoins;
//    }

}
