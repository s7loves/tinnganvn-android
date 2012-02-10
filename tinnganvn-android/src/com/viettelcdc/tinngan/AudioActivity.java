package com.viettelcdc.tinngan;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.viettelcdc.tinngan.util.Utils;

public class AudioActivity extends Activity implements OnClickListener, Runnable,
OnErrorListener, OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener {
    private static final String TAG = "VideoPlayer";

    private MediaPlayer mediaPlayer;
    private SurfaceView surfaceView;
    private Button buttonPlayPause;
    private Button buttonPrev;
    private Button buttonNext;
    private TextView durrationTextView;
    private long durration;
    private TextView currentPositionTextView;
    private SeekBar seekbar;
    private Handler handler;
    
    /**
     * Called when the activity is first created.
     */
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.video_player_activity);

        handler = new Handler();
        
        durrationTextView = (TextView) findViewById(R.id.durration);
        currentPositionTextView = (TextView) findViewById(R.id.current_position);
     
        seekbar = (SeekBar) findViewById(R.id.seekbar);
        
        buttonPlayPause = (Button) findViewById(R.id.play_pause_button);
        
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);

        // Set the surface for the video output
        surfaceView = (SurfaceView) findViewById(R.id.video);
        mediaPlayer.setDisplay(surfaceView.getHolder());
        
        try {
			mediaPlayer.setDataSource("/mnt/sdcard/DCIM/Movies/test.flv");
			playVideo();
        } catch (Exception e) {
			e.printStackTrace();
		}
    }

    private void playVideo() {
        try {           
            if(durration != 0) onPrepared(mediaPlayer);
            else mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
        Log.e(TAG, "onError: " + what + ", extra:" + extra);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        return true;
    }

    @Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		if(percent == 100) {
			seekbar.setSecondaryProgress(0);
		} else {
			seekbar.setSecondaryProgress(percent);
		}
	}

    public void onCompletion(MediaPlayer arg0) {
        mediaPlayer.stop();
    }

    public void onPrepared(MediaPlayer mediaplayer) {
    	durration = mediaPlayer.getDuration();
        durrationTextView.setText(Utils.msToClockTime(durration));
        durrationTextView.setVisibility(View.VISIBLE);
        mediaPlayer.start();
        startTimeTracking();
    }
    
    void startTimeTracking() {
    	handler.postDelayed(this, 1000);
    }
    
    @Override
    protected void onPause() {
    	try {
    		mediaPlayer.pause();
    	} catch (Exception e) {
			e.printStackTrace();
		}
    	super.onPause();
    }
    
    @Override
    protected void onDestroy() {
    	try {
            mediaPlayer.stop();
            mediaPlayer.release();
        } catch (Exception e) {
        	e.printStackTrace();
        }
    	super.onDestroy();
    }

	@Override
	public void run() {
		if(mediaPlayer.isPlaying()) {
			long currentPosition = mediaPlayer.getCurrentPosition();
			currentPositionTextView.setText(Utils.msToClockTime(currentPosition));
			
			seekbar.setProgress((int)((currentPosition * 100)/durration));
			startTimeTracking();
		}
	}

	@Override
	public void onClick(View view) {
		if(view == buttonPlayPause) {
			if(mediaPlayer.isPlaying()) {
				mediaPlayer.pause();
			} else {
				playVideo();
			}
		}
	}
}