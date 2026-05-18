package roomescape.fixture;

import roomescape.dao.row.ReservationRow;
import roomescape.dao.row.ThemeRow;
import roomescape.dao.row.TimeRow;

import java.util.HashMap;
import java.util.Map;

public class FakeDatabase {
    public static final Map<Long, ThemeRow> themes = new HashMap<>();
    public static final Map<Long, TimeRow> times = new HashMap<>();
    public static final Map<Long, ReservationRow> reservations = new HashMap<>();
    public static long themeSequence = 0L;
    public static long timeSequence = 0L;
    public static long reservationSequence = 0L;

    public static Long generateTimeId() {
        return ++timeSequence;
    }

    public static Long generateThemeId() {
        return ++themeSequence;
    }

    public static synchronized Long generateReservationId() {
        return ++reservationSequence;
    }

    public static void clearAll() {
        clearReservation();
        clearTheme();
        clearTime();
    }

    public static void clearReservation() {
        reservations.clear();
        reservationSequence = 0L;
    }

    public static void clearTheme() {
        themes.clear();
        themeSequence = 0L;
    }

    public static void clearTime() {
        times.clear();
        timeSequence = 0L;
    }
}
