package com.hard.playback_player.app;

import android.app.Application;

import com.hard.playback_player.utils.Player;
import com.hard.playback_player.utils.managers.MetronomesManager;
import com.hard.playback_player.utils.managers.PlaybacksManager;
import com.hard.playback_player.utils.managers.ScoresManager;

public class PlaybackPlayerApplication extends Application {
    private ScoresManager scoresManager;
    private PlaybacksManager playbacksManager;
    private MetronomesManager metronomesManager;

    private Player player;

    public PlaybackPlayerApplication() {
        scoresManager = new ScoresManager();
        playbacksManager = new PlaybacksManager();
        metronomesManager = new MetronomesManager();

        player = new Player();
    }

    public ScoresManager getScoresManager() {
        return scoresManager;
    }

    public void setScoresManager(ScoresManager scoresManager) {
        this.scoresManager = scoresManager;
    }

    public PlaybacksManager getPlaybacksManager() {
        return playbacksManager;
    }

    public void setPlaybacksManager(PlaybacksManager playbacksManager) {
        this.playbacksManager = playbacksManager;
    }

    public MetronomesManager getMetronomesManager() {
        return metronomesManager;
    }

    public void setMetronomesManager(MetronomesManager metronomesManager) {
        this.metronomesManager = metronomesManager;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
