package com.hard.playback_player.activities.fragments.song;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hard.playback_player.R;
import com.hard.playback_player.app.PlaybackPlayerApplication;
import com.hard.playback_player.models.Playback;
import com.hard.playback_player.models.Song;
import com.hard.playback_player.utils.managers.PlaybacksManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class PlaybackFragment extends Fragment {
    private static final String TITLE = "title";
    private static final String SONG = "song";

    private String title;
    private Song song;

    private PlaybacksManager playbacksManager;

    private View view;

    private int group_id = 2;

    public PlaybackFragment() {

    }

    public static PlaybackFragment newInstance(String title, Song song) {
        PlaybackFragment fragment = new PlaybackFragment();

        Bundle bundle = new Bundle();

        bundle.putString(TITLE, title);
        bundle.putSerializable(SONG, song);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            title = getArguments().getString(TITLE);
            song = (Song) getArguments().getSerializable(SONG);
        }

        PlaybackPlayerApplication application = (PlaybackPlayerApplication) getActivity().getApplication();
        playbacksManager = application.getPlaybacksManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_playback, container, false);

        // menu >
        LinearLayout playback_menu_layout = view.findViewById(R.id.playback_menu_layout);
        playback_menu_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerForContextMenu(v);
                getActivity().openContextMenu(v);
            }
        });
        // menu <

        update(view);

        return view;
    }

    private void update(View view) {
        if (playbacksManager.getCurrentPlayback() == null)
            return;

        updateScreen(view);
    }

    private void updateScreen(View view) {
        TextView playback_screen = view.findViewById(R.id.playback_screen);
        playback_screen.setEnabled(true);
        playback_screen.setText(playbacksManager.getCurrentPlayback().getTitle());

        TextView playback_textView = getActivity().findViewById(R.id.playback_textView);
        playback_textView.setText(playbacksManager.getCurrentPlayback().getTitle());
    }

    /**
     * Context Menu
     */

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);

        if (song.getPlaybacks() == null)
            return;

        int menu_id = 0;

        Collection<Playback> playbacks = song.getPlaybacks();
        Iterator<Playback> iterator = playbacks.iterator();
        while (iterator.hasNext()) {
            Playback playback = iterator.next();

            menu.add(group_id, menu_id, 1, playback.getTitle());

            if (playback == playbacksManager.getCurrentPlayback()) {
                MenuItem menuItem = menu.getItem(menu_id);
                menuItem.setChecked(true);
            }

            menu_id++;
        }

        menu.setGroupCheckable(group_id, true, true);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getGroupId() != group_id)
            return false;

        if (item.isChecked())
            return true;

        int id = item.getItemId();

        playbacksManager.setCurrentPlayback(id);

        item.setChecked(true);

        update(view);

        return super.onContextItemSelected(item);
    }
}