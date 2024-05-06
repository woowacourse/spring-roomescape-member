package roomescape.repository.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.model.Reservation;
import roomescape.repository.dto.ReservationSavedDto;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcReservationDao implements ReservationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertActor;

    public JdbcReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public long save(Reservation reservation) {
        Map<String, Object> parameters = new HashMap<>(4);
        parameters.put("name", reservation.getName());
        parameters.put("date", reservation.getDate());
        parameters.put("time_id", reservation.getTime().getId());
        parameters.put("theme_id", reservation.getTheme().getId());
        return insertActor.executeAndReturnKey(parameters).longValue();
    }

    @Override
    public List<ReservationSavedDto> findAll() {
        String sql2 = "select id, name, date, time_id, theme_id from reservation";
        return jdbcTemplate.query(sql2, (resultSet, rowNum) -> new ReservationSavedDto(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getDate("date").toLocalDate(),
                resultSet.getLong("time_id"),
                resultSet.getLong("theme_id")
        ));
    }

    @Override
    public Optional<ReservationSavedDto> findById(long id) {
        String sql2 = "select id, name, date, time_id, theme_id from reservation where id = ?";
        ReservationSavedDto reservation = jdbcTemplate.queryForObject(sql2, (resultSet, rowNum) -> new ReservationSavedDto(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getDate("date").toLocalDate(),
                resultSet.getLong("time_id"),
                resultSet.getLong("theme_id")
        ), id);
        return Optional.ofNullable(reservation);
    }

    @Override
    public List<ReservationSavedDto> findByDateAndThemeId(LocalDate date, long themeId) {
        String sql = "select id, name, date, time_id, theme_id from reservation where date = ? and theme_id = ?";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> new ReservationSavedDto(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getDate("date").toLocalDate(),
                resultSet.getLong("time_id"),
                resultSet.getLong("theme_id")
                ), date, themeId);
    }

    @Override
    public List<Long> findThemeIdByDateAndOrderByThemeIdCountAndLimit(LocalDate startDate, LocalDate endDate, int limit) {
        String sql = """
                select theme_id
                from reservation
                where date between ? and ? 
                group by theme_id
                order by count(theme_id) desc, theme_id asc
                limit ?
                """;
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> resultSet.getLong("theme_id"),
                startDate, endDate, limit);
    }

    @Override
    public void deleteById(long id) {
        String sql = "delete from reservation where id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Boolean isExistById(long id) {
        String sql = "select exists (select id from reservation where id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }

    @Override
    public Boolean isExistByTimeId(long timeId) {
        String sql = "select exists (select id from reservation where time_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, timeId);
    }

    @Override
    public Boolean isExistByDateAndTimeId(LocalDate date, long timeId) {
        String sql = "select exists (select id from reservation where date = ? and time_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId);
    }
}
