package edu.whu.cs.lyxxcy.yyjq;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener{ 
	private TextView txt_exchange; 
	private TextView txt_task; 
	private TextView txt_publish; 
	private TextView txt_message; 
	private TextView txt_profile; 
	private MyFragment fg1,fg2,fg3,fg4,fg5; 
	private FragmentManager fManager; 
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.activity_main);
	    fManager = getFragmentManager();
	    bindViews();
	    txt_exchange.performClick(); //模拟一次点击，既进去后选择第一项
	} 
	//UI组件初始化与事件绑定 
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
	} //重置所有文本的选中状态 
	private void setSelected(){
		txt_exchange.setSelected(false);
		txt_task.setSelected(false);
		txt_publish.setSelected(false);
		txt_message.setSelected(false);
		txt_profile.setSelected(false);
	} //隐藏所有Fragment 
	
	private void hideAllFragment(FragmentTransaction fragmentTransaction){
		if(fg1 != null)fragmentTransaction.hide(fg1);
		if(fg2 != null)fragmentTransaction.hide(fg2); 
		if(fg3 != null)fragmentTransaction.hide(fg3);
		if(fg4 != null)fragmentTransaction.hide(fg4);
		if(fg5 != null)fragmentTransaction.hide(fg5);
	} 
	
	@Override 
	public void onClick(View v) {
		FragmentTransaction fTransaction = fManager.beginTransaction();
		hideAllFragment(fTransaction); 
		switch (v.getId()){ 
			case R.id.txt_exchange:
	            setSelected();
	            txt_exchange.setSelected(true); 
	            fg1 = new MyFragment(R.layout.fg_exchange);
	            fTransaction.add(R.id.ly_content,fg1);
	            break; 
            case R.id.txt_task:
	            setSelected();
	            txt_task.setSelected(true); 
	            fg2 = new MyFragment(R.layout.fg_task);
	            fTransaction.add(R.id.ly_content,fg2);
	            break; 
            case R.id.txt_publish:
	            setSelected();
	            txt_publish.setSelected(true); 
	            fg3 = new MyFragment(R.layout.fg_publish);
	            fTransaction.add(R.id.ly_content,fg3);
	            break; 
            case R.id.txt_message:
	            setSelected();
	            txt_message.setSelected(true); 
	            fg4 = new MyFragment(R.layout.fg_message);
	            fTransaction.add(R.id.ly_content,fg4);
	            break;
            case R.id.txt_profile:
	            setSelected();
	            txt_profile.setSelected(true); 
	            fg5 = new MyFragment(R.layout.fg_profile);
	            fTransaction.add(R.id.ly_content,fg5);
	            break;
    }
    fTransaction.commit();
}
}