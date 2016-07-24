package edu.whu.cs.lyxxcy.yyjq;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
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
    private UserDB userDB;
    private SQLiteDatabase dbWriter;
    private View view_exchange;
    private View view_task;
    private View view_publish;
    private View view_message;
    private View view_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        userDB = new UserDB(this);
        dbWriter = userDB.getWritableDatabase();
        bindViews();
        txt_exchange.performClick();
    }

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
    } 

    private void setSelected() {
        txt_exchange.setSelected(false);
        txt_task.setSelected(false);
        txt_publish.setSelected(false);
        txt_message.setSelected(false);
        txt_profile.setSelected(false);
    } 

    @SuppressLint("CutPasteId")
    @Override
    public void onClick(View v) {
        
        switch (v.getId()) {
            case R.id.txt_exchange:
                setSelected();
                txt_exchange.setSelected(true);
                if(view_task != null){
                    view_task.setVisibility(View.GONE);
                }
                if(view_publish != null){
                    view_publish.setVisibility(View.GONE);
                }
                if(view_message != null){
                    view_message.setVisibility(View.GONE);
                }
                if(view_profile != null){
                    view_profile.setVisibility(View.GONE);
                }
                if(view_exchange != null){
                	view_exchange.setVisibility(View.GONE);
                }
                
                view_exchange = findViewById(R.id.fg_exchange);
                view_exchange.setVisibility(View.VISIBLE);
                mAdapterView = (MultiColumnListView) findViewById(R.id.list1);
                Button bt = (Button) view_exchange.findViewById(R.id.testButton);
                bt.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						System.exit(0);
					}
                });
                imageUrls = new ArrayList<String>();
                adapter = new ImageGridAdapter(this, imageUrls);
                mAdapterView.setAdapter(adapter);
                queryMediaImages();
                
                break;
            case R.id.txt_task:
                setSelected();
                txt_task.setSelected(true);
                if(view_task != null){
                    view_task.setVisibility(View.GONE);
                }
                if(view_publish != null){
                    view_publish.setVisibility(View.GONE);
                }
                if(view_message != null){
                    view_message.setVisibility(View.GONE);
                }
                if(view_profile != null){
                    view_profile.setVisibility(View.GONE);
                }
                if(view_exchange != null){
                	view_exchange.setVisibility(View.GONE);
                }
                
                view_task = findViewById(R.id.fg_task);
                view_task.setVisibility(View.VISIBLE);
                break;
            case R.id.txt_publish:
                setSelected();
                txt_publish.setSelected(true);
                if(view_task != null){
                    view_task.setVisibility(View.GONE);
                }
                if(view_publish != null){
                    view_publish.setVisibility(View.GONE);
                }
                if(view_message != null){
                    view_message.setVisibility(View.GONE);
                }
                if(view_profile != null){
                    view_profile.setVisibility(View.GONE);
                }
                if(view_exchange != null){
                	view_exchange.setVisibility(View.GONE);
                }
                
                view_publish = findViewById(R.id.fg_publish);
                view_publish.setVisibility(View.VISIBLE);
                break;
            case R.id.txt_message:
                setSelected();
                txt_message.setSelected(true);
                if(view_task != null){
                    view_task.setVisibility(View.GONE);
                }
                if(view_publish != null){
                    view_publish.setVisibility(View.GONE);
                }
                if(view_message != null){
                    view_message.setVisibility(View.GONE);
                }
                if(view_profile != null){
                    view_profile.setVisibility(View.GONE);
                }
                if(view_exchange != null){
                	view_exchange.setVisibility(View.GONE);
                }
                
                view_message = findViewById(R.id.fg_message);
                view_message.setVisibility(View.VISIBLE);
                break;
            case R.id.txt_profile:
                setSelected();
                txt_profile.setSelected(true);
                if(view_task != null){
                    view_task.setVisibility(View.GONE);
                }
                if(view_publish != null){
                    view_publish.setVisibility(View.GONE);
                }
                if(view_message != null){
                    view_message.setVisibility(View.GONE);
                }
                if(view_profile != null){
                    view_profile.setVisibility(View.GONE);
                }
                if(view_exchange != null){
                	view_exchange.setVisibility(View.GONE);
                }
                
                view_profile = findViewById(R.id.fg_profile);
                view_profile.setVisibility(View.VISIBLE);
                break;
        }
        
    }

    public void addDB() {
//        ContentValues cv = new ContentValues();
//        cv.put(UserDB.ID, "5");
//        cv.put(UserDB.NAME, "xcy");
//        cv.put(UserDB.SIGN, "hahah");
//        dbWriter.insert(UserDB.TABLE_NAME, null, cv);
//        dbWriter.close();
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