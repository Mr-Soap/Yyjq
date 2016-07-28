package com.lyxxcy.ui;

import com.lyxxcy.db.UserInfo;
import com.lyxxcy.db.UserProvider;
import com.lyxxcy.widgets.CustomScrollView;
import com.lyxxcy.yyjq.AppManager;
import com.lyxxcy.yyjq.R;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileActivity extends BaseActivity implements OnClickListener {

    private ImageView mBackgroundImageView = null;
    private Button mExitButton;
    private TextView username, profile_sign, profile_gra, profile_score;
    private CustomScrollView mScrollView = null;

    private Intent mIntent = null;

    public static UserInfo user;
    private Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this);
        setContentView(R.layout.activity_profile);
        Intent intent = this.getIntent();
        user = (UserInfo) intent.getSerializableExtra("user");
        context = ProfileActivity.this;

        findViewById();
        initView();
    }

    protected void findViewById() {

        mBackgroundImageView = (ImageView) findViewById(R.id.profile_background_image);
        mScrollView = (CustomScrollView) findViewById(R.id.profile_scrollView);
        mExitButton = (Button) findViewById(R.id.profile_exit);
        username = (TextView) findViewById(R.id.username);
        profile_sign = (TextView) findViewById(R.id.profile_sign);
        profile_gra = (TextView) findViewById(R.id.profile_gra);
        profile_score = (TextView) findViewById(R.id.profile_score);

    }

    protected void initView() {
        mScrollView.setImageView(mBackgroundImageView);

        String sign = "";
        int gra = 0, score = 0;
        ContentResolver crp = context.getContentResolver();
        Cursor cp1 = crp.query(UserProvider.CONTENT_URI,
                new String[] { UserProvider.ID + " as _id", UserProvider.SIGN, UserProvider.CREDIT_LEVEL_EXCHANGE,
                        UserProvider.CREDIT_POINTS_EXCHANGE, UserProvider.CREDIT_LEVEL_TASK,
                        UserProvider.CREDIT_LEVEL_TASK },
                "_id =?", new String[] { user.getId() + "" }, null);
        for (cp1.moveToFirst(); !cp1.isAfterLast(); cp1.moveToNext()) {
            sign = cp1.getString(1);
            gra = cp1.getInt(2) + cp1.getInt(4);
            score = cp1.getInt(3) + cp1.getInt(5);
        }
        if (sign.equals("")) {
            sign = "这个人很懒==, 目前没有签名";
        }

        username.setText("用户昵称:   " + user.getName());
        profile_sign.setText("个性签名:   " + sign);
        profile_gra.setText("信用等级:   " + gra);
        profile_score.setText("信用积分:   " + score);

        mExitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.profile_login_button:
                //                mIntent = new Intent(ProfileActivity.this, LoginActivity.class);

                //                startActivityForResult(mIntent, LOGIN_CODE);
                break;

            case R.id.profile_more_button:
                //                mIntent = new Intent(ProfileActivity.this, MoreActivity.class);
                //                startActivity(mIntent);
                break;

            case R.id.profile_exit:

                //实例化SelectPicPopupWindow
                //                exit = new ExitView(ProfileActivity.this, itemsOnClick);
                //显示窗口
                //                exit.showAtLocation(ProfileActivity.this.findViewById(R.id.layout_profile),
                //                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置

                break;

            default:
                break;
        }
    }
}
