package com.dong.music_play;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
//工程说明
//该工程利用mediaplayer，广播，服务，listView完成整体任务
//--------ListView的使用----------------
//	1、用来展示列表的View
//	2、适配器  用来把数据映射到ListView上的中介
//	3、数据 具体的将别映射的字符串，图片，或者基本组件
//根据适配器的属性，列表分为三种，ArrayAdapter，SimpleAdapter和SimpleCursorAdapter
public class MainActivity extends Activity {
	private CheckBox mBtnBeforeMusic,mBtnNextMusic, mBtnPauseMusic;
	private SeekBar seekbar;
	private TextView  mTextViewAllTime, mTextViewCurrentTime,mTextViewMusicName;
	
	//变量
	private ListView mListView;
	private File[] musics;
	private MyBroadCastService myBoard;
	
	private int mPosition;//记录音乐的位置
	public static final String MUSIC_TIME="com.dong.music";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//取消标题显示
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//setContentView(R.layout.activity_main);
		//开始测试ArrayAdapter阶段
//		listView=new ListView(this);
//		listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,array_getData()));
//		setContentView(listView);
		//开始测试SimpleAdapter阶段
			//有时候需要在里面使用到一些除文字以外的东西，例如图片等，可以自定义ListView里面的item的内容
		//listView=findViewById(R.id.)
		//开始******
		setContentView(com.dong.music_play.R.layout.activity_main);
		
		//控件的初始化(之前忘记写，于2016-4-7补上)
		mBtnBeforeMusic=(CheckBox) findViewById(com.dong.music_play.R.id.btn_brfore_music);
		mBtnNextMusic=(CheckBox) findViewById(com.dong.music_play.R.id.btn_next_music);
		mBtnPauseMusic=(CheckBox) findViewById(com.dong.music_play.R.id.btn_pause_music);
		seekbar=(SeekBar) findViewById(com.dong.music_play.R.id.seekbar);
		mListView=(ListView) findViewById(com.dong.music_play.R.id.listview);
		   //时间条的控件
		 mTextViewAllTime=(TextView) findViewById(com.dong.music_play.R.id.textview_all_time);
		 mTextViewCurrentTime=(TextView) findViewById(com.dong.music_play.R.id.textview_current_time);
		    //音乐名字显示
		 mTextViewMusicName=(TextView) findViewById(com.dong.music_play.R.id.textview_music_name);
		//获取所有的歌曲
		//获取SD卡上的音乐,通用做法：先判断sd卡是否存在
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))//SD卡存在
		{
			Toast.makeText(getApplicationContext(), "sd卡OK", Toast.LENGTH_LONG).show();
			Log.v("xiaodong", "sdcard is ok");
			final File music=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
			musics = music.listFiles();
//			
//		      for (File item:musics){
//		            Log.d("歌曲的位置", "→"+item);
//		        }
		}else{
			Toast.makeText(getApplicationContext(), "sd卡读取失败", Toast.LENGTH_LONG).show();
		}
		//添加到listView里面
		//备注2016-3-2日调试下面，没有成功
		//final LayoutInflater inflater=getLayoutInflater();
		LayoutInflater inflater=getLayoutInflater();
		MusicAdapter musicadapter=new MusicAdapter(inflater,musics);
		//---于2016-4-7解决【没有初始化mlistView控件】
		mListView.setAdapter(musicadapter);//这句为什么会出问题
		//注册广播
		 myBoard = new MyBroadCastService();
		 IntentFilter filter=new IntentFilter();
		 filter.addAction(MUSIC_TIME);
		 registerReceiver(myBoard, filter);
		 //动态注销广播
		 
		 //按钮监听
		 //设置上一首音乐
		 mBtnBeforeMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				//不是最后一首音乐时才+1
				if(mPosition!=musics.length-1){
					mPosition++;
				}
				Intent intent=new Intent();
				intent.setClass(getApplicationContext(), myservice.class);
			}
		});
	}
//***************************ListView**********************
	//function：getData()
	//仅供ListView测试使用
	private List<String> array_getData() {
		// TODO Auto-generated method stub
		List<String> data=new ArrayList<String>();
		data.add("测试使用1");
		data.add("测试使用2");
		data.add("测试使用3");
		return data;
	}
//*************************ListView用到东西结束*****************
	public class MyBroadCastService extends  BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	 @Override
	 public void onDestroy(){
		 super.onDestroy();
		//将广播注销掉
		 unregisterReceiver(myBoard);
	 }
}
