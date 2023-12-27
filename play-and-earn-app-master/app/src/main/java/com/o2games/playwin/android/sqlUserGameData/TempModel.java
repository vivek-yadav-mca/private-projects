package com.o2games.playwin.android.sqlUserGameData;

import android.text.TextUtils;

public class TempModel {

    String chancesLeft;
    String totalCoins;

    public TempModel() {}

    public TempModel(String chanceLeft, String coins) {
        if(!TextUtils.isEmpty(chanceLeft)) {
            this.chancesLeft = chanceLeft;
        }
        if(!TextUtils.isEmpty(coins)) {
            this.totalCoins = coins;
        }
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
}
