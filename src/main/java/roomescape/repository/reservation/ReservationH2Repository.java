package roomescape.repository.reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservatorName;
import roomescape.domain.Theme;
import roomescape.domain.ThemeName;

@Repository
public class ReservationH2Repository implements ReservationRepository {

    private static final String TABLE_NAME = "RESERVATION";
    private static final int ANY_INTEGER_FOR_COUNTING = 0;

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
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.getName().name())
                .addValue("date", reservation.getDate(DateTimeFormatter.ISO_DATE))
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getTheme().getId());
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return new Reservation(id, reservation.getName(), reservation.getDate(), reservation.getTime(),
                reservation.getTheme());
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
                    new ThemeName(resultSet.getString("theme_name")),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail")
            );
            return new Reservation(
                    resultSet.getLong("id"),
                    new ReservatorName(resultSet.getString("name")),
                    LocalDate.parse(resultSet.getString("date")),
                    reservationTime,
                    theme
            );
        };
    }

    @Override
    public boolean isAlreadyBooked(Reservation reservation) {
        return !jdbcTemplate.query(
                "SELECT * FROM reservation WHERE date = ? AND time_id = ? AND theme_id = ?",
                (rs, rowNum) -> ANY_INTEGER_FOR_COUNTING,
                reservation.getDate(),
                reservation.getTime().getId(),
                reservation.getTheme().getId()
        ).isEmpty();
    }
}
