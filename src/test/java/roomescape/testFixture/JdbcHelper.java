package roomescape.testFixture;

import java.util.Arrays;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.auth.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;

public class JdbcHelper {
    public static void insertTheme(JdbcTemplate template, Theme theme) {
        template.update("INSERT INTO theme (name, description, thumbnail) VALUES (?,?,?)",
                theme.getName(), theme.getDescription(), theme.getThumbnail());
    }

    public static void insertThemes(JdbcTemplate template, Theme... themes) {
        Arrays.stream(themes)
                .forEach(theme -> insertTheme(template, theme));
    }

    public static void insertReservationTime(JdbcTemplate template, ReservationTime reservationTime) {
        template.update("INSERT INTO reservation_time (start_at) VALUES (?)",
                reservationTime.getStartAt());
    }

    public static void insertReservationTimes(JdbcTemplate template, ReservationTime... reservationTimes) {
        Arrays.stream(reservationTimes)
                .forEach(reservationTime -> insertReservationTime(template, reservationTime));
    }

    public static void insertReservation(JdbcTemplate template, Reservation reservation) {
        template.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                reservation.getName(), reservation.getReservationDate(), reservation.getReservationTime().getId(),
                reservation.getTheme().getId());
    }

    public static void insertReservations(JdbcTemplate template, Reservation... reservations) {
        Arrays.stream(reservations)
                .forEach(reservation -> insertReservation(template, reservation));
    }

    public static void insertMember(JdbcTemplate jdbcTemplate, Member member) {
        jdbcTemplate.update("INSERT INTO members (email, password, name) VALUES (?, ?, ?)",
                member.getEmail(), member.getPassword(), member.getName());
    }
}
