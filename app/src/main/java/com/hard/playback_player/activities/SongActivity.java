package com.hard.playback_player.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.hard.playback_player.R;
import com.hard.playback_player.activities.fragments.song.PlaybackFragment;
import com.hard.playback_player.activities.fragments.song.ScoreFragment;
import com.hard.playback_player.activities.fragments.song.TextFragment;
import com.hard.playback_player.app.PlaybackPlayerApplication;
import com.hard.playback_player.models.Metronome;
import com.hard.playback_player.models.Score;
import com.hard.playback_player.models.Settings;
import com.hard.playback_player.models.Song;
import com.hard.playback_player.settings.Constants;
import com.hard.playback_player.utils.Player;
import com.hard.playback_player.utils.Screen;
import com.hard.playback_player.utils.managers.MetronomesManager;
import com.hard.playback_player.utils.managers.PlaybacksManager;
import com.hard.playback_player.utils.managers.ScoresManager;

import java.util.ArrayList;
import java.util.List;

public class SongActivity extends AppCompatActivity {
    private ScoresManager scoresManager;
    private PlaybacksManager playbacksManager;
    private MetronomesManager metronomesManager;

    private Player player;
    private Screen screen;

    public Screen getScreen() {
        return screen;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);

        PlaybackPlayerApplication application = (PlaybackPlayerApplication) getApplication();

        scoresManager = application.getScoresManager();
        playbacksManager = application.getPlaybacksManager();
        metronomesManager = application.getMetronomesManager();

        player = application.getPlayer();
        player.setActivity(this);

        // screen >
        screen = new Screen();
        screen.setActivity(this);
        Score currentScore = scoresManager.getCurrentScore();
        screen.setScore(currentScore);
        // screen <

        // init settings >
        if (Settings.isKeepScreenOn())
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // init settings <

        Intent intent = getIntent();
        Song song = (Song) intent.getSerializableExtra("song");

        scoresManager.setSong(song);
        playbacksManager.setSong(song);
        metronomesManager.setSong(song);

        // Toolbar >
        Toolbar toolbar = findViewById(R.id.song_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        actionBar.setDisplayHomeAsUpEnabled(true);
        // Toolbar <

        // Screen >
        TextView key_signature_textView = findViewById(R.id.key_signature_textView);
        key_signature_textView.setText(song.getKeySignature());

        Metronome metronome = metronomesManager.getCurrentMetronome();
        if (metronome != null) {
            // time signature
            TextView time_signature_textView = findViewById(R.id.time_signature_textView);
            String timeSignature = metronome.getNumerator() + "/" + metronome.getDenominator();
            time_signature_textView.setText(timeSignature);

            // tempo
            TextView tempo_textView = findViewById(R.id.tempo_textView);
            String tempo = String.valueOf(metronome.getTempo());
            tempo_textView.setText(tempo);
        }
        // Screen <

        // Panel >
        Button play_button = findViewById(R.id.play_button);
        Button stop_button = findViewById(R.id.stop_button);

        if (player.isPlaying()) {
            play_button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_pause_24, 0, 0, 0);
        }

        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Constants.GOOGLE_DRIVE_FILE + playbacksManager.getCurrentPlayback().getUrl();

                player.setUrl(url);

                player.play();

                play_button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_pause_24, 0, 0, 0);
            }
        });

        stop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.stop();
            }
        });
        // Panel >

        // View Pager >
        ViewPager viewPager = findViewById(R.id.song_viewPager);
        TabLayout tabLayout = findViewById(R.id.song_tabLayout);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Fragments >
        if (song.getText() != null && song.getText().getUrl() != null) {
            TextFragment textFragment = TextFragment.newInstance("Text", song);
            viewPagerAdapter.addItem(textFragment);
        }

        if (song.getScores() != null && !song.getScores().isEmpty()) {
            ScoreFragment scoreFragment = ScoreFragment.newInstance("Score", song);
            viewPagerAdapter.addItem(scoreFragment);
        }

        if (song.getPlaybacks() != null && !song.getPlaybacks().isEmpty()) {
            PlaybackFragment playbackFragment = PlaybackFragment.newInstance("Playback", song);
            viewPagerAdapter.addItem(playbackFragment);
        }
        // Fragments <

        viewPager.setOffscreenPageLimit(viewPagerAdapter.getCount() - 1);

        viewPager.setAdapter(viewPagerAdapter);
        // View Pager <

        // Menu >
        LinearLayout screen_linearLayout = findViewById(R.id.screen_linearLayout);

        screen_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout score_menu_layout = findViewById(R.id.score_menu_layout);
                score_menu_layout.callOnClick();
            }
        });

        screen_linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                LinearLayout playback_menu_layout = findViewById(R.id.playback_menu_layout);
                playback_menu_layout.callOnClick();
                return true;
            }
        });
        // Menu <

        setResult(1, intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        screen.clearScreen();
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private static final String TITLE = "title";

        private List<Fragment> fragments = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        public void addItem(Fragment fragment) {
            fragments.add(fragment);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            Fragment fragment = fragments.get(position);
            Bundle bundle = fragment.getArguments();
            String title = bundle.getString(TITLE);
            return title;
        }
    }

    /**
     * Menu
     */

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}