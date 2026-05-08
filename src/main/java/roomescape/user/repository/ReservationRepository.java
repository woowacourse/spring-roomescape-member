package roomescape.user.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.user.domain.Reservation;
import roomescape.user.domain.ReservationTime;
import roomescape.admin.domain.Theme;

@Repository
public class ReservationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final RowMapper<Reservation> rowMapper = (resultSet, rowNum) -> Reservation.of(
            resultSet.getLong("reservation_id"),
            resultSet.getString("name"),
            resultSet.getDate("date").toLocalDate(),
            ReservationTime.of(
                    resultSet.getLong("time_id"),
                    resultSet.getTime("time_start_at").toLocalTime(),
                    resultSet.getTime("time_finish_at").toLocalTime()
            ),
            Theme.of(
                    resultSet.getLong("theme_id"),
                    resultSet.getString("theme_name"),
                    resultSet.getString("theme_description"),
                    resultSet.getString("theme_image_url")
            )
    );

    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public Reservation save(Reservation reservation) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", reservation.getName())
                .addValue("date", reservation.getDate())
                .addValue("time_id", reservation.getTime().getId())
                .addValue("theme_id", reservation.getTheme().getId());
        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return Reservation.of(id, reservation.getName(), reservation.getDate(), reservation.getTime(), reservation.getTheme());
    }

    public void deleteById(Long id) {
        String query = "delete from reservation where id = ?";
        jdbcTemplate.update(query, id);
    }

}
