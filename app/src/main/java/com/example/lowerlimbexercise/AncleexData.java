package com.example.lowerlimbexercise;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * 足関節運動データクラス（AccountData）
 */
@Entity(tableName = "ancleex_data") //テーブル名を定義
public class AncleexData implements Cloneable {
    @PrimaryKey(autoGenerate = true)
    public int id;           //「id」カラムを定義
    public String content;   //「内容」カラムを定義
    public long starttime;  //運動開始時刻
    public long duration;   //継続時間
    public int timedivision;//運動時間区分　1:8-10 2:10-12 3:12-14 ...
    public int count_over_r;    //角度オーバーした回数（右）
    public int count_best_r;    //角度が良であった回数（右）
    public int count_under_r;   //角度不足だった回数（右）
    public int count_over_l;    //角度オーバーした回数（左）
    public int count_best_l;    //角度が良であった回数（左）
    public int count_under_l;   //角度不足だった回数（左）
    public String raw_data_r;   //センサ収集データファイル名（右）
    public String raw_data_l;   //センサ収集データファイル名（左）
    /**
     * コンストラクタ
     * @param content 内容
     * @param starttime    運動開始時刻
     * @param duration 継続時間
     * @param timedivision   運動時間区分
     * @param count_over_r    角度オーバーした回数
     * @param count_best_r    角度が良であった回数
     * @param  count_under_r　　角度不足だった回数（右）
     * @param count_over_l    角度オーバーした回数
     * @param count_best_l 角度が良であった回数
     * @param count_under_l   角度不足だった回数
     * @param raw_data_r    センサ収集データファイル名
     * @param raw_data_l    センサ収集データファイル名
     */
    public AncleexData(String content,  long starttime, long duration, int timedivision, int count_over_r, int count_best_r, int count_under_r,
                       int count_over_l, int count_best_l, int count_under_l, String raw_data_r, String raw_data_l) {
        this.content = content;  //「内容」を設定
        this.starttime=starttime;  //運動開始時刻
        this.duration = duration;   //継続時間
        this.timedivision = timedivision;//運動時間区分　1:8-10 2:10-12 3:12-14 ...
        this.count_over_r = count_over_r; //角度オーバーした回数（右）
        this.count_best_r = count_best_r;    //角度が良であった回数（右）
        this.count_under_r = count_under_r;   //角度不足だった回数（右）
        this.count_over_l = count_over_l;    //角度オーバーした回数（左）
        this.count_best_l = count_best_l;    //角度が良であった回数（左）
        this.count_under_l = count_under_l;   //角度不足だった回数（左）
        this.raw_data_r = raw_data_r;   //センサ収集データファイル名（右）
        this.raw_data_l = raw_data_l;   //センサ収集データファイル名（左）

    }


   /* public AncleexData( ) {
        this.content = "";  //「内容」を設定
        this.price = 0;      //「金額」を設定
        this.date = 0;        //「日付」を設定
        this.starttime=0;  //運動開始時刻
        this.duration = 0;   //継続時間
        this.timedivision = 0;//運動時間区分　1:8-10 2:10-12 3:12-14 ...
        this.count_over_r = 0; //角度オーバーした回数（右）
        this.count_best_r = 0;    //角度が良であった回数（右）
        this.count_under_r = 0;   //角度不足だった回数（右）
        this.count_over_l = 0;    //角度オーバーした回数（左）
        this.count_best_l = 0;    //角度が良であった回数（左）
        this.count_under_l = 0;   //角度不足だった回数（左）
        this.raw_data_r = "";   //センサ収集データファイル名（右）
        this.raw_data_l = "";   //センサ収集データファイル名（左）
    }*/

    /**
     * 「id」を取得（Getter）
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * 「内容」を取得（Getter）
     * @return 内容
     */
    public String getContent() {
        return content;
    }







    /**
     *　「運動開始時刻」を取得（Getter）
     * @return
     */
    public long getstarttime(){return starttime;}

    /**
     *　「継続時間」を取得（Getter）
     * @return
     */
    public long getduration(){ return duration; }

    /**
     *　「運動時間区分」を取得（Getter）
     * @return  1:8-10 2:10-12 3:12-14 ...
     */
    public int gettimedivision(){ return timedivision; }

    /**
     * 「角度オーバー回数（右）」を取得(Getter)
     * @return
     */
    public int getCountOverR(){return count_over_r; }

    /**
     *　「角度が良であった回数（右）」を取得（Getter）
     * @return
     */
    public int getCountBestR(){ return count_best_r; }

    /**
     *　「角度不足だった回数（右）」を取得（Getter）
     * @return
     */
    public int getCountUnderR(){ return count_under_r; }

    /**
     *　「角度オーバーした回数（左）」を取得（Getter）
     * @return
     */
    public int getCountOverL(){return count_over_l;}

    /**
     *　「角度が良であった回数（左）」を取得（Getter）
     * @return
     */
    public int getCountBestL(){ return count_best_l; }

    /**
     *　「角度不足だった回数（左）」を取得（Getter）
     * @return
     */
    public int getCountUnderL(){return count_under_l;}

    /**
     *　「センサ収集データファイル名（右）」を取得（Getter）
     * @return
     */
    public String getRawDataR(){ return raw_data_r; }

    /**
     *　「センサ収集データファイル名（左）」を取得（Getter）
     * @return
     */
    public String getRawDataL(){ return raw_data_l; }


    /**
     * 「足関節運動データ」を更新
     * @param content 更新する「内容」
     * @param starttime    運動開始時刻
     * @param duration 継続時間
     * @param timedivision   運動時間区分
     * @param count_over_r    角度オーバーした回数
     * @param count_best_r    角度が良であった回数
     * @param  count_under_r　　角度不足だった回数（右）
     * @param count_over_l    角度オーバーした回数
     * @param count_best_l 角度が良であった回数
     * @param count_under_l   角度不足だった回数
     * @param raw_data_r    センサ収集データファイル名
     * @param raw_data_l    センサ収集データファイル名
     * @return 更新した「家計簿データ」
     */
    public AncleexData update(String content,  long starttime, long duration, int timedivision, int count_over_r, int count_best_r, int count_under_r,
                              int count_over_l, int count_best_l, int count_under_l, String raw_data_r, String raw_data_l ){
        this.content = content;  //「内容」を設定

        this.starttime=starttime;  //運動開始時刻
        this.duration = duration;   //継続時間
        this.timedivision = timedivision;//運動時間区分　1:8-10 2:10-12 3:12-14 ...
        this.count_over_r = count_over_r; //角度オーバーした回数（右）
        this.count_best_r = count_best_r;    //角度が良であった回数（右）
        this.count_under_r = count_under_r;   //角度不足だった回数（右）
        this.count_over_l = count_over_l;    //角度オーバーした回数（左）
        this.count_best_l = count_best_l;    //角度が良であった回数（左）
        this.count_under_l = count_under_l;   //角度不足だった回数（左）
        this.raw_data_r = raw_data_r;   //センサ収集データファイル名（右）
        this.raw_data_l = raw_data_l;   //センサ収集データファイル名（左）

        return this;
    }

    @Override
    public AncleexData clone()throws CloneNotSupportedException{
        AncleexData clone = (AncleexData)super.clone();
        return clone;

    }

}
