package com.hard.playback_player.utils.managers;

import com.hard.playback_player.models.Metronome;
import com.hard.playback_player.models.Song;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class MetronomesManager {
    private Song song;
    private Metronome currentMetronome;

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;

        setCurrentMetronome();
    }

    public Metronome getCurrentMetronome() {
        return currentMetronome;
    }

    public void setCurrentMetronome(int id) {
        Collection<Metronome> metronomes = song.getMetronomes();

        try {
            Metronome metronome = new ArrayList<>(metronomes).get(id);
            currentMetronome = metronome;
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    private void setCurrentMetronome() {
        if (song.getMetronomes() == null)
            return;

        Collection<Metronome> metronomes = song.getMetronomes();
        Iterator<Metronome> iterator = metronomes.iterator();
        while (iterator.hasNext()) {
            Metronome metronome = iterator.next();

            currentMetronome = metronome;

            break;
        }
    }
}
