package com.example.lowerlimbexercise;

import static java.lang.Boolean.TRUE;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class AncleExRadyFragment extends Fragment {

    private AncleExRadyFragment.AncleExRadyFragmentListener listener = null;

    public interface AncleExRadyFragmentListener {
        void onAncleExRadyFragmentEvent(AncleExRadyFragmentEvent event);
    }

    //ProgressDialog progressDialog;
    public class MyAsyncTask extends AsyncTask<Object,Integer,Integer> {
        private Context context;
        private ProgressDialog dialog;

        // コンストラクタ
        protected  MyAsyncTask(Context context) {
            this.context = context;
        }

        // バックグラウンドの処理前
        @Override
        protected void onPreExecute() {

            // ProgressDialogを生成する
            dialog = new ProgressDialog(context);
            dialog.setTitle("センサーデータ取得");
            dialog.setMessage("センサーから情報を取得しています。\n そのまま動かずに、しばらくお待ち下さい。");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setCancelable(true);

            // キャンセルボタン以外のキャンセルイベント
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // キャンセルする
                    cancel(true);
                }
            });

            // キャンセルボタンによるキャンセルイベント
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // キャンセルする
                            cancel(true);
                        }
                    });
            dialog.setMax(100);
            dialog.setProgress(0);
            dialog.show();
        }

        // バックグラウンド処理
        @Override
        protected Integer doInBackground(Object[] object) {



            // mHandler.sendMessage(msg);
           /* case Constants.MESSAGE_INITIALIZE:
            switch (msg.arg1) {
                case BluetoothChatService.STATE_INITSTART:*/

            for (int i = 1; i <= 100; i++) {
                try {
                    // キャンセル時はループを抜ける
                    if (isCancelled()) break;

                    // プログレスバーの処理
                    publishProgress(i);

                    // なんらかの重い処理
                    Thread.sleep(10);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return 100;
        }

        // バックグラウンドの処理後
        @Override
        protected void onPostExecute(Integer o) {
            dialog.dismiss();
            Log.i("MyTAG", "処理が完了しました。");

            ((AncleExerciseActivity) getActivity()).setDefaultPosition(Boolean.FALSE);
            listener.onAncleExRadyFragmentEvent(AncleExRadyFragmentEvent.EVENT1);
        }

        // プログレスバー
        @Override
        protected void onProgressUpdate(Integer[] values) {
            dialog.setProgress(values[0]);
        }

        // キャンセル
        @Override
        protected void onCancelled() {
            Log.i("MyTAG","キャンセルされました。");
        }
    }
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

        // ボタン要素を取得
        Button bt1 = view.findViewById(R.id.button_exercise);
        //Button bt2 = view.findViewById(R.id.bt2);

        // ①ボタンをクリックした時の処理
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((AncleExerciseActivity) getActivity()).setDefaultPosition(TRUE);

                MyAsyncTask my = new MyAsyncTask(getActivity());
                my.execute();

                // SubFragment1に遷移させる
                //replaceFragment(new AncleTrainFragment());
            }
        });


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
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        // 実装されてなかったらException吐かせて実装者に伝える
        if (!(activity instanceof AncleExRadyFragment.AncleExRadyFragmentListener)) {
            throw new UnsupportedOperationException(
                    "Listener is not Implementation.");
        } else {
            // ここでActivityのインスタンスではなくActivityに実装されたイベントリスナを取得
            listener = (AncleExRadyFragment.AncleExRadyFragmentListener) activity;
        }

    }
    // 表示させるFragmentを切り替えるメソッドを定義（表示したいFragmentを引数として渡す）
    private void replaceFragment(Fragment fragment) {
        // フラグメントマネージャーの取得
        FragmentManager manager = getFragmentManager(); // アクティビティではgetSupportFragmentManager()?
        // フラグメントトランザクションの開始
        FragmentTransaction transaction = manager.beginTransaction();
        // レイアウトをfragmentに置き換え（追加）
        transaction.replace(R.id.activityAncleTrainMain, fragment);
        // 置き換えのトランザクションをバックスタックに保存する
        transaction.addToBackStack(null);
        // フラグメントトランザクションをコミット
        transaction.commit();
    }
}
