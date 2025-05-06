package roomescape.domain.reservation.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
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
import roomescape.domain.reservation.entity.Name;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.entity.ReservationTime;
import roomescape.domain.reservation.entity.Theme;
import roomescape.domain.reservation.repository.ReservationRepository;

@Repository
public class ReservationDao implements ReservationRepository {

    private static final String TABLE_NAME = "reservation";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public ReservationDao(final NamedParameterJdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                select rs.id as reservation_id, rs.name, rs.date, 
                       rst.id as reservation_time_id, rst.start_at,
                       th.id as theme_id, th.name as theme_name, th.description, th.thumbnail
                from reservation rs
                INNER JOIN reservation_time rst ON rs.time_id = rst.id
                INNER JOIN theme th ON rs.theme_id = th.id
                """;

        return jdbcTemplate.query(sql,
                (resultSet, rowNum) -> reservationOf(resultSet)
        );
    }

    @Override
    public List<Reservation> findByDateAndThemeId(final LocalDate date, final Long themeId) {
        String sql = """
                select rs.id as reservation_id, rs.name, rs.date,
                    rst.id as reservation_time_id, rst.start_at,
                    th.id as theme_id, th.name as theme_name, th.description, th.thumbnail
                from reservation rs
                inner join reservation_time rst on rs.time_id = rst.id
                inner join theme th on rs.theme_id = th.id
                where th.id = :theme_id and rs.date = :date
                """;

        Map<String, Object> params = Map.of("theme_id", themeId, "date", date);

        return jdbcTemplate.query(sql, params,
                (resultSet, rowNum) -> reservationOf(resultSet)
        );
    }

    @Override
    public Optional<Reservation> findById(final Long id) {
        String sql = """
                select rs.id as reservation_id, rs.name, rs.date,
                       rst.id as reservation_time_id, rst.start_at,
                       th.id as theme_id, th.name as theme_name, th.description, th.thumbnail
                from reservation rs
                INNER JOIN reservation_time rst ON rs.time_id = rst.id
                INNER JOIN theme th ON rs.theme_id = th.id
                WHERE rs.id = :reservation_id
                """;

        Map<String, Long> params = Map.of("reservation_id", id);

        return reservationOf(sql, params);
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
        String sql = "delete from reservation where id = :id";
        Map<String, Long> params = Map.of("id", id);

        int updatedRowCount = jdbcTemplate.update(sql, params);

        if (updatedRowCount != 1) {
            throw new EntityNotFoundException("Reservation with id " + id + " not found");
        }
    }

    @Override
    public boolean existsByDateAndTimeIdAndThemeId(final LocalDate date, final Long timeId, final Long themeId) {
        String sql = """
                select count(*)
                from reservation
                where date = :date and time_id = :time_id and theme_id = :theme_id
                """;
        Map<String, Object> params = Map.of("date", date, "time_id", timeId, "theme_id", themeId);

        int rowCountByDateAndTimeAndTheme = jdbcTemplate.queryForObject(sql, params, Integer.class);
        return rowCountByDateAndTimeAndTheme == 1;
    }

    @Override
    public boolean existsByTimeId(final Long timeId) {
        String selectSql = "select count(*) from reservation where time_id = :time_id";
        Map<String, Long> params = Map.of("time_id", timeId);

        int rowCountByTimeId = jdbcTemplate.queryForObject(selectSql, params, Integer.class);

        return rowCountByTimeId > 0;
    }

    @Override
    public boolean existsByThemeId(final Long themeId) {
        String selectSql = "select count(*) from reservation where theme_id = :theme_id";

        Map<String, Long> params = Map.of("theme_id", themeId);

        int rowCountByThemeId = jdbcTemplate.queryForObject(selectSql, params, Integer.class);

        return rowCountByThemeId > 0;
    }

    private Optional<Reservation> reservationOf(final String sql, final Map<String, Long> params) {
        try {
            Reservation reservation = jdbcTemplate.queryForObject(sql,
                    params,
                    (resultSet, rowNum) -> reservationOf(resultSet));
            return Optional.ofNullable(reservation);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("entity not found");
        }
    }

    private Reservation reservationOf(final ResultSet resultSet) throws SQLException {
        return new Reservation(
                resultSet.getLong("reservation_id"),
                new Name(resultSet.getString("name")),
                LocalDate.parse(resultSet.getString("date")),
                reservationTimeOf(resultSet),
                themeOf(resultSet)
        );
    }

    private ReservationTime reservationTimeOf(final ResultSet resultSet) throws SQLException {
        return new ReservationTime(
                resultSet.getLong("reservation_time_id"),
                LocalTime.parse(resultSet.getString("start_at")
                ));
    }

    private Theme themeOf(final ResultSet resultSet) throws SQLException {
        return Theme.builder()
                .id(resultSet.getLong("theme_id"))
                .name(resultSet.getString("theme_name"))
                .description(resultSet.getString("description"))
                .thumbnail(resultSet.getString("thumbnail"))
                .build();
    }

    private Reservation create(final Reservation reservation) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.getName().getValue())
                .addValue("date", reservation.getReservationDate())
                .addValue("time_id", reservation.getReservationTimeId())
                .addValue("theme_id", reservation.getThemeId());

        long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return new Reservation(id, reservation.getName(), reservation.getReservationDate(),
                reservation.getReservationTime(), reservation.getTheme());
    }

    private Reservation update(final Reservation reservation) {
        String updateReservationSql = """
                update reservation 
                set name = :name, date = :date, time_id = :time_id, theme_id = :theme_id
                where id =:id
                """;

        checkReservation(reservation);

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.getName().getValue())
                .addValue("date", reservation.getReservationDate())
                .addValue("time_id", reservation.getReservationTimeId())
                .addValue("theme_id", reservation.getThemeId())
                .addValue("id", reservation.getId());

        int updatedRowCount = jdbcTemplate.update(updateReservationSql, params);

        if (updatedRowCount == 0) {
            throw new EntityNotFoundException("Reservation with id " + reservation.getId() + " not found");
        }

        return reservation;
    }

    private void checkReservation(final Reservation reservation) {
        if (reservation.getReservationTime() == null || reservation.getTheme() == null) {
            throw new EntityNotFoundException("reservation field is null");
        }
    }
}
