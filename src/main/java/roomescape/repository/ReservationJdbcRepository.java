package roomescape.repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Repository
public class ReservationJdbcRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Reservation> rowMapper = (resultSet, rowNum) -> {
        ReservationTime time = new ReservationTime(
                resultSet.getLong("time_id"),
                resultSet.getTime("start_at").toLocalTime()
        );

        Theme theme = new Theme(
                resultSet.getLong("theme_id"),
                resultSet.getString("theme_name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail_image_url")
        );

        return new Reservation(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                theme,
                resultSet.getDate("date").toLocalDate(),
                time
        );
    };

    public ReservationJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                select r.id, r.name, r.date,
                       t.id as time_id, t.start_at,
                       th.id as theme_id, th.name as theme_name, th.description, th.thumbnail_image_url
                from reservation r
                join reservation_time t on r.time_id = t.id
                join theme th on r.theme_id = th.id
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Reservation findById(Long id) {
        String sql = """
                select r.id, r.name, r.date,
                       t.id as time_id, t.start_at,
                       th.id as theme_id, th.name as theme_name, th.description, th.thumbnail_image_url
                from reservation r
                join reservation_time t on r.time_id = t.id
                join theme th on r.theme_id = th.id
                where r.id = ?
                """;
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    @Override
    public Long save(Reservation reservation) {
        String sql = "insert into reservation(name, theme_id, date, time_id) values(?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, reservation.getName());
            ps.setLong(2, reservation.getTheme().getId());
            ps.setDate(3, Date.valueOf(reservation.getDate()));
            ps.setLong(4, reservation.getTime().getId());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public void deleteById(Long id) {
        String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Long> findTimeIdsByThemeIdAndDate(Long themeId, LocalDate date) {
        String sql = """
                select time_id
                from reservation
                where theme_id = ? and date = ?
                """;
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> resultSet.getLong("time_id"), themeId, date);
    }

    @Override
    public boolean existsByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId) {
        String sql = "select exists(select 1 from reservation where date = ? and time_id = ? and theme_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId, themeId));
    }
}
