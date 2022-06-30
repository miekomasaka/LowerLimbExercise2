package com.example.lowerlimbexercise;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Room DataBaseクラス
 */
@Database(entities = {AncleexData.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    //DAO用メソッド
    public abstract AccountDao accountDao();
}
