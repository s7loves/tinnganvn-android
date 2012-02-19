package com.viettelcdc.tinngan.util.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;
import android.widget.RemoteViews;

import com.viettelcdc.tinngan.HomeActivity;
import com.viettelcdc.tinngan.R;
import com.viettelcdc.tinngan.entity.AudioTrack;

public class PlaybackService extends ForegroundService implements OnPreparedListener, OnBufferingUpdateListener, OnCompletionListener {
	private static final int NOTIFICATION_ID = 1;
	
    public static final String TRACK_EXTRA = "track";
	
	static PlaybackService running = null;

	static private int lastSavedPosition = 0;

	static Notification notification = null;
	static RemoteViews expandView;
	static MediaPlayer mediaPlayer;
	static AudioTrack track;
	
	static private OnPreparedListener onPreparedListener;
	static private OnBufferingUpdateListener bufferingListener;
	public static interface OnPlayListener {
		void onPlay();
	}
	
	static private OnPlayListener onPlayListener;
	
	public static void setOnPlayListener(OnPlayListener playListener) {
		onPlayListener = playListener;
	}
	
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
			intent.putExtra(HomeActivity.TAB_EXTRA, getString(R.string.tab_audio));
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
				if(onPlayListener != null) {
					onPlayListener.onPlay();
				}
			} else {	
				if(track == null) stopSelf();
				initMediaPlayer();
				mediaPlayer.setDataSource(track.streamUrl);
				mediaPlayer.prepareAsync();
				buffering = true;
			}
			
			notification.tickerText = track.title;
			expandView.setTextViewText(R.id.player_notification_text, track.title);
			startForegroundCompat(NOTIFICATION_ID, notification);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {

		lastSavedPosition = getCurrentPosition();
		if(mediaPlayer.isPlaying()){
			mediaPlayer.pause();
		} else {
			mediaPlayer = null;
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
	
	public static AudioTrack getCurrentTrack() {
		return track;
	}

	public static int getCurrentPosition() {
		if (mediaPlayer != null && mediaPlayer.isPlaying())
			return mediaPlayer.getCurrentPosition();
		return 0;
	}

	public static int getDuration() {
		if (mediaPlayer != null && mediaPlayer.isPlaying())
			return mediaPlayer.getDuration();
		return 0;
	}

	public static boolean isRunning() {
		return running != null;
	}

	public static boolean isBuffering() {
		return running != null && buffering;
	}
	
	public static boolean isPlaying(){
		return mediaPlayer != null && mediaPlayer.isPlaying();
	}
	
	public static boolean isPausing(){
		return lastSavedPosition != 0 && !isPlaying();
	}
	
    @Override
    public void onStart(Intent intent, int startId) {
        AudioTrack newTrack = (AudioTrack) intent.getSerializableExtra(TRACK_EXTRA);
        if(newTrack != null) {
        	track = newTrack;
        	lastSavedPosition = 0;
        }
        play();
    }

    public static void setOnPreparedListener(OnPreparedListener listener) {
    	onPreparedListener = listener;
    }
    
    @Override
	public void onPrepared(MediaPlayer arg0) {
    	if(isRunning() && mediaPlayer != null) {
			mediaPlayer.start();
			if(onPlayListener != null) {
				onPlayListener.onPlay();
			}
			if(onPreparedListener != null) {
				onPreparedListener.onPrepared(mediaPlayer);
			}
			
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
		if(bufferingListener != null) {
			bufferingListener.onBufferingUpdate(mp, percent);
		}
	}
	
	@Override
	public void onCompletion(MediaPlayer mp) {
		stopSelf();
	}
	
	public static void stop() {
		if(isRunning()) {
			running.stopSelf();
		}
	}
}