package com.hard.playback_player.utils;

import android.app.Activity;
import android.media.MediaPlayer;
import android.widget.Button;
import android.widget.Toast;

import com.hard.playback_player.R;

import java.io.IOException;

public class Player {
    private static final int PLAY = 1;
    private static final int PAUSE = 2;
    private static final int STOP = 3;

    private static int STATE = STOP;

    private Activity activity;
    private MediaPlayer player;
    private String url;

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void play() {
        if (player != null)
            return;

        if (url == null)
            return;

        player = new MediaPlayer();

        try {
            player.setDataSource(url);
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stop();
            }
        });

        player.start();

        STATE = PLAY;
    }

    public void pause() {
        if (player != null)
            return;

        player.pause();

        STATE = PAUSE;
    }

    public void stop() {
        if (player == null)
            return;

        player.stop();
        player.release();
        player = null;

        STATE = STOP;

        Toast.makeText(activity, "Done", Toast.LENGTH_SHORT).show();
        Button play_button = activity.findViewById(R.id.play_button);
        play_button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_play_arrow_24, 0, 0, 0);
    }

    public boolean isPlaying() {
        if (player == null)
            return false;

        if (player.isPlaying())
            return true;

        return false;
    }

    public boolean isPaused() {
        if (player == null)
            return false;

        if (!player.isPlaying())
            return true;

        return false;
    }
}
