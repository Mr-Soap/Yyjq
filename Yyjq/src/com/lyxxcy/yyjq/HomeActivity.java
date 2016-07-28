package com.lyxxcy.yyjq;

import com.lyxxcy.db.UserInfo;
import com.lyxxcy.ui.ExchangeActivity;
import com.lyxxcy.ui.MessageActivity;
import com.lyxxcy.ui.ProfileActivity;
import com.lyxxcy.ui.PublishActivity;
import com.lyxxcy.ui.TaskActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

public class HomeActivity extends TabActivity {

    public static final String TAG = LogActivity.class.getSimpleName();

    private RadioGroup mTabButtonGroup;
    private TabHost mTabHost;
    public static UserInfo user;
    public static final String TAB_EXCHANGE = "EXCHANGE_ACTIVITY";
    public static final String TAB_TASK = "TASK_ACTIVITY";
    public static final String TAB_PUBLISH = "PUBLISH_ACTIVITY";
    public static final String TAB_MESSAGE = "MEAASGEACTIVITY";
    public static final String TAB_PROFILE = "PROFILE_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this);
        
        setContentView(R.layout.activity_main);
        
        Intent intent = this.getIntent();
        user = (UserInfo) intent.getSerializableExtra("user");

        Dialog dialog1 = new AlertDialog.Builder(HomeActivity.this)
                .setMessage("user is" + user.getName() + "user id is" + user.getId())
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {
                        dialog1.dismiss();
                    }
                }).create();

        dialog1.show();

        findViewById();
        initView();
    }

    private void findViewById() {
        mTabButtonGroup = (RadioGroup) findViewById(R.id.home_radio_button_group);
    }

    private void initView() {

        mTabHost = getTabHost();

        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);

        Intent i_exchange = new Intent(this, ExchangeActivity.class);
        i_exchange.putExtras(bundle);
        Intent i_task = new Intent(this, TaskActivity.class);
        i_task.putExtras(bundle);
        Intent i_publish = new Intent(this, PublishActivity.class);
        i_publish.putExtras(bundle);
        Intent i_message = new Intent(this, MessageActivity.class);
        i_message.putExtras(bundle);
        Intent i_profile = new Intent(this, ProfileActivity.class);
        i_profile.putExtras(bundle);

        mTabHost.addTab(mTabHost.newTabSpec(TAB_EXCHANGE).setIndicator(TAB_EXCHANGE).setContent(i_exchange));
        mTabHost.addTab(mTabHost.newTabSpec(TAB_TASK).setIndicator(TAB_TASK).setContent(i_task));
        mTabHost.addTab(mTabHost.newTabSpec(TAB_PUBLISH).setIndicator(TAB_PUBLISH).setContent(i_publish));
        mTabHost.addTab(mTabHost.newTabSpec(TAB_MESSAGE).setIndicator(TAB_MESSAGE).setContent(i_message));
        mTabHost.addTab(mTabHost.newTabSpec(TAB_PROFILE).setIndicator(TAB_PROFILE).setContent(i_profile));

        mTabHost.setCurrentTabByTag(TAB_EXCHANGE);
        
        RadioButton publish = (RadioButton) findViewById(R.id.btn_publish);
        
        //点击向上滑出选择界面
        publish.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(HomeActivity.this,SelectPicPopupWindow.class));  
                mTabHost.setCurrentTabByTag(TAB_PUBLISH);
			}
		});
        
        mTabButtonGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.btn_exchange:
                        mTabHost.setCurrentTabByTag(TAB_EXCHANGE);
                        break;

                    case R.id.btn_task:
                        mTabHost.setCurrentTabByTag(TAB_TASK);
                        break;

                    case R.id.btn_publish:
//                    	startActivity(new Intent(HomeActivity.this,SelectPicPopupWindow.class));  
//                      mTabHost.setCurrentTabByTag(TAB_PUBLISH);
                        break;

                    case R.id.btn_message:
                        mTabHost.setCurrentTabByTag(TAB_MESSAGE);
                        break;

                    case R.id.btn_profile:
                        mTabHost.setCurrentTabByTag(TAB_PROFILE);
                        break;

                    default:
                        break;
                }
            }
        });
    }

}
