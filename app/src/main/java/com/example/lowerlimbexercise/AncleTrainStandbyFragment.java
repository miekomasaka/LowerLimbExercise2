package com.example.lowerlimbexercise;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class AncleTrainStandbyFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_ancleexrady, container, false);

        // 所属親アクティビティを取得
        AncleExerciseActivity activity = (AncleExerciseActivity) getActivity();
        // アクションバーにタイトルをセット
        activity.setTitle("サブフラグメント1");
        // 戻るボタンを表示する
        activity.setupBackButton(true);

        // この記述でフラグメントでアクションバーメニューが使えるようになる
        setHasOptionsMenu(true);

        return view;
    }

    // アクションバーボタンを押した時の処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // android.R.id.homeで戻るボタン「←」を押した時の動作を検知
            case android.R.id.home:
                // 遷移前に表示していたFragmentに戻る処理を実行
                getFragmentManager().popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
