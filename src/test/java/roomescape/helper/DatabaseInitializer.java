package roomescape.helper;

import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Component
public class DatabaseInitializer {
    private final JdbcTemplate jdbcTemplate;
    private Member member;
    private Theme theme;
    private ReservationTime time;
    private Reservation reservation;

    public DatabaseInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void execute() {
        member = createMember();
        theme = createTheme();
        time = createInitTime();
        reservation = createInitReservation(time, theme);
    }

    private Member createMember() {
        jdbcTemplate.update("INSERT INTO member (name, email, password) VALUES (?, ?, ?)",
                "어드민", "admin@email.com", "password");
        return new Member(1L, "어드민", "admin@email.com", "password");
    }

    private Theme createTheme() {
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "레벨2", "내용이다.", "https://www.naver.com/");
        return new Theme(1L, "레벨2", "내용이다.", "https://www.naver.com/");
    }

    private ReservationTime createInitTime() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        return new ReservationTime(1L, LocalTime.of(10, 0));
    }

    private Reservation createInitReservation(ReservationTime time, Theme theme) {
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "브라운", "2024-08-05", "1", "1");
        return new Reservation(1L, "브라운", LocalDate.of(2024, 8, 5), time, theme);
    }
}
