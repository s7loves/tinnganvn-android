package com.viettelcdc.tinngan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.pulltorefresh.library.PullToRefreshListView;
import com.viettelcdc.tinngan.entity.AudioTrack;
import com.viettelcdc.tinngan.util.Utils;
import com.viettelcdc.tinngan.util.asyntask.DownloadAudioNewsTask;
import com.viettelcdc.tinngan.util.service.PlaybackService;
import com.viettelcdc.tinngan.util.service.PlaybackService.OnPlayListener;
import com.viettelcdc.tinngan.widget.adapter.AudioArticleListAdapter;

public class AudioNewsActivity extends Activity
	implements Runnable, OnPlayListener, OnPreparedListener, OnRefreshListener,
	OnBufferingUpdateListener, OnItemClickListener, OnClickListener, OnSeekBarChangeListener {

	public static int TIMER_INTERVAL = 1000;
	
    private long durration;
    private Handler handler;
    private ListView listView;
    private Button buttonPlayPause;
    private TextView durrationTextView;
    private TextView currentPositionTextView;
    private SeekBar seekbar;
    private View playerControl;
    private View loadingStub;
	private PullToRefreshListView wrapListView;
	private BaseAdapter adapter;
    
    private static List<AudioTrack> tracks = new ArrayList<AudioTrack>();
    
    public void onCreate(Bundle icicle) {    	
        super.onCreate(icicle);
        setContentView(R.layout.audionews_activity);
        
        durrationTextView = (TextView) findViewById(R.id.durration);
		currentPositionTextView = (TextView) findViewById(R.id.current_position);
		seekbar = (SeekBar) findViewById(R.id.seekbar);
		buttonPlayPause = (Button) findViewById(R.id.btn_play_pause);
		buttonPlayPause.setOnClickListener(this);
		playerControl = findViewById(R.id.player_control);
		
		seekbar.setOnSeekBarChangeListener(this);
		
		PlaybackService.setOnPreparedListener(this);
		PlaybackService.setOnBufferingUpdateListener(this);
		PlaybackService.setOnPlayListener(this);
		
		handler = new Handler();
		wrapListView = (PullToRefreshListView) findViewById(R.id.listview);
		
		wrapListView.setOnRefreshListener(this);
		loadingStub = findViewById(R.id.loading_stub);
		
		listView = wrapListView.getRefreshableView();
		adapter = new AudioArticleListAdapter(this, tracks);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		
		if(tracks.size() == 0) {
			onRefresh();
		} else {
			loadingStub.setVisibility(View.INVISIBLE);
		}
	}

	public void onRefresh() {
		new DownloadAudioNewsTask() {
			protected void onPostExecute(Object result) {
				adapter.notifyDataSetChanged();
				wrapListView.onRefreshComplete();
				loadingStub.setVisibility(View.INVISIBLE);
			};
		}.execute(new Object[] { tracks, tracks.size() + 1});
	}
	
    @Override
    protected void onResume() {
		if(PlaybackService.isRunning()) {
			buttonPlayPause.setBackgroundResource(R.drawable.btn_pause);
			startTimeTracking();
		} else {
			buttonPlayPause.setBackgroundResource(R.drawable.btn_play);
		}
		super.onResume();
    }

    @Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		if(percent == 100) {
			seekbar.setSecondaryProgress(0);
		} else {
			seekbar.setSecondaryProgress(percent);
		}
	}
    
    public void onPrepared(MediaPlayer mediaplayer) {
    	durration = PlaybackService.getDuration();
    	durrationTextView.setText(" / " + Utils.msToClockTime(durration));
    	buttonPlayPause.setEnabled(true);
    }
    
    void startTimeTracking() {
    	handler.postDelayed(this, TIMER_INTERVAL);
    }

	@Override
	public void run() {
		if(PlaybackService.isPlaying()) {
			long currentPosition = PlaybackService.getCurrentPosition();
			currentPositionTextView.setText(Utils.msToClockTime(currentPosition));			
			if(currentPosition < durration)
			startTimeTracking();
		}
	}

	@Override
	public void onClick(View view) {
		if(view == buttonPlayPause) {
			if(PlaybackService.isPlaying()) {
				PlaybackService.setOnBufferingUpdateListener(null);
				PlaybackService.stop();
				buttonPlayPause.setBackgroundResource(R.drawable.btn_play);
			} else {
				PlaybackService.setOnBufferingUpdateListener(this);
				
				if(!PlaybackService.isBuffering()) {
					seekbar.setSecondaryProgress(0);
				}
				
				startService(new Intent(this, PlaybackService.class));
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
		playerControl.setVisibility(View.VISIBLE);
		
		AudioTrack track = tracks.get(pos);
		AudioTrack playing = PlaybackService.getCurrentTrack();
		
		if(playing == null ||
				!(PlaybackService.isPlaying() && track.streamUrl.equals(playing.streamUrl))) {

			PlaybackService.stop();
			
			Intent intent = new Intent(this, PlaybackService.class);
			intent.putExtra(PlaybackService.TRACK_EXTRA, (Serializable) track);
			startService(intent);
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if(fromUser) {
			PlaybackService.seekTo((progress * (int)durration)/100);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {}

	@Override
	public void onPlay() {
    	startTimeTracking();
		buttonPlayPause.setBackgroundResource(R.drawable.btn_pause);
	}
}