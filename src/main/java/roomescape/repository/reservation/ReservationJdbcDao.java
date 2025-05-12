package roomescape.repository.reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.reservation.Theme;
import roomescape.exceptions.EntityNotFoundException;
import roomescape.repository.reservation.mapper.ReservationRowMapper;

@Repository
public class ReservationJdbcDao implements ReservationRepository {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public ReservationJdbcDao(NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    @Override
    public List<Reservation> findAll() {
        String sql = """
                select r.id as reservation_id, r.date, t.id as time_id, t.start_at, th.id as theme_id,th.name as theme_name, th.description, th.thumbnail, m.id as member_id, m.name as member_name,m.email, m.password 
                from reservation as r 
                inner join reservation_time as t 
                on r.time_id = t.id 
                inner join theme as th 
                on r.theme_id = th.id 
                inner join member as m 
                on r.member_id = m.id 
                order by r.id;
                """;
        return namedJdbcTemplate.query(sql, ReservationRowMapper.INSTANCE);
    }

    @Override
    public Reservation save(Reservation reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into reservation (date, time_id, theme_id, member_id) values (:date, :timeId, :themeId, :memberId)";

        SqlParameterSource params = new BeanPropertySqlParameterSource(reservation);

        namedJdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return Reservation.of(id,
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme(),
                reservation.getMember()
        );
    }

    @Override
    public void deleteById(long id) {
        String sql = "delete from reservation where id = :id";
        SqlParameterSource params = new MapSqlParameterSource("id", id);
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
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("date", date)
                .addValue("themeId", themeId);

        return namedJdbcTemplate.query(sql, params, (rs, rowNum) -> rs.getLong("time_id"));
    }

    @Override
    public boolean existsByDateAndTimeAndTheme(LocalDate date,
                                               ReservationTime time,
                                               Theme theme,
                                               Member member) {
        String sql = "select count(*) from reservation where date = :date and time_id = :timeId and theme_id = :themeId and member_id = :memberId";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("date", date)
                .addValue("timeId", time.getId())
                .addValue("themeId", theme.getId())
                .addValue("memberId", member.getId());
        Integer count = namedJdbcTemplate.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }
}
