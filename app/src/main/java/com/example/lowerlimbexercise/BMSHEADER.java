package com.example.lowerlimbexercise;

public class BMSHEADER {
    public static final int BMS_MAXBUFFER = 16*16;

    long lPlayer;       //プレイモード
    String  mGenre;     //データのジャンル
    String  mTitle;     //データのタイトル
    String mArtist;     //データの製作者
    float fBpm;         //初期テンポ
    String mMidifile;   //バックグラウンドで流すMIDIファイル
    int     lPlayLebel; //ゲームの難易度
    int     lWavVol;    //音量を元の何％にするか
    int     lTotal;     //ゲージ増量
    String mStagePic;       //曲開始時に表示する画像
    int[] fBpmIndex = new int[ BMS_MAXBUFFER];       //店舗インデックス（初期値は120)
    int lEndBar;            //終了小節
    int lMaxCOunt;          //最大カウント数
}
