package roomescape.repository.time;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcReservationTimeRepository implements ReservationTimeRepository {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final JdbcTemplate template;

    public JdbcReservationTimeRepository(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public ReservationTime createReservationTime(ReservationTime reservationTime) {
        String sql = "INSERT INTO reservation_time(start_at) VALUES (?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        template.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, reservationTime.getStartAt().format(TIME_FORMATTER));
            return ps;
        }, keyHolder);

        long key = keyHolder.getKey().longValue();
        return new ReservationTime(key, TIME_FORMATTER.format(reservationTime.getStartAt())); // QUESTION: 이럴 때 그냥 객체 새로 만들어서 보내면 되는 건지 아니면 만들어진걸 조회해서 보내야 하는 건지
    }

    // TODO: 개행처리
    @Override
    public List<ReservationTime> findAll() {
        String sql = "SELECT id, start_at FROM reservation_time;";

        List<ReservationTime> times = template.query(sql, reservationTimeRowMapper());

        return times;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation_time WHERE id = ?;";

        template.update(sql, id);
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        List<ReservationTime> times = template.query(
                "SELECT id, start_at FROM reservation_time WHERE id = ?;",
                reservationTimeRowMapper(),
                id);

        return times.stream().findFirst();
    }

    @Override
    public List<ReservationTime> findByDateAndThemeId(LocalDate date, Long themeId) {
        String formattedDate = DATE_FORMATTER.format(date);

        return template.query(
            """
                SELECT 
                    t.id as time_id, 
                    t.start_at as time_value 
                FROM reservation_time as t 
                    LEFT JOIN reservation as r 
                        ON t.id = r.time_id 
                               AND r.res_date = ? 
                               AND r.theme_id = ? 
                WHERE r.id IS NULL
                """,
                reservationTimeRowMapper(),
                formattedDate,
                themeId
        );
    }

    private RowMapper<ReservationTime> reservationTimeRowMapper() {
        return ((rs, rowNum) -> {
            ReservationTime reservationTime = new ReservationTime(
                    rs.getLong("id"),
                    rs.getString("start_at"));
            return reservationTime;
        });
    }
}
