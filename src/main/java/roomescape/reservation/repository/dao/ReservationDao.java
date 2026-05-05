package roomescape.reservation.repository.dao;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.repository.entity.ReservationEntity;

@Repository
public class ReservationDao {

    private static final RowMapper<ReservationEntity> reservationRowMapper = (rs, rowNum) ->
            new ReservationEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    LocalDate.parse(rs.getString("date")),
                    rs.getLong("time_id"),
                    rs.getLong("theme_id")
            );
    private final JdbcTemplate jdbcTemplate;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ReservationEntity> selectAll() {
        String sql = "select * from reservation;";
        return jdbcTemplate.query(sql, reservationRowMapper);
    }

    public Long insert(String name, LocalDate date, Long timeId, Long themeId) {
        String sql = "insert into reservation (name, date, time_id, theme_id) values (?,?,?,?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement pstm = connection.prepareStatement(sql, new String[]{"id"});
            pstm.setString(1, name);
            pstm.setString(2, String.valueOf(date));
            pstm.setString(3, String.valueOf(timeId));
            pstm.setString(4, String.valueOf(themeId));
            return pstm;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("ID 값이 생성되지 않았습니다.");
        }
        return key.longValue();
    }

    public int deleteById(Long id) {
        String sql = "delete from reservation where id = ?;";
        return jdbcTemplate.update(sql, id);
    }
}
