package roomescape.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequestDto;

@Primary
@Repository
public class JdbcReservationDao implements ReservationDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Reservation create(ReservationRequestDto requestDto, ReservationTime reservationTime) {
        String sql = "INSERT INTO `reservation`(`name`, `date`, `time_id`) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, requestDto.name());
            preparedStatement.setDate(2, Date.valueOf(requestDto.date()));
            preparedStatement.setLong(3, requestDto.timeId());

            return preparedStatement;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return new Reservation(id, requestDto.name(), requestDto.date(), reservationTime);
    }

    @Override
    public List<Reservation> readAll() {
        String sql = "SELECT r.id, r.name, r.date, t.id as time_id, t.start_at as time_value "
                + "FROM `reservation` r "
                + "INNER JOIN `reservation_time` t ON r.time_id = t.id";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long id = rs.getLong("id");
            String name = rs.getString("name");
            LocalDate date = rs.getDate("date").toLocalDate();
            Long timeId = rs.getLong("time_id");
            LocalTime timeValue = rs.getTime("time_value").toLocalTime();

            ReservationTime reservationTime = new ReservationTime(timeId, timeValue);
            return new Reservation(id, name, date, reservationTime);
        });
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM `reservation` WHERE `id` = ?";

        jdbcTemplate.update(sql, id);
    }
}
