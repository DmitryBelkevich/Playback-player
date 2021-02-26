package com.hard.playback_player.models;

import com.hard.playback_player.models.abstracts.Model;

public class Settings extends Model {
    private static boolean modeNight = true;
    private static boolean keepScreenOn = true;

    private static String defaultScore = "Full Score";
    private static int volume = 100;

    public static boolean isModeNight() {
        return modeNight;
    }

    public static void setModeNight(boolean modeNight) {
        Settings.modeNight = modeNight;
    }

    public static boolean isKeepScreenOn() {
        return keepScreenOn;
    }

    public static void setKeepScreenOn(boolean keepScreenOn) {
        Settings.keepScreenOn = keepScreenOn;
    }

    public static String getDefaultScore() {
        return defaultScore;
    }

    public static void setDefaultScore(String defaultScore) {
        Settings.defaultScore = defaultScore;
    }

    public static int getVolume() {
        return volume;
    }

    public static void setVolume(int volume) {
        Settings.volume = volume;
    }
}
