package com.hard.playback_player.utils;

import android.app.Activity;
import android.widget.TextView;

import com.hard.playback_player.R;
import com.hard.playback_player.models.Score;

public class Screen {
    private static final int LIMIT = 30;

    private Activity activity;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setScore(Score score) {
        TextView score_textView = activity.findViewById(R.id.score_textView);

        if (score == null) {
            clearScore();
            return;
        }

        String title = score.getTitle().length() > LIMIT ? score.getTitle().substring(0, LIMIT) : score.getTitle();
        score_textView.setText(title);
        score_textView.setEnabled(true);
    }

    public void clearScore() {
        TextView score_textView = activity.findViewById(R.id.score_textView);
        score_textView.setText("No Score");
        score_textView.setEnabled(false);
    }

    public void setVerticalOrientation() {
        TextView orientation_textView = activity.findViewById(R.id.orientation_textView);
        orientation_textView.setText("^v");
    }

    public void setHorizontalOrientation() {
        TextView orientation_textView = activity.findViewById(R.id.orientation_textView);
        orientation_textView.setText("<>");
    }

    public void clearOrientation() {
        TextView orientation_textView = activity.findViewById(R.id.orientation_textView);
        orientation_textView.setText("No orientations");
    }

    public void clearScreen() {
        clearScore();
        clearOrientation();
    }
}
