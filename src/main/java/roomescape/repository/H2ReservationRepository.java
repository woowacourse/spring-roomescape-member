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
                + "r.id as reservation_id, r.name as name, r.date as date, t.id as time_id, t.start_at as time_value "
                + "FROM reservation as r "
                + "inner join reservation_time as t "
                + "on r.time_id = t.id";

        return template.query(sql, itemRowMapper());
    }

    private RowMapper<Reservation> itemRowMapper() {
        return ((rs, rowNum) -> new Reservation(
                rs.getLong("reservation_id"),
                new ClientName(rs.getString("name")),
                rs.getDate("date").toLocalDate(),
                new ReservationTime(rs.getLong("time_id"), rs.getTime("time_value").toLocalTime())
        ));
    }

    @Override
    public Optional<Reservation> findById(final Long reservationId) {
        String sql = "SELECT "
                + "r.id as reservation_id, r.name as name, r.date as date, t.id as time_id, t.start_at as time_value "
                + "FROM reservation as r "
                + "inner join reservation_time as t "
                + "on r.time_id = t.id "
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
        String sql = "INSERT INTO reservation(name, date, time_id) VALUES (:name, :date, :timeId)";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("name", reservation.getClientName().getValue())
                .addValue("date", reservation.getDate())
                .addValue("timeId", reservation.getTime().getId());
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
    public boolean existByDate(final LocalDate date) {
        String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE date = :date)";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("date", date);

        return Boolean.TRUE.equals(template.queryForObject(sql, param, Boolean.class));
    }
}
