package com.example.broadcastreceiver;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{
	TextView title,author;
	ImageButton play,stop;
	ActivityReceiver activityReceiver;
	public static final String CONTROL="com.example.broadcastreceiver.control";
	public static final String UPDATE="com.example.broadcastreceiver.update";
	int status=0x11;
	String[] titleStrs=new String[]{"老男孩","春天里","在路上"};
	String[]authorStrs=new String[]{"筷子兄弟","汪峰","刘欢"};
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		play=(ImageButton)this.findViewById(R.id.play);
		stop=(ImageButton)this.findViewById(R.id.stop);
		title=(TextView)findViewById(R.id.title);
		author=(TextView)findViewById(R.id.author);
		play.setOnClickListener(this);
		stop.setOnClickListener(this);
		activityReceiver=new ActivityReceiver();
		IntentFilter filter=new IntentFilter(UPDATE);
		registerReceiver(activityReceiver,filter);
		Intent intent=new Intent(this,MusicService.class);
		startService(intent);
		}
public class ActivityReceiver extends BroadcastReceiver{
			public void onReceive (Context contect,Intent intent){
				
				int update=intent.getIntExtra("updata",-1);
				int current=intent.getIntExtra("current",-1);
				if(current>=0){
					title.setText(titleStrs[current]);
					author.setText(authorStrs[current]);
					
				}
				switch(update){
				case 0x11:
					play.setImageResource(R.drawable.play);
					status=0x11;
					break;
				case 0x12:
					play.setImageResource(R.drawable.pause);
					status=0x12;
					break;
				case 0x13:
					play.setImageResource(R.drawable.play);
					status=0x14;
					break;
				
				}
			}
			
		}
		public void onClick(View source) {
			// 创建Intent
			Intent intent = new Intent(CONTROL);
			System.out.println(source.getId());
			System.out.println(source.getId() == R.id.play);
			switch (source.getId()) {
			// 按下播放/暂停按钮
			case R.id.play:
				intent.putExtra("control", 1);
				break;
			// 按下停止按钮
			case R.id.stop:
				intent.putExtra("control", 2);
				break;
			}
			// 发送广播 ，将被Service组件中的BroadcastReceiver接收到
			sendBroadcast(intent);
		}
		
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
