package com.hard.playback_player.utils.managers;

import com.hard.playback_player.models.Playback;
import com.hard.playback_player.models.Song;
import com.hard.playback_player.settings.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PlaybacksManager {
    private Song song;
    private Playback currentPlayback;

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;

        setCurrentPlayback();
    }

    public Playback getCurrentPlayback() {
        return currentPlayback;
    }

    public void setCurrentPlayback(int id) {
        Collection<Playback> playbacks = song.getPlaybacks();

        try {
            Playback playback = new ArrayList<>(playbacks).get(id);
            currentPlayback = playback;
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    private void setCurrentPlayback() {
        if (song.getPlaybacks() == null)
            return;

        Playback originalPlayback = null;
        Map<Byte, Playback> notOriginalPlaybacks = new HashMap<>();

        Collection<Playback> playbacks = song.getPlaybacks();
        Iterator<Playback> iterator = playbacks.iterator();
        while (iterator.hasNext()) {
            Playback playback = iterator.next();

            if (playback.getTitle().equalsIgnoreCase(Constants.ORIGINAL))
                originalPlayback = playback;

            try {
                byte transposition = Byte.parseByte(playback.getTitle());
                notOriginalPlaybacks.put(transposition, playback);
            } catch (NumberFormatException e) {
            }
        }

        if (notOriginalPlaybacks.size() > 0) {
            Playback notTransposedPlayback = notOriginalPlaybacks.get((byte) 0);
            if (notTransposedPlayback != null)
                currentPlayback = notTransposedPlayback;
            else {
                Set<Byte> transpositions = notOriginalPlaybacks.keySet();
                byte minTransposition = Collections.min(transpositions);
                Playback transposedPlayback = notOriginalPlaybacks.get(minTransposition);
                currentPlayback = transposedPlayback;
            }
        } else if (originalPlayback != null)
            currentPlayback = originalPlayback;
    }
}
