package roomescape.repository.dao;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;

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

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Reservation> selectAll() {
        String selectAllQuery = """
                SELECT r.id, r.name, r.date, r.time_id, r.theme_id, rt.start_at, th.name AS th_name, th.description AS th_description, th.thumbnail AS th_thumbnail
                FROM reservation r
                INNER JOIN reservation_time rt ON r.time_id = rt.id
                INNER JOIN theme th ON r.theme_id = th.id
                """;
        return jdbcTemplate.query(selectAllQuery, DEFAULT_ROW_MAPPER);
    }

    @Override
    public Reservation insertAndGet(Reservation reservation) {
        String insertQuery = "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertQuery, new String[] {"id"});
            ps.setString(1, reservation.name());
            ps.setString(2, reservation.date().toString());
            ps.setLong(3, reservation.time().id());
            ps.setLong(4, reservation.theme().id());
            return ps;
        }, keyHolder);
        Long id = keyHolder.getKey().longValue();

        return reservation.withId(id);
    }

    @Override
    public Optional<Reservation> selectById(Long id) {
        String selectQuery = """
                SELECT r.id, r.name, r.date, r.time_id, r.theme_id, rt.start_at, th.name AS th_name, th.description AS th_description, th.thumbnail AS th_thumbnail
                FROM reservation r
                INNER JOIN reservation_time rt ON r.time_id = rt.id
                INNER JOIN theme th ON r.theme_id = th.id
                WHERE r.id = ?
                """;
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(selectQuery, DEFAULT_ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(Long id) {
        String deleteQuery = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(deleteQuery, id);
    }

    @Override
    public boolean existDuplicatedDateTime(LocalDate date, Long timeId, Long themeId) {
        String query = """
                SELECT count(*)
                FROM reservation
                WHERE time_id = ? AND date = ? AND theme_id = ?
                """;
        // TODO : npe 가능성 점검
        int duplicatedCount = jdbcTemplate.queryForObject(query, Integer.class, timeId, date, themeId);
        return duplicatedCount > 0;
    }
}
