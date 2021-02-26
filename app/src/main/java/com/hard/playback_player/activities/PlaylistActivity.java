package com.hard.playback_player.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hard.playback_player.R;
import com.hard.playback_player.app.PlaybackPlayerApplication;
import com.hard.playback_player.models.Settings;
import com.hard.playback_player.models.Song;
import com.hard.playback_player.models.User;
import com.hard.playback_player.services.UserService;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class PlaylistActivity extends AppCompatActivity {
    private int user_id = 1;
    private UserService userService = new UserService();
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        // init settings >
        if (Settings.isKeepScreenOn())
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (!Settings.isModeNight())
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        // init settings <

        // Toolbar >
        Toolbar toolbar = findViewById(R.id.playlist_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Playlist");
        // Toolbar <

        SwipeRefreshLayout playlist_swipeRefreshLayout = findViewById(R.id.playlist_swipeRefreshLayout);
        ListView playlist_listView = findViewById(R.id.playlist_listView);

        playlist_swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                playlist_swipeRefreshLayout.setRefreshing(true);

                update(playlist_listView);

                playlist_swipeRefreshLayout.setRefreshing(false);
            }
        });

        playlist_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PlaylistActivity.this, SongActivity.class);

                Song song = (Song) parent.getAdapter().getItem(position);

                intent.putExtra("song", song);

                startActivityForResult(intent, 1);
            }
        });

        update(playlist_listView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        Song result_song = (Song) data.getSerializableExtra("song");
//
//        Collection<Song> songs = user.getSongs();
//        Iterator<Song> iterator = songs.iterator();
//        while (iterator.hasNext()) {
//            Song song = iterator.next();
//
//            if (song.getId() == result_song.getId()) {
//                song.setScores(result_song.getScores());
//                song.setCurrentScore(result_song.getCurrentScore());
//
//                song.setPlaybacks(result_song.getPlaybacks());
//                song.setCurrentPlayback(result_song.getCurrentPlayback());
//            }
//        }
    }

    private class PlaylistAdapter extends ArrayAdapter<Song> {
        private Context context;
        private int resource;

        public PlaylistAdapter(@NonNull Context context, int resource, @NonNull List<Song> songs) {
            super(context, resource, songs);

            this.context = context;
            this.resource = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);

            convertView = layoutInflater.inflate(resource, parent, false);

            Song song = getItem(position);

            // title

            TextView playlist_songTitle_textView = convertView.findViewById(R.id.playlist_songTitle_textView);
            playlist_songTitle_textView.setText(song.getTitle());

            TextView playlist_bandTitle_textView = convertView.findViewById(R.id.playlist_bandTitle_textView);
            playlist_bandTitle_textView.setText(song.getBand().getTitle());

            // icons

            if (song.getText() != null && song.getText().getUrl() != null) {
                ImageView songHasText_imageView = convertView.findViewById(R.id.songHasText_imageView);
                songHasText_imageView.setVisibility(View.VISIBLE);
            }

            if (song.getScores() != null && !song.getScores().isEmpty()) {
                ImageView songHasScores_checkBox = convertView.findViewById(R.id.songHasScores_imageView);
                songHasScores_checkBox.setVisibility(View.VISIBLE);
            }

            if (song.getPlaybacks() != null && !song.getPlaybacks().isEmpty()) {
                ImageView songHasPlaybacks_imageView = convertView.findViewById(R.id.songHasPlaybacks_imageView);
                songHasPlaybacks_imageView.setVisibility(View.VISIBLE);
            }

            return convertView;
        }
    }

    public void update(ListView playlist_listView) {
        new Thread(() -> {
            user = userService.getById(user_id);
            Collection<Song> songs = user.getSongs();

            runOnUiThread(() -> {
                ArrayAdapter<Song> arrayAdapter = new PlaylistAdapter(this, R.layout.playlist_item, (List<Song>) songs);
                playlist_listView.setAdapter(arrayAdapter);
            });
        }).start();
    }

    /**
     * Menu
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.playlist_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.settings_item:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}