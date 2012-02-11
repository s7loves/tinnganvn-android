package com.viettelcdc.tinngan;

import android.app.Activity;

public class AudioNewsActivity extends Activity {
	/*implements OnPreparedListener, OnBufferingUpdateListener, OnItemClickListener, OnClickListener {

    private static final String TAG = "VideoPlayer";

    private long durration;
    private Handler handler;
    private ListView listView;
    private Button buttonPlayPause;
    private TextView durrationTextView;
    private TextView currentPositionTextView;
    private SeekBar seekbar;
    private View playerControl;
    
    List<AudioNews> tracks = new ArrayList<AudioNews>();
    
    *//**
     * Called when the activity is first created.
     *//*
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.audionews_activity);
        listView = (ListView)findViewById(R.id.listview);
        
        List<String> titles = new ArrayList<String>();
        
        for(AudioNews track: tracks) {
        	titles.add(track.title);
        }
        
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.audionews_item, R.id.title, titles));
        listView.setOnItemClickListener(this);
        
        playerControl = findViewById(R.id.player_control);
        
        durrationTextView = (TextView) findViewById(R.id.durration);
		currentPositionTextView = (TextView) findViewById(R.id.current_position);
		seekbar = (SeekBar) findViewById(R.id.seekbar);
		buttonPlayPause = (Button) findViewById(R.id.play_pause_button);
		
		PlaybackService.setOnPreparedListener(this);
		PlaybackService.setOnBufferingUpdateListener(this);
		
		handler = new Handler();
	}
	
    @Override
    protected void onResume() {
		AudioNews nowPlaying = PlaybackService.getCurrentTrack();
		
		View control = findViewById(R.id.player_control);
		
		for(AudioNews track: tracks) {
        	if(nowPlaying != null && nowPlaying.streamUrl.equals(track.streamUrl));
        }
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
    	
        startTimeTracking();
    }
    
    void startTimeTracking() {
    	handler.postDelayed(this, 1000);
    }

	@Override
	public void run() {
		if(mediaPlayer.isPlaying()) {
			long currentPosition = PlaybackService.getCurrentPosition();
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

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
		AudioNews track = tracks.get(pos);
		AudioNews playing = PlaybackService.getCurrentTrack();
		
		//(if)
	}*/
}