package roomescape.dao;

import java.sql.PreparedStatement;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeRequestDto;
import roomescape.exception.CustomException;
import roomescape.exception.ErrorCode;

@Primary
@Repository
public class JdbcReservationTimeDao implements ReservationTimeDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ReservationTime create(ReservationTimeRequestDto requestDto) {
        String sql = "INSERT INTO `reservation_time`(`start_at`) VALUES ?";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setTime(1, Time.valueOf(requestDto.startAt()));

            return preparedStatement;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return new ReservationTime(id, requestDto.startAt());
    }

    @Override
    public ReservationTime read(Long id) {
        String sql = "SELECT * FROM `reservation_time` WHERE `id` = id";

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                LocalTime startAt = rs.getTime("start_at").toLocalTime();
                return new ReservationTime(id, startAt);
            });
        } catch (EmptyResultDataAccessException exception) {
            throw new CustomException(ErrorCode.NOT_FOUND_RESERVATION_TIME);
        }
    }

    @Override
    public List<ReservationTime> readAll() {
        String sql = "SELECT * FROM `reservation_time`";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long id = rs.getLong("id");
            LocalTime startAt = rs.getTime("start_at").toLocalTime();
            return new ReservationTime(id, startAt);
        });
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM `reservation_time` WHERE `id` = ?";
        jdbcTemplate.update(sql, id);
    }
}
