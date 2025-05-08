package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;

@Repository
public class RoomescapeRepositoryImpl implements RoomescapeRepository {

    private static final int SUCCESS_COUNT = 1;

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert insert;
    private final RowMapper<Reservation> reservationRowMapper;

    public RoomescapeRepositoryImpl(final DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.insert = new SimpleJdbcInsert(dataSource).withTableName("reservation").usingGeneratedKeyColumns("id");
        this.reservationRowMapper = (rs, rowNum) -> {
            ReservationTime reservationTime = new ReservationTime(rs.getLong("time_id"),
                    rs.getTime("time_value").toLocalTime());
            ReservationTheme reservationTheme = new ReservationTheme(rs.getLong("theme_id"), rs.getString("theme_name"),
                    rs.getString("theme_description"), rs.getString("theme_thumbnail"));
            return new Reservation(rs.getLong("reservation_id"), rs.getString("name"), rs.getDate("date").toLocalDate(),
                    reservationTime, reservationTheme);
        };
    }

    @Override
    public Optional<Reservation> findById(final Long id) {
        String sql = joinReservationAndTime("WHERE r.id = :id");
        try {
            SqlParameterSource param = new MapSqlParameterSource("id", id);
            Reservation reservation = template.queryForObject(sql, param, reservationRowMapper);
            return Optional.of(reservation);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Reservation> findByDate(final LocalDate date) {
        String sql = joinReservationAndTime("WHERE r.date = :date");
        SqlParameterSource param = new MapSqlParameterSource("date", date);
        return template.query(sql, param, reservationRowMapper);
    }

    @Override
    public List<Reservation> findAll() {
        String sql = joinReservationAndTime("");
        return template.query(sql, reservationRowMapper);
    }

    @Override
    public Reservation save(final Reservation reservation) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("name", reservation.getName())
                .addValue("date", reservation.getDate().toString()).addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getTheme().getId());
        Number key = insert.executeAndReturnKey(param);
        return reservation.assignId(key.longValue());
    }

    @Override
    public boolean deleteById(final Long id) {
        String sql = "delete from reservation where id = :id";
        SqlParameterSource param = new MapSqlParameterSource("id", id);
        return template.update(sql, param) == SUCCESS_COUNT;
    }

    @Override
    public boolean existsByThemeId(final Long themeId) {
        String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE theme_id = :theme_id)";
        SqlParameterSource param = new MapSqlParameterSource("theme_id", themeId);
        return Boolean.TRUE.equals(template.queryForObject(sql, param, Boolean.class));
    }

    @Override
    public boolean existsByTimeId(final Long timeId) {
        String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE time_id = :time_id)";
        SqlParameterSource param = new MapSqlParameterSource("time_id", timeId);
        return Boolean.TRUE.equals(template.queryForObject(sql, param, Boolean.class));
    }

    @Override
    public boolean existsByDateAndTime(final LocalDate date, final ReservationTime time) {
        String sql = wrapExistsQuery(joinReservationAndTime("WHERE r.date = :date and t.start_at = :startAt"));
        SqlParameterSource param = new MapSqlParameterSource().addValue("date", date)
                .addValue("startAt", time.getStartAt());
        return Boolean.TRUE.equals(template.queryForObject(sql, param, Boolean.class));
    }

    private String wrapExistsQuery(String sql) {
        return "SELECT EXISTS(" + sql + ")";
    }

    private String joinReservationAndTime(String where) {
        String sql = """
                SELECT r.id AS reservation_id, r.name, r.date, t.id AS time_id, t.start_at AS time_value, th.name AS theme_name, th.description AS theme_description, th.thumbnail AS theme_thumbnail, th.id AS theme_id
                FROM reservation as r 
                INNER JOIN reservation_time AS t
                ON r.time_id = t.id
                INNER JOIN reservation_theme AS th
                ON r.theme_id = th.id
                """;
        return sql + where;
    }
}
