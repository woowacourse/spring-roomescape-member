package roomescape.persistence.dao;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.business.domain.PlayTime;
import roomescape.business.domain.Reservation;
import roomescape.business.domain.Theme;
import roomescape.persistence.entity.ReservationEntity;
import roomescape.presentation.dto.reservation.ReservationAvailableTimeResponse;
import roomescape.presentation.dto.reservation.ReservationFilterDto;

@Repository
public class JdbcReservationDao implements ReservationDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcReservationDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(final Reservation reservation) {
        final ReservationEntity reservationEntity = ReservationEntity.from(reservation);
        final String sql = "INSERT INTO RESERVATION (user_id, date, time_id, theme_id) values (?, ?, ?, ?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, reservationEntity.userEntity().id());
            ps.setString(2, reservationEntity.date());
            ps.setLong(3, reservationEntity.playTimeEntity().id());
            ps.setLong(4, reservationEntity.themeEntity().id());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public List<Reservation> findAll() {
        final String sql =
                """
                        SELECT
                        r.id AS reservation_id,
                        u.id AS user_id,
                        u.name AS user_name,
                        u.email AS user_email,
                        u.password AS user_password,
                        u.role AS user_role,
                        r.date,
                        rt.id AS time_id,
                        rt.start_at AS time_value,
                        t.id AS theme_id,
                        t.name AS theme_name,
                        t.description AS theme_description,
                        t.thumbnail AS theme_thumbnail
                        FROM reservation AS r
                        INNER JOIN users AS u 
                        ON r.user_id = u.id
                        INNER JOIN reservation_time AS rt
                        ON r.time_id = rt.id 
                        INNER JOIN theme AS t 
                        ON r.theme_id = t.id
                        """;

        return jdbcTemplate.query(sql, ReservationEntity.getDefaultRowMapper()).stream()
                .map(ReservationEntity::toDomain)
                .toList();
    }

    @Override
    public boolean remove(final Long id) {
        final String sql = "DELETE FROM RESERVATION WHERE id = ?";
        final int rowNum = jdbcTemplate.update(sql, id);

        return rowNum == 1;
    }

    @Override
    public boolean existsByDateAndTimeAndTheme(
            final LocalDate date,
            final PlayTime time,
            final Theme theme
    ) {
        final String sql = "SELECT EXISTS (SELECT 1 FROM reservation WHERE date = ? AND time_id = ? AND theme_id = ?) AS is_exists";
        final int flag = jdbcTemplate.queryForObject(
                sql, Integer.class,
                ReservationEntity.formatDate(date),
                time.getId(),
                theme.getId()
        );

        return flag == 1;
    }

    @Override
    public List<ReservationAvailableTimeResponse> findAvailableTimesByDateAndTheme(
            final LocalDate date,
            final Theme theme
    ) {
        final String sql = """
                SELECT 
                    start_at, 
                    t.id AS time_id,                 
                    r.id AS reservation_id
                FROM
                    reservation_time AS t
                    LEFT JOIN ( 
                        SELECT *
                        FROM reservation
                        WHERE date = ? and theme_id = ?
                    ) AS r 
                    ON t.id = r.time_id
                """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new ReservationAvailableTimeResponse(
                        rs.getString(1),
                        rs.getLong(2),
                        rs.getLong(3) != 0
                ),
                ReservationEntity.formatDate(date),
                theme.getId()
        );
    }

    @Override
    public List<Reservation> findByFilter(final ReservationFilterDto filter) {
        final String sql =
                """
                        SELECT
                        r.id AS reservation_id,
                        u.id AS user_id,
                        u.name AS user_name,
                        u.email AS user_email,
                        u.password AS user_password,
                        u.role AS user_role,
                        r.date,
                        rt.id AS time_id,
                        rt.start_at AS time_value,
                        t.id AS theme_id,
                        t.name AS theme_name,
                        t.description AS theme_description,
                        t.thumbnail AS theme_thumbnail
                        FROM reservation AS r
                        INNER JOIN users AS u 
                        ON r.user_id = u.id
                        INNER JOIN reservation_time AS rt
                        ON r.time_id = rt.id 
                        INNER JOIN theme AS t 
                        ON r.theme_id = t.id
                        WHERE 1=1
                        """ + buildWhereQuery(filter);

        return jdbcTemplate.query(sql, ReservationEntity.getDefaultRowMapper()).stream()
                .map(ReservationEntity::toDomain)
                .toList();
    }

    private String buildWhereQuery(final ReservationFilterDto filter) {
        final StringBuilder whereQuery = new StringBuilder();

        if (filter.themeId() != null) {
            whereQuery.append(" AND ")
                    .append("t.id = ")
                    .append(filter.themeId());
        }
        if (filter.userId() != null) {
            whereQuery.append(" AND ")
                    .append("u.id = ")
                    .append(filter.userId());
        }
        if (filter.from() != null) {
            whereQuery.append(" AND ")
                    .append("r.date >= ")
                    .append("'")
                    .append(filter.from())
                    .append("'");
        }
        if (filter.to() != null) {
            whereQuery.append(" AND ")
                    .append("r.date <= ")
                    .append("'")
                    .append(filter.to())
                    .append("'");
        }

        return whereQuery.toString();
    }
}
