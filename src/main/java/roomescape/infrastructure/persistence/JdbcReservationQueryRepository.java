package roomescape.infrastructure.persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationQueryRepository;
import roomescape.domain.Theme;
import roomescape.domain.dto.AvailableTimeDto;

@Repository
public class JdbcReservationQueryRepository implements ReservationQueryRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationQueryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Reservation> findById(long id) {
        String sql = """
                select r.id as id, r.name as reservation_name, date, time_id, start_at,
                theme_id, t.name as theme_name, description, thumbnail from reservation as r
                left join reservation_time as rt on time_id = rt.id
                left join theme as t on theme_id = t.id
                where r.id = ?
                """;
        try {
            Reservation reservation = jdbcTemplate.queryForObject(sql, ReservationRowMapper::joinedMapRow, id);
            return Optional.of(Objects.requireNonNull(reservation));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                select r.id as id, r.name as reservation_name, date, time_id, start_at,
                theme_id, t.name as theme_name, description, thumbnail from reservation as r
                left join reservation_time as rt on time_id = rt.id
                left join theme as t on theme_id = t.id
                """;
        return jdbcTemplate.query(sql, ReservationRowMapper::joinedMapRow);
    }

    @Override
    public long findReservationCountByTimeId(long timeId) {
        String sql = "select count(*) from reservation where time_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, timeId);
    }

    @Override
    public boolean existBy(LocalDate date, long timeId, long themeId) {
        String sql = "select exists(select 1 from reservation where date = ? and time_id = ? and theme_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId, themeId);
    }

    @Override
    public List<AvailableTimeDto> findAvailableReservationTimes(LocalDate date, long themeId) {
        String sql = """
                select  id, 
                        start_at, 
                        start_at in (
                            select start_at
                            from reservation as r
                            left join reservation_time as rt 
                            on r.time_id = rt.id
                            where date = ? and r.theme_id = ?
                        ) as is_booked
                from    reservation_time;
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new AvailableTimeDto(
                rs.getLong("id"),
                rs.getTime("start_at").toLocalTime(),
                rs.getBoolean("is_booked")
        ), date, themeId);
    }

    @Override
    public List<Theme> findPopularThemesDateBetween(LocalDate startDate, LocalDate endDate, int limit) {
        String sql = """
                select  t.id, 
                        t.name, 
                        t.description, 
                        t.thumbnail, 
                        count(r.id) as reservation_count
                from    theme as t 
                left join reservation as r 
                on t.id = r.theme_id
                where r.date between ? and ?
                group by t.id
                order by reservation_count desc
                limit ?
                """;

        return jdbcTemplate.query(sql, ThemeRowMapper::mapRow, startDate, endDate, limit);
    }
}
