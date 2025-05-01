package roomescape.reservation.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private static final RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) -> {
        final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        final LocalTime time = LocalTime.parse(resultSet.getString("time_value"), timeFormatter);
        return new Reservation(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getObject("date", LocalDate.class),
                new ReservationTime(resultSet.getLong("time_id"), time),
                new Theme(resultSet.getLong("theme_id"), resultSet.getString("theme_name"),
                        resultSet.getString("theme_description"), resultSet.getString("theme_thumbnail"))
        );
    };

    private final JdbcTemplate template;
    private final SimpleJdbcInsert inserter;

    public JdbcReservationRepository(final DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        this.inserter = new SimpleJdbcInsert(dataSource).withTableName("reservation")
                .usingGeneratedKeyColumns("id")
                .usingColumns("name", "date", "time_id", "theme_id");
    }

    @Override
    public List<Reservation> findAll() {
        final String sql = """
                select 
                    r.id, 
                    r.name, r.date, 
                    t.id as time_id, 
                    t.start_at as time_value,
                    th.id as theme_id,
                    th.name as theme_name,
                    th.description as theme_description,
                    th.thumbnail as theme_thumbnail
                from reservation as r
                inner join reservation_time as t 
                on r.time_id = t.id
                inner join theme as th
                on r.theme_id = th.id
                """;
        return template.query(sql, reservationRowMapper);
    }

    @Override
    public boolean existsByDateAndTimeAndTheme(final LocalDate date, final LocalTime time, final Long themeId) {
        final String sql = """
                select count(*) 
                from reservation as r                
                inner join reservation_time as rt
                on r.time_id = rt.id
                where r.date = ? 
                  and rt.start_at = ?
                  and r.theme_id = ?
                """;
        final Integer count = template.queryForObject(sql, Integer.class, date, time, themeId);
        return count != null && count > 0;
    }

    @Override
    public boolean existsByReservationTimeId(final Long reservationTimeId) {
        final String sql = """
                        select count(*) 
                        from reservation as r
                        where r.time_id = ?
                """;
        final Integer count = template.queryForObject(sql, Integer.class, reservationTimeId);
        return count != null && count > 0;
    }

    @Override
    public boolean existsByThemeId(final Long themeId) {
        final String sql = """
                        select count(*) 
                        from reservation as r
                        where r.theme_id = ?
                """;
        final Integer count = template.queryForObject(sql, Integer.class, themeId);
        return count != null && count > 0;
    }

    @Override
    public Reservation save(final Reservation reservation) {
        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getTheme().getId());

        final long newId = inserter.executeAndReturnKey(params).longValue();
        return new Reservation(newId, reservation.getName(),
                reservation.getDate(), reservation.getTime(), reservation.getTheme());
    }

    @Override
    public Optional<Reservation> findById(final Long id) {
        final String sql = """
                select 
                    r.id, 
                    r.name, r.date, 
                    t.id as time_id, 
                    t.start_at as time_value,
                    th.id as theme_id,
                    th.name as theme_name,
                    th.description as theme_description,
                    th.thumbnail as theme_thumbnail
                from reservation as r
                inner join reservation_time as t 
                on r.time_id = t.id
                inner join theme as th
                on r.theme_id = th.id
                where r.id = ?
                """;
        return template.query(sql, reservationRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public void deleteById(final Long id) {
        final String sql = "delete from reservation where id = ?";
        final int rows = template.update(sql, id);
        if (rows == 0) {
            throw new NoSuchElementException("삭제하려고 하는 예약이 존재하지 않습니다. " + id);
        }
    }
}
