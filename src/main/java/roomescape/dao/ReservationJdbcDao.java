package roomescape.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.dao.vo.ReservationRow;
import roomescape.dao.vo.ReservationRows;
import roomescape.domain.Reservation;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class ReservationJdbcDao implements ReservationDao{

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<ReservationRow> rowMapper = (resultSet, rowNum) -> {
        ReservationRow row = new ReservationRow(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                LocalDate.parse(resultSet.getString("date")),
                resultSet.getLong("time_id"),
                LocalTime.parse(resultSet.getString("start_at"))
        );

        return row;
    };

    public ReservationJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                SELECT 
                    r.id,
                    r.name,
                    r.date,
                    t.id AS time_id,
                    t.start_at
                    FROM reservation r
                INNER JOIN reservation_time t ON r.time_id = t.id
                """;
        return new ReservationRows(jdbcTemplate.query(sql, rowMapper)).toReservations();
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = """
                SELECT
                    r.id,
                    r.name,
                    r.date,
                    t.id AS time_id,
                    t.start_at
                FROM reservation r
                INNER JOIN reservation_time t ON r.time_id = t.id
                WHERE r.id = ?
                """;
        return jdbcTemplate.query(sql, rowMapper, id).stream()
                .findFirst()
                .map(ReservationRow::toReservation);
    }

    @Override
    public Long insert(Reservation reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = """
                INSERT INTO reservation
                (name, date, time_id)
                VALUES (?, ?, ?)
                """;
        jdbcTemplate.update(con -> {
            PreparedStatement pstmt = con.prepareStatement(sql, new String[]{"id"});
            pstmt.setString(1, reservation.getName());
            pstmt.setString(2, reservation.getDate().toString());
            pstmt.setLong(3, reservation.getTime().getId());
            return pstmt;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public int delete(Long id) {
        String sql = """
                DELETE FROM reservation
                WHERE id = ?
                """;
        return jdbcTemplate.update(sql, id);
    }
}
