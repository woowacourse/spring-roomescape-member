package roomescape.reservation.repository;

import static roomescape.reservationtime.repository.JdbcReservationTimeRepository.RESERVATION_TIME_ROW_MAPPER;
import static roomescape.theme.repository.JdbcThemeRepository.THEME_ROW_MAPPER;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.exception.ReservationDuplicatedException;
import roomescape.reservation.exception.ReservationNotFoundException;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.theme.entity.Theme;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private static final String SELECT_RESERVATION_WITH_TIME = """
            SELECT
                r.id AS reservation_id,
                r.name AS reservation_name,
                r.date,
                rt.id AS id,
                rt.start_at,
                t.id AS theme_id,
                t.name AS theme_name,
                t.description,
                t.thumbnail_url,
                t.runtime
            FROM reservation r
            INNER JOIN reservation_time rt
                ON r.time_id = rt.id
            INNER JOIN theme t
                ON r.theme_id = t.id
            """;

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<Reservation> reservationRowMapper = (rs, rowNum) ->
            Reservation.of(
                    rs.getLong("reservation_id"),
                    rs.getString("reservation_name"),
                    rs.getObject("date", LocalDate.class),
                    RESERVATION_TIME_ROW_MAPPER.mapRow(rs, rowNum),
                    THEME_ROW_MAPPER.mapRow(rs, rowNum)
            );

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate,
                                     NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                     DataSource source) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(source)
                .withTableName("RESERVATION")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Reservation save(Reservation reservation) {
        ReservationTime time = reservation.getTime();
        Theme theme = reservation.getTheme();
        try {
            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("name", reservation.getName())
                    .addValue("date", reservation.getDate())
                    .addValue("timeId", time.getId())
                    .addValue("themeId", theme.getId());
            Long id = jdbcInsert.executeAndReturnKey(params).longValue();
            return Reservation.toEntity(reservation, id);
        } catch (DuplicateKeyException e) {
            throw new ReservationDuplicatedException(reservation.getDate(), time.getId(), theme.getId());
        }
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = SELECT_RESERVATION_WITH_TIME + "WHERE r.id = :id";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        try {
            Reservation reservation = namedParameterJdbcTemplate.queryForObject(sql, params, reservationRowMapper);
            return Optional.ofNullable(reservation);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Reservation> findAll() {
        String sql = SELECT_RESERVATION_WITH_TIME + "ORDER BY r.id";
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = :id";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        int affectedRows = namedParameterJdbcTemplate.update(sql, params);
        if (affectedRows == 0) {
            throw new ReservationNotFoundException(id);
        }
    }
}
