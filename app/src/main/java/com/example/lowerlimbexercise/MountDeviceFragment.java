package com.example.lowerlimbexercise;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class MountDeviceFragment extends Fragment {

    private MountDeviceFragmentListener listener = null;

    public interface MountDeviceFragmentListener {
        void onMountDeviceFragmentEvent(MountDeviceFragmentEvent event);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // フラグメントで表示する画面をlayoutファイルからインフレートする
        View view = inflater.inflate(R.layout.fragment_mountdevice, container, false);

        // 所属している親アクティビティを取得
        AncleExerciseActivity activity = (AncleExerciseActivity) getActivity();
        // アクションバーにタイトルをセット
        activity.setTitle("センサ接続");
        // 戻るボタンは非表示にする（MainFragmentでは戻るボタン不要）
        // ここをfalseにしておかないとサブフラグメントから戻ってきた際に戻るボタンが表示されたままになってしまう
        activity.setupBackButton(false);

        // ボタン要素を取得
        Button bt1 = view.findViewById(R.id.md_startbutton);
        //Button bt2 = view.findViewById(R.id.bt2);

        // ①ボタンをクリックした時の処理
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // SubFragment1に遷移させる
                //replaceFragment(new AncleExRadyFragment());
                listener.onMountDeviceFragmentEvent(MountDeviceFragmentEvent.EVENT1);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        // 実装されてなかったらException吐かせて実装者に伝える
        if (!(activity instanceof MountDeviceFragmentListener)) {
            throw new UnsupportedOperationException(
                    "Listener is not Implementation.");
        } else {
            // ここでActivityのインスタンスではなくActivityに実装されたイベントリスナを取得
            listener = (MountDeviceFragmentListener) activity;
        }

    }

}
