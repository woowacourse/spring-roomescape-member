package roomescape.time.repository.dao;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.time.repository.entity.ReservationTimeEntity;

@Repository
public class ReservationTimeDao {

    private static final RowMapper<ReservationTimeEntity> reservationTimeRowMapper = (rs, rowNum) ->
            new ReservationTimeEntity(
                    rs.getLong("id"),
                    LocalTime.parse(rs.getString("start_at"), DateTimeFormatter.ofPattern("HH:mm"))
            );

    private final JdbcTemplate jdbcTemplate;

    public ReservationTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long insert(LocalTime startAt) {
        String sql = "insert into reservation_time (start_at) values (?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement pstm = connection.prepareStatement(sql, new String[]{"id"});
            pstm.setString(1, String.valueOf(startAt));
            return pstm;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("ID 값이 생성되지 않았습니다.");
        }
        return key.longValue();
    }

    public Optional<ReservationTimeEntity> selectById(Long id) {
        String sql = "select * from reservation_time where id = ?;";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, reservationTimeRowMapper, id));
    }

    public ReservationTimeEntity getByID(Long id) {
        String sql = "select * from reservation_time where id = ?;";
        return Optional.of(jdbcTemplate.queryForObject(sql, reservationTimeRowMapper, id))
                .orElseThrow(() -> new IllegalArgumentException("ReservationTime not found"));
    }
    public List<ReservationTimeEntity> selectAll() {
        String sql = "select * from reservation_time;";
        return jdbcTemplate.query(sql, reservationTimeRowMapper);
    }

    public int deleteById(Long id) {
        String sql = "delete from reservation_time where id = ?;";
        return jdbcTemplate.update(sql, id);
    }

    public List<Long> selectReservedTimeIds(Long themeId, LocalDate date) {
        String sql = "select time_id " +
                "from reservation " +
                "where theme_id = ? and date = ?;";

        return jdbcTemplate.queryForList(sql, Long.class, themeId, date.toString());
    }
}
