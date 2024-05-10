package roomescape.repository;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;

@Repository
public class JdbcReservationRepository {

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

    public List<Reservation> findAllReservations() {
        String sql = """
                SELECT 
                r.id AS reservation_id, 
                m.id AS member_id,
                m.email AS member_email,
                m.password AS member_password,
                m.name AS member_name,
                m.role AS member_role,
                th.id AS theme_id,
                th.name AS theme_name,
                th.description AS theme_description,
                th.thumbnail AS theme_thumbnail,
                r.date, 
                t.id AS time_id, 
                t.start_at AS time_value 
                FROM reservation AS r 
                INNER JOIN member AS m ON m.id = r.member_id
                INNER JOIN reservation_time AS t ON r.time_id = t.id
                INNER JOIN theme AS th ON r.theme_id = th.id;
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Reservation insertReservation(Reservation reservation) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("member_id", reservation.getMember().getId())
                .addValue("theme_id", reservation.getThemeId())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTimeId());
        long savedId = jdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return findReservationById(savedId);
    }

    public void deleteReservationById(long id) {
        String sql = "DELETE FROM reservation WHERE id = :id";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", id);
        jdbcTemplate.update(sql, parameterSource);
    }

    public boolean isReservationExistsByTimeId(long timeId) {
        String sql = "SELECT 1 FROM reservation WHERE time_id = :timeId";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("timeId", timeId);
        List<Integer> result = jdbcTemplate.queryForList(sql, parameterSource, Integer.class);
        return !result.isEmpty();
    }

    public boolean isReservationExistsByThemeId(long themeId) {
        String sql = "SELECT 1 FROM reservation WHERE theme_id = :themeId";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("themeId", themeId);
        List<Integer> result = jdbcTemplate.queryForList(sql, parameterSource, Integer.class);
        return !result.isEmpty();
    }

    public boolean isReservationExistsById(long id) {
        String sql = "SELECT 1 FROM reservation WHERE id = :id";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", id);
        List<Integer> result = jdbcTemplate.queryForList(sql, parameterSource, Integer.class);
        return !result.isEmpty();
    }

    public boolean isReservationExistsByDateAndTimeIdAndThemeId(Reservation reservation) {
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
                m.email AS member_email,
                m.password AS member_password,
                m.name AS member_name,
                m.role AS member_role,
                th.id AS theme_id,
                th.name AS theme_name,
                th.description AS theme_description,
                th.thumbnail AS theme_thumbnail,
                r.date, 
                t.id AS time_id, 
                t.start_at AS time_value 
                FROM reservation AS r 
                INNER JOIN member AS m ON m.id = r.member_id
                INNER JOIN reservation_time AS t ON r.time_id = t.id 
                INNER JOIN theme AS th ON r.theme_id = th.id
                WHERE r.id = :savedId;
                """;
        SqlParameterSource paramMap = new MapSqlParameterSource().addValue("savedId", savedId);
        return jdbcTemplate.query(sql, paramMap, rowMapper).get(0);
    }
}
