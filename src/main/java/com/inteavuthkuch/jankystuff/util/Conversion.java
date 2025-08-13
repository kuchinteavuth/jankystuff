package com.inteavuthkuch.jankystuff.util;

public class Conversion {
    public static int tickFromSecond(int second) {
        return 20 * second;
    }

    public static int tickFromMinute(int minute) {
        return 20 * minute * 60;
    }

    public static int tickFromHour(int hour) {
        return 20 * hour * 60 * 60;
    }

    public static String formatDuration(int ticks) {
        int seconds = ticks / 20;
        int minutes = seconds / 60;
        seconds = seconds % 60;

        return String.format("%d:%02d", minutes, seconds);
    }
}
