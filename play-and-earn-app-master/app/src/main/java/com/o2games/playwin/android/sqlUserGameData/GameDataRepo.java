package com.o2games.playwin.android.sqlUserGameData;

public class GameDataRepo {

//    private static GameDataRepo instance;
//    private DBHelper database;
//
//    private GameDataRepo(Context context) {
//        database = DBHelper.getInstance(context);
//    }
//
//    public static GameDataRepo getInstance(Context context) {
//        if (instance == null) {
//            instance = new GameDataRepo(context);
//        }
//        return instance;
//    }
//
//    public Boolean insertUserGameData(GameData gameData) {
//        SQLiteDatabase db = database.getWritableDatabase();
//        Cursor cursor = db.rawQuery("Select * from user where id = ?", new String[]{gameData.getUserId()});
//        if (cursor.getCount() <= 0) {
//            ContentValues contentValues = new ContentValues();
//            contentValues.put("user_id", gameData.getUserId());
//            contentValues.put("game_id", gameData.getGameId());
//            contentValues.put("total_coin", gameData.getTotalCoins());
//            contentValues.put("chances_left", gameData.getChancesLeft());
//
//            long result = db.insert(Constants.GAME_DATA_TABLE, null, contentValues);
//            if (result == -1) {
//                return false;
//            } else {
//                return true;
//            }
//        }
//        return false;
//    }

//    public Boolean updateTotalCoins(String userId, Long totalCoin) {
//        SQLiteDatabase DB = database.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("total_coin", totalCoin);
//        Cursor cursor = DB.rawQuery("Select * from user where id = ?", new String[]{userId});
//        if (cursor.getCount() > 0) {
//            long result = DB.update(Constants.GAME_DATA_TABLE, contentValues, "id=?", new String[]{userId});
//            if (result == -1) {
//                return false;
//            } else {
//                return true;
//            }
//        } else {
//            return false;
//        }
//    }


}
