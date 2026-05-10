package roomescape.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import roomescape.domain.reservationtime.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationTimeQueryingDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ReservationTimeQueryingDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ReservationTime> reservationTimeRowMapper = (resultSet, rowNum) -> {
        ReservationTime reservationTime = new ReservationTime(
                resultSet.getLong("id"),
                resultSet.getObject("start_at", LocalTime.class)
        );
        return reservationTime;
    };

    public Optional<ReservationTime> findReservationTimeById(long id) {
        String sql = "select id, start_at from reservation_time where id = :id";
        try {
            SqlParameterSource param = new MapSqlParameterSource()
                    .addValue("id", id);
            ReservationTime reservationTime = jdbcTemplate.queryForObject(sql, param, reservationTimeRowMapper);
            return Optional.of(reservationTime);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    public List<ReservationTime> findAllReservationTime(LocalDate date, Long themeId) {
        if (date == null && themeId == null) {
            String sql = """
                   select t.id, t.start_at from reservation_time as t
                   """;
            return jdbcTemplate.query(sql, reservationTimeRowMapper);
        }

        String sql = """
                select t.id, t.start_at from reservation_time as t
                left join reservation as r on t.id = r.time_id
                                        and r.date = :date
                                        and r.theme_id = :theme_id
                where r.id is null;
                """;

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("date", date)
                .addValue("theme_id", themeId);
        return jdbcTemplate.query(sql, param, reservationTimeRowMapper);
    }
}
