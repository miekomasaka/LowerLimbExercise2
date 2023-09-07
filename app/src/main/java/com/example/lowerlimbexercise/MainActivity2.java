package com.example.lowerlimbexercise;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ViewAnimator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 *  メインアクティビティ
 */
public class MainActivity2 extends AccountUtilities {
    private RecyclerView mAccountRecyclerView;                  //リサイクラービュー
    private RecyclerView.Adapter mRecyclerAdapter;              //アダプター
    private RecyclerView.LayoutManager mRecyclerLayoutManager;  //レイアウトマネージャー
    private long mDisplayStartDate = 0;                         //表示開始時刻（タイムスタンプ)
    private long mDisplayLastDate = 0;                          //表示終了時刻（タイムスタンプ)

    private ImageView tsndImage;
    private boolean mLogShown;
    private boolean bRealDevice = TRUE;


    SharedPreferences sharedPref;



    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //インフレータを使ってメニューを表示させる
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toppagemenu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        System.out.println("++onOptionsItemSelected");

            switch (item.getItemId()) {
                case R.id.item1:
                    break;
                case R.id.item2:
                    final String[] items = {"Milkeyway", "short", "Milkeyway-consts"};  //本来は、構造体としてどこかに保管しておく
                    int defaultItem = 0; // デフォルトでチェックされているアイテム
                    final List<Integer> checkedItems = new ArrayList<>();
                    checkedItems.add(defaultItem);
                    new AlertDialog.Builder(this)
                            .setTitle("Selector")
                            .setSingleChoiceItems(items, defaultItem, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    checkedItems.clear();
                                    checkedItems.add(which);
                                }
                            })
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (!checkedItems.isEmpty()) {
                                        Log.d("checkedItem:", "" + checkedItems.get(0));
                                        SharedPreferences.Editor editor= sharedPref.edit();
                                        String filename=items[checkedItems.get(0)]+".bms";
                                        editor.putString("bmsfilename",filename);
                                        editor.commit();
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                    break;
                default:
                    break;
            }
            /*
                    mLogShown = !mLogShown;
          //          ViewAnimator output = findViewById(R.id.sample_output);  あとで動くようにする必要あり
                    if (mLogShown) {
         //               output.setDisplayedChild(1);
                    } else {
         //               output.setDisplayedChild(0);
                    }
                    invalidateOptionsMenu();
                    return true;
            }*/

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //AccountUtilitiesクラスへインスタンスを設定
        setContext(getApplicationContext());      //コンテキストを設定
        setMainActivity(this);                    //メインアクティビティを設定
        setDatabaseHelper(new DatabaseHelper());  //データベースヘルパーを設定

        sharedPref =
                PreferenceManager.getDefaultSharedPreferences(this);

        //デバッグ用に初期値を入れておく　・・最終的には削除する
        SharedPreferences.Editor editor= sharedPref.edit();
        //editor.putString("sensor_righttoe","00:07:80:48:0E:D6");
        editor.putString("sensor_righttoe","00:07:80:48:0C:28");
        editor.commit();
        editor.putString("sensor_lefttoe","00:07:80:48:0C:80");
        editor.commit();
        editor.putString("bmsfilename","Milkeyway.bms");
        editor.commit();



        //「運動を始める」ボタンのイベントリスナー
        ImageButton ancleexImageButton = (ImageButton)findViewById(R.id.imageButton2);
        //ancleexImageButton.setImageResource(R.drawable.ancleicon);

        // perform click event on button's
        ancleexImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(bRealDevice == TRUE) {
                    Intent intent = new Intent(getApplication(), SelectexerciseActivity.class);
                    startActivity(intent);                                 //画面遷移
                }else{
                   // Intent intent = new Intent(getApplication(), AncleexerciseActivity.class);
                    //startActivity(intent);                                 //画面遷移
                }
            }
        });

        getBtn(R.id.top_showresultbutton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //「データ登録ボタン」がクリックされたら「データ登録」用アクティビティを表示
                        //startActivity(new Intent(getApplication(), RegistAccountDataActivity.class));
                        //Intent intent = new Intent(getApplication(), AncleexerciseActivity.class);  //インテントの作成
                        //Intent intent = new Intent(getApplication(), AnclFinnishedActivity.class);
                        Intent intent = new Intent(getApplication(),SelectexerciseActivity.class);
                        startActivity(intent);

                    }
                }
        );



    }



    /**
     * 「表示開始日」のタイムスタンプを取得(Getter)
     * @return 「表示開始日」のタイムスタンプ
     */
    public long getDisplayStartDate() {
        return mDisplayStartDate;
    }

    /**
     * 「表示開始日」のタイムスタンプを設定(Setter)
     * @param displayStartDate
     */
    public void setDisplayStartDate(long displayStartDate) {
        mDisplayStartDate = displayStartDate;
    }

    /**
     * 「表示終了日」のタイムスタンプを取得(Getter)
     * @return 「表示終了日」のタイムスタンプ
     */
    public long getDisplayLastDate() {
        return mDisplayLastDate;
    }

    /**
     * 「表示終了日」のタイムスタンプを取得(Setter)
     * @param displayLastDate 「表示終了日」のタイムスタンプ
     */
    public void setDisplayLastDate(long displayLastDate) {
        mDisplayLastDate = displayLastDate;
    }














}