package com.shbj.refreashrvdemo;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTabHost fthTab=findViewById(R.id.fth_tab);
        fthTab.setup(this,getSupportFragmentManager(),R.id.fl_container);
        fthTab.addTab(fthTab.newTabSpec("Demo1").setIndicator("Demo1"),Demo1Fragment.class,null);
        fthTab.addTab(fthTab.newTabSpec("Demo2").setIndicator("Demo2"),Demo2Fragment.class,null);
    }
}
