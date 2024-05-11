package roomescape.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

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
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTimeId())
                .addValue("theme_id", reservation.getThemeId())
                .addValue("member_id", reservation.getMemberId());
        return jdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    @Override
    public List<Reservation> findAllByDateAndThemeId(final String date, final long themeId) {
        final String query = """
                SELECT
                    r.id AS reservation_id,
                    r.date,
                    t.id AS time_id,
                    t.start_at AS time_value,
                    m.id AS theme_id,
                    m.name AS theme_name,
                    m.description AS theme_description,
                    m.thumbnail AS theme_thumbnail,
                    b.id AS member_id,
                    b.name AS member_name,
                    b.email AS member_email,
                    b.password AS member_password,
                    b.role AS member_role
                FROM reservation AS r
                INNER JOIN reservation_time AS t
                ON r.time_id = t.id
                INNER JOIN theme AS m
                ON r.theme_id = m.id
                INNER JOIN member AS b
                ON r.member_id = b.id
                WHERE r.date = ? AND r.theme_id = ?
                """;
        return jdbcTemplate.query(query, getReservationRowMapper(), date, themeId);
    }

    @Override
    public List<Reservation> findAllWithConditions(
            final Long memberId,
            final Long themeId,
            final LocalDate dateFrom,
            final LocalDate dateTo
    ) {
        final StringBuilder queryBuilder = new StringBuilder("""
                SELECT
                    r.id AS reservation_id,
                    r.date,
                    t.id AS time_id,
                    t.start_at AS time_value,
                    m.id AS theme_id,
                    m.name AS theme_name,
                    m.description AS theme_description,
                    m.thumbnail AS theme_thumbnail,
                    b.id AS member_id,
                    b.name AS member_name,
                    b.email AS member_email,
                    b.password AS member_password,
                    b.role AS member_role
                FROM reservation AS r
                INNER JOIN reservation_time AS t
                ON r.time_id = t.id
                INNER JOIN theme AS m
                ON r.theme_id = m.id
                INNER JOIN member AS b
                ON r.member_id = b.id
                """
        );

        final List<String> arguments = new ArrayList<>();

        appendQueryIfNotNull(memberId, " r.member_id = ?", queryBuilder, arguments);
        appendQueryIfNotNull(themeId, " r.theme_id = ?", queryBuilder, arguments);
        appendQueryIfNotNull(dateFrom, " r.date >= ?", queryBuilder, arguments);
        appendQueryIfNotNull(dateTo, " r.date <= ?", queryBuilder, arguments);

        return jdbcTemplate.query(queryBuilder.toString(), getReservationRowMapper(), arguments.toArray());
    }

    @Override
    public boolean hasReservationAtTime(final long timeId) {
        final String query = "SELECT EXISTS(SELECT 1 FROM reservation WHERE time_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, timeId));
    }

    @Override
    public boolean hasReservationWithTheme(final long themeId) {
        final String query = "SELECT EXISTS(SELECT 1 FROM reservation WHERE theme_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, themeId));
    }

    @Override
    public boolean hasDuplicateReservation(final String date, final long timeId, final long themeId) {
        final String query = "SELECT EXISTS(SELECT 1 FROM reservation WHERE date = ? AND time_id = ? AND theme_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class, date, timeId, themeId));
    }

    @Override
    public void deleteById(final long id) {
        jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
    }

    private RowMapper<Reservation> getReservationRowMapper() {
        return (resultSet, rowNum) -> {
            final ReservationTime time = new ReservationTime(
                    resultSet.getLong("time_id"),
                    resultSet.getString("time_value")
            );

            final Theme theme = new Theme(
                    resultSet.getLong("theme_id"),
                    resultSet.getString("theme_name"),
                    resultSet.getString("theme_description"),
                    resultSet.getString("theme_thumbnail")
            );

            final Member member = new Member(
                    resultSet.getLong("member_id"),
                    resultSet.getString("member_name"),
                    resultSet.getString("member_email"),
                    resultSet.getString("member_password"),
                    resultSet.getString("member_role")
            );

            return new Reservation(
                    resultSet.getLong("id"),
                    member,
                    resultSet.getString("date"),
                    time,
                    theme
            );
        };
    }

    private void appendQueryIfNotNull(
            final Object value,
            final String condition,
            final StringBuilder queryBuilder,
            final List<String> arguments
    ) {
        if (value != null) {
            queryBuilder.append(findConjunction(arguments));
            queryBuilder.append(condition);
            arguments.add(String.valueOf(value));
        }
    }

    private String findConjunction(final List<String> arguments) {
        if (arguments.isEmpty()) {
            return " WHERE";
        }
        return " AND";
    }
}
