package com.example.lowerlimbexercise;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.Boolean.getBoolean;

public class CBmsPro {

    //定数定義
    public static final int BMS_MAXBUFFER = 16 * 16;
    public static final int BMS_RESOLUTION = 9600;  //1小節のカウント値
    public static final int BMS_TEMPO = 0x11;
    int[] iBmsData = new int[BMS_MAXBUFFER];
    BMSBAR[] mBmsBar = new BMSBAR[1000 + 1];
    BMSHEADER mBH = new BMSHEADER();
    String[] mWaveFile = new String[BMS_MAXBUFFER];

    List<ListData> objects = new ArrayList<ListData>();

    ArrayList<BMSDATA> pBmsData11 = new ArrayList<>();      //左足のデータは、cc=11
    ArrayList<BMSDATA> pBmsData14 = new ArrayList<>();      //右足のデータは、cc=15

    SharedPreferences sharedPref;

    public CBmsPro(){

        for(int i=0;i<mBmsBar.length;i++){
            mBmsBar[i] = new BMSBAR();
        }

    }
    public void Clear() {
        for (int i = 0; i < BMS_MAXBUFFER; i++)
            iBmsData[i] = 0;

        for (int i = 0; i < 1001; i++)
            mBmsBar[i].fScale = 1.0f;
    }

    //時間からBMSカウント値を計算
    public long GetCountFromeTime(long sec) {
        long cnt = 0;
        double t = 0;
        double bpm = 130;

       // System.out.println("++GetCountFromeTime(sec="+sec+")");

        //シンプルにカウンタに変換 (fData非対応）
        cnt= (long) ((sec)*mBH.fBpm/(4*60)*9600/1000);



        return cnt;
    }

    public Boolean readLine(Context context,String filename) {
        //ヘッダ読み込み
        if (!LoadHeader(context,filename)) {
            return FALSE;
        }

        //実データ読み込み
        if (!LoadBmsData(context,filename)){
            return FALSE;
        }
        return TRUE;
    }

    private Boolean LoadHeader(Context context,String filename) {
        int cmd;    //コマンド番号

        AssetManager assetManager = context.getResources().getAssets();
        System.out.println("++LoadHeader("+filename+")");
        try {
            // CSVファイルの読み込み
            InputStream inputStream = assetManager.open(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferReader.readLine()) != null) {
               // System.out.println("readLine line="+line);
                //コマンド以外なら次の行へ
                if (!line.startsWith("#")) continue;

                //コマンドの種類を取得
                cmd = GetCommand(line);

                //不明なコマンドならスキップ
                if (cmd <= -2) continue;

                //読み取った情報を　ｍBHクラスにセット
                switch (cmd) {
                    default:
                        break;
                    case -1:
                        //小節番号の取得
                        int Barnum = Integer.parseInt(line.substring(1, 4));
                        //チャンネル番号の取得
                        int ch = Integer.parseInt(line.substring(4, 6), 16);//16進数表記なので
                        if (mBH.lEndBar < Barnum) mBH.lEndBar = Barnum;
                        break;
                    case 0: //PLAYER
                        System.out.println("PLAYER"+line);
                        String[] RowData = line.split(" ");
                        mBH.lPlayer = Integer.parseInt(RowData[1]);
                        break;

                    case 4:  //BPM
                        System.out.println("BPM"+line);
                        String[] bpm = line.split(" ");
                        mBH.fBpm = Integer.parseInt(bpm[1]);
                        break;
                    case 11:   //WAV
                        System.out.println("WAV"+line);
                        String[] wavefile = line.split(" ");
                        mWaveFile[Integer.parseInt(wavefile[0].substring(4, 6), 16)] = wavefile[1];
                        break;
                }
            }

            //最後の小説にもデータ後sン在するためその次の小節を終端小節とする
            mBH.lEndBar++;

            //すべての小節情報を算出　（小節倍データ非対応）
            int cnt = 0;
            for (int i = 0; i < mBH.lEndBar; i++) {
                //小節リスト加算
                mBmsBar[i].lTime = cnt;   //現在の小節開始カウントを記録
                mBmsBar[i].lLength = BMS_RESOLUTION;   //この小節の長さ

                //この小節のカウント数を加算して次の小節の開始カウントとする
                cnt += mBmsBar[i].lLength;
            }

            //最大カウントを保存
            mBH.lMaxCOunt = cnt;

            bufferReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("--LoadHeader()");
        return TRUE;
    }

    private boolean LoadBmsData(Context context,String filename) {
        int cmd;    //コマンド番号
        System.out.println("++LoadBmsData("+filename+")");
        AssetManager assetManager = context.getResources().getAssets();
        try {
            // CSVファイルの読み込み
            InputStream inputStream = assetManager.open(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferReader.readLine()) != null) {

                //コマンド以外なら次の行へ
                if (!line.startsWith("#")) continue;

                //コマンドの種類を取得
                cmd = GetCommand(line);

                //オブジェクト以外は、無視
                if (cmd != -1) continue;

                //小節番号の取得
                int Barnum = Integer.parseInt(line.substring(1, 4));

                //チャンネル番号の取得
                int ch = Integer.parseInt(line.substring(4, 6), 16);//16進数表記なので
                if (mBH.lEndBar < Barnum) mBH.lEndBar = Barnum;
                String[] RowData = line.split(":");

                if (RowData[1].length() <= 0) {
                    Log.d("debug", "データが定義されていない箇所が存在");
                    continue;
                }

                //データが偶数か
                if ((RowData[1].length() % 2) == 1) {
                    Log.d("debug", "データが奇数の箇所が存在");
                    continue;
                }

                //データ数
                int len = RowData[1].length() / 2;

                //現在の小節カウント値から１音符分のカウント値を算出
                double tick = mBmsBar[Barnum].lLength / len;

                //実データを追加
                for (int i = 0; i < len; i++) {
                    int data = Integer.parseInt(RowData[1].substring(i * 2, (i * 2 + 2)), 16);
                    if (data > 0) AddData(ch, (long) (mBmsBar[Barnum].lTime + (tick * i)), data);
                }

            }

            bufferReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("--LoadBmsData()");
        return TRUE;
    }

    private Boolean AddData(int ch, long cnt, long data) {
        //System.out.println("++AddData(ch="+ch+")");
        //チャンネル番号をチェック
        if (ch < 0 || ch > 255) return FALSE;

        //データがなければ何もしない
        if (data == 0) return TRUE;

        //データを追加
        //chは、11と15のみ使用
        switch (ch) {
            default:
                break;
            case 17:
                iBmsData[ch]++;
                BMSDATA tBMSData = new BMSDATA();
                tBMSData.bFlag = TRUE;
                tBMSData.lTime = cnt;
                tBMSData.lData = data;
                tBMSData.fData = (float) data;
                tBMSData.bDoAnimation = TRUE;
                pBmsData11.add(tBMSData);
                break;
            case 20:
                iBmsData[ch]++;
                BMSDATA tBMSData14 = new BMSDATA();
                tBMSData14.bFlag = TRUE;
                tBMSData14.lTime = cnt;
                tBMSData14.lData = data;
                tBMSData14.fData = (float) data;
                tBMSData14.bDoAnimation = TRUE;
                pBmsData14.add(tBMSData14);
                break;
        }
        //System.out.println("--AddData(ch="+ch+")");
        return TRUE;


    }

    //読み込んだ文字列がコマンドであるかをチェックする
    //コマンドであれば　コマンド番号を返す
    //nnncc形式であれば   -1
    //不明なコマンドであれば　-2 を返す

    public int GetCommand(String line) {
        String[] command = {"#PLAYER",
                "#GENRE",
                "#TITLE",
                "#ARTIST",
                "#BPM",
                "#MIDIFILE",
                "#PLAYLEVEL",
                "#RANK",
                "#VOLWAV",
                "#TOTAL",
                "#StageFile",
                "#WAV",
                "#BMP"};

        //コマンド検索
        for (int i = 0; i < command.length; i++) {
            if (line.startsWith(command[i])) return i;
        }

        //先頭が#nnncc形式か
        String[] result = line.split(":", 0);

        //文字数があっているか
        if (result[0].length() == 6) {
            String tmp = line.substring(1, 5);
            int nnncc = Integer.parseInt(tmp);
            if (((nnncc > 0) && (nnncc < 99999)) == TRUE) return -1;
        } else {
            return -2;
        }
        return -2;

    }

    public long GetMaxCount(){
        return mBH.lMaxCOunt;
    }

    public int GetObjectNum(int ch){
        return iBmsData[ch];
    }

    public BMSDATA GetObje(int ch, int num){
     //   System.out.println("++GetObje("+ch+","+num+")");
        if(ch == 0x11){
           return pBmsData11.get(num);
        }
        if(ch == 0x14){
            return pBmsData14.get(num);
        }
        return null;
    }

    public  void SetObje(int ch, int num ,BMSDATA b) {
     //   System.out.println("++SetObje(" + ch + "," + num + ")");
        if (ch == 0x11) {
            pBmsData11.set(num, b);
        }
        if (ch == 0x15) {
            pBmsData14.set(num, b);
        }
    }



    public BMSBAR GetBar(int num){
        return mBmsBar[num];
    }
    public int GetBarNum(){
        return mBH.lEndBar+1;
    }

    public long GetSecondFromBar(long bar){
        return (long)(bar/(mBH.fBpm/4)*60);
    }
}
