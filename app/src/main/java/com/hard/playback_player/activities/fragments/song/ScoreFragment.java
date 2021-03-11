package com.hard.playback_player.activities.fragments.song;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.barteksc.pdfviewer.PDFView;
import com.hard.playback_player.R;
import com.hard.playback_player.activities.SongActivity;
import com.hard.playback_player.app.PlaybackPlayerApplication;
import com.hard.playback_player.models.Score;
import com.hard.playback_player.models.Song;
import com.hard.playback_player.settings.Constants;
import com.hard.playback_player.utils.Screen;
import com.hard.playback_player.utils.managers.ScoresManager;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;

public class ScoreFragment extends Fragment {
    private static final String TITLE = "title";
    private static final String SONG = "song";

    private String title;
    private Song song;

    private ScoresManager scoresManager;
    private Screen screen;

    private View view;

    private int group_id = 1;

    // Views
    private SwipeRefreshLayout song_score_swipeRefreshLayout;

    public ScoreFragment() {

    }

    public static ScoreFragment newInstance(String title, Song song) {
        ScoreFragment fragment = new ScoreFragment();

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
        scoresManager = application.getScoresManager();

        SongActivity songActivity = (SongActivity) getActivity();
        screen = songActivity.getScreen();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_score, container, false);

        song_score_swipeRefreshLayout = view.findViewById(R.id.song_score_swipeRefreshLayout);

        // menu >
        LinearLayout score_menu_layout = view.findViewById(R.id.score_menu_layout);
        score_menu_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerForContextMenu(v);
                getActivity().openContextMenu(v);
            }
        });
        // menu <

        song_score_swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                update(view);
            }
        });

        update(view);

        return view;
    }

    private void update(View view) {
        if (scoresManager.getCurrentScore() == null)
            return;

        song_score_swipeRefreshLayout.setRefreshing(true);

        updateScreen(view);

        // update pdfView

        PDFView score_pdfView = view.findViewById(R.id.score_pdfView);

        String url = Constants.GOOGLE_DRIVE_FILE + scoresManager.getCurrentScore().getUrl();

        new Thread(() -> {
            try {
                score_pdfView.fromStream(new URL(url).openStream())
//                        .pages(0, 1, 2) // all pages are displayed by default
                        .enableSwipe(true) // allows to block changing pages using swipe
                        .swipeHorizontal(scoresManager.isHorizontal())
                        .enableDoubletap(false)
                        .defaultPage(0)
                        // allows to draw something on the current page, usually visible in the middle of the screen
//                        .onDraw(onDrawListener)
                        // allows to draw something on all pages, separately for every page. Called only for visible pages
//                        .onDrawAll(onDrawListener)
//                        .onLoad(onLoadCompleteListener) // called after document is loaded and starts to be rendered
//                        .onPageChange(onPageChangeListener)
//                        .onPageScroll(onPageScrollListener)
//                        .onError(onErrorListener)
//                        .onPageError(onPageErrorListener)
//                        .onRender(onRenderListener) // called after document is rendered for the first time
                        // called on single tap, return true if handled, false to toggle scroll handle visibility
//                        .onTap(onTapListener)
//                        .onLongPress(onLongPressListener)
                        .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                        .password(null)
                        .scrollHandle(null)
                        .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                        // spacing between pages in dp. To define spacing color, set view background
                        .spacing(1)
//                        .autoSpacing(false) // add dynamic spacing to fit each page on its own on the screen
//                        .linkHandler(DefaultLinkHandler)
//                        .pageFitPolicy(FitPolicy.WIDTH) // mode to fit pages in the view
//                        .fitEachPage(false) // fit each page to the view, else smaller pages are scaled relative to largest page.
//                        .pageSnap(false) // snap pages to screen boundaries
//                        .pageFling(false) // make a fling change only a single page like ViewPager
//                        .nightMode(false) // toggle night mode
                        .load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            getActivity().runOnUiThread(() -> {
                song_score_swipeRefreshLayout.setRefreshing(false);
            });
        }).start();
    }

    private void updateScreen(View view) {
        // current score
        Score currentScore = scoresManager.getCurrentScore();
        screen.setScore(currentScore);

        // score orientation
        if (scoresManager.isVertical())
            screen.setVerticalOrientation();
        else
            screen.setHorizontalOrientation();
    }

    /**
     * Context Menu
     */

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);

        if (song.getScores() == null)
            return;

        int menu_id = 0;

        Collection<Score> scores = song.getScores();
        Iterator<Score> iterator = scores.iterator();
        while (iterator.hasNext()) {
            Score score = iterator.next();

            menu.add(group_id, menu_id, 1, score.getTitle());

            if (score == scoresManager.getCurrentScore()) {
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

        scoresManager.setCurrentScore(id);

        item.setChecked(true);

        update(view);

        return super.onContextItemSelected(item);
    }
}