package roomescape.dao;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;

@Repository
public class ReservationJdbcDao implements ReservationDao {
    public static final RowMapper<Reservation> ROW_MAPPER = (rs, rowNum) ->
            new Reservation(
                    rs.getLong("id"),
                    rs.getString("name"),
                    LocalDate.parse(rs.getString("date")),
                    TimeJdbcDao.ROW_MAPPER.mapRow(rs, rowNum),
                    ThemeDaoJdbcDao.ROW_MAPPER.mapRow(rs, rowNum)
            );


    private final JdbcTemplate jdbcTemplate;

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
        return jdbcTemplate.query(sql, ROW_MAPPER);
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
        return jdbcTemplate.query(sql, ROW_MAPPER, id).stream()
                .findFirst();
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
