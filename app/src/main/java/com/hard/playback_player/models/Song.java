package com.hard.playback_player.models;

import com.hard.playback_player.models.abstracts.Model;

import java.util.Collection;
import java.util.Map;

public class Song extends Model {
    private String title;
    private String keySignature;
    private Band band;
    private Text text;
    private Collection<Score> scores;
    private Collection<Playback> playbacks;
    private Collection<Metronome> metronomes;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeySignature() {
        return keySignature;
    }

    public void setKeySignature(String keySignature) {
        this.keySignature = keySignature;
    }

    public Band getBand() {
        return band;
    }

    public void setBand(Band band) {
        this.band = band;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public Collection<Score> getScores() {
        return scores;
    }

    public void setScores(Collection<Score> scores) {
        this.scores = scores;
    }

    public Collection<Playback> getPlaybacks() {
        return playbacks;
    }

    public void setPlaybacks(Collection<Playback> playbacks) {
        this.playbacks = playbacks;
    }

    public Collection<Metronome> getMetronomes() {
        return metronomes;
    }

    public void setMetronomes(Collection<Metronome> metronomes) {
        this.metronomes = metronomes;
    }
}
