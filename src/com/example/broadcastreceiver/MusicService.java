package com.example.broadcastreceiver;

import java.io.IOException;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;

public class MusicService extends Service{
	ServiceReceiver serviceRecevice;
	AssetManager am;
	String[]musics=new String[]{"oldboy.mp3","spring.mp3","way.mp3"};
	MediaPlayer mPlayer;
	int status=0x11;
	int current=0;
	public IBinder onBinder(Intent intent){
		return null;
	}
	public void onCreate(){
		am=getAssets();
		serviceRecevice=new ServiceReceiver();
		IntentFilter filter=new IntentFilter(MainActivity.CONTROL);
		registerReceiver(serviceRecevice,filter);
		mPlayer=new MediaPlayer();
        mPlayer.setOnCompletionListener(new OnCompletionListener(){
		public void onCompletion(MediaPlayer mp){
			current++;
			if(current>=3){
				current=0;
			}
			Intent sendIntent=new Intent(MainActivity.UPDATE);
			sendIntent.putExtra("current",current);
			sendBroadcast(sendIntent);
			prepareAndPlay(musics[current]);
		}
	});
	
	
	       super.onCreate();
	}

     public class ServiceReceiver extends BroadcastReceiver{
	   

		@Override
		public void onReceive( final Context context, Intent intent) {
			// TODO Auto-generated method stub
			  int control=intent.getIntExtra("control",-1);
		    	switch(control){
		    		case 1:
		    			 if(status==0x11){
		    				 prepareAndPlay(musics[current]);
		    			}
		    			 else if(status==0x12){
		    				 mPlayer.pause();
		    				 status=0x13;
		    			   }
		    			 else if(status==0x13){
		    				 mPlayer.start();
		    				 status=0x12;
		    			  }
		    			   break;
		    	    case 2:
		    		      if(status==0x12||status==0x13){
		    				 mPlayer.stop();
		    				 status=0x11;
		    		     }
		    	}
		                
		       Intent sendIntent=new Intent(MainActivity.UPDATE);
		       sendIntent.putExtra("update",status);
		       sendIntent.putExtra("current", current);
		       sendBroadcast(sendIntent);
		}
	    	 }
	    	   
	    private void prepareAndPlay(String music){
	      try{
	    	   AssetFileDescriptor afd=am.openFd(music);
	    	   mPlayer.reset();
	    	   mPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
	    	   mPlayer.prepare();
	    	   mPlayer.start();
	    	   }
	    	   catch(IOException e){
	    		 e.printStackTrace();
	    	   }
	       }
		@Override
		public IBinder onBind(Intent intent) {
			// TODO Auto-generated method stub
			return null;
		}
	
     

    }

