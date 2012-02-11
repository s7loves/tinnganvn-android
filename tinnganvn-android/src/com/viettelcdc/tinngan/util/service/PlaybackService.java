package com.viettelcdc.tinngan.util.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.widget.RemoteViews;

import com.viettelcdc.tinngan.HomeActivity;
import com.viettelcdc.tinngan.R;
import com.viettelcdc.tinngan.entity.AudioNews;

public class PlaybackService extends ForegroundService implements OnPreparedListener, OnBufferingUpdateListener, OnCompletionListener {
	private static final int NOTIFICATION_ID = 1;
	
    static final String TRACK_EXTRA = "track";
	
	static PlaybackService running = null;

	static private int lastSavedPosition = 0;

	static Notification notification = null;
	static RemoteViews expandView;
	static MediaPlayer mediaPlayer;
	static AudioNews track;
	
	static private OnPreparedListener onPreparedListener;
	static private OnBufferingUpdateListener bufferingListener;
	
	static private boolean buffering = false;
	
	@Override
	public void onCreate() {
		super.onCreate();
		running = this;
		if (notification == null) {
			notification = new Notification(R.drawable.ic_stat_notify_now_playing, "",
					System.currentTimeMillis());
			expandView = new RemoteViews(getPackageName(),
					R.layout.player_notification);
			notification.contentView = expandView;
			Intent intent = new Intent(getBaseContext(), HomeActivity.class);
			notification.contentIntent = PendingIntent.getActivity(this, 0,
					intent, 0);
		}
	}
	
	void initMediaPlayer(){
		if(mediaPlayer != null) mediaPlayer.release();
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnPreparedListener(this);
		mediaPlayer.setOnBufferingUpdateListener(this);
		mediaPlayer.setOnCompletionListener(this);
	}
	
	public synchronized void play() {
		try {
			if (lastSavedPosition != 0) {
				seekTo(lastSavedPosition);
				mediaPlayer.start();
				return;
			}
			
			if(track == null) stopSelf();
			initMediaPlayer();
			mediaPlayer.setDataSource(track.streamUrl);
			
			buffering = true;
			notification.tickerText = track.title;
			expandView.setTextViewText(R.id.player_notification_text, track.title);
			startForegroundCompat(NOTIFICATION_ID, notification);
			mediaPlayer.prepareAsync();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		lastSavedPosition = getCurrentPosition();
		if(mediaPlayer.isPlaying()){
			mediaPlayer.pause();
		}
		running = null;
		super.onDestroy();
	}
	
	public static void seekTo(int msec) {
		mediaPlayer.seekTo(msec);
	}

	public static void seekToPercent(int percent) {
		mediaPlayer.seekTo((getDuration() * percent) / 100);
	}
	
	public static AudioNews getCurrentTrack() {
		return track;
	}

	public static int getCurrentPosition() {
		if (mediaPlayer.isPlaying())
			return mediaPlayer.getCurrentPosition();
		return 0;
	}

	public static int getDuration() {
		if (mediaPlayer.isPlaying())
			return mediaPlayer.getDuration();
		return 0;
	}

	public static boolean isRunning() {
		return running != null;
	}

	public boolean isBuffering() {
		return buffering;
	}
	
	public boolean isPlaying(){
		return mediaPlayer.isPlaying();
	}
	
    @Override
    public void onStart(Intent intent, int startId) {
        track = (AudioNews) intent.getSerializableExtra(TRACK_EXTRA);
        play();
    }

    public static void setOnPreparedListener(OnPreparedListener listener) {
    	onPreparedListener = listener;
    }
    
    @Override
	public void onPrepared(MediaPlayer arg0) {
		if(running != null) {
			mediaPlayer.start();
			onPreparedListener.onPrepared(mediaPlayer);
		}
	}
	
    public static void setOnBufferingUpdateListener(OnBufferingUpdateListener listener) {
		bufferingListener = listener;
	}
    
	@Override
	public void onBufferingUpdate(MediaPlayer mp,
			int percent) {
		if (percent == 100)
			buffering = false;
		bufferingListener.onBufferingUpdate(mp, percent);
	}
	
	@Override
	public void onCompletion(MediaPlayer mp) {
		lastSavedPosition = 0;
	}
}