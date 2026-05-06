package roomescape.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcTemplateThemeRepository implements ThemeRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateThemeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Theme> findAll() {
        return jdbcTemplate.query("SELECT id, name, description, thumbnail_url FROM theme",
                (rs, rowNum) -> new Theme(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("thumbnail_url")
                ));
    }

    @Override
    public Theme save(Theme theme) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                conn -> {
                    PreparedStatement preparedStatement = conn.prepareStatement(
                            "INSERT INTO theme(name, description, thumbnail_url) " +
                                    "VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, theme.name());
                    preparedStatement.setString(2, theme.description());
                    preparedStatement.setString(3, theme.thumbnailUrl());

                    return preparedStatement;
                },
                keyHolder);

        return new Theme(
                Objects.requireNonNull(keyHolder.getKey()).longValue(),
                theme.name(),
                theme.description(),
                theme.thumbnailUrl());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM theme WHERE id = ?", id);
    }

    @Override
    public Optional<Theme> findById(Long id) {
        try {
            Theme theme = jdbcTemplate.queryForObject(
                    "SELECT id, name, description, thumbnail_url FROM theme WHERE id = ?",
                    (rs, rowNum) -> new Theme(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getString("thumbnail_url")
                    ),
                    id);

            return Optional.ofNullable(theme);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ReservationTime> findAvailableTimes(Long themeId, LocalDate date) {
        return jdbcTemplate.query(
                "SELECT t.id AS time_id, t.start_at " +
                        "FROM reservation_time t " +
                        "WHERE t.id NOT IN (" +
                        "  SELECT r.time_id FROM reservation r WHERE r.theme_id = ? AND r.date = ?" +
                        ")",

                (rs, rowNum) -> {
                    long timeId = rs.getLong("time_id");
                    LocalTime time = rs.getTime("start_at").toLocalTime();

                    ReservationTime reservationTime = new ReservationTime(timeId, time);

                    return reservationTime;
                },
                themeId,
                date
        );
    }
}
