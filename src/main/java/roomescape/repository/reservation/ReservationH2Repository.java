package roomescape.repository.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.sql.DataSource;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Repository
public class ReservationH2Repository implements ReservationRepository {

    private static final String TABLE_NAME = "RESERVATION";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ReservationH2Repository(JdbcTemplate jdbcTemplate, DataSource source) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(source)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Reservation save(Reservation reservation) {
        validateDateTime(reservation);
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.name().getName())
                .addValue("date", reservation.date(DateTimeFormatter.ISO_DATE))
                .addValue("time_id", reservation.time().id())
                .addValue("theme_id", reservation.getTheme().getId());
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return new Reservation(id, reservation.name(), reservation.date(), reservation.time(), reservation.getTheme());
    }

    private void validateDateTime(Reservation reservation) {
        LocalDateTime localDateTime = LocalDateTime.of(reservation.date(), reservation.time().startAt());
        LocalDateTime now = LocalDateTime.now();

        if (localDateTime.isBefore(now)) {
            throw new IllegalArgumentException("과거 시간은 예약할 수 없습니다.");
        }
        if (isDuplicatedDateTime(reservation)) {
            throw new IllegalArgumentException("이미 예약된 시간에는 예약할 수 없습니다.");
        }
    }

    private boolean isDuplicatedDateTime(Reservation reservation) {
        String sql = "SELECT * FROM reservation WHERE date = ? AND time_id = ? AND theme_id = ?";
        return !jdbcTemplate.query(sql, (rs, rowNum) -> 0, reservation.date(), reservation.time().id(), reservation.getTheme().getId()).isEmpty();
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM RESERVATION WHERE id = ?", id);
    }

    @Override
    public List<Reservation> findAll() {
        return jdbcTemplate.query(
                "SELECT r.id as reservation_id, r.name, r.date, time.id as time_id, time.start_at as time_value,theme.id as theme_id, theme.name as theme_name, theme.description, theme.thumbnail "
                        + "FROM reservation as r "
                        + "inner join reservation_time as time on r.time_id = time.id "
                        + "inner join theme on r.theme_id = theme.id",
                getReservationRowMapper()
        );
    }

    private RowMapper<Reservation> getReservationRowMapper() {
        return (resultSet, rowNum) -> {
            ReservationTime reservationTime = new ReservationTime(
                    resultSet.getLong("time_id"),
                    LocalTime.parse(resultSet.getString("time_value"))
            );
            Theme theme = new Theme(
                    resultSet.getLong("theme_id"),
                    new Name(resultSet.getString("theme_name")),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail")
            );
            return new Reservation(
                    resultSet.getLong("id"),
                    new Name(resultSet.getString("name")),
                    LocalDate.parse(resultSet.getString("date")),
                    reservationTime,
                    theme
            );
        };
    }
}
