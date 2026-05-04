package roomescape.time.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import roomescape.reservation.domain.ReservationTime;

@Repository
public class JdbcTimeRepository implements TimeRepository {
  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert timeInsert;

  public JdbcTimeRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    this.timeInsert = new SimpleJdbcInsert(jdbcTemplate)
        .withTableName("reservation_time")
        .usingGeneratedKeyColumns("id");
  }

  @Override
  public List<ReservationTime> findAll() {
    return jdbcTemplate.query(
        "SELECT id, start_at FROM reservation_time ORDER BY id",
        new ReservationTimeRowMapper()
    );
  }

  @Override
  public ReservationTime save(String startAt) {
    Number id = timeInsert.executeAndReturnKey(
        new MapSqlParameterSource()
            .addValue("start_at", startAt)
    );

    long timeId = id.longValue();
    return new ReservationTime(timeId, startAt);
  }

  @Override
  public Optional<ReservationTime> findById(long id) {
    List<ReservationTime> results = jdbcTemplate.query(
        "SELECT id, start_at FROM reservation_time WHERE id = ?",
        new ReservationTimeRowMapper(),
        id
    );
    return results.stream().findFirst();
  }

  @Override
  public boolean deleteById(long id) {
    int affectedRows = jdbcTemplate.update(
        "DELETE FROM reservation_time WHERE id = ?",
        id
    );
    return affectedRows > 0;
  }

  private static class ReservationTimeRowMapper implements RowMapper<ReservationTime> {
    @Override
    public ReservationTime mapRow(ResultSet rs, int rowNum) throws SQLException {
      return new ReservationTime(
          rs.getLong("id"),
          rs.getString("start_at")
      );
    }
  }
}

