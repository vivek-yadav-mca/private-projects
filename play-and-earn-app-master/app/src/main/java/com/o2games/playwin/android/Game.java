package com.o2games.playwin.android;

public enum Game {

    NORMAL_SPIN("normal_spin"),
    NORMAL_SCRATCH("normal_scratch"),
    NORMAL_FLIP("normal_flip"),
//    GOLDEN_SPIN("golden_spin"),
//    GOLDEN_SCRATCH("golden_scratch"),
//    GOLDEN_FLIP("golden_flip"),
    TOTAL_CASH_COINS("total_coins");
//    TOTAL_CASH("total_cash");
//    OFFERWALL_COINS("offerwall_coins");

    String id;

    Game(String id) {
        this.id = id;
    }

    public static Game getInstance(String name) {
        for (Game game : Game.values()) {
            if (game.getId().equalsIgnoreCase(name)) {
                return game;
            }
        }
        return null;
    }

    public String getId() {
        return id;
    }
}
