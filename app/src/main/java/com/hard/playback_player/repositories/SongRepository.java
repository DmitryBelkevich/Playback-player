package com.hard.playback_player.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hard.playback_player.models.Band;
import com.hard.playback_player.models.Metronome;
import com.hard.playback_player.models.Playback;
import com.hard.playback_player.models.Score;
import com.hard.playback_player.models.Song;
import com.hard.playback_player.models.Text;
import com.hard.playback_player.settings.Constants;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class SongRepository {
    public Collection<Song> getAll() {
        Collection<Song> songs = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();

        String url = Constants.GOOGLE_DRIVE_FILE + Constants.DATABASE;

        try {
            Collection<Band> bands = new ArrayList<>();

            Collection<LinkedHashMap> bandsMap = new ArrayList<>();
            bandsMap = mapper.readValue(new URL(url), bandsMap.getClass());
            Iterator<LinkedHashMap> bandIterator = bandsMap.iterator();
            while (bandIterator.hasNext()) {
                LinkedHashMap bandMap = bandIterator.next();

                Band band = buildBand(bandMap);

                Collection<Song> songsInBand = band.getSongs();

                songs.addAll(songsInBand);

                bands.add(band);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return songs;
    }

    // TODO parser

    private Band buildBand(LinkedHashMap bandMap) {
        Band band = new Band();

        // band.id
        long id = ((Number) bandMap.get("id")).longValue();
        band.setId(id);

        // band.title
        String title = (String) bandMap.get("title");
        band.setTitle(title);

        // band.songs
        Collection<Song> songs = new ArrayList<>();

        Collection<LinkedHashMap> songs_collectionsMaps = (Collection<LinkedHashMap>) bandMap.get("songs");
        Iterator<LinkedHashMap> iterator = songs_collectionsMaps.iterator();
        while (iterator.hasNext()) {
            LinkedHashMap songMap = iterator.next();

            Song song = buildSong(songMap, band);

            songs.add(song);
        }

        band.setSongs(songs);

        return band;
    }

    private Song buildSong(LinkedHashMap songMap, Band band) {
        Song song = new Song();

        // song.id
        long id = ((Number) songMap.get("id")).longValue();
        song.setId(id);

        // song.title
        String title = (String) songMap.get("title");
        song.setTitle(title);

        // song.band
        song.setBand(band);

        // song.keySignature
        String keySignature = (String) songMap.get("key_signature");
        song.setKeySignature(keySignature);

        // song.text
        LinkedHashMap textMap = (LinkedHashMap) songMap.get("text");
        if (textMap != null)
            buildText(textMap, song);

        // song.scores
        Collection<LinkedHashMap> scoresMap = (Collection<LinkedHashMap>) songMap.get("scores");
        if (scoresMap != null)
            buildScores(scoresMap, song);

        // song.playbacks
        Collection<LinkedHashMap> playbacksMap = (Collection<LinkedHashMap>) songMap.get("playbacks");
        if (playbacksMap != null)
            buildPlaybacks(playbacksMap, song);

        // song.metronomes
        Collection<LinkedHashMap> metronomesMap = (Collection<LinkedHashMap>) songMap.get("metronomes");
        if (metronomesMap != null)
            buildMetronomes(metronomesMap, song);

        return song;
    }

    private void buildText(LinkedHashMap textMap, Song song) {
        Text text = new Text();

        // text.id
        long id = ((Number) textMap.get("id")).longValue();
        text.setId(id);

        // text.url
        String url = (String) textMap.get("url");
        text.setUrl(url);

        // text.song
        text.setSong(song);

        song.setText(text);
    }

    private void buildScores(Collection<LinkedHashMap> scoresMap, Song song) {
        Collection<Score> scores = new ArrayList<>();

        Iterator<LinkedHashMap> iterator = scoresMap.iterator();
        while (iterator.hasNext()) {
            LinkedHashMap linkedHashMap = iterator.next();

            Score score = new Score();

            // score.id
            long id = ((Number) linkedHashMap.get("id")).longValue();
            score.setId(id);

            // score.title
            String title = (String) linkedHashMap.get("title");
            score.setTitle(title);

            // score.url
            String url = (String) linkedHashMap.get("url");
            score.setUrl(url);

            // score.song
            score.setSong(song);

            scores.add(score);
        }

        song.setScores(scores);
    }

    private void buildPlaybacks(Collection<LinkedHashMap> playbacksMap, Song song) {
        Collection<Playback> playbacks = new ArrayList<>();

        Iterator<LinkedHashMap> iterator = playbacksMap.iterator();
        while (iterator.hasNext()) {
            LinkedHashMap linkedHashMap = iterator.next();

            Playback playback = new Playback();

            // playback.id
            long id = ((Number) linkedHashMap.get("id")).longValue();
            playback.setId(id);

            // playback.title
            String title = (String) linkedHashMap.get("title");
            playback.setTitle(title);

            // playback.url
            String url = (String) linkedHashMap.get("url");
            playback.setUrl(url);

            // playback.song
            playback.setSong(song);

            playbacks.add(playback);
        }

        song.setPlaybacks(playbacks);
    }

    private void buildMetronomes(Collection<LinkedHashMap> metronomeMap, Song song) {
        Collection<Metronome> metronomes = new ArrayList<>();

        Iterator<LinkedHashMap> iterator = metronomeMap.iterator();
        while (iterator.hasNext()) {
            LinkedHashMap linkedHashMap = iterator.next();

            Metronome Metronome = new Metronome();

            // Metronome.id
            long id = ((Number) linkedHashMap.get("id")).longValue();
            Metronome.setId(id);

            // Metronome.title
            String title = (String) linkedHashMap.get("title");
            Metronome.setTitle(title);

            // Metronome.numerator
            int numerator = (int) linkedHashMap.get("numerator");
            Metronome.setNumerator(numerator);

            // Metronome.denominator
            int denominator = (int) linkedHashMap.get("denominator");
            Metronome.setDenominator(denominator);

            // Metronome.tempo
            int tempo = (int) linkedHashMap.get("tempo");
            Metronome.setTempo(tempo);

            // Metronome.song
            Metronome.setSong(song);

            metronomes.add(Metronome);
        }

        song.setMetronomes(metronomes);
    }
}
