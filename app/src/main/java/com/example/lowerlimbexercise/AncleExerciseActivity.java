package com.example.lowerlimbexercise;

import static com.example.lowerlimbexercise.AccountUtilities.getCalendar;
import static com.example.lowerlimbexercise.AccountUtilities.setDatabaseHelper;
import static com.example.lowerlimbexercise.BluetoothChatFragment.devicenumber_lefttoe;
import static com.example.lowerlimbexercise.BluetoothChatFragment.devicenumber_righttoe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AncleExerciseActivity extends AppCompatActivity implements MountDeviceFragment.MountDeviceFragmentListener, AncleExRadyFragment.AncleExRadyFragmentListener ,AncleExerciseFragment.AncleExerciseFragmentListener, AncleExFinnishFragment.AncleExFinnishFragmentListener {

    private double moveanglelefttoe;
    private double moveanglerighttoe;
    public AncleexData mad;

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ancleexercise);

        sharedPref =
                PreferenceManager.getDefaultSharedPreferences(this);

       //setDatabaseHelper(new DatabaseHelper());  //データベースヘルパーを設定

        //運動記録のための準備
        //追加データの生成
        mad = new AncleexData("", 0,0,0,0, 0, 0, 0, 0, 0,
                 "", "");


        // メソッドを呼び出し、デフォルトでMainFragmentを表示
        addFragment(new MountDeviceFragment());

        //デバッグ用
        /*addFragment(new AncleTrainFragment());
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_bluetooth_chat);
        hidefragment(fragment);*/
    }

    @Override
    protected void onStop(){
        System.out.println("++AncleExrciseActivity::onStop()");
        super.onStop();
        //計測終了イベント
        Fragment fragmentbt = getSupportFragmentManager().findFragmentById(R.id.fragment_bluetooth_chat);
        ((BluetoothChatFragment)fragmentbt).onDestroy();
        finish();
        System.out.println("--AncleExrciseActivity::onStop()");

    }

    // Fragmentを表示させるメソッドを定義（表示したいFragmentを引数として渡す）
    private void addFragment(Fragment fragment) {
        // フラグメントマネージャーの取得
        FragmentManager manager = getSupportFragmentManager();
        // フラグメントトランザクションの開始
        FragmentTransaction transaction = manager.beginTransaction();
        // MainFragmentを追加
        transaction.add(R.id.activityAncleTrainMain, fragment);

        // フラグメントトランザクションのコミット。コミットすることでFragmentの状態が反映される
        transaction.commit();
    }

    // Fragmentを表示させるメソッドを定義（表示したいFragmentを引数として渡す）
    private void replaceFragment(Fragment fragment,String name) {
        // フラグメントマネージャーの取得
        FragmentManager manager = getSupportFragmentManager();
        // フラグメントトランザクションの開始
        FragmentTransaction transaction = manager.beginTransaction();
        // MainFragmentを追加
        transaction.replace(R.id.activityAncleTrainMain, fragment,name);
        // フラグメントトランザクションのコミット。コミットすることでFragmentの状態が反映される
        transaction.commit();
    }



    // 戻るボタン「←」をアクションバー（上部バー）にセットするメソッドを定義
    public void setupBackButton(boolean enableBackButton) {
        // アクションバーを取得
        ActionBar actionBar = getSupportActionBar();
        // アクションバーに戻るボタン「←」をセット（引数が true: 表示、false: 非表示）
        actionBar.setDisplayHomeAsUpEnabled(enableBackButton);
    }




    /*setDefaultPosition
    // 引数　bStart :true  初期化開始
    //              false  初期化終了
     */
    public void setDefaultPosition(boolean bstart){
        //アクティビティからフラグメントを呼ぶ方法
        System.out.println("++AncleTrainActivity::setDefaultPosition");
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_bluetooth_chat);
        if(fragment != null )((BluetoothChatFragment)fragment).setDefaultPosition(bstart);


    }

    //右つま先底屈検知
    public void rightAncleClick(){
       Fragment fragment = getSupportFragmentManager().findFragmentByTag("AncleExerciseFragment");
       if(fragment != null )((AncleExerciseFragment)fragment).setrightAncleclicke();

    }


    //左つま先底屈検知
    public void leftAncleClick(){
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("AncleExerciseFragment");
        if(fragment != null )((AncleExerciseFragment)fragment).setleftAncleclicke();

    }



    @Override
    public void onMountDeviceFragmentEvent(MountDeviceFragmentEvent event) {
        event.apply(this);

        if(event == MountDeviceFragmentEvent.EVENT1){
            // メソッドを呼び出し、デフォルトでMainFragmentを表示 後でフラグメントにイベント渡しするため名前を設定
            replaceFragment(new AncleExRadyFragment(),"AncleExRadyFragment");
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_bluetooth_chat);
            ((BluetoothChatFragment)fragment).connectDevices();
            hidefragment(fragment);

        }
    }

    @Override
    public void onAncleExRadyFragmentEvent(AncleExRadyFragmentEvent event) {
        event.apply(this);

        if(event == AncleExRadyFragmentEvent.EVENT1){
            //運動開始時刻を記録
            //現在のタイムスタンプを取得
            AccountDataUtilities au = new AccountDataUtilities();
            mad.starttime = au.getCurrentTimestamp();
            mad.content = "足関節底背屈運動";

            // メソッドを呼び出し、デフォルトでMainFragmentを表示
            replaceFragment(new AncleExerciseFragment(),"AncleExerciseFragment");

        }

    }

    @Override
    public void onAncleExerciseFragmentEvent(AncleExerciseFragmentEvent event) {
        event.apply(this);

        if(event == AncleExerciseFragmentEvent.EVENT1){
            // メソッドを呼び出し、デフォルトでMainFragmentを表示
            System.out.println("onAncleExerciseFragmentEvent!");
            //運動データをDBに書き込む  デバッグ用にいったん削除

            //計測終了イベント
            Fragment fragmentbt = getSupportFragmentManager().findFragmentById(R.id.fragment_bluetooth_chat);
            ((BluetoothChatFragment)fragmentbt).stopMeasurement();

            //現在のタイムスタンプを取得
            RegistData rg = new RegistData();
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("AncleExerciseFragment");
            ((AncleExerciseFragment)fragment).getExData(mad);

            rg.SetData(mad);
            replaceFragment(new AncleExFinnishFragment(),"AncleExFinnishFragment");

        }

    }
    @Override
    public void onAncleExFinnishFragmentEvent(AncleExFinnishFragmentEvent event) {
        event.apply(this);

        if(event == AncleExFinnishFragmentEvent.EVENT1){
            // メソッドを呼び出し、デフォルトでMainFragmentを表示
            System.out.println("onAncleExFinnishFragmentEvent!");
            replaceFragment(new AncleexResultFragment(),"AncleexResultFragment");
           /* Intent intent = new Intent(this, AncleexResultActivity.class);  //インテントの作成
            startActivity(intent);   */                              //画面遷移

        }

    }
    public void hidefragment(Fragment fragment){

        // 非表示処理
        // フラグメントマネージャーの取得
        FragmentManager manager = getSupportFragmentManager();
        // フラグメントトランザクションの開始
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.hide(fragment);
        transaction.commit();

    }

    public double getMoveangle(int devicenumber){
        System.out.println("++AncleTrainActivity::getMoveAngle("+devicenumber+")");
        System.out.println("--AncleTrainActivity::getMoveangle(moveanglerighttoe="+moveanglerighttoe+",moveanglelefttoe="+moveanglelefttoe+")");
        if(devicenumber == devicenumber_righttoe)
            return moveanglerighttoe;
        else
            return moveanglelefttoe;
    }


    public void setMoveangle(int devicenumber,double angle){
        System.out.println("++AncleTrainActivity::setMoveAngle("+devicenumber+","+angle+")");

        if(devicenumber == devicenumber_righttoe)
             moveanglerighttoe = angle;
        else
            moveanglelefttoe = angle;
    }

    public AncleexData getancleexdata(){
        System.out.println("++AncleTrainActivity::getancleexdata( )");
        System.out.println("mad.count_over_l="+mad.count_over_l);
        System.out.println("mad.count_best_l="+mad.count_best_l);
        System.out.println("mad.count_under_l="+mad.count_under_l);

        return mad;
    }

    public String getsensoraddress(int devicenum){
        String macaddress = "";
        if(devicenum == devicenumber_righttoe){
            macaddress = sharedPref.getString("sensor_righttoe","");
        }
        if(devicenum == devicenumber_lefttoe){
            macaddress = sharedPref.getString("sensor_lefttoe","");
        }
        System.out.println("--getsensoraddress("+macaddress+")");
        return macaddress;
    }

    public String getBMSfilename(){
        String filename="";
        filename = sharedPref.getString("bmsfilename","");
        return filename;
    }




}
