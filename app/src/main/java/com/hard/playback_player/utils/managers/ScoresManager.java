package com.hard.playback_player.utils.managers;

import com.hard.playback_player.models.Score;
import com.hard.playback_player.models.Settings;
import com.hard.playback_player.models.Song;
import com.hard.playback_player.settings.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ScoresManager {
    private Song song;
    private Score currentScore;
    private boolean horizontal = true;

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;

        setCurrentScore();
        setScoreOrientation();
    }

    public Score getCurrentScore() {
        return currentScore;
    }

    private void setCurrentScore() {
        if (song.getScores() == null)
            return;

        Collection<Score> scores = song.getScores();
        Iterator<Score> iterator = scores.iterator();
        while (iterator.hasNext()) {
            Score score = iterator.next();

            if (score.getTitle().contains(Settings.getDefaultScore())) {
                currentScore = score;
                break;
            }
        }
    }

    public void setCurrentScore(int id) {
        Collection<Score> scores = song.getScores();
        try {
            Score score = new ArrayList<>(scores).get(id);
            currentScore = score;
            setScoreOrientation();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    private void setScoreOrientation() {
        if (currentScore == null)
            return;

        if (currentScore.getTitle().equals(Constants.FULL_SCORE))
            horizontal = true;
        else
            horizontal = false;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

    public boolean isVertical() {
        return !horizontal;
    }

    public void setVertical(boolean vertical) {
        this.horizontal = !vertical;
    }
}
