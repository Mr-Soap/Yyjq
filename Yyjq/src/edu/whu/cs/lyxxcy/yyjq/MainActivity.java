package edu.whu.cs.lyxxcy.yyjq;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import edu.whu.cs.lyxxcy.db.UserDB;
import edu.whu.cs.lyxxcy.lib.ImageGridAdapter;
import edu.whu.cs.lyxxcy.lib.MultiColumnListView;

public class MainActivity extends Activity implements View.OnClickListener {

    public static MultiColumnListView mAdapterView = null;
    public static ArrayList<String> imageUrls = null;
    public static ImageGridAdapter adapter;

    private TextView txt_exchange;
    private TextView txt_task;
    private TextView txt_publish;
    private TextView txt_message;
    private TextView txt_profile;
    private MyFragment fg1, fg2, fg3, fg4, fg5;
    private FragmentManager fManager;

    private UserDB userDB;
    private SQLiteDatabase dbWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //        setContentView(R.layout.fg_exchange);

        userDB = new UserDB(this);
        dbWriter = userDB.getWritableDatabase();
        //        addDB();
        fManager = getFragmentManager();
        bindViews();
        //        txt_exchange.performClick(); //妯℃嫙涓�娆＄偣鍑伙紝鏃㈣繘鍘诲悗閫夋嫨绗竴椤�
        txt_message.performClick();
    }

    //UI缁勪欢鍒濆鍖栦笌浜嬩欢缁戝畾
    private void bindViews() {

        txt_exchange = (TextView) findViewById(R.id.txt_exchange);
        txt_message = (TextView) findViewById(R.id.txt_message);
        txt_publish = (TextView) findViewById(R.id.txt_publish);
        txt_task = (TextView) findViewById(R.id.txt_task);
        txt_profile = (TextView) findViewById(R.id.txt_profile);

        txt_exchange.setOnClickListener(this);
        txt_message.setOnClickListener(this);
        txt_publish.setOnClickListener(this);
        txt_profile.setOnClickListener(this);
        txt_task.setOnClickListener(this);
    } //閲嶇疆鎵�鏈夋枃鏈殑閫変腑鐘舵��

    private void setSelected() {
        txt_exchange.setSelected(false);
        txt_task.setSelected(false);
        txt_publish.setSelected(false);
        txt_message.setSelected(false);
        txt_profile.setSelected(false);
    } //闅愯棌鎵�鏈塅ragment

    private void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (fg1 != null) {
            fragmentTransaction.hide(fg1);
        }
        if (fg2 != null) {
            fragmentTransaction.hide(fg2);
        }
        if (fg3 != null) {
            fragmentTransaction.hide(fg3);
        }
        if (fg4 != null) {
            fragmentTransaction.hide(fg4);
        }
        if (fg5 != null) {
            fragmentTransaction.hide(fg5);
        }
    }

    @SuppressLint("CutPasteId")
    @Override
    public void onClick(View v) {
        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        switch (v.getId()) {
            case R.id.txt_exchange:
                setSelected();
                txt_exchange.setSelected(true);
                fg1 = new MyFragment(R.layout.fg_exchange);
                fTransaction.add(R.id.ly_content, fg1);

                mAdapterView = (MultiColumnListView) findViewById(R.id.list);
                imageUrls = new ArrayList<String>();
                adapter = new ImageGridAdapter(this, imageUrls);
                mAdapterView.setAdapter(adapter);
                queryMediaImages();

                break;
            case R.id.txt_task:
                setSelected();
                txt_task.setSelected(true);
                fg2 = new MyFragment(R.layout.fg_task);
                fTransaction.add(R.id.ly_content, fg2);

                break;
            case R.id.txt_publish:
                setSelected();
                txt_publish.setSelected(true);
                fg3 = new MyFragment(R.layout.fg_publish);
                fTransaction.add(R.id.ly_content, fg3);
                break;
            case R.id.txt_message:
                setSelected();
                txt_message.setSelected(true);
                fg4 = new MyFragment(R.layout.fg_message);
                fTransaction.add(R.id.ly_content, fg4);
                break;
            case R.id.txt_profile:
                setSelected();
                txt_profile.setSelected(true);
                fg5 = new MyFragment(R.layout.fg_profile);
                fTransaction.add(R.id.ly_content, fg5);
                break;
        }
        fTransaction.commit();
    }

    public void addDB() {
        ContentValues cv = new ContentValues();
        cv.put(UserDB.ID, "5");
        cv.put(UserDB.NAME, "xcy");
        cv.put(UserDB.SIGN, "hahah");
        dbWriter.insert(UserDB.TABLE_NAME, null, cv);
        dbWriter.close();
    }

    public void queryMediaImages() {
        Cursor c = getContentResolver().query(Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    long id = c.getLong(c.getColumnIndex(Images.Media._ID));
                    Uri imageUri = Uri.parse(Images.Media.EXTERNAL_CONTENT_URI + "/" + id);
                    imageUrls.add(imageUri.toString());
                    //imageUrls.add(getRealFilePath(MainActivity.this, imageUri));
                } while (c.moveToNext());
            }
        }
        c.close();
        c = null;
        adapter.notifyDataSetChanged();
    }

}