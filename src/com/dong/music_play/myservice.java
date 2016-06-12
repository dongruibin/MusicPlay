package com.dong.music_play;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

/** * @author  作者 E-mail: * @date 创建时间：2016-4-7 下午3:13:29 * @version 1.0 * @parameter  * @since  * @return  */
public class myservice extends Service {
	public class MySeekBar extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while(mediaPlayer.isPlaying()){
				int  now=mediaPlayer.getCurrentPosition();
				Intent intent1=new Intent(MainActivity.MUSIC_TIME);
				intent1.putExtra("type", 1);
				intent1.putExtra("time",now);
				sendBroadcast(intent1);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	MediaPlayer mediaPlayer;
	String musicName;
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		int type=intent.getIntExtra("type", Config.MUSIC_CLICK_START);//鉴别点击按钮
		switch(type){
		case Config.MUSIC_BUTTON_START:
			startMusic(intent);
			break;
		case Config.MUSIC_BUTTON_PAUSE:
			pauseMusic(intent);
			break;
		case Config.MUSIC_NEXT:
			startMusic(intent);//直接播放音乐
			break;
		case Config.MUSIC_PREVIOUS:
			startMusic(intent);//直接播放音乐
			break;
		case Config.MUSIC_SEEK:
			seekToProgress(intent);
			break;
		default : break;
		}
		return super.onStartCommand(intent, flags, startId);
	}

	private void seekToProgress(Intent intent) {
		// TODO Auto-generated method stub
		int progress=intent.getIntExtra("progress", 0);
		//歌曲跳转到progress后保持不变
		mediaPlayer.seekTo(progress);
	}

	private void pauseMusic(Intent intent) {
		// TODO Auto-generated method stub
		
	}

	private void startMusic(Intent intent) {//音乐的正式播放
		// TODO Auto-generated method stub
		//音乐位置以及名称
		String musicPath=intent.getStringExtra("musicPath");
		 musicName=intent.getStringExtra("musicName");
		//防止多个歌曲重复
		if(mediaPlayer==null){
			mediaPlayer=new MediaPlayer();
		}
		mediaPlayer.reset();
		
		try {
			mediaPlayer.setDataSource(musicPath);//这个需要try-catch
			mediaPlayer.prepare();
			mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				//属于监测是否播放完成
				@Override
				public void onPrepared(MediaPlayer mp) {
					// TODO Auto-generated method stub
					mp.start();
					int alltime=mediaPlayer.getDuration();
					//发送广播将时间参数送出去
					Intent intent1=new Intent(MainActivity.MUSIC_TIME);
					intent1.putExtra("type", 0);
					intent1.putExtra("time", alltime);
					intent1.putExtra("name", musicName);
					sendBroadcast(intent1);
					//服务内不允许耗时的操作，启动一个线程来传递音乐的进度（耗时操作）
					MySeekBar seekbar=new MySeekBar();
					seekbar.start();
				}
			});
		} catch (IllegalArgumentException | SecurityException
				| IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
