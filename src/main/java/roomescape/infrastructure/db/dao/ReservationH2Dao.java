package roomescape.infrastructure.db.dao;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.entity.Reservation;
import roomescape.domain.entity.ReservationTheme;
import roomescape.domain.entity.ReservationTime;

@Repository
@RequiredArgsConstructor
public class ReservationH2Dao implements ReservationDao {

    private static final RowMapper<Reservation> DEFAULT_ROW_MAPPER = (resultSet, rowNum) -> new Reservation(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getDate("date").toLocalDate(),
            new ReservationTime(
                    resultSet.getLong("time_id"),
                    resultSet.getTime("start_at").toLocalTime()
            ),
            new ReservationTheme(
                    resultSet.getLong("theme_id"),
                    resultSet.getString("th_name"),
                    resultSet.getString("th_description"),
                    resultSet.getString("th_thumbnail")
            )
    );

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Reservation> selectAll() {
        String selectAllQuery = """
                SELECT r.id, r.name, r.date, r.time_id, r.theme_id, rt.start_at, 
                       th.name AS th_name, th.description AS th_description, th.thumbnail AS th_thumbnail
                FROM reservation r
                INNER JOIN reservation_time rt ON r.time_id = rt.id
                INNER JOIN theme th ON r.theme_id = th.id
                """;
        return jdbcTemplate.query(selectAllQuery, new MapSqlParameterSource(), DEFAULT_ROW_MAPPER);
    }

    @Override
    public Reservation insertAndGet(Reservation reservation) {
        String insertQuery = """
                INSERT INTO reservation (name, date, time_id, theme_id)
                VALUES (:name, :date, :timeId, :themeId)
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.name())
                .addValue("date", reservation.date())
                .addValue("timeId", reservation.time().id())
                .addValue("themeId", reservation.theme().id());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(insertQuery, params, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return reservation.assignId(id);
    }

    @Override
    public Optional<Reservation> selectById(Long id) {
        String selectQuery = """
                SELECT r.id, r.name, r.date, r.time_id, r.theme_id, rt.start_at, 
                       th.name AS th_name, th.description AS th_description, th.thumbnail AS th_thumbnail
                FROM reservation r
                INNER JOIN reservation_time rt ON r.time_id = rt.id
                INNER JOIN theme th ON r.theme_id = th.id
                WHERE r.id = :id
                """;

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    selectQuery,
                    Map.of("id", id),
                    DEFAULT_ROW_MAPPER
            ));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(Long id) {
        String deleteQuery = "DELETE FROM reservation WHERE id = :id";
        jdbcTemplate.update(deleteQuery, Map.of("id", id));
    }

    @Override
    public boolean existDuplicatedDateTime(LocalDate date, Long timeId, Long themeId) {
        String query = """
                SELECT count(*)
                FROM reservation
                WHERE time_id = :timeId AND date = :date AND theme_id = :themeId
                """;

        Map<String, Object> params = new HashMap<>();
        params.put("timeId", timeId);
        params.put("date", date);
        params.put("themeId", themeId);

        Integer count = jdbcTemplate.queryForObject(query, params, Integer.class);
        return count != null && count > 0;
    }

    @Override
    public boolean existsByThemeId(Long reservationThemeId) {
        String sql = """
                    SELECT EXISTS (
                        SELECT 1
                        FROM reservation
                        WHERE theme_id = :themeId
                    )
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("themeId", reservationThemeId);

        Boolean exists = jdbcTemplate.queryForObject(sql, params, Boolean.class);
        return exists != null && exists;
    }
}
