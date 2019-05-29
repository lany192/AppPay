package com.github.lany192.pay.sample;

import com.lany.box.Box;

public class MyApp extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Box.of().init(this);
    }
}
