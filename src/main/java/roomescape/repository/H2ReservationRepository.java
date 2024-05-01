package roomescape.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.ClientName;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.ThemeDescription;
import roomescape.domain.ThemeName;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class H2ReservationRepository implements ReservationRepository {
    private final NamedParameterJdbcTemplate template;

    public H2ReservationRepository(final NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public List<Reservation> findAll() {
        String sql = "SELECT "
                + "r.id as reservation_id, r.name as reservation_name, r.date as reservation_date, "
                + "rt.id as time_id, rt.start_at as reservation_time, "
                + "th.id as theme_id, th.name as theme_name, th.description as theme_description, th.thumbnail as theme_thumbnail "
                + "FROM reservation as r "
                + "inner join reservation_time as rt "
                + "on r.time_id = rt.id "
                + "inner join theme as th "
                + "on r.theme_id = th.id";

        return template.query(sql, itemRowMapper());
    }

    private RowMapper<Reservation> itemRowMapper() {
        return ((rs, rowNum) -> new Reservation(
                rs.getLong("reservation_id"),
                new ClientName(rs.getString("reservation_name")),
                rs.getDate("reservation_date").toLocalDate(),
                new ReservationTime(rs.getLong("time_id"), rs.getTime("reservation_time").toLocalTime()),
                new Theme(
                        rs.getLong("theme_id"),
                        new ThemeName(rs.getString("theme_name")),
                        new ThemeDescription(rs.getString("theme_description")),
                        rs.getString("theme_thumbnail"))
        ));
    }

    @Override
    public Optional<Reservation> findById(final Long reservationId) {
        String sql = "SELECT "
                + "r.id as reservation_id, r.name as reservation_name, r.date as reservation_date, "
                + "rt.id as time_id, rt.start_at as reservation_time, "
                + "th.id as theme_id, th.name as theme_name, th.description as theme_description, th.thumbnail as theme_thumbnail "
                + "FROM reservation as r "
                + "inner join reservation_time as rt "
                + "on r.time_id = rt.id "
                + "inner join theme as th "
                + "on r.theme_id = th.id "
                + "WHERE r.id = :reservationId";

        try {
            MapSqlParameterSource param = new MapSqlParameterSource()
                    .addValue("reservationId", reservationId);
            Reservation reservation = template.queryForObject(sql, param, itemRowMapper());

            return Optional.of(reservation);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Reservation save(final Reservation reservation) {
        String sql = "INSERT INTO reservation(name, date, time_id, theme_id) VALUES (:name, :date, :timeId, :themeId)";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("name", reservation.getClientName().getValue())
                .addValue("date", reservation.getDate())
                .addValue("timeId", reservation.getTime().getId())
                .addValue("themeId", reservation.getTheme().getId());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(sql, param, keyHolder);

        long savedReservationId = keyHolder.getKey().longValue();

        return reservation.initializeIndex(savedReservationId);
    }

    @Override
    public void deleteById(final Long reservationId) {
        String sql = "DELETE FROM reservation WHERE id = :id";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", reservationId);
        template.update(sql, param);
    }

    @Override
    public boolean existByDateAndTimeId(final LocalDate date, final Long timeId) {
        String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE date = :date AND time_id = :timeId)";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("date", date)
                .addValue("timeId", timeId);

        return Boolean.TRUE.equals(template.queryForObject(sql, param, Boolean.class));
    }

    @Override
    public boolean existByTimeId(final Long reservationTimeId) {
        String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE time_id = :timeId)";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("timeId", reservationTimeId);

        return Boolean.TRUE.equals(template.queryForObject(sql, param, Boolean.class));
    }

    @Override
    public List<Reservation> findAllByDateAndThemeId(final LocalDate date, final Long themeId) {
        String sql = "SELECT "
                + "r.id as reservation_id, r.name as reservation_name, r.date as reservation_date, "
                + "rt.id as time_id, rt.start_at as reservation_time, "
                + "th.id as theme_id, th.name as theme_name, th.description as theme_description, th.thumbnail as theme_thumbnail "
                + "FROM reservation as r "
                + "inner join reservation_time as rt "
                + "on r.time_id = rt.id "
                + "inner join theme as th "
                + "on r.theme_id = th.id "
                + "WHERE date = :date and theme_id = :themeId";

        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("date", date)
                .addValue("themeId", themeId);

        return template.query(sql, param, itemRowMapper());
    }
}
