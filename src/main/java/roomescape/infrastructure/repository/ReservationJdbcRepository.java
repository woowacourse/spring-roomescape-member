package roomescape.infrastructure.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationDateTime;
import roomescape.domain.ReserverName;
import roomescape.domain.Theme;
import roomescape.domain.ReservationTime;

@Repository
public class ReservationJdbcRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final static RowMapper<Reservation> reservationRowMapper = (resultSet, rowNum) ->
            new Reservation(
                    resultSet.getLong("reservation_id"),
                    resultSet.getString("name"),
                    LocalDate.parse(resultSet.getString("date")),
                    new ReservationTime(
                            resultSet.getLong("time_id"),
                            LocalTime.parse(resultSet.getString("time_value"))
                    ),
                    new Theme(
                            resultSet.getLong("theme_id"),
                            resultSet.getString("theme_name"),
                            resultSet.getString("theme_description"),
                            resultSet.getString("theme_thumbnail")
                    ));

    public ReservationJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public List<Reservation> findAll() {
        String sql = """
                select r.id as reservation_id, 
                       r.name,
                       r.date, 
                       t.id as time_id, 
                       t.start_at as time_value, 
                       th.id as theme_id, 
                       th.name as theme_name, 
                       th.description as theme_description, 
                       th.thumbnail as theme_thumbnail
                from reservation as r
                inner join reservation_time as t on r.time_id = t.id
                inner join theme as th on r.theme_id = th.id
                """;

        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    @Override
    public Reservation save(ReserverName reserverName, ReservationDateTime reservationDateTime, Theme theme) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", reserverName.getName())
                .addValue("date", reservationDateTime.reservationDate().getDate())
                .addValue("time_id", reservationDateTime.reservationTime().getId())
                .addValue("theme_id", theme.getId());
        Long id = jdbcInsert.executeAndReturnKey(parameters).longValue();

        return new Reservation(id, reserverName.getName(), reservationDateTime.reservationDate().getDate(),
                new ReservationTime(reservationDateTime.reservationTime().getId(),
                        reservationDateTime.reservationTime().getStartAt()),
                theme);
    }

    public void deleteById(Long id) {
        String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Optional<Reservation> findById(Long id) {
        String sql = """
                select r.id as reservation_id, 
                       r.name,
                       r.date, 
                       t.id as time_id, 
                       t.start_at as time_value, 
                       th.id as theme_id, 
                       th.name as theme_name, 
                       th.description as theme_description, 
                       th.thumbnail as theme_thumbnail
                from reservation as r
                inner join reservation_time as t on r.time_id = t.id
                inner join theme as th on r.theme_id = th.id
                where r.id = ?
                """;

        return jdbcTemplate.query(sql, reservationRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public boolean existsByDateTimeAndTheme(ReservationDate reservationDate, Long timeId, Long themeId) {
        String sql = "SELECT 1 FROM reservation WHERE date = ? AND time_id = ? AND theme_id = ? LIMIT 1";
        List<Integer> results = jdbcTemplate.queryForList(sql,
                Integer.class,
                reservationDate.getDate(), timeId, themeId);
        return !results.isEmpty();
    }

    @Override
    public boolean existsByTimeId(Long timeId) {
        String sql = "SELECT 1 FROM reservation WHERE time_id = ? LIMIT 1";
        List<Integer> results = jdbcTemplate.queryForList(sql, Integer.class, timeId);
        return !results.isEmpty();
    }

    @Override
    public boolean existsByThemeId(Long themeId) {
        String sql = "SELECT 1 FROM reservation WHERE theme_id = ? LIMIT 1";
        List<Integer> results = jdbcTemplate.queryForList(sql, Integer.class, themeId);
        return !results.isEmpty();
    }

}
