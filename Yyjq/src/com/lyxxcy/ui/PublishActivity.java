package com.lyxxcy.ui;

import com.lyxxcy.yyjq.AppManager;
import com.lyxxcy.yyjq.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class PublishActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        AppManager.getInstance().addActivity(this);
        setContentView(R.layout.activity_publish);
    }
}