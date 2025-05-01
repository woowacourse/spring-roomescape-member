package roomescape.unit.fixture;

import java.time.LocalDate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDate;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

@Component
public class ReservationDbFixture {

    private JdbcTemplate jdbcTemplate;

    public ReservationDbFixture(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Reservation 예약_한스_25_4_22_10시_공포(ReservationTime reservationTime, Theme theme) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");

        String name = ReserverNameFixture.한스.getName();
        LocalDate date = ReservationDateFixture.예약날짜_25_4_22.getDate();

        Long id = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("date", date)
                .addValue("time_id", reservationTime.getId())
                .addValue("theme_id", theme.getId())
        ).longValue();

        return new Reservation(id, name, date, reservationTime, theme);
    }

    public Reservation 예약_생성_한스(ReservationDate reservationDate, ReservationTime reservationTime, Theme theme) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");

        String name = ReserverNameFixture.한스.getName();
        LocalDate date = reservationDate.getDate();

        Long id = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("date", date)
                .addValue("time_id", reservationTime.getId())
                .addValue("theme_id", theme.getId())
        ).longValue();

        return new Reservation(id, name, date, reservationTime, theme);
    }


}
