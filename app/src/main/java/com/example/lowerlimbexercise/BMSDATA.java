package com.example.lowerlimbexercise;

public class BMSDATA {
    long    lTime;      //このデータの開始位置（BMSカウント値）
    long    lData;     //鳴らすデータ(ox01～0xff)
    float    fData;     //少数値データ（テンポ用）
    Boolean  bFlag;     //アプリが使用できる任意の変数
    boolean bDoAnimation; //アニメーションを開始したらFALSE
}
