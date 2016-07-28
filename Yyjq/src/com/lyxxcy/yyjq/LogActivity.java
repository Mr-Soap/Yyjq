package com.lyxxcy.yyjq;

import com.lyxxcy.db.UserInfo;
import com.lyxxcy.db.UserProvider;
import com.lyxxcy.ui.RegisterActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LogActivity extends Activity implements OnClickListener {
    private EditText et_name, et_pass;
    private Button mLoginButton, mLogoutButton, mLoginError, mRegister;
    int selectIndex = 1;
    int tempSelect = selectIndex;
    boolean isReLogin = false;

    // private String [] coutry_phone_sn_array,coutry_name_array;
    public final static int LOGIN_ENABLE = 0x01; //注册完毕了
    public final static int LOGIN_UNABLE = 0x02; //注册完毕了
    public final static int PASS_ERROR = 0x03; //注册完毕了
    public final static int NAME_ERROR = 0x04; //注册完毕了

    Context context;
    public static UserInfo user;

    final Handler UiMangerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case LOGIN_ENABLE:
                    mLoginButton.setClickable(true);
                    //       mLoginButton.setText(R.string.login);
                    break;
                case LOGIN_UNABLE:
                    mLoginButton.setClickable(false);
                    break;
                case PASS_ERROR:

                    break;
                case NAME_ERROR:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private Button bt_username_clear;
    private Button bt_pwd_clear;
    private Button bt_pwd_eye;
    private TextWatcher username_watcher;
    private TextWatcher password_watcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  requestWindowFeature(Window.FEATURE_NO_TITLE);      
        //  //不显示系统的标题栏          
        //  getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //       WindowManager.LayoutParams.FLAG_FULLSCREEN );
        AppManager.getInstance().addActivity(this);
        setContentView(R.layout.activity_login);
        context = LogActivity.this;
        et_name = (EditText) findViewById(R.id.username);
        et_pass = (EditText) findViewById(R.id.password);

        bt_username_clear = (Button) findViewById(R.id.bt_username_clear);
        bt_pwd_clear = (Button) findViewById(R.id.bt_pwd_clear);
        bt_pwd_eye = (Button) findViewById(R.id.bt_pwd_eye);
        bt_username_clear.setOnClickListener(this);
        bt_pwd_clear.setOnClickListener(this);
        bt_pwd_eye.setOnClickListener(this);
        initWatcher();
        et_name.addTextChangedListener(username_watcher);
        et_pass.addTextChangedListener(password_watcher);

        mLoginButton = (Button) findViewById(R.id.login);
        mLogoutButton = (Button) findViewById(R.id.logout_xcy);
        mLoginError = (Button) findViewById(R.id.login_error);
        mRegister = (Button) findViewById(R.id.register);
        mLoginButton.setOnClickListener(this);
        mLogoutButton.setOnClickListener(this);
        mLoginError.setOnClickListener(this);
        mRegister.setOnClickListener(this);

    }

    /**
     * 手机号，密码输入控件公用这一个watcher
     */
    private void initWatcher() {
        username_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                et_pass.setText("");
                if (s.toString().length() > 0) {
                    bt_username_clear.setVisibility(View.VISIBLE);
                } else {
                    bt_username_clear.setVisibility(View.INVISIBLE);
                }
            }
        };

        password_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    bt_pwd_clear.setVisibility(View.VISIBLE);
                } else {
                    bt_pwd_clear.setVisibility(View.INVISIBLE);
                }
            }
        };
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.login:
                String name = et_name.getText().toString();
                String password = et_pass.getText().toString();
                user = new UserInfo();
                if (verifyUser(name, password) == 0) { // 存在
                    long id = 0;
                    ContentResolver crp = context.getContentResolver();
                    Cursor cp1 = crp.query(UserProvider.CONTENT_URI,
                            new String[] { UserProvider.ID + " as _id", UserProvider.USER_NAME }, "user_name =?",
                            new String[] { name }, null);
                    for (cp1.moveToFirst(); !cp1.isAfterLast(); cp1.moveToNext()) {
                        id = cp1.getLong(0);
                    }

                    user.setName(name);
                    user.setPassword(password);
                    user.setId(id);

                    Intent intent = new Intent();
                    intent.setClass(LogActivity.this, HomeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", user);
                    intent.putExtras(bundle);
                    this.startActivity(intent);

                } else {
                    final Dialog dialog1 = new AlertDialog.Builder(LogActivity.this).setMessage("密码错误或用户不存在")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog1, int which) {
                                    dialog1.dismiss();
                                }
                            }).create();

                    dialog1.show();
                }

                break;

            case R.id.logout_xcy:
                finish();
                break;

            case R.id.login_error: //无法登陆(忘记密码了吧)
                //   Intent login_error_intent=new Intent();
                //   login_error_intent.setClass(LoginActivity.this, ForgetCodeActivity.class);
                //   startActivity(login_error_intent);
                break;
            case R.id.register: //注册新的用户
                Intent ir = new Intent(this, RegisterActivity.class);
                startActivity(ir);
                break;
            case R.id.bt_username_clear:
                et_name.setText("");
                et_pass.setText("");
                break;
            case R.id.bt_pwd_clear:
                et_pass.setText("");
                break;
            case R.id.bt_pwd_eye:
                if (et_pass.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    bt_pwd_eye.setBackgroundResource(R.drawable.button_eye_n);
                    et_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                } else {
                    bt_pwd_eye.setBackgroundResource(R.drawable.button_eye_n);
                    et_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                et_pass.setSelection(et_pass.getText().toString().length());
                break;
        }
    }

    /**
     * 监听Back键按下事件,方法2:
     * 注意:
     * 返回值表示:是否能完全处理该事件
     * 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (isReLogin) {
                Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
                mHomeIntent.addCategory(Intent.CATEGORY_HOME);
                mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                //                LoginActivity.this.startActivity(mHomeIntent);
            } else {
                //                LoginActivity.this.finish();
            }
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private int verifyUser(String username, String password) {
        UserInfo user = UserProvider.getUserFromName(context, username);
        if (user != null) {
            boolean re = (password != null) && password.equals(user.getPassword());
            if (re) {
                return 0; //存在
            } else {
                return 1; // 密码错误
            }

        } else {
            return 1; // 不存在
        }
    }

}
