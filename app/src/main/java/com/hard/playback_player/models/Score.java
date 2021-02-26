package com.hard.playback_player.models;

import com.hard.playback_player.models.abstracts.Model;

public class Score extends Model {
    private String title;
    private String url;
    private Song song;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }
}
