package roomescape.testFixture;

import static roomescape.testFixture.Fixture.MEMBER_1;
import static roomescape.testFixture.Fixture.RESERVATION_TIME_1;
import static roomescape.testFixture.Fixture.RESERVATION_TIME_2;
import static roomescape.testFixture.Fixture.RESERVATION_TIME_3;
import static roomescape.testFixture.Fixture.THEME_1;
import static roomescape.testFixture.Fixture.THEME_2;
import static roomescape.testFixture.Fixture.THEME_3;

import java.util.Arrays;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservation.domain.Member;
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

    public static void prepareAndInsertReservation(JdbcTemplate template, Reservation reservation) {
        insertReservationTimes(template, RESERVATION_TIME_1, RESERVATION_TIME_2, RESERVATION_TIME_3);
        insertThemes(template, THEME_1, THEME_2, THEME_3);
        insertMember(template, MEMBER_1);
        template.update("INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                reservation.getMemberId(), reservation.getReservationDate(), reservation.getReservationTime().getId(),
                reservation.getTheme().getId());
    }

    public static void insertReservationOnly(JdbcTemplate template, Reservation reservation) {
        template.update("INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                reservation.getMemberId(), reservation.getReservationDate(), reservation.getReservationTime().getId(),
                reservation.getTheme().getId());
    }

    public static void insertReservations(JdbcTemplate template, Reservation... reservations) {
        insertReservationTimes(template, RESERVATION_TIME_1, RESERVATION_TIME_2, RESERVATION_TIME_3);
        insertThemes(template, THEME_1, THEME_2, THEME_3);
        insertMember(template, MEMBER_1);
        Arrays.stream(reservations)
                .forEach(reservation -> {
                    template.update("INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                            reservation.getMemberId(), reservation.getReservationDate(),
                            reservation.getReservationTime().getId(),
                            reservation.getTheme().getId());
                });
    }

    public static void insertMember(JdbcTemplate jdbcTemplate, Member member) {
        jdbcTemplate.update("INSERT INTO members (email, password, name) VALUES (?, ?, ?)",
                member.getEmail(), member.getPassword(), member.getName());
    }
}
