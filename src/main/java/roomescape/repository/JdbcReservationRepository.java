package roomescape.repository;

import java.util.List;
import java.util.NoSuchElementException;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;

@Repository
public class JdbcReservationRepository implements ReservationRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<Reservation> rowMapper;

    public JdbcReservationRepository(DataSource dataSource, RowMapper<Reservation> rowMapper) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
        this.rowMapper = rowMapper;
    }

    @Override
    public List<Reservation> findAllReservations() {
        String sql = """
                SELECT 
                r.id AS reservation_id, 
                m.id AS member_id,
                m.name AS member_name,
                m.role AS member_role,
                m.email AS member_email,
                m.password AS member_password,
                th.id AS theme_id,
                th.name AS theme_name,
                th.description AS theme_description,
                th.thumbnail AS theme_thumbnail,
                r.date, 
                t.id AS time_id, 
                t.start_at AS time_value 
                FROM reservation AS r 
                INNER JOIN reservation_time AS t ON r.time_id = t.id
                INNER JOIN theme AS th ON r.theme_id = th.id
                INNER JOIN member AS m ON r.member_id = m.id;
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Reservation insertReservation(Reservation reservation) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("member_id", reservation.getMemberId())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTimeId())
                .addValue("theme_id", reservation.getThemeId());
        long savedId = jdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return findReservationById(savedId);
    }

    @Override
    public void deleteReservationById(long id) {
        String sql = "DELETE FROM reservation WHERE id = :id";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", id);
        jdbcTemplate.update(sql, parameterSource);
    }

    @Override
    public boolean hasReservationOfTimeId(long timeId) {
        String sql = "SELECT 1 FROM reservation WHERE time_id = :timeId";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("timeId", timeId);
        List<Integer> result = jdbcTemplate.queryForList(sql, parameterSource, Integer.class);
        return !result.isEmpty();
    }

    @Override
    public boolean hasReservationOfThemeId(long themeId) {
        String sql = "SELECT 1 FROM reservation WHERE theme_id = :themeId";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("themeId", themeId);
        List<Integer> result = jdbcTemplate.queryForList(sql, parameterSource, Integer.class);
        return !result.isEmpty();
    }

    @Override
    public boolean isExistReservationOf(long id) {
        String sql = "SELECT 1 FROM reservation WHERE id = :id";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", id);
        List<Integer> result = jdbcTemplate.queryForList(sql, parameterSource, Integer.class);
        return !result.isEmpty();
    }

    @Override
    public boolean hasSameReservationForThemeAtDateTime(Reservation reservation) {
        String sql = "SELECT 1 FROM reservation WHERE date = :date AND time_id = :timeId AND theme_id = :themeId";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("date", reservation.getDate())
                .addValue("timeId", reservation.getTimeId())
                .addValue("themeId", reservation.getThemeId());
        List<Integer> result = jdbcTemplate.queryForList(sql, parameterSource, Integer.class);
        return !result.isEmpty();
    }

    private Reservation findReservationById(long savedId) {
        String sql = """
                SELECT 
                r.id AS reservation_id, 
                m.id AS member_id,
                m.name AS member_name,
                m.role AS member_role,
                m.email AS member_email,
                m.password AS member_password,
                th.id AS theme_id,
                th.name AS theme_name,
                th.description AS theme_description,
                th.thumbnail AS theme_thumbnail,
                r.date, 
                t.id AS time_id, 
                t.start_at AS time_value 
                FROM reservation AS r 
                INNER JOIN reservation_time AS t ON r.time_id = t.id
                INNER JOIN theme AS th ON r.theme_id = th.id
                INNER JOIN member AS m ON r.member_id = m.id
                WHERE r.id = :savedId;
                """;

        SqlParameterSource paramMap = new MapSqlParameterSource().addValue("savedId", savedId);

        try {
            return jdbcTemplate.queryForObject(sql, paramMap, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchElementException("아이디가 " + savedId + "인 예약이 존재하지 않습니다.");
        }
    }
}
