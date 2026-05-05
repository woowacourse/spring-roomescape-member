package roomescape.repository;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Reservation save(Reservation reservation) {
        String sql = "insert into reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    new String[]{"id"});
            ps.setString(1, reservation.getName());
            ps.setString(2, reservation.getDate().toString());
            ps.setLong(3, reservation.getTime().getId());
            ps.setLong(4, reservation.getTheme().getId());
            return ps;
        }, keyHolder);

        return findById(keyHolder.getKey().longValue()).stream().findFirst()
                .orElseThrow(() -> new RuntimeException("존재하지 않는 데이터입니다."));
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
                    where r.id = ?
                """;

        List<Reservation> results = jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> {
                    ReservationTime time = new ReservationTime(
                            resultSet.getLong("reservation_time_id"),
                            LocalTime.parse(resultSet.getString("time_value"))
                    );
                    Theme theme = new Theme(
                            resultSet.getLong("reservation_theme_id"),
                            resultSet.getString("reservation_theme_name"),
                            resultSet.getString("reservation_theme_description"),
                            resultSet.getString("reservation_theme_image_url")
                    );
                    return new Reservation(
                            resultSet.getLong("reservation_id"),
                            resultSet.getString("name"),
                            LocalDate.parse(resultSet.getString("date")),
                            time,
                            theme
                    );
                }, id
        );
        return results.stream().findFirst();
    }

    @Override
    public boolean existByTimeId(Long timeId) {
        String sql = "select count(*) from reservation where time_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, timeId);
        return count != null && count > 0;
    }

    @Override
    public List<Reservation> findByTimeId(Long timeId) {
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
                    where r.time_id = ?
                """;
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            ReservationTime time = new ReservationTime(
                    resultSet.getLong("reservation_time_id"),
                    LocalTime.parse(resultSet.getString("time_value"))
            );
            Theme theme = new Theme(
                    resultSet.getLong("reservation_theme_id"),
                    resultSet.getString("reservation_theme_name"),
                    resultSet.getString("reservation_theme_description"),
                    resultSet.getString("reservation_theme_image_url")
            );
            return new Reservation(
                    resultSet.getLong("reservation_id"),
                    resultSet.getString("name"),
                    LocalDate.parse(resultSet.getString("date")),
                    time,
                    theme
            );
        }, timeId);
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
                (resultSet, rowNum) -> {
                    ReservationTime time = new ReservationTime(
                            resultSet.getLong("reservation_time_id"),
                            LocalTime.parse(resultSet.getString("time_value"))
                    );
                    Theme theme = new Theme(
                            resultSet.getLong("reservation_theme_id"),
                            resultSet.getString("reservation_theme_name"),
                            resultSet.getString("reservation_theme_description"),
                            resultSet.getString("reservation_theme_image_url")
                    );
                    return new Reservation(
                            resultSet.getLong("reservation_id"),
                            resultSet.getString("name"),
                            LocalDate.parse(resultSet.getString("date")),
                            time,
                            theme
                    );
                }

        );
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("delete from reservation where id = ?", id);
    }
}
