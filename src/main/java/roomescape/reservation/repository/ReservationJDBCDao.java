package roomescape.reservation.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.global.exception.EntityNotFoundException;
import roomescape.member.entity.Member;
import roomescape.member.entity.Role;
import roomescape.reservation.entity.Reservation;
import roomescape.theme.entity.Theme;
import roomescape.time.entity.ReservationTime;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Repository
public class ReservationJDBCDao implements ReservationRepository {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public ReservationJDBCDao(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                select 
                    r.id as reservation_id, 
                    r.date, 
                    m.id as member_id,
                    m.email as member_email,
                    m.name as member_name,
                    m.role as member_role,
                    t.id as time_id, 
                    t.start_at, 
                    th.id as theme_id,
                    th.name as theme_name, 
                    th.description, 
                    th.thumbnail 
                from reservation as r 
                inner join member as m on r.member_id = m.id
                inner join reservation_time as t on r.time_id = t.id
                inner join theme as th on r.theme_id = th.id
                """;
        return namedJdbcTemplate.query(sql, getReservationRowMapper());
    }

    @Override
    public Reservation save(Reservation reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into reservation (member_id, date, time_id, theme_id) values (:memberId, :date, :timeId, :themeId)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberId", reservation.getMember().getId())
                .addValue("date", Date.valueOf(reservation.getDate()))
                .addValue("timeId", reservation.getTime().getId())
                .addValue("themeId", reservation.getTheme().getId());

        namedJdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new Reservation(id, reservation.getMember(), reservation.getDate(), reservation.getTime(), reservation.getTheme());
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

    @Override
    public List<Reservation> findAllByFilter(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo) {
        String sql = """
                select 
                r.id as reservation_id, 
                r.date as date, 
                t.id as time_id,
                t.start_at,
                th.id as theme_id,
                th.name as theme_name, 
                th.description as theme_description, 
                th.thumbnail as theme_thumbnail,
                m.id as member_id,
                m.email as member_email,
                m.name as member_name,
                m.role as member_role
                from reservation as r
                inner join reservation_time as t 
                on r.time_id = t.id
                inner join theme as th 
                on r.theme_id = th.id
                inner join member as m
                on r.member_id = m.id
                where th.id = :theme_id and m.id = :member_id 
                and r.date between :date_from and :date_to
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("theme_id", themeId)
                .addValue("member_id", memberId)
                .addValue("date_from", dateFrom)
                .addValue("date_to", dateTo);
        return namedJdbcTemplate.query(sql, params, getReservationRowMapper());
    }

    private RowMapper<Reservation> getReservationRowMapper() {
        return (resultSet, rowNum) -> new Reservation(
                resultSet.getLong("reservation_id"),
                new Member(
                        resultSet.getLong("member_id"),
                        resultSet.getString("member_email"),
                        null,
                        resultSet.getString("member_name"),
                        Role.valueOf(resultSet.getString("member_role"))
                ),
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
