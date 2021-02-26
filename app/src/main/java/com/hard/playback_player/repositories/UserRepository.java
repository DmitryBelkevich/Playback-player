package com.hard.playback_player.repositories;

import com.hard.playback_player.models.Settings;
import com.hard.playback_player.models.Song;
import com.hard.playback_player.models.User;
import com.hard.playback_player.services.SongService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class UserRepository {
    private SongService songService = new SongService();

    public Collection<User> getAll() {
        Collection<User> users = new ArrayList<>();

        User user = new User();

        user.setId(1);
        user.setName("Hard");

        // user.settings
        Settings settings = new Settings();
        user.setSettings(settings);

        // user.songs
        Collection<Song> songs = songService.getAll();
        user.setSongs(songs);

        users.add(user);

        return users;
    }

    public User getById(long id) {
        Collection<User> users = getAll();

        Iterator<User> iterator = users.iterator();
        while (iterator.hasNext()) {
            User user = iterator.next();

            if (user.getId() == id)
                return user;
        }

        return null;
    }
}
