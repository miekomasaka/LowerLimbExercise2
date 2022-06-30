package com.example.lowerlimbexercise;

import android.app.Activity;

public enum AncleExerciseFinnishedFragmentEvent {
        EVENT1 {
            @Override
            public void apply(Activity activity) {
                // Fragment→ActivityのActivity側の処理とか
            }
        },
        EVENT2 {
            @Override
            public void apply(Activity activity) {
                // Fragment→ActivityのActivity側の処理とか
            }
        },
        EVENT3 {
            @Override
            public void apply(Activity activity) {
                // Fragment→ActivityのActivity側の処理とか
            }
        };

        abstract public void apply(Activity activity);

}
