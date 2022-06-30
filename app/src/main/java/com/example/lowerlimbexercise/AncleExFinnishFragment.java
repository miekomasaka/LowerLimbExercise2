package com.example.lowerlimbexercise;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

public class AncleExFinnishFragment extends Fragment{
    private AncleExFinnishFragment.AncleExFinnishFragmentListener listener = null;

    public interface AncleExFinnishFragmentListener {
        void onAncleExFinnishFragmentEvent(AncleExFinnishFragmentEvent event);
    }

        @Override
        public View onCreateView(
                LayoutInflater inflater,
                ViewGroup container,
                Bundle savedInstanceState) {

            System.out.println("AncleExFinnishFragment::onCreateView()");
            View view = inflater.inflate(R.layout.fragment_ancleexfinnished, container, false);

            ImageView otsukareImage = view.findViewById(R.id.otsukare_View);
            otsukareImage.setImageResource(R.drawable.otsukare);

            //Animation anim = AnimationUtils.loadAnimation(this,R.anim.animation_otsukare);
            //UttiImage.startAnimation(anim);
            Animation animation_otsukare = AnimationUtils.loadAnimation(getContext(),R.anim.animation_otsukare);

            animation_otsukare.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationStart(Animation a) {
                    Log.d("debug", "---- animation start listener called"  );

                }
                public void onAnimationRepeat(Animation a) {}
                public void onAnimationEnd(Animation a) {
                    Log.d("debug", "---- animation end listener called"  );
                    listener.onAncleExFinnishFragmentEvent(AncleExFinnishFragmentEvent.EVENT1);
                }
            });

            otsukareImage.startAnimation(animation_otsukare);


            return view;
        }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        // 実装されてなかったらException吐かせて実装者に伝える
        if (!(activity instanceof AncleExFinnishFragment.AncleExFinnishFragmentListener)) {
            throw new UnsupportedOperationException(
                    "Listener is not Implementation.");
        } else {
            // ここでActivityのインスタンスではなくActivityに実装されたイベントリスナを取得
            listener = (AncleExFinnishFragment.AncleExFinnishFragmentListener) activity;
        }

    }

}
