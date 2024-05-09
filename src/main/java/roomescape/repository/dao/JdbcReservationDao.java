package roomescape.repository.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
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
    private final RowMapper<ReservationSavedDto> rowMapper = (resultSet, rowNum) -> new ReservationSavedDto(
            resultSet.getLong("id"),
            resultSet.getDate("date").toLocalDate(),
            resultSet.getLong("time_id"),
            resultSet.getLong("theme_id"),
            resultSet.getLong("member_id")
    );

    public JdbcReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertActor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public long save(ReservationSavedDto reservationSavedDto) {
        Map<String, Object> parameters = new HashMap<>(4);
        parameters.put("date", reservationSavedDto.getDate());
        parameters.put("time_id", reservationSavedDto.getTimeId());
        parameters.put("theme_id", reservationSavedDto.getThemeId());
        parameters.put("member_id", reservationSavedDto.getMemberId());
        return insertActor.executeAndReturnKey(parameters).longValue();
    }

    @Override
    public List<ReservationSavedDto> findAll() {
        String sql = "select id, date, time_id, theme_id, member_id from reservation";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<ReservationSavedDto> findById(long id) {
        String sql = "select id, date, time_id, theme_id, member_id from reservation where id = ?";
        ReservationSavedDto reservation = jdbcTemplate.queryForObject(sql, rowMapper, id);
        return Optional.ofNullable(reservation);
    }

    @Override
    public List<ReservationSavedDto> findByDateAndThemeId(LocalDate date, long themeId) {
        String sql = "select id, date, time_id, theme_id, member_id from reservation where date = ? and theme_id = ?";
        return jdbcTemplate.query(sql, rowMapper, date, themeId);
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
        return jdbcTemplate.queryForList(sql, Long.class, startDate, endDate, limit);
//        return jdbcTemplate.query(sql, (resultSet, rowNum) -> resultSet.getLong("theme_id"),
//                startDate, endDate, limit);
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
    public Boolean isExistByDateAndTimeIdAndThemeId(LocalDate date, long timeId, long themeId) {
        String sql = "select exists (select id from reservation where date = ? and time_id = ? and theme_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, date, timeId, themeId);
    }
}
