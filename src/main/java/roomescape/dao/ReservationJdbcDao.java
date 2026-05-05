package roomescape.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;

@Repository
public class ReservationJdbcDao implements ReservationDao {
    public static final RowMapper<Reservation> ROW_MAPPER = (rs, rowNum) ->
            new Reservation(
                    rs.getLong("id"),
                    rs.getString("name"),
                    LocalDate.parse(rs.getString("date")),
                    TimeJdbcDao.ROW_MAPPER.mapRow(rs, rowNum),
                    ThemeJdbcDao.ROW_MAPPER.mapRow(rs, rowNum)
            );


    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ReservationJdbcDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                SELECT
                    r.id,
                    r.name,
                    r.date,
                    t.id AS time_id,
                    t.start_at,
                    th.id AS theme_id,
                    th.name,
                    th.thumbnail_url,
                    th.description
                FROM reservations r
                INNER JOIN times t ON r.time_id = t.id
                INNER JOIN themes th ON r.theme_id = th.id;
                """;
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = """
                SELECT
                    r.id,
                    r.name,
                    r.date,
                    t.id AS time_id,
                    t.start_at,
                    th.id AS theme_id,
                    th.name,
                    th.thumbnail_url,
                    th.description
                FROM reservations r
                INNER JOIN times t ON r.time_id = t.id
                INNER JOIN themes th ON r.theme_id = th.id
                WHERE r.id = :id
                """;
        SqlParameterSource params = new MapSqlParameterSource("id", id);
        return jdbcTemplate.query(sql, params, ROW_MAPPER).stream()
                .findFirst();
    }

    @Override
    public Long insert(Reservation reservation) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate())
                .withTableName("reservations")
                .usingGeneratedKeyColumns("id")
                .usingColumns("name", "date", "time_id", "theme_id");

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getTheme().getId());
        
        return simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
    }

    @Override
    public int delete(Long id) {
        String sql = """
                DELETE FROM reservations
                WHERE id = :id
                """;
        SqlParameterSource params = new MapSqlParameterSource("id", id);
        return jdbcTemplate.update(sql, params);
    }
}
