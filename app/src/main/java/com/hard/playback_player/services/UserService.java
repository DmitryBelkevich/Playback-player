package com.hard.playback_player.services;

import com.hard.playback_player.models.User;
import com.hard.playback_player.repositories.UserRepository;

import java.util.Collection;

public class UserService {
    private UserRepository userRepository = new UserRepository();

    public Collection<User> getAll() {
        return userRepository.getAll();
    }

    public User getById(long id) {
        return userRepository.getById(id);
    }
}
