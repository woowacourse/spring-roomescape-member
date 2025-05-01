package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Repository
public class JdbcReservationDao implements ReservationRepository {

    private static final RowMapper<Reservation> rowMapper = (rs, rowNum) -> {
        String date = rs.getString("date");
        Long timeId = rs.getLong("reservation_time_id");
        String timeValue = rs.getString("start_at");
        ReservationTime reservationTime = new ReservationTime(timeId, LocalTime.parse(timeValue));
        Theme theme = new Theme(
                rs.getLong("reservation_theme_id"),
                rs.getString("theme_name"),
                rs.getString("description"),
                rs.getString("thumbnail")
        );

        Reservation reservation = new Reservation(
                rs.getLong("id"),
                rs.getString("name"),
                LocalDate.parse(date),
                reservationTime,
                theme
        );
        return reservation;
    };

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcReservationDao(final JdbcTemplate jdbcTemplate, DataSource source) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(source)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<Reservation> save(final Reservation reservation) {
        try {
            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("name", reservation.name())
                    .addValue("date", reservation.date())
                    .addValue("time_id", reservation.time().id())
                    .addValue("theme_id", reservation.theme().id());

            long id = jdbcInsert.executeAndReturnKey(params).longValue();
            return findById(id);
        } catch (DuplicateKeyException e) {
            throw new IllegalStateException("[ERROR] 이미 등록된 예약 입니다.");
        }
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                SELECT
                r.id,
                r.date,
                r.name,
                r.time_id as reservation_time_id,
                r.theme_id as reservation_theme_id,
                t.start_at,
                th.name as theme_name,
                th.description,
                th.thumbnail
                FROM reservation as r
                inner join reservation_time as t on r.time_id = t.id 
                inner join theme as th on r.theme_id = th.id
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<Reservation> findById(final Long id) {
        String sql = """
                SELECT
                r.id,
                r.date,
                r.name,
                r.time_id as reservation_time_id,
                r.theme_id as reservation_theme_id,
                t.start_at,
                th.name as theme_name,
                th.description,
                th.thumbnail
                FROM reservation as r
                inner join reservation_time as t on r.time_id = t.id
                inner join theme as th on r.theme_id = th.id
                where r.id = ?
                """;
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Reservation> findByDateTime(LocalDate date, LocalTime time) {
        String sql = """
                SELECT
                r.id,
                r.date,
                r.name,
                r.time_id as reservation_time_id,
                r.theme_id as reservation_theme_id,
                t.start_at,
                th.name as theme_name,
                th.description,
                th.thumbnail
                FROM reservation as r
                inner join reservation_time as t on r.time_id = t.id
                inner join theme as th on r.theme_id = th.id
                where r.date = ? and t.start_at = ?
                """;
        try {
            return jdbcTemplate.query(sql, rowMapper, date, time);
        } catch (EmptyResultDataAccessException e) {
            return List.of();
        }
    }

    @Override
    public List<Reservation> findByDate(LocalDate date) {
        String sql = """
                SELECT
                r.id,
                r.date,
                r.name,
                r.time_id as reservation_time_id,
                r.theme_id as reservation_theme_id,
                t.start_at,
                th.name as theme_name,
                th.description,
                th.thumbnail
                FROM reservation as r
                INNER JOIN reservation_time as t on r.time_id = t.id
                inner join theme as th on r.theme_id = th.id
                WHERE r.date = ?
                """;
        try {
            return jdbcTemplate.query(sql, rowMapper, date);
        } catch (EmptyResultDataAccessException e) {
            return List.of();
        }
    }

    @Override
    public int deleteById(final long id) {
        String sql = "delete from reservation where id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
