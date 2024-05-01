package roomescape.dao;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTheme;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationThemeDao {

    private final JdbcTemplate jdbcTemplate;

    public ReservationThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ReservationTheme> findAll() {
        String findAllSql = "SELECT * FROM theme";
        return jdbcTemplate.query(findAllSql,
                (resultSet, numRow) -> new ReservationTheme(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail")
                        )
                );
    }

    public Optional<ReservationTheme> findById(Long id) {
        String findByIdSql = "SELECT id, name, description, thumbnail FROM theme WHERE id = ?";
        List<ReservationTheme> reservationThemes = jdbcTemplate.query(findByIdSql,
                (resultSet, numRow) -> new ReservationTheme(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail")
                ), id);

        return Optional.ofNullable(DataAccessUtils.singleResult(reservationThemes));
    }

    public Long insert(String name, String description, String thumbnail) {
        String insertSql = "INSERT INTO theme(name, description, thumbnail) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    insertSql,
                    new String[]{"id"});
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setString(3, thumbnail);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void deleteById(Long id) {
        String deleteFromIdSql = "DELETE FROM theme WHERE id = ?";
        jdbcTemplate.update(deleteFromIdSql, id);
    }
}
