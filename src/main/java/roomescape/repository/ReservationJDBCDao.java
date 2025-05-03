package roomescape.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;
import roomescape.entity.Theme;
import roomescape.exceptions.EntityNotFoundException;

@Repository
public class ReservationJDBCDao implements ReservationRepository {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public ReservationJDBCDao(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                select r.id as reservation_id, r.name, r.date, t.id as time_id, t.start_at, th.id as theme_id,th.name as theme_name, th.description, th.thumbnail 
                from reservation as r 
                inner join reservation_time as t 
                on r.time_id = t.id
                inner join theme as th
                on r.theme_id = th.id
                """;
        return namedJdbcTemplate.query(sql, getReservationRowMapper());
    }

    @Override
    public Reservation save(Reservation reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into reservation (name, date, time_id, theme_id) values (:name, :date, :timeId, :themeId)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", reservation.name())
                .addValue("date", Date.valueOf(reservation.date()))
                .addValue("timeId", reservation.time().id())
                .addValue("themeId", reservation.theme().id());

        namedJdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new Reservation(id, reservation.name(), reservation.date(), reservation.time(), reservation.theme());
    }

    @Override
    public void deleteById(long id) {
        String sql = "delete from reservation where id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        int result = namedJdbcTemplate.update(sql, params);
        if (result == 0) {
            throw new EntityNotFoundException("예약 데이터를 찾을 수 없습니다:" + id);
        }
    }

    @Override
    public List<Long> findBookedTimeIdsByDateAndThemeId(LocalDate date, Long themeId) {
        String sql = """
                select t.id as time_id
                from reservation as r 
                inner join reservation_time as t 
                on r.time_id = t.id
                inner join theme as th
                on r.theme_id = th.id
                where r.date = :date
                and th.id = :themeId
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("date", date)
                .addValue("themeId", themeId);

        return namedJdbcTemplate.query(sql, params, (rs, rowNum) -> rs.getLong("time_id"));
    }

    private RowMapper<Reservation> getReservationRowMapper() {
        return (resultSet, rowNum) -> new Reservation(
                resultSet.getLong("reservation_id"),
                resultSet.getString("name"),
                resultSet.getDate("date").toLocalDate(),
                new ReservationTime(
                        resultSet.getLong("time_id"),
                        resultSet.getTime("start_at").toLocalTime()
                ),
                new Theme(
                        resultSet.getLong("theme_id"),
                        resultSet.getString("theme_name"),
                        resultSet.getString("description"),
                        resultSet.getString("thumbnail")
                )
        );
    }
}
