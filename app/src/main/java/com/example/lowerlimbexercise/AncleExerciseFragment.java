package com.example.lowerlimbexercise;

import static com.example.lowerlimbexercise.BluetoothChatFragment.devicenumber_lefttoe;
import static com.example.lowerlimbexercise.BluetoothChatFragment.devicenumber_righttoe;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.Math.abs;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.PorterDuff;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.media.AudioManager;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.io.IOException;
import java.util.Calendar;

public class AncleExerciseFragment extends Fragment implements Animator.AnimatorListener {



    //BMSファイル読み込み用変数
    private BmsInfo bmsinfo = new BmsInfo();
    private CBmsPro bms = new CBmsPro();
    private boolean[] bOnKey = new boolean[5];

    //音楽再生用変数
    private SoundPool soundPool;
    private int sound10,sound20,sound30,sound40,soundover40,soundTwo;
    private MediaPlayer mediaPlayer;

    //ゲームコントロール用
    private long StartTime;  //ゲームの開始時間 (ゲーム時間コントロール用）
    private int[] iStartNum = new int[256];
    private static final long START_TIME = 10000;
    private long mTimeLeftInMillis = START_TIME;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private LayoutInflater mInflater;
    private ViewGroup mcontainer;


    private AncleexData mad;
    //アニメーション用変数
    private ImageView judgeImage;
    private ImageView judgeImager;

    private boolean mKeyDownl;
    private boolean mKeyDownr;

    public Button buttonGood ;
    public Button buttonGoodr ;

    private  FrameLayout mlayoutl;
    private  FrameLayout mlayoutr;

    private ScaleAnimation mscaleAnimation;

    private AncleExerciseFragment.AncleExerciseFragmentListener listener = null;

    public interface AncleExerciseFragmentListener {
        void onAncleExerciseFragmentEvent(AncleExerciseFragmentEvent event);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_ancleexercise, container, false);

        mInflater = inflater;
        mcontainer = container;


        // 所属親アクティビティを取得
        AncleExerciseActivity activity = (AncleExerciseActivity) getActivity();
        // アクションバーにタイトルをセット
        activity.setTitle("サブフラグメント２");
        // 戻るボタンを表示する
        activity.setupBackButton(true);

        // この記述でフラグメントでアクションバーメニューが使えるようになる
        setHasOptionsMenu(true);
        mad = new AncleexData("content",
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                "test",
                "test"
        );


        Button buttonStart = view.findViewById(R.id.button_start);
         buttonGood = view.findViewById(R.id.button_good);
         buttonGoodr = view.findViewById(R.id.button3);


        //中央下側の足のイラストを描画
        ImageView imageViewlbasel = new ImageView(activity);
        ImageView imageViewlbaser = new ImageView(activity);

        //花火のイラストを描画
        judgeImage = new ImageView(activity);
        judgeImager = new ImageView(activity);


        //画像指定
        imageViewlbasel.setImageResource(R.drawable.foot_left);
        imageViewlbaser.setImageResource(R.drawable.foot_right);
        judgeImage.setImageResource(R.drawable.firework2);
        judgeImager.setImageResource(R.drawable.firework2);

        //花火アニメーション設定
        float fromX = 0;
        float ftoX = 1.0F;
        float fromY = 0;
        float ftoY = 1.0F;
        mscaleAnimation = new ScaleAnimation(fromX, ftoX, fromY, ftoY,
                Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        // animation時間 msec
        mscaleAnimation.setDuration(300);
        // 繰り返し回数
        mscaleAnimation.setRepeatCount(0);
        // animationが終わったら消す
        mscaleAnimation.setFillAfter(false);

        //レイアウトを取得
        mlayoutl = view.findViewById(R.id.constantlayoutl);
        mlayoutr = view.findViewById(R.id.constantlayoutr);

        // PropertyValuesHolderを使ってＸ軸方向移動範囲のpropertyを保持
        int width=mlayoutl.getWidth();
        int height=mlayoutl.getHeight();

        int imageWith = 370;  //この数字も本当は、きちんと取得すべき　ひとまず仮の値
        int imageHeight = 370;

        RelativeLayout.LayoutParams layoutParams= new RelativeLayout.LayoutParams(imageWith,imageHeight);
        layoutParams.leftMargin = 380;
        layoutParams.topMargin = 240;
        imageViewlbasel.setLayoutParams(layoutParams);

        RelativeLayout.LayoutParams layoutParamsr= new RelativeLayout.LayoutParams(imageWith,imageHeight);
        layoutParamsr.leftMargin = 0;
        layoutParamsr.topMargin = 240;
        imageViewlbaser.setLayoutParams(layoutParamsr);

        imageWith = 250;
        imageHeight = 250;
        RelativeLayout.LayoutParams layoutParamsji = new RelativeLayout.LayoutParams(imageWith,imageHeight);
        layoutParamsji.leftMargin = 420;
        layoutParamsji.topMargin = 60;
        judgeImage.setLayoutParams(layoutParamsji);
        judgeImage.setVisibility(View.INVISIBLE);


        RelativeLayout.LayoutParams layoutParamsjir = new RelativeLayout.LayoutParams(imageWith,imageHeight);
        layoutParamsjir.leftMargin = 90;
        layoutParamsjir.topMargin = 60;
        judgeImager.setLayoutParams(layoutParamsjir);
        judgeImager.setVisibility(View.INVISIBLE);


        mlayoutl.addView(imageViewlbasel);
        mlayoutl.addView(judgeImage);
        //layoutl.addView(imageViewlbaser);
        mlayoutr.addView(imageViewlbaser);
        mlayoutr.addView(judgeImager);



        //Animation 開始
        buttonStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //BMSファイル読み込み
                System.out.println("BMSファイル読み込み");
                //ファイル名の取得

                bms.readLine(activity.getApplicationContext(),activity.getBMSfilename());
                System.out.println("音楽再生"+bms.mWaveFile[0xff]);
                //音楽再生開始  後で有効にする
                audioPlay(bms.mWaveFile[0xff]);

                //効果音のためのSounpool設定
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        // USAGE_MEDIA
                        // USAGE_GAME
                        .setUsage(AudioAttributes.USAGE_GAME)
                        // CONTENT_TYPE_MUSIC
                        // CONTENT_TYPE_SPEECH, etc.
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .build();

                soundPool = new SoundPool.Builder()
                        .setAudioAttributes(audioAttributes)
                        // ストリーム数に応じて
                        .setMaxStreams(2)
                        .build();

                // 効果音  しつこいので止めた
                //sound10 = soundPool.load(activity.getApplicationContext(), R.raw.s10, 1);
                //sound20 = soundPool.load(activity.getApplicationContext(), R.raw.s20, 1);
                sound30 = soundPool.load(activity.getApplicationContext(), R.raw.se, 1);
                //sound40 = soundPool.load(activity.getApplicationContext(), R.raw.s40, 1);
                //soundover40 = soundPool.load(activity.getApplicationContext(), R.raw.sover, 1);

                // two.wav をロードしておく
                //soundTwo = soundPool.load(activity.getApplicationContext(), R.raw.two, 1);

                // load が終わったか確認する場合
                soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                        Log.d("debug","sampleId="+sampleId);
                        Log.d("debug","status="+status);
                    }
                });

                GameRun(FALSE);
                startTimer();

            }
        });

        //Goodアニメーション
        buttonGood.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mKeyDownl = TRUE;

            }
        });

        //Goodアニメーション
        buttonGoodr.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mKeyDownr = TRUE;
                //setAnimation();
            }
        });

        //スタートボタンを押下を疑似的に発行
        buttonStart.performClick();

        // View viewのが良い？
        return view;
    }
    private void audioPlay(String filename) {
        FragmentActivity factivity = getActivity();

        if (mediaPlayer == null) {
            // audio ファイルを読出し
            if (audioSetup(filename)){
                Toast.makeText(factivity, "Rread audio file", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(factivity, "Error: read audio file", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        else{
            // 繰り返し再生する場合
            mediaPlayer.stop();
            mediaPlayer.reset();
            // リソースの解放
            mediaPlayer.release();
        }

        // 再生する
        mediaPlayer.start();

        //開始時刻を取得　（ゲーム管理用）
        StartTime = System.currentTimeMillis();
        System.out.println("onClick StartTime="+StartTime);

        //年月日を取得
        Calendar cl = Calendar.getInstance();  //カレンダーの取得 現在の日付と時刻で初期化
        mad.starttime = cl.getTimeInMillis();

        int hour = cl.get(Calendar.HOUR);  //何時か
        //運動時間区分　1:8-10 2:10-12 3:12-14 ...
        if(hour <= 10) mad.timedivision = 1;
        else if (hour <= 12) mad.timedivision =2;
        else if (hour <= 14) mad.timedivision =3;
        else if(hour <= 16) mad.timedivision =4;
        else if(hour <= 18) mad.timedivision =5;
        else  mad.timedivision =6;



        // 終了を検知するリスナー
//        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                Log.d("debug","end of audio");
//                audioStop();
//            }
//        });
        // lambda
        mediaPlayer.setOnCompletionListener( mp -> {
            //音楽終了時の処理終了
            Log.d("debug","end of audio");
            audioStop();

            //登録が終わったら終了画面を表示する
            //運動継続時刻を取得
            Calendar calendar2 = Calendar.getInstance();
            long millis2 = calendar2.getTimeInMillis();

            mad.duration = millis2 - mad.starttime;
            System.out.println("運動継続時間は" + mad.duration  + "ミリ秒です");

            listener.onAncleExerciseFragmentEvent(AncleExerciseFragmentEvent.EVENT1);

        });

    }
    //音楽の制御
    private boolean audioSetup(String filePath){

        // インタンスを生成
        mediaPlayer = new MediaPlayer();

        //音楽ファイル名, あるいはパス
        // String filePath = "background.mp3";
        // String filePath = bmsinfo.BgmSountFile.substring(0);

        boolean fileCheck = false;

        // assetsから mp3 ファイルを読み込み
        try(AssetFileDescriptor afdescripter = getActivity().getAssets().openFd(filePath))
        {
            // MediaPlayerに読み込んだ音楽ファイルを指定
            mediaPlayer.setDataSource(afdescripter.getFileDescriptor(),
                    afdescripter.getStartOffset(),
                    afdescripter.getLength());
            // 音量調整を端末のボタンに任せる
            getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
            fileCheck = true;
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return fileCheck;
    }

    private void audioStop() {
        // 再生終了
        mediaPlayer.stop();
        // リセット
        mediaPlayer.reset();
        // リソースの解放
        mediaPlayer.release();

        mediaPlayer = null;
    }

    /*Goodエフェクト
    judge  0 :POOR
           3:GREAT
           2:GOOD
           1:BAD
     angle 0:～10度
     　　　　1:10～20度
         　 2:20～30度
            3:30～40度
            4:40～ 度
     */

    private void effect(int j,int judge,int angle){

        System.out.println("++AncleTrainFragment::effect(j="+j+"judge="+judge+"angle="+angle+")");

        //花火サイズの変更   入れるとややこしくなるので、ひとまず削除
              /*if(judge == 2){
                  ftoX = (float) (0.8);
                  ftoY = (float) (0.8);
              }else if(judge == 1){
                  ftoX = (float) (0.5);
                  ftoY = (float) (0.5);
              }else if(judge == 0){
                  ftoX =0;
                  ftoY = 0;
              }*/

            //左足アニメーションの開始

        if(j==devicenumber_lefttoe) {

           if(angle == 0){
                judgeImage.setColorFilter(getResources().getColor(R.color.collor_10),PorterDuff.Mode.SRC_IN);
                //オブジェクトの音を再生
               //(sound10,10.f,1.0f,0,0,1);
            }

            if(angle == 1){
                judgeImage.setColorFilter(getResources().getColor(R.color.collor_1020),PorterDuff.Mode.SRC_IN);
                //オブジェクトの音を再生
                //soundPool.play(sound20,10.f,1.0f,0,0,1);
            }
            if(angle == 2){
                judgeImage.setColorFilter(null);
                //オブジェクトの音を再生
                soundPool.play(sound30,10.f,1.0f,0,0,1);
            }
            if(angle == 3){
                judgeImage.setColorFilter(getResources().getColor(R.color.collor_3040), PorterDuff.Mode.SRC_IN);
                //オブジェクトの音を再生
                //soundPool.play(sound40,10.f,1.0f,0,0,1);
            }
            if(angle == 4){
                judgeImage.setColorFilter(getResources().getColor(R.color.collor_over40), PorterDuff.Mode.SRC_IN);
                //オブジェクトの音を再生
                ///soundPool.play(soundover40,10.f,1.0f,0,0,1);

            }
            judgeImage.startAnimation(mscaleAnimation);
        }

        //右足アニメーションの開始
        if(j==devicenumber_righttoe) {
            if(angle == 0){
                judgeImager.setColorFilter(getResources().getColor(R.color.collor_10),PorterDuff.Mode.SRC_IN);
                //オブジェクトの音を再生
                //soundPool.play(sound10,10.f,1.0f,0,0,1);
            }

            if(angle == 1){
                judgeImager.setColorFilter(getResources().getColor(R.color.collor_1020),PorterDuff.Mode.SRC_IN);
                //オブジェクトの音を再生
                //soundPool.play(sound20,10.f,1.0f,0,0,1);
            }
            if(angle == 2){
                judgeImager.setColorFilter(null);
                //オブジェクトの音を再生
                soundPool.play(sound30,10.f,1.0f,0,0,1);
            }
            if(angle == 3){
                judgeImager.setColorFilter(getResources().getColor(R.color.collor_3040), PorterDuff.Mode.SRC_IN);
                //オブジェクトの音を再生
                //soundPool.play(sound40,10.f,1.0f,0,0,1);
            }
            if(angle == 4){
                judgeImager.setColorFilter(getResources().getColor(R.color.collor_over40), PorterDuff.Mode.SRC_IN);
                //オブジェクトの音を再生
                //soundPool.play(soundover40,10.f,1.0f,0,0,1);

            }
            judgeImager.startAnimation(mscaleAnimation);
        }




    }

    private int GameRun(Boolean demo) {
        //System.out.println("++GameRun()");

        //int[] index = {0,2,4,1,3,5};
        int[] index = {0,1,2,3,4,5};

        //開始時から経過した時間を算出
        long currentTime = System.currentTimeMillis();
        //System.out.println("currentTime="+currentTime);
        long ElapsedTime = (currentTime - StartTime);
        //long ElapsedTime = mediaPlayer.getCurrentPosition();

        // bms.Clear();
        Long now_count = bms.GetCountFromeTime(ElapsedTime);
        //System.out.println("now_count"+now_count);

        //BMSカウンタが曲の最大カウントを超えたら終了
        if ((bms.GetMaxCount() + bms.BMS_RESOLUTION )<= now_count)
        {
            System.out.println("BMSカウンタが曲の最大カウントを超えたので終了 bms.GetMaxCOount="+bms.GetMaxCount()+"now_count="+now_count);
            mCountDownTimer.cancel();

            return 1;
        }



        //判定処理
        //仮想入力ハードウェア（押された瞬間だけTRUEとなる配列
        Boolean[] press = new Boolean[6];
        for (int i = 0; i < press.length; i++) press[i] = FALSE;

        // for (int i = 0; i < 5; i++){
        if (mKeyDownl == TRUE) {
            //入力あり
            //System.out.println("入力あり");
            if (!bOnKey[0]) {
                //まだ押されていなければ押された瞬間とする
                press[0] = TRUE;
                bOnKey[0] = TRUE;

            }
            mKeyDownl = FALSE;
        } else {
            //押されていなければフラグをリセット
            bOnKey[0] = FALSE;
        }

        if (mKeyDownr == TRUE) {
            //入力あり
            //System.out.println("入力あり");
            if (!bOnKey[3]) {
                //まだ押されていなければ押された瞬間とする
                press[3] = TRUE;
                bOnKey[3] = TRUE;

            }
            mKeyDownr = FALSE;
        } else {
            //押されていなければフラグをリセット
            bOnKey[3] = FALSE;
        }
        // }

        //入力判定
/*        long GREAT_RANGE = bms.BMS_RESOLUTION / 48;        //GREATと判定する中心からの範囲(前後合わせて24分音符内）
        long GOOD_RANGE = bms.BMS_RESOLUTION / 16;          //GOODと判定する中心からの範囲（前後合わせて８分音符以内）
        long BAD_RANGE = bms.BMS_RESOLUTION / 8;            //BADと判定する中心からの範囲
        long POOR_RANGE = bms.BMS_RESOLUTION / 2;           //POOR判定する中心からの範囲
*/
        long GREAT_RANGE = bms.BMS_RESOLUTION / 8;        //GREATと判定する中心からの範囲(前後合わせて24分音符内）
        long GOOD_RANGE = bms.BMS_RESOLUTION / 4;          //GOODと判定する中心からの範囲（前後合わせて８分音符以内）
        long BAD_RANGE = bms.BMS_RESOLUTION / 2;            //BADと判定する中心からの範囲
        long POOR_RANGE = bms.BMS_RESOLUTION ;           //POOR判定する中心からの範囲

        //全チャンネル分を処理 →　左足の処理
        //for(int j = 0; j<6; j++){　　
        int j= 0;
        for(int i=iStartNum[j+0x11+0x20]; i<bms.GetObjectNum(0x11+j);i++) {
            BMSDATA b=bms.GetObje(0x11+j,i);
            //System.out.println("j="+j+" i="+i+" lTime="+b.lTime+" now_count="+now_count);
            if(b.bFlag == TRUE){
                //まだ未判定オブジェクトなら
                if(b.lTime < (now_count-BAD_RANGE)){
                    //System.out.println("見逃し");
                    //良判定を超えたら見逃し扱いとする
                    b.bFlag = FALSE;
                    //判定オブジェをその次からに変更
                    iStartNum[j+0x11+0x21]= i+1;

                    bms.SetObje(0x11+j,i,b);
                    //次のオブジェをチェック
                    continue;
                }

                //オブジェが判定外なら抜ける
                if((now_count+POOR_RANGE)<b.lTime){
                    break;
                }

                //オブジェが判定内ならキーが押された瞬間かをチェック
                if(press[j]){
                    //キーが押した瞬間なら精度判定
                    long sub = abs(now_count - b.lTime);
                    //System.out.println("精度判定");
                    int jadge = 0;
                    if(sub<= GREAT_RANGE) {
                        jadge = 3;
                        mad.count_best_l ++;
                        System.out.println("L GREAT!");
                    }else if(sub <= GOOD_RANGE) {
                        jadge = 2;
                        mad.count_over_l++;
                        System.out.println("L GOOD!");

                    }else if(sub <= BAD_RANGE){
                        jadge = 1;
                        mad.count_under_l++;
                        System.out.println("L BAD!");
                    }

                    if(jadge >= 1){
                        // 所属親アクティビティを取得
                        AncleExerciseActivity activity = (AncleExerciseActivity) getActivity();
                        int moveangle = (int)activity.getMoveangle(devicenumber_lefttoe);

                        int angle = 0;

                        if(moveangle <=10)angle = 0;
                        else if(moveangle <=20)angle = 1;
                        else if(moveangle <= 30)angle = 2;
                        else if(moveangle <=40)angle = 3;
                        if(moveangle > 40)angle = 4;
                        //花火を表示
                        effect(devicenumber_lefttoe,jadge,angle);

                        //オブジェクトを処理
                        b.bFlag = FALSE;
                        bms.SetObje(0x11+j,i,b);
                        //判定オブジェクトをその次からに変更
                        iStartNum[j+0x11+0x20]=i+1;

                        //CancelAnimation();
                    }
                }
            }
        }

        // }

        //右足の処理
        //for(int j = 0; j<6; j++){　　
        j= 3;
        for(int i=iStartNum[j+0x11+0x20]; i<bms.GetObjectNum(0x11+j);i++) {
            BMSDATA b=bms.GetObje(0x11+j,i);
            System.out.println("j="+j+" i="+i+" lTime="+b.lTime+" now_count="+now_count);
            if(b.bFlag == TRUE){
                //まだ未判定オブジェクトなら
                if(b.lTime < (now_count-BAD_RANGE)){
                    System.out.println("R 見逃し");
                    //良判定を超えたら見逃し扱いとする
                    b.bFlag = FALSE;

                    //判定オブジェをその次からに変更
                    iStartNum[j+0x11+0x21]= i+1;

                    bms.SetObje(0x11+j,i,b);
                    //次のオブジェをチェック
                    continue;
                }

                //オブジェが判定外なら抜ける
                if((now_count+POOR_RANGE)<b.lTime){
                    break;
                }

                //オブジェが判定内ならキーが押された瞬間かをチェック
                if(press[j]){
                    //キーが押した瞬間なら精度判定
                    long sub = abs(now_count - b.lTime);
                    //System.out.println("精度判定");
                    int jadge = 0;
                    if(sub<= GREAT_RANGE) {
                        jadge = 3;
                        mad.count_best_r ++;
                        System.out.println("R GREAT! j="+j+" i="+i+" lTime="+b.lTime+" now_count="+now_count);
                    }else if(sub <= GOOD_RANGE) {
                        jadge = 2;
                        mad.count_over_r ++;     //判定方法変更
                        System.out.println("R GOOD! j="+j+" i="+i+" lTime="+b.lTime+" now_count="+now_count);

                    }else if(sub <= BAD_RANGE){
                        jadge = 1;
                        mad.count_under_r ++;
                        System.out.println("R BAD! j="+j+" i="+i+" lTime="+b.lTime+" now_count="+now_count);
                    }

                    if(jadge >= 1){
                        // 所属親アクティビティを取得
                        AncleExerciseActivity activity = (AncleExerciseActivity) getActivity();
                        int moveangle = (int)activity.getMoveangle(devicenumber_righttoe);

                        int angle = 0;

                        if(moveangle <=10)angle = 0;
                        else if(moveangle <=20)angle = 1;
                        else if(moveangle <= 30)angle = 2;
                        else if(moveangle <=40)angle = 3;
                        if(moveangle > 40)angle = 4;

                        //花火を表示
                        effect(devicenumber_righttoe,jadge,angle);

                        //オブジェクトを処理
                        b.bFlag = FALSE;
                        bms.SetObje(0x11+j,i,b);
                        //判定オブジェクトをその次からに変更
                        iStartNum[j+0x11+0x20]=i+1;

                        //CancelAnimation();
                    }
                }
            }
        }



        //描画処理
       // System.out.println("描画処理");
        for(int k=0;k<6;k++) {
            for (int i = iStartNum[0x11 + index[k]]; i < bms.GetObjectNum(0x11 + index[k]); i++) {
                BMSDATA b = bms.GetObje(0x11 + index[k], i);
                // System.out.println("k="+k+" i="+i+" lTime="+b.lTime+" now_count="+now_count);
                //判定ラインより下ならば表示はせず、次回からその次の小節から参照する
                if (b.lTime < now_count) {
                   // System.out.println("b.lTime < now_count! i="+i);
                    iStartNum[index[k] + 0x11] = i + 1;
                    continue;
                }

                //画面の上より外ならば、その先は、すべて描画スキップ
                //1小節前に表示するとすると　9600以上離れていればbreak
                if (b.lTime > (now_count + 9600)) break;


                //画面内に初めて入った
                if (b.bDoAnimation == TRUE) {
                    //アニメーション開始
                    if (k == 0) {   //左足
                       // System.out.println("アニメーションスタートl");
                        setAnimation();
                    } else if (k == 3) {         //右足
                       // System.out.println("アニメーションスタートr");
                        setAnimationr();
                    }
                    b.bDoAnimation = FALSE;
                    bms.SetObje(0x11 + index[k], i, b);
                }
            }
        }


        //System.out.println("--GameRun()");

        return 0;
    }
    //アニメーション制御　（左足）
    private void setAnimation(){

        //System.out.println("++AncleTrainFragment::setAnimation()");
        //FragmentActivity activity = getActivity();
        // 所属親アクティビティを取得
        AncleExerciseActivity activity = (AncleExerciseActivity) getActivity();


        View view = mInflater.inflate(R.layout.fragment_ancleexercise, mcontainer, false);

        //中央下側の足のイラストを描画
        ImageView imageViewl = new ImageView(activity);
        //ImageView imageViewr = new ImageView(activity);


        //画像指定
        imageViewl.setImageResource(R.drawable.foot_left);
        //imageViewr.setImageResource(R.drawable.ancleicon);


       // mlayoutl.setBackgroundColor(Color.argb(0xff, 0xaa, 0xcc, 0xff));

        // PropertyValuesHolderを使ってＸ軸方向移動範囲のpropertyを保持
        int width=mlayoutl.getWidth();
        int height=mlayoutl.getHeight();

        int imageWith = 370;  //この数字も本当は、きちんと取得すべき　ひとまず仮の値
        int imageHeight = 370;

        RelativeLayout.LayoutParams layoutParams= new RelativeLayout.LayoutParams(imageWith,imageHeight);
        //layoutParams.leftMargin = 300;
        //layoutParams.topMargin = 300;
        layoutParams.leftMargin = 0;
        layoutParams.topMargin = 0;
        imageViewl.setLayoutParams(layoutParams);
       // imageViewr.setLayoutParams(layoutParams);



        mlayoutl.addView(imageViewl);
        //mlayoutr.addView(imageViewr);


        PropertyValuesHolder vhX = PropertyValuesHolder.ofFloat(
                "translationX",
                0.0f,
                width-260 );   //中央の足と合わせるため微調整

        // PropertyValuesHolderを使ってＹ軸方向移動範囲のpropertyを保持
        PropertyValuesHolder vhY = PropertyValuesHolder.ofFloat(
                "translationY",
                0.0f,
                height-350 );  //中央の足と合わせるため微調整

        // PropertyValuesHolderを使って回転範囲のpropertyを保持
        PropertyValuesHolder vhRotaion = PropertyValuesHolder.ofFloat(
                "rotation",
                0.0f,
                0.0f );

        // ObjectAnimatorにセットする
        // ObjectAnimatorにセットする
        ObjectAnimator mobjectAnimatorl = ObjectAnimator.ofPropertyValuesHolder(
                imageViewl,
                vhX ,
                vhY ,
                vhRotaion );



        // 再生時間を設定
        int duration = (int)(4*60*1000/bms.mBH.fBpm);
        //int duration = 3000;
        //System.out.println("duration="+duration);
        mobjectAnimatorl.setDuration(duration);


        // リスナーを設定
        mobjectAnimatorl.addListener(this);



        // アニメーションを開始する
        mobjectAnimatorl.start();


    }

    private void CancelAnimation(Animator animator){

        // View imageview = findViewById(R.id.image_View);
        // imageview.setVisibility(View.INVISIBLE);
        animator.cancel();


    }
    //アニメーション制御  (右足）
    private void setAnimationr(){

        //System.out.println("++AncleTrainFragment::setAnimationr()");
        //FragmentActivity activity = getActivity();
        // 所属親アクティビティを取得
        AncleExerciseActivity activity = (AncleExerciseActivity) getActivity();


        View view = mInflater.inflate(R.layout.fragment_ancleexercise, mcontainer, false);

        //右足のイラストを描画
        ImageView imageViewr = new ImageView(activity);


        //画像指定
        imageViewr.setImageResource(R.drawable.foot_right);


        // mlayoutl.setBackgroundColor(Color.argb(0xff, 0xaa, 0xcc, 0xff));

        // PropertyValuesHolderを使ってＸ軸方向移動範囲のpropertyを保持
        int width=mlayoutl.getWidth();
        int height=mlayoutl.getHeight();

        int imageWith = 370;  //この数字も本当は、きちんと取得すべき　ひとまず仮の値
        int imageHeight = 370;

        RelativeLayout.LayoutParams layoutParams= new RelativeLayout.LayoutParams(imageWith,imageHeight);
        //layoutParams.leftMargin = 300;
        //layoutParams.topMargin = 300;
        layoutParams.leftMargin = 0;
        layoutParams.topMargin = 0;

         imageViewr.setLayoutParams(layoutParams);

        mlayoutr.addView(imageViewr);

        // PropertyValuesHolderを使ってＸ軸方向移動範囲のpropertyを保持
        PropertyValuesHolder vhX = PropertyValuesHolder.ofFloat(
                "translationX",
                width-260,
                0 );

        // PropertyValuesHolderを使ってＹ軸方向移動範囲のpropertyを保持
        PropertyValuesHolder vhY = PropertyValuesHolder.ofFloat(
                "translationY",
                0.0f,
                height-350 );

        // PropertyValuesHolderを使って回転範囲のpropertyを保持
        PropertyValuesHolder vhRotaion = PropertyValuesHolder.ofFloat(
                "rotation",
                0.0f,
                0.0f );

        // ObjectAnimatorにセットする
        // ObjectAnimatorにセットする
        ObjectAnimator objectAnimatorr = ObjectAnimator.ofPropertyValuesHolder(
                imageViewr,
                vhX ,
                vhY ,
                vhRotaion );


        // 再生時間を設定 3000msec=3sec
        //objectAnimatorr.setDuration(3000);
        // 再生時間を設定
        int duration = (int)(4*60*1000/bms.mBH.fBpm);
        //System.out.println("duration="+duration);
        objectAnimatorr.setDuration(duration);


        // リスナーを設定
        objectAnimatorr.addListener(this);

        // アニメーションを開始する
        objectAnimatorr.start();
    }

    private void startTimer(){
        //タイマー実行

        mTimeLeftInMillis = bms.GetSecondFromBar(bms.mBH.lEndBar)*1000;
        //System.out.println("++startTimer("+mTimeLeftInMillis+")");
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis,100) {
            @Override
            public void onTick(long millisUntilFinished) {
              //  System.out.println("onTick()");
                mTimeLeftInMillis = millisUntilFinished;
                GameRun(FALSE);
            }
            @Override
            public void onFinish() {
                mTimerRunning = false;

            }
        }.start();

        mTimerRunning = true;

    }


    // アクションバーのボタンを押した時の処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // 戻るボタン「←」を押した時android.R.id.homeに値が入る
            case android.R.id.home:
                // 遷移前に表示していたFragmentに戻る処理を実行
                getFragmentManager().popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    public void setrightAncleclicke(){
        buttonGoodr.performClick();

    }

    public void setleftAncleclicke(){
        buttonGood.performClick();

    }
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        // 実装されてなかったらException吐かせて実装者に伝える
        if (!(activity instanceof AncleExerciseFragment.AncleExerciseFragmentListener)) {
            throw new UnsupportedOperationException(
                    "Listener is not Implementation.");
        } else {
            // ここでActivityのインスタンスではなくActivityに実装されたイベントリスナを取得
            listener = (AncleExerciseFragment.AncleExerciseFragmentListener) activity;
        }

    }

    public void getExData(AncleexData ad){
        ad.count_best_l =  mad.count_best_l;
        ad.count_best_r = mad.count_best_r;
        ad.count_over_l = mad.count_over_l;
        ad.count_over_r = mad.count_over_r;
        ad.count_under_l = mad.count_under_l;
        ad.count_under_r = mad.count_under_r;
        ad.duration = mad.duration;
    }
}
