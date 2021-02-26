package com.hard.playback_player.models;

import com.hard.playback_player.models.abstracts.Model;

public class Text extends Model {
    private String url;
    private Song song;

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
