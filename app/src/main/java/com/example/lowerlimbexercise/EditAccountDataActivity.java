package com.example.lowerlimbexercise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import java.util.Calendar;

/**
 *  「データ編集」画面のアクティビティ
 */
public class EditAccountDataActivity extends AccountDataUtilities {

    int mSelectId = 0;          //「編集データ」のID
    int mSelectPosition = 0;    //「編集データ」のアダプターが保持している「ArrayList」の「position（index）」

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_account_data);

        //インテントの取得
        Intent intent = getIntent();
        if(intent != null){
            //インテントからIDを取得
            mSelectId = intent.getIntExtra("id", 0);
            //インテントからポジションを取得
            mSelectPosition = intent.getIntExtra("position", 0);
        }

        // 「編集データ」を取得
        AncleexData ad = getAdapter().getAccountData(mSelectPosition);

        //「内容」を表示
        getEt(R.id.EaInputContentEt).setText(ad.getContent());

        //「価格」を表示
       // getEt(R.id.EaInputPriceEt).setText(String.valueOf(ad.getPrice()));

        Calendar calendar = getCalendar();       //カレンダーの取得
      //  calendar.setTimeInMillis(ad.getDate());  //カレンダーに「編集データ」の日付を設定

        int year = calendar.get(Calendar.YEAR);         //「年」を取得
        int month = calendar.get(Calendar.MONTH);       //「月」を取得
        int day = calendar.get(Calendar.DAY_OF_MONTH);  //「日」を取得

        //「表示日付」を設定
        ((AccountDataUtilities)this).setDate(year, month, day, R.id.EaDisplayDateTv, R.id.EaErrorInputDateTv, CheckDateType.INPUT_EDIT);

        //「日付設定」ボタンのイベントリスナーを設定
        setDateEventListener(this, R.id.EaSetDateBtn, R.id.EaDisplayDateTv, R.id.EaErrorInputDateTv, year, month, day ); //「日付設定ボタン」のイベントリスナー設定

        //「データ更新ボタン」のイベントリスナーを設定
        getBtn(R.id.EaInputScrRegistDataBtn).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //エラー表示をクリア
                        clearError(R.id.EaErrorInputContentTv, R.id.EaErrorInputPriceTv, R.id.EaErrorInputDateTv);

                        //更新するタイムスタンプを取得
                        long date = getCurrentTimestamp();

                        //入力された「内容」「価格」のチェック
                        boolean resultCheckContent = checkInputContent(R.id.EaInputContentEt,R.id.EaErrorInputContentTv);
                        boolean resultCheckPrice = checkInputPrice(R.id.EaInputPriceEt,R.id.EaErrorInputPriceTv);

                        if(resultCheckContent && resultCheckPrice){

                            //「内容」を取得
                            String content = getEt(R.id.EaInputContentEt).getText().toString();

                            //「価格」を取得
                            int price = Integer.parseInt(getEt(R.id.EaInputPriceEt).getText().toString());

                            long starttime = 1 ;  //運動開始時刻
                            long duration = 1;   //継続時間
                            int timedivision = 1;//運動時間区分　1:8-10 2:10-12 3:12-14 ...
                            int count_over_r = 1;    //角度オーバーした回数（右）
                            int count_best_r = 1;    //角度が良であった回数（右）
                            int count_under_r = 1;   //角度不足だった回数（右）
                            int count_over_l = 1;    //角度オーバーした回数（左）
                            int count_best_l = 1;    //角度が良であった回数（左）
                            int count_under_l = 1;   //角度不足だった回数（左）
                            String raw_data_r = "test.csv";   //センサ収集データファイル名（右）
                            String raw_data_l = "test.csv";   //センサ収集データファイル名（左）

                            //DBのデータを更新
                            //mDbh.updateData(mSelectPosition, content, starttime, duration, timedivision, count_over_r, count_best_r, count_under_r,
                            // count_over_l, count_best_l, count_under_l, raw_data_r, raw_data_l);

                            //データの変更をリサイクラービューに通知
                            getAdapter().notifyItemChanged(mSelectPosition);

                            //メッセージを表示
                            displayMessage(getString(R.string.successUpdateData));

                            //「データ編集画面」を非表示
                            finish();
                        }
                    }
                }
        );
    }
}
