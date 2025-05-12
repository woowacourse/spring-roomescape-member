package roomescape.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.entity.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class H2ReservationDao implements ReservationDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public H2ReservationDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
            SELECT
                 r.id AS id,
                 m.id AS member_id,
                 m.name AS member_name,
                 m.email AS member_email,
                 m.role AS member_role,
                 r.date AS date,
                 t.id AS time_id,
                 t.start_at AS time_value,
                 th.id AS theme_id,
                 th.name AS theme_name,
                 th.description AS theme_description,
                 th.thumbnail AS theme_thumbnail
            FROM reservation r
            JOIN reservation_time t ON r.time_id = t.id
            JOIN theme th ON r.theme_id = th.id
            JOIN member m ON r.member_id = m.id
            """;
        return jdbcTemplate.query(sql, getReservationRowMapper());
    }

    @Override
    public Reservation save(Reservation reservation) {
        String sql = "INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES(:member_id, :date, :time_id, :theme_id)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("member_id", reservation.getMember().getId())
            .addValue("date", reservation.getDate())
            .addValue("time_id", reservation.getTime().getId())
            .addValue("theme_id", reservation.getTheme().getId());
        jdbcTemplate.update(sql, mapSqlParameterSource, keyHolder);

        Number key = keyHolder.getKey();
        return new Reservation(key.longValue(), reservation.getMember(), reservation.getDate(), reservation.getTime(), reservation.getTheme());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM reservation WHERE id = :id";
        jdbcTemplate.update(sql, new MapSqlParameterSource("id", id));
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        String sql = """
            SELECT
                 r.id AS id,
                 m.id AS member_id,
                 m.name AS member_name,
                 m.email AS member_email,
                 m.role AS member_role,
                 r.date AS date,
                 t.id AS time_id,
                 t.start_at AS time_value,
                 th.id AS theme_id,
                 th.name AS theme_name,
                 th.description AS theme_description,
                 th.thumbnail AS theme_thumbnail
            FROM reservation r
            JOIN reservation_time t ON r.time_id = t.id
            JOIN theme th ON r.theme_id = th.id
            JOIN member m ON r.member_id = m.id
            WHERE r.id = :id
            """;
        List<Reservation> findReservation = jdbcTemplate.query(
            sql, new MapSqlParameterSource("id", id), getReservationRowMapper());

        return findReservation.stream().findFirst();
    }

    @Override
    public boolean isExistByTimeId(Long timeId) {
        String sql = """
            SELECT EXISTS 
                (SELECT 1 
                 FROM reservation 
                 WHERE time_id = :time_id
                )
            """;
        return Boolean.TRUE == jdbcTemplate.queryForObject(
            sql, new MapSqlParameterSource("time_id", timeId), Boolean.class);
    }

    @Override
    public List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId) {
        String sql = """
            SELECT
                 r.id AS id,
                 m.id AS member_id,
                 m.name AS member_name,
                 m.email AS member_email,
                 m.role AS member_role,
                 r.date AS date,
                 t.id AS time_id,
                 t.start_at AS time_value,
                 th.id AS theme_id,
                 th.name AS theme_name,
                 th.description AS theme_description,
                 th.thumbnail AS theme_thumbnail
            FROM reservation r
            JOIN reservation_time t ON r.time_id = t.id
            JOIN theme th ON r.theme_id = th.id
            JOIN member m ON r.member_id = m.id
            WHERE th.id = :theme_id AND r.date = :date
            """;
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("theme_id", themeId)
            .addValue("date", date);
        return jdbcTemplate.query(sql, mapSqlParameterSource, getReservationRowMapper());
    }

    @Override
    public List<Reservation> findByMemberIdAndThemeIdAndDateBetween(Long memberId, Long themeId, LocalDate from, LocalDate to) {
        String sql = """
            SELECT
                 r.id AS id,
                 m.id AS member_id,
                 m.name AS member_name,
                 m.email AS member_email,
                 m.role AS member_role,
                 r.date AS date,
                 t.id AS time_id,
                 t.start_at AS time_value,
                 th.id AS theme_id,
                 th.name AS theme_name,
                 th.description AS theme_description,
                 th.thumbnail AS theme_thumbnail
            FROM reservation r
            JOIN reservation_time t ON r.time_id = t.id
            JOIN theme th ON r.theme_id = th.id
            JOIN member m ON r.member_id = m.id
            WHERE m.id = :member_id AND th.id = :theme_id
                  AND r.date BETWEEN :date_from AND :date_to
            """;

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
            .addValue("theme_id", themeId)
            .addValue("member_id", memberId)
            .addValue("date_from", from)
            .addValue("date_to", to);
        return jdbcTemplate.query(sql, mapSqlParameterSource, getReservationRowMapper());
    }

    private RowMapper<Reservation> getReservationRowMapper() {
        return (resultSet, rowNum) -> new Reservation(
            resultSet.getLong("id"),
            new Member(
                resultSet.getLong("member_id"),
                resultSet.getString("member_name"),
                resultSet.getString("member_email"),
                Role.valueOf(resultSet.getString("member_role"))
            ),
            resultSet.getObject("date", LocalDate.class),
            new ReservationTime(
                resultSet.getLong("time_id"),
                resultSet.getObject("time_value", LocalTime.class)
            ),
            new Theme(
                resultSet.getLong("theme_id"),
                resultSet.getString("theme_name"),
                resultSet.getString("theme_description"),
                resultSet.getString("theme_thumbnail")
            )
        );
    }
}
