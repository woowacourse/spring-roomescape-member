package roomescape.reservation.repositoy;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.auth.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

@Repository
public class H2ReservationRepository implements ReservationRepository {

  private final NamedParameterJdbcTemplate template;

  public H2ReservationRepository(final NamedParameterJdbcTemplate template) {
    this.template = template;
  }

  @Override
  public List<Reservation> findAll(LocalDate dateFrom, LocalDate dateTo, Long themeId,
      Long memberId) {
    StringBuilder sqlBuilder = new StringBuilder("""
        SELECT 
        r.id as reservation_id, r.date as reservation_date,
        m.id as member_id, m.name as member_name, m.email as member_email, m.password as member_password, 
        rt.id as time_id, rt.start_at as reservation_time, 
        th.id as theme_id, th.name as theme_name, th.description as theme_description, th.thumbnail as theme_thumbnail,
        ro.name as role_name
        FROM reservation as r 
        inner join member as m 
        on r.member_id = m.id
        inner join reservation_time as rt 
        on r.time_id = rt.id 
        inner join theme as th 
        on r.theme_id = th.id
        inner join role as ro
        on m.role_id = ro.id
        """);

    sqlBuilder.append("WHERE 1=1 ");
    final MapSqlParameterSource param = new MapSqlParameterSource();

    if (dateFrom != null && dateTo != null) {
      sqlBuilder.append("AND r.date BETWEEN :dateFrom AND :dateTo ");
      param.addValue("dateFrom", dateFrom);
      param.addValue("dateTo", dateTo);
    } else if (dateFrom != null && dateTo == null) {
      sqlBuilder.append("AND r.date >= :dateFrom ");
      param.addValue("dateFrom", dateFrom);
    } else if (dateFrom == null && dateTo != null) {
      sqlBuilder.append("AND r.date <= :dateTo ");
      param.addValue("dateTo", dateTo);
    }

    if (themeId != null) {
      sqlBuilder.append("AND th.id = :themeId ");
      param.addValue("themeId", themeId);
    }

    if (memberId != null) {
      sqlBuilder.append("AND m.id = :memberId ");
      param.addValue("memberId", memberId);
    }

    String sql = sqlBuilder.toString();

    // 쿼리 실행 및 결과 반환
    return template.query(sql, param, itemRowMapper());
  }

  private RowMapper<Reservation> itemRowMapper() {
    return ((rs, rowNum) -> Reservation.createInstance(
        rs.getLong("reservation_id"),
        Member.createInstance(
            rs.getLong("member_id"),
            rs.getString("member_name"),
            rs.getString("member_email"),
            rs.getString("member_password"),
            rs.getString("role_name")
        ),
        rs.getDate("reservation_date").toLocalDate(),
        new ReservationTime(rs.getLong("time_id"), rs.getTime("reservation_time").toLocalTime()),
        Theme.of(
            rs.getLong("theme_id"),
            rs.getString("theme_name"),
            rs.getString("theme_description"),
            rs.getString("theme_thumbnail"))
    ));
  }

  @Override
  public Optional<Reservation> findById(final Long reservationId) {
    final String sql = """
        SELECT 
        r.id as reservation_id, r.date as reservation_date, 
        m.id as member_id, m.name as member_name, m.email as member_email, m.password as member_password, 
        rt.id as time_id, rt.start_at as reservation_time, 
        th.id as theme_id, th.name as theme_name, th.description as theme_description, th.thumbnail as theme_thumbnail,
        ro.name as role_name
        FROM reservation as r 
        inner join member as m 
        on r.member_id = m.id 
        inner join reservation_time as rt 
        on r.time_id = rt.id 
        inner join theme as th 
        on r.theme_id = th.id 
        inner join role as ro
        on m.role_id = ro.id
        WHERE r.id = :reservationId
        """;

    try {
      final MapSqlParameterSource param = new MapSqlParameterSource()
          .addValue("reservationId", reservationId);
      final Reservation reservation = template.queryForObject(sql, param, itemRowMapper());

      return Optional.of(reservation);
    } catch (final EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public Reservation save(final Reservation reservation) {
    final String sql = "INSERT INTO reservation(member_id, date, time_id, theme_id) VALUES (:memberId, :date, :timeId, :themeId)";
    final MapSqlParameterSource param = new MapSqlParameterSource()
        .addValue("memberId", reservation.getMember().getId())
        .addValue("date", reservation.getDate().getValue())
        .addValue("timeId", reservation.getTime().getId())
        .addValue("themeId", reservation.getTheme().getId());
    final KeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(sql, param, keyHolder);

    final long savedReservationId = keyHolder.getKey().longValue();

    return reservation.copyWithId(savedReservationId);
  }

  @Override
  public void deleteById(final Long reservationId) {
    final String sql = "DELETE FROM reservation WHERE id = :id";
    final MapSqlParameterSource param = new MapSqlParameterSource()
        .addValue("id", reservationId);
    template.update(sql, param);
  }

  @Override
  public boolean existByDateAndTimeIdAndThemeId(final LocalDate date, final Long timeId,
      final Long themeId) {
    final String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE date = :date AND time_id = :timeId AND :themeId)";
    final MapSqlParameterSource param = new MapSqlParameterSource()
        .addValue("date", date)
        .addValue("timeId", timeId)
        .addValue("themeId", themeId);

    return Boolean.TRUE.equals(template.queryForObject(sql, param, Boolean.class));
  }

  @Override
  public boolean existByTimeId(final Long reservationTimeId) {
    final String sql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE time_id = :timeId)";
    final MapSqlParameterSource param = new MapSqlParameterSource()
        .addValue("timeId", reservationTimeId);

    return Boolean.TRUE.equals(template.queryForObject(sql, param, Boolean.class));
  }

  @Override
  public List<Reservation> findAllByDateAndThemeId(final LocalDate date, final Long themeId) {
    final String sql = """
        SELECT 
        r.id as reservation_id, r.date as reservation_date,
        m.id as member_id, m.name as member_name, m.email as member_email, m.password as member_password, 
        rt.id as time_id, rt.start_at as reservation_time, 
        th.id as theme_id, th.name as theme_name, th.description as theme_description, th.thumbnail as theme_thumbnail,
        ro.name as role_name 
        FROM reservation as r
        inner join member as m 
        on r.member_id = m.id 
        inner join reservation_time as rt 
        on r.time_id = rt.id 
        inner join theme as th 
        on r.theme_id = th.id 
        inner join role as ro
        on m.role_id = ro.id
        WHERE date = :date and theme_id = :themeId
        """;

    final MapSqlParameterSource param = new MapSqlParameterSource()
        .addValue("date", date)
        .addValue("themeId", themeId);

    return template.query(sql, param, itemRowMapper());
  }
}
