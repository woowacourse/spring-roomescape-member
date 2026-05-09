package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
    }

    private static RowMapper<Reservation> getReservationRowMapper() {
        return (resultSet, rowNum) -> {
            ReservationTime time = ReservationTime.of(
                    resultSet.getLong("reservation_time_id"),
                    LocalTime.parse(resultSet.getString("time_value"))
            );

            Theme theme = Theme.of(
                    resultSet.getLong("reservation_theme_id"),
                    resultSet.getString("reservation_theme_name"),
                    resultSet.getString("reservation_theme_description"),
                    resultSet.getString("reservation_theme_image_url")
            );
            return Reservation.of(
                    resultSet.getLong("reservation_id"),
                    resultSet.getString("name"),
                    LocalDate.parse(resultSet.getString("date")),
                    time,
                    theme
            );
        };
    }

    @Override
    public Reservation save(Reservation reservation) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(
                jdbcTemplate.getJdbcTemplate())
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");

        long generatedKey = simpleJdbcInsert.executeAndReturnKey(
                new BeanPropertySqlParameterSource(reservation))
                .longValue();

        return Reservation.of(
                generatedKey,
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme()
        );
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                    select r.id as reservation_id,   
                    r.name, r.date, 
                    t.id as reservation_time_id,
                    t.start_at as time_value,
                    th.id as reservation_theme_id,
                    th.name as reservation_theme_name,
                    th.description as reservation_theme_description,
                    th.image_url as reservation_theme_image_url
                    
                    from reservation as r 
                    inner join reservation_time as t
                    on r.time_id = t.id 
                    
                    inner join theme as th
                    on r.theme_id = th.id
                """;

        return jdbcTemplate.query(
                sql,
                getReservationRowMapper()
        );
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = """
                    select r.id as reservation_id,   
                    r.name, r.date, 
                    t.id as reservation_time_id,
                    t.start_at as time_value,
                    th.id as reservation_theme_id,
                    th.name as reservation_theme_name,
                    th.description as reservation_theme_description,
                    th.image_url as reservation_theme_image_url
                    from reservation as r 
                    inner join reservation_time as t
                    on r.time_id = t.id 
                    inner join theme as th
                    on r.theme_id = th.id
                    where r.id = :id
                """;

        Map<String, Object> params = Map.of("id", id);

        List<Reservation> results = jdbcTemplate.query(
                sql,
                params,
                getReservationRowMapper()
        );
        return results.stream().findFirst();
    }

    @Override
    public void delete(Long id) {
        String sql = "delete from reservation where id = :id";
        Map<String, Object> params = Map.of("id", id);
        jdbcTemplate.update(sql, params);
    }

    @Override
    public boolean existByTimeId(Long timeId) {
        String sql = "select count(*) from reservation where time_id = :time_id";
        Map<String, Object> params = Map.of("time_id", timeId);
        Integer count = jdbcTemplate.queryForObject(sql, params, Integer.class);
        return count > 0;
    }
}
