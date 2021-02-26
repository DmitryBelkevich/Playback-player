package com.hard.playback_player.models;

import com.hard.playback_player.models.abstracts.Model;

import java.util.Collection;

public class User extends Model {
    private String name;
    private Settings settings;
    private Collection<Song> songs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public Collection<Song> getSongs() {
        return songs;
    }

    public void setSongs(Collection<Song> songs) {
        this.songs = songs;
    }
}
