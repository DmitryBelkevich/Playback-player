package com.hard.playback_player.activities.fragments.song;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hard.playback_player.R;
import com.hard.playback_player.models.Song;
import com.hard.playback_player.services.FileService;
import com.hard.playback_player.settings.Constants;

public class TextFragment extends Fragment {
    private static final String TITLE = "title";
    private static final String SONG = "song";

    private String title;
    private Song song;

    // Views
    private SwipeRefreshLayout song_text_swipeRefreshLayout;

    public TextFragment() {

    }

    public static TextFragment newInstance(String title, Song song) {
        TextFragment fragment = new TextFragment();

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text, container, false);

        song_text_swipeRefreshLayout = view.findViewById(R.id.song_text_swipeRefreshLayout);

        song_text_swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                update(view, song);
            }
        });

        update(view, song);

        return view;
    }

    private void update(View view, Song song) {
        TextView song_title_textView = view.findViewById(R.id.song_title_textView);
        TextView band_title_textView = view.findViewById(R.id.band_title_textView);
        TextView song_text_textView = view.findViewById(R.id.song_text_textView);

        song_title_textView.setText(song.getTitle());
        band_title_textView.setText(song.getBand().getTitle());

        if (song.getText() == null)
            return;

        song_text_swipeRefreshLayout.setRefreshing(true);

        FileService fileService = new FileService();
        String url = Constants.GOOGLE_DRIVE_FILE + song.getText().getUrl();

        new Thread(() -> {
            String text = fileService.read(url);

            getActivity().runOnUiThread(() -> {
                song_text_textView.setText(text);

                song_text_swipeRefreshLayout.setRefreshing(false);
            });
        }).start();
    }
}