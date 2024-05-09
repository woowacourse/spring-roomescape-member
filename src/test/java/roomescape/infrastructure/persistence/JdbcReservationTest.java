package roomescape.infrastructure.persistence;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import roomescape.domain.PlayerName;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeName;
import roomescape.domain.ThemeRepository;

@JdbcTest
public abstract class JdbcReservationTest {
    protected final JdbcTemplate jdbcTemplate;
    protected final SimpleJdbcInsert jdbcInsert;
    protected final ThemeRepository themeRepository;
    protected final ReservationTimeRepository reservationTimeRepository;

    @Autowired
    public JdbcReservationTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingColumns("name", "date", "time_id", "theme_id")
                .usingGeneratedKeyColumns("id");
        this.themeRepository = new JdbcThemeRepository(jdbcTemplate);
        this.reservationTimeRepository = new JdbcReservationTimeRepository(jdbcTemplate);
    }

    protected Reservation createReservation() {
        ReservationTime reservationTime = reservationTimeRepository.create(new ReservationTime(LocalTime.of(12, 0)));
        Theme theme = themeRepository.create(new Theme(new ThemeName("theme1"), "desc", "url"));
        LocalDate date = LocalDate.of(2024, 12, 25);
        long id = jdbcInsert.executeAndReturnKey(Map.of(
                "name", "test",
                "date", date,
                "time_id", reservationTime.getId(),
                "theme_id", theme.getId()
        )).longValue();
        return new Reservation(id, new PlayerName("test"), date, reservationTime, theme);
    }
}
