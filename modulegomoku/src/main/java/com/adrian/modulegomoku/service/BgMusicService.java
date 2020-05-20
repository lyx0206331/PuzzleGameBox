package com.adrian.modulegomoku.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.adrian.modulegomoku.R;

public class BgMusicService extends Service {

    public static final String ACTION_START_PLAY = "com.adrian.gomoku.play";
    public static final String ACTION_PAUSE_PLAY = "com.adrian.gomoku.pause";

    private MediaPlayer mediaPlayer;

    public BgMusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initPlayer();
    }

    private void initPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.bg_music);
            mediaPlayer.setLooping(true);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initPlayer();
        String action = intent.getAction();
        if (action.equals(ACTION_START_PLAY) && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        } else if (action.equals(ACTION_PAUSE_PLAY) && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
