package roomescape.domain.reservation.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.common.exception.EntityNotFoundException;
import roomescape.domain.auth.entity.Name;
import roomescape.domain.auth.entity.Password;
import roomescape.domain.auth.entity.Roles;
import roomescape.domain.auth.entity.User;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.entity.ReservationTime;
import roomescape.domain.reservation.entity.Theme;
import roomescape.domain.reservation.repository.ReservationRepository;

@Repository
public class ReservationDAO implements ReservationRepository {

    private static final String TABLE_NAME = "reservation";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public ReservationDAO(final NamedParameterJdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> findAll() {
        final String sql = """
                select rs.id as reservation_id, rs.user_id, rs.date,
                rst.id as reservation_time_id, rst.start_at,
                th.id as theme_id, th.name as theme_name, th.description, th.thumbnail,
                u.email, u.name as user_name, u.password, u.role
                from reservation rs
                INNER JOIN reservation_time rst ON rs.time_id = rst.id
                INNER JOIN theme th ON rs.theme_id = th.id
                INNER JOIN users u ON rs.user_id = u.id
                """;

        return jdbcTemplate.query(sql, (resultSet, rowNum) -> reservationOf(resultSet));
    }

    @Override
    public List<Reservation> findReservations(final Long themeId, final Long userId, final LocalDate dateFrom,
                                              final LocalDate dateTo) {
        final String sql = """
                select rs.id as reservation_id, rs.user_id, rs.date,
                rst.id as reservation_time_id, rst.start_at,
                th.id as theme_id, th.name as theme_name, th.description, th.thumbnail,
                u.email, u.name as user_name, u.password, u.role
                from reservation rs
                INNER JOIN reservation_time rst ON rs.time_id = rst.id
                INNER JOIN theme th ON rs.theme_id = th.id
                INNER JOIN users u ON rs.user_id = u.id
                WHERE (:themeId IS NULL OR th.id = :themeId)
                  AND (:user_id IS NULL OR rs.user_id = :user_id)
                  AND (:dateFrom IS NULL OR rs.date >= :dateFrom)
                  AND (:dateTo IS NULL OR rs.date <= :dateTo)
                """;

        final Map<String, Object> params = findAllParamsOf(themeId, userId, dateFrom, dateTo);

        return jdbcTemplate.query(sql, params, (resultSet, rowNum) -> reservationOf(resultSet));
    }

    private Map<String, Object> findAllParamsOf(final Long themeId, final Long userId, final LocalDate dateFrom,
                                                final LocalDate dateTo) {
        final HashMap<String, Object> params = new HashMap<>();

        params.put("themeId", themeId);
        params.put("user_id", userId);
        params.put("dateFrom", dateFrom);
        params.put("dateTo", dateTo);

        return params;
    }

    @Override
    public List<Reservation> findByDateAndThemeId(final LocalDate date, final Long themeId) {
        final String sql = """
                select rs.id as reservation_id, rs.user_id, rs.date,
                rst.id as reservation_time_id, rst.start_at,
                th.id as theme_id, th.name as theme_name, th.description, th.thumbnail,
                u.email, u.name as user_name, u.password, u.role
                from reservation rs
                inner join reservation_time rst on rs.time_id = rst.id
                inner join theme th on rs.theme_id = th.id
                INNER JOIN users u ON rs.user_id = u.id
                where th.id = :theme_id and rs.date = :date
                """;

        final Map<String, Object> params = Map.of("theme_id", themeId, "date", date);

        return jdbcTemplate.query(sql, params, (resultSet, rowNum) -> reservationOf(resultSet));
    }

    @Override
    public Optional<Reservation> findById(final Long id) {
        final Map<String, Long> params = Map.of("reservation_id", id);

        return reservationOf(params);
    }

    @Override
    public Reservation save(final Reservation reservation) {
        if (reservation.existId()) {
            return update(reservation);
        }

        return create(reservation);
    }

    @Override
    public void deleteById(final Long id) {
        final String sql = "delete from reservation where id = :id";
        final Map<String, Long> params = Map.of("id", id);

        final int updatedRowCount = jdbcTemplate.update(sql, params);

        if (updatedRowCount != 1) {
            throw new EntityNotFoundException("Reservation with id " + id + " not found");
        }
    }

    @Override
    public boolean existsByDateAndTimeId(final LocalDate date, final Long timeId) {
        final String sql = "select exists(select 1 from " + TABLE_NAME + " where date = :date and time_id = :time_id)";
        final Map<String, Object> params = Map.of("date", date, "time_id", timeId);

        return jdbcTemplate.queryForObject(sql, params, Boolean.class);
    }

    @Override
    public boolean existsByTimeId(final Long timeId) {
        final String selectSql = "select exists(select 1 from " + TABLE_NAME + " where time_id = :time_id)";
        final Map<String, Long> params = Map.of("time_id", timeId);

        return jdbcTemplate.queryForObject(selectSql, params, Boolean.class);
    }

    @Override
    public boolean existsByThemeId(final Long themeId) {
        final String selectSql = "select exists(select 1 from " + TABLE_NAME + " where theme_id = :theme_id)";

        final Map<String, Long> params = Map.of("theme_id", themeId);

        return jdbcTemplate.queryForObject(selectSql, params, Boolean.class);
    }

    private Reservation update(final Reservation reservation) {
        final String updateReservationSql = """
                update reservation
                set user_id = :user_id, date = :date, time_id = :time_id, theme_id = :theme_id
                where id =:id
                """;

        final MapSqlParameterSource params = new MapSqlParameterSource().addValue("date",
                        reservation.getReservationDate())
                .addValue("time_id", reservation.getReservationTimeId())
                .addValue("theme_id", reservation.getThemeId())
                .addValue("user_id", reservation.getUser()
                        .getId())
                .addValue("id", reservation.getId());

        final int updatedRowCount = jdbcTemplate.update(updateReservationSql, params);

        if (updatedRowCount == 0) {
            throw new EntityNotFoundException("Reservation with id " + reservation.getId() + " not found");
        }

        return reservation;
    }

    private Reservation create(final Reservation reservation) {
        final MapSqlParameterSource params = new MapSqlParameterSource().addValue("date",
                        reservation.getReservationDate())
                .addValue("time_id", reservation.getReservationTimeId())
                .addValue("theme_id", reservation.getThemeId())
                .addValue("user_id", reservation.getUser()
                        .getId());

        final long id = jdbcInsert.executeAndReturnKey(params)
                .longValue();

        return new Reservation(id, reservation.getUser(), reservation.getReservationDate(),
                reservation.getReservationTime(), reservation.getTheme());
    }

    private Optional<Reservation> reservationOf(final Map<String, Long> params) {
        final String sql = """
                select rs.id as reservation_id, rs.user_id, rs.date,
                rst.id as reservation_time_id, rst.start_at,
                th.id as theme_id, th.name as theme_name, th.description, th.thumbnail,
                u.email, u.name as user_name, u.password, u.role
                from reservation rs
                INNER JOIN reservation_time rst ON rs.time_id = rst.id
                INNER JOIN theme th ON rs.theme_id = th.id
                INNER JOIN users u ON rs.user_id = u.id
                WHERE rs.id = :reservation_id
                """;

        try {
            final Reservation reservation = jdbcTemplate.queryForObject(sql, params,
                    (resultSet, rowNum) -> reservationOf(resultSet));
            return Optional.ofNullable(reservation);
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private Reservation reservationOf(final ResultSet resultSet) throws SQLException {
        return Reservation.builder()
                .id(resultSet.getLong("reservation_id"))
                .user(userOf(resultSet))
                .reservationDate(LocalDate.parse(resultSet.getString("date")))
                .reservationTime(reservationTimeOf(resultSet))
                .theme(themeOf(resultSet))
                .build();
    }

    private User userOf(final ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("user_id"))
                .email(resultSet.getString("email"))
                .username(new Name(resultSet.getString("user_name")))
                .password(Password.of(resultSet.getString("password")))
                .role(Roles.from(resultSet.getString("role")))
                .build();
    }

    private ReservationTime reservationTimeOf(final ResultSet resultSet) throws SQLException {

        return ReservationTime.builder()
                .id(resultSet.getLong("reservation_time_id"))
                .startAt(LocalTime.parse(resultSet.getString("start_at")))
                .build();
    }

    private Theme themeOf(final ResultSet resultSet) throws SQLException {
        return Theme.builder()
                .id(resultSet.getLong("theme_id"))
                .name(resultSet.getString("theme_name"))
                .description(resultSet.getString("description"))
                .thumbnail(resultSet.getString("thumbnail"))
                .build();
    }
}
