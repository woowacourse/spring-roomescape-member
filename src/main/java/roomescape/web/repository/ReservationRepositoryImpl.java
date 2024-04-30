package roomescape.web.repository;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.core.domain.Reservation;
import roomescape.core.domain.ReservationTime;
import roomescape.core.domain.Theme;
import roomescape.core.repository.ReservationRepository;

@Repository
public class ReservationRepositoryImpl implements ReservationRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ReservationRepositoryImpl(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Long save(final Reservation reservation) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTimeId())
                .addValue("theme_id", reservation.getThemeId());
        return jdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    @Override
    public List<Reservation> findAll() {
        final String query = """
                SELECT
                    r.id as reservation_id,
                    r.name,
                    r.date,
                    t.id as time_id,
                    t.start_at as time_value,
                    m.id as theme_id,
                    m.name as theme_name,
                    m.description as theme_description,
                    m.thumbnail as theme_thumbnail
                FROM reservation as r
                inner join reservation_time as t
                on r.time_id = t.id
                inner join theme as m
                on r.theme_id = m.id
                """;

        return jdbcTemplate.query(query, getReservationRowMapper());
    }

    private RowMapper<Reservation> getReservationRowMapper() {
        return (resultSet, rowNum) -> {
            final Long id = resultSet.getLong("id");
            final String name = resultSet.getString("name");
            final String date = resultSet.getString("date");
            final Long tId = resultSet.getLong("time_id");
            final String timeValue = resultSet.getString("time_value");
            final ReservationTime time = new ReservationTime(tId, timeValue);
            final Long mId = resultSet.getLong("theme_id");
            final String themeName = resultSet.getString("theme_name");
            final String themeDescription = resultSet.getString("theme_description");
            final String themeThumbnail = resultSet.getString("theme_thumbnail");
            final Theme theme = new Theme(mId, themeName, themeDescription, themeThumbnail);

            return new Reservation(id, name, date, time, theme);
        };
    }

    @Override
    public Integer countByTimeId(final long timeId) {
        final String query = """
                SELECT count(*)
                FROM reservation
                WHERE time_id = ?
                """;
        return jdbcTemplate.queryForObject(query, Integer.class, timeId);
    }

    @Override
    public Integer countByThemeId(final long themeId) {
        final String query = """
                SELECT count(*)
                FROM reservation
                WHERE theme_id = ?
                """;
        return jdbcTemplate.queryForObject(query, Integer.class, themeId);
    }

    @Override
    public Integer countByDateAndTimeIdAndThemeId(final String date, final long timeId, final long themeId) {
        final String query = """
                SELECT count(*)
                FROM reservation as r
                inner join reservation_time as t
                on r.time_id = t.id
                inner join theme as m
                on r.theme_id = m.id
                WHERE r.date = ? and t.id = ? and m.id = ?
                """;

        return jdbcTemplate.queryForObject(query, Integer.class, date, timeId, themeId);
    }

    @Override
    public void deleteById(final long id) {
        jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
    }
}
