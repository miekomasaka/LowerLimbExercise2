package com.example.lowerlimbexercise;

import java.util.List;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

/**
 * Room(SQLite)用データベース操作インターフェース
 */
@Dao
public interface AccountDao {

    /**
     * 指定した期間のデータを取得
     * @param startDate 表示開始日
     * @param lastDate  表示終了日
     * @return 家計簿データリスト
     */
    @Query("SELECT * FROM ancleex_data WHERE starttime > :startDate AND starttime < :lastDate ORDER BY starttime ASC")
   // @Query("SELECT * FROM ancleex_data where date =(select max(date) from ancleex_data)")
    List<AncleexData> getData( long startDate,long lastDate);

    /**
     * データを追加
     * @param ad 追加データ
     */
    @Insert
    void insert(AncleexData ad);

    /**
     * データを更新
     * @param ad 更新データ
     */
    @Update
    void update(AncleexData ad);

    /**
     * データを削除
     * @param ad 削除データ
     */
    @Delete
    void delete(AncleexData ad);
}
