package com.lyxxcy.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.lyxxcy.db.UserProvider;
import com.lyxxcy.yyjq.AppManager;
import com.lyxxcy.yyjq.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class RegisterActivity extends Activity {

    private static final String[] sexarray = { "男", "女" };
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    Context context = RegisterActivity.this;
    public static String user;

    private EditText username, email, phone, student_id, password, password_again, sign;
    private Button btn_login;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this);
        setContentView(R.layout.activity_register);

        spinner = (Spinner) findViewById(R.id.Spinnersex);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sexarray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setVisibility(View.VISIBLE);

        findViewById();
        btn_login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                long i_id = calendar.getTime().getTime();
                String i_name = username.getText().toString();
                String i_sex = spinner.getSelectedItem().toString();
                String i_email = email.getText().toString();
                String i_phone = phone.getText().toString();
                String i_student_id = student_id.getText().toString();
                String i_password = password.getText().toString();
                String i_password_again = password_again.getText().toString();
                String i_sign = sign.getText().toString();
                String i_time = getTime();

                String judge = "";
                ContentResolver crp = context.getContentResolver();
                Cursor cp1 = crp.query(UserProvider.CONTENT_URI,
                        new String[] { UserProvider.ID + " as _id", UserProvider.USER_NAME }, "user_name =?",
                        new String[] { i_name }, null);
                for (cp1.moveToFirst(); !cp1.isAfterLast(); cp1.moveToNext()) {
                    judge = cp1.getString(1);
                }

                boolean flag = (i_name.equals("")) || (i_phone.equals("")) || (i_student_id.equals(""))
                        || ((i_password.equals("")));

                if (flag) {
                    Dialog dialog1 = new AlertDialog.Builder(RegisterActivity.this).setMessage("存在必填项为空")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog1, int which) {
                                    dialog1.dismiss();
                                }
                            }).create();

                    dialog1.show();
                } else if (judge.equals("")) {
                    if ((password == null) || !i_password.equals(i_password_again)) {
                        showHintDialog("密码错误信息", "密码不一致");
                        return;
                    }
                    UserProvider.addUser(context, i_id, i_name, i_sex, i_email, i_phone, i_student_id, i_password_again,
                            i_sign, i_time);

                    Dialog userAddDialog = new AlertDialog.Builder(RegisterActivity.this).setTitle("您的ID : " + i_id)
                            .setNeutralButton("知道了，返回登录", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                    finish();
                                }
                            }).create();
                    userAddDialog.show();
                } else {
                    Dialog dialog1 = new AlertDialog.Builder(RegisterActivity.this).setMessage("用户昵称已存在，试试别的吧")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog1, int which) {
                                    dialog1.dismiss();
                                }
                            }).create();

                    dialog1.show();
                }

            }
        });

    }

    private void findViewById() {
        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.phone);
        student_id = (EditText) findViewById(R.id.student_id);
        password = (EditText) findViewById(R.id.password);
        password_again = (EditText) findViewById(R.id.password_again);
        sign = (EditText) findViewById(R.id.sign);
        btn_login = (Button) findViewById(R.id.login);
    }

    private String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");
        Date curDate = new Date();
        String str = format.format(curDate);
        return str;
    }

    private void showHintDialog(String title, String msg) {
        new AlertDialog.Builder(RegisterActivity.this).setTitle(title).setMessage(msg)
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

}
