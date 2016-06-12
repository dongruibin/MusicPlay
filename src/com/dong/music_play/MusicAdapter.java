package com.dong.music_play;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private File[] musics;

	public MusicAdapter(LayoutInflater inflater, File[] musics) {
		// TODO Auto-generated constructor stub
		//构造方法
		this.mInflater = mInflater;
        this.musics = musics;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		//重写该方法
		//return 0;
		return musics.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		//return null;
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		//return 0;
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//return null;
		 ViewHolder vh=null;
	        if(convertView==null){
	            vh=new ViewHolder();
	            convertView=mInflater.inflate(R.layout.listview_item,null);
	            vh.musicName = (TextView) convertView.findViewById(R.id.textview_music_name);
	            vh.musicAuther = (TextView) convertView.findViewById(R.id.tv_music_auther);
	            vh.img= (ImageView) convertView.findViewById(R.id.img);
	            convertView.setTag(vh);
	        }
	        vh= (ViewHolder) convertView.getTag();
//	        设置音乐名称
	        vh.musicName.setText(musics[position].getName());

//	      获取作家
	        MediaMetadataRetriever mmr=new MediaMetadataRetriever();
	        mmr.setDataSource(musics[position].getAbsolutePath());
	        String auther=mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
//	        判断作者是否为空
	        if(auther!=null){
	            vh.musicAuther.setText(auther);
	        }else {
	            vh.musicAuther.setText("<未知>");
	        }
	        //获得图片
	        byte[] image=mmr.getEmbeddedPicture();
	        if(image!=null){
	            Bitmap bitmap= BitmapFactory.decodeByteArray(image,0,image.length);
	            vh.img.setImageBitmap(bitmap);
	        }else{
	            vh.img.setImageResource(R.drawable.ic_launcher);
	        }
	        return convertView;
	    }
	    class ViewHolder{
	        TextView musicName;
	        TextView musicAuther;
	        ImageView img;
	}

}
