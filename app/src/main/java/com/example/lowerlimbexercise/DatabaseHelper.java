package com.example.lowerlimbexercise;

import android.os.Handler;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import androidx.room.Room;

/**
 *   データベースヘルパー
 */
public class DatabaseHelper extends AccountUtilities{
    private AppDatabase mDb;                            //DB
    private AccountDao mDao;                            //DAO
    private AncleexData mAd;                            //足関節運動データ
    private List<AncleexData> mLad = null;              //足関節運動データリスト
    private final String DB_NAME = "account-database2";  //DB名
    final Handler mHandler = new Handler();             //ハンドラー

    /**
     *  コンストラクタ
     */
    public DatabaseHelper() {
        //Roomインスタンスを取得
        mDb = Room.databaseBuilder(getMainActivity(), AppDatabase.class, DB_NAME).build();
        //DAOを取得
        mDao = mDb.accountDao();
    }

    /**
     *  DBからデータ取得&表示
     */
    public void getData() {
        //ExecutorServiceを取得
        ExecutorService executor = Executors.newSingleThreadExecutor();
        //ExecutorServiceでタスクを実行（非同期処理）
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    long startDate = getMainActivity().getDisplayStartDate();  //「表示開始日」の取得
                    long lastDate = getMainActivity().getDisplayLastDate();    //「表示終了日」の取得

                    mLad = mDao.getData(startDate, lastDate);  //DBからデータを取得
                    //mLad = mDao.getData( );  //DBからデータを取得
                } catch (Exception e) {
                    //データ取得エラー時の処理
                    displayMessage(AccountUtilities.getStr(R.string.canNotGetData));
                }

                //データ取得成功時の処理
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //リサイクラービューのデータを更新
//                        getMainActivity().updateDataView(mLad);
                        //合計金額の表示
                        //getMainActivity().displaySumPrice(mLad);
                    }
                });
            }
        });
    }

    /**
     *  DBからデータ取得&表示
     */

    public AncleexData getLastAncleexData( ) {

        System.out.println("++getLastAncleexData()");
        AncleexData ancleexData = new AncleexData("", 0,  0, 0, 0, 0, 0, 0,
                0, 0,  "", "");
        //ExecutorServiceを取得
        ExecutorService executor = Executors.newSingleThreadExecutor();
        //ExecutorServiceでタスクを実行（非同期処理）
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    long startDate = getMainActivity().getDisplayStartDate();  //「表示開始日」の取得
                    long lastDate = getMainActivity().getDisplayLastDate();    //「表示終了日」の取得

                    mLad = mDao.getData(startDate, lastDate);  //DBからデータを取得

                    //mLad = mDao.getData( );  //DBからデータを取得

                } catch (Exception e) {
                    //データ取得エラー時の処理
                    displayMessage(AccountUtilities.getStr(R.string.canNotGetData));
                }



                //データ取得成功時の処理
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //リサイクラービューのデータを更新
                        //getMainActivity().updateDataView(mLad);
                        //合計金額の表示
                       // getMainActivity().displaySumPrice(mLad);
                    }
                });
            }
        });

        try {

            int size = mLad.size();
            ancleexData = (AncleexData) mLad.get(size-1).clone();

        }catch (Exception e){

        }

        return ancleexData;

    }

    /**
     *  DBからデータ取得&表示
     */
    public void getLastData() {
        //ExecutorServiceを取得
        ExecutorService executor = Executors.newSingleThreadExecutor();
        //ExecutorServiceでタスクを実行（非同期処理）
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    long startDate = getMainActivity().getDisplayStartDate();  //「表示開始日」の取得
                    long lastDate = getMainActivity().getDisplayLastDate();    //「表示終了日」の取得

                    mLad = mDao.getData(startDate, lastDate);  //DBからデータを取得
                    System.out.println("データ長："+mLad.size());
                  //  mLad = mDao.getData( );  //DBからデータを取得
                } catch (Exception e) {
                    //データ取得エラー時の処理
                    displayMessage(AccountUtilities.getStr(R.string.canNotGetData));
                }

                //データ取得成功時の処理
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //リサイクラービューのデータを更新
                        //getMainActivity().updateDataView(mLad);
                        //合計金額の表示
                        //getMainActivity().displaySumPrice(mLad);
                    }
                });
            }
        });
    }


    /**
     * DBへデータ追加
     * @param ad 追加する足関節運動データ
     */
    public void insertData(AncleexData ad){
        mAd = ad;  //足関節運動データ

        System.out.println("++DatabaseHelper::insertData()");

        //ExecutorServiceを取得
        ExecutorService executor = Executors.newSingleThreadExecutor();
        //ExecutorServiceでタスクを実行（非同期処理）
        executor.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("ExcutorService:threadID"+Thread.currentThread().getId());
                try {
                    //足関節運動データをDBへ追加
                    mDao.insert(mAd);
                } catch (Exception e) {
                    //データ追加エラー時の処理
                    displayMessage(AccountUtilities.getStr(R.string.canNotAddData));
                    System.out.println("データが登録できませんでした");
                }

                //データ追加成功時の処理
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        displayMessage(AccountUtilities.getStr(R.string.successRegistData));
                        System.out.println("データを登録しました");
                        getData();                                //DBからデータを取得
                        //getMainActivity().displaySumPrice(mLad);  //合計金額の表示
                    }
                });
            }
        });
    }

    /**
     * DBのデータを更新
     * @param position  更新データの位置
     * @param content   内容
     * @param year ;       //「年」カラムを定義
     * @param month ;      //「月」カラムを定義
     * @param day;         //「日」カラムを定義
     */
    public void updateData(int position, String content, int year,int month,int day,long starttime,long duration,int timedivision,int count_over_r,int count_best_r,int count_under_r,
                           int count_over_l,int count_best_l,int count_under_l,String raw_data_r,String raw_data_l){
        //ExecutorServiceの処理に渡す値の定数
        final String pContent = content;  //内容
        final int pyear = year;          //日付
        final int pmonth = month;          //日付
        final int pday = day;          //日付
        final long pstarttime = starttime;  //運動開始時刻
        final long pduration = duration;   //継続時間
        final int ptimedivision = timedivision;//運動時間区分　1:8-10 2:10-12 3:12-14 ...
        final int pcount_over_r = count_over_r;    //角度オーバーした回数（右）
        final int pcount_best_r = count_best_r;    //角度が良であった回数（右）
        final int pcount_under_r = count_under_r;   //角度不足だった回数（右）
        final int pcount_over_l = count_over_l;    //角度オーバーした回数（左）
        final int pcount_best_l = count_best_l;    //角度が良であった回数（左）
        final int pcount_under_l = count_under_l;   //角度不足だった回数（左）
        final String praw_data_r = raw_data_r;   //センサ収集データファイル名（右）
        final String praw_data_l = raw_data_l;   //センサ収集データファイル名（左）

        final int pPosition = position;   //データの位置

        //ExecutorServiceを取得
        ExecutorService executor = Executors.newSingleThreadExecutor();
        //ExecutorServiceでタスクを実行（非同期処理）
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    AncleexData updateAd = null;  //更新する家計簿データ
                    try {
                        //アダプター内の家計簿データを更新＆更新データの取得
                        updateAd = getAdapter().getAccountData(pPosition).update(pContent,pstarttime,pduration,ptimedivision,pcount_over_r,pcount_best_r,pcount_under_r,
                                pcount_over_l,pcount_best_l,pcount_under_l,praw_data_r,praw_data_l);
                    } catch (Exception e){
                        //データ更新エラー時の処理
                        displayMessage(AccountUtilities.getStr(R.string.canNotUpdateData));
                    }
                    //DBのデータを更新
                    mDao.update(updateAd);
                } catch (Exception e) {
                    //データ更新エラー時の処理
                    displayMessage(AccountUtilities.getStr(R.string.canNotUpdateData));
                }

                //データ更新成功時の処理
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        displayMessage(AccountUtilities.getStr(R.string.successUpdateData));
                        //getMainActivity().displaySumPrice(mLad);  //合計金額の表示
                    }
                });
            }
        });
    }

    /**
     * DBのデータを削除
     * @param ad        削除する家計簿データ
     * @param position  データの位置
     */
    public void deleteData(AncleexData ad, final int position){
        //ExecutorServiceの処理に渡す値の定数
        mAd = ad;

        //ExecutorServiceを取得
        ExecutorService executor = Executors.newSingleThreadExecutor();
        //ExecutorServiceでタスクを実行（非同期処理）
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //DBのデータを削除
                    mDao.delete(mAd);
                    //アダプター内の家計簿データを削除
                    getAdapter().deleteAccountDataList(position);
                } catch (Exception e) {
                    //データ削除エラー時の処理
                    displayMessage(AccountUtilities.getStr(R.string.canNotDeleteData));
                }

                //データ削除成功時の処理
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        displayMessage(AccountUtilities.getStr(R.string.successDeleteData));
                        //getMainActivity().displaySumPrice(mLad);  //合計金額の表示
                    }
                });
            }
        });
    }
}
