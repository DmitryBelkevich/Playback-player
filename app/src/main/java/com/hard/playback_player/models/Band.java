package com.hard.playback_player.models;

import com.hard.playback_player.models.abstracts.Model;

import java.util.Collection;

public class Band extends Model {
    private String title;
    private Collection<Song> songs;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Collection<Song> getSongs() {
        return songs;
    }

    public void setSongs(Collection<Song> songs) {
        this.songs = songs;
    }
}
